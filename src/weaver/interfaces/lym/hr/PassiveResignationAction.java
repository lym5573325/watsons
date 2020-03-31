package weaver.interfaces.lym.hr;

import weaver.general.BaseBean;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

public class PassiveResignationAction extends BaseBean implements Action {

    @Override
    public String execute(RequestInfo request) {
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID
        String formtable = "formtable_main_"+(request.getRequestManager().getFormid()*-1);

        new BaseBean().writeLog("被动离职(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);
        return Action.SUCCESS;
    }
}
