<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%


        String ip = request.getHeader("x-forwarded-for");

    out.print("<br>0:"+ip);
    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

        ip = request.getHeader("Proxy-Client-IP");
        out.print("<br>1:"+ip);
    }

    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

        ip = request.getHeader("WL-Proxy-Client-IP");
        out.print("<br>2:"+ip);
    }

    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

        ip = request.getRemoteAddr();
    }

%>
<%!

%>
%>
