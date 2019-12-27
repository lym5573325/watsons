package com.engine.sync.entity;

public class ResourceHrms2Bean {

    public String getWorkcode() {
        return workcode;
    }

    public void setWorkcode(String workcode) {
        this.workcode = workcode;
    }

    public String getCertificatenum() {
        return certificatenum;
    }

    public void setCertificatenum(String certificatenum) {
        this.certificatenum = certificatenum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //工号
    private String workcode;
    //身份证号码
    private String certificatenum;
    //性别
    private String sex;
    //邮箱
    private String email;

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("workcode:").append(workcode).append(",");
        sb.append("certificatenum:").append(certificatenum).append(",");
        sb.append("sex:").append(sex).append(",");
        sb.append("email:").append(email).append(",");
        return sb.toString();
    }
}
