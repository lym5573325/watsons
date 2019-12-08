package com.engine.sync.entity;

public class PositionHrmsBean {

    private String jobtitleCode;
    private String jobtitleName;
    private String job_pk;
    private String jobdepartmentCode;
    private String locationCode;

    //特殊处理字段
    private String jobtitleMark;

    public String getJobtitleCode() {
        return jobtitleCode;
    }

    public void setJobtitleCode(String jobtitleCode) {
        this.jobtitleCode = jobtitleCode;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public void setJobtitleName(String jobtitleName) {
        this.jobtitleName = jobtitleName;
    }

    public String getJob_pk() {
        return job_pk;
    }

    public void setJob_pk(String job_pk) {
        this.job_pk = job_pk;
    }

    public String getJobdepartmentCode() {
        return jobdepartmentCode;
    }

    public void setJobdepartmentCode(String jobdepartmentCode) {
        this.jobdepartmentCode = jobdepartmentCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getJobtitleMark() {
        return jobtitleMark;
    }

    public void setJobtitleMark(String jobtitleMark) {
        this.jobtitleMark = jobtitleMark;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("jobtitleCode:").append(jobtitleCode).append(",");
        sb.append("jobtitleName:").append(jobtitleName).append(",");
        sb.append("jobtitleMark:").append(jobtitleMark).append(",");
        sb.append("job_pk:").append(job_pk).append(",");
        sb.append("jobdepartmentCode:").append(jobdepartmentCode).append(",");
        sb.append("locationCode:").append(locationCode);
        return sb.toString();
    }
}
