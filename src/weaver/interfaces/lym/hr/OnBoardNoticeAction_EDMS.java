package weaver.interfaces.lym.hr;

import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.edms.util.EdmsHttpUtils;
import weaver.interfaces.lym.util.ImageFileUtils;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.io.File;
import java.util.Map;

/**
 * workflow:入职通知
 * description:把附件推送到EDMS
 */
public class OnBoardNoticeAction_EDMS extends BaseBean implements Action {

    public String getDocTypeNames() {
        return docTypeNames;
    }

    public void setDocTypeNames(String docTypeNames) {
        this.docTypeNames = docTypeNames;
    }

    public String getDocFileFields() {
        return docFileFields;
    }

    public void setDocFileFields(String docFileFields) {
        this.docFileFields = docFileFields;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String docTypeNames;
    private String docFileFields;

    public String getStaffNameField() {
        return staffNameField;
    }

    public void setStaffNameField(String staffNameField) {
        this.staffNameField = staffNameField;
    }

    public String getStaffIDField() {
        return staffIDField;
    }

    public void setStaffIDField(String staffIDField) {
        this.staffIDField = staffIDField;
    }

    private String staffNameField;
    private String staffIDField;

    private String path;//文档目录路径    /xxx/xxx/
    @Override
    public String execute(RequestInfo request) {
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID
        String errorMsg = "";

        new BaseBean().writeLog("OnBoardNoticeAction_EDMS Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        EdmsHttpUtils edmsHttpUtils = new EdmsHttpUtils();//EDMS工具类

        //主表信息
        String staffName = mainTableInfo.get(staffNameField);//员工姓名
        String staffID = mainTableInfo.get(staffIDField);//员工编号

        //验证参数docTypeNames和docFileFields的长度
        String[] docTypeNameArray = docTypeNames.split(",");
        String[] docFileFieldArray = docFileFields.split(",");
        if(docTypeNameArray.length>0 && docTypeNameArray.length==docFileFieldArray.length){

            //验证EDMS是否能登录
            if(edmsHttpUtils.login() && edmsHttpUtils.loadDirectory(staffID)!=null) {//登录成功
                writeLog("登录登录、加载员工对应文件夹成功");

                //依次发送文档
                for (int i = 0; i < docTypeNameArray.length; i++) {
                    writeLog("遍历第"+(i+1)+"文档");

                    String docTypeName = docTypeNameArray[i];//文档类型名称
                    int docid = Util.getIntValue(mainTableInfo.get(docFileFieldArray[i]));//附件docid
                    writeLog("docTypeName:"+docTypeName+"    docid:"+docid);
                    if (docTypeName.length() > 0 && docid > 0) {

                        String filePath = path + ImageFileUtils.getFileName(docid + "");//文档目录+附件名称
                        writeLog("文件目录："+filePath);

                        if(filePath.length()>path.length()) {

                            //解密文档，加载
                            ImageFileUtils.downloadFile(docid, filePath);
                            File file = new File(filePath);
                            if (file.exists()) {
                                writeLog("文件加密、加载成功");

                                if (edmsHttpUtils.loadDocumentType(docTypeName) != null) {
                                    writeLog("成功加载文档类型:"+docTypeName);
                                    boolean b = false;
                                    if (edmsHttpUtils.loadDocument() == null) {//Doc don't Exist，Create
                                        writeLog("创建文档");
                                        b = edmsHttpUtils.createDocument(file, staffName, staffID, "123");
                                    } else {//Doc Exist, Update
                                        writeLog("更新文档");
                                        b = edmsHttpUtils.updateDocument(file);
                                    }
                                    if(!b) errorMsg = "发送文档失败!";
                                }else errorMsg = "文件类型load失败!";
                            }else errorMsg = "解密附件保存失败!";
                        }else errorMsg = "获取附件名称失败!";
                    }

                }
            }else errorMsg = "EDMS登录失败!";
        }else errorMsg = "参数docTypeNames和docFileFields配置错误!";

        edmsHttpUtils.logout();
        if(errorMsg.length()>0) {
            request.getRequestManager().setMessage("10000");
            request.getRequestManager().setMessagecontent(errorMsg + " 请联系管理员!");
            return "0";
        }
        return Action.SUCCESS;
    }

}
