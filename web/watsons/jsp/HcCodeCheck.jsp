<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.interfaces.lym.formmode.HcMode" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.json.JSONObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    JSONObject result = new JSONObject();
    RecordSet rs = new RecordSet();
    String option = Util.null2String(request.getParameter("option")).toLowerCase();
    String hcid = Util.null2String(request.getParameter("hcid"));
    String department = Util.null2String(request.getParameter("department"));
    if(option.equals("staffaddapply")){//人员增补申请
        rs.execute("select id from " + HcMode.tableName + " where id = '"+hcid + "' and departmentfororacle ='"+department+"'" +
                " and (recruitmentstatus in (3,4,5) or (recruitmentstatus =2 and (leavers is not null or name is null)))");
        if(rs.next())   result.put("result",true);
        else result.put("result",false);
    }
    out.clear();
    out.print(result);


%>