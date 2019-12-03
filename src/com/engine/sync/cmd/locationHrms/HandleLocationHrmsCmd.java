package com.engine.sync.cmd.locationHrms;

import com.engine.sync.entity.LocationHrmsBean;
import com.engine.sync.util.LocationUtils;
import org.apache.commons.lang.StringUtils;

public class HandleLocationHrmsCmd {

    public void handle(LocationHrmsBean bean){
        if(StringUtils.isNotBlank(bean.getCode())){
            if(LocationUtils.getLocationIdByCode(bean.getCode())>0){    //地点已存在==>更新
                new UpdateLocationHrmsCmd().updateLocation(bean);
            }else{  //新增
                new AddLocationHrmsCmd().addLocation(bean);
            }
        }
    }
}
