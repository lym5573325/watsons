<%@ page import="java.io.*" %>
<%@ page import="com.engine.sync.cmd.ResourceHrms.ReadResourceHrmsCmd" %>
<%@ page import="weaver.hrm.resource.ResourceComInfo" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page import="com.engine.sync.cmd.ResourceHrms.HandleResourceHrmsCmd" %>
<%@ page import="com.engine.sync.entity.ResourceHrmsBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    File file = new File("/app/lym/sync20200329/Employee-20200329.txt");
    if(file.exists()){
        out.print("文件存在");
        HandleResourceHrmsCmd cmd = new HandleResourceHrmsCmd();
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
            while((tempString = reader.readLine()) != null){
                //new BaseBean().writeLog("第" + line + "行长度:" + tempString.getBytes("gbk").length);
                //new BaseBean().writeLog("beanInfo：{" + new ReadResourceHrmsCmd(tempString).getBean().toString() + "}");
                ResourceHrmsBean bean = new ReadResourceHrmsCmd(tempString).getBean();
                cmd.handle(bean);
                line++;
            }
        }catch (Exception e){
            out.print("异常:"+e.toString());
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
%>