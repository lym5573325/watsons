package weaver.formmode.interfaces.impl;

import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import weaver.conn.RecordSet;
import weaver.formmode.interfaces.ImportFieldTransAction;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 * @author Administrator
 *
 */
public class ImportFieldTransHC implements ImportFieldTransAction{

    @Override
    public String getTransValue(Map<String, Object> param, User user, Sheet sheet, int row, int col) {

        // 获取模块ID
        Integer modeId = Util.getIntValue(param.get("modeid").toString());
        //表单id
        Integer formId = Util.getIntValue(param.get("formid").toString());
        //当前字段id
        String fieldid = Util.null2String(param.get("fieldid"));

        // 获取当前登录人员ID
        Integer userId = user.getUID();

        // 获取当前单元格
        Cell cell = sheet.getCell(col, row);
        String value=cell.getContents();
        new BaseBean().writeLog(value);
        if ("7334".equals(fieldid) || "7343".equals(fieldid)) {//Department (for oracle)  部门
            value=getDepartmentId(fieldid);//自定义转换逻辑
        }else if ("7340".equals(fieldid)) {//Title  职位
            value=getJobtitlesId(fieldid);
        }
        return value;
    }

    private String getDepartmentId(String departmentname){
        RecordSet rs = new RecordSet();
        rs.execute("select id from hrmdepartment where departmentname='"+departmentname+"'");
        return rs.next()? Util.null2String(rs.getString(1)) : departmentname;
    }

    private String getJobtitlesId(String jobtitlesName){
        RecordSet rs = new RecordSet();
        rs.execute("select id from hrmjobtitles where jobtitlemark='"+jobtitlesName+"'");
        return rs.next()? Util.null2String(rs.getString(1)) : jobtitlesName;
    }
}
