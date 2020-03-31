<%@ page import="weaver.general.BaseBean" %>
<%@ page import="java.io.*" %>
<%@ page import="com.engine.sync.cmd.organizationHrms.HandleOrganizationHrmsCmd" %>
<%@ page import="com.engine.sync.entity.OrganizationHrmsBean" %>
<%@ page import="com.engine.sync.cmd.organizationHrms.ReadOrganizationHrmsCmd" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    new BaseBean().writeLog("===OrganizationHrmsServiceImpl===");
    File file = new File("/app/lym/sync20200329/WTCCN-ERMS-ORG-20200329124938.txt");
    HandleOrganizationHrmsCmd cmd = new HandleOrganizationHrmsCmd();
    if(file.exists()){
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try{
            //reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));



            while((tempString = reader.readLine()) != null ){
                if(line>=3) {
                    OrganizationHrmsBean bean = new ReadOrganizationHrmsCmd(tempString).getBean();
                    new BaseBean().writeLog("第" + line + "行:" + bean.toString());
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
%>