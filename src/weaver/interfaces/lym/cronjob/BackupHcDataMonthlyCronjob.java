package weaver.interfaces.lym.cronjob;

import weaver.conn.RecordSet;
import weaver.formmode.data.ModeDataIdUpdate;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.formmode.HcBackupMode;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.formmode.YearMode;
import weaver.interfaces.lym.util.CalendarMethods;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 备份上个月的HC数据
 * 1.HC台账快照开发，每个月5号晚上24点，自动将当前HC的台账的数据复制一份出来存入快照，
 * 其中year和month字段为上一个月。举例2020年1月5日，生成的快照数据，year=2019年，month=12月
 */
public class BackupHcDataMonthlyCronjob extends BaseCronJob {

    public void execute() {
        RecordSet rs = new RecordSet();

        //一个月前
        String beforeOneMonth = CalendarMethods.dateOperation(CalendarMethods.getCurrentDate(),-1,4);
        String year = beforeOneMonth.substring(0,4);
        String month = Integer.parseInt(beforeOneMonth.substring(5,7))+"";
        new BaseBean().writeLog("year:"+year+"    month:"+month);
        year = YearMode.getIdByYear(year);
        new BaseBean().writeLog("2222:year:"+year+"    month:"+month);
        rs.execute("select * from  " + HcMode.tableName + " where year = '"+year+"' and month = '"+month+"'");
        while(rs.next()){
            ModeDataIdUpdate idUpdate = new ModeDataIdUpdate();
            int billid = idUpdate.getModeDataNewId(HcBackupMode.tableName, HcBackupMode.modeid, 1, 1, CalendarMethods.getCurrentDate(), CalendarMethods.getCurrentTime2());
            rs.executeUpdate("update " + HcBackupMode.tableName + " set " +
                            "year=?,month=?,recuriter=?,budgetyear=?,budget=?,transferwithhc=?,mrreceiveddate=?,empcode=?,name=?,departmentfororacle=?,departmentforsummary=?," +
                            "departmentforfin=?,offerreceiveddate=?,onboarddate=?,title=?,stafftype=?,positiongrade=?,subfunctionforhrms=?,costcenter=?," +
                            "targetleadtimeinmth=?,targetleadtimeindate=?,actualleadtimeinmth=?,leavers=?,leavingdate=?,hcenddate=?," +
                            "remark=?,province=?,city=?,stafftype1=?,exleavers=?,exleavedate=?,exgrade=?,recruitmentstatus=?,hcnowstatus=?,hcjckfy=?,hccodekfy=?,hccode=? where id=?",
                    Util.null2String(rs.getString("year")),Util.null2String(rs.getString("month")),Util.null2String(rs.getString("recuriter2")),
                    Util.null2String(rs.getString("budgetyear")),Util.null2String(rs.getString("budget")),Util.null2String(rs.getString("transferwithhc"))+"",
                    Util.null2String(rs.getString("mrreceiveddate")),Util.null2String(rs.getString("empcode")),Util.null2String(rs.getString("name")),
                    Util.null2String(rs.getString("departmentfororacle")),Util.null2String(rs.getString("departmentforsummary")),Util.null2String(rs.getString("departmentforfin")),
                    Util.null2String(rs.getString("offerreceiveddate")),Util.null2String(rs.getString("onboarddate")),Util.null2String(rs.getString("title")),
                    Util.null2String(rs.getString("stafftype")),Util.null2String(rs.getString("positiongrade")),Util.null2String(rs.getString("subfunctionforhrms")),
                    Util.null2String(rs.getString("costcenter")),Util.null2String(rs.getString("targetleadtimeinmth")),Util.null2String(rs.getString("targetleadtimeindate")),
                    Util.null2String(rs.getString("actualleadtimeinmth")),Util.null2String(rs.getString("leavers")),Util.null2String(rs.getString("leavingdate")),
                    Util.null2String(rs.getString("hcenddate")),Util.null2String(rs.getString("remark")),Util.null2String(rs.getString("province")),
                    Util.null2String(rs.getString("city")),Util.null2String(rs.getString("stafftype1")),Util.null2String(rs.getString("exleavers")),
                    Util.null2String(rs.getString("exleavedate")),Util.null2String(rs.getString("exgrade")),Util.null2String(rs.getString("recruitmentstatus")),
                    Util.null2String(rs.getString("hcnowstatus")),Util.null2String(rs.getString("hcjckfy")),Util.null2String(rs.getString("hccodekfs")),
                    Util.null2String(rs.getString("hccode")),billid+"");
            //更新表单建模 权限
            new ModeRightInfo().editModeDataShare(1, HcBackupMode.modeid, billid);
        }
    }

}
