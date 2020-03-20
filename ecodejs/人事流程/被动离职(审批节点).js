
var sqrqfield = WfForm.convertFieldNameToId("applicationdate");;//申请日期
var rzrqfield = WfForm.convertFieldNameToId("joineddate");;//入职日期
var fwnxyfield = WfForm.convertFieldNameToId("fwnxyserviceyearmonth");;//服务年限(月份)
var sysjbmfield = WfForm.convertFieldNameToId("sysjbmkfy");;//所有上级部门
var lzlxfield = WfForm.convertFieldNameToId("lzlx");//离职类型



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

jQuery(document).ready(function(){
    isHiddenDt3();
});




