package weaver.interfaces.lym.hr;

import weaver.common.DateUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

/**
 * HC台账:Actual Lead time(in mth)，在offer prosoal流程结束后，更新规则是offer prosoal审批完成时间-MR审批完成时间/30
 */
public class OfferProsoalAction extends BaseBean implements Action  {

    public String getHcField() {
        return hcField;
    }

    public void setHcField(String hcField) {
        this.hcField = hcField;
    }

    private String hcField = "";


    @Override
    public String execute(RequestInfo request) {
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID

        new BaseBean().writeLog("OfferProsoal(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        //HC台账ID
        String[]  hcidArr = mainTableInfo.get(hcField).split(",");
        for(int i=0;i<hcidArr.length;i++) {
            int hcid = Util.getIntValue(hcidArr[i]);
            writeLog("hcid:"+hcid);
            String currentDate = DateUtil.getCurrentDate();
            float actualleadTimeInmth = HcMode.countActualleadTimeInmth(hcid, currentDate);
            writeLog("actualleadTimeInmth:" + actualleadTimeInmth);
            RecordSet rs = new RecordSet();
            rs.executeUpdate("update " + HcMode.tableName + " set  hcnowstatus=?,recruitmentstatus=?,actualleadtimeinmth=?,offerreceiveddate=? where id=?",
                    "1", "1", actualleadTimeInmth, currentDate, hcid);
        }
        /*
        //prosoal审批完成时间
        String spwcrq = mainTableInfo.get("spwcrq");
        //MR审批完成时间
        String mrreceiveddate = HcMode.getMrreceiveddateById(hcid);
        writeLog("hcid:"+hcid+"    spwcrq:"+spwcrq+"    mrreceiveddate:"+mrreceiveddate);
        if(spwcrq.length()>0 && mrreceiveddate.length()>0){
            try {
                float actualleadtimeinmth = ((int) ((CalendarMethods.dateFormat.parse(spwcrq).getTime() - CalendarMethods.dateFormat.parse(mrreceiveddate).getTime()) / (1000 * 60 * 60 * 24))) / 30.0f;
                actualleadtimeinmth = new BigDecimal(actualleadtimeinmth).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                writeLog("actualleadtimeinmth:"+actualleadtimeinmth);
                RecordSet rs = new RecordSet();
                rs.executeUpdate("update " + HcMode.tableName + " set actualleadtimeinmth=?,offerreceiveddate=? where id=?",actualleadtimeinmth+"",spwcrq, hcid+"");
            }catch (Exception e){
                request.getRequestManager().setMessage("10000");
                request.getRequestManager().setMessagecontent("时间格式化异常！ 请联系管理员!");
            }
        }else{
            request.getRequestManager().setMessage("10000");
            request.getRequestManager().setMessagecontent("审批完成时间和MR审批完成时间必填! 请检查表单！");
            return "0";
        }
        */
        return Action.SUCCESS;
    }

}
