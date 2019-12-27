<%@ page import="org.json.JSONObject" %>
<%@ page import="java.io.OutputStreamWriter" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page import="com.engine.epdocking.util.TokenInfoUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    out.print("<br>"+new TokenInfoUtil().getToken());
    out.print("<br>"+new TokenInfoUtil().getEncrykey());
    /*JSONObject json = new JSONObject();
    try {
        json.put("appid", "2F9BF68276935A957FC9EDD58318D2AD8AF0FA6C4C44D580");
        json.put("password", "159B016C418A35618CF2725365060943");
    }catch (Exception e){
        out.print("异常");
    }
    out.print("<br>json:"+json);
    String result  = sendPost("http://124.172.191.50:8080/EP_OtherApi/api/Token",json.toString(),false);
    out.print("<br>result:"+result);*/
%>
<%!
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *   发送请求的 URL
     * @param param
     *   请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param isproxy
     *    是否使用代理模式
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,boolean isproxy) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            if(isproxy){//使用代理模式
                //@SuppressWarnings("static-access")
                //Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                //conn = (HttpURLConnection) realUrl.openConnection(proxy);
            }else{
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // POST方法


            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
%>