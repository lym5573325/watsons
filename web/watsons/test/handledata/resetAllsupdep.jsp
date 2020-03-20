<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.hrm.company.DepartmentComInfo" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    BaseBean bb = new BaseBean();
    RecordSet rs = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    bb.writeLog("option111:"+option);
    if(option.equals("temp")){
        bb.writeLog("进入option->temp");
        rs.execute("select * from hrmdepartment where allsupdepid = '0'");
        int line =0;
        while(rs.next()){
            line++;
            bb.writeLog("第:"+line+"行");
            String supdepid = Util.null2String(rs.getString("supdepid"));
            try {
                String allSupdepid = DepartmentComInfo.getAllParentDepartId(supdepid, supdepid);
                if(allSupdepid.length()>0) allSupdepid=","+allSupdepid+",";
                RecordSet rs2 = new RecordSet();
                rs2.execute("update hrmdepartment set allsupdepid='"+allSupdepid+"' where id="+Util.null2String(rs.getString("id")));
            }catch (Exception e){}
        }
    }else if(option.equals("temp2")){
        bb.writeLog("进入option->temp2");
        int line =0;
        rs.execute("select supdepid,id from hrmdepartment");
        bb.writeLog("5555555555555555");
        while(rs.next()){
            bb.writeLog("66666666");
            line++;
            bb.writeLog("第:"+line+"行");
            String supdepid = Util.null2String(rs.getString("supdepid"));
            try {
                String allSupdepid = DepartmentComInfo.getAllParentDepartId(supdepid, supdepid);
                if(allSupdepid.length()>0) allSupdepid=","+allSupdepid+",";
                RecordSet rs2 = new RecordSet();
                rs2.execute("update hrmdepartment set allsupdepid='"+allSupdepid+"' where id="+Util.null2String(rs.getString("id")));
            }catch (Exception e){}
        }
    }
    out.print("resetAllsupdep完成");
%>