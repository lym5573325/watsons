package weaver.interfaces.lym.hr;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

public class StaffAddChangeApplyAction extends BaseBean implements Action {

    @Override
    public String execute(RequestInfo request) {
        RecordSet rs = new RecordSet();
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID

        new BaseBean().writeLog("人员增补变更申请(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        //MR Reopen Date    (mrreceiveddate)
        String mrreceiveddate = mainTableInfo.get("mrreceiveddate");
        //变更状态 bgzt
        int bgzt = Util.getIntValue(mainTableInfo.get("bgzt"),-1);
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


    /**
     * 变更状态转换
     * @param bgzt  流程变更状态
     * @return  对应的HC台账HC Now Status（hcnowstatus）
     */
    private int transBgzt(int bgzt){
        if(bgzt==0) return 2;
        else if(bgzt==1) return 1;
        else if(bgzt==2) return 3;
        else if(bgzt==3) return 4;
        else return -1;

    }
}
