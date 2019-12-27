<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    RecordSet rs = new RecordSet();
    RecordSet rs2 = new RecordSet();
    rs.execute("select field1 from position_20191220");
    int line=0;
    while(rs.next()){
        line++;
        String field1 = Util.null2String(rs.getString("field1"));
        String[] temp = field1.split("\\|");
        out.print("<br>第"+line+"行");
        if(temp.length==2){
            rs2.execute("update hrmjobtitles set jobtitleremark='"+temp[1]+"' where jobtitlemark='"+temp[0]+"'");
        }else{
            out.print("失败");
        }
    }
%>