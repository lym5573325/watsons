package com.engine.sync.cmd.ResourceHrms;

import com.engine.sync.entity.ResourceHrmsBean;
import com.engine.sync.util.ResourceUtils;
import weaver.general.BaseBean;

public class HandleResourceHrmsCmd {

    BaseBean bb = new BaseBean();

    UpdateResourceHrmsCmd ur = new UpdateResourceHrmsCmd();
    AddResourceHrmsCmd ar = new AddResourceHrmsCmd();

    public void handle(ResourceHrmsBean bean){
        //判断人是否在系统
        if(bean.getWorkcode().length()>0) {
            new BaseBean().writeLog("人员:"+bean.getWorkcode());
            if (ResourceUtils.getUidByWorkcode(bean.getWorkcode()) > 0) {   //人员已存在 ==>更新
                //new BaseBean().writeLog("更新");
                ur.execute(bean);
            }else{  //人员不存在==>新增
                //new BaseBean().writeLog("新增");
                ar.execute(bean);
            }
        }else{
            new BaseBean().writeLog("人员编号为空:"+bean.toString());
        }
    }
}
