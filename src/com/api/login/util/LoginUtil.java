package com.api.login.util;

import HT.HTSrvAPI;
import com.api.login.biz.LoginBiz;
import com.cloudstore.dev.api.util.Util_DataMap;
import com.engine.epdocking.util.LoginCheckUtil;
import com.engine.hrm.util.HrmOrganizationVirtualUtil;
import com.engine.integration.biz.CASLogoutUtil;
import ln.LN;
import weaver.common.DateUtil;
import weaver.common.StringUtil;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.*;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.hrm.common.DbFunctionUtil;
import weaver.hrm.loginstrategy.LoginStrategyManager;
import weaver.hrm.loginstrategy.exception.LoginStrategyException;
import weaver.hrm.settings.BirthdayReminder;
import weaver.hrm.settings.ChgPasswdReminder;
import weaver.hrm.settings.HrmSettingsComInfo;
import weaver.hrm.settings.RemindSettings;
import weaver.login.Base64;
import weaver.login.*;
import weaver.rsa.security.RSA;
import weaver.sm.SM3Utils;
import weaver.sm.SM4Utils;
import weaver.sms.SMSManager;
import weaver.systeminfo.SysMaintenanceLog;
import weaver.systeminfo.SystemEnv;
import weaver.usb.UsbKeyProxy;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoginUtil extends BaseBean {
    private String isADAccount = "";
    private String ipAddress = "";//用于记录日志用的IP地址

    private String ldapError = "";
    private String loginSession = "";
    private LoginCheckUtil loginCheckUtil = new LoginCheckUtil();

    public String[] checkLogin(ServletContext application, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String usercheck = beforeCheckUser(request, response);
        writeLog("userCheck:"+usercheck);
        if (usercheck.equals("")) {
            writeLog("userCheck null");
            usercheck = getUserCheck(application, request, response);
            if(usercheck.equals("17"))usercheck="16";
        }
        writeLog("userCheck2:"+usercheck);
        afterCheckUser(application, request, response, usercheck);
        return getErrorMsg(application, request, response, usercheck);
    }

    private String AddToken(HttpServletRequest request, User user, String sessionId) {
        String accessuuids = "";
        BaseBean bb = new BaseBean();
        List lsParams = null;
        String status = Util.null2String(bb.getPropValue("weaver_cloudtoken", "status"));
        if ("1".equals(status)) {
            RecordSet rs = new RecordSet();
            String selectsql = "select userid from cloud_logintoken  where userid =? ";
            rs.executeQuery(selectsql, user.getUID());

            String times = System.currentTimeMillis() + "";
            if (rs.next()) {
                accessuuids = UUID.randomUUID() + "";
                lsParams = new ArrayList();
                lsParams.add(times);
                lsParams.add(accessuuids);
                lsParams.add(sessionId);
                lsParams.add(user.getUID());
                String updatesql = "update cloud_logintoken set updatetimes = ? ,accesstoken=?,sessionid=?  where userid=?";
                rs.executeUpdate(updatesql, lsParams);
                rs.next();
            } else {
                accessuuids = UUID.randomUUID() + "";
                lsParams = new ArrayList();
                lsParams.add(user.getUID());
                lsParams.add(user.getLoginid());
                lsParams.add(accessuuids);
                lsParams.add(times);
                lsParams.add(times);
                lsParams.add(sessionId);
                lsParams.add(0);
                String insertsql = "insert into cloud_logintoken (userid,loginid,access_token,logintimes,updatetimes,sessionid,status) values(?,?,?,?,?,?,?)";
                rs.executeUpdate(insertsql, lsParams);
                rs.next();
            }
        }

        return accessuuids;
    }

    public void checkLogout(ServletContext application, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(true);
            String weaver_login_type = Util.null2String(session.getAttribute("weaver_login_type"));
            User user = HrmUserVarify.getUser(request, response);
            ChgPasswdReminder reminder = new ChgPasswdReminder();
            RemindSettings settings0 = reminder.getRemindSettings();
            Map logmessages = (Map) application.getAttribute("logmessages");
            String a_logmessage = "";
            if (logmessages != null) {
                a_logmessage = Util.null2String((String) logmessages.get(user.getUID()));
            }
            String s_logmessage = Util.null2String((String) session.getAttribute("logmessage"));
            if (s_logmessage == null) {
                s_logmessage = "";
            }
            String relogin0 = Util.null2String(settings0.getRelogin());

            if (request.getSession(true).getAttribute("layoutStyle") != null) {
                request.getSession(true).setAttribute("layoutStyle", null);
            }

            if ((!relogin0.equals("1")) && (!s_logmessage.equals(a_logmessage))) {
                return;
            }
            logmessages = (Map) application.getAttribute("logmessages");
            if (logmessages != null) {
                logmessages.remove(user.getUID());
            }

            new LicenseCheckLogin().updateOnlinFlag("" + user.getUID());
            request.getSession(true).removeValue("moniter");
            request.getSession(true).removeValue("WeaverMailSet");
            request.getSession(true).removeAttribute("weaver_user@bean");
            request.getSession(true).removeAttribute("accounts");
            request.getSession(true).removeAttribute("ep_loginsession");
            request.getSession(true).invalidate();
            request.getSession(true).setAttribute("weaver_login_type",weaver_login_type);
            try {
                response.addHeader("Set-Cookie", "__clusterSessionIDCookieName=" + Util.getCookie(request, "__clusterSessionIDCookieName") + ";expires=Thu, 01-Dec-1994 16:00:00 GMT;Path=/;HttpOnly");
            } catch (Exception e) {
            }
            //weaver.hrm.HrmUserVarify.invalidateCookie(request,response);

            //cas相关
            //cas相关
            new CASLogoutUtil().checkLogout(request, user, weaver_login_type);
            /*记录登出日志*/
            SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
            sysMaintenanceLog.resetParameter();
            sysMaintenanceLog.setRelatedId(user.getUID());
            sysMaintenanceLog.setRelatedName(user.getLastname());
            sysMaintenanceLog.setOperateType("303");
            sysMaintenanceLog.setOperateDesc(SystemEnv.getHtmlLabelName(25149, user.getLanguage()));
            sysMaintenanceLog.setOperateItem("505");
            sysMaintenanceLog.setOperateUserid(user.getUID());
            sysMaintenanceLog.setClientAddress(request.getRemoteAddr());
            sysMaintenanceLog.setSysLogInfo();
            /*记录登出日志*/

        } catch (Exception localException) {
        }
    }

    private String getUserCheck(ServletContext application, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RecordSet rs = new RecordSet();
        char separator = Util.getSeparator();
        String message = "";
        String login_id = Util.null2String(request.getParameter("loginid"));
        String user_password = Util.null2String(request.getParameter("userpassword"));
        String isrsaopen = Util.null2String(rs.getPropValue("openRSA", "isrsaopen"));
        List<String> decriptList = new ArrayList<>() ;

        if("1".equals(isrsaopen)){
            RSA rsa = new RSA();
            decriptList.add(login_id) ;
            decriptList.add(user_password) ;
            List<String> resultList = rsa.decryptList(request,decriptList) ;
            login_id = resultList.get(0) ;
            user_password = resultList.get(1) ;

            if(!rsa.getMessage().equals("0")){
                writeLog("rsa.getMessage()", rsa.getMessage());
                return "184";
            }
        }


        if (user_password.endsWith("_random_")) {
            SM4Utils sm4 = new SM4Utils();
            BaseBean bb = new BaseBean();
            String key = Util.null2String(bb.getPropValue("weaver_client_pwd", "key"));
            if (!"".equals(key)) {
                user_password = user_password.substring(0, user_password.lastIndexOf("_random_"));
                user_password = sm4.decrypt(user_password, key);
            }
        }

        login_id = LoginBiz.getLoginId(login_id,request);
        if(login_id.length()==0){
            writeLog("loginid is null");
            return "99";
        }

        String ismobile = Util.null2String(request.getParameter("ismobile")) ;
        if(!"1".equals(ismobile)){
            try{
                LoginStrategyManager.checkLoginStrategy(login_id, Util.getIpAddr(request));
            }catch (LoginStrategyException e){
                return e.getCode() ;
            }
        }

        String login_file = Util.null2String(request.getParameter("loginfile"));
        String login_type = Util.null2String(request.getParameter("logintype"), "1");
        String messages = Util.null2String(request.getParameter("messages"));
        String usbserver = Prop.getPropValue(GCONST.getConfigFile(), "usbserver.ip");
        String serial = Util.null2String(request.getParameter("serial"));
        String username = Util.null2String(request.getParameter("username"));
        String rnd = Util.null2String(request.getParameter("rnd"));

        HrmSettingsComInfo sci = new HrmSettingsComInfo();
        Calendar today = Calendar.getInstance();
        String currentdate = Util.add0(today.get(1), 4) + "-" + Util.add0(today.get(2) + 1, 2) + "-" + Util.add0(today.get(5), 2);
        String currenttime = Util.add0(today.get(11), 2) + ":" + Util.add0(today.get(12), 2) + ":" + Util.add0(today.get(13), 2);
        try {
            boolean ismutilangua = Util.isEnableMultiLang();
            int islanguid = 7;//系统使用语言,未使用多语言的用户默认为中文。
            String languid = "7";
            if (ismutilangua) {
                islanguid = Util.getIntValue(request.getParameter("islanguid"), 0);
                if (islanguid == 0) {//如何未选择，则默认系统使用语言为简体中文
                    islanguid = 7;
                }
                languid = String.valueOf(islanguid);
                Cookie syslanid = new Cookie("Systemlanguid", languid);
                syslanid.setMaxAge(-1);
                syslanid.setPath("/");
                response.addCookie(syslanid);
            }

            if (login_type.equals("1") || login_type.equals("3")) {
                boolean isAdmin = false;
                rs.executeQuery("select * from HrmResource where loginid = ?", login_id);
                if (rs.next()) {
                    this.isADAccount = rs.getString("isADAccount");
                }
                //String mode = Prop.getPropValue(GCONST.getConfigFile(), "authentic");
                boolean isAdLogin = Boolean.FALSE;

                if ("1".equals(this.isADAccount) && !login_id.equals("sysadmin")) {
                    com.weaver.integration.ldap.util.AuthenticUtil au = new com.weaver.integration.ldap.util.AuthenticUtil();
                    isAdLogin = au.checkType(login_id);

                    if (isAdLogin) {
                        String ret = au.checkLogin(login_id, user_password);
                        if (!"100".equalsIgnoreCase(ret)) {
                            ldapError = ret;
                            return "16";
                        }
                    }
                }

                if (!isAdLogin) {

                    String[] loginCheck = checkUserPass(request, login_id, user_password, messages);
                    if (loginCheck[0].equals("-2"))
                        return "55";
                    if (loginCheck[0].equals("-1"))
                        return "17";
                    if (loginCheck[1].equals("0"))
                        return "16";
                    if (loginCheck[1].equals("101"))
                        return "101";
                    else if (loginCheck[1].equals("730"))
                        return "730";
                    else if (loginCheck[1].equals("57"))
                        return "57";
                    else if (loginCheck[1].equals("2"))
                        return "556";
                    else if (loginCheck[0].equals("0")) {
                        rs.executeQuery("select * from HrmResource where loginid=? ", login_id);
                        rs.next();
                    } else {
                        isAdmin = true;
                        rs.executeQuery("select * from HrmResourceManager where loginid=?", login_id);
                        rs.next();
                    }
                }
                String startdate = rs.getString("startdate");
                String enddate = rs.getString("enddate");
                int status = rs.getInt("status");
                if ((status != 0) && (status != 1) && (status != 2) && (status != 3)) {
                    return "17";
                }

                User user = new User();
                user.setUid(rs.getInt("id"));
                user.setLoginid(login_id);
                user.setPwd(user_password);
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setAliasname(rs.getString("aliasname"));
                user.setTitle(rs.getString("title"));
                user.setTitlelocation(rs.getString("titlelocation"));
                user.setSex(rs.getString("sex"));
                String languageidweaver = Util.null2String(rs.getString("systemlanguage"), "7");
                if (!languid.equalsIgnoreCase(languageidweaver) && ismutilangua) {
                    User.setUserLang(rs.getInt("id"), Util.getIntValue(languid, 7));
					/*
                    RecordSet rsUp = new RecordSet();
                    if (isAdmin) {
                        rsUp.executeUpdate("update hrmresourceManager set  systemlanguage = ? where id =?", languid, rs.getInt("id"));
                        User.setUserLang(rs.getInt("id"), Util.getIntValue(languid, 7));
                    } else {
                        rsUp.executeUpdate("update hrmresource set  systemlanguage = ? where id =?", languid, rs.getInt("id"));
                        User.setUserLang(rs.getInt("id"), Util.getIntValue(languid, 7));
                    }*/
                    languageidweaver = languid;

                }
                if ("".equalsIgnoreCase(languageidweaver)) {
                    writeLog("in rs :" + Util.null2String(rs.getString("systemlanguage"), "7") + ":in request:" + languid + ":in ismutilangua:" + ismutilangua + ":Util.isEnableMultiLang():" + Util.isEnableMultiLang());
                }
                user.setLanguage(Util.getIntValue(languageidweaver, 7));
                user.setTelephone(rs.getString("telephone"));
                user.setMobile(rs.getString("mobile"));
                user.setMobilecall(rs.getString("mobilecall"));
                user.setEmail(rs.getString("email"));
                user.setCountryid(rs.getString("countryid"));
                user.setLocationid(rs.getString("locationid"));
                user.setResourcetype(rs.getString("resourcetype"));
                user.setStartdate(startdate);
                user.setEnddate(enddate);
                user.setContractdate(rs.getString("contractdate"));
                user.setJobtitle(rs.getString("jobtitle"));
                user.setJobgroup(rs.getString("jobgroup"));
                user.setJobactivity(rs.getString("jobactivity"));
                user.setJoblevel(rs.getString("joblevel"));
                user.setSeclevel(rs.getString("seclevel"));
                user.setUserDepartment(Util.getIntValue(rs.getString("departmentid"), 0));
                user.setUserSubCompany1(Util.getIntValue(rs.getString("subcompanyid1"), 0));
                user.setUserSubCompany2(Util.getIntValue(rs.getString("subcompanyid2"), 0));
                user.setUserSubCompany3(Util.getIntValue(rs.getString("subcompanyid3"), 0));
                user.setUserSubCompany4(Util.getIntValue(rs.getString("subcompanyid4"), 0));
                user.setManagerid(rs.getString("managerid"));
                user.setAssistantid(rs.getString("assistantid"));
                user.setPurchaselimit(rs.getString("purchaselimit"));
                user.setCurrencyid(rs.getString("currencyid"));
                user.setLastlogindate(currentdate);
                user.setLogintype(login_type);
                user.setAccount(rs.getString("account"));
                user.setIsAdmin(isAdmin);

                CheckIpNetWork checkipnetwork = new CheckIpNetWork();
                String clientIP = Util.getIpAddr(request);
                boolean checktmp = checkipnetwork.checkIpSeg(clientIP);

                int needusb = rs.getInt("needusb");
                int usbstate = rs.getInt("usbstate");

                if (usbstate != 2) {
                    checktmp = true;
                }

                String usbType = sci.getUsbType();
                String needusbHt = sci.getNeedusbHt();
                String needusbDt = sci.getNeedusbDt();
                String userUsbType = Util.null2String(rs.getString("userUsbType"));
                if (!userUsbType.equals("")) {
                    usbType = userUsbType;
                }
                needusb = (userUsbType.equals("2")) || (userUsbType.equals("3")) ? 1 : 0;

                if (needusb == 1) {
                    if ((checktmp) && (usbstate != 1)) {
                        if ("1".equals(usbType)) {
                            String serialNo = Util.null2String(rs.getString("serial"));
                            byte[] bts = Base64.decode(serial);
                            String serial1 = new String(bts, "ISO8859_1");
                            long firmcode = Util.getIntValue(sci.getFirmcode());
                            long usercode = Util.getIntValue(sci.getUsercode());
                            String serialNo1 = null;
                            if ((usbserver != null) && (!usbserver.equals(""))) {
                                UsbKeyProxy proxy = new UsbKeyProxy(usbserver);
                                serialNo1 = proxy.decrypt(firmcode, usercode, Long.parseLong(rnd), serial1);
                            } else {
                                serialNo1 = AuthenticUtil.decrypt(firmcode, usercode, Long.parseLong(rnd), serial1);
                            }
                            if (serial.equals("0"))
                                return "45";
                            if ((serial.equals("1")) || (serial.equals(serialNo)))
                                return "46";
                            if (serialNo.equals(serialNo1)) {
                                user.setNeedusb(needusb);
                                user.setSerial(serialNo);
                            } else {
                                if (serialNo1.equals("0")) {
                                    return "48";
                                }
                                return "47";
                            }
                        } else if ((needusbDt.equals("1")) && ("3".equals(usbType))) {
                            //qc172088 对于绑定了动态令牌的人员的逻辑是，在网段外需要使用动态令牌登录，在网段内不需要直接使用普通用户名、密码登录即可。
                            //	                		 * 当网段策略没有开启的时候，正常验证海泰key和动态令牌
                            //	                		 * 当网段策略开启的时候，网段内海泰key和动态令牌不做验证
                            //	                		 * 当网段策略开启的时候，网段外的海泰key和动态令牌验证
                            boolean isNeedIp = true;
                            int forbidLogin = Util.getIntValue(sci.getForbidLogin(), 0);
                            if (forbidLogin == 0) {
                                isNeedIp = false;
                                if (usbstate == 2 && !checktmp) isNeedIp = true;
                            } else {
                                isNeedIp = checkIpSegByForbidLogin(request, login_id);
                            }
                            if (!isNeedIp) {
                                String tokenAuthKey = Util.null2String(request.getParameter("tokenAuthKey"));
                                String tokenKey = Util.null2String(rs.getString("tokenKey"));
                                if (tokenKey.equals(""))
                                    return "120"; //未绑定令牌

                                else {
                                    TokenJSCX token = new TokenJSCX();
                                    boolean isTokenAuthKeyPass = false;

                                    RecordSet recordSet = new RecordSet();
                                    String sql = "select * from tokenJscx WHERE tokenKey=?";
                                    recordSet.executeQuery(sql, tokenKey);
                                    if (recordSet.next()) {
                                        if (tokenKey.startsWith("1"))
                                            isTokenAuthKeyPass = token.checkDLKey(tokenKey, tokenAuthKey);
                                        else if (tokenKey.startsWith("2"))
                                            isTokenAuthKeyPass = token.checkDLKey(tokenKey, tokenAuthKey);
                                        else if (tokenKey.startsWith("3"))
                                            isTokenAuthKeyPass = token.checkKey(tokenKey, tokenAuthKey);

                                        if (!isTokenAuthKeyPass)
                                            return "122"; //验证不通过
                                    } else
                                        return "120";     //令牌未进行初始化操作
                                }
                            }
                        } else if ((needusbHt.equals("1")) && (userUsbType.equals("2"))) {
                            String username1 = Util.null2String(rs.getString("loginid"));
                            String serialNo = rs.getString("serial");
                            HTSrvAPI htsrv = new HTSrvAPI();
                            String sharv = "";
                            sharv = htsrv.HTSrvSHA1(rnd, rnd.length());

                            sharv = sharv + "04040404";
                            String ServerEncData = htsrv.HTSrvCrypt(0, serialNo, 0, sharv);
                            if (serial.equals("0"))
                                return "45";
                            if (!username1.equals(username))
                                return "17";
                            if (!ServerEncData.equals(serial)) {
                                return "16";
                            }
                            user.setNeedusb(needusb);
                            user.setSerial(serialNo);
                        }
                    } else
                        user.setNeedusb(0);
                } else {
                    int needusbnetwork = Util.getIntValue(sci.getNeedusbnetwork());

                    boolean isSysadmin = false;
                    RecordSet rs1 = new RecordSet();
                    rs1.executeQuery("select count(loginid) from HrmResourceManager where loginid = ?", login_id);
                    if ((rs1.next()) && (rs1.getInt(1) > 0)) {
                        isSysadmin = true;
                    }

                    if ((needusbnetwork == 1) && (!isSysadmin)) {
                        if (checktmp) {
                            return "45";
                        }
                        user.setNeedusb(0);
                    } else {
                        user.setNeedusb(0);
                    }
                }

                user.setLoginip(Util.getIpAddr(request));
                //System.out.println(">>>>>>>>>>>>>>begin>>>>>>>>>>>>>>"+request.getSession(true).getId());
                String weaver_login_type = Util.null2String(request.getSession(true).getAttribute("weaver_login_type"));
                request.getSession(true).invalidate();
                //System.out.println(">>>>>>>>>>>>>>after>>>>>>>>>>>>>>"+request.getSession(true).getId());
                request.getSession(true).setAttribute("ep_loginsession",loginSession);
                request.getSession(true).setAttribute("weaver_login_type",weaver_login_type);
                request.getSession(true).setAttribute("weaver_user@bean", user);
                request.getSession(true).setAttribute("rtxlogin", "1");
//                if(login_file.trim().length()>10) {
//                    Util.setCookie(response, "loginfileweaver", login_file, 172800);
//                }
                Util.setCookie(response, "loginidweaver", user.getUID() + "", -1);
                Util.setCookie(response, "languageidweaver", Util.null2s(languageidweaver, "7"), -1);

                char separater = Util.getSeparator();
                rs.execute("HrmResource_UpdateLoginDate", rs.getString("id") + separater + currentdate);

                SysMaintenanceLog log = new SysMaintenanceLog();
                log.resetParameter();
                log.setRelatedId(rs.getInt("id"));
                log.setRelatedName((rs.getString("firstname") + " " + rs.getString("lastname")).trim());
                log.setOperateType("6");
                log.setOperateDesc("");
                log.setOperateItem("60");
                log.setOperateUserid(rs.getInt("id"));
                log.setClientAddress(request.getRemoteAddr());
                log.setSysLogInfo();
            } else if (login_type.equals("2")) {
                rs.execute("CRM_CustomerInfo_SByLoginID", login_id);
                if (rs.next()) {
                    if (rs.getString("deleted").equals("1")) {
                        return "17";
                    }
                    String salt = Util.null2String(rs.getString("salt"));
                    String portalPassword = rs.getString("PortalPassword");
                    if (salt.equals("")) {
                        //明文密码对比，兼容历史数据
                        if (!portalPassword.equals(user_password)) {
                            return "16";
                        }
                    } else {
                        //加密加盐后的密码对比
                        if (!portalPassword.equals(SM3Utils.getEncrypt(user_password, salt))) {
                            return "16";
                        }
                    }
                    if (!rs.getString("PortalStatus").equals("2")) {
                        return "17";
                    }
                    User user = new User();
                    user.setUid(rs.getInt("id"));
                    user.setLoginid(login_id);
                    user.setFirstname(rs.getString("name"));
                    //user.setLanguage(Util.getIntValue("7", 0));
                    String languageidweaver = Util.null2String(rs.getString("systemlanguage"), "7");
                    if (!languid.equalsIgnoreCase(languageidweaver) && ismutilangua) {
                        RecordSet rs2 = new RecordSet();
                        rs2.executeUpdate("update CRM_CustomerInfo set  language = ? where id =? ", languid, rs.getInt("id"));
                        languageidweaver = languid;
                    }
                    user.setLanguage(Util.getIntValue(languageidweaver, 7));
                    User.setUserLang4cus(rs.getInt("id"), Util.getIntValue(languid, 7));
                    user.setUserDepartment(Util.getIntValue(rs.getString("department"), 0));
                    user.setUserSubCompany1(Util.getIntValue(rs.getString("subcompanyid1"), 0));
                    user.setManagerid(rs.getString("manager"));
                    user.setCountryid(rs.getString("country"));
                    user.setEmail(rs.getString("email"));
                    user.setAgent(Util.getIntValue(rs.getString("agent"), 0));
                    user.setType(Util.getIntValue(rs.getString("type"), 0));
                    user.setParentid(Util.getIntValue(rs.getString("parentid"), 0));
                    user.setProvince(Util.getIntValue(rs.getString("province"), 0));
                    user.setCity(Util.getIntValue(rs.getString("city"), 0));
                    user.setLogintype("2");
                    user.setSeclevel(rs.getString("seclevel"));
                    user.setLoginip(request.getRemoteAddr());
                    request.getSession(true).setAttribute("weaver_user@bean", user);
                    request.getSession(true).setAttribute("rtxlogin", "1");

//                    Util.setCookie(response, "loginfileweaver", login_file, 172800);
                    Util.setCookie(response, "loginidweaver", user.getUID() + "", -1);
                    Util.setCookie(response, "languageidweaver", "7", -1);

                    String para = String.valueOf(rs.getInt("id")) + separator + currentdate + separator + currenttime + separator + request.getRemoteAddr();
                    rs.executeProc("CRM_LoginLog_Insert", para);
                } else {
                    return "15";
                }
            }
        } catch (Exception e) {
            writeLog(e);
            throw e;
        }
        return message;
    }

    private String beforeCheckUser(HttpServletRequest request, HttpServletResponse response) {
        RecordSet rs = new RecordSet();
        StaticObj staticobj = StaticObj.getInstance();
        Calendar today = Calendar.getInstance();
        String currentdate = Util.add0(today.get(1), 4) + "-" + Util.add0(today.get(2) + 1, 2) + "-" + Util.add0(today.get(5), 2);
        try {
            String loginid = Util.null2String(request.getParameter("loginid"));
            loginid = LoginBiz.getLoginId(loginid,request);
            String logintype = Util.null2String(request.getParameter("logintype"), "1");
            String validatecode = Util.null2String(request.getParameter("validatecode"));

            if (loginid.length()==0) {
                writeLog("loginid is null");
                return "99";
            }

            if (!checkLoginType(loginid, logintype)) {
                return "16";
            }

            if (!checkIpSegByForbidLogin(request, loginid)) {//判断是否开启了【禁止网段外登录】，如果开启了，判断是否在网段内
                if (checkIsNeedIp(loginid)) {
                    return "88";
                }
            }

            ChgPasswdReminder reminder = new ChgPasswdReminder();
            RemindSettings settings = reminder.getRemindSettings();
            int needvalidate = settings.getNeedvalidate();
            String validateRand = Util.null2String((String) request.getSession(true).getAttribute("validateRand")).trim();
            if (validateRand.length() == 0) {//从redis缓存中获取验证码
                String validateCodeKey = Util.null2String(request.getParameter("validateCodeKey"));
                if (validateCodeKey.length() > 0) {
                    validateRand = Util.null2String(Util_DataMap.getObjVal(validateCodeKey));
                    Util_DataMap.clearVal(validateCodeKey);
                }
            }
            int numvalidatewrong = settings.getNumvalidatewrong();
            int sumpasswordwrong = 0;

            boolean canpass = new VerifyPasswdCheck().getUserCheck(loginid, "", 1);
            if (canpass) {
                return "110";
            }

            rs.executeQuery("select isADAccount from hrmresource where loginid=?", loginid);
            if (rs.next()) {
                this.isADAccount = rs.getString("isADAccount");
            }

            if ((loginid.indexOf(";") > -1) || (loginid.indexOf("--") > -1) || (loginid.indexOf(" ") > -1) || (loginid.indexOf("'") > -1)) {
                return "16";
            }

            String isLicense = (String) staticobj.getObject("isLicense");

            LN ckLicense = new LN();
            try {
                if (!ckLicense.CkLicense(currentdate).equals("1")) {
                    return "19";
                } else {
                    staticobj.putObject("isLicense", "true");
                }
            } catch (Exception e) {
                return "19";
            }

            String concurrentFlag = Util.null2String(ckLicense.getConcurrentFlag());
            int hrmnumber = Util.getIntValue(ckLicense.getHrmnum());
            if ("1".equals(concurrentFlag)) {
                LicenseCheckLogin lchl = new LicenseCheckLogin();
                if (lchl.getLicUserCheck(loginid, hrmnumber)) {
                    recordFefuseLogin(loginid); //拒绝登陆记录
                    return "26";
                }

            }

            if (logintype.equals("1")) {
                if ((needvalidate == 1)) {
                    if (validateRand.trim().equals("") || "".equals(validatecode.trim())) {
                        return "52";
                    } else if ((sumpasswordwrong >= numvalidatewrong) && (!validateRand.toLowerCase().equals(validatecode.trim().toLowerCase()))) {
                        return "52";
                    }
                }
            }

            String software = (String) staticobj.getObject("software");
            String portal = "n";
            String multilanguage = "n";
            if (software == null) {
                rs.executeQuery("select * from license");
                if (rs.next()) {
                    software = rs.getString("software");
                    if (software.equals("")) {
                        software = "ALL";
                    }
                    staticobj.putObject("software", software);
                    portal = rs.getString("portal");
                    if (portal.equals("")) {
                        portal = "n";
                    }
                    staticobj.putObject("portal", portal);
                    multilanguage = rs.getString("multilanguage");
                    if (multilanguage.equals("")) {
                        multilanguage = "n";
                    }
                    staticobj.putObject("multilanguage", multilanguage);
                }
            }
        } catch (Exception e) {
            return "-1";
        }

        return "";
    }

    private void afterCheckUser(ServletContext application, HttpServletRequest request, HttpServletResponse response, String usercheck) {
        try {
            HttpSession session = request.getSession(true);
            session.removeAttribute("validateRand");
            session.setAttribute("isie", Util.null2String(request.getParameter("isie")));
            session.setAttribute("browser_isie", Util.null2String(request.getParameter("isie")));

            String loginid = Util.null2String(request.getParameter("loginid"));
            loginid = LoginBiz.getLoginId(loginid,request);
            String loginfile = Util.null2String(request.getParameter("loginfile"));

            new VerifyPasswdCheck().getUserCheck(loginid, usercheck, 2);

            User user = (User) request.getSession(true).getAttribute("weaver_user@bean");
            if (user == null)
                return;
            boolean MOREACCOUNTLANDING = GCONST.getMOREACCOUNTLANDING();
            if (MOREACCOUNTLANDING) {
                if (user.getUID() != 1) {
                    VerifyLogin VerifyLogin = new VerifyLogin();
                    List accounts = VerifyLogin.getAccountsById(user.getUID());
                    request.getSession(true).setAttribute("accounts", accounts);
                }
                //Util.setCookie(response, "loginfileweaver", loginfile, 172800);
                Util.setCookie(response, "loginidweaver", loginid, -1);
            }

            Map logmessages = (Map) application.getAttribute("logmessages");
            if (logmessages == null) {
                logmessages = new WHashMap();
                logmessages.put(user.getUID(), "");
                application.setAttribute("logmessages", logmessages);
            }
            if ((user != null) && (!loginid.equals(user.getLoginid())) && usercheck.equals("0")) {
                request.getSession(true).removeAttribute("weaver_user@bean");
                writeLog("VerifyLogin Error>>>>>>>>>>>>>>>>>>loginid==" + loginid + "user.getLoginid()==" + user.getLoginid());
            } else {
                RecordSet rs = new RecordSet();
                String loginuuids = user.getUID() + "";
                rs.executeQuery("select id from hrmresource where status in(0,1,2,3) and belongto = ? ", user.getUID());
                if (rs.next()) {
                    if (loginuuids.length() > 0)
                        loginuuids = loginuuids + ",";
                    loginuuids = loginuuids + rs.getInt("id");
                }
                Cookie ckloginuuids = new Cookie("loginuuids", loginuuids);
                ckloginuuids.setMaxAge(-1);
                ckloginuuids.setPath("/");
                response.addCookie(ckloginuuids);
                //writeLog("VerifyLogin successful>>>>>>>>>>>>>>>>>>loginid==" + loginid + "user.getLoginid()==" + user.getLoginid());

                Map userSessions = (Map) application.getAttribute("userSessions");
                String uId = String.valueOf(user.getUID());
                if (userSessions == null) {
                    userSessions = new java.util.concurrent.ConcurrentHashMap();
                }
                List slist = (List) userSessions.get(uId);
                slist = slist == null ? new ArrayList() : slist;
                slist.add(session);
                userSessions.put(uId, slist);
                application.setAttribute("userSessions", userSessions);
            }
        } catch (Exception localException) {
        }
    }

    private String[] checkUserPass(HttpServletRequest request, String loginid, String pass, String messages) {
        String ClientIP = Util.getIpAddr(request);
        String[] returnValue = new String[2];
        returnValue[0] = "-1";
        returnValue[1] = "-1";

        HrmSettingsComInfo sci = new HrmSettingsComInfo();
        int needdynapass_sys = Util.getIntValue(sci.getNeeddynapass());
        int dynapasslen = Util.getIntValue(sci.getDynapasslen());
        int needpassword = Util.getIntValue(sci.getNeedpassword());

        boolean ipaddress = false;
        int passwordstateip = 1;
        int needdynapass = 0;
        String mobile = "";

        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        String sql = "";
        String idTemp = "0";
        String passwordTemp = "";
        sql = "select id,needdynapass,mobile,usbstate as passwordstate from HrmResource where loginid=? and (accounttype is null  or accounttype=0)";
        rs.executeQuery(sql, loginid);
        if ((rs.next()) && (Util.getIntValue(rs.getString(1), 0) > 0)) {
            idTemp = rs.getString(1);
            returnValue[0] = "0";
            returnValue[1] = "0";
            needdynapass = rs.getInt(2);

            if (needdynapass == 1) {
                rs1.executeQuery("select id from hrmpassword where id=?", idTemp);
                if (!rs1.next()) {
                    rs1.executeUpdate("insert into hrmpassword(id,loginid,created) values(?,?,"+ DbFunctionUtil.getCurrentFullTimeFunction(rs.getDBType())+")", idTemp, loginid);
                }
            }

            sql = "select password,usbstate as passwordstate,salt from HrmResource where id= ?";
            rs.executeQuery(sql, idTemp);
            if (rs.next()) {
                passwordTemp = Util.null2String(rs.getString(1));
                String salt = rs.getString("salt");
                //lym   20191225    是否开启EP登录验证获取LoginSession
                boolean passwordCheck = false;
                if(getPropValue("epcheck","openeplogincheck").equals("1")){
                    this.loginSession = loginCheckUtil.getLoginSession(loginid,pass);
                    if(this.loginSession.length()>0)    passwordCheck=true;
                }else{
                    passwordCheck = pass.length()>0 && PasswordUtil.check(pass, passwordTemp, salt);
                }
                if (needdynapass != 1) {
                    new BaseBean().writeLog("needdynapass!=1");
                    if (passwordCheck){
                        returnValue[1] = "1";
                    }
                } else {
                    if (needdynapass_sys == 1) {
                        sql = "select password,usbstate as passwordstate from HrmResource where loginid=?";
                        rs2.executeQuery(sql, loginid);
                        if (rs2.next())
                            passwordstateip = rs2.getInt("passwordstate");
                    }
                    ipaddress = checkIpSeg(request, loginid, passwordstateip);

                    if((needpassword==0 &&passwordstateip!=1 && (dynapasslen > 0) && (ipaddress)) || ((passwordstateip == 0) || (passwordstateip == 2)) && (dynapasslen > 0) && (ipaddress)) {
                        rs.executeQuery("select password,salt from hrmpassword where id=?", idTemp);
                        String pswd = "";
                        if (rs.next()) {
                            pswd = StringUtil.vString(rs.getString(1));
                            String dySalt = rs.getString("salt");
                            String dynamicPassword = Util.null2String(request.getParameter("dynamicPassword"));
                            if (pswd.length() == 0) {
                                returnValue[1] = "730";
                            } else {
                                if(PasswordUtil.check(dynamicPassword, pswd, dySalt)){
                                    if(needpassword==1){
                                        if(passwordCheck){
                                            returnValue[1] = "1";
                                        }
                                    }else{
                                        returnValue[1] = "1";
                                    }
                                    if(returnValue[1].equals("1")){
                                        rs.executeUpdate("update hrmpassword set password='',created='' where id=?", idTemp);
                                    }
                                }
                            }
                        }
                    } else{
                        if(passwordCheck){
                            returnValue[1] = "1";
                        }
                    }
                }
            }
        } else {
            rs.executeProc("SystemSet_Select", "");

            rs.next();
            String detachable = Util.null2String(rs.getString("detachable"));
            sql = "select count(id),id from HrmResourceManager where loginid=? group by id";
            rs.executeQuery(sql, loginid);
            if ((rs.next()) && (Util.getIntValue(rs.getString(1), 0) > 0)) {
                if ((!detachable.equals("1")) && (!loginid.equalsIgnoreCase("sysadmin"))) {
                    returnValue[0] = "-1";
                    returnValue[1] = "0";
                    return returnValue;
                }

                idTemp = rs.getString(2);
                returnValue[0] = "1";
                returnValue[1] = "0";

                sql = "select password,userUsbType,usbstate,mobile,salt from HrmResourceManager where id= ?";
                rs.executeQuery(sql, idTemp);
                if (rs.next()) {
                    passwordTemp = Util.null2String(rs.getString(1));
                    String salt = rs.getString("salt");
                    needdynapass = rs.getInt(2);
                    boolean passwordCheck = pass.length()>0 && PasswordUtil.check(pass, passwordTemp, salt);
                    if (needdynapass != 4) {
                        if (PasswordUtil.check(pass, passwordTemp, salt))
                            returnValue[1] = "1";
                    } else {
                        if (needdynapass_sys == 1) {
                            sql = "select password,usbstate as passwordstate from HrmResourceManager where loginid=?";
                            rs2.executeQuery(sql, loginid);
                            if (rs2.next())
                                passwordstateip = rs2.getInt("passwordstate");
                        }
                        ipaddress = checkIpSeg(request, loginid, passwordstateip);

                        if ((needpassword==0 &&passwordstateip!=1&& (dynapasslen > 0) && (ipaddress))|| ((passwordstateip == 0) || (passwordstateip == 2)) && (dynapasslen > 0) && (ipaddress)) {
                            rs.executeQuery("select password,salt from hrmpassword where id=?", idTemp);
                            String pswd = "";
                            if (rs.next()) {
                                pswd = StringUtil.vString(rs.getString(1));
                                String dySalt = rs.getString("salt");
                                String dynamicPassword = Util.null2String(request.getParameter("dynamicPassword"));
                                if (pswd.length() == 0) {
                                    returnValue[1] = "730";
                                } else {
                                    if(PasswordUtil.check(dynamicPassword, pswd, dySalt)){
                                        if(needpassword==1){
                                            if(passwordCheck){
                                                returnValue[1] = "1";
                                            }
                                        }else{
                                            returnValue[1] = "1";
                                        }
                                        if(returnValue[1].equals("1")){
                                            rs.executeUpdate("update hrmpassword set password='',created='' where id=?", idTemp);
                                        }
                                    }
                                }
                            }
                        } else{
                            if(passwordCheck){
                                returnValue[1] = "1";
                            }
                        }
                    }
                }
            }
        }
        return returnValue;
    }

    public boolean checkIpSeg(HttpServletRequest request, String loginid, int passwordstateip) {
        String ClientIP = Util.getIpAddr(request);
        boolean ipaddress = true;

        HrmSettingsComInfo sci = new HrmSettingsComInfo();
        int needdynapass_sys = Util.getIntValue(sci.getNeeddynapass());
        if (needdynapass_sys == 1) {
            RecordSet rs = new RecordSet();

            String inceptipaddress = "";
            String endipaddress = "";
            String ipAddressType = "";
            String sql = "select * from HrmnetworkSegStr";
            rs.executeQuery(sql);

            while (rs.next()) {
                inceptipaddress = rs.getString("inceptipaddress");
                endipaddress = rs.getString("endipaddress");
                ipAddressType = rs.getString("ipAddressType");

                if (ipAddressType.equals("IPv4") && ClientIP.indexOf(".") > -1) {
                    long ip1 = IpUtils.ip2number(inceptipaddress);
                    long ip2 = IpUtils.ip2number(endipaddress);
                    long ip3 = IpUtils.ip2number(ClientIP);

                    if (passwordstateip == 2) {
                        if ((ip3 >= ip1) && (ip3 <= ip2)) {
                            ipaddress = false;
                            break;
                        }
                        if ((ip3 < ip1) || (ip3 > ip2)) {
                            ipaddress = true;
                        }
                    } else if (passwordstateip == 0) {
                        ipaddress = true;
                    } else if (passwordstateip == 1) {
                        ipaddress = false;
                        break;
                    }
                } else if (ipAddressType.equals("IPv6") && ClientIP.indexOf(":") > -1) {
                    String ip1 = IpUtils.parseAbbreviationToFullIPv6(inceptipaddress);
                    String ip2 = IpUtils.parseAbbreviationToFullIPv6(endipaddress);
                    String ip3 = IpUtils.parseAbbreviationToFullIPv6(ClientIP);

                    if (passwordstateip == 2) {
                        if (ip3.compareTo(ip1) >= 0 && ip3.compareTo(ip2) <= 0) {
                            ipaddress = false;
                            break;
                        }
                        if (ip3.compareTo(ip1) < 0 || ip3.compareTo(ip2) > 0) {
                            ipaddress = true;
                        }
                    } else if (passwordstateip == 0) {
                        ipaddress = true;
                    } else if (passwordstateip == 1) {
                        ipaddress = false;
                        break;
                    }
                }
            }
        }
        return ipaddress;
    }

    public boolean sendOk(String ln, String sDypadcon, int dynapasslen, String mobile, String time, String tmpid, String sValiditySec, String ip) {
        String dypadcon = Util.null2String(sDypadcon);
        String dynapass = "";
        if (dypadcon.equals("0"))
            dynapass = Util.passwordBuilderNo(dynapasslen);
        else if (dypadcon.equals("1"))
            dynapass = Util.passwordBuilderEn(dynapasslen);
        else if (dypadcon.equals("2")) {
            dynapass = Util.passwordBuilder(dynapasslen);
        }
        SMSManager sm = new SMSManager();
        boolean sendflag = sm.sendSMS(mobile, "您在" + time + "登录系统的动态密码为：" + dynapass + ip);
//        System.out.println("您在" + time + "登录系统的动态密码为：" + dynapass + ip);
//        sendflag = true;
        if (sendflag) {
            String[] pwdArr = PasswordUtil.encrypt(dynapass);
            RecordSet rs = new RecordSet();
            rs.executeUpdate("update hrmpassword set password=? ,salt=?, created="+ DbFunctionUtil.getCurrentFullTimeFunction(rs.getDBType())+" where id=?", pwdArr[0], pwdArr[1],tmpid);
            upPswdJob(tmpid, sValiditySec);
        }
        return sendflag;
    }

    private void upPswdJob(final String arg0, final String arg1) {
        final long sleeps = StringUtil.parseToLong(arg1, 120) * 1000;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(sleeps);
                    new RecordSet().executeUpdate("update hrmpassword set password='',created='' where id=?", arg0);
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    /**
     * 判断是否开启了【禁止网段外登录】，如果开启了，判断是否在网段内
     *
     * @param request
     * @return 是否被禁止登陆：false-不允许登录、true-允许登录
     */
    public boolean checkIpSegByForbidLogin(HttpServletRequest request, String loginId) {
        RecordSet rs = new RecordSet();
        rs.executeQuery("select * from HrmResourceManager where loginid = ?", loginId);
        if (rs.next()) return true;

        String ClientIP = Util.getIpAddr(request);
        if (ClientIP.equals("0:0:0:0:0:0:0:1")) return true;
        HrmSettingsComInfo sci = new HrmSettingsComInfo();
        int forbidLogin = Util.getIntValue(sci.getForbidLogin(), 0);//是否开启了【禁止网段外登录】：0-未开启、1-开启
        if (forbidLogin == 0) return true;

        boolean ipaddress = false;//是否被禁止登陆：false-不允许登录、true-允许登录
        String inceptipaddress = "";//网段策略起始地址
        String endipaddress = "";//网段策略截止地址
        String ipAddressType = "";//网段策略类型:IPv4、IPv6
        String sql = "select * from HrmnetworkSegStr";
        rs.executeQuery(sql);
        if (rs.getCounts() == 0) return false;
        while (rs.next()) {
            inceptipaddress = rs.getString("inceptipaddress");
            endipaddress = rs.getString("endipaddress");
            ipAddressType = rs.getString("ipAddressType").equals("IPv6") ? "IPv6" : "IPv4";

            if (ipAddressType.equals("IPv4") && ClientIP.indexOf(".") > -1) {
                long ip1 = IpUtils.ip2number(inceptipaddress);
                long ip2 = IpUtils.ip2number(endipaddress);
                long ip3 = IpUtils.ip2number(ClientIP);
                if (ip3 >= ip1 && ip3 <= ip2) {
                    ipaddress = true;
                    break;
                }
            } else if (ipAddressType.equals("IPv6") && ClientIP.indexOf(":") > -1) {
                String ip1 = IpUtils.parseAbbreviationToFullIPv6(inceptipaddress);
                String ip2 = IpUtils.parseAbbreviationToFullIPv6(endipaddress);
                String ip3 = IpUtils.parseAbbreviationToFullIPv6(ClientIP);
                if (ip3.compareTo(ip1) >= 0 && ip3.compareTo(ip2) <= 0) {
                    ipaddress = true;
                    break;
                }
            }
        }
        return ipaddress;
    }

    /**
     * 检测当前用户是否开启了辅助简阳方式，是否需要受网段策略控制
     *
     * @param loginId
     * @return
     */
    private boolean checkIsNeedIp(String loginId) {
        RecordSet rs = new RecordSet();
        rs.executeQuery("select userusbtype,usbstate from hrmresource where loginid=?", loginId);
        rs.next();
        String userusbtype = rs.getString("userusbtype");//辅助检验方式：2-海泰KEY、3-动态令牌
        String usbstate = rs.getString("usbstate");//辅助检验方式状态：0-启用、1-禁止、2-网段策略(位于网段策略内的人可直接登录，无需辅助检验。)
        //动态令牌 || 海泰key
        if ((userusbtype.equals("3") && !usbstate.equals("1")) || (userusbtype.equals("2") && !usbstate.equals("1"))) {
            return false;
        }

        return true;
    }

    private String[] getErrorMsg(ServletContext application, HttpServletRequest request, HttpServletResponse response, String msgid) {
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        String[] errorMsg = new String[5];
        int imsgid = Util.getIntValue(msgid, 0);
        errorMsg[0] = "false";
        errorMsg[1] = "" + imsgid;
        errorMsg[2] = "";
        errorMsg[3] = "";
        errorMsg[4] = "";
        int languageid = Util.getIntValue(request.getParameter("islanguid"), 0);
        if (languageid == 0) {//如何未选择，则默认系统使用语言为简体中文
            languageid = 7;
        }

        BirthdayReminder birth_reminder = new BirthdayReminder();
        RemindSettings settings = birth_reminder.getRemindSettings();
        if (settings == null) {
            return errorMsg;
        }
        String loginid = Util.null2String(request.getParameter("loginid"));
        loginid = LoginBiz.getLoginId(loginid,request);

        if (imsgid == 0) {// 登录成功
            errorMsg[0] = "true";
            errorMsg[2] = "登录成功";
            User user = (User) request.getSession().getAttribute("weaver_user@bean");
            String sessionId = request.getSession().getId();
            String access_token = AddToken(request, user, sessionId);
            errorMsg[4] = access_token;
        } else {
            if (imsgid == 16 || imsgid == 17) {

                if (!ldapError.isEmpty() && !"124919".equalsIgnoreCase(ldapError)) {
                    errorMsg[2] = SystemEnv.getHtmlLabelNames(ldapError, languageid);
                } else {
                    String userpassword = Util.null2String(request.getParameter("userpassword"));
                    String dynamicPassword = Util.null2String(request.getParameter("dynamicPassword"));
                    if(userpassword.length()>0 && dynamicPassword.length()>0){
                        errorMsg[2] = SystemEnv.getHtmlLabelName(508167, languageid);
                    }else if(dynamicPassword.length()>0){
                        errorMsg[2] = SystemEnv.getHtmlLabelName(508177, languageid);
                    }	else{
                        //errorMsg[2] = SystemEnv.getHtmlLabelName(124919, languageid);
                        errorMsg[2] = loginCheckUtil.getError_msc();
                    }

                    if (imsgid == 16) {
                        String OpenPasswordLock = settings.getOpenPasswordLock();//是否开启密码输入错误自动锁定
                        if ("1".equals(OpenPasswordLock)) {
                            String needPasswordLockMin = settings.getNeedPasswordLockMin();//是否需要自动解锁
                            String passwordLockReason = needPasswordLockMin.equals("1") ? "C" : "B";//账号锁定原因
                            String passwordLockMin = settings.getPasswordLockMin();//多少分钟后自动解锁
                            rs.executeQuery("select id from HrmResourceManager where loginid=?", loginid);
                            if (!rs.next()) {
                                String sql = "select sumpasswordwrong from hrmresource where loginid=?";
                                rs1.executeQuery(sql, loginid);
                                rs1.next();
                                int sumpasswordwrong = Util.getIntValue(rs1.getString(1));
                                int sumPasswordLock = Util.getIntValue(settings.getSumPasswordLock(), 3);
                                int leftChance = sumPasswordLock - sumpasswordwrong;
                                if (leftChance == 0) {
                                    String now = DateUtil.getFullDate();
                                    if (rs.getDBType().equalsIgnoreCase("oracle") || rs.getDBType().equalsIgnoreCase("dm") || rs.getDBType().equalsIgnoreCase("st")) {
                                        sql = "update HrmResource set passwordlock=1,sumpasswordwrong=0, passwordlocktime=to_date(?,'yyyy-mm-dd hh24:mi:ss'),passwordLockReason=? where loginid=?";
                                    } else {
                                        sql = "update HrmResource set passwordlock=1,sumpasswordwrong=0, passwordlocktime=?,passwordLockReason=? where loginid=?";
                                    }
                                    rs1.executeUpdate(sql, now, passwordLockReason, loginid);
                                    /*记录密码锁定的日志*/
                                    setIpAddress(Util.getIpAddr(request));
                                    recordPasswordLock(loginid);
                                    /*记录密码锁定的日志*/
                                    if (needPasswordLockMin.equals("1")) {
                                        errorMsg[2] = SystemEnv.getHtmlLabelName(24593, languageid) + sumPasswordLock + SystemEnv.getHtmlLabelName(18083, languageid)
                                                + "，" + SystemEnv.getHtmlLabelName(504522, languageid) + "，" + passwordLockMin + SystemEnv.getHtmlLabelName(504525, languageid)
                                                + " " + SystemEnv.getHtmlLabelName(504526, languageid);
                                    } else {
                                        errorMsg[2] = SystemEnv.getHtmlLabelName(24593, languageid) + sumPasswordLock + SystemEnv.getHtmlLabelName(18083, languageid)
                                                + "，" + SystemEnv.getHtmlLabelName(504522, languageid) + "，" + SystemEnv.getHtmlLabelName(504523, languageid);
                                    }
                                } else {
                                    errorMsg[2] = SystemEnv.getHtmlLabelName(24466, languageid) + leftChance + SystemEnv.getHtmlLabelName(24467, languageid);
                                }
                            }
                        }
                    }
                }
            } else if (imsgid == 26) {
                errorMsg[2] = SystemEnv.getHtmlLabelName(23656, languageid);
            } else if (imsgid == 45) {
                errorMsg[2] = SystemEnv.getHtmlLabelName(84259, languageid);
            } else if (imsgid == 46) {
                errorMsg[2] = SystemEnv.getHtmlLabelName(23656, languageid);
            } else if (imsgid == 122) {
                errorMsg[2] = SystemEnv.getHtmlLabelName(84268, languageid);
            } else if (imsgid == 110) {
                int sumPasswordLock = Util.getIntValue(settings.getSumPasswordLock(), 3);//输入密码错误累计多少次锁定账号
                int needPasswordLockMin = Util.getIntValue(settings.getNeedPasswordLockMin(), 0);//是否需要自动解锁
                String passwordLockMin = settings.getPasswordLockMin();//多少分钟后自动解锁
                String passwordLockReason = "";//账号被锁定的原因
                String sql = "select passwordLockReason from HrmResource where loginid=?";
                rs1.executeQuery(sql, loginid);
                if (rs1.next()) {
                    passwordLockReason = rs1.getString("passwordLockReason");
                }
                switch (passwordLockReason) {
                    case "A":
                        //您的账号已被管理员锁定，请联系系统管理员！
                        errorMsg[2] = SystemEnv.getHtmlLabelName(504527, languageid);
                        break;
                    case "B":
                    case "C":
                        if (needPasswordLockMin == 1) {
                            //您输入密码错误已达到X次，账号被锁定，Y分钟后自动解锁或联系管理员！
                            errorMsg[2] = SystemEnv.getHtmlLabelName(24593, languageid) + sumPasswordLock + SystemEnv.getHtmlLabelName(18083, languageid)
                                    + "，" + SystemEnv.getHtmlLabelName(504522, languageid) + "，" + passwordLockMin + SystemEnv.getHtmlLabelName(504525, languageid)
                                    + " " + SystemEnv.getHtmlLabelName(504526, languageid);
                        } else {
                            //您输入密码错误已达到X次，账号被锁定，请联系管理员！
                            errorMsg[2] = SystemEnv.getHtmlLabelName(24593, languageid) + sumPasswordLock + SystemEnv.getHtmlLabelName(18083, languageid)
                                    + "，" + SystemEnv.getHtmlLabelName(504522, languageid) + "，" + SystemEnv.getHtmlLabelName(504523, languageid);
                        }
                        break;
                    case "D":
                        //您长时间未登录系统，账号已被锁定，请联系管理员！
                        errorMsg[2] = SystemEnv.getHtmlLabelName(504528, languageid);
                        break;
                    default:
                        //您的账号已被管理员锁定，请联系系统管理员！
                        errorMsg[2] = SystemEnv.getHtmlLabelName(504527, languageid);
                        break;
                }
            } else if (imsgid == 730) {
                errorMsg[2] = SystemEnv.getHtmlLabelName(23771, languageid);
            } else if (imsgid == 19) {
                errorMsg[2] = SystemEnv.getHtmlLabelNames("18014,127353", languageid);
            } else if (imsgid == 88) {
                errorMsg[2] = SystemEnv.getHtmlLabelName(81628, languageid);
            } else if (imsgid == 99) {
                errorMsg[2] = SystemEnv.getHtmlLabelName(	386481, languageid);
            } else {
                errorMsg[2] = SystemEnv.getErrorMsgName(imsgid, languageid);
            }

        }
        return errorMsg;
    }

    /**
     * 拒绝登录记录
     *
     * @param loginid 登录人员的loginid
     */
    public void recordFefuseLogin(String loginid) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String currentdate = dateFormat.format(calendar.getTime());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        String sql = "select id from HrmRefuseCount where refuse_date=? and refuse_hour=? and refuse_loginid=?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql, currentdate, currentHour, loginid);
        if (!rs.next()) {
            sql = "insert into HrmRefuseCount(refuse_date,refuse_year,refuse_month,refuse_hour,refuse_loginid)" +
                    "values(?,?,?,?,?)";
            rs.executeUpdate(sql, currentdate, currentYear, currentMonth, currentHour, loginid);
        }
    }

    public boolean checkLoginType(String loginid, String loginType) {
        boolean flag = false;
        int docUserType = new HrmOrganizationVirtualUtil().getDocUserTypeByLoginid(loginid);
        if (loginType.equals("3")) {//公文登录页登录
            if (docUserType == 2 || docUserType == 3) {
                flag = true;
            }
        } else if (loginType.equals("1")) {
            if (docUserType == 1 || docUserType == 3) {
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 记录登录失败的日志
     *
     * @param loginId 登录账号
     * @param desc    登录失败的原因
     * @throws Exception
     */
    public void recordFailedLogin(String loginId, String desc) {
        try {
            SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
            sysMaintenanceLog.resetParameter();
            sysMaintenanceLog.setRelatedId(0);
            sysMaintenanceLog.setRelatedName(loginId);
            sysMaintenanceLog.setOperateType("302");
            sysMaintenanceLog.setOperateDesc(desc);
            sysMaintenanceLog.setOperateItem("503");
            sysMaintenanceLog.setOperateUserid(0);
            sysMaintenanceLog.setClientAddress(this.ipAddress);
            sysMaintenanceLog.setSysLogInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 密码被锁定的日志
     *
     * @param loginId 人员登陆账号
     * @throws Exception
     */
    public void recordPasswordLock(String loginId) {
        try {
            SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
            sysMaintenanceLog.resetParameter();
            sysMaintenanceLog.setRelatedId(0);
            sysMaintenanceLog.setRelatedName(loginId);
            sysMaintenanceLog.setOperateType("304");
            sysMaintenanceLog.setOperateDesc(SystemEnv.getHtmlLabelName(24706, 7));
            sysMaintenanceLog.setOperateItem("506");
            sysMaintenanceLog.setOperateUserid(0);
            sysMaintenanceLog.setClientAddress(this.ipAddress);
            sysMaintenanceLog.setSysLogInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * 记录登录失败的日志
     *
     * @param loginId   登录账号
     * @param desc      登录失败原因
     * @param ipAddress IP地址
     */
    public static void recordFailedLogin(String loginId, String desc, String ipAddress) {
        try {
            SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
            sysMaintenanceLog.resetParameter();
            sysMaintenanceLog.setRelatedId(0);
            sysMaintenanceLog.setRelatedName(loginId);
            sysMaintenanceLog.setOperateType("302");
            sysMaintenanceLog.setOperateDesc(desc);
            sysMaintenanceLog.setOperateItem("503");
            sysMaintenanceLog.setOperateUserid(0);
            sysMaintenanceLog.setClientAddress(ipAddress);
            sysMaintenanceLog.setSysLogInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 密码被锁定的日志
     *
     * @param loginId   登录账号
     * @param ipAddress IP地址
     */
    public static void recordPasswordLock(String loginId, String ipAddress) {
        try {
            SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
            sysMaintenanceLog.resetParameter();
            sysMaintenanceLog.setRelatedId(0);
            sysMaintenanceLog.setRelatedName(loginId);
            sysMaintenanceLog.setOperateType("304");
            sysMaintenanceLog.setOperateDesc(SystemEnv.getHtmlLabelName(24706, 7));
            sysMaintenanceLog.setOperateItem("506");
            sysMaintenanceLog.setOperateUserid(0);
            sysMaintenanceLog.setClientAddress(ipAddress);
            sysMaintenanceLog.setSysLogInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录登出日志
     *
     * @param user      目前登录人员
     * @param ipAddress IP地址
     */
    public static void recordLogout(User user, String ipAddress) {
        try {
            /*记录登出日志*/
            SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
            sysMaintenanceLog.resetParameter();
            sysMaintenanceLog.setRelatedId(user.getUID());
            sysMaintenanceLog.setRelatedName(user.getLastname());
            sysMaintenanceLog.setOperateType("303");
            sysMaintenanceLog.setOperateDesc(SystemEnv.getHtmlLabelName(25149, user.getLanguage()));
            sysMaintenanceLog.setOperateItem("505");
            sysMaintenanceLog.setOperateUserid(user.getUID());
            sysMaintenanceLog.setClientAddress(ipAddress);
            sysMaintenanceLog.setSysLogInfo();
            /*记录登出日志*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录登录日志
     *
     * @param hrmResourceId 人员ID
     * @param lastname      人员姓名
     * @param ipAddress     IP地址
     */
    public static void recordLogin(int hrmResourceId, String lastname, String ipAddress) {
        try {
            /*记录登入日志*/
            SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
            sysMaintenanceLog.resetParameter();
            sysMaintenanceLog.setRelatedId(hrmResourceId);
            sysMaintenanceLog.setRelatedName(lastname);
            sysMaintenanceLog.setOperateType("6");
            sysMaintenanceLog.setOperateDesc("");
            sysMaintenanceLog.setOperateItem("60");
            sysMaintenanceLog.setOperateUserid(hrmResourceId);
            sysMaintenanceLog.setClientAddress(ipAddress);
            sysMaintenanceLog.setSysLogInfo();
            /*记录登入日志*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}