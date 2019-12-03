package com.engine.sync.cmd.positionHrms;

import com.engine.sync.entity.PositionHrmsBean;
import com.engine.sync.util.PositionUtils;
import org.apache.commons.lang.StringUtils;

public class HandlePositionHrmsCmd {

    public void handle(PositionHrmsBean bean){
        if(StringUtils.isNotBlank(bean.getJobtitleCode())){
            if(PositionUtils.getJobIdByCode(bean.getJobtitleCode()) > 0 ){
                new AddPositionHrmsCmd().addPostion(bean);
            }else{
                new UpdatePositionHrmsCmd().updatePosition(bean);
            }
        }
    }
}
