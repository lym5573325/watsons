package weaver.formmode.customjavacode.modeexpand;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 * @author Administrator
 *
 */
public class CreateInternalApplication extends AbstractModeExpandJavaCodeNew {
    /**
     * 执行模块扩展动作
     * @param param
     *  param包含(但不限于)以下数据
     *  user 当前用户
     *  importtype 导入方式(仅在批量导入的接口动作会传输) 1 追加，2覆盖,3更新，获取方式(int)param.get("importtype")
     *  导入链接中拼接的特殊参数(仅在批量导入的接口动作会传输)，比如a=1，可通过param.get("a")获取参数值
     * @return
     */
    public Map<String, String> doModeExpand(Map<String, Object> param) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            User user = (User)param.get("user");
            int billid = -1;//数据id
            int modeid = -1;//模块id
            RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
            if(requestInfo!=null){
                billid = Util.getIntValue(requestInfo.getRequestid());
                modeid = Util.getIntValue(requestInfo.getWorkflowid());
                if(billid>0&&modeid>0){
                    new BaseBean().writeLog("生成内部招聘流程Action！ billid:"+billid);
                    /**
                     * 创建流程
                     */
                    RecordSet rs = new RecordSet();
                    //rs.execute("select id from uf_nbzptz where id="+billid+" and NVL(sfyscnbzplc,0)=0");
                    //if(rs.next()) {

                        String applicationdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//当前日期
                        int workflowid = 1021;

                        //主表信息
                        Map<String, String> mainTableInfo = new HashMap<>();
                        mainTableInfo.put("nbyp", billid + "");//内部硬盘
                        mainTableInfo.put("applier",user.getUID()+"");//申请人
                        mainTableInfo.put("applicationdate",applicationdate);//申请日期
                        //标题
                        String bt = getWorkflowName(workflowid)+"-"+user.getLastname()+"-"+applicationdate;

                        //生成流程
                        String requestid = WorkflowToolMethods.creatRequest(user.getUID()+"",workflowid+"",bt,"0",mainTableInfo,new HashMap<String, List<Map<String, String>>>());
                        new BaseBean().writeLog("requestid:"+requestid);
                        if(Util.getIntValue(requestid)>0) {
                            rs.execute("update uf_nbzptz set sfyscnbzplc=1 where id=" + billid);
                        }else{
                            result.put("errmsg","流程生成失败!");
                            result.put("flag", "false");
                        }
                    //}else{
                        //result.put("errmsg","已生成过内部招聘流程");
                        //result.put("flag", "false");
                    //}

                }
            }
        } catch (Exception e) {
            result.put("errmsg","自定义出错信息");
            result.put("flag", "false");
        }
        return result;
    }

    private String getWorkflowName(int workflowid){
        RecordSet rs = new RecordSet();
        rs.execute("select workflowname from workflow_base where id="+workflowid);
        rs.next();
        return Util.null2String(rs.getString("workflowname"));
    }

}