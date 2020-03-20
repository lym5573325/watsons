<%@page import="weaver.conn.RecordSet"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/page/maint/common/initNoCache.jsp"%>
<%@page import="weaver.general.*"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<%@ include file="/plugin/element/util/Util.jsp"%>
<%
	out.clear();
	String key =  Util.null2String(request.getParameter("key"));
	String folder = "iconblock";
	JSONObject json = new JSONObject();
	JSONObject jsonconf = this.getJSONObject(folder,key);
	JSONArray arrs = jsonconf.getJSONArray("list"); 
	json.put("datas", formatHrefforList(arrs,user,request));
	json.put("colnum", jsonconf.getString("colnum"));
	json.put("status", "0");
	out.write(json.toString());
%>