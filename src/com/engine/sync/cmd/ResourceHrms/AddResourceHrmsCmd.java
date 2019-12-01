package com.engine.sync.cmd.ResourceHrms;

import com.engine.sync.entity.ResourceHrmsBean;
import weaver.conn.RecordSet;

public class AddResourceHrmsCmd {

    private static final String addSql = "insert into hrmresource (workcode,lastname,managerid,departmentid,subcompanyid,jobtitle,mobile,usekind,companystartdate,statues)" +
            "values (?,?,?,?,?,?,?,?,?,?)";


    //protected boolean execute(String workcode, String lastname, String managerid,String departmentid, String subcompanyid1 ){
    protected boolean execute(ResourceHrmsBean bean){
        RecordSet rs = new RecordSet();
        if(rs.executeUpdate(addSql, bean.getWorkcode(),bean.getLastname(),bean.getManagerid(),bean.getDepartmentid(),
                bean.getSubcompanyid1(),bean.getJobtitle(),bean.getPhone(),bean.getUsekind(),bean.getCompanystartdate(),bean.getStatus())
        ) cusData(bean);
        return false;
    }

    /**
     * 新增人员自定义属性(cus_fielddata)
     * @return
     */
    private boolean cusData(ResourceHrmsBean bean){

        return true;
    }
}
