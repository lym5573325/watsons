package com.engine.sync.cmd.positionHrms;

import com.engine.sync.entity.PositionHrmsBean;
import com.engine.sync.util.OrgUtil;
import weaver.conn.RecordSet;

public class UpdatePositionHrmsCmd {

    private static final String updateSql = "update hrmjobtitles set jobtitlemark=?, jobtitlename=?, jobdepartmentid=?, jobactivityid=?, ecology_pinyin_search=? where jobtitlecode=?";

    public boolean updatePosition(PositionHrmsBean bean){
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(updateSql, bean.getJobtitleMark(), bean.getJobtitleName(), OrgUtil.getOrgidByCode(bean.getJobdepartmentCode()), bean.getJob_pk(),bean.getJobtitlePinyin(), bean.getJobtitleCode());
    }
}
