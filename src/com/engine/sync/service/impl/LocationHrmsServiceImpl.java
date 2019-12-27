package com.engine.sync.service.impl;

import com.engine.sync.cmd.locationHrms.HandleLocationHrmsCmd;
import com.engine.sync.cmd.locationHrms.ReadLocationHrmsCmd;
import com.engine.sync.cmd.organizationHrms.HandleOrganizationHrmsCmd;
import com.engine.sync.cmd.organizationHrms.ReadOrganizationHrmsCmd;
import com.engine.sync.entity.LocationHrmsBean;
import com.engine.sync.entity.OrganizationHrmsBean;
import com.engine.sync.service.LocationHrmsService;
import weaver.general.BaseBean;

import java.io.*;

public class LocationHrmsServiceImpl implements LocationHrmsService {

    public void handle(){
        new BaseBean().writeLog("===LocationHrmsServiceImpl===");
        File file = new File("C:\\Users\\hrpt.support\\Desktop\\syncinfo\\WTCCN-ERMS-LOCATION-20190903103809.txt.txt");
        HandleLocationHrmsCmd handle = new HandleLocationHrmsCmd();
        if(file.exists()){
            BufferedReader reader = null;
            String tempString = null;
            int line = 1;
            try{
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                while((tempString = reader.readLine()) != null){
                    if(line>2) {
                        LocationHrmsBean bean = new ReadLocationHrmsCmd(tempString).getBean();
                        new BaseBean().writeLog("第" + line + "行：" + bean.toString());
                        handle.handle(bean);
                    }
                    line++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(reader != null){
                    try{
                        reader.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
