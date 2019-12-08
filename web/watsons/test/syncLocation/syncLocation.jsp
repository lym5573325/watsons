<%@ page import="weaver.general.BaseBean" %>
<%@ page import="com.engine.sync.cmd.locationHrms.HandleLocationHrmsCmd" %>
<%@ page import="com.engine.sync.entity.LocationHrmsBean" %>
<%@ page import="com.engine.sync.cmd.locationHrms.ReadLocationHrmsCmd" %>
<%@ page import="java.io.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    new BaseBean().writeLog("===LocationHrmsServiceImpl===");
    File file = new File("C:\\Users\\hrpt.support\\Desktop\\syncinfo\\WTCCN-ERMS-LOCATION-20190903103809.txt.txt");
    HandleLocationHrmsCmd handle = new HandleLocationHrmsCmd();
    if(file.exists()){
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            while((tempString = reader.readLine()) != null){
                LocationHrmsBean bean = new ReadLocationHrmsCmd(tempString).getBean();
                new BaseBean().writeLog("第" + line + "行："+ bean.toString());
                //handle.handle(bean);
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