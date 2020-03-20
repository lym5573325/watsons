<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    RecordSet rs = new RecordSet();
    RecordSet rs2 = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    rs.execute("select * from hrmresource order by id asc");
    int line = 0 ;
    while(rs.next()){
        line++;
        String cer = Util.null2String(rs.getString("certificatenum"));
        if(cer.length()>=17){
            cer = cer.substring(0,6) + "********" + cer.substring(14);
            if(line%1000==0)    new BaseBean().writeLog("第"+line+"行:   ID："+rs.getString("id")+"   身份证:"+cer);
            rs2.execute("update hrmresource set certificatenum='"+cer + "' where id="+Util.null2String(rs.getString("id")));
        }
    }
%>