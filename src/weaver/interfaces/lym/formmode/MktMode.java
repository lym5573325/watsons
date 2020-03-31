package weaver.interfaces.lym.formmode;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class MktMode {

    public static final String tableName = "uf_MKT";

    public static String getMarketName(int billid){
        RecordSet rs = new RecordSet();
        rs.execute("select mkt from " + tableName + " where id="+billid);
        return rs.next()? Util.null2String(rs.getString(1)) : "";
    }
}
