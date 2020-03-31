<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="org.json.JSONObject"%>
<%@ include file="/systeminfo/init_wev8.jsp"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<%
	String key = Util.null2String(request.getParameter("key"));
	
	int uid = user.getUID();
	int todoNum = 0;
	String dbsql = "select (case requestlevel " +
                "          when 0 then 0 " +
                "          when 1 then 1 " +
                "          when 2 then 2 " +
                "          else -1 end) as requestlevelorder, " +
                "       t1.requestid, " +
                "       t1.requestmark, " +
                "       t1.createdate, " +
                "       t1.createtime, " +
                "       t1.creater, " +
                "       t1.creatertype, " +
                "       t1.workflowid, " +
                "       t1.requestname, " +
                "       t1.requestnamenew, " +
                "       t1.status, " +
                "       t1.requestlevel, " +
                "       t1.currentnodeid, " +
                "       t2.viewtype, " +
                "       t2.userid, " +
                "       t2.receivedate, " +
                "       t2.receivetime, " +
                "       t2.isremark, " +
                "       t2.nodeid, " +
                "       t2.agentorbyagentid, " +
                "       t2.agenttype, " +
                "       t2.isprocessed, " +
                "       t1.seclevel, " +
                "       '0'             as systype, " +
                "       t2.workflowtype, " +
                "       t2.viewDate, " +
                "       t2.viewTime, " +
                "       t1.lastFeedBackDate, " +
                "       t1.lastFeedBackTime, " +
                "       t2.needwfback, " +
                "       t1.lastFeedBackOperator " +
                "from workflow_requestbase t1, " +
                "     workflow_currentoperator t2, " +
                "     workflow_base t3 " +
                "where t1.requestid = t2.requestid " +
                "  and t1.workflowid = t3.id " +
                "  and t2.userid in ("+uid+") " +
                "  and t2.usertype = 0 " +
                "  and (isnull(t1.currentstatus, -1) = -1 or (isnull(t1.currentstatus, -1) = 0 and t1.creater in ("+uid+"))) " +
                "  and (t1.deleted <> 1 or t1.deleted is null or t1.deleted = '') " +
                "  and ((t2.isremark = '0' and (t2.takisremark is null or t2.takisremark = 0)) or " +
                "       t2.isremark in ('1', '5', '8', '9', '7', '11')) " +
                "  and t2.islasttimes = 1 " +
                "  and (isprocessing = '' or isprocessing is null) " +
                "  and t3.isvalid in (1, 3) " +
                "order by t1.createdate desc, t1.createtime desc, receivedate desc, receivetime desc";
	if(rs.next()){
		todoNum = rs.getCounts();
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
				<div style="height:0px;"></div>
			</div>
		</div>
		<script type="text/javascript" src="/plugin/element/js/main/main.js"></script>
		<script type="text/javascript">
			$(function(){
				var colnum = 1;
				var width = (100 / colnum);
				var url = "";
				cursor = "pointer";
					
					//var item = "<div class='block-item' title='待办数量:<%=todoNum%>' url='/spa/workflow/static/index.html?e9timestamp=1578398788713#/main/workflow/listDoing?menuldus=1,13qe' style='width:"+width+"%;height:100%;cursor:"+cursor+"'> "+
					var item = "<div class='block-item' title='待办数量:<%=todoNum%>' url='/wui/index.html#/main/workflow/listDoing?menuldus=1,13qe' style='width:"+width+"%;height:130px;cursor:"+cursor+"'> "+
						"<div style='margin-top: 15px'>"+
						"	<div style='float:left;display:inlin;margin-left: 20%;'><img alt='' class='' width='80px' "+
						"		src='/plugin/element/uploadImg/tdwf.png'> </div>"+
						"	<div class='block-item-content' style='display:inline;float:left;margin-left: 15px;'> "+
						"		<div class='block-item-title' style='font-size:35px'> "+
						"			<a href=\"javascript:void(0);\" style=\"cursor:"+cursor+"\"  title='查看待办'><%=todoNum%></a> "+
						"		</div> "+
						"		<div class='block-item-title' style='font-size:16px'> "+
						"			<a href=\"javascript:void(0);\" style=\"cursor:"+cursor+"\"  title='查看待办'>待办数量</a> "+
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