package com.engine.hrm.cmd.entryremind;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.common.DateUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.settings.ChgPasswdReminder;
import weaver.hrm.settings.RemindSettings;
import weaver.interfaces.lym.util.ImageFileUtils;
import weaver.systeminfo.SystemEnv;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * 入职周年提醒设置--获取弹窗提示信息
 */
public class GetEntryRemindInfoCmd extends AbstractCommonCommand<Map<String, Object>> {

    private ServletContext application;

    public GetEntryRemindInfoCmd(Map<String, Object> params, User user, ServletContext application) {
        this.user = user;
        this.params = params;
        this.application = application;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //RemindSettings settings = (RemindSettings) application.getAttribute("hrmsettings");//入职一周年提醒参数
            ChgPasswdReminder reminder = new ChgPasswdReminder();
            RemindSettings settings = reminder.getRemindSettings();
            String entryValid = Util.null2String(settings.getEntryValid());//是否开启了提醒
            String today = TimeUtil.getCurrentDateString();//今天的日期

            /**
             * 入职周年提醒
             */
            boolean isEntryRemind = true;/*今天是否有入职周年提醒*/
            String companyStartDate = getCompanyStratDate(user);//人员账号的入职日期
            new BaseBean().writeLog("user："+user.getUID()+"   入职日期:"+companyStartDate);


            /*只有在入职日期那一天才会弹窗提醒，且入职日期为空是不会有提醒的*/

            if(companyStartDate.equals("")||!today.substring(5).equals(companyStartDate.substring(5))){
                //resultMap.put("datas", "");
                //return resultMap;
                isEntryRemind = false;
            }

            int lenOfEntry = getLenOfEntry(user);//入职时长
            int yearNum = lenOfEntry/365;//入职年限
            
            if ((!entryValid.equals("1")) || yearNum==0 ) {
                //resultMap.put("datas", "");
                //return resultMap;
                isEntryRemind = false;
            }

            if(isEntryRemind) {/*入职周年提醒*/
                int entryDialogStyle = Util.getIntValue(settings.getEntryDialogStyle(), 1);//弹窗图片
                String entryCongrats = Util.null2String(settings.getEntryCongrats());//祝福词
                entryCongrats = entryCongrats.replace("$years$", "" + yearNum);//显示年份
                entryCongrats = entryCongrats.replace("$name$", user.getLastname());//显示姓名
                String[] entryCongratsArr = entryCongrats.split("<br>");
                String entryCongratsColor = Util.null2String(settings.getEntryCongratsColor());//祝福词颜色
                String entryFont = Util.null2String(settings.getEntryFont());
                String entryFontSize = Util.null2String(settings.getEntryFontSize());
                if ("".equals(entryCongratsColor)) {
                    entryCongratsColor = "3b486d";
                }
                String url = "/images_face/ecologyFace_1/entryRemind.png";
                int rowIndex = 0;
                RecordSet recordSet = new RecordSet();
                String sql = "select docid,docname from HrmResourcefile where resourceid='0' and scopeId ='-98' and fieldid='-98' order by id";
                recordSet.executeQuery(sql);
                while (recordSet.next()) {
                    rowIndex++;
                    if (entryDialogStyle == rowIndex) {
                        url = "/weaver/weaver.file.FileDownload?fileid=" + Util.null2String(recordSet.getString("docid"));
                    }
                }
                DepartmentComInfo departmentComInfo = new DepartmentComInfo();
                SubCompanyComInfo subCompanyComInfo = new SubCompanyComInfo();
                String deptId = "" + user.getUserDepartment();
                String subcomId = "" + user.getUserSubCompany1();

                dataMap.put("bgimg", url);//弹窗使用的图片
                dataMap.put("congratulation", entryCongratsArr);//祝词
                dataMap.put("textcolor", entryCongratsColor);//祝词颜色
                dataMap.put("lastname", user.getLastname());//姓名
                dataMap.put("font", getEntryFontName(entryFont));//祝词字体
                dataMap.put("fontsize", entryFontSize);//祝词字号
                resultMap.put("datas", dataMap);
            }else{/*试用期提醒*/
                RecordSet rs = new RecordSet();
                rs.execute("select * from uf_probationremind where isValid=0 order by id desc");
                if(rs.next()){
                    String probationRemindDate = getProbationRemindDate(user);
                    if(!probationRemindDate.equals(today)){/*今天不提醒*/
                        resultMap.put("datas", "");
                        return resultMap;
                    }

                    String congrats = "<br>["+ Util.null2String(rs.getString("congrats"))+"]";
                    congrats = Util.fromScreen(congrats, user.getLanguage());
                    String[] entryCongratsArr = congrats.split("<br>");
                    String entryCongratsColor = Util.null2String(rs.getString("fontcolor"));
                    if ("".equals(entryCongratsColor)) {
                        entryCongratsColor = "3b486d";
                    }
                    String entryFont = Util.null2String(rs.getString("font"));
                    String entryFontSize = Util.null2String(rs.getString("fontsize"));

                    String url = "/images_face/ecologyFace_1/entryRemind.png";
                    String imgId = Util.null2String(rs.getString("image"));
                    if(!imgId.equals(""))
                        url = "/weaver/weaver.file.FileDownload?fileid=" + ImageFileUtils.getImageFileId(imgId);

                    dataMap.put("bgimg", url);//弹窗使用的图片
                    dataMap.put("congratulation", entryCongratsArr);//祝词
                    dataMap.put("textcolor", entryCongratsColor);//祝词颜色
                    dataMap.put("lastname", user.getLastname());//姓名
                    dataMap.put("font", getEntryFontName(entryFont));//祝词字体
                    dataMap.put("fontsize", entryFontSize);//祝词字号
                    resultMap.put("datas", dataMap);
                }else{/*没有最新有效的入职提醒*/
                    resultMap.put("datas", "");
                    return resultMap;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取人员的入职时间
     * @param user
     * @return
     */
    private int getLenOfEntry(User user){
        String today = TimeUtil.getCurrentDateString();//今天的日期
        String companyStartDate = "";//人员的入职日期
        int lenOfEntry = 0;//入职日期的时长
        String sql = "select companyStartDate from HrmResource where id="+user.getUID();
        RecordSet recordSet = new RecordSet();
        recordSet.executeQuery(sql);
        if(recordSet.next()){
            companyStartDate = recordSet.getString("companyStartDate");
        }
        lenOfEntry = DateUtil.dayDiff(companyStartDate,today);

        List<String> inList = new ArrayList<String>();
        List<String> outList = new ArrayList<String>();
        String type = "";//人员状态变更类型
        String changeDate = "";//人员状态变更日期
        sql = "select changedate,type_n from HrmStatusHistory where resourceid="+user.getUID()+" order by id asc";
        recordSet.executeQuery(sql);
        while(recordSet.next()){
            type = recordSet.getString("type_n");
            changeDate = recordSet.getString("changedate");
            if(type.equals("6")||type.equals("1")||type.equals("5")||type.equals("9")){
                outList.add(changeDate);
            }else if(type.equals("7")){
                inList.add(changeDate);
            }
        }
        int num = outList.size()>inList.size()?inList.size():outList.size();//可能存在离职（退休、解聘等）操作和返聘操作没有成对出现的情况
        for (int i=0;i<num;i++){
            int temp = DateUtil.dayDiff(outList.get(i),inList.get(i));//该人员离职（退休、解聘等）不在公司的时间
            if(temp>0){
                lenOfEntry = lenOfEntry - temp;//创建日期至今的时间减去不在公司的时间
            }
        }
        return lenOfEntry;
    }

    /**
     * 获取人员的入职日期
     * 排除掉外部人员
     * yyyy-MM-dd
     * @param user
     * @return
     */
    private String getCompanyStratDate(User user){
        String companyStartDate = "";//人员账号创建日期
        RecordSet recordSet = new RecordSet();
        String sql = "select companyStartDate from HrmResource where id="+user.getUID();
        if (recordSet.getDBType().equals("oracle")){
            sql += " and departmentid is not null and departmentid !=0 " +
                    " and subcompanyid1 is not null and subcompanyid1 !=0 ";
        }else {
            sql += " and departmentid is not null and departmentid <> '' and departmentid !=0 " +
                    " and subcompanyid1 is not null and subcompanyid1 <> '' and subcompanyid1 !=0 ";
        }
        recordSet.executeQuery(sql);
        if(recordSet.next()){
            companyStartDate = recordSet.getString("companyStartDate");
        }
        return companyStartDate;
    }

    private String getEntryFontName(String entryFont){
        String entryFontName = "Britannic";
        if(entryFont.equalsIgnoreCase("0")){
            entryFontName = "Britannic";
        }else if(entryFont.equalsIgnoreCase("1")){
            entryFontName = SystemEnv.getHtmlLabelName(84210,user.getLanguage());
        }else if(entryFont.equalsIgnoreCase("2")){
            entryFontName = SystemEnv.getHtmlLabelName(16190,user.getLanguage());
        }else if(entryFont.equalsIgnoreCase("3")){
            entryFontName = SystemEnv.getHtmlLabelName(16191,user.getLanguage());
        }else if(entryFont.equalsIgnoreCase("4")){
            entryFontName = SystemEnv.getHtmlLabelName(16192,user.getLanguage());
        }else if(entryFont.equalsIgnoreCase("5")){
            entryFontName = SystemEnv.getHtmlLabelName(16193,user.getLanguage());
        }else if(entryFont.equalsIgnoreCase("6")){
            entryFontName = SystemEnv.getHtmlLabelName(16194,user.getLanguage());
        }else if(entryFont.equalsIgnoreCase("7")){
            entryFontName = SystemEnv.getHtmlLabelName(16195,user.getLanguage());
        }else if(entryFont.equalsIgnoreCase("8")){
            entryFontName = SystemEnv.getHtmlLabelName(16196,user.getLanguage());
        }
        return entryFontName;
    }

    /**
     * 获取试用期提醒日期
     * @param user
     * @return
     */
    private String getProbationRemindDate(User user){
        RecordSet rs = new RecordSet();
        rs.execute("select companyStartDate from hrmresource where id="+user.getUID());
        if(rs.next()){
            String companyStartDate = Util.null2String(rs.getString(1));
            if(companyStartDate.length()==10){
                Calendar cal = DateUtil.getCalendar(companyStartDate);
                DateUtil.addMonth(cal,5);
                DateUtil.addDay(cal,-1);
                return DateUtil.getCalendarDate(cal);
            }
        }
        return "";
    }



}
