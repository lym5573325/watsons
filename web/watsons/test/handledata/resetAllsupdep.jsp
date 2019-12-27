<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.hrm.company.DepartmentComInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    RecordSet rs = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    if(option.equals("temp")){
        rs.execute("select * from hrmdepartment where allsupdepid = '0'");
        while(rs.next()){
            String supdepid = Util.null2String(rs.getString("supdepid"));
            try {
                String allSupdepid = DepartmentComInfo.getAllParentDepartId(supdepid, supdepid);
                if(allSupdepid.length()>0) allSupdepid=","+allSupdepid+",";
                RecordSet rs2 = new RecordSet();
                rs2.execute("update hrmdepartment set allsupdepid='"+allSupdepid+"' where id="+Util.null2String(rs.getString("id")));
            }catch (Exception e){}
        }
    }
%>