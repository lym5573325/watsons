package com.engine.sync.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class LocationUtils {

    public static int getLocationIdByCode(String code){
        RecordSet rs = new RecordSet();
        if(code.length()>0) rs.execute("select id from uf_location where code = '" + code + "'");
        return rs.next()? Util.getIntValue(rs.getString("id")) : -1;
    }
}
