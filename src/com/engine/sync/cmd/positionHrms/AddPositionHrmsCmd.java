package com.engine.sync.cmd.positionHrms;

import com.engine.sync.entity.PositionHrmsBean;
import com.engine.sync.util.OrgUtil;
import weaver.conn.RecordSet;

public class AddPositionHrmsCmd {

    private static final String addSql = "insert into hrmjobtitles(jobtitlemark, jobtitlename, jobdepartmentid, jobtitlecode, jobactivityid) values (?,?,?,?,?)";

    public boolean addPostion(PositionHrmsBean bean){
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(addSql, bean.getJobtitleMark(), bean.getJobtitleName(), OrgUtil.getOrgidByCode(bean.getJobdepartmentCode()), bean.getJobtitleCode(), bean.getJob_pk());
    }
}
