<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="weaver.general.Util" %>
<%@ page import="java.util.Enumeration" %>
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
						//document.writeln("获取数据成功");
						//document.writeln("data.systemList2.error_code:"+data.systemList2);
						//document.writeln("<br><br>:"+JSON.parse(data.systemList2).error_msg);
						data = JSON.parse(data.systemList);
						//if(data && data.status == "0"){
						if(data && data.error_code == "00"){
							//document.writeln("请求成功:"+data.data);
							var datas = data.data;
							//var colnum = Number(data.colnum);
							var colnum = "6";
							var width = (100 / colnum);
							var url = "";
							var cursor = "auto";
							
							datas.map(function(elt, i) {
								if(elt.sysclassify=="2"){
									if(elt.url && elt.url!=""){
										url = elt.url;
										cursor = "pointer";
									}else{
										 url = "";
										 cursor = "auto";
									}
									/*
									document.writeln("<br><br>sys_cn_name:"+elt.sys_cn_name);
									document.writeln("<br><br>width:"+width);
									document.writeln("<br><br>url:"+elt.url);
									document.writeln("<br><br>cursor:"+cursor);
									document.writeln("<br><br>url:"+elt.logourl);
									*/
									var item = "<div class='block-item' title='"+elt.sys_cn_name+"' url='"+url+"' style='width:"+width+"%;cursor:"+cursor+"' runkind='"+elt.runkind+"' otherexename='"+elt.otherexename+"' browsertype='"+elt.browsertype+"'> "+
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
										//document.writeln("<br><br>item:"+item);
									$(".containerdiv").append(item);	
								}
							});
						}
					}
				});
			});
			
			$(".block-item").live("click",function(event){
				var url = $(this).attr("url");
				var runkind = $(this).attr("runkind");
				var otherexename = $(this).attr("otherexename");
				var browsertype = $(this).attr("browsertype");
				if(url && url!=""){
					//原浏览器
					if(runkind=="1"){
						window.open(url);
					}else{//第三方应用
						Run_New(otherexename,url)
						
					}
					
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
			
			function Run_New(exe,url){
				try {
					var objShell = new ActiveXObject('wscript.shell');
					//objShell.Run(exe + " " + url);
					objShell.Run(exe + " " + url);
					objShell = null;
				} catch (e) {
					//alert('找不到指定文件' + objPath + '(或它的組件之一)，請確定文件或路徑是否正確，并確定更改了你的IE安全級別：Internet選項-->安全-->自定義級別-->對沒有標記為安全的ActiveX控件進行初始化和腳本運行-->啟用!');
					//alert("打开异常，请确认你是否已安装了" + objName + "，并确认了你的IE安全级别：Internet选项-->安全-->自定义级别-->对没有标志为安全的ActiveX控件进行初始化和脚本运行-->启用！");
					alert("打开异常，请联系IT Supporter");
				}

				return false;
			}
		</script>
	</body>
</html>