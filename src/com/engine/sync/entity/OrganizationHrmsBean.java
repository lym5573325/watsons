package com.engine.sync.entity;

public class OrganizationHrmsBean {

    public static final int subcompanyid = 1;

    public String getHrms_OrgId() {
        return hrms_OrgId;
    }

    public void setHrms_OrgId(String hrms_OrgId) {
        this.hrms_OrgId = hrms_OrgId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptlevel() {
        return deptlevel;
    }

    public void setDeptlevel(String deptlevel) {
        this.deptlevel = deptlevel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDepttype() {
        return depttype;
    }

    public void setDepttype(String depttype) {
        this.depttype = depttype;
    }

    //（取消）
    private String hrms_OrgId;
    //部门名称
    private String deptName;
    //部门层级
    private String deptlevel;
    //地址
    private String address;
    //部门编码
    private String deptCode;
    //部门类型(取消)
    private String depttype;

    public String getSupdepid() {
        return supdepid;
    }

    public void setSupdepid(String supdepid) {
        this.supdepid = supdepid;
    }

    //上级部门
    private String supdepid;

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("hrms_OrgId").append(":").append(hrms_OrgId).append(",");
        sb.append("deptName").append(":").append(deptName).append(",");
        sb.append("deptlevel").append(":").append(deptlevel).append(",");
        sb.append("address").append(":").append(address).append(",");
        sb.append("deptCode").append(":").append(deptCode).append(",");
        sb.append("depttype").append(":").append(depttype).append(",");
        return sb.toString();
    }
}
