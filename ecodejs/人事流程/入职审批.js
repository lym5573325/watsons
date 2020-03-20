var ygxzfield = WfForm.convertFieldNameToId("ygxzstafftype");//用工性质
var htksrqfield = WfForm.convertFieldNameToId("htksrq");//合同开始日期
var htjsrqfield = WfForm.convertFieldNameToId("htjsrq");//合同结束日期

/**
 * 如果用功性质为正式工->合同结束日期=合同开始日期+3年-1天
 */
function takeOutHtjsrq(){
    var htksrq = WfForm.getFieldValue(htksrqfield);
    if(WfForm.getFieldValue(ygxzfield)=="1" && htksrq!=""){
        var date = new Date(htksrq.replace(/-/g,"/"));
        date.setFullYear(date.getFullYear()+3);
        date.setDate(date.getDate()-1);
        WfForm.changeFieldValue(htjsrqfield, {
            value: ""+getFormatDate(date),
            specialobj: {
                showhtml: ""+getFormatDate(date)
            }
        });
    }
}

/**
 * 格式化时间
 * @param date
 * @returns {string}
 */
function getFormatDate(date) {
    var seperator1 = "-";
    var seperator2 = ":";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if (hour >= 1 && hour <= 9) {
        hour = "0" + hour;
    }
    if (minute >= 1 && minute <= 9) {
        minute = "0" + minute;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

jQuery(document).ready(function(){

    //用功性质、合同开始日期
    WfForm.bindFieldChangeEvent(ygxzfield+","+htksrqfield, function(obj,id,value){
        takeOutHtjsrq();//如果用功性质为正式工->合同结束日期=合同开始日期+3年-1天
    });
})