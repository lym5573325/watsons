package com.engine.sync.cmd.locationHrms;

import com.engine.sync.entity.LocationHrmsBean;
import weaver.conn.RecordSet;

public class UpdateLocationHrmsCmd {

    private static final String  updateSql = "update uf_location set name=?,pk_areacl=?,def1=?,def2=?,city=?,province=?,country=? where code = ?";

    public boolean updateLocation(LocationHrmsBean bean){
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(updateSql, bean.getName(), bean.getPk_areacl(), bean.getDef1(), bean.getDef2(), bean.getCity(), bean.getProvince(), bean.getCountry(), bean.getCode());
    }
}
