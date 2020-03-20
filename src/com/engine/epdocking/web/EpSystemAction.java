package com.engine.epdocking.web;

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.epdocking.service.EpSystemService;
import com.engine.epdocking.service.impl.EpSystemServiceImpl;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class EpSystemAction {

    private EpSystemService getService(User user){
        return (EpSystemService) ServiceUtil.getService(EpSystemServiceImpl.class, user);
    }

    @GET
    @Path("/getSystemList")
    @Produces({MediaType.TEXT_PLAIN})
    public String getSystemList(@Context HttpServletRequest request, @Context HttpServletResponse response){

        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {

            Map<String, Object> params = ParamUtil.request2Map(request);
            params.put("ipaddress2",getIp(request));
            params.put("ep_loginsession",request.getSession().getAttribute("ep_loginsession"));
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(getService(user).getSystemList(params));
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    private String getIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
