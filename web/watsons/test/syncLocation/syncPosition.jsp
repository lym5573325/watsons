<%@ page import="weaver.general.BaseBean" %>
<%@ page import="java.io.*" %>
<%@ page import="com.engine.sync.cmd.positionHrms.HandlePositionHrmsCmd" %>
<%@ page import="com.engine.sync.entity.PositionHrmsBean" %>
<%@ page import="com.engine.sync.cmd.positionHrms.ReadPositionHrmsCmd" %>
<%@ page import="weaver.hrm.job.JobTitlesComInfo" %>
<%@ page import="com.engine.common.service.impl.HrmCommonServiceImpl" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    new BaseBean().writeLog("===PositionHrmsServiceImpl===");
    File file = new File("/app/lym/sync20200329/WTCCN-ERMS-POSITION-20200329124949.txt");
    HandlePositionHrmsCmd cmd = new HandlePositionHrmsCmd();
    if(file.exists()){
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try{
            //reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            while((tempString = reader.readLine()) != null){
                if(line>=3) {
                    PositionHrmsBean bean = new ReadPositionHrmsCmd(tempString).getBean();
                    new BaseBean().writeLog("第" + line + "行:" + bean.toString());
                    cmd.handle(bean);
                }
                line++;
            }

            //更新拼音
            HrmCommonServiceImpl hcsi = new HrmCommonServiceImpl();
            RecordSet rs = new RecordSet();
            RecordSet rs2 = new RecordSet();
            rs.execute("select id,jobtitlename from hrmjobtitles order by id desc");
            while(rs.next()){
                String pinyin = hcsi.generateQuickSearchStr(Util.null2String(rs.getString("jobtitlename")).toLowerCase().replaceAll(" ",""));
                rs2.executeUpdate("update hrmjobtitles set ecology_pinyin_search=? where id=?", pinyin, Util.null2String(rs.getString("id")));
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