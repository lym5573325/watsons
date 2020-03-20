package weaver.interfaces.lym.cronjob;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.util.CalendarMethods;
import weaver.interfaces.schedule.BaseCronJob;

import java.util.Calendar;
import java.util.Date;

/**
 * Recruitment Status( Active HC Only)，根据入职流程的入职日期，当日晚上12点更新为on board
 * 1.例如2020-01-02遍历2020-01-01的数据
 */
public class UpdateHcRecstatusCronjob extends BaseCronJob {

    public void execute() {
        RecordSet rs = new RecordSet();
        //昨天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,-1);
        String yesterday = CalendarMethods.dateFormat.format(calendar.getTime());
        new BaseBean().writeLog("根据入职流程的入职日期，当日晚上12点更新为on board         yesterday:"+yesterday);
        rs.execute("update " + HcMode.tableName + "set recruitmentstatus=2 where onboarddate='"+yesterday+"'");
    }

}
