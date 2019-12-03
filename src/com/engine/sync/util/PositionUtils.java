package com.engine.sync.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class PositionUtils {

    public static int getJobIdByCode(String code){
        RecordSet rs = new RecordSet();
        if(code.length()>0) rs.execute("select id from hrmjobtitles where jobtitlecode = '" + code + "'");
        return rs.next()? Util.getIntValue(rs.getString("id")) : -1;
    }

    public static int getJobidByName(String jobtileName){
        RecordSet rs = new RecordSet();
        rs.execute("select id from hrmjobtitles where jobtitlename = '"+jobtileName+"'");
        if(rs.next())   return Util.getIntValue(rs.getString("id"));
        return -1;
    }
}
