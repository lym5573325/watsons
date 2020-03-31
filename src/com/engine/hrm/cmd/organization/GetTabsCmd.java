package com.engine.hrm.cmd.organization;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTabsCmd extends AbstractCommonCommand<Map<String, Object>> {

    public GetTabsCmd(Map<String, Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> retmap = new HashMap<String,Object>();
        try{
            List<Map<String,Object>> tabs = new ArrayList<Map<String,Object>>();
            Map<String,Object> tab = null;
            String type = Util.null2String(params.get("type"));

            if(type.equals("company")){
                tab = new HashMap<String, Object>();
                tab.put("key", "1");
                tab.put("title", SystemEnv.getHtmlLabelName(33209,user.getLanguage()));
                tabs.add(tab);

                tab = new HashMap<String, Object>();
                tab.put("key", "2");
                tab.put("title", SystemEnv.getHtmlLabelName(17898,user.getLanguage()));
                tabs.add(tab);
            }else if(type.equals("subcompany")){
                tab = new HashMap<String, Object>();
                tab.put("key", "1");
                tab.put("title", SystemEnv.getHtmlLabelName(32816,user.getLanguage()));
                tabs.add(tab);

                tab = new HashMap<String, Object>();
                tab.put("key", "2");
                tab.put("title", SystemEnv.getHtmlLabelName(17898,user.getLanguage()));
                tabs.add(tab);

                tab = new HashMap<String, Object>();
                tab.put("key", "3");
                tab.put("title", SystemEnv.getHtmlLabelName(17587,user.getLanguage()));
                tabs.add(tab);
            }else if(type.equals("department")){
                tab = new HashMap<String, Object>();
                tab.put("key", "1");
                tab.put("title", SystemEnv.getHtmlLabelName(16289,user.getLanguage()));
                tabs.add(tab);
                /*
                tab = new HashMap<String, Object>();
                tab.put("key", "2");
                tab.put("title", SystemEnv.getHtmlLabelName(17587,user.getLanguage()));
                tabs.add(tab);

                 */
                /*
                tab = new HashMap<String, Object>();
                tab.put("key", "3");
                tab.put("title", SystemEnv.getHtmlLabelName(179,user.getLanguage()));
                tabs.add(tab);

                 */
            }
            retmap.put("status", "1");
            retmap.put("tabs", tabs);
        }catch (Exception e) {
            retmap.put("status", "-1");
            retmap.put("message",  SystemEnv.getHtmlLabelName(382661,user.getLanguage()));
            writeLog(e);
        }

        return retmap;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }
}
