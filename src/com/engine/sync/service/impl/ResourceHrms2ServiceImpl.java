package com.engine.sync.service.impl;

import com.engine.sync.cmd.ResourceHrms2.HandleResourceHrms2Cmd;
import com.engine.sync.cmd.ResourceHrms2.ReadResourceHrms2Cmd;
import com.engine.sync.entity.ResourceHrms2Bean;
import com.engine.sync.service.ResourceHrms2Service;
import weaver.general.BaseBean;

import java.io.*;

public class ResourceHrms2ServiceImpl implements ResourceHrms2Service {

    @Override
    public void handle() {
        new BaseBean().writeLog("===ResourceHrms2ServiceImpl===");
        File file = new File("C:\\Users\\hrpt.support\\Desktop\\syncinfo\\WTCCN-ERMS-LOCATION-20190903103809.txt.txt");
        HandleResourceHrms2Cmd handle = new HandleResourceHrms2Cmd();
        if(file.exists()){
            BufferedReader reader = null;
            String tempString = null;
            int line = 1;
            try{
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                while((tempString = reader.readLine()) != null){
                    ResourceHrms2Bean bean = new ReadResourceHrms2Cmd(tempString).getBean();
                    handle.handle(bean);
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
