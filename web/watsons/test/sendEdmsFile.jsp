<%@ page import="weaver.interfaces.lym.edms.util.EdmsHttpUtils" %>
<%@ page import="java.io.File" %>
<%@ page import="weaver.interfaces.lym.util.ImageFileUtils" %>
<%@ page import="org.slf4j.helpers.Util" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="weaver.file.ImageFileManager" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String option = weaver.general.Util.null2String(request.getParameter("option"));
    boolean b = false;
    EdmsHttpUtils edmsHttpUtils = new EdmsHttpUtils();
    String path = "/app/weaver/ecology/watsons/edmsfile/"+ImageFileUtils.getFileName("210");
    out.print("<br>path:"+path);
    try{
        InputStream in = ImageFileManager.getInputStreamById(1198);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;

        while((length = in.read(buffer)) > 0){
            output.write(buffer,0,length);
        }
        fileOutputStream.write(output.toByteArray());
        in.close();
        fileOutputStream.close();
        b=true;
    }catch(Exception e){
        out.print("<br>exception:"+e.getMessage());
    }
    if(b){
        out.print("下载文件成功:"+path);
        File file = new File(path);
        if(file.exists()){
            out.print("<br>文件exist");
            if(option.equals("execute")){

                if(edmsHttpUtils.sendDocument("41068101","培训协议/承诺书",file,"周彫","41068101","123")){
                    out.print("<br>send成功");
                }else{
                    out.print("<br>send 失败");
                }
            }
        }
    }


%>
<%

%>