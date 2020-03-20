package weaver.interfaces.lym.hr;

import weaver.conn.RecordSet;
import weaver.formmode.data.ModeDataIdUpdate;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.formmode.HcjcMode;
import weaver.interfaces.lym.formmode.ZjdzbMode;
import weaver.interfaces.lym.util.CalendarMethods;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * 人员增补申请流程归档接口
 * ===============================================================================================================
 * 1.将流程数据转入到HC台账(uf_hc)
 *      如果用工需求申请的增补类别=非预算编制(2)，人员增补申请流程归档后按部门最大的HC CODE+1生成(招聘人数)条新的CODE记录
 *      需要生成的字段：
 *      1、Recuriter（部门矩阵的Recuriter）                     ==>recuriter
 *      2、HC Code（根据招聘人数 Hiring HC生成对应条数的HC）        hccode
 *      3、Budget Year（MR的预算年度 Budget Year）                  budgetyear
 *      4、HC Now Status（等于hiring）   （固定=1）                  hcnowstatus
 *      5、Budget（等于Non-Budget）       （固定=1）                          budget
 *      6、MR Received Date（MR审批完成日期）                        mrreceiveddate
 *      7、Department (for oracle)（MR的部门及组别 Dept. & Sub-function）            departmentfororacle
 *      8、Recruitment Status ( Active HC Only)（默认hiring==1）                    recruitmentstatus
 *      9、Title（MR的职位名称 Position）                                       title
 *      10、Staff Type（MR的用工性质 Employment Type（字段联动））                stafftype
 *      11、Position Grade（MR的职级 Grade）                                      positiongrade
 *      12、Target Lead Time (in mth)（根据职级对应表带出并赋值）  (从uf_zjdzb取)        targetleadtimeinmth
 *      13、Target Lead Time (in Date)（MR Received Date+字段12*30） （流程没有,根据12算出）       targetleadtimeindate
 *      14、City（MR的工作地点 Work Location）                                           city
 *      15、subfunctionforhrms
 * ===================================================================================================================
 * 在Manpower Requistition Approval流程结束后自动更新，更新规则是当增补类型为预算内编制，将流程的HC Code对应的数据进行更新：
 * 1、审批完成日期更新到MR Reciveice data
 * 2、Target Lead Time (in Date)更新为MR审批完成时间+lead time月数*30
 * 3、HC Now Status更新为hiring
 */
public class StaffAddApplyAction extends BaseBean implements Action {

    @Override
    public String execute(RequestInfo request) {
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID
        String formtable = "formtable_main_"+(request.getRequestManager().getFormid()*-1);

        new BaseBean().writeLog("人员增补申请(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        int sqr = Util.getIntValue(mainTableInfo.get("applier"));//申请人
        String zblb = mainTableInfo.get("typeofrequisition");//增补类别
        writeLog("sqr："+sqr+"    zblb:"+zblb);
        if(zblb.equals("2")){

            String billids="";//生成HCCODE.id（多个","分割）

            String recuriter = mainTableInfo.get("recuriter");
            String budgetyear = mainTableInfo.get("budgetyear");
            String hcnowstatus = "1";
            String budget = "1";
            String mrreceiveddate = mainTableInfo.get("spwcrq");
            String departmentfororacle = mainTableInfo.get("level5department");
            String recruitmentstatus = "0";
            String title = mainTableInfo.get("position");
            String stafftype = mainTableInfo.get("ygxzzdld");
            String positiongrade = mainTableInfo.get("grade");

            float targetleadtimeinmth = ZjdzbMode.getTargetleadtimeByGrade(Util.getIntValue(positiongrade,0));
            String targetleadtimeindate = "";
            if(mrreceiveddate.length()==10 && targetleadtimeinmth>0){
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(CalendarMethods.dateFormat.parse(mrreceiveddate));
                    cal.add(cal.DATE,(int)targetleadtimeinmth*30);
                    targetleadtimeindate = CalendarMethods.dateFormat.format(cal.getTime());
                }catch(Exception e){}
            }
            String city = mainTableInfo.get("worklocation");
            String subfunctionforhrms = mainTableInfo.get("deptsubfunction");
            writeLog("recuriter:"+recuriter+"   budgetyear:"+budgetyear+"   hcnowstatus:"+hcnowstatus+"   hcnowstatus:"+hcnowstatus+"   budget:"+budget
                    +"   mrreceiveddate:"+mrreceiveddate+"   departmentfororacle:"+departmentfororacle+"   recruitmentstatus:"+recruitmentstatus+"   title:"+title+"   stafftype:"+stafftype
                    +"   positiongrade:"+positiongrade+"   targetleadtimeinmth:"+targetleadtimeinmth+"   targetleadtimeindate:"+targetleadtimeindate);

            int zprs = Util.getIntValue(mainTableInfo.get("zprs"));//招聘人数
            int hcjc = Util.getIntValue(mainTableInfo.get("hcjc"),0);//HC简称——ID
            String hcjcName = HcjcMode.getHcjc(hcjc);//HC简称--中文
            int xh = HcMode.getMaxxhByHcjc(hcjc);//序号
            writeLog("zprs："+zprs+"    hcjc:"+hcjc+"    hcjcName:"+hcjcName+"    xh:"+xh );
            for(int i=0;i<zprs;i++){
                xh++;
                String xh_format ="";
                xh_format = xh<1000? new DecimalFormat("000").format(xh) : xh+"";
                String hccode = hcjcName + xh_format;//HRCODE

                /**
                 * 写入建模
                 */
                ModeDataIdUpdate idUpdate = new ModeDataIdUpdate();
                int billid = idUpdate.getModeDataNewId(HcMode.tableName, HcMode.modeid, 1, 1, CalendarMethods.getCurrentDate(), CalendarMethods.getCurrentTime2());
                updateMode(billid,recuriter,budgetyear,hcnowstatus,budget,mrreceiveddate,departmentfororacle,recruitmentstatus,title,stafftype,positiongrade,
                        targetleadtimeinmth+"",targetleadtimeindate,city,hccode,hcjc+"",xh+"",subfunctionforhrms);
                //更新表单建模 权限
                new ModeRightInfo().editModeDataShare(sqr, HcMode.modeid, billid);

                billids = billids+billid+",";
            }
            RecordSet rs = new RecordSet();
            if(billids.length()>0) billids = billids.substring(0,billids.length()-1);
            rs.execute("update " + formtable + " set hccode='"+billids+"'  where requestid="+requestid);
        }else if(zblb.equals("1")){//预算内编制
            //HCID
            String hcids = Util.null2String(mainTableInfo.get("hccode"));
            String[] hcidArray = hcids.split(",");
            for(int i=0;i<hcidArray.length;i++) {
                int hcid = Util.getIntValue(hcidArray[i]);
                //审批完成日期
                String spwcrq = mainTableInfo.get("spwcrq");

                String positiongrade = mainTableInfo.get("grade");
                float targetleadtimeinmth = ZjdzbMode.getTargetleadtimeByGrade(Util.getIntValue(positiongrade, 0));
                String targetleadtimeindate = "";
                if (spwcrq.length() == 10 && targetleadtimeinmth > 0) {
                    try {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(CalendarMethods.dateFormat.parse(spwcrq));
                        cal.add(cal.DATE, (int) targetleadtimeinmth * 30);
                        targetleadtimeindate = CalendarMethods.dateFormat.format(cal.getTime());
                    } catch (Exception e) {
                    }
                }
                writeLog("hcid:" + hcid + "    spwcrq:" + spwcrq + "    positiongrade:" + positiongrade + "    targetleadtimeinmth：" + targetleadtimeinmth + "    targetleadtimeindate:"+targetleadtimeindate);
                //Hiring
                String hcnowstatus = "1";
                RecordSet rs = new RecordSet();
                rs.executeUpdate("update " + HcMode.tableName + " set mrreceiveddate=?,targetleadtimeindate=?, hcnowstatus=? where id=?", spwcrq, targetleadtimeindate, hcnowstatus, hcid + "");
            }
        }


        return Action.SUCCESS;
    }

    public static boolean updateMode(int billid,String recuriter,String budgetyear,String hcnowstatus,String budget,String mrreceiveddate,
                                     String departmentfororacle,String recruitmentstatus,String title,String stafftype,String positiongrade,
                                     String targetleadtimeinmth,String targetleadtimeindate,String city,String hccode,String hcjckfy,String hccodekfy,String subfunctionforhrms){
        RecordSet rs = new RecordSet();
        return rs.executeUpdate("update " + HcMode.tableName + " set recuriter2=?,budgetyear=?,hcnowstatus=?,budget=?,mrreceiveddate=?,departmentfororacle=?," +
                "recruitmentstatus=?,title=?,stafftype=?,positiongrade=?,targetleadtimeinmth=?,targetleadtimeindate=?,city=?,hccode=?,hcjckfy=?,hccodekfy=?,subfunctionforhrms=?  where id=?",
                recuriter,budgetyear,hcnowstatus,budget,mrreceiveddate,departmentfororacle,recruitmentstatus,title,stafftype,positiongrade,targetleadtimeinmth,
                targetleadtimeindate,city,hccode,hcjckfy,hccodekfy,subfunctionforhrms,billid);
    }


}
