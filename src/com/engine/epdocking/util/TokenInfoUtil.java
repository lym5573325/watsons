package com.engine.epdocking.util;

import org.json.JSONException;
import org.json.JSONObject;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.CalendarMethods;
import weaver.interfaces.lym.util.HttpServiceUtil;

import java.util.Date;

public class TokenInfoUtil {

    BaseBean bb = new BaseBean ();
    private final String appid = bb.getPropValue("epinfo","appid");
    private final String password = bb.getPropValue("epinfo","password");
    private final String url = bb.getPropValue("epinfo","tokenaddress");

    private static String token;
    private static String encrykey;

    public String getToken(){
        try {
            JSONObject temp = getInfo();
            if(temp!=null)
                return temp.getString("token");
        }catch(JSONException e){
            bb.writeLog("getToken()==>异常");
        }
        return "";
    }

    public String getEncrykey(){
        try {
            JSONObject temp = getInfo();
            if(temp!=null)
                return temp.getString("encrykey");
        }catch(JSONException e){
            //bb.writeLog("getEncrykey()==>异常");
            System.out.println("getEncrykey()==>异常");
        }
        return "";
    }

    private static JSONObject info = null;

    public JSONObject getInfo(){
        if(isValid()){
            return info;
        }else{
            info = send();
        }

        if(isValid())   return info;
        else return null;
    }

    private  boolean isValid(){
        bb.writeLog("isValid");
        try {
            if (info != null){
                bb.writeLog("info222:"+info.toString());
                if(info.getString("error_code").equals("00") &&
                    CalendarMethods.dateTimeFormat2.parse(info.getString("endTime")).getTime() > new Date().getTime())
                return true;
            }

        }catch(Exception e){
            bb.writeLog("token异常:"+e.toString());
        }
        bb.writeLog("token无效");
        return false;
    }

    private JSONObject send(){
        bb.writeLog("发送请求获取token");
        System.out.println("发送请求获取token");
        JSONObject requestInfo = new JSONObject();
        JSONObject responseInfo = new JSONObject();
        try {
            requestInfo.put("appid", this.appid);
            requestInfo.put("password", this.password);
            //发送请求
            bb.writeLog("请求:"+requestInfo,toString());
            bb.writeLog("请求地址："+this.url);
            String response = HttpServiceUtil.sendPost(this.url,requestInfo.toString(),false);
            bb.writeLog("响应:"+response);
            responseInfo = new JSONObject(response);
            //计算到期时间
            if(responseInfo.getString("error_code").equals("00")){//请求成功
                responseInfo.put("endTime",CalendarMethods.dateTimeFormat2.format(new Date().getTime()+Util.getIntValue(responseInfo.getString("expiretime"),0)*1000));
            }
            bb.writeLog("响应2:"+responseInfo.toString());
        }catch(JSONException e){
            bb.writeLog("异常:"+e.toString());
            responseInfo = null;
        }finally {
            info = responseInfo;
            bb.writeLog("info:"+info.toString());
        }
        return responseInfo;
    }

    public static void main(String[] args){
        new TokenInfoUtil().getToken();
    }
}
