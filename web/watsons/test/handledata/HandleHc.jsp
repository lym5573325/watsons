<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.conn.RecordSetDataSource" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String option = Util.null2String(request.getParameter("option"));
    RecordSet rs = new RecordSet();
    RecordSet rs2 = new RecordSet();
    rs.execute("select * from uf_hc order by id asc");
    while(rs.next()){
        String hcid = Util.null2String(rs.getString("id"));
        String name = Util.null2String(rs.getString("name"));
        if(!"".equals(name)){
            String deptid = getDepartmentByUid(name);
            String subfunctionforhrms = getDepartment_level(deptid,"6");
            String departmentfororacle = getDepartment_level(deptid,"5");
            String title = getTitle(name);

            if("".equals(subfunctionforhrms) || "".equals(departmentfororacle)) out.print("==========");
            out.print("<br>hcid:"+hcid+"   deptid:"+deptid+"    subfunctionforhrms:"+subfunctionforhrms+"    departmentfororacle:"+departmentfororacle+"   title："+title);
            if(option.equals("execute"))  rs2.executeUpdate("update uf_hc set subfunctionforhrms=?,departmentfororacle=?,title=? where id="+hcid,subfunctionforhrms,departmentfororacle,title);
        }
    }

%>
<%!
    public String getDepartmentByUid(String uid){
        RecordSet rs = new RecordSet();
        rs.execute("select departmentid from hrmresource where id="+uid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }

    public String getDepartment_level(String deptid,String tlevel){
        RecordSet rs = new RecordSet();
        rs.execute("select departmentname,id from hrmdepartment   where ( instr((select TO_CHAR(allsupdepid)  from hrmdepartment where id='"+deptid+"'),','||id||',')>0 or id='"+deptid+"' ) and tlevel="+tlevel);
        return rs.next()? Util.null2String(rs.getString("id")) : deptid;
    }

    public String getTitle(String uid){
        RecordSet rs = new RecordSet();
        rs.execute("select jobtitle from hrmresource where id="+uid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }
%>

