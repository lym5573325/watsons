package com.engine.sync.cmd.organizationHrms;

import com.engine.sync.entity.OrganizationHrmsBean;
import com.engine.sync.util.LocationUtils;
import com.engine.sync.util.OrgUtil;
import weaver.conn.RecordSet;
import weaver.hrm.company.DepartmentComInfo;

public class ReadOrganizationHrmsCmd {
    private String orgInfo = "";
    //分隔符
    public static final String separator = "\\|";

    public ReadOrganizationHrmsCmd(String orgInfo){
        this.orgInfo = orgInfo;
    }

    public OrganizationHrmsBean getBean(){
        OrganizationHrmsBean bean = new OrganizationHrmsBean();
        //验证数据合法性
        String[] arr = orgInfo.split(separator);
        System.out.println("length："+arr.length);
        if(arr.length>=5){
            for(int i=0;i<arr.length;i++){
                String tempStr = arr[i];
                System.out.println("tempStr:"+tempStr);
                switch (i){
                    case 0:
                        bean.setHrms_OrgId(tempStr);
                        break;
                    case 1:
                        bean.setDeptName(tempStr);
                        break;
                    case 2:
                        bean.setDeptlevel(tempStr);
                        break;
                    case 3:
                        bean.setAddress(tempStr);
                        break;
                    case 4:
                        bean.setDeptCode(tempStr);
                        break;
                    case 5:
                        bean.setDepttype(tempStr);
                        break;
                }
            }

            //根据hrms_orgId获取上级部门
            String supdepid = getSupdepidByHrmsOrgID(bean.getHrms_OrgId());
            bean.setSupdepid(supdepid);
            //部门名称转换  将"&"转成"."
            bean.setDeptName(bean.getDeptName().replaceAll("&","."));
            //获取所有上级部门
            try {
                String allSupdepid = DepartmentComInfo.getAllParentDepartId(supdepid, supdepid);
                if(allSupdepid.length()>0) bean.setAllSupdepid(","+allSupdepid+",");
            }catch (Exception e){}
        }
        return bean;
    }

    /**
     * 根据HRMS_ORGID获取上级部门
     * @param var1   HRMS_ORGID的值
     * @return  返回OA上上级部门的ID
     * 822-809-34088  未三级部门，部门层级由高到低,"-"分割
     */
    private String getSupdepidByHrmsOrgID(String var1){
        String[] supdepArr = var1.split("-");
        int supdepid = supdepArr.length>=2 ? OrgUtil.getOrgidByCode(supdepArr[supdepArr.length-2]) : 0;
        if(supdepid>0)  return supdepid+"";
        return "";
    }

}
