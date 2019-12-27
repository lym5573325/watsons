package com.engine.sync.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ResourceUtils {

    public static int getUidByWorkcode(String var1) {
        int uid = 0;
        RecordSet rs = new RecordSet();
        if (var1.length() > 0) rs.execute("select id from hrmresource where workcode='" + var1 + "'");
        if (rs.next()) uid = Util.getIntValue(rs.getString("id"), 0);
        return uid;
    }

    public static int getMaxId() {
        RecordSet rs = new RecordSet();
        int var=0;
        rs.execute("select max(id) as maxid from hrmresource ");
        if(rs.next())   var = Util.getIntValue(rs.getString("maxid"),0);
        return var+1;
    }


    public static int getUseKind(String param1){
        RecordSet rs = new RecordSet();
        int result=0;
        rs.execute("select id from hrmusekind where description = '"+param1+"'");
        if(rs.next())   result = Util.getIntValue(rs.getString("id"));
        return result;
    }

    public static String transSex(String sex){
        if(sex.toLowerCase().equals("female")){//女
            sex = "1";
        }else{//男
            sex = "0";
        }
        return sex;
    }
}