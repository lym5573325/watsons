package com.engine.epdocking.entity;

/**
 * 可访问系列实体
 */
public class SystemBean {

    public String getRole_key() {
        return role_key;
    }

    public void setRole_key(String role_key) {
        this.role_key = role_key;
    }

    public String getSys_cn_name() {
        return sys_cn_name;
    }

    public void setSys_cn_name(String sys_cn_name) {
        this.sys_cn_name = sys_cn_name;
    }

    public String getSys_en_name() {
        return sys_en_name;
    }

    public void setSys_en_name(String sys_en_name) {
        this.sys_en_name = sys_en_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRunkind() {
        return runkind;
    }

    public void setRunkind(String runkind) {
        this.runkind = runkind;
    }

    public String getOtherexename() {
        return otherexename;
    }

    public void setOtherexename(String otherexename) {
        this.otherexename = otherexename;
    }

    public String getBrowsertype() {
        return browsertype;
    }

    public void setBrowsertype(String browsertype) {
        this.browsertype = browsertype;
    }

    public String getViewcount() {
        return viewcount;
    }

    public void setViewcount(String viewcount) {
        this.viewcount = viewcount;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getHaveprocess() {
        return haveprocess;
    }

    public void setHaveprocess(String haveprocess) {
        this.haveprocess = haveprocess;
    }

    //系统标识
    private String role_key;
    //系统中文名称
    private String sys_cn_name;
    //系统英文名称
    private String sys_en_name;
    //系统跳转URL
    private String url;
    //跳转方式  1=原浏览器,2=第三方应用
    private String runkind;
    //外部exe文件名称
    private String otherexename;
    //浏览器标  如runkind=2时,并所需要的浏览器标识跟当前的浏览器标识一样,就直接打开
    private String browsertype;
    //前一天所有日期的访问次数
    private String viewcount;
    //系统原默认的图标URL
    private String logourl;
    //是否有待办信息   0=表示没有待办,1=表示有待办信息
    private String haveprocess;

}
