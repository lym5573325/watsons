package weaver.interfaces.lym.operatoraction;

import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.OperatorAction;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取节点操作者
 * 获取所有上级，直到functionHead后停止
 */
public class getAllManagerEndWithFunctionHEAD implements OperatorAction {

    @Override
    public List<String> execute(RequestInfo request) {
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID

        List<String> operatorList = new ArrayList<>();

        new BaseBean().writeLog("获取所有上级，直到functionHead后停止 OperatorAction!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        //申请人
        String applier = mainTableInfo.get("applier");
        //申请部门
        String applicationdepartment = mainTableInfo.get("applicationdepartment");
        //function head
        String functionHead = getFunctionHead(applicationdepartment);
        /**
         * 获取所有上级,直到functionHead
         */
        String nowManager = applier;
        if(functionHead.length()>0){
            while(nowManager.length()>0 ){
                nowManager = getManagerid(nowManager);
                operatorList.add(nowManager);
                if(checkIn(functionHead,nowManager))    break;
            }
        }

        return operatorList;
    }

    public String getAllManager_HasEnd(String uid,String end){
        String allManager = "";
        String nowManager = uid;
        while(StringUtils.isNotBlank(nowManager = getManagerid(nowManager)) && !checkIn(end,nowManager)){
            allManager = allManager + nowManager + ",";
        }
        return allManager.endsWith(",") ? allManager.substring(0,allManager.length()-1) : allManager;
    }

    private String getManagerid(String  uid){
        RecordSet rs = new RecordSet();
        rs.execute("select managerid from hrmresource where id="+uid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }

    private boolean checkIn(String arrStr,String param){
        String[] arr = arrStr.split(",");
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals(param))    return true;
        }
        return false;
    }

    private String getFunctionHead(String departmentid){
        RecordSet rs = new RecordSet();
        rs.execute("select fuctionhead from matrixtable_2 where id="+departmentid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }

}
