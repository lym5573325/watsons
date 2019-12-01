package com.engine.sync.cmd.ResourceHrms;

import com.engine.sync.entity.ResourceHrmsBean;
import com.engine.sync.util.ResourceUtils;
import weaver.general.BaseBean;

public class HandleResourceHrmsCmd {

    BaseBean bb = new BaseBean();

    public void handle(ResourceHrmsBean bean){
        //判断人是否在系统
        if(bean.getWorkcode().length()>0) {
            if (ResourceUtils.getUidByWorkcode(bean.getWorkcode()) > 0) {   //人员已存在 ==>更新
                new UpdateResourceHrmsCmd().execute(bean);
            }else{  //人员不存在==>新增
                new AddResourceHrmsCmd().execute(bean);
            }
        }
    }
}
