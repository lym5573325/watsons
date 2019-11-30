package com.engine.sync.util;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;

public class OrgUtil {

    private static final String table = "hrmdepartment";

    /**
     * 根据组织编码查找部门ID
     * @param var1
     * @return
     */
    public static int getOrgidByCode(String var1){
        RecordSet rs = new RecordSet();
        rs.execute("select id from " + table + " where departmentcode = '"+ var1 +"'");
        if(rs.next())   return Util.getIntValue(rs.getString("id"));
        return 0;
    }


}
