<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String result = "";
    RecordSet rs = new RecordSet();
    String uid = Util.null2String(request.getParameter("uid"));
    rs.execute("select * from hrmdepartment where id = (select departmentid from hrmresource where id=" + uid + ")");
    if(rs.next()){
        if(Util.getIntValue(rs.getString("tlevel"),0) >5 && Util.null2String(rs.getString("allsupdepid"))){
            rs.execute("select id,departmentname from hrmdepartment where tlevel=5 and NVL(canceled,0)=0 and id in (0"+Util.null2String(rs.getString("allsupdepid"))+"0)");
            if(rs.next())   result = Util.null2String(rs.getString("id"))+","+Util.null2String(rs.getString("departmentname"));
        }else{
            result = Util.null2String(rs.getString("id"))+","+Util.null2String(rs.getString("departmentname"));
        }
    }

    out.print(result);
%>