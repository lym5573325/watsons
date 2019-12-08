package com.engine.sync.cmd.locationHrms;

import com.engine.sync.entity.LocationHrmsBean;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.formmode.data.ModeDataIdUpdate;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.interfaces.lym.util.CalendarMethods;

public class AddLocationHrmsCmd {
    //private static final String addSql = "insert into " + LocationHrmsBean.tableName + "(code,name,pk_areacl,def1,def2,city,province,country) values (?,?,?,?,?,?,?,?)";
    private static final String addSql = "update " + LocationHrmsBean.tableName + " set code=?,name=?,type=?,def1=?,def2=?,city=?,province=?,country=? where id = ?";

    public boolean addLocation(LocationHrmsBean bean){
        new BaseBean().writeLog("添加地点");
        if(StringUtils.isNotBlank(bean.getCode())){
            RecordSet rs = new RecordSet();
            //return rs.executeUpdate(addSql, bean.getCode(), bean.getName(), bean.getPk_areacl(), bean.getDef1(), bean.getDef2(), bean.getCity(), bean.getProvince(), bean.getCountry());
            ModeDataIdUpdate idUpdate = new ModeDataIdUpdate();
            int billid = idUpdate.getModeDataNewId(LocationHrmsBean.tableName, LocationHrmsBean.modeid, 1, 1, CalendarMethods.getCurrentDate(), CalendarMethods.getCurrentTime2());
            boolean b = rs.executeUpdate(addSql, bean.getCode(), bean.getName(), bean.getPk_areacl(), bean.getDef1(), bean.getDef2(), bean.getCity(), bean.getProvince(), bean.getCountry(), billid);
            new ModeRightInfo().editModeDataShare(1, LocationHrmsBean.modeid, billid);
            return b;
        }
        return false;
    }
}
