<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.hrm.company.SubCompanyComInfo" %>
<%@ page import="weaver.hrm.company.DepartmentComInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    RecordSet rs = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    if(option.equals("temp")){
        SubCompanyComInfo subCompanyComInfo = new SubCompanyComInfo();
        subCompanyComInfo.removeCompanyCache();
        DepartmentComInfo departmentComInfo = new DepartmentComInfo();
        departmentComInfo.removeCompanyCache();

        rs.execute("update hrmdepartment set subcompanyid1=1 ");
        rs.execute("select * from hrmsubcompany order by id asc");
        while(rs.next()){
            out.print("<br>  id:"+Util.null2String(rs.getString("id"))+"    name:"+Util.null2String(rs.getString("subcompanyname")));
        }
        out.print("<br><br>===========部门=============");
        rs.execute("select * from hrmdepartment order by id asc");
        while(rs.next()){
            out.print("<br>  id:"+Util.null2String(rs.getString("id"))+"    name:"+Util.null2String(rs.getString("departmentname")) +"  上级部门:"+Util.null2String(rs.getString("supdepid"))+"   分部:"+Util.null2String(rs.getString("subcompanyid1")));
        }
    }
%>