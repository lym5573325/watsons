package com.engine.sync.cmd.organizationHrms;

import weaver.conn.RecordSet;

public class AddOrganizationHrmsCmd {

    private static final String addSql = " insert into hrmdepartment (departmentcode,departmentmark,departmentname,supdepid,subcompanyid1,tlevel,creater,created,modifier,modified) " +
            " values (?,?,?,?,?,?,?,?,?)";

    /**
     * 新增部门
     * @param departmentmark    部门简称
     * @param departmentname    部门名称
     * @param supdepid  上级部门ID
     * @param subcompanyid1 所属分部
     * @param tlevel    部门层级
     * @param creater   创建人
     * @param created   创建时间
     * @param modifier  最后修改人
     * @param modified  最后修改时间
     * @return
     */
    public boolean addOrg(String departmentcode,String departmentmark,String departmentname,String supdepid,int subcompanyid1,int tlevel,int creater,String created,int modifier,String modified){
        return  new RecordSet().executeUpdate(addSql,departmentcode,departmentmark,departmentname,supdepid,subcompanyid1+"",tlevel+"",creater+"",created,modifier+"",modified);
    }


}
