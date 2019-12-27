package com.engine.sync.cmd.ResourceHrms2;

import com.engine.sync.cmd.ResourceHrms.AddResourceHrmsCmd;
import com.engine.sync.cmd.ResourceHrms.UpdateResourceHrmsCmd;
import com.engine.sync.entity.ResourceHrms2Bean;
import com.engine.sync.entity.ResourceHrmsBean;
import com.engine.sync.util.ResourceUtils;
import weaver.general.BaseBean;

public class HandleResourceHrms2Cmd {

    BaseBean bb = new BaseBean();
    UpdateResourceHrms2Cmd urhc = new UpdateResourceHrms2Cmd();

    public void handle(ResourceHrms2Bean bean){

        //判断人是否在系统
        if(bean.getWorkcode().length()>0) {
            new BaseBean().writeLog("人员:"+bean.getWorkcode());
            if (ResourceUtils.getUidByWorkcode(bean.getWorkcode()) > 0) {   //人员已存在 ==>更新
                urhc.execute(bean);
            }
        }else{
            new BaseBean().writeLog("人员编号为空:"+bean.toString());
        }
    }
}
