<%@ page import="weaver.general.BaseBean" %>
<%@ page import="java.io.*" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    File file = new File("D:\\WEAVER9\\syncinfo\\20191216\\EmployeeReportTo_WTCCN_20191215.txt");
    if(file.exists()){
        BaseBean bb = new BaseBean();
        RecordSet rs = new RecordSet();
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            while((tempString = reader.readLine()) != null){
                //System.out.println("第"+line+"行:"+tempString+"    长度:"+tempString.length());
                String var1 = tempString.substring(2,10);
                var1 = transCode(var1);
                String var2 = tempString.substring(12,20);
                var2 = transCode(var2);
                bb.writeLog("第"+line+"行====>" + "   员工:"+var1+"   直接上级:"+var2);
                rs.execute("select id from hrmresource where workcode='"+var2+"'");
                if(rs.next()){
                    String managerid = Util.null2String(rs.getString("id"));
                    rs.execute("select id from hrmresource where workcode = " + var1);
                    if(rs.next()){
                        rs.execute("update hrmresource set managerid ='" + managerid + "' where id = "+ Util.null2String(rs.getString("id")));

                    }else{
                        bb.writeLog("(同步直接上级错误)没有找到需要更新的人员!");
                    }
                }else{
                    bb.writeLog("(同步直接上级错误)没有找到直接上级!");
                }
                line++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }else{
        System.out.println("文件不存在");
    }
%>
<%!
    public  String transCode(String var1){
        if(var1.equals("40099915")) var1 = "41000006";
        else if(var1.equals("40099934")) var1 = "41000014";
        else if(var1.equals("40099864")) var1 = "41000007";
        else if(var1.equals("40224362")) var1 = "41038978";
        else if(var1.equals("40231350")) var1 = "41057879";
        else if(var1.equals("40209960")) var1 = "41078460";
        else if(var1.equals("40099805")) var1 = "41143833";
        else if(var1.equals("40284653")) var1 = "41165890";
        return var1;
    }
%>