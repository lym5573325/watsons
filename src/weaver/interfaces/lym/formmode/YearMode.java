package weaver.interfaces.lym.formmode;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class YearMode {

    public static String getIdByYear(String year){
        RecordSet rs = new RecordSet();
        rs.execute("select id from uf_year where n = '"+year+"'" );
        if(rs.next())   return Util.null2String(rs.getString("id"));
        return "";
    }
}
