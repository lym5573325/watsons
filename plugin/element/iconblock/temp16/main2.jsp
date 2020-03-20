<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="org.json.JSONObject"%>
<%@ include file="/systeminfo/init_wev8.jsp"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<%
	String key = Util.null2String(request.getParameter("key"));
	Enumeration pNames=request.getParameterNames();
	JSONObject jsonNames = new JSONObject();
	while(pNames.hasMoreElements()){
	    String name=(String)pNames.nextElement();
	    String value=request.getParameter(name);
	    jsonNames.put(name, value);
	}
%>
<html>
	<head>
		<link rel="stylesheet" href="/plugin/element/css/main.css" type="text/css" />
		<link rel="stylesheet" href="css/base.css" type="text/css" />
		<link rel="stylesheet" href="css/<%=key %>.css" type="text/css" />
		<style type="text/css">
		</style>
	</head>
	<body>
	<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
	<%
	
	%>
	<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
		<%
		%>
		<div class="maindiv">
			<%
			if(key.length() == 0){
				%>
				<div class="errormsg">key不能为空！</div>
				<%
				return;
			}
			%>
			<div class="containerdiv">
				<div class="wea-temp16-container" id="wea_temp16" >
				
				</div>
			</div>
		</div>
		<script type="text/javascript" src="/plugin/element/js/main/main.js"></script>
		<script type="text/javascript">
			// 发送请求加载配置项
			$(function(){
				var jsonNames = <%=jsonNames%>;
				//获取数据
				$.post("Operation.jsp",jsonNames,function(data){
					if(data && data.status == "0"){
						var datas = data.datas;
						var tag = "";
						datas.map(function(item, i) {
								var url = (item.href && item.href!="")? item.href :"javascript:void(0)";
								var cursor = (item.href && item.href!="")?"pointer":"auto";
							    tag += "<a class=\"wea-temp16-box wea-temp-shodow\" id=" + item.id + " href=" + url + " target=\"_blank\" style=\"background:linear-gradient("+item.linearDirection+", "+item.linearStart +", "+item.linearEnd+");cursor:"+cursor+" \"  >";
								tag +=    "<div class=\"wea-temp16-box-left-custom\">";
								tag += "<p class=\"wea-temp16-box-title-custom\">" + item.title + "</p>";
								if(item.text && item.text != ""){
									tag +=      "<p class=\"wea-temp16-box-text\">" + item.text + "</p>";
								}
								tag +=    "</div>";
								tag +=    "<div class=\"wea-temp16-box-right\">";
								tag +=      "<img class=\"wea-temp16-box-icon\" src=\"" + item.icon + "\" />";
								tag +=    "</div>";
								tag +=  "</a>";
						})
						$("#wea_temp16").append(tag);
					}
				},"json");
			});
		</script>
	</body>
</html>