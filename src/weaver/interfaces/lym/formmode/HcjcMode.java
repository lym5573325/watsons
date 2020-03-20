package weaver.interfaces.lym.formmode;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class HcjcMode {

    public static final String tableName = "uf_hcjc";
    public static final int modeid = 5005;

    public static String getHcjc(int hcjcid){
        RecordSet rs = new RecordSet();
        rs.execute("select jc from "+tableName+" where id="+hcjcid);
        if(rs.next()) return Util.null2String(rs.getString("jc"));
        else return "";
    }
}
