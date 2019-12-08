package com.engine.sync.service.impl;

import com.engine.sync.cmd.organizationHrms.HandleOrganizationHrmsCmd;
import com.engine.sync.cmd.organizationHrms.ReadOrganizationHrmsCmd;
import com.engine.sync.cmd.positionHrms.HandlePositionHrmsCmd;
import com.engine.sync.cmd.positionHrms.ReadPositionHrmsCmd;
import com.engine.sync.entity.OrganizationHrmsBean;
import com.engine.sync.entity.PositionHrmsBean;
import weaver.general.BaseBean;
import weaver.hrm.job.JobTitlesComInfo;

import java.io.*;

public class PositionHrmsServiceImpl {
    public void handle(){
        new BaseBean().writeLog("===PositionHrmsServiceImpl===");
        File file = new File("C:\\Users\\Ming\\Desktop\\WTCCN-ERMS-POSITION-20190903103846.txt");
        HandlePositionHrmsCmd cmd = new HandlePositionHrmsCmd();
        if(file.exists()){
            BufferedReader reader = null;
            String tempString = null;
            int line = 1;
            try{
                //reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                while((tempString = reader.readLine()) != null){
                    PositionHrmsBean bean = new ReadPositionHrmsCmd(tempString).getBean();
                    new BaseBean().writeLog("第" + line + "行:" + bean.toString());
                    cmd.handle(bean);
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
        //刷新岗位缓存
        JobTitlesComInfo jobTitlesComInfo = new JobTitlesComInfo();
        jobTitlesComInfo.removeJobTitlesCache();
    }
}
