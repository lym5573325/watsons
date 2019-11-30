package com.engine.sync.service.impl;

import com.engine.sync.cmd.organizationHrms.ReadOrganizationHrmsCmd;
import com.engine.sync.service.OrganizationHrmsService;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;

import java.io.*;

public class OrganizationHrmsServiceImpl implements OrganizationHrmsService {

    public static void main(String[] args){
        new OrganizationHrmsServiceImpl().handle();
    }

    public void handle(){
        File file = new File("C:\\Users\\Ming\\Desktop\\WTCCN-ERMS-ORG-20190903103812.txt");
        if(file.exists()){
            BufferedReader reader = null;
            String tempString = null;
            int line = 1;
            try{
                //reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
                while((tempString = reader.readLine()) != null){
                    //System.out.println("第" + line + "行长度:" + tempString.getBytes("gbk").length);
                    System.out.println("Organization：{" + new ReadOrganizationHrmsCmd(tempString).getBean().toString() + "}");
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
