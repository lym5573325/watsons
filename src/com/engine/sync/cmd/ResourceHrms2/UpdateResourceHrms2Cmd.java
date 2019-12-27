package com.engine.sync.cmd.ResourceHrms2;

import com.engine.sync.entity.ResourceHrms2Bean;
import weaver.conn.RecordSet;

public class UpdateResourceHrms2Cmd {

    private static String sql=" update hrmresource set certificatenum=?,sex=?,email=? where workcode=?";

    public boolean execute(ResourceHrms2Bean bean){
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(sql, bean.getCertificatenum(), bean.getSex(), bean.getEmail(), bean.getWorkcode());
    }
}
