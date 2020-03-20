package weaver.interfaces.lym.formmode;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ZjdzbMode {

    public static final String tableName = "uf_zjdzb";
    public static final int modeid = 5007;

    public static float getTargetleadtimeByGrade(float grade){
        RecordSet rs = new RecordSet();
        rs.execute("select targetleadtime from " + tableName + " where grade='"+grade+"'");
        if(rs.next()) return Util.getFloatValue(rs.getString("targetleadtime"),0);
        else return 0f;
    }
}
