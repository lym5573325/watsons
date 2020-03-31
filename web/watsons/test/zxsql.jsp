<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.engine.common.service.impl.HrmCommonServiceImpl" %>
<%@ page import="weaver.hrm.job.JobTitlesComInfo" %>
<%@ page import="weaver.matrix.MatrixUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    RecordSet rs = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    /*
    rs.execute("update hrmdepartmentdefined set mktdepthead=mkthead where mktdepthead='' or mktdepthead is null");
    rs.execute("update matrixtable_2 set mktdepthead=mkthead where mktdepthead='' or mktdepthead is null");

    rs.execute("update hrmdepartmentdefined set regiondepthead=regiongm where regiondepthead='' or regiondepthead is null");
    rs.execute("update matrixtable_2 set regiondepthead=regiongm where regiondepthead='' or regiondepthead is null");

    rs.execute("update hrmdepartmentdefined set mkter=regioner where mkter='' or mkter is null");
    rs.execute("update matrixtable_2 set mkter=regioner where mkter='' or mkter is null");

     */
    rs.execute("update uf_hc set subfunctionforhrms=(select departmentid from hrmresource where hrmresource.workcode=uf_hc.empcode) where subfunctionforhrms is  null");
    rs.execute("update uf_hc set departmentfororacle=(select departmentid from hrmresource where hrmresource.workcode=uf_hc.empcode) where departmentfororacle is  null");
    rs.execute("update uf_hc set title=(select jobtitle from hrmresource where hrmresource.workcode=uf_hc.empcode) where jobtitle is  null");
    rs.execute("update uf_hc set name=(select id from hrmresource where hrmresource.workcode=uf_hc.empcode)");

%>

