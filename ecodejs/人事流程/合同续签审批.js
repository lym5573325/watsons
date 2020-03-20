var syfhtqxfield = WfForm.convertFieldNameToId("syfhtqxpreviouscontractperiod");//上一份合同期限 （开始日期）
var rzrqfield = WfForm.convertFieldNameToId("joineddate");//入职日期
var xhtlxfield = WfForm.convertFieldNameToId("xhtlxnescontracttype");//新合同类型
var htqxfield = WfForm.convertFieldNameToId("htqxstaffingadmin");//合同期限
var xhtqxkssjfield = WfForm.convertFieldNameToId("xhtqxnewcontractperiod");//新合同期限开始时间
var xhtqxjssjfield = WfForm.convertFieldNameToId("xhtqxz");//新合同期限结束时间
var htqxlxgsfield = WfForm.convertFieldNameToId("htqxstaffingadmin");//合同期限类型（公司）


function DateAdd(interval, number, date) {
    switch (interval) {
        case "y ":
        {
            date.setFullYear(date.getFullYear() + number);
            return date;
            break;
        }
        case "q ":
        {
            date.setMonth(date.getMonth() + number * 3);
            return date;
            break;
        }
        case "m ":
        {
            date.setMonth(date.getMonth() + number);
            return date;
            break;
        }
        case "w ":
        {
            date.setDate(date.getDate() + number * 7);
            return date;
            break;
        }
        case "d ":
        {
            date.setDate(date.getDate() + number);
            return date;
            break;
        }
        case "h ":
        {
            date.setHours(date.getHours() + number);
            return date;
            break;
        }
        case "m ":
        {
            date.setMinutes(date.getMinutes() + number);
            return date;
            break;
        }
        case "s ":
        {
            date.setSeconds(date.getSeconds() + number);
            return date;
            break;
        }
        default:
        {
            date.setDate(d.getDate() + number);
            return date;
            break;
        }
    }
}

/**
 * 1、IF(入职日期=上一份合同的开始日期)，则新合同类型为续签，新合同期限结束时间为新合同期限开始时间加3年
 * 2.IF(如果入职日期不等于上一份合同开始日期)，则新合同类型默认第二次续签，且合同期限（Staffing admin）默认为无固定期限，新合同期限结束时间为空
 */
function default1(){
    //alert("default1");
    var rzrq = WfForm.getFieldValue(rzrqfield);
    var syfhtqx = WfForm.getFieldValue(syfhtqxfield);
    //alert("rzrq:"+rzrq+"    syfhtqx:"+syfhtqx);
    if(rzrq == syfhtqx){//等于
        WfForm.changeFieldValue(xhtlxfield,{value:"1"});
        WfForm.changeFieldValue(xhtqxjssjfield, { value: "", specialobj:[ {id:"",name:""}] });
    }else{//不等于
        WfForm.changeFieldValue(xhtlxfield,{value:"2"});
        WfForm.changeFieldValue(htqxfield,{value:"0"});
        //新合同期限结束时间为新合同期限开始时间加3年

        WfForm.changeFieldValue(xhtqxjssjfield, { value: "", specialobj:[ {id:"",name:""}] });
    }
}

/**
 * 1、入职日期=上一份合同的开始日期,且合同期限（Staffing admin）不能选择无固定期限。
 *
 */
function forbidSelect(){
    //alert("forbidSelect");
    var rzrq = WfForm.getFieldValue(rzrqfield);
    var syfhtqx = WfForm.getFieldValue(syfhtqxfield);
    var htqx = WfForm.getFieldValue(htqxfield);
    //alert("rzrq:"+rzrq+"    syfhtqx:"+syfhtqx+"    htqx:"+htqx);
    if(rzrq == syfhtqx && htqx=="0"){//等于
        WfForm.changeFieldValue(htqxfield,{value:""});
        alert("合同期限不允许选择''无固定期限''!");
    }
}
/**
 * 1、IF(入职日期=上一份合同的开始日期)，新合同期限结束时间为新合同期限开始时间加3年
 * 2.IF(如果入职日期不等于上一份合同开始日期)，新合同期限结束时间为空
 */
function default2(){
    //alert("default2");
    var rzrq = WfForm.getFieldValue(rzrqfield);
    var syfhtqx = WfForm.getFieldValue(syfhtqxfield);
    //alert("rzrq:"+rzrq+"    syfhtqx:"+syfhtqx);
    if(rzrq == syfhtqx){//等于
        var xhtqxkssj = WfForm.getFieldValue(xhtqxkssjfield);
        //alert("xhtqxkssj："+xhtqxkssj);
        if(xhtqxkssj!=""){
            var xhtqxjssj = DateAdd("y ", 3,new Date("2019-12-02".replace(/-/g,  "/")));
            var xhtqxjssj = DateAdd("y ", 3,new Date(xhtqxkssj.replace(/-/g,  "/")));
            WfForm.changeFieldValue(xhtqxjssjfield, { value: xhtqxjssj, specialobj:[ {id:xhtqxjssj,name:xhtqxjssj}] });
        }
    }else{//不等于
        WfForm.changeFieldValue(xhtqxjssjfield, { value: "", specialobj:[ {id:"",name:""}] });
    }
}

function showTip(){
    var htqxlxgs = WfForm.getFieldValue(htqxlxgsfield);
    if(htqxlxgs=="0") jQuery(".tip1").show();
    else jQuery(".tip1").hide();
}

jQuery(document).ready(function(){
    forbidSelect();
    showTip();

    WfForm.bindFieldChangeEvent(rzrqfield+","+syfhtqxfield, function(obj,id,value){
        default1();
        default2();
        forbidSelect();
    });

    WfForm.bindFieldChangeEvent(xhtqxkssjfield, function(obj,id,value){
        default2();
    });

    WfForm.bindFieldChangeEvent(htqxlxgsfield, function(obj,id,value){
        showTip();
    });
})