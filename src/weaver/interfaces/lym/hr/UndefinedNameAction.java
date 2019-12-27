package weaver.interfaces.lym.hr;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * 根据日期带出“社保结算值”、“公积金结算至”字段
 * 1.如果最后工作日期是本月15日及之前，则日期算上个月最后一天
 * 2.如果最后工作日期是本月16日及之后，择日期算本月最后一天
 */
public class UndefinedNameAction extends BaseBean implements Action {
    //判断日期
    private String field1;
    //社保结算至日期
    private String field2;
    //公积金结算至日期
    private String field3;

    @Override
    public String execute(RequestInfo request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID
        String formTable = "formtable_main_"+(request.getRequestManager().getFormid()*-1);

        new BaseBean().writeLog("获取社保公积金结算日期 Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        //最后工作日
        String lastWorkDate = mainTableInfo.get(field1);
        Calendar cal = Calendar.getInstance();

        writeLog("lastWrokDate:"+lastWorkDate);
        if(lastWorkDate.length()==10){
            try {
                cal.setTime(dateFormat.parse(lastWorkDate));
                if(Util.getIntValue(lastWorkDate.substring(8, 10), 0) < 15) cal.set(Calendar.DAY_OF_MONTH,0);
                else cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String date = dateFormat.format(cal.getTime());
                writeLog("date:"+date);
                RecordSet rs = new RecordSet();
                rs.executeUpdate("update " + formTable + " set " + field2 + "=?,"+ field3 +"=? where requestid=?",date, date, requestid);
            }catch (Exception e){}
        }else{
            return "0";
        }

        return Action.SUCCESS;
    }

    public static void main(String[] args){

    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }
}
