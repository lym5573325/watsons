<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="org.json.JSONObject"%>
<%@ include file="/systeminfo/init_wev8.jsp"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<%
	String key = Util.null2String(request.getParameter("key"));
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
				<div style="height:25px;"></div>
			</div>
		</div>
		<script type="text/javascript" src="/plugin/element/js/main/main.js"></script>
		<script type="text/javascript">
			$(function(){
				var colnum = 1;
				var width = (100 / colnum);
				var url = "";
				cursor = "pointer";
					
					var item = "<div class='block-item' title='新建流程' url='/spa/workflow/static/index.html?e9timestamp=1578398788713#/main/workflow/add?menulds=1,12' style='width:"+width+"%;height:100%;cursor:"+cursor+"'> "+
					    "<div class='block-main2'>"+
						"	<img alt='' class='block-item-icon2' "+
						"		src='/plugin/element/uploadImg/59.png'> "+
						"	<div class='block-item-content'> "+
						"		<div class='block-item-title'> "+
						"			<a href=\"javascript:void(0);\" style=\"cursor:"+cursor+"\"  title='新建流程'>新建流程</a> "+
						"		</div> "+
						"	</div> "+
						"</div>"+
						"</div> ";
					$(".containerdiv").append(item);
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