package com.engine.epdocking.service.impl;

import com.engine.core.impl.Service;
import com.engine.epdocking.cmd.epSystem.GetSystemList;
import com.engine.epdocking.service.EpSystemService;

import java.util.Map;

public class EpSystemServiceImpl extends Service implements EpSystemService {

    @Override
    public Map<String, Object> getSystemList(Map<String, Object> params){
        return commandExecutor.execute(new GetSystemList(user, params));
    }
}
