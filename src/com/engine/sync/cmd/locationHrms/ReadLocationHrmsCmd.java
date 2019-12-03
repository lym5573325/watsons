package com.engine.sync.cmd.locationHrms;

import com.engine.sync.entity.LocationHrmsBean;

public class ReadLocationHrmsCmd {

    //分隔符
    private static final String separator = "\\|";

    private String locationInfo;
    public ReadLocationHrmsCmd(String tempString){
        this.locationInfo = tempString;
    }

    public LocationHrmsBean getBean(){
        LocationHrmsBean bean = new LocationHrmsBean();
        //验证数据合法性
        String[] arr = locationInfo.split(separator);
        System.out.println("length："+arr.length);
        if(arr.length>=7){
            for(int i=0;i<arr.length;i++){
                String tempStr = arr[i];
                System.out.println("tempStr:"+tempStr);
                switch (i){
                    case 0:
                        bean.setCode(tempStr);
                        break;
                    case 1:
                        bean.setName(tempStr);
                        break;
                    case 2:
                        bean.setPk_areacl(tempStr);
                        break;
                    case 3:
                        bean.setDef1(tempStr);
                        break;
                    case 4:
                        bean.setDef2(tempStr);
                        break;
                    case 5:
                        bean.setCity(tempStr);
                        break;
                    case 6:
                        bean.setProvince(tempStr);
                        break;
                    case 7:
                        bean.setCountry(tempStr);
                        break;
                }
            }
        }
        return bean;
    }
}
