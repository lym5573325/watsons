package com.engine.sync.entity;

import java.util.ArrayList;
import java.util.List;

public class ResourceHrmsBean {

    public String getWorkcode() {
        return workcode;
    }

    public void setWorkcode(String workcode) {
        this.workcode = workcode;
    }


    public String getTempfield1() {
        return tempfield1;
    }

    public void setTempfield1(String tempfield1) {
        this.tempfield1 = tempfield1;
    }


    public String getCompanystartdate() {
        return companystartdate;
    }

    public void setCompanystartdate(String companystartdate) {
        this.companystartdate = companystartdate;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public void setJobtitleName(String jobtitleName) {
        this.jobtitleName = jobtitleName;
    }

    public String getDepartmentcode() {
        return departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode;
    }

    public String getTempfield2() {
        return tempfield2;
    }

    public void setTempfield2(String tempfield2) {
        this.tempfield2 = tempfield2;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getTempfield3() {
        return tempfield3;
    }

    public void setTempfield3(String tempfield3) {
        this.tempfield3 = tempfield3;
    }


    public String getTempfield4() {
        return tempfield4;
    }

    public void setTempfield4(String tempfield4) {
        this.tempfield4 = tempfield4;
    }


    public String getUsekind() {
        return usekind;
    }

    public void setUsekind(String usekind) {
        this.usekind = usekind;
    }


    public String getTempfield5() {
        return tempfield5;
    }

    public void setTempfield5(String tempfield5) {
        this.tempfield5 = tempfield5;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTempfield6() {
        return tempfield6;
    }

    public void setTempfield6(String tempfield6) {
        this.tempfield6 = tempfield6;
    }

    public String getTempfield7() {
        return tempfield7;
    }

    public void setTempfield7(String tempfield7) {
        this.tempfield7 = tempfield7;
    }

    public String getTempfield8() {
        return tempfield8;
    }

    public void setTempfield8(String tempfield8) {
        this.tempfield8 = tempfield8;
    }


    public String getTempfield9() {
        return tempfield9;
    }

    public void setTempfield9(String tempfield9) {
        this.tempfield9 = tempfield9;
    }

    public String getTempfield10() {
        return tempfield10;
    }

    public void setTempfield10(String tempfield10) {
        this.tempfield10 = tempfield10;
    }

    public String getTempfield11() {
        return tempfield11;
    }

    public void setTempfield11(String tempfield11) {
        this.tempfield11 = tempfield11;
    }

    public String getTempfield12() {
        return tempfield12;
    }

    public void setTempfield12(String tempfield12) {
        this.tempfield12 = tempfield12;
    }

    public String getTempfield13() {
        return tempfield13;
    }

    public void setTempfield13(String tempfield13) {
        this.tempfield13 = tempfield13;
    }

    public String getTempfield14() {
        return tempfield14;
    }

    public void setTempfield14(String tempfield14) {
        this.tempfield14 = tempfield14;
    }

    public String getTempfield15() {
        return tempfield15;
    }

    public void setTempfield15(String tempfield15) {
        this.tempfield15 = tempfield15;
    }

    public String getTempfield16() {
        return tempfield16;
    }

    public void setTempfield16(String tempfield16) {
        this.tempfield16 = tempfield16;
    }


    public static List<Integer> getLengthList() {
        return lengthList;
    }

    public static void setLengthList(List lengthList) {
        ResourceHrmsBean.lengthList = lengthList;
    }

    //工号
    private String workcode;
    private static int workcodeLength = 8;
    //家庭电话
    private String tempfield1;
    private static int tempfield1Length = 20;
    //入职日期
    private String companystartdate;
    private static int companystartdateLength = 8;
    //职位
    private String jobtitleName;
    private static int jobtitleNameLength = 120;
    //所在组织代码
    private String departmentcode;
    private static int departmentcodeLength = 240;
    //离职日期
    private String tempfield2;
    private static int tempfield2Length = 8;
    //姓名
    private String lastname;
    private static int lastnameLength = 60;
    //private static int lastnameLength = 58;
    //拼音
    private String tempfield3;
    private static int tempfield3Length = 150;
    //英文名
    private String tempfield4;
    private static int tempfield4Length = 150;
    //员工类别
    private String usekind;
    private static int usekindLength = 240;
    //private static int usekindLength = 237;
    //分配状态
    private String tempfield5;
    private static int tempfield5Length = 80;
    //手机号
    private String phone;
    private static int phoneLength = 60;
    //职级
    private String tempfield6;
    private static int tempfield6Length = 240;
    //分公司
    private String tempfield7;
    private static int tempfield7Length = 4;
    //分公司(手工维护)
    private String tempfield8;
    private static int tempfield8Length = 4;
    //部门代码
    private String tempfield9;
    private static int tempfield9Length = 2;
    //部门代码(手工维护)
    private String tempfield10;
    private static int tempfield10Length = 2;
    //成本中心
    private String tempfield11;
    private static int tempfield11Length = 4;
    //成本中心(手工维护)
    private String tempfield12;
    private static int tempfield12Length = 4;
    //工作地点
    private String tempfield13;
    private static int tempfield13Length = 60;
    //调店组织
    private String tempfield14;
    private static int tempfield14Length = 240;
    //调店区域
    private String tempfield15;
    private static int tempfield15Length = 2;
    //调店生效日
    private String tempfield16;
    private static int tempfield16Length = 8;

    public int getSumLength() {
        return sumLength;
    }

    private final int sumLength = workcodeLength + tempfield1Length + companystartdateLength + jobtitleNameLength + departmentcodeLength
            + tempfield2Length + lastnameLength + tempfield3Length + tempfield4Length + usekindLength + tempfield5Length
            + phoneLength + tempfield6Length + tempfield7Length + tempfield8Length + tempfield9Length + tempfield10Length
            + tempfield11Length + tempfield12Length + tempfield13Length + tempfield14Length + tempfield15Length + tempfield16Length;

    /**
     * 转换字段
     */
    //直接上级
    private String managerid ;
    //部门ID
    private String departmentid ;
    //分部ID
    private String subcompanyid1 ;
    //职位ID
    private String jobtitle;
    //安全级别
    private String seclevel;
    //状态
    private String status;
    //拼音
    private String pinyinlastname;

    public String getManagerid() {
        return managerid;
    }

    public void setManagerid(String managerid) {
        this.managerid = managerid;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getSubcompanyid1() {
        return subcompanyid1;
    }

    public void setSubcompanyid1(String subcompanyid1) {
        this.subcompanyid1 = subcompanyid1;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getSeclevel() {   return seclevel; }

    public void setSeclevel(String seclevel) { this.seclevel = seclevel; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPinyinlastname() { return pinyinlastname; }

    public void setPinyinlastname(String pinyinlastname) { this.pinyinlastname = pinyinlastname; }

    private static List<Integer> lengthList = new ArrayList<Integer>(){{
        add(workcodeLength);
        add(tempfield1Length);
        add(companystartdateLength);
        add(jobtitleNameLength);
        add(departmentcodeLength);
        add(tempfield2Length);
        add(lastnameLength);
        add(tempfield3Length);
        add(tempfield4Length);
        add(usekindLength);
        add(tempfield5Length);
        add(phoneLength);
        add(tempfield6Length);
        add(tempfield7Length);
        add(tempfield8Length);
        add(tempfield9Length);
        add(tempfield10Length);
        add(tempfield11Length);
        add(tempfield12Length);
        add(tempfield13Length);
        add(tempfield14Length);
        add(tempfield15Length);
        add(tempfield16Length);
    }};



    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("StaffID").append(":").append(workcode).append(",");
        sb.append("Phone").append(":").append(tempfield1).append(",");
        sb.append("JoinDate").append(":").append(companystartdate).append(",");
        sb.append("Position").append(":").append(jobtitleName).append(",");
        sb.append("OrgID").append(":").append(departmentcode).append(",");
        sb.append("SeparationDate").append(":").append(tempfield2).append(",");
        sb.append("ChineseName").append(":").append(lastname).append(",");
        sb.append("PinyinName").append(":").append(tempfield3).append(",");
        sb.append("EnglishName").append(":").append(tempfield4).append(",");
        sb.append("EmployeeType").append(":").append(usekind).append(",");
        sb.append("AssignmentType").append(":").append(tempfield5).append(",");
        sb.append("MobileNo.").append(":").append(phone).append(",");
        sb.append("Grade").append(":").append(tempfield6).append(",");
        sb.append("JV").append(":").append(tempfield7).append(",");
        sb.append("PersonalJV").append(":").append(tempfield8).append(",");
        sb.append("SalesChannel").append(":").append(tempfield9).append(",");
        sb.append("PersonalSalesChannel").append(":").append(tempfield10).append(",");
        sb.append("CostCenter").append(":").append(tempfield11).append(",");
        sb.append("PersonalCostCenter").append(":").append(tempfield12).append(",");
        sb.append("Location").append(":").append(tempfield13).append(",");
        sb.append("TransOrg").append(":").append(tempfield14).append(",");
        sb.append("Region").append(":").append(tempfield15).append(",");
        sb.append("OrgChangedDate").append(":").append(tempfield16);


        sb.append("managerid").append(":").append(managerid);
        sb.append("departmentid").append(":").append(departmentid);
        sb.append("subcompanyid1").append(":").append(subcompanyid1);
        sb.append("jobtitle").append(":").append(jobtitle);
        sb.append("status").append(":").append(status);

        return sb.toString();
    }


}
