package com.engine.sync.entity;

public class LocationHrmsBean {

    //建模表modeID
    public static final int modeid = 1;
    //建模表名
    public static final String tableName = "uf_location";

    //编码
    private String code;
    //地点名
    private String name;
    //地点类型
    private String pk_areacl;
    //地址1
    private String def1;
    //地址2
    private String def2;
    //城市
    private String city;
    //省份
    private String province;
    //国家
    private String country;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPk_areacl() {
        return pk_areacl;
    }

    public void setPk_areacl(String pk_areacl) {
        this.pk_areacl = pk_areacl;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("code:").append(code).append(",");
        sb.append("name:").append(name).append(",");
        sb.append("pk_areacl:").append(pk_areacl).append(",");
        sb.append("def1:").append(def1).append(",");
        sb.append("def2:").append(def2).append(",");
        sb.append("city:").append(city).append(",");
        sb.append("province:").append(province).append(",");
        sb.append("country:").append(country);
        return sb.toString();
    }
}
