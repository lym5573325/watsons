package com.engine.sync.cmd.locationHrms;

import com.engine.sync.entity.LocationHrmsBean;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;

public class UpdateLocationHrmsCmd {

    private static final String  updateSql = "update uf_location set name=?,type=?,def1=?,def2=?,city=?,province=?,country=? where code = ?";

    public boolean updateLocation(LocationHrmsBean bean){
        new BaseBean().writeLog("更新地点");
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(updateSql, bean.getName(), bean.getPk_areacl(), bean.getDef1(), bean.getDef2(), bean.getCity(), bean.getProvince(), bean.getCountry(), bean.getCode());
    }
}
