package weaver.interfaces.lym.hr;

import org.apache.commons.lang.StringUtils;
import weaver.common.DateUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class StaffAddChangeApplyAction extends BaseBean implements Action {

    public String getDtNum() {
        return dtNum;
    }

    public void setDtNum(String dtNum) {
        this.dtNum = dtNum;
    }

    private String dtNum = "";

    @Override
    public String execute(RequestInfo request) {
        RecordSet rs = new RecordSet();
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID

        new BaseBean().writeLog("人员增补变更申请(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        List<Map<String, String>> dtInfo = WorkflowToolMethods.getDetailTableInfo(request,Util.getIntValue(dtNum,1));

        for(int i=0;i<dtInfo.size();i++){
            Map<String, String> row = dtInfo.get(i);
            int hcBillid = Util.getIntValue(row.get("bzdmhccode"));//编制代码 HC CODE
            String mrreceiveddate = row.get("mrcxkfrqmrreopendate");//MR重新开放日期 MR Reopen Date
            if(hcBillid>0){
                int bgzt = Util.getIntValue(row.get("ztbgstatechanges"));
                writeLog("bgzt:"+bgzt);
                int recuritementStatus = transBgzt(bgzt);//转换成对应hc状态
                writeLog("recuritementStatus:"+recuritementStatus);
                if(bgzt!=-1) {
                    int hcnowstatus = HcMode.getHcNowStatusByRecuitmentStatus(recuritementStatus);

                    /*拼接SQL*/
                    String sql = "";
                    sql += "update " + HcMode.tableName + "  set  hcnowstatus='"+  hcnowstatus +"',recruitmentstatus='"+recuritementStatus+"' ";
                    //选择Reopen==>更新mrreceiveddate和TargetLeadTime
                    if(bgzt==1){
                        sql += ",mrreceiveddate='"+  mrreceiveddate +"' ";
                        float targetLeadTime = Util.getFloatValue(HcMode.getTargetLeadTime(hcBillid));//获取targetLeadTime (in mth)
                        writeLog("targetLeadTime:"+targetLeadTime);
                        if(targetLeadTime>0){
                            sql += ",targetleadtimeindate='"+HcMode.getTargetTime(mrreceiveddate,targetLeadTime)+"' ";//计算出的targetLeadTime(in date)
                        }
                    }
                    sql += " where id="+hcBillid;
                    writeLog("SQL:"+sql);
                    rs.execute(sql);
                }
            }
        }

        return Action.SUCCESS;
    }



    /*
    public String execute2(RequestInfo request) {
        RecordSet rs = new RecordSet();
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID

        new BaseBean().writeLog("人员增补变更申请(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        //MR Reopen Date    (mrreceiveddate)
        String mrreceiveddate = mainTableInfo.get("mrreceiveddate");
        //变更状态 bgzt
        int bgzt = transBgztUtil.getIntValue(mainTableInfo.get("bgzt"),-1);
        //int hcnowstatus = transBgzt(bgzt);
        int hcnowstatus = HcMode.getHcNowStatusByRecuitmentStatus(bgzt);

        //编制代码 HC CODE  uf_hc.id
        String hcids = mainTableInfo.get("hccode");
        String[] hcidArray = hcids.split(",");
        for(String hcid : hcidArray){
            String sql = "";
            sql += "update " + HcMode.tableName + "  set  hcnowstatus='"+  hcnowstatus +"'";

            //选择Reopen==>更新
            if(hcnowstatus==1) sql += ",mrreceiveddate='"+  mrreceiveddate +"' ";

            sql += " where id="+hcid;
            writeLog("SQL:"+sql);
            rs.execute(sql);

            if(hcnowstatus==1)  HcMode.updateTargetTimeById(Util.getIntValue(hcid));

        }
        return Action.SUCCESS;
    }

     */


    /**
     * 变更状态转换
     * @param bgzt  流程变更状态
     * @return  对应的HC台账HC Now Status（hcnowstatus）
     */
    private int transBgzt(int bgzt){
        if(bgzt==0) return 3;
        else if(bgzt==1) return 0;
        else if(bgzt==2) return 4;
        else if(bgzt==3) return 5;
        else return -1;

    }
}
