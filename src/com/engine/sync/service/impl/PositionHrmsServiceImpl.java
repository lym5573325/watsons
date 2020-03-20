package com.engine.sync.service.impl;

import com.engine.common.service.impl.HrmCommonServiceImpl;
import com.engine.sync.cmd.organizationHrms.HandleOrganizationHrmsCmd;
import com.engine.sync.cmd.organizationHrms.ReadOrganizationHrmsCmd;
import com.engine.sync.cmd.positionHrms.HandlePositionHrmsCmd;
import com.engine.sync.cmd.positionHrms.ReadPositionHrmsCmd;
import com.engine.sync.entity.OrganizationHrmsBean;
import com.engine.sync.entity.PositionHrmsBean;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
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
                    if(line>2) {
                        PositionHrmsBean bean = new ReadPositionHrmsCmd(tempString).getBean();
                        new BaseBean().writeLog("第" + line + "行:" + bean.toString());
                        cmd.handle(bean);
                    }
                    line++;
                }

                //更新拼音
                HrmCommonServiceImpl hcsi = new HrmCommonServiceImpl();
                RecordSet rs = new RecordSet();
                RecordSet rs2 = new RecordSet();
                rs.execute("select id,jobtitlename from hrmjobtitles order by id desc");
                while(rs.next()){
                    String pinyin = hcsi.generateQuickSearchStr(Util.null2String(rs.getString("jobtitlename")).toLowerCase().replaceAll(" ",""));
                    rs2.executeUpdate("update hrmjobtitles set ecology_pinyin_search=? where id=?", pinyin, Util.null2String(rs.getString("id")));
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
