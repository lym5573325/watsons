package com.engine.sync.cmd.ResourceHrms;

import com.engine.sync.entity.ResourceHrmsBean;
import com.engine.sync.util.LocationUtils;
import com.engine.sync.util.ResourceUtils;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;

public class UpdateResourceHrmsCmd {

    private static final String sql1 = "update hrmresource set lastname=?,managerid=?,departmentid=?," +
            "subcompanyid1=?,jobtitle=?,mobile=?,usekind=?,companystartdate=?,status=?,joblevel=? where workcode=?";

    protected void execute(ResourceHrmsBean bean){
        new BaseBean().writeLog("更新人员："+bean.getWorkcode());
        RecordSet rs = new RecordSet();
        if(
                rs.executeUpdate(sql1,bean.getLastname(),bean.getManagerid(),bean.getDepartmentid(),bean.getSubcompanyid1(),
                bean.getJobtitle(),bean.getPhone(),bean.getUsekind(),bean.getCompanystartdate(),bean.getStatus(),bean.getTempfield6(),
                        bean.getWorkcode())
        )   cusData(bean);
    }

    /**
     * field0==>    离职日期
     * field1==>    拼音
     * field2==>    英文名
     * field3==>    分公司
     * field4==>    部门代码
     * field5==>    成本中心
     * field6==>    调店组织
     * field7==>    调店区域
     * field8==>    调店生效日
     * field9==>    分配状态
     * field10==>   地点
     */
    private static final String sql2 = "update cus_fielddata set field0=?,field1=?,field2=?,field3=?,field4=?,field5=?,field6=?,field7=?,field8=?,field9=?,field10=? where id=? and scopeid=1";
    private boolean cusData(ResourceHrmsBean bean){
        new BaseBean().writeLog("更新人员自定义表");
        RecordSet rs = new RecordSet();
        int uid = ResourceUtils.getUidByWorkcode(bean.getWorkcode());
        if(uid>0) {
            rs.execute("select id from cus_fielddata where id="+uid+" and scopeid=1");
            if(rs.next()) {
                new BaseBean().writeLog("更新");
                rs.executeUpdate(sql2,
                        bean.getTempfield2(), bean.getTempfield3(), bean.getTempfield4(), bean.getTempfield7(), bean.getTempfield9(), bean.getTempfield11(),
                        bean.getTempfield14(), bean.getTempfield15(), bean.getTempfield16(), bean.getTempfield5(), LocationUtils.getLocationIdByCode(bean.getTempfield13()),
                        uid + "");
            }else{
                new BaseBean().writeLog("新增");
                new AddResourceHrmsCmd().cusData(bean);
            }
        }
        return true;
    }
}
