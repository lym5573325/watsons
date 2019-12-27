<%@ page import="com.engine.sync.cmd.ResourceHrms2.HandleResourceHrms2Cmd" %>
<%@ page import="com.engine.sync.entity.ResourceHrms2Bean" %>
<%@ page import="com.engine.sync.cmd.ResourceHrms2.ReadResourceHrms2Cmd" %>
<%@ page import="java.io.*" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    BaseBean bb = new BaseBean();
    File file = new File("D:\\WEAVER9\\syncinfo\\20191216\\Employee-eDMS-20191216.txt");
    HandleResourceHrms2Cmd handle = new HandleResourceHrms2Cmd();
    if(file.exists()){
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            while((tempString = reader.readLine()) != null && line<500){
                ResourceHrms2Bean bean = new ReadResourceHrms2Cmd(tempString).getBean();
                bb.writeLog("第"+line+"行:"+bean.toString());
                handle.handle(bean);
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
%>