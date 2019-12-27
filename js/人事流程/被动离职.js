
var sqrqfield = WfForm.convertFieldNameToId("applicationdate");//申请日期
var rzrqfield = WfForm.convertFieldNameToId("joineddate");//入职日期
var fwnxyfield = WfForm.convertFieldNameToId("fwnxy");//服务年限(月份)
var sysjbmfield = WfForm.convertFieldNameToId("sysjbmkfy");//所有上级部门
var marketfield = WfForm.convertFieldNameToId("market");//市场market
var zwxmfield = WfForm.convertFieldNameToId("zwxmfullname");;//中文姓名
var sfsyhobmfield = WfForm.convertFieldNameToId("sfsyhobm");;//是否属于HO部门

//申请日期
WfForm.bindFieldChangeEvent(sqrqfield, function(obj,id,value){
    takeoutFwnx();
});

//入职日期
WfForm.bindFieldChangeEvent(rzrqfield, function(obj,id,value){
    takeoutFwnx();
});

function takeoutFwnx(){
    jQuery.ajax({
        url: "/watsons/jsp/getXcyf.jsp?t=" + new Date().getTime(),
        data: {"ksrq": WfForm.getFieldValue(rzrqfield),"jsrq":WfForm.getFieldValue(sqrqfield)},
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
WfForm.bindFieldChangeEvent(sysjbmfield, function(obj,id,value){
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
    }else{

        showOrHiddenDt3("1");
        showOrHiddenCostinfo("2");
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
    //alert("showOrHiddenDt3:"+type);
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
    alert("sfsyhobmfield:"+sfsyhobmfield);
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




