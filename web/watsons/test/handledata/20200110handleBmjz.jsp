<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    /**
     * 20200110开慧提供更新部门矩阵
     */
    int line = 0;
    RecordSet rs = new RecordSet();
    RecordSet rs2 = new RecordSet();
    rs.execute("select * from uf_handleData order by id asc");

    while(rs.next()){
        line++;
        String departmentname = Util.null2String(rs.getString("departmentname"));
        String[] temp = departmentname.split("/");
        departmentname = temp[temp.length-1].replaceAll("&",".");
        rs2.execute("select id from hrmdepartment where departmentname = '"+departmentname+"' ");
        rs2.next();
        String depid = Util.null2String(rs2.getString("id"));
        String sql ="";
        if(!depid.equals("")){
            String regionhrhead = getUids(Util.null2String(rs.getString("regionhrhead")));
            if(regionhrhead.length()>0) sql += "regionhrhead='"+regionhrhead+"',";

            String regionstaffingmanager = getUids(Util.null2String(rs.getString("regionstaffingmanager")));
            if(regionstaffingmanager.length()>0) sql += "regionstaffingmanager='"+regionstaffingmanager+"',";

            String regioncb = getUids(Util.null2String(rs.getString("regioncb")));
            if(regioncb.length()>0) sql += "regioncb='"+regioncb+"',";

            String regioner = getUids(Util.null2String(rs.getString("regioner")));
            if(regioner.length()>0) sql += "regioner='"+regioner+"',";

            String regionia = getUids(Util.null2String(rs.getString("regionia")));
            if(regionia.length()>0) sql += "regionia='"+regionia+"',";

            String regionsecurity = getUids(Util.null2String(rs.getString("regionsecurity")));
            if(regionsecurity.length()>0) sql += "regionsecurity='"+regionsecurity+"',";

            String regiondepthead = getUids(Util.null2String(rs.getString("regiondepthead")));
            if(regiondepthead.length()>0) sql += "regiondepthead='"+regiondepthead+"',";

            String regiongm = getUids(Util.null2String(rs.getString("regiongm")));
            if(regiongm.length()>0) sql += "regiongm='"+regiongm+"',";

            String hodepthead = getUids(Util.null2String(rs.getString("hodepthead")));
            if(hodepthead.length()>0) sql += "depthead='"+hodepthead+"',";

            String mkthrhead = getUids(Util.null2String(rs.getString("mkthrhead")));
            if(mkthrhead.length()>0) sql += "mkthrhead='"+mkthrhead+"',";

            String mktcb = getUids(Util.null2String(rs.getString("mktcb")));
            if(mktcb.length()>0) sql += "mktcb='"+mktcb+"',";

            String mktstaffing = getUids(Util.null2String(rs.getString("mktstaffing")));
            if(mktstaffing.length()>0) sql += "mktstaffing='"+mktstaffing+"',";

            String mkter = getUids(Util.null2String(rs.getString("mkter")));
            if(mkter.length()>0) sql += "mkter='"+mkter+"',";

            String mktiamanager = getUids(Util.null2String(rs.getString("mktiamanager")));
            if(mktiamanager.length()>0) sql += "mktiamanager='"+mktiamanager+"',";

            String mktsecuritymanager = getUids(Util.null2String(rs.getString("mktsecuritymanager")));
            if(mktsecuritymanager.length()>0) sql += "mktsecuritymanager='"+mktsecuritymanager+"',";

            String deptassistant = getUids(Util.null2String(rs.getString("deptassistant")));
            if(deptassistant.length()>0) sql += "deptassistant='"+deptassistant+"',";

            String mktdepthead = getUids(Util.null2String(rs.getString("mktdepthead")));
            if(mktdepthead.length()>0) sql += "mktdepthead='"+mktdepthead+"',";

            String mkthead = getUids(Util.null2String(rs.getString("mkthead")));
            if(mkthead.length()>0) sql += "mkthead='"+mkthead+"',";

			/*
			1、如果MKT head有值，MKT dept head没有值，则用MKT head更新MKT dept head ，反之不更新
			2、如果Region GM有值，Region dept head没有值，则用Region GM更新Region dept head ，反之不更新
			3、如果Region ER有值，MKT ER没有值，则用Region ER更新MKT ER，反之不更新
			*/
            if(mktdepthead.length()==0 && mkthead.length()>0){
                out.print("<br> mktdepthead null");
                sql += "mktdepthead='"+mkthead+"',";
            }
            if(regiondepthead.length()==0 && regiongm.length()>0){
                out.print("<br> regiondepthead null");
                sql += "regiondepthead='"+regiongm+"',";
            }
            if(mkter.length()==0 && regioner.length()>0){
                out.print("<br> mkter null");
                sql += "mkter='"+regioner+"',";
            }

                if(sql.length()>0){
                    sql = sql.substring(0,sql.length()-1);
                    String sql2 = "update hrmdepartmentdefined set " + sql + " where deptid="+depid;
                    sql = "update matrixtable_2 set " + sql +" where id="+depid;
                    //new BaseBean().writeLog("第"+line+"行SQL:"+sql);
                    out.print("<br>"+sql+";");
                    if(Util.null2String(request.getParameter("option")).equals("111")) {
                        rs2.execute(sql);
                        rs2.execute(sql2);
                    }
                }
        }else{
            out.print("<br>第"+line+"行错误:找不到部门:"+departmentname);
        }
    }
%>
<%!
    public String getDepidByName(String departmentname){
        RecordSet rs = new RecordSet();
        rs.execute("select id from hrmdepartment where departmentname = '"+departmentname+"' ");
        if(rs.next())   return Util.null2String(rs.getString("id"));
        return "";
    }

    public String getUids(String workcode){
        String result ="";
        String[] workcodeArray = workcode.split("/");
        for(int i=0;i<workcodeArray.length;i++){
            String uid = getUid(workcodeArray[i]);
            if(uid.length()>0)  result += uid+",";
        }
        if(result.length()>0)  return result.substring(0,result.length()-1);
        return "";
    }

    public String getUid(String workcode){
        RecordSet rs = new RecordSet();
        rs.execute("select id from hrmresource where workcode='"+workcode+"'");
        if(workcode.length()>0){
            if(rs.next()){
                return Util.null2String(rs.getString("id"));
            }else{
                new BaseBean().writeLog("找不到人:"+workcode);
            }
        }
        return "";
    }
%>