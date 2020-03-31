package com.engine.sync.cmd.ResourceHrms;

import com.engine.common.service.impl.HrmCommonServiceImpl;
import com.engine.sync.entity.OrganizationHrmsBean;
import com.engine.sync.entity.ResourceHrmsBean;
import com.engine.sync.util.OrgUtil;
import com.engine.sync.util.PositionUtils;
import com.engine.sync.util.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import weaver.general.Util;
import weaver.interfaces.lym.util.CalendarMethods;

import java.text.ParseException;
import java.util.Date;

public class ReadResourceHrmsCmd {

    private String resourceInfo;
    private static HrmCommonServiceImpl hcsi = new HrmCommonServiceImpl();

    public ReadResourceHrmsCmd(String resourceInfo){
        this.resourceInfo = resourceInfo;
    }


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
                        bean.setJobtitleName(tempString);
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

            /**
             * 转换信息
             */
            //直接上级
            //bean.setManagerid("");
            //部门
            bean.setDepartmentid(OrgUtil.getOrgidByCode(bean.getDepartmentcode())+"");
            //分部
            bean.setSubcompanyid1(OrganizationHrmsBean.subcompanyid+"");
            //职位
            bean.setJobtitle(PositionUtils.getJobidByName(bean.getJobtitleName())+"");
            //员工类别(用工性质)
            bean.setUsekind(ResourceUtils.getUseKind(bean.getUsekind())+"");

            /**
             * 特殊处理信息
             */
            //离职信息==>如果有离职日期，离职当天状态为离职
            if(Util.null2String(bean.getTempfield2()).length()==8){//格式yyyyMMdd
                String temp = bean.getTempfield2().substring(0,4)+"-"+bean.getTempfield2().substring(4,6)+"-"+bean.getTempfield2().substring(6,8);
                //new BaseBean().writeLog("离职日期:"+temp);
                bean.setTempfield2(temp);
                try {
                    if (CalendarMethods.dateFormat.parse(temp).getTime()>new Date().getTime())//未离职
                        bean.setStatus("1");
                    else//已离职
                        bean.setStatus("5");
                }catch (ParseException e){
                    bean.setStatus("1");
                }
                //new BaseBean().writeLog("状态:"+bean.getStatus());
            }else{
                bean.setStatus("1");
            }

            //入职日期
            if(StringUtils.isNotBlank(bean.getCompanystartdate()) && bean.getCompanystartdate().length()==8){
                String temp = bean.getCompanystartdate().substring(0,4)+"-"+bean.getCompanystartdate().substring(4,6)+"-"+bean.getCompanystartdate().substring(6,8);
                bean.setCompanystartdate(temp);
            }


            //职级    G.N.N.11=>11
            if(StringUtils.isNotBlank(bean.getTempfield6())){
                String[] var1 = bean.getTempfield6().split("\\.");
                if(var1.length>0) {
                    String var2 = var1[var1.length - 1];
                    bean.setTempfield6(var2);

                    //安全级别
                    bean.setSeclevel((Util.getIntValue(var2,0) * 10)+"");
                }
            }

            //拼音
            bean.setPinyinlastname(hcsi.generateQuickSearchStr(bean.getLastname()));


            //分公司(优先取手工维护（Personal）)
            if(bean.getTempfield8().length()>0) bean.setTempfield7(bean.getTempfield8());
            //部门代码
            if(bean.getTempfield10().length()>0) bean.setTempfield9(bean.getTempfield10());
            //成本中心
            if(bean.getTempfield12().length()>0) bean.setTempfield11(bean.getTempfield12());

            /*处理特殊符号*/
            bean.setLastname(bean.getLastname().replaceAll("·","."));
        }
        return bean;
    }


}
