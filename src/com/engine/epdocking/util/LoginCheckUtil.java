package com.engine.epdocking.util;

import Edge.DES.DES;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.HttpServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCheckUtil {

    BaseBean bb = new BaseBean();

    private final String url = bb.getPropValue("epinfo","logincheckaddress");

    public String getError_msc() {
        return error_msc;
    }

    private String error_msc;


    public String getLoginSession(String loginid,String password){
        bb.writeLog("getLoginSession");
        JSONObject info = loginCheckRequest(loginid, password);
        bb.writeLog("info："+info.toString());
        String loginSession = "";
        if(info != null){
            try{
                if(info.getString("error_code").equals("00")){
                    loginSession = info.getString("login_session");
                }else{
                    this.error_msc = info.getString("error_msg");
                    new BaseBean().writeLog("登录失败:"+info.getString("error_msg"));
                }
            }catch(JSONException e){
                new BaseBean().writeLog("登录异常:"+e.toString());
                e.printStackTrace();
            }
        }
        return loginSession;
    }

    public String getLoginSession(HttpServletRequest request,String loginid, String password){
        HttpSession httpSession = request.getSession();
        String loginsession = Util.null2String(httpSession.getAttribute("ep_loginsession"));
        bb.writeLog("loginsession:"+loginsession);
        if(loginsession.length()>0) return loginsession;
        else {
            loginsession = getLoginSession(loginid, password);
            bb.writeLog("loginsession2:"+loginsession);
            if(loginsession.length()>0) httpSession.setAttribute("ep_loginsession",loginsession);
            bb.writeLog("loginsession3:"+Util.null2String(httpSession.getAttribute("ep_loginsession")));
        }
        return loginsession;
    }

    /**
     * 登录验证请求
     * @param loginid   登录账号
     * @param password  登录密码
     * @return
     */
    private JSONObject  loginCheckRequest(String loginid, String password){
        bb.writeLog("loginCheckRequest");
        JSONObject responseInfo = null;
        BaseBean bb = new BaseBean();

        bb.writeLog("账号:"+loginid);
        bb.writeLog("密码:"+password);
        TokenInfoUtil tokenInfoUtil = new TokenInfoUtil();
        //获取token信息
        String token = tokenInfoUtil.getToken();
        String encrykey = tokenInfoUtil.getEncrykey();
        bb.writeLog("token:"+token+"     encrykey:"+encrykey);

        if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(encrykey)){//请求TOKEN成功
            bb.writeLog("开始请求登录验证");
            try {
                JSONObject requestInfo = new JSONObject();
                //加密账号密码
                String loginid_en = DES.encrypt(loginid, encrykey);
                String password_en = DES.encrypt(password, encrykey);
                bb.writeLog("加密后的账号:"+loginid_en);
                bb.writeLog("加密后的密码:"+password_en);

                requestInfo.put("token",token);
                requestInfo.put("empno",loginid_en);
                requestInfo.put("password",password_en);
                requestInfo.put("lang","zh-CN");
                bb.writeLog("requestInfo"+requestInfo.toString());
                //响应结果
                responseInfo = new JSONObject(HttpServiceUtil.sendPost(this.url,requestInfo.toString(),false));
                bb.writeLog("responseInfo"+responseInfo.toString());

            }catch (Exception e){
                bb.writeLog("异常");
                e.printStackTrace();
            }
        }

        return responseInfo;
    }

}
