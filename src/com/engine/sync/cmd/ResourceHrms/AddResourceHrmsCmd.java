package com.engine.sync.cmd.ResourceHrms;

import com.engine.sync.entity.ResourceHrmsBean;
import com.engine.sync.util.LocationUtils;
import com.engine.sync.util.ResourceUtils;
import weaver.conn.RecordSet;
import weaver.interfaces.lym.util.CalendarMethods;

public class AddResourceHrmsCmd {

    private static final String addSql = "insert into hrmresource (id,workcode,lastname,departmentid,subcompanyid1," +
            "jobtitle,mobile,usekind,companystartdate,status,joblevel,loginid,lastmoddate,modified,creater,created,seclevel," +
            "pinyinlastname,ecology_pinyin_search)" +
            "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?,?)";


    //protected boolean execute(String workcode, String lastname, String managerid,String departmentid, String subcompanyid1 ){
    protected boolean execute(ResourceHrmsBean bean){
        //new BaseBean().writeLog("新增人员:"+bean.toString());
        RecordSet rs = new RecordSet();
        if(rs.executeUpdate(addSql,ResourceUtils.getMaxId(), bean.getWorkcode(),bean.getLastname(),bean.getDepartmentid(),
                bean.getSubcompanyid1(),bean.getJobtitle(),bean.getPhone(),bean.getUsekind(),bean.getCompanystartdate(),
                bean.getStatus(), bean.getTempfield6(), bean.getWorkcode(), CalendarMethods.getCurrentDate(),"1",bean.getSeclevel(),
                bean.getPinyinlastname(), bean.getPinyinlastname())
        ) cusData(bean);
        return false;
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
    private static final String sql2 = "insert into cus_fielddata(id,scopeid,scope,field0,field1,field2,field3,field4,field5,field6,field7,field8,field9,field10)" +
            " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * 新增人员自定义属性
     * @param bean
     * @return
     */
    protected boolean cusData(ResourceHrmsBean bean){
        //new BaseBean().writeLog("新增人员自定义字段表");
        RecordSet rs = new RecordSet();
        rs.executeUpdate(sql2, ResourceUtils.getUidByWorkcode(bean.getWorkcode()), "3","HrmCustomFieldByInfoType",
                bean.getTempfield2(), bean.getTempfield3(), bean.getTempfield4(), bean.getTempfield7(),bean.getTempfield9(), bean.getTempfield11(),
                bean.getTempfield14(), bean.getTempfield15(), bean.getTempfield16(), bean.getTempfield5(), LocationUtils.getLocationIdByCode(bean.getTempfield13()));
        return true;
    }
}
