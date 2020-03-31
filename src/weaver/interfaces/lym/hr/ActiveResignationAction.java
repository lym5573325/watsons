package weaver.interfaces.lym.hr;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

public class ActiveResignationAction extends BaseBean implements Action {

    @Override
    public String execute(RequestInfo request) {
        RecordSet rs = new RecordSet();

        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID
        String formtable = "formtable_main_"+(request.getRequestManager().getFormid()*-1);

        new BaseBean().writeLog("主动离职(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        String lastWorkingDate = "";
        int hcid = Util.getIntValue(mainTableInfo.get(""));//HC的ID

        if(hcid>0) {
            boolean isExistMr = isExistMr(hcid);//是否存在MR单
            if (isExistMr) {//存在MR单
                boolean isBeforeLastWorking = true;//判断是否在LastWorkingDay+1天前
                if (isBeforeLastWorking) {//如果在lastWorkingDay+1天前
                    /*   recuriter_status=pengding，HC Now Satus=pending   */
                    rs.executeUpdate("update " + HcMode.tableName + "  set recruitmentstatus=?,hcnowstatus=? where id=?", "3", "2", hcid + "");
                } else {//
                    /*   recuriter_status不更新，HC Now Satus=hiring   清空name */
                    rs.executeUpdate("udpate " + HcMode.tableName + " set hcnowstatus=?,name=null where id=?","1");
                }
            } else {//不存在MR单
                /*   两个状态都更新为pending，更新leave=name，leaving time=last working data，name清空   */
                rs.executeUpdate("update " + HcMode.tableName + " set recruitmentstatus=?,hcnowstatus=?,leavers=name,leavingdate=? where id=?",
                        "3", "2", lastWorkingDate, hcid+"");
                rs.execute("update " + HcMode.tableName + " set name=null where id="+hcid);
            }
        }
        return Action.SUCCESS;
    }

    public boolean isExistMr(int hcid){
        RecordSet rs = new RecordSet();
        return true;
    }


}
