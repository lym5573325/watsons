<%@ page import="weaver.general.BaseBean" %>
<%@ page import="java.io.*" %>
<%@ page import="com.engine.sync.cmd.positionHrms.HandlePositionHrmsCmd" %>
<%@ page import="com.engine.sync.entity.PositionHrmsBean" %>
<%@ page import="com.engine.sync.cmd.positionHrms.ReadPositionHrmsCmd" %>
<%@ page import="weaver.hrm.job.JobTitlesComInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    new BaseBean().writeLog("===PositionHrmsServiceImpl===");
    File file = new File("C:\\Users\\hrpt.support\\Desktop\\syncinfo\\WTCCN-ERMS-POSITION-20190903103846.txt");
    HandlePositionHrmsCmd cmd = new HandlePositionHrmsCmd();
    if(file.exists()){
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try{
            //reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            while((tempString = reader.readLine()) != null){
                PositionHrmsBean bean = new ReadPositionHrmsCmd(tempString).getBean();
                new BaseBean().writeLog("第" + line + "行:" + bean.toString());
                //cmd.handle(bean);
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
    //刷新岗位缓存
    JobTitlesComInfo jobTitlesComInfo = new JobTitlesComInfo();
    jobTitlesComInfo.removeJobTitlesCache();
%>