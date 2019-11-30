package com.engine.sync.cmd.organizationHrms;

import com.engine.sync.util.OrgUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;

public class UpdateOrganizationHrmsCmd {

    //private static final String updateSql = "";
    public boolean updateOrgByCode(String departmentcode, String departmentmark,String departmentname,String supdepid,int subcompanyid1,int tlevel,int creater,String created,int modifier,String modified){
        RecordSet rs = new RecordSet();
        int depid = 0;
        if(departmentcode.length()>0 && (depid = OrgUtil.getOrgidByCode(departmentcode))>0) {
            new BaseBean().writeLog("更新部门 id:"+depid + "code:"+departmentcode);
            return rs.executeUpdate("update hrmdepartment set departmentname=?,departmentmark=?,supdepid=?,subcompanyid1=?,tlevel=?,creater=?,created=?,modifier=?,modified=? where id = "+depid);
        }else return false;
    }

}
