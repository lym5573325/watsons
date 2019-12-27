package weaver.formmode.customjavacode.browser;

import java.util.*;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.User;
import weaver.formmode.customjavacode.AbstractCustomSqlConditionJavaCode;

/**
 * 部门特殊按钮
 * 1.属于“部门矩阵”里的"MKT-ER","Recuriter"的部门
 * 2.属于角色“ W-SR HR Manager（ID:542）、W-Fuction Assistant（ID:541）”可以选择所有部门
 */
public class DepartmentBrowser1 extends AbstractCustomSqlConditionJavaCode {

	/**
	 * 生成SQL查询限制条件
	 * @param param
	 *  param包含(但不限于)以下数据
	 *  user 当前用户
	 * 
	 * @return
	 *  返回的查询限制条件的格式举例为: t1.a = '1' and t1.b = '3' and t1.c like '%22%'
	 *  其中t1为表单主表表名的别名
	 */
	public String generateSqlCondition(Map<String, Object> param) throws Exception {
		User user = (User)param.get("user");

		String sqlCondition = "1=0";
		int uid = user.getUID();
		if(uid==1)	return "1=1";
		RecordSet rs = new RecordSet();
		rs.execute("select id from hrmrolemembers where resourceid="+uid+" and roleid in (542,543)");
		if(rs.next()){
			return  "t1.id>0 and NVL(t1.canceled,0)!='1' ";
		}

		sqlCondition =  "t1.id in (select id " +
				" from matrixtable_2 " +
				" where " +
				" instr(','|| MKTER ||',', ','||"+uid+"||',',1,1)>0 " +
				" or " +
				" instr(','|| Recuriter ||',', ','||"+uid+"||',',1,1)>0)";

		new BaseBean().writeLog("虚拟部门按钮1==>  uid:"+uid+"    sqlwhere:"+sqlCondition);
		return sqlCondition;
	}

}