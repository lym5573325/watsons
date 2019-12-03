package com.engine.sync.cmd.positionHrms;

import com.engine.sync.entity.PositionHrmsBean;
import com.engine.sync.util.OrgUtil;
import weaver.conn.RecordSet;

public class UpdatePositionHrmsCmd {

    private static final String updateSql = "update hrmjobtitles set jobtitlemark, jobtitlename, jobdepartmentid where jobtitlecode=?";

    public boolean updatePosition(PositionHrmsBean bean){
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(updateSql, bean.getJobtitleName(), bean.getJobtitleName(), bean.getJobtitleCode(), OrgUtil.getOrgidByCode(bean.getJobdepartmentCode()));
    }
}
