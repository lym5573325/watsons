package weaver.interfaces.lym.hr;

import weaver.conn.RecordSet;
import weaver.formmode.data.ModeDataIdUpdate;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.interfaces.lym.formmode.HcMode;
import weaver.interfaces.lym.formmode.HcjcMode;
import weaver.interfaces.lym.util.CalendarMethods;
import weaver.interfaces.lym.util.WorkflowToolMethods;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * 调用审批
 * 1.如果调动流程选择带HC，则HC台账的Transfer with HC字段更新为1，不带HC更新为-1；且选择带HC，该员工的HC code变成新部门的HC code最大值+1，
 * 原部门的该HC code的状态（Recruitment Status和HC Now Status）变更cancel。
 */
public class TranferApproveAction extends BaseBean implements Action {

    public String getSfdhcfield() {
        return sfdhcfield;
    }

    public void setSfdhcfield(String sfdhcfield) {
        this.sfdhcfield = sfdhcfield;
    }

    public String getYhcfield() {
        return yhcfield;
    }

    public void setYhcfield(String yhcfield) {
        this.yhcfield = yhcfield;
    }

    public String getXhcfield() {
        return xhcfield;
    }

    public void setXhcfield(String xhcfield) {
        this.xhcfield = xhcfield;
    }

    //是否带HC
    private String sfdhcfield;
    //原HC
    private String yhcfield;
    //新hc
    private String xhcfield;



    @Override
    public String execute(RequestInfo request) {
        String requestid = request.getRequestid();//获取本次提交的请求Id
        String workflowid = request.getWorkflowid();//获取流程ID
        String formtable = "formtable_main_"+(request.getRequestManager().getFormid()*-1);
        String currentDate = TimeUtil.getCurrentDateString();

        RecordSet rs = new RecordSet();

        writeLog("调用审批(归档) Action!	requestid:" + requestid + " workflowid: " + workflowid);
        Map<String, String> mainTableInfo = WorkflowToolMethods.getMainTableInfo(request);

        int yhc = Util.getIntValue(mainTableInfo.get(yhcfield));//原HC
        int xhc = Util.getIntValue(mainTableInfo.get(xhcfield));//原HC
        //申请人
        int sqr = Util.getIntValue(mainTableInfo.get("applier"));
        String sxrq = Util.null2String(mainTableInfo.get(""));//生效日期
        int mkt = Util.getIntValue(mainTableInfo.get("mkt"));
        boolean isBeforeSxrq = TimeUtil.dateInterval(currentDate, sxrq)>0 ? true : false;//是否在生效日期之前 true=>是
        TimeUtil.dateInterval(TimeUtil.getCurrentDateString(),sxrq);
        writeLog("yhc:"+yhc+"   xhc:"+xhc);

        if(Util.getIntValue(mainTableInfo.get(sfdhcfield))==0){//是否带编制==>是
            writeLog("带HC");
            //新增一条HC台账数据（复制原来的那条），HC code变成新部门的HC code最大值+1
            rs.execute("select * from " + HcMode.tableName +"  where id="+yhc);
            if(rs.next()){

                /*新成成的hccode*/
                int hcjckfy = Util.getIntValue(mainTableInfo.get("hcjc"));
                int hccodekfs = HcMode.getMaxxhByHcjc(hcjckfy,mkt)+1;
                String xh = hccodekfs<1000? new DecimalFormat("000").format(hccodekfs) : hccodekfs+"";
                String hccode = HcjcMode.getHcjc(hcjckfy) + xh;
                writeLog("hcjckfy:"+hcjckfy+"   xh:"+xh+"    hccode："+hccode+"    recruitmentstatus:"+Util.null2String(rs.getString("recruitmentstatus")));

                /**
                 * recuriter要取新部门的（新部门及组别New Dept. & Sub-function	）recuriter，
                 * Offer Received Date=流程审批结束日期，
                 * Title=新岗位，
                 * Position Grade=新职级，
                 * Sub-Function (for HRMS)=流程的新部门及组别New Dept. & Sub-function，
                 * Target Lead Time (inmth）=新职级对应的数据
                 */
                String recuriter = mainTableInfo.get("");//新部门Recuriter
                String offerreceiveddate = mainTableInfo.get("");//Offer Received Date=流程审批结束日期
                String title = mainTableInfo.get("");
                String positiongrade = mainTableInfo.get("");
                String subfunctionforhrms = mainTableInfo.get("");
                String targetleadtimeinmth = mainTableInfo.get("");

                /**
                 * 1.生效日期之前，新的HC的recuriter_status=hiring，
                 * 旧的HC的recuriter_status=ON board，
                 *  leaver=被调动人员，
                 *  leavering-date=生效日期
                 * 2.生效日期当天(生效日期或者生效日期之后)，
                 * 新的HC的recuriter_status=ON board，
                 * 旧的HC的recuriter_status=Cancel，
                 * leaver和leavering-date清空，
                 * 更新到ex-leaver和ex-leaver-date，
                 * Ex-Grade、Staff Type（Leaver）根据Ex-Leavers带出，
                 * Leaver相关的字段清空。
                 */
                /*new HC*/
                String recruitmentstatus = "";
                /*old HC*/
                String recruitmentstatus_old = "";
                String leavers_lod = "";
                String leavingDate_old = "";
                String ex_leaver_old = "";
                String ex_leaverDate_old = "";
                String ex_grade_old = "";
                String staffType_leaver_old = "";

                if(isBeforeSxrq){//生效日期before
                    recruitmentstatus = "0";//hiring
                    recruitmentstatus_old = "2";//on board
                    leavers_lod = mainTableInfo.get("");//被调动人
                    leavingDate_old = sxrq;
                }else{//生效日期after
                    recruitmentstatus = "2";//on board
                    recruitmentstatus_old = "4";//cancel
                    leavers_lod = "";
                    leavingDate_old = "";
                    ex_leaver_old = Util.null2String(rs.getString(""));//原HC.leaver
                    ex_leaverDate_old = Util.null2String(rs.getString(""));//原HC.leaverDate
                }

                String transferwithhc="1";

                /*生成新的hc*/
                ModeDataIdUpdate idUpdate = new ModeDataIdUpdate();
                int billid = idUpdate.getModeDataNewId(HcMode.tableName, HcMode.modeid, sqr, 1, CalendarMethods.getCurrentDate(), CalendarMethods.getCurrentTime2());
                rs.executeUpdate("update " + HcMode.tableName + " set " +
                        "year=?,month=?,recuriter2=?,budgetyear=?,budget=?,transferwithhc=?,mrreceiveddate=?,empcode=?,name=?,departmentfororacle=?,departmentforsummary=?," +
                        "departmentforfin=?,offerreceiveddate=?,onboarddate=?,title=?,stafftype=?,positiongrade=?,subfunctionforhrms=?,costcenter=?," +
                        "targetleadtimeinmth=?,targetleadtimeindate=?,actualleadtimeinmth=?,leavers=?,leavingdate=?,hcenddate=?," +
                        "remark=?,province=?,city=?,stafftype1=?,exleavers=?,exleavedate=?,exgrade=?,recruitmentstatus=?,hcnowstatus=?,hcjckfy=?,hccodekfy=?,hccode=? where id=?",
                        Util.null2String(rs.getString("year")),Util.null2String(rs.getString("month")),Util.null2String(rs.getString("recuriter2")),
                        Util.null2String(rs.getString("budgetyear")),Util.null2String(rs.getString("budget")),transferwithhc+"",
                        Util.null2String(rs.getString("mrreceiveddate")),Util.null2String(rs.getString("empcode")),Util.null2String(rs.getString("name")),
                        Util.null2String(rs.getString("departmentfororacle")),Util.null2String(rs.getString("departmentforsummary")),Util.null2String(rs.getString("departmentforfin")),
                        Util.null2String(rs.getString("offerreceiveddate")),Util.null2String(rs.getString("onboarddate")),Util.null2String(rs.getString("title")),
                        Util.null2String(rs.getString("stafftype")),Util.null2String(rs.getString("positiongrade")),Util.null2String(rs.getString("subfunctionforhrms")),
                        Util.null2String(rs.getString("costcenter")),Util.null2String(rs.getString("targetleadtimeinmth")),Util.null2String(rs.getString("targetleadtimeindate")),
                        Util.null2String(rs.getString("actualleadtimeinmth")),Util.null2String(rs.getString("leavers")),Util.null2String(rs.getString("leavingdate")),
                        Util.null2String(rs.getString("hcenddate")),Util.null2String(rs.getString("remark")),Util.null2String(rs.getString("province")),
                        Util.null2String(rs.getString("city")),Util.null2String(rs.getString("stafftype1")),Util.null2String(rs.getString("exleavers")),
                        Util.null2String(rs.getString("exleavedate")),Util.null2String(rs.getString("exgrade")),recruitmentstatus,
                        Util.null2String(rs.getString("hcnowstatus")),hcjckfy,hccodekfs,hccode,billid+"");
                //更新表单建模 权限
                new ModeRightInfo().editModeDataShare(1, HcMode.modeid, billid);
                rs.execute("update " + formtable + " set " + xhcfield + "='"+billid+"' where requestid="+requestid);


                /*更新旧的*/
                rs.executeUpdate("update " + HcMode.tableName + " set ");
            }

        }else{//是否带编制==>否

            String name = mainTableInfo.get("dzrytransferemploypee");//被调动人姓名
            String empcode = mainTableInfo.get("staffno");//工号
            String departmentfororacle = mainTableInfo.get("xbmjzb");//新部门
            String positiongrade = mainTableInfo.get("xzj");//新职级
            String title = mainTableInfo.get("xzw");//新岗位
            String recruitmentstatus = "3";//pending
            rs.executeUpdate("update " + HcMode.tableName + " set name=?,empcode=?,departmentfororacle=?,positiongrade=?," +
                    "title=?,recruitmentstatus=? where id=?",name,empcode,departmentfororacle,positiongrade,title,recruitmentstatus,xhc);

        }

        //清空old hc的name
        rs.execute("update " + HcMode.tableName + " set name=null where id="+yhc);

        //原部门的该HC code的状态（Recruitment Status和HC Now Status）变更cancel(4)。  transferwithhc更新为-1
        rs.executeUpdate("update " + HcMode.tableName + " set  recruitmentstatus=?,transferwithhc=?  where id=?", "4", "-1", yhc);
        HcMode.updateHcNowStatusByRecuitmentStatus(yhc);
        HcMode.updateHcNowStatusByRecuitmentStatus(xhc);

        return Action.SUCCESS;
    }

    private String getDeptMatrix(String field, int dimension){
        RecordSet rs = new RecordSet();
        rs.execute("select " + field + " from matrixtable_2  where id="+dimension);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }
}
