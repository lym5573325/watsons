<%@ page import="com.engine.common.service.impl.HrmCommonServiceImpl" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //更新拼音
    HrmCommonServiceImpl hcsi = new HrmCommonServiceImpl();
    RecordSet rs = new RecordSet();
    RecordSet rs2 = new RecordSet();
    String option = Util.null2String(request.getParameter("option"));
    if(option.equals("gwReset")) {
        rs.execute("select id,jobtitlename from hrmjobtitles order by id desc");
        while (rs.next()) {
            String pinyin = hcsi.generateQuickSearchStr(Util.null2String(rs.getString("jobtitlename")).toLowerCase().replaceAll(" ", ""));
            rs2.executeUpdate("update hrmjobtitles set ecology_pinyin_search=? where id=?", pinyin, Util.null2String(rs.getString("id")));
        }
        out.print("岗位同步完成");
    }else if(option.equals("bmReset")){
        out.print("部门同步完成");
    }
%>