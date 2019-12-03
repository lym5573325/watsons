package weaver.interfaces.lym.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarMethods {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	public static String getCurrentDate(){
		return dateFormat.format(new Date()).substring(0,10);
	}

	public static String getCurrentTime2(){
		return timeFormat2.format(new Date());
	}

	/**
	 * 加减日期（yyyy-MM-dd）
	 * @param date 原时间(yyyy-MM-dd)
	 * @param nums 加减的时间
	 * @param type 加减的类型 1->分钟	2->小时	3->天	4->月	5->年
	 * @return
	 */
	public static String dateOperation(String date,int nums,int type){
		String result = "";
		try{
			result = dateFormat.format(dateOperation(dateFormat.parse(date),nums,type));
		}catch(ParseException e){}
		return result;
	}

	/**
	 * 加减时间
	 * @param date 原时间
	 * @param nums 加减的时间
	 * @param type 加减的类型 1->分钟	2->小时	3->天	4->月	5->年
	 * @return
	 */
	public static Date dateOperation(Date date,int nums,int type) {
		Calendar calendar=Calendar.getInstance();
		switch (type) {
		case 1:
			calendar.setTime(new Date(date.getTime()+(nums*60*1000)));
			break;
		case 2:
			calendar.setTime(new Date(date.getTime()+(nums*60*60*1000)));
			break;
		case 3:
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, nums);
			break;
		case 4:
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, nums);
			break;
		case 5:
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, nums);
			break;

		default:
			break;
		}
		return calendar.getTime();
	}
	
	/* 比较两个日期的大小
	 * @rq1 日期1的日期
	 * @sj1 日期1的时间
	 * @rq2 日期2的日期
	 * @sj2 日期2的时间 
	 * 返回0为日期1 小于或等于 日期2 	返回1为大于
	 * */
	public static String compareDateTime(String rq1,String sj1,String rq2,String sj2) throws ParseException {
		String result="0";
		
		String date1=rq1+" "+sj1;
		String date2=rq2+" "+sj2;
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		long temp=dateFormat.parse(date1).getTime()-dateFormat.parse(date2).getTime();
		
		if(temp>0) {
			result="1";
		}
		
		return result;
	}

	
	/*
	 * 计算日期1 减 日期2的
	 * @rq1 日期1的日期
	 * @sj1 日期1的时间
	 * @rq2 日期2的日期
	 * @sj2 日期2的时间
	 * @type 返回类型	1==>小时	2==>天
	 * @return
	 * */
	public static float datetimeSubtract(String rq1,String sj1,String rq2,String sj2,int type) throws ParseException {
		float result=0;
		
		String date1=rq1+" "+sj1;
		String date2=rq2+" "+sj2;
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		long temp=dateFormat.parse(date1).getTime()-dateFormat.parse(date2).getTime();

		if(type==1)	return result=new BigDecimal(String.format("%.1f", (float)temp/(3600*1000))).setScale(1,  BigDecimal.ROUND_HALF_UP).floatValue();
		else return new BigDecimal(String.format("%.1f", (float)temp/(3600*1000*24))).setScale(1,  BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public static float dateSubtract(String rq1,String rq2, int type) throws ParseException{
		return datetimeSubtract(rq1, "00:00",rq2, "00:00", type);
	}




	/**
	 * 计算2个日期之间相差的  相差多少年
	 * 比如：2011-02-02 到  2017-03-02 相差 6年
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	public static JSONObject dayComparePrecise(String fromDate,String toDate) throws ParseException, JSONException{
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	    Calendar  from  =  Calendar.getInstance();
	    from.setTime(df.parse(fromDate));
	    Calendar  to  =  Calendar.getInstance();
	    to.setTime(df.parse(toDate));

	    int fromYear = from.get(Calendar.YEAR);
	    int fromMonth = from.get(Calendar.MONTH);
	    int fromDay = from.get(Calendar.DAY_OF_MONTH);

	    int toYear = to.get(Calendar.YEAR);
	    int toMonth = to.get(Calendar.MONTH);
	    int toDay = to.get(Calendar.DAY_OF_MONTH);
	    int year = toYear  -  fromYear;
	    int month = toMonth  - fromMonth;
	    int day = toDay  - fromDay;
	    if(month<0 || (month==0 && day<0)) {
	    	year -= 1;
	    }
	    JSONObject resultjson=new JSONObject();
	    resultjson.put("year", year);
	    return resultjson;
	}
	
	/**
	 * 获取两个日期之间的所有日期(包括开始、结束日期)
	 * @param startTime	开始日期	格式yyyy-MM-dd
	 * @param endTime 结束日期	格式yyyy-MM-dd
	 * @return 日期List数组
	 */
	public static List<String> getDays(String startTime,String endTime){
		List<String> days = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);
			if(end.getTime() < start.getTime()) {
				Date temp = start;
				start = end;
				end = temp;
			}
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(start);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(end);
			endCal.add(Calendar.DAY_OF_YEAR, 1);//包含结束日期
			while(startCal.before(endCal)) {
				days.add(dateFormat.format(startCal.getTime()));
				startCal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}catch(ParseException e) {
			e.printStackTrace();
		}
		return days;
	}
	
	/**
	 * 获取当年月最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public String getLastDayOfMonth(String year,String month) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(dateformat.parse(year+"-"+month+"-01"));
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
		} catch (ParseException e) {
			return "";
		}
		return dateformat.format(cal.getTime());
	}

	/**
	 * 获取月份最大天数
	 * @param year 年份
	 * @param month 月份
	 * @return 当前年份和月份最大天数
	 */
	public int getMonthMaxDay(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 0); 	//	输入类型为int类型
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取两日日期相差的月份
	 */
	public static int monthsBetween(String startDate, String endDate){
		return (Integer.parseInt(endDate.substring(0,4)) - Integer.parseInt(startDate.substring(0,4))) * 12 +  (Integer.parseInt(endDate.substring(5,7)) - Integer.parseInt(startDate.substring(5,7)));
	}
}
