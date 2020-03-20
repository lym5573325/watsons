<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    RecordSet rs = new RecordSet();
    RecordSet rs2 = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    if(option.equals("bm")){
        rs.execute("select * from hrmdepartment where departmentname like '%·%'");
        int line=0;
        while(rs.next()){
            line++;
            new BaseBean().writeLog("第:"+line+"行   id:"+Util.null2String(rs.getString("id")));
            String departmentname  = Util.null2String(rs.getString("departmentname")).replaceAll("·",".");
            String departmentmark = Util.null2String(rs.getString("departmentmark")).replaceAll("·",".");
            rs2.executeUpdate("update hrmdepartment set departmentname=?,departmentmark=? where id=?",departmentname,departmentmark, Util.null2String(rs.getString("id")));
        }
        out.print("更新部门特殊符号·完成");
    }else if(option.equals("bm2")){
        rs.execute("select * from hrmdepartment where departmentname like '%/%'");
        int line=0;
        while(rs.next()){
            line++;
            new BaseBean().writeLog("第:"+line+"行   id:"+Util.null2String(rs.getString("id")));
            String departmentname  = Util.null2String(rs.getString("departmentname")).replaceAll("/",".");
            String departmentmark = Util.null2String(rs.getString("departmentmark")).replaceAll("/",".");
            rs2.executeUpdate("update hrmdepartment set departmentname=?,departmentmark=? where id=?",departmentname,departmentmark, Util.null2String(rs.getString("id")));
        }
        out.print("更新部门特殊符号/完成");
    }
%>