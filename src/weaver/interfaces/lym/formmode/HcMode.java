package weaver.interfaces.lym.formmode;

import weaver.common.DateUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.util.CalendarMethods;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    public static int getMaxxhByHcjc(int hcjc,int mkt){
        RecordSet rs = new RecordSet();
        rs.execute("select hccodekfy as xh   from  "+ tableName + " where hcjckfy = '" + hcjc +"' and mkt='"+mkt+"' and NVL(hccodekfy,0)>0  order by hccodekfy desc");
        return rs.next()? Util.getIntValue(rs.getString("xh"),0) : 0;
    }


    /**
     * 获取最大需要
     * @param staffType 员工性质
     * @param hcjc  HC简称
     * @return
     */
    public static int getMaxxh(int staffType,int hcjc, int mkt){
        RecordSet rs = new RecordSet();
        if(staffType==21 || staffType==23){//残疾人 disable  ||  实习生
            rs.execute("select hccodekfy as xh   from  "+ tableName + " where stafftype='"+staffType+"' and mkt='"+mkt+"' and NVL(hccodekfy,0)>0  order by hccodekfy desc");
        }else{//其他
            rs.execute("select hccodekfy as xh   from  "+ tableName + " where hcjckfy = '" + hcjc +"' and mkt='"+mkt+"'  and NVL(hccodekfy,0)>0  order by hccodekfy desc");
        }
        return rs.next()? Util.getIntValue(rs.getString("xh"),0) : 0;
    }

    public static String getMaxHccode(int mkt,int staffType,int hcjc){
        String hccode = "";
        new BaseBean().writeLog("getMaxHccode==>   mkt:"+mkt+"    staffType:"+staffType+"     hcjc:"+hcjc);
        String mktName = MktMode.getMarketName(mkt);
        int xh = getMaxxh(staffType,hcjc,mkt) + 1;
        String xh_ = xh<1000? new DecimalFormat("000").format(xh) : xh+"";
        String hcjcName = HcjcMode.getHcjc(hcjc);
        new BaseBean().writeLog("getMaxHccode==>   mktName:"+mktName+"    xh:"+xh+"     hcjcName:"+hcjcName);
        if(staffType==21)   hccode = mktName + "Disable" + xh_;//残疾人      MKT+"Disable"+最大序号+1
        else if(staffType==23)  hccode = mktName + "Intern" + xh_;//实习生      MKT+"Intern"+最大序号+1
        else hccode = mktName + hcjcName + xh_;
        new BaseBean().writeLog("getMaxHccode   return ==>"+hccode);
        return hccode;
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

    public static String getTargetLeadTime(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select targetleadtimeinmth from " + tableName + " where id="+billid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }

    public static String getTargetLeadTimeIndate(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select targetleadtimeindate from " + tableName + " where id="+billid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }

    /**
     * 根据日期、职级计算getTargetTime
     * @param date  日期
     * @param grade 职级
     * @return  返回计算结果日期=date + 30*grade对应的月份
     */
    public static String getTargetTime(String date,int grade){
        float targetleadtimeinmth = ZjdzbMode.getTargetleadtimeByGrade(grade);
        return getTargetTime(date,targetleadtimeinmth);
    }

    public static String getTargetTime(String date,float targetleadtimeinmth){
        String targetleadtimeindate = "";
        if(date.length()==10 && targetleadtimeinmth>0){
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(CalendarMethods.dateFormat.parse(date));
                cal.add(cal.DATE,(int)(targetleadtimeinmth*30));
                targetleadtimeindate = CalendarMethods.dateFormat.format(cal.getTime());
            }catch(Exception e){}
        }
        return targetleadtimeindate;
    }

    public static boolean updateTargetTimeById(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select mrreceiveddate from " + tableName + " where id="+billid);
        if(rs.next())
            return rs.executeUpdate("update " + tableName + " set targetleadtimeindate=? where id=?", getTargetTime(Util.null2String(rs.getString("mrreceiveddate")), getGradeById(billid)), billid + "");
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

    /**
     * 根据offerReceiveDate-Target Lead Time (in Date)/30计算Actual Lead time (in mth)
     * @param billid
     * @param offerReceivedDate
     * @return
     */
    public static float countActualleadTimeInmth(int billid,String offerReceivedDate){
        return (new BigDecimal((float) DateUtil.dayDiff(getTargetLeadTimeIndate(billid),offerReceivedDate)/30).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
    }

}
