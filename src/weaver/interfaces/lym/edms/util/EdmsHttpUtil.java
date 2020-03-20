package weaver.interfaces.lym.edms.util;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.HttpServiceUtil;

import java.io.InputStream;
import java.net.URLEncoder;

/**
 * EMDS的登录功能
 */
public class EdmsHttpUtil {
    BaseBean bb = new BaseBean();


    public JSONObject login(){
        return login(null,bb.getPropValue("edmsinfo","loginname"),bb.getPropValue("edmsinfo","password"));
    }

    /**
     * 验证登录
     * @param loginName
     * @param password
     * @return
     */
    public JSONObject login(HttpClient client,String loginName, String password){
        //String url = bb.getPropValue("edmsaddress","httpservice")+bb.getPropValue("edmsaddress","login");
        String url = "http://10.82.24.32/WebAPI/api/main/Login";

        return new JSONObject();
    }

    public JSONObject loadDirectory(String empid){
        String url = bb.getPropValue("edmsaddress","httpservice")+bb.getPropValue("edmsaddress","loaddir");
        JSONObject response;
        try {
            String param = "empID="+ URLEncoder.encode(empid,"utf-8");
            String responseStr = HttpServiceUtil.sendGet(url, param);
            bb.writeLog("loadDirectory响应:"+responseStr);
            response = new JSONObject(responseStr);
        }catch(Exception e){
            response = null;
        }
        return  response;
    }

    public JSONObject loadDocumentType(String docTypeName){
        String url = bb.getPropValue("edmsaddress","httpservice")+bb.getPropValue("edmsaddress","loaddocumenttype");
        JSONObject response;
        try {
            String param = "docTypeName="+ URLEncoder.encode(docTypeName,"utf-8");
            String responseStr = HttpServiceUtil.sendGet(url, param);
            bb.writeLog("loadDocumentType响应:"+responseStr);
            response = new JSONObject(responseStr);
        }catch(Exception e){
            response = null;
        }
        return  response;
    }

    public JSONObject loadDocument(int dirID,String docTypeID){
        String url = bb.getPropValue("edmsaddress","httpservice")+bb.getPropValue("edmsaddress","loaddocument")+"/"+dirID;
        JSONObject response;
        try {
            String param = "docTypeID="+ URLEncoder.encode(docTypeID,"utf-8");
            String responseStr = HttpServiceUtil.sendGet(url, param);
            bb.writeLog("loadDocument响应:"+responseStr);
            response = new JSONObject(responseStr);
        }catch(Exception e){
            response = null;
        }
        return  response;
    }

    public JSONObject createDocument(InputStream fileStream,String documentName,String parentId,String fieldName,String fieldContent){
        String url = bb.getPropValue("edmsaddress","httpservice")+bb.getPropValue("edmsaddress","createdocument");
        JSONObject request = new JSONObject();
        JSONObject response;
        try {
            request.put("file",fileStream);
            request.put("selDocTypeName",documentName);
            request.put("parentID",parentId);
            request.put("fld_"+fieldName,fieldContent);
            String responseStr = HttpServiceUtil.sendPost(url, request.toString(),false);
            bb.writeLog("createDocument响应:"+responseStr);
            response = new JSONObject(responseStr);
        }catch(Exception e){
            response = null;
        }
        return  response;
    }


    public boolean logout(){
        String url = bb.getPropValue("edmsaddress","httpservice")+bb.getPropValue("edmsaddress","logout");
        String responseStr = Util.null2String(HttpServiceUtil.sendGet(url,""));
        if(responseStr.indexOf("ok")!=-1) return true;
        else return false;
    }
}
