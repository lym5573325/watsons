package com.engine.sync.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ResourceUtils {

    public static int getUidByWorkcode(String var1){
        int uid = 0;
        RecordSet rs = new RecordSet();
        if(var1.length()>0) rs.execute("select id from hrmresource where workcode='"+var1+"'");
        if(rs.next())   uid = Util.getIntValue(rs.getString("id"),0);
        return uid;
    }
}
