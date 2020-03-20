package weaver.interfaces.lym.cronjob;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.lym.util.CalendarMethods;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.schedule.BaseCronJob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateProbationConfirmationCronjob extends BaseCronJob {

    public String getWorkflowid1() {
        return workflowid1;
    }

    public void setWorkflowid1(String workflowid1) {
        this.workflowid1 = workflowid1;
    }

    public String getWorkflowid2() {
        return workflowid2;
    }

    public void setWorkflowid2(String workflowid2) {
        this.workflowid2 = workflowid2;
    }

    public String getMinJobLevel2() {
        return minJobLevel2;
    }

    public void setMinJobLevel2(String minJobLevel2) {
        this.minJobLevel2 = minJobLevel2;
    }

    private String workflowid1 = "1023";//试用期转正流程<=14

    private String workflowid2 = "2021";//试用期转正流程>=15
    private String minJobLevel2 = "15";

    public void execute() {
        RecordSet rs = new RecordSet();
        String currentDate = CalendarMethods.getCurrentDate();
        String fiveMonthAgo = CalendarMethods.dateOperation(CalendarMethods.getCurrentDate(),-5,4);//五个月之前

        //所有入职为五个月之前
        rs.execute("select * from hrmresource where status in (0,1,2,3) and companystartdate is not null and companystartdate='"+fiveMonthAgo+"' and NVL(joblevel,0)>0 order by id asc");
        while(rs.next()){
            Map<String, String> mainTableInfo = new HashMap<>();
            String workflowid = "";
            String uid = Util.null2String(rs.getString("id"));//人员ID
            int joblevel = Util.getIntValue("joblevel");//职级

            //获取创建的流程对应的workflowid
            if(joblevel>=Util.getIntValue(minJobLevel2,15)) workflowid = workflowid2;
            else workflowid = workflowid1;

            //标题
            String requestName = getWorkflowName(workflowid) + "-" + getLastName(uid) + "-" + currentDate ;
            //生成字段数据
            mainTableInfo.put("applier", uid);//申请人
            mainTableInfo.put("applicationdate",currentDate);
            WorkflowToolMethods.creatRequest(uid,workflowid,requestName,"0",mainTableInfo,new HashMap<String, List<Map<String, String>>>());
        }
    }

    private String getWorkflowName(String workflowid){
        RecordSet rs = new RecordSet();
        rs.execute("select workflowname from workflow_base where id="+workflowid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }

    private String getLastName(String uid){
        RecordSet rs = new RecordSet();
        rs.execute("select lastname from hrmresource where id="+uid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }
}
