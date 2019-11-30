package com.engine.sync.cmd.ResourceHrms;

import com.engine.sync.entity.ResourceHrmsBean;
import org.apache.commons.lang.StringUtils;

public class ReadResourceHrmsCmd {

    private String resourceInfo;

    public ReadResourceHrmsCmd(String resourceInfo){
        this.resourceInfo = resourceInfo;
    }

    /*public static String cut(String str, int be, int end) throws Exception{
        byte[] strByte = str.getBytes("gbk");
        byte[] newStrByte = new byte[end-be];
        System.arraycopy(strByte,be,newStrByte,0,end-be);
        String temp = new String(newStrByte,"gbk");
        int count = 0;
        if ((count = StringUtils.countMatches(temp,"·"))==0)  return temp;
        else{
            System.arraycopy(strByte,be,newStrByte,0,end-be+count);
            return new String(newStrByte,"gbk");
        }
    }*/
    public static String cut(String str, int be, int end) throws Exception{
        byte[] strByte = str.getBytes("gbk");
        byte[] newStrByte = new byte[end-be];
        System.arraycopy(strByte,be,newStrByte,0,end-be);
        return  new String(newStrByte,"gbk");
    }



    public ResourceHrmsBean getBean(){
        ResourceHrmsBean bean = new ResourceHrmsBean();
        if(resourceInfo.length()>= bean.getSumLength()) {
            int nowLength = 0;
            for (int i = 0; i < bean.getLengthList().size(); i++) {
                int length = bean.getLengthList().get(i);
                //String tempString = resourceInfo.substring(nowLength, nowLength + length).trim();
                String tempString;
                int var1 = 0;
                try {
                    tempString = cut(resourceInfo, nowLength, nowLength + length).trim();
                    if((var1=StringUtils.countMatches(tempString,"·"))>0)  tempString = cut(resourceInfo, nowLength, nowLength + var1).trim();
                }catch (Exception e){
                    tempString = "";
                }finally {
                    nowLength += (length + var1);
                }
                switch (i){
                    case 0:
                        bean.setWorkcode(tempString);
                        break;
                    case 1:
                        bean.setTempfield1(tempString);
                        break;
                    case 2:
                        bean.setCompanystartdate(tempString);
                        break;
                    case 3:
                        bean.setJobtitle(tempString);
                        break;
                    case 4:
                        bean.setDepartmentcode(tempString);
                        break;
                    case 5:
                        bean.setTempfield2(tempString);
                        break;
                    case 6:
                        bean.setLastname(tempString);
                        break;
                    case 7:
                        bean.setTempfield3(tempString);
                        break;
                    case 8:
                        bean.setTempfield4(tempString);
                        break;
                    case 9:
                        bean.setUsekind(tempString);
                        break;
                    case 10:
                        bean.setTempfield5(tempString);
                        break;
                    case 11:
                        bean.setPhone(tempString);
                        break;
                    case 12:
                        bean.setTempfield6(tempString);
                        break;
                    case 13:
                        bean.setTempfield7(tempString);
                        break;
                    case 14:
                        bean.setTempfield8(tempString);
                        break;
                    case 15:
                        bean.setTempfield9(tempString);
                        break;
                    case 16:
                        bean.setTempfield10(tempString);
                        break;
                    case 17:
                        bean.setTempfield11(tempString);
                        break;
                    case 18:
                        bean.setTempfield12(tempString);
                        break;
                    case 19:
                        bean.setTempfield13(tempString);
                        break;
                    case 20:
                        bean.setTempfield14(tempString);
                        break;
                    case 21:
                        bean.setTempfield15(tempString);
                        break;
                    case 22:
                        bean.setTempfield16(tempString);
                        break;
                }
            }
        }
        return bean;
    }


}
