package com.engine.sync.cmd.organizationHrms;

import com.engine.sync.entity.OrganizationHrmsBean;
import com.engine.sync.util.OrgUtil;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * 同步部门信息
 * 增删改（OrganizationHrmsBean）部门
 */
public class HandleOrganizationHrmsCmd {

    BaseBean baseBean = new BaseBean();
    public void handle(OrganizationHrmsBean bean){
        if(bean.getDeptCode().length()>0 && bean.getDeptName().length()>0) {
            if (OrgUtil.getOrgidByCode(bean.getDeptCode()) > 0) {//部门已在，更新信息
                baseBean.writeLog("修改部门");
                System.out.println("新增部门");
                new UpdateOrganizationHrmsCmd().updateOrgByCode(bean.getDeptCode(), bean.getDeptName(), bean.getDeptName(), bean.getSupdepid(), OrganizationHrmsBean.subcompanyid, Util.getIntValue(bean.getDeptlevel()), 1, "", 1, "",bean.getAllSupdepid());
            } else if (bean.getDeptCode().length() > 0) {//部门不在，新增部门
                baseBean.writeLog("新增部门");
                System.out.println("新增部门");
                new AddOrganizationHrmsCmd().addOrg(bean.getDeptCode(), bean.getDeptName(), bean.getDeptName(), bean.getSupdepid(), OrganizationHrmsBean.subcompanyid, Util.getIntValue(bean.getDeptlevel()), 1, "", 1, "", bean.getAllSupdepid());
            }
        }else{
            new BaseBean().writeLog("部门信息为空:"+bean.toString());
        }
    }


}
