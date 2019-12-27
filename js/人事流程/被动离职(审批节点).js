
var sqrqfield = WfForm.convertFieldNameToId("applicationdate");;//申请日期
var rzrqfield = WfForm.convertFieldNameToId("joineddate");;//入职日期
var fwnxyfield = WfForm.convertFieldNameToId("fwnxyserviceyearmonth");;//服务年限(月份)
var sysjbmfield = WfForm.convertFieldNameToId("sysjbmkfy");;//所有上级部门


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
            //WfForm.delDetailRow("detail_3","all");
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

jQuery(document).ready(function(){
    isHiddenDt3();
});




