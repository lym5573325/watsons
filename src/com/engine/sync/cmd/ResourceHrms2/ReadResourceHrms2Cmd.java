package com.engine.sync.cmd.ResourceHrms2;

import com.engine.sync.entity.ResourceHrms2Bean;
import com.engine.sync.entity.ResourceHrmsBean;
import com.engine.sync.util.ResourceUtils;

public class ReadResourceHrms2Cmd {

    private String separator = ";";
    private String resourceInfo;

    public ReadResourceHrms2Cmd(String resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public ResourceHrms2Bean getBean() {
        ResourceHrms2Bean bean = new ResourceHrms2Bean();
        //验证数据合法性
        String[] arr = resourceInfo.split(separator);
        if (arr.length >= 14) {
            for (int i = 0; i < arr.length; i++) {
                String tempStr = arr[i];
                System.out.println("tempStr:" + tempStr);
                switch (i) {
                    case 0:
                        bean.setWorkcode(tempStr);
                        break;
                    case 10:
                        bean.setCertificatenum(tempStr);
                        break;
                    case 11:
                        bean.setSex(tempStr);
                        break;
                    case 14:
                        bean.setEmail(tempStr);
                        break;
                }
            }
        }

        /**
         * 转换信息
         */
        //性别
        bean.setSex(ResourceUtils.transSex(bean.getSex()));
        return bean;
    }
}
