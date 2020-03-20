package com.engine.sync.cmd.positionHrms;

import com.engine.common.service.impl.HrmCommonServiceImpl;
import com.engine.sync.entity.PositionHrmsBean;
import org.apache.commons.lang.StringUtils;
import weaver.general.BaseBean;

public class ReadPositionHrmsCmd {

    private static HrmCommonServiceImpl hcsi = new HrmCommonServiceImpl();

    //分隔符
    private static final String separator = "\\|";
    private String positionInfo;

    public ReadPositionHrmsCmd(String tempString){
        this.positionInfo = tempString;
    }

    public PositionHrmsBean getBean(){
        PositionHrmsBean bean = new PositionHrmsBean();
        //验证数据合法性
        String[] arr = positionInfo.split(separator);
        System.out.println("length："+arr.length);
        if(arr.length>=4){
            for(int i=0;i<arr.length;i++){
                String tempStr = arr[i];
                System.out.println("tempStr:"+tempStr);
                switch (i){
                    case 0:
                        bean.setJobtitleCode(tempStr);
                        break;
                    case 1:
                        bean.setJobtitleName(tempStr);
                        break;
                    case 2:
                        bean.setJob_pk(tempStr);
                        break;
                    case 3:
                        bean.setJobdepartmentCode(tempStr);
                        break;
                    case 4:
                        bean.setLocationCode(tempStr);
                        break;
                }
            }

            if(StringUtils.isNotBlank(bean.getJobtitleName())){
                String[] var1 = bean.getJobtitleName().split("\\.");
                if(var1.length==2)  bean.setJobtitleMark(var1[0]);
            }

            bean.setJobtitlePinyin(hcsi.generateQuickSearchStr(bean.getJobtitleName()).toLowerCase().replaceAll(" ",""));
            //获取职务
            bean.setJob_pk("1");//暂时默认等于1
        }
        return bean;
    }

}
