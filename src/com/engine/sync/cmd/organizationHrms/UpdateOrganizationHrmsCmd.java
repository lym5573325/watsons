package com.engine.sync.cmd.organizationHrms;

import com.api.integration.Base;
import com.engine.sync.util.LocationUtils;
import com.engine.sync.util.OrgUtil;
import org.docx4j.wml.U;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class UpdateOrganizationHrmsCmd {

    //private static final String updateSql = "";
    public static boolean updateOrgByCode(String departmentcode, String departmentmark,String departmentname,String supdepid,int subcompanyid1,int tlevel,int creater,String created,int modifier,String modified,String allSupdepid){
        RecordSet rs = new RecordSet();
        int depid = 0;
        if(departmentcode.length()>0 && (depid = OrgUtil.getOrgidByCode(departmentcode))>0) {
            new BaseBean().writeLog("更新部门 id:"+depid + "code:"+departmentcode);
            return rs.executeUpdate("update hrmdepartment set departmentname=?,departmentmark=?,supdepid=?,subcompanyid1=?,tlevel=?,modifier=?,modified=sysdate,allSupdepid=? where id = ?",
                    departmentname,departmentmark,supdepid,subcompanyid1+"", tlevel+"",modifier+"", allSupdepid, depid);
        }else return false;
    }

    protected  boolean updateCusData(String departmentcode,String locationCode){
        RecordSet rs = new RecordSet();
        int locationId = LocationUtils.getLocationIdByCode(locationCode);
        int deptid = OrgUtil.getOrgidByCode(departmentcode);
        new BaseBean().writeLog("locationId:"+locationId+"   deptid:"+deptid +"   departmentcode:"+departmentcode);
        if(locationId>0) {
            rs.execute("select id from hrmdepartmentdefined where deptid="+OrgUtil.getOrgidByCode(departmentcode));
            if (rs.next()) {
                new BaseBean().writeLog("更新部门自定义字段");
                return rs.executeUpdate("update hrmdepartmentdefined set location=? where id=?", LocationUtils.getLocationIdByCode(locationCode), Util.null2String(rs.getString("id")));
            } else if(deptid>0){
                new BaseBean().writeLog("新增");
                //return rs.executeUpdate("insert into hrmdepartmentdefined(deptid,location) values (?,?)", deptid, locationId);
            }

        }
        return false;
    }

}
