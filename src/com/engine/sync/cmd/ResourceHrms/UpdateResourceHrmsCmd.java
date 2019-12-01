package com.engine.sync.cmd.ResourceHrms;

import com.engine.sync.entity.ResourceHrmsBean;
import weaver.conn.RecordSet;

public class UpdateResourceHrmsCmd {

    private static final String sql1 = "update hrmresource set lastname=?,managerid=?,departmentid=?," +
            "subcompanyid=?,jobtitle=?,mobile=?,usekind=?,companystartdate=?,status=? where workcode=?";

    protected void execute(ResourceHrmsBean bean){
        RecordSet rs = new RecordSet();
        if(
                rs.executeUpdate(sql1,bean.getLastname(),bean.getManagerid(),bean.getDepartmentid(),bean.getSubcompanyid1(),
                bean.getJobtitle(),bean.getPhone(),bean.getUsekind(),bean.getCompanystartdate(),bean.getStatus(),bean.getWorkcode())
        )   cusData(bean);
    }

    private boolean cusData(ResourceHrmsBean bean){
        return true;
    }
}
