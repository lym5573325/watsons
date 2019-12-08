package com.engine.sync.cmd.locationHrms;

import com.engine.sync.entity.LocationHrmsBean;
import com.engine.sync.util.LocationUtils;
import org.apache.commons.lang.StringUtils;
import weaver.general.BaseBean;

public class HandleLocationHrmsCmd {

    public void handle(LocationHrmsBean bean){
        boolean b=false;
        if(StringUtils.isNotBlank(bean.getCode())){
            if(LocationUtils.getLocationIdByCode(bean.getCode())>0){    //地点已存在==>更新
                b = new UpdateLocationHrmsCmd().updateLocation(bean);
            }else{  //新增
                b = new AddLocationHrmsCmd().addLocation(bean);
            }
        }
        if(b)   new BaseBean().writeLog("更新成功");
        else new BaseBean().writeLog("更新失败");

    }
}
