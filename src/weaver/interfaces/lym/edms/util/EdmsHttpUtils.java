package weaver.interfaces.lym.edms.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import weaver.general.BaseBean;

import java.io.File;
import java.nio.charset.Charset;

public class EdmsHttpUtils {

    BaseBean bb = new BaseBean();

    private CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    private String loginName = bb.getPropValue("edmsinfo","loginname");//"41139486";
    private String password = bb.getPropValue("edmsinfo","password");//"vitaldoc";

    private String ipport = bb.getPropValue("edmsaddress","httpservice");//"http://10.82.24.32";
    private String loginUrl = ipport+bb.getPropValue("edmsaddress","login");////"/WebAPI/api/main/Login";
    private String loadDirectoryUrl = ipport+bb.getPropValue("edmsaddress","loaddir");//"/WebAPI/api/main/LoadDir";
    private String loadDocumentTypeUrl = ipport+bb.getPropValue("edmsaddress","loaddocumenttype");//"/WebAPI/api/main/LoadDocType";
    private String loadDocuemntUrl = ipport+bb.getPropValue("edmsaddress","loaddocument");//"/WebAPI/api/main/LoadDocByDirID";
    private String createDocumentUrl = ipport+bb.getPropValue("edmsaddress","createdocument");//"/WebAPI/api/main/CreateDocument";
    private String updateDocumentUrl = ipport+bb.getPropValue("edmsaddress","updatedocument");//"/WebAPI/api/main/UpdateDocument";
    private String logoutUrl = ipport+bb.getPropValue("edmsaddress","logout");

    /**
     * 登录   账号默认loginName   password
     * @return
     */
    public boolean login(){
        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.addHeader("content-type", "application/json;charset=utf-8");
        httpPost.addHeader("accept", "application/json");
        JSONObject reqInfo = new JSONObject();
        JSONObject resInfo = new JSONObject();
        try {
            reqInfo.put("LoginName", loginName);
            reqInfo.put("Password", password);
            httpPost.setEntity(new StringEntity(reqInfo.toString(), Charset.forName("utf-8")));
            resInfo = new JSONObject(EntityUtils.toString(httpClient.execute(httpPost).getEntity()));
            System.out.println("Login Success  ID:"+resInfo.getString("ID")+"    Fullname："+resInfo.getString("Fullname"));
            return true;
        } catch (Exception e) {
            System.out.println("Exception Login Fail");
            try{
                System.out.println("Login Fail    "+resInfo.getString("Message"));
            }catch(Exception e2){}
            e.printStackTrace();
        }
        return false;
    }

    private String dirID;

    /**
     * 加载文件夹
     * @param empID 员工ID
     * @return
     */
    public String loadDirectory(String empID){
        HttpGet httpGet = new HttpGet(loadDirectoryUrl+"?empID="+empID);
        JSONObject resInfo;
        try{
            String resString = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
            System.out.println("resString:"+resString);
            if(!resString.equals("null")) {
                resInfo = new JSONObject(resString);
                dirID = resInfo.getString("ID");
                System.out.println("LoadDirectory Success    ID:" + dirID);
            }else{
                System.out.println("loadDirectory Fail    Response:"+resString);
            }
        }catch (Exception e){
            System.out.println("loadDirectory Fail");
            dirID = null;
            e.printStackTrace();
        }
        return dirID;
    }

    private String docTypeID;//文档类型ID
    private JSONObject docTypeJson;//请求文档类型的response
    /**
     * 加载文件类型
     * @param docTypeName   文件类型名称，参考文档
     * @return
     */
    public String loadDocumentType(String docTypeName){
        String resString = "";
        HttpGet httpGet = new HttpGet(loadDocumentTypeUrl+"?docTypeName="+docTypeName);
        JSONObject resInfo;
        try{
            resString = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
            System.out.println("resString:"+resString);
            if(!resString.equals("null")) {
                docTypeJson = new JSONObject(resString);
                System.out.println("loadDocumentType:" + docTypeJson.toString());
                docTypeID = docTypeJson.getString("ID");
                System.out.println("loadDocumentType Success    ID:" + docTypeID);
            }else{
                System.out.println("loadDocumentType Fail   Response:"+resString);
            }
        }catch (Exception e){
            System.out.println("loadDocumentType Fail    ErrorInfo:"+resString);
            docTypeID = null;
            docTypeJson = null;
            e.printStackTrace();
        }
        return docTypeID;
    }

    private String docID;//文档ID

    /**
     * 加载文档
     * @return  文档的ID，如果!=null则为文档存在=》更新 ，反之创建
     */
    public String loadDocument(){
        String resString = "";
        if(dirID!=null && docTypeID!=null) {
            HttpGet httpGet = new HttpGet(loadDocuemntUrl + "/" + dirID + "?docTypeID=" + docTypeID);
            JSONObject resInfo;
            try {
                resString = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
                if(!resString.equals("null")) {
                    resInfo = new JSONObject(resString);
                    docID = resInfo.getString("ID");
                    System.out.println("loadDocument Success    ID:" + docID);
                }else{
                    System.out.println("loadDocument Fail   Response:"+resString);
                }
            } catch (Exception e) {
                System.out.println("loadDocument Fail   ErrorInfo:"+resString);
                docID = null;
                e.printStackTrace();
            }
        }else{
            docID=null;
        }
        return docID;
    }

    /**
     * 创建文档
     * @param file  文档File
     * @param staffName 员工姓名
     * @param staffID   员工编号
     * @param idNo  员工身份证
     * @return
     */
    public boolean createDocument(File file,String staffName,String staffID,String idNo){
        JSONObject resInfo = new JSONObject();
        String resString = "";

        HttpPost post = new HttpPost(createDocumentUrl);
        post.addHeader("accept", "application/json");
        FileBody fileBody = new FileBody(file);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.RFC6532);

        try {
            //添加参数
            multipartEntityBuilder.addPart("file",fileBody);
            System.out.println("Name:"+docTypeJson.getString("Name"));
            multipartEntityBuilder.addPart("selDocTypeName",new StringBody(docTypeJson.getString("Name"),Charset.forName("utf-8")));
            multipartEntityBuilder.addPart("parentID",new StringBody(dirID, ContentType.TEXT_PLAIN));
            JSONArray docTypeFields = docTypeJson.getJSONObject("DTFields").getJSONArray("Fields");
            for(int i=0;i<docTypeFields.length();i++){
                JSONObject docTypeField = docTypeFields.getJSONObject(i);
                if (docTypeField.getString("FieldName").equals("Staff ID"))
                    multipartEntityBuilder.addPart("fld_Staff ID" ,new StringBody(staffID, ContentType.TEXT_PLAIN));
                else if (docTypeField.getString("FieldName").equals("Staff Name"))
                    multipartEntityBuilder.addPart("fld_Staff Name" ,new StringBody(staffName, ContentType.TEXT_PLAIN));
                else if (docTypeField.getString("FieldName").equals("ID No"))
                    multipartEntityBuilder.addPart("fld_ID No" ,new StringBody(idNo, ContentType.TEXT_PLAIN));
            }

            HttpEntity reqEntity = multipartEntityBuilder.build();
            post.setEntity(reqEntity);
            resString = EntityUtils.toString(httpClient.execute(post).getEntity());
            System.out.println("resString:"+resString);
            resInfo = new JSONObject(resString);
            if(resString.contains("ID")){
                System.out.println("createDocument Success    ID:"+resInfo.getString("ID"));
                return true;
            }else{
                System.out.println("createDocument Fail    ErrorInfo:"+resString);
            }
        }catch (Exception e){
            System.out.println("createDocument Exception    ErrorInfo:"+resString);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新文档
     * @param file  文档File
     * @return
     */
    public boolean updateDocument(File file){
        JSONObject resInfo = new JSONObject();
        String resString = "";

        HttpPost post = new HttpPost(updateDocumentUrl);
        post.addHeader("accept", "application/json");
        StringBody documentID = new StringBody(docID, ContentType.TEXT_PLAIN);
        FileBody fileBody = new FileBody(file);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.RFC6532);
        HttpEntity reqEntity = multipartEntityBuilder.
                addPart("documentID",documentID).
                addPart("file",fileBody).
                build();
        try {
            post.setEntity(reqEntity);
            resString = EntityUtils.toString(httpClient.execute(post).getEntity());
            resInfo = new JSONObject(resString);
            if(resString.contains("ID")){
                System.out.println("updateDocument Success    ID:"+resInfo.getString("ID"));
                return true;
            }else{
                System.out.println("updateDocument Fail    ErrorInfo:"+resString);
            }
        }catch (Exception e){
            System.out.println("updateDocument Exception    ErrorInfo"+resString);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 登出
     * @return
     */
    public boolean logout(){
        String resString = "";
        HttpGet httpGet = new HttpGet(logoutUrl);
        try {
            resString = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
            if(resString.contains("ok")){
                System.out.println("logout Success    responseInfo:"+resString);
                return true;
            }else{
                System.out.println("logout Fail    responseInfo:"+resString);
            }
        }catch (Exception e){
            System.out.println("logout Fail         CauseBy："+resString);
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendDocument(String empID,String docTypeName,File file,String staffName,String staffID,String idNo){
        boolean success = false;
        if(login() && loadDirectory(empID)!=null && loadDocumentType(docTypeName)!=null){
            if(loadDocument()==null){//Doc don't Exist，Create
                success = createDocument(file,staffName,staffID,idNo);
            }else{//Doc Exist, Update
                success = updateDocument(file);
            }
        }
        logout();
        return success;
    }

}
