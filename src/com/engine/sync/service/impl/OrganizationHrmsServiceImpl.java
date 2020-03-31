package com.engine.sync.service.impl;

import com.engine.sync.cmd.organizationHrms.HandleOrganizationHrmsCmd;
import com.engine.sync.cmd.organizationHrms.ReadOrganizationHrmsCmd;
import com.engine.sync.entity.OrganizationHrmsBean;
import com.engine.sync.service.OrganizationHrmsService;
import weaver.general.BaseBean;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;

import java.io.*;

public class OrganizationHrmsServiceImpl implements OrganizationHrmsService {


    public void handle(){
        new BaseBean().writeLog("===OrganizationHrmsServiceImpl===");
        File file = new File("C:\\Users\\Ming\\Desktop\\WTCCN-ERMS-ORG-20190903103812.txt");
        HandleOrganizationHrmsCmd cmd = new HandleOrganizationHrmsCmd();
        if(file.exists()){
            BufferedReader reader = null;
            String tempString = null;
            int line = 1;
            try{
                //reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                while((tempString = reader.readLine()) != null ){
                    if(line>2) {
                        OrganizationHrmsBean bean = new ReadOrganizationHrmsCmd(tempString).getBean();
                        new BaseBean().writeLog("第" + line + "行长度:" + tempString.getBytes("UTF-8").length);
                        new BaseBean().writeLog("Organization：{" + bean.toString() + "}");
                        cmd.handle(bean);
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

        SubCompanyComInfo subCompanyComInfo = new SubCompanyComInfo();
        subCompanyComInfo.removeCompanyCache();
        DepartmentComInfo departmentComInfo = new DepartmentComInfo();
        departmentComInfo.removeCompanyCache();

    }


}
