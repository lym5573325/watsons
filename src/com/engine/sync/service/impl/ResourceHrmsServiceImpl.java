package com.engine.sync.service.impl;

import com.engine.sync.cmd.ResourceHrms.ReadResourceHrmsCmd;
import com.engine.sync.service.ResourceHrmsService;
import weaver.hrm.resource.ResourceComInfo;

import java.io.*;

public class ResourceHrmsServiceImpl implements ResourceHrmsService {


    @Override
    public void handle() {
        File file = new File("C:\\Users\\Ming\\Desktop\\Employee-20191206.txt");
        if(file.exists()){
            BufferedReader reader = null;
            String tempString = null;
            int line = 1;
            try{
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
                while((tempString = reader.readLine()) != null && line<100){
                    System.out.println("第" + line + "行长度:" + tempString.getBytes("gbk").length);
                    System.out.println("beanInfo：{" + new ReadResourceHrmsCmd(tempString).getBean().toString() + "}");
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

            //清除人员缓存
            try {
                ResourceComInfo resourceComInfo = new ResourceComInfo();
                resourceComInfo.removeResourceCache();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ResourceHrmsServiceImpl().handle();
    }

    public static String cut(String str, int be, int end) throws Exception{
        byte[] strByte = str.getBytes("gbk");
        byte[] newStrByte = new byte[end-be];
        System.arraycopy(strByte,be,newStrByte,0,end-be);
        return new String(newStrByte,"gbk");
    }




    public static String substringByte(String orignal, int start, int count) {

        // 如果目标字符串为空，则直接返回，不进入截取逻辑；
        if (orignal == null || "".equals(orignal))
            return orignal;

        // 截取Byte长度必须>0
        if (count <= 0)
            return orignal;

        // 截取的起始字节数必须比
        if (start < 0)
            start = 0;

        // 目标char Pull buff缓存区间；
        StringBuffer buff = new StringBuffer();

        try {

            // 截取字节起始字节位置大于目标String的Byte的length则返回空值
            if (start >= getStringByteLenths(orignal))
                return null;

            // int[] arrlen=getByteLenArrays(orignal);
            int len = 0;

            char c;

            // 遍历String的每一个Char字符，计算当前总长度
            // 如果到当前Char的的字节长度大于要截取的字符总长度，则跳出循环返回截取的字符串。
            for (int i = 0; i < orignal.toCharArray().length; i++) {

                c = orignal.charAt(i);

                // 当起始位置为0时候
                if (start == 0) {

                    len += String.valueOf(c).getBytes("utf-8").length;
                    if (len <= count)
                        buff.append(c);
                    else
                        break;

                } else {

                    // 截取字符串从非0位置开始
                    len += String.valueOf(c).getBytes("utf-8").length;
                    if (len >= start && len <= start + count) {
                        buff.append(c);
                    }
                    if (len > start + count)
                        break;

                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 返回最终截取的字符结果;
        // 创建String对象，传入目标char Buff对象
        return new String(buff);
    }

    public static int getStringByteLenths(String args) throws UnsupportedEncodingException {
        return args != null && args != "" ? args.getBytes("utf-8").length : 0;
    }

}
