<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.hrm.company.SubCompanyComInfo" %>
<%@ page import="weaver.hrm.company.DepartmentComInfo" %>
<%@ page import="weaver.hrm.resource.ResourceComInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    RecordSet rs = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    if(option.equals("temp")){
        RecordSet rs2 = new RecordSet();
        rs.execute("select id from hrmresource order by id asc");
        while(rs.next()){
            String uid = Util.null2String(rs.getString("id"));
            rs2.execute("select seqorder from cus_fielddata where id = "+uid+" order by id desc");
            if(rs2.next()){
                String seqorder = Util.null2String(rs2.getString("seqorder"));
                rs2.execute("delete from cus_fielddata where seqorder!='" + seqorder + "' and scopeid=3  and id="+uid);
            }

        }

        ResourceComInfo resourceComInfo = new ResourceComInfo();
        resourceComInfo.removeResourceCache();

    }
%>