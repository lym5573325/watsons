package com.engine.epdocking.cmd.epSystem;

import Edge.DES.DES;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.epdocking.util.TokenInfoUtil;
import org.json.JSONObject;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.lym.util.HttpServiceUtil;

import java.util.HashMap;
import java.util.Map;

public class GetSystemList extends AbstractCommonCommand<Map<String, Object>> {

    public GetSystemList(User user, Map<String,Object> params) {
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        BaseBean bb = new BaseBean();
        bb.writeLog("GetSystemList");
        //地址
        String url = bb.getPropValue("epinfo","getsessionaddress");
        bb.writeLog("地址:"+url);

        //定义返回数据
        Map<String, Object> apidatas = new HashMap<String, Object>();

        if (null == user) {
            apidatas.put("hasRight", false);
            apidatas.put("errorMsg", "请登录OA账号！");
            return apidatas;
        }

        TokenInfoUtil tokenInfoUtil = new TokenInfoUtil();
        String ip = Util.null2String(params.get("ipaddress2"));
        String token = tokenInfoUtil.getToken();
        String encrykey = tokenInfoUtil.getEncrykey();
        String loginid = user.getLoginid();
        //String loginid = "41027105";
        String loginSession = Util.null2String(params.get("ep_loginsession"));
        bb.writeLog("ip:"+ip);
        bb.writeLog("token:"+token);
        bb.writeLog("encrykey:"+encrykey);
        bb.writeLog("loginid:"+loginid);
        bb.writeLog("loginSession:"+loginSession);

        if(token.length()==0 || encrykey.length()==0){
            apidatas.put("hasRight", false);
            apidatas.put("errorMsg", "获取TOKEN失败");
            return apidatas;
        }else if(loginSession.length()==0){
            apidatas.put("hasRight", false);
            apidatas.put("errorMsg", "获取Session失败!");
            return apidatas;
        }

        JSONObject requestInfo = new JSONObject();
        JSONObject responseInfo = null;

        try{
            String loginid_en = DES.encrypt(loginid,encrykey);
            String ip_en = DES.encrypt(ip,encrykey);
            bb.writeLog("(加密后)loginid:"+loginid_en);
            bb.writeLog("（加密后）ip:"+ip_en);


            requestInfo.put("token",token);
            requestInfo.put("empno",loginid_en);
            requestInfo.put("user_ip",ip_en);
            requestInfo.put("login_session",loginSession);
            requestInfo.put("lang","zh-CN");
            bb.writeLog("请求信息:"+requestInfo.toString());
            responseInfo = new JSONObject(HttpServiceUtil.sendPost(url,requestInfo.toString(),false));
            bb.writeLog("响应信息:"+responseInfo);

        }catch(Exception e){
            bb.writeLog("异常:"+toString());
        }finally {
            bb.writeLog("finally:"+responseInfo.toString());
            try {
                apidatas.put("systemList", responseInfo.toString());
                bb.writeLog("apidatas："+apidatas.toString());
            }catch(Exception e){}
        }

        return apidatas;
    }



}
