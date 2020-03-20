
var num1feild = "field7441";
var num2feild = "field7442";
var num3feild = "field7443";
var num4feild = "field7444";
var num5feild = "field7445";
var num6feild = "field7446";
var num7feild = "field7447";
var num8feild = "field7448";
var num9feild = "field7449";
var num10feild = "field7450";
var num11feild = "field7451";
var num12feild="field16325";
var zznumfield = "field7461";
WfForm.bindFieldChangeEvent("field7441,field7442,field7443,field7444,field7445,field7446,field7447,field7448,field7449,field7450,field7451,field16325",function(obj,id,value){

    var num = 12;
    var sum = 0;
    var num1 = jQuery("#"+num1feild).val();
    var num2 = jQuery("#"+num2feild).val();
    var num3 = jQuery("#"+num3feild).val();
    var num4 = jQuery("#"+num4feild).val();
    var num5 = jQuery("#"+num5feild).val();
    var num6 = jQuery("#"+num6feild).val();
    var num7 = jQuery("#"+num7feild).val();
    var num8 = jQuery("#"+num8feild).val();
    var num9 = jQuery("#"+num9feild).val();
    var num10 = jQuery("#"+num10feild).val();
    var num11 = jQuery("#"+num11feild).val();
    var num12 = jQuery("#"+num12feild).val();
    //1
    if(num1 == "6" || num1 == ""){
        num = parseInt(num)-1;
        num1 = 0;
    }else{
        num1 = parseInt(num1)+1;
    }
    //2
    if(num2 == "6" || num2 == ""){
        num = parseInt(num)-1;
        num2 = 0;
    }else{
        num2 = parseInt(num2)+1;
    }
    //3
    if(num3 == "6" || num3 == ""){
        num = parseInt(num)-1;
        num3 = 0;
    }else{
        num3 = parseInt(num3)+1;
    }
    //4
    if(num4 == "6" || num4 == ""){
        num = parseInt(num)-1;
        num4 = 0;
    }else{
        num4 = parseInt(num4)+1;
    }
    //5
    if(num5 == "6" || num5 == ""){
        num = parseInt(num)-1;
        num5 = 0;
    }else{
        num5 = parseInt(num5)+1;
    }
    //6
    if(num6 == "6" || num6 == ""){
        num = parseInt(num)-1;
        num6 = 0;
    }else{
        num6 = parseInt(num6)+1;
    }
    //7
    if(num7 == "6" || num7 == ""){
        num = parseInt(num)-1;
        num7 = 0;
    }else{
        num7 = parseInt(num7)+1;
    }
    //8
    if(num8 == "6" || num8 == ""){
        num = parseInt(num)-1;
        num8 = 0;
    }else{
        num8 = parseInt(num8)+1;
    }
    //9
    if(num9 == "6" || num9 == ""){
        num = parseInt(num)-1;
        num9 = 0;
    }else{
        num9 = parseInt(num9)+1;
    }
    //10
    if(num10 == "6" || num10 == ""){
        num = parseInt(num)-1;
        num10 = 0;
    }else{
        num10 = parseInt(num10)+1;
    }
    //11
    if(num11 == "6" ||　num11 == ""){
        num = parseInt(num)-1;
        num11 = 0;
    }else{
        num11 = parseInt(num11)+1;
    }
    //12
    if(num12 == "6" ||　num12 == ""){
        num = parseInt(num)-1;
        num12 = 0;
    }else{
        num12 = parseInt(num12)+1;
    }
    sum = parseInt(num1)+parseInt(num2)+parseInt(num3)+parseInt(num4)+parseInt(num5)+parseInt(num6)+parseInt(num7)+parseInt(num8)+parseInt(num9)+parseInt(num10)+parseInt(num11)+parseInt(num12);
    //alert(sum);
    //alert(num);
    var avg = Math.round(sum/num);
    //alert(avg);
    avg = parseInt(avg)-1;
    WfForm.changeFieldValue("field7461", {value:avg});
    //alert(avg);
});
function DateAdd(interval, number, date) {
    switch (interval) {
        case "y ": {
            date.setFullYear(date.getFullYear() + number);
            return date;
            break;
        }
        case "q ": {
            date.setMonth(date.getMonth() + number * 3);
            return date;
            break;
        }
        case "m ": {
            date.setMonth(date.getMonth() + number);
            return date;
            break;
        }
        case "w ": {
            date.setDate(date.getDate() + number * 7);
            return date;
            break;
        }
        case "d ": {
            date.setDate(date.getDate() + number);
            return date;
            break;
        }
        case "h ": {
            date.setHours(date.getHours() + number);
            return date;
            break;
        }
        case "m ": {
            date.setMinutes(date.getMinutes() + number);
            return date;
            break;
        }
        case "s ": {
            date.setSeconds(date.getSeconds() + number);
            return date;
            break;
        }
        default: {
            date.setDate(d.getDate() + number);
            return date;
            break;
        }
    }
}

/**
 * 试用期评估结果绑定事件
 * Depft Approve节点
 * 1.当直属上司选"通过",则带出"生效日期（试用期结束日期+1天）"
 * 2.当直属上司选择"不通过",清空终止日期(日期不能超过试用期结束日期)
 */
WfForm.bindFieldChangeEvent("field7475",function(obj,id,value){
    //alert("试用期评估结果绑定事件");
    if(value=="0"){//通过
        //清空终止日期
        WfForm.changeFieldValue("field7477", {
            value: "",
            specialobj: {
                showhtml: ""
            }
        });

        var syqjsrq = WfForm.getFieldValue("field7429"); //试用期结束日期
        //alert("试用期结束日期:"+syqjsrq);
        if(syqjsrq!=""){
            var syqjsrq_date = new Date(syqjsrq);
            syqjsrq_date.setDate(syqjsrq_date.getDate()+1);
            var sxrq = formatDate(syqjsrq_date);
            //alert("sxrq:"+sxrq);
            WfForm.changeFieldValue("field7476", {
                value: ""+sxrq,
                specialobj: {
                    showhtml: ""+sxrq
                }
            });
        }
    }else if(value=="1"){//不通过
        //清空终止日期
        WfForm.changeFieldValue("field7477", {
            value: "",
            specialobj: {
                showhtml: ""
            }
        });
        //清空试用期结束日期
        WfForm.changeFieldValue("field7476", {
            value: "",
            specialobj: {
                showhtml: ""
            }
        });

    }
});

function formatDate(date) {
    var date = new Date(date);
    var YY = date.getFullYear() + '-';
    var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
    var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    return YY + MM + DD;
}

jQuery().ready(function(){
    alert("111");
    var rzrq = WfForm.getFieldValue("field7384");
    var now = new Date(rzrq);
    var newDate = DateAdd( "m ",6,now);
    var now1 = new Date(newDate);
    var newDate1 = DateAdd( "d ",-1,now1);
    var oldTime = (new Date(newDate1)).getTime();
    var result = new Date(oldTime);
    var mm = result.getMonth() + 1;
    var ded = result.getDate();
    if(mm<10){
        mm = "0"+mm;
    }
    if(ded<10){
        ded = "0"+ded;
    }
    var kk = result.getFullYear() + "-" + mm+ "-" + ded;
    //WfForm.changeFieldValue("field7476", {value:kk});
    WfForm.changeFieldValue("field7429", {value:kk});

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT,function(callback){
        //... 执行自定义逻辑

        var syqpgjg = WfForm.getFieldValue("field7475");
        //alert(syqpgjg);
        var syqjsrq = WfForm.getFieldValue("field7429"); //field7429
        var jqzssyrq = WfForm.getFieldValue("field7477"); //field7477
        if(syqpgjg == 1 && jqzssyrq > syqjsrq){
            window.top.Dialog.alert("建议终止聘用生效日不能大于试用期结束日期");
            return;
        }
        callback();
    });
    alert("222");
    //alert(kk);
});



