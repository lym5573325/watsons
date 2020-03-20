package weaver.interfaces.lym.formmode;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.CalendarMethods;

import java.util.Calendar;

/**
 * HC台账
 */
public class HcMode {

    public final static String tableName = "uf_hc";
    public final static int modeid = 1001;

    /**
     * 根据HC简称获取当前最大的序号
     * @return
     */
    public static int getMaxxhByHcjc(int hcjc){
        RecordSet rs = new RecordSet();
        rs.execute("select hccodekfy as xh   from  "+ tableName + " where hcjckfy = '" + hcjc +"' and xh is not null order by hccodekfy desc");
        return rs.next()? Util.getIntValue(rs.getString("xh"),0) : 0;
    }

    /**
     * 根据ID获取MR审批完成时间
     * @param billid
     * @return
     */
    public static String getMrreceiveddateById(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select mrreceiveddate from " + tableName + " where id="+billid);
        return rs.next()? Util.null2String(rs.getString("mrreceiveddate")) : "";
    }

    public static int getGradeById(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select positiongrade from " + tableName + " where id="+billid);
        return rs.next()? Util.getIntValue(rs.getString("positiongrade"),0) : 0;
    }

    /**
     * 根据日期、职级计算targerTime
     * @param date  日期
     * @param grade 职级
     * @return  返回计算结果日期=date + 30*grade对应的月份
     */
    public static String getTargertime(String date,int grade){
        String targetleadtimeindate = "";
        float targetleadtimeinmth = ZjdzbMode.getTargetleadtimeByGrade(grade);
        if(date.length()==10 && targetleadtimeinmth>0){
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(CalendarMethods.dateFormat.parse(date));
                cal.add(cal.DATE,(int)targetleadtimeinmth*30);
                targetleadtimeindate = CalendarMethods.dateFormat.format(cal.getTime());
            }catch(Exception e){}
        }
        return targetleadtimeindate;
    }

    public static boolean updateTargetTimeById(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select mrreceiveddate from " + tableName + " where id="+billid);
        if(rs.next())
            return rs.executeUpdate("update " + tableName + " set targetleadtimeindate=? where id=?", getTargertime(Util.null2String(rs.getString("mrreceiveddate")), getGradeById(billid)), billid + "");
        else
            return false;
    }

    /**
     * 根据RecuitmentStatus更新HcNowStatus
     * @param billid    数据ID
     * @return
     */
    public static boolean updateHcNowStatusByRecuitmentStatus(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select recruitmentstatus from " + tableName + " where id="+billid);
        if(rs.next()){
            int hcnowstatus = getHcNowStatusByRecuitmentStatus(Util.getIntValue(rs.getString("recruitmentstatus")));
            return rs.executeUpdate("update " + tableName +" set hcnowstatus=? where id=?", hcnowstatus, billid);
        }
        return false;
    }

    /**
     * 根据RecuitmentStatus获取对应HcNowStatus
     * 1、 Recruitment Status= hiring或者offering，则HC Now Status= hiring
     * 2、 Recruitment Status= on board，则HC Now Status= Existing
     * 3、 Recruitment Status= pending，则HC Now Status= pending
     * 4、 Recruitment Status= cancel，则HC Now Status= cancel
     * 5、 Recruitment Status= end，则HC Now Status= end
     * @param recruitmentStatus
     * @return
     */
    public static int getHcNowStatusByRecuitmentStatus(int recruitmentStatus){
        //on board->Existing
        if(recruitmentStatus==2) return 0;
        //Hiring || offering->Hiring
        else if(recruitmentStatus==0 || recruitmentStatus==1) return 1;
        //pending->Pending
        else if(recruitmentStatus==3) return 2;
        //cancel->Cancel
        else if(recruitmentStatus==4) return 3;
        //end->End
        else if (recruitmentStatus==5)return 4;
        else return -1;
    }

}
