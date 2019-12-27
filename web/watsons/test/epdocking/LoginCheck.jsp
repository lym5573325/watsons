<%@ page import="com.engine.epdocking.util.LoginCheckUtil" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.interfaces.lym.util.HttpServiceUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String loginid = Util.null2String(request.getParameter("loginid"));
    String password = Util.null2String(request.getParameter("password"));
    String loginSession = new LoginCheckUtil().getLoginSession(request,"41027105","123@abc#");
    if(loginSession.length()>0){
        out.print("<br>登录成功:"+loginSession);
        out.print("<br>LoginSession" + request.getSession().getAttribute("ep_loginsession"));
        String sysList = HttpServiceUtil.sendPost("http://127.0.0.1:8091/api/epdocking/epSystemLIst/getSystemList","",false);
        out.print("<br>:"+sysList);
    }else{
        out.print("登录失败");
    }

%>
