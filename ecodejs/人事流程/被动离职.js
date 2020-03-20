
var sqrqfield = WfForm.convertFieldNameToId("applicationdate");//申请日期
var rzrqfield = WfForm.convertFieldNameToId("joineddate");//入职日期
var fwnxyfield = WfForm.convertFieldNameToId("fwnxy");//服务年限(月份)
var sysjbmfield = WfForm.convertFieldNameToId("sysjbmkfy");//所有上级部门
var marketfield = WfForm.convertFieldNameToId("market");//市场market
var zwxmfield = WfForm.convertFieldNameToId("zwxmfullname");;//中文姓名
var sfsyhobmfield = WfForm.convertFieldNameToId("sfsyhobm");;//是否属于HO部门
var bofedltfield = WfForm.convertFieldNameToId("balanceofentitledannuelleaveti");//Balance of Entitled Annuel Leave till to
var lzlxfield = WfForm.convertFieldNameToId("lzlx");//离职类型

//Total
var total_hiddenfield = WfForm.convertFieldNameToId("costcount");//隐藏Total字段
var total_xs_afield = WfForm.convertFieldNameToId("totalxsjca");//Total协商A
var total_xs_bfield = WfForm.convertFieldNameToId("totalxsjcb");//Total协商B
var total_htfield = WfForm.convertFieldNameToId("totalhtdqbxy");//Total合同
var total_ldfield = WfForm.convertFieldNameToId("totalldjczc");//Total劳动
var total_mkt_afield = WfForm.convertFieldNameToId("mkttotal");//Total(MKT_A)
var total_mkt_bfield = WfForm.convertFieldNameToId("mkttotal1");//Total(MKT_B)

//APPROVED Scenario
var approvedScenario_hofield = WfForm.convertFieldNameToId("approvedscenarioho");//APPROVED Scenario_HO
var approvedScenario_fhofield = WfForm.convertFieldNameToId("approvedscenariofho");//APPROVED Scenario_非HO


//申请日期
WfForm.bindFieldChangeEvent(bofedltfield, function(obj,id,value){
    takeoutFwnx();
});

//入职日期
WfForm.bindFieldChangeEvent(rzrqfield, function(obj,id,value){
    takeoutFwnx();
});

function takeoutFwnx(){
    //alert(WfForm.getFieldValue(rzrqfield)+"    ----    "+WfForm.getFieldValue(bofedltfield));
    jQuery.ajax({
        url: "/watsons/jsp/getXcyf.jsp?t=" + new Date().getTime(),
        //data: {"ksrq": WfForm.getFieldValue(rzrqfield),"jsrq":WfForm.getFieldValue(sqrqfield)},
        data: {"ksrq": WfForm.getFieldValue(rzrqfield),"jsrq":WfForm.getFieldValue(bofedltfield)},
        async: false,
        success: function (result) {
            //alert("服务年限:"+result);
            WfForm.changeFieldValue(""+fwnxyfield, {
                value: ""+result.trim(),
                specialobj: {
                    showhtml: ""+result.trim()
                }
            });
        }
    });
}

//所有上级部门
WfForm.bindFieldChangeEvent(sysjbmfield+","+lzlxfield, function(obj,id,value){
    isHiddenDt3();
});

//判断是否隐藏明细3，以前条件为隐藏
//1.部门属于WTCCN Head Office（ID:27）
function isHiddenDt3(){
    var result = 0 ;
    var sysjbm = WfForm.getFieldValue(sysjbmfield);
    if(sysjbm.indexOf(",27,")>=0){
        result = 1;
    }
    //隐藏并删除明细
    if(result==1){
        showOrHiddenDt3("2");
        showOrHiddenCostinfo("1");
        var lzlx = WfForm.getFieldValue(lzlxfield);
        if(lzlx=="0"){//合同到期
            showOrHiddenCostinfo2("1");
            showOrHiddenCostinfo1("2");
            showOrHiddenCostinfo3("2");
        }else if(lzlx=="1"){//协商解除
            showOrHiddenCostinfo1("1");
            showOrHiddenCostinfo2("2");
            showOrHiddenCostinfo3("2");
        }else if(lzlx=="2"){//劳动解除
            showOrHiddenCostinfo3("1");
            showOrHiddenCostinfo1("2");
            showOrHiddenCostinfo2("2");
        }else{
            showOrHiddenCostinfo1("2");
            showOrHiddenCostinfo2("2");
            showOrHiddenCostinfo3("2");
        }
    }else{
        showOrHiddenDt3("1");
        showOrHiddenCostinfo("2");
        showOrHiddenCostinfo1("2");
        showOrHiddenCostinfo2("2");
        showOrHiddenCostinfo3("2");
    }
}

function showOrHiddenDt3(type){
    //alert("showOrHiddenDt3:"+type);
    if(type=="1"){//显示
        jQuery(".dt3Div").each(function(){
            jQuery(this).show();
        })
    }else{
        //隐藏
        jQuery(".dt3Div").each(function(){
            jQuery(this).hide();
            //删除明细3所有行
            WfForm.delDetailRow("detail_3","all");
        })
    }
}

function showOrHiddenCostinfo(type){
    if(type=="1"){//显示
        jQuery(".costinfoDiv").each(function(){
            jQuery(this).show();
        })
    }else{
        //隐藏
        jQuery(".costinfoDiv").each(function(){
            jQuery(this).hide();
        })
    }
}

//协商解除DIV
function showOrHiddenCostinfo1(type){
    if(type=="1"){//显示
        jQuery(".costinfoDiv1").each(function(){
            jQuery(this).show();
        })
    }else{
        //隐藏
        jQuery(".costinfoDiv1").each(function(){
            jQuery(this).hide();
        })
    }
}

//合同到期不续约DIV
function showOrHiddenCostinfo2(type){
    if(type=="1"){//显示
        jQuery(".costinfoDiv2").each(function(){
            jQuery(this).show();
        })
    }else{
        //隐藏
        jQuery(".costinfoDiv2").each(function(){
            jQuery(this).hide();
        })
    }
}

//劳动解除仲裁DIV
function showOrHiddenCostinfo3(type){
    if(type=="1"){//显示
        jQuery(".costinfoDiv3").each(function(){
            jQuery(this).show();
        })
    }else{
        //隐藏
        jQuery(".costinfoDiv3").each(function(){
            jQuery(this).hide();
        })
    }
}


WfForm.bindFieldChangeEvent(total_xs_afield+","+total_xs_bfield+","+total_htfield+","+total_ldfield+","+total_mkt_afield+","+total_mkt_bfield+
    ","+approvedScenario_hofield+","+approvedScenario_fhofield+","+lzlxfield, function(obj,id,value){
    countTotal();
});

/**
 * 获取对应的Total金额
 * 1.如果是HO
 *  a)如果是“合同到期”，获取合同到期不续约Total
 *  b)如果是“协商解除”，还要根据(Approved Scenario_Ho)获取协商解除Total
 *  c)如果是“劳动解除”，获取劳动解除不续约Total
 * 2.非HO
 *  根据(Approved Scenario_非Ho)获取MKT_Total解除Totle
 */
function countTotal(){
    var result = 0 ;
    var sysjbm = WfForm.getFieldValue(sysjbmfield);
    if(sysjbm.indexOf(",27,")>=0){
        result = 1;
    }

    var total = 0;
    if(result==1){//HO
        var lzlx = WfForm.getFieldValue(lzlxfield);
        if(lzlx=="0"){//合同到期
            total = WfForm.getFieldValue(total_htfield);
        }else if(lzlx=="1"){//协商解除
            var approvedScenario_ho = WfForm.getFieldValue(approvedScenario_hofield);
            if(approvedScenario_ho=="0"){//A
                total = WfForm.getFieldValue(total_xs_afield);
            }else if(approvedScenario_ho=="1"){//B
                total = WfForm.getFieldValue(total_xs_bfield);
            }
        }else if(lzlx=="2"){//劳动解除
            total = WfForm.getFieldValue(total_ldfield);
        }
    }else{//非HO
        var approvedScenario_fho = WfForm.getFieldValue(approvedScenario_fhofield);
        if(approvedScenario_fho=="0"){//A
            total = WfForm.getFieldValue(total_mkt_afield);
        }else if(approvedScenario_fho=="1"){//B
            total = WfForm.getFieldValue(total_mkt_bfield);
        }
    }
    WfForm.changeFieldValue(total_hiddenfield, {value:""+total});
}

//姓名
WfForm.bindFieldChangeEvent(zwxmfield, function(obj,id,value){
    jQuery.ajax({
        url: "/watsons/jsp/getL5Dept.jsp?t=" + new Date().getTime(),
        data: {"uid": value},
        async: false,
        success: function (result) {
            //alert("部门:"+result);
            var temp = result.trim().split(",");
            WfForm.changeFieldValue(marketfield, { value: temp[0], specialobj:[ {id:temp[0],name:temp[1]} ] });
        }
    });
});

//
WfForm.bindFieldChangeEvent(sysjbmfield, function(obj,id,value){
    var sysjbm = WfForm.getFieldValue(sysjbmfield);
    if(sysjbm.indexOf(",27,")>=0){
        WfForm.changeFieldValue(sfsyhobmfield,{value:"0"});
    }else{
        WfForm.changeFieldValue(sfsyhobmfield,{value:"1"});
    }
});



jQuery(document).ready(function(){
    isHiddenDt3();
    takeoutFwnx();
});




