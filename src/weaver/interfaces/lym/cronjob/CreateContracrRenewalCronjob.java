package weaver.interfaces.lym.cronjob;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.CalendarMethods;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.schedule.BaseCronJob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据合同结束日期到期前$days$天自动创建合同续签流程
 */
public class CreateContracrRenewalCronjob extends BaseCronJob {

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getWorkflowid() {
        return workflowid;
    }

    public void setWorkflowid(String workflowid) {
        this.workflowid = workflowid;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    private String creator = "";//创建人
    private String workflowid = "";//流程ID
    private String days = "";//提前的天数

    public void execute() {
        RecordSet rs = new RecordSet();
        String currentDate = CalendarMethods.getCurrentDate();
        String targetDate = CalendarMethods.dateOperation(CalendarMethods.getCurrentDate(),Util.getIntValue(getDays(),45),3);//五个月之前

        //所有入职为五个月之前
        //rs.execute("select * from hrmresource where status in (0,1,2,3) and enddate='"+targetDate+"'");
        rs.execute("select * from uf_ldht where sfzxht=0 and htzzrq='"+targetDate+"'");
        while(rs.next()){
            Map<String, String> mainTableInfo = new HashMap<>();
            String uid = Util.null2String(rs.getString("xm"));//人员ID
            new BaseBean().writeLog("uid:"+uid);
            mainTableInfo.put("xqygxmnameofstaff",uid);
            
            //标题
            String requestName = "合同续签 Contract Renewal-"+getLastName(uid)+"-"+currentDate ;
            //生成字段数据
            mainTableInfo.put("applier", getCreator());//申请人
            mainTableInfo.put("applicationdate",currentDate);
            mainTableInfo.put("bmjzbdeptsubfunction",getDepartment(uid));//部门及组别
            WorkflowToolMethods.creatRequest(getCreator(),workflowid,requestName,"1",mainTableInfo,new HashMap<String, List<Map<String, String>>>());
        }
    }

    private String getDepartment(String  uid){
        RecordSet rs = new RecordSet();
        rs.execute("select departmentid from hrmresource where id="+uid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }

    private String getLastName(String uid){
        RecordSet rs = new RecordSet();
        rs.execute("select lastname from hrmresource where id="+uid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }
}
