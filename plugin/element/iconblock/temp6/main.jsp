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
	out.print("jsonNames:"+jsonNames.toString());
	out.print("loginSession:"+request.getSession(true).getAttribute("ep_loginsession"));
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
				
			</div>
		</div>
		<script type="text/javascript" src="/plugin/element/js/main/main.js"></script>
		<script type="text/javascript">
			$(function(){
				var jsonNames = <%=jsonNames%>;
				//获取数据
				//$.GET("/api/epdocking/epSystemList/getSystemList",jsonNames,function(data){
				jQuery.ajax({
					url : "/api/epdocking/epSystemList/getSystemList",
					type : "get",
					processData : false,
					data : "",
					dataType : "json",
					success : function do4Success(data){
						//document.writeln("<br><br>data:"+data);
						document.writeln("获取数据成功");
						//document.writeln("data.systemList2.error_code:"+data.systemList2);
						//document.writeln("<br><br>:"+JSON.parse(data.systemList2).error_msg);
						data = JSON.parse(data.systemList2);
						//if(data && data.status == "0"){
						if(data && data.error_code == "00"){
							document.writeln("请求成功:"+data.data);
							var datas = data.data;
							//var colnum = Number(data.colnum);
							var colnum = "8";
							var width = (100 / colnum);
							var url = "";
							var cursor = "auto";
							
							datas.map(function(elt, i) {
								document.writeln("<br><br>elt:"+elt.url);
								if(elt.url && elt.url!=""){
									url = elt.url;
									cursor = "pointer";
								}else{
									 url = "";
									 cursor = "auto";
								}
								var item = "<div class='block-item' title='"+elt.sys_cn_name+"' url='"+url+"' style='width:"+width+"%;cursor:"+cursor+"'> "+
									"<div class='block-main'>"+
									"	<img alt='' class='block-item-icon' "+
									"		src='"+elt.logourl+"'> "+
									"	<div class='block-item-content'> "+
									"		<div class='block-item-title'> "+
									"			<a href=\"javascript:void(0);\" style=\"cursor:"+cursor+"\"  title='查看"+elt.sys_cn_name+"'>"+elt.sys_cn_name+"</a> "+
									"		</div> "+
									"	</div> "+
									"</div>"+
									"</div> ";
								$(".containerdiv").append(item);	
							});
						}
					}
				});
			});
			
			$(".block-item").live("click",function(event){
				var url = $(this).attr("url");
				if(url && url!=""){
					window.open(url);
				}
				event.stopPropagation();
				event.preventDefault();
			})
			
			var linenum = 1;
			function getLine(colnum){
				var line = "<div class='liney'></div>";
				if(linenum++ == colnum){
					linenum = 1;
					line = "";
				}
				return line;
			}
		</script>
	</body>
</html>