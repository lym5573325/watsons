var ksrqfield = WfForm.convertFieldNameToId("zhgzrq");//最后工作日
var zhgzrkfyfield = WfForm.convertFieldNameToId("zhgzrqkfy");//最后工作日
var jsrqfield = WfForm.convertFieldNameToId("syqjsrq");//结束日期
var rzrqfield = WfForm.convertFieldNameToId("joineddate");//入职日期
var ygztfield = WfForm.convertFieldNameToId("ygxzstafftype");//员工状态
var sqrqfield = WfForm.convertFieldNameToId("applicationdate");//申请日期
var fwnxyfield = WfForm.convertFieldNameToId("fwnxy");//服务年限(月)
var sfymrdczhgzrfield = WfForm.convertFieldNameToId("sfymrdczhgzr");//是否已默认带出最后工作日
var yzhgzrqkfyfield = WfForm.convertFieldNameToId("yzhgzrqkfy");//原最后工作日期
var sfwzhgzrfield = WfForm.convertFieldNameToId("sfwzhgzr");//是否为最后工作日

var sjhmfield = WfForm.convertFieldNameToId("lxdh");//联系电话
var yxfield = WfForm.convertFieldNameToId("gryx");//个人邮箱
var hr7field = WfForm.convertFieldNameToId("HR7");//Complete Social Insurance 社会保险结算至
var hr8field = WfForm.convertFieldNameToId("HR8");//Housing Fund 住房公积金结算至

/**
 * 格式化时间
 * @param date
 * @returns {string}
 */
function getNowFormatDate(date) {
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
    var currentdate = year + seperator1 + month + seperator1 + strDate + " " + hour + seperator2 + minute;
    return currentdate;
}

//比较两个日期差  格式:yyyy-MM-dd		DateOne-DateTwo
function daysBetween(DateOne,DateTwo)
{
    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));
    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);
    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));

    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));
    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);
    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));

    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);
    return cha;
}

function dateAdd(rq,addDay){
    return getNowFormatDate(new Date(new Date(rq.replace(/-/g,'/')).getTime() + 24*60*60*1000*parseInt(addDay))).substring(0,10);
}

/**
 * 带出最后工作日
 */
function takeoutKsrq(){
    var ygzt = WfForm.getFieldValue(ygztfield);
    var sqrq = WfForm.getFieldValue(sqrqfield);
    //var sqrq = "2019-12-15";
    //alert("带出最后工作日期         申请日期:"+sqrq+"    用工状态:"+ygzt);
    var addDay = 3;
    //alert("天数:"+daysBetween(WfForm.getFieldValue(sqrqfield), WfForm.getFieldValue(rzrqfield)));
    if((ygzt=="21" && ygzt=="22") || (ygzt=="1" && daysBetween(WfForm.getFieldValue(sqrqfield), WfForm.getFieldValue(rzrqfield)) >=180))    addDay=30;
    var zhgzrkfy = dateAdd(sqrq,addDay);
    //alert("addDay:"+addDay+"   zhgzrkfy:"+zhgzrkfy);
    WfForm.changeFieldValue(""+zhgzrkfyfield, {
        value: ""+zhgzrkfy,
        specialobj:[
            {id:""+zhgzrkfy,name:""+zhgzrkfy}
        ]
    });


}

/**
 * 带出试用结束日期
 */
function takeoutJsrq(){
    var ygzt = WfForm.getFieldValue(ygztfield);
    //alert("takeoutjsrq:    ygzt:"+ygzt+"   rzrq:"+WfForm.getFieldValue(rzrqfield));
    if(WfForm.getFieldValue(jsrqfield)=="" && (ygzt=="22" || ygzt=="23")){//实习生
        jQuery.ajax({
            url: "/watsons/jsp/getSyjsrq.jsp?t=" + new Date().getTime(),
            data: {"ksrq": WfForm.getFieldValue(rzrqfield)},
            async: false,
            success: function (result) {
                //alert("result:"+result);
                WfForm.changeFieldValue(""+jsrqfield , {
                    value: ""+result.trim(),
                    specialobj:[
                        {id:""+result.trim(),name:""+result.trim()}
                    ]
                });
            }
        });
    }
    //alert("takeout jsrq end!!!");
}

/**
 * 用工性质变更事件
 */
WfForm.bindFieldChangeEvent(ygztfield+","+sqrqfield, function(obj,id,value){
    //alert("用工性质变更事件");
    //带出最后工作日期
    takeoutKsrq();
    //带出试用结束日期
    takeoutJsrq();
});

/**
 * 最后工作日期(开发用)
 */
WfForm.bindFieldChangeEvent(zhgzrkfyfield, function(obj,id,value) {
    //alert("最后工作日期(开发用)变更事件");
    takeoutzhgzr();
    //alert("最后工作日期(开发用)变更事件2222");
    //带出年限
    takeoutFwnx();
});

/**
 * 入职日期
 */
WfForm.bindFieldChangeEvent(rzrqfield, function(obj,id,value) {
    //带出年限
    takeoutFwnx();
});

/**
 * 根据最后工作日(开发用)带出最后工作日
 */
function takeoutzhgzr(){
    //alert("根据最后工作日(开发用)带出最后工作日");
    var zhgzrkfy = WfForm.getFieldValue(zhgzrkfyfield);
    jQuery.ajax({
        url: "/watsons/jsp/getLastWorkday.jsp?t=" + new Date().getTime(),
        data: {"ksrq": zhgzrkfy},
        async: false,
        success: function (result) {
            //alert("最后工作日：" + result);
            var zhgzr = result.trim();
            WfForm.changeFieldValue("" + ksrqfield, {
                value: "" + zhgzr,
                specialobj: [
                    {id: "" + zhgzr, name: "" + zhgzr}
                ]
            });

            if(WfForm.getFieldValue(yzhgzrqkfyfield)=="" || WfForm.getFieldValue(yzhgzrqkfyfield)==WfForm.getFieldValue(sqrqfield)){
                //alert("999");
                WfForm.changeFieldValue(""+yzhgzrqkfyfield, {
                    value: ""+zhgzr,
                    specialobj:[
                        {id:""+zhgzr,name:""+zhgzr}
                    ]
                });
            }
            /*
            //如果修改了最后工作日期->备注必填
            if(zhgzr!=WfForm.getFieldValue(yzhgzrqkfyfield)){
//alert("不是最后工作日");
                WfForm.changeFieldValue(sfwzhgzrfield, {value:"1"});
            }else{//可编辑
            //alert("最后工作日");
              WfForm.changeFieldValue(sfwzhgzrfield, {value:"0"});
            }
            */
            if(parseFloat(daysBetween(result.trim(), WfForm.getFieldValue(yzhgzrqkfyfield)))<0) WfForm.changeFieldValue(sfwzhgzrfield, {value:"1"});
            else WfForm.changeFieldValue(sfwzhgzrfield, {value:"0"});

            //算出社会保险结算知、公积金结算至
            //1.如果最后工作日期是本月15日及之前，则日期算上个月最后一天
            //2.如果最后工作日期是本月16日及之后，择日期算本月最后一天
            /*
            if(zhgzr!=""){
              var tempDate = "0";
              if(parseInt(zhgzr.substring(8,10))<=15){
                tempDate = new Date(zhgzr.substring(0,8)+"01");
              }else{
                if(parseInt(zhgzr.substring(5,7))==12)	tempDate=(parseInt(zhgzr.substring(0,4))+1)+"-"+"01"+"-"+"01";
                else if(parseInt(zhgzr.substring(5,7))<=8) tempDate = (zhgzr.substring(0,4))+"-"+"0"+(parseInt(zhgzr.substring(5,7))+1)+"-"+"01";
                else tempDate = (zhgzr.substring(0,4))+"-"+(parseInt(zhgzr.substring(5,7))+1)+"-"+"01";
                tempDate = new Date(tempDate);
              }
              tempDate.setDate(tempDate.getDate()-1);
              tempDate = getNowFormatDate(tempDate).substring(0,10);
              //alert("结算日:"+tempDate);
              WfForm.changeFieldValue("" + hr7field, {
                  value: "" + tempDate,
                  specialobj: [
                    {id: "" + tempDate, name: "" + tempDate}
                  ]
              });
              WfForm.changeFieldValue("" + hr8field, {
                  value: "" + tempDate,
                  specialobj: [
                    {id: "" + tempDate, name: "" + tempDate}
                  ]
              });
            }
            */
        }
    });
}

function takeoutFwnx(){
    //alert("入职日期："+WfForm.getFieldValue(rzrqfield) + "  最后工作日"+WfForm.getFieldValue(zhgzrkfyfield));
    jQuery.ajax({
        url: "/watsons/jsp/getXcyf.jsp?t=" + new Date().getTime(),
        data: {"ksrq": WfForm.getFieldValue(rzrqfield),"jsrq":WfForm.getFieldValue(zhgzrkfyfield)},
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

//最后工作日期变更时间
WfForm.bindFieldChangeEvent(ksrqfield, function(obj,id,value){

    if(WfForm.getFieldValue(sfymrdczhgzrfield)!="") {
        WfForm.changeFieldValue("" + zhgzrkfyfield, {
            value: "" + value,
            specialobj: [
                {id: "" + value, name: "" + value}
            ]
        });
    }else{
        WfForm.changeFieldValue(sfymrdczhgzrfield,{value:"1"});
    }

    takeoutFwnx();
});


function checkFormat(){

    //手机号码
    if(WfForm.getFieldValue(sjhmfield).length!=11){
        alert("请填写11位数字联系电话!")
        return false;
    }

    var myreg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    if(!myreg.test(WfForm.getFieldValue(yxfield))){
        alert("个人邮箱格式不正确！");
        return false;
    }
    return true;
}

jQuery(document).ready(function(){
    if(WfForm.getFieldValue(sfymrdczhgzrfield)==""){
        //带出最后工作日期
        takeoutKsrq();
        //带出试用结束日期
        takeoutJsrq();
    }

    WfForm.registerCheckEvent(WfForm.OPER_SAVE+","+WfForm.OPER_SUBMIT,function(callback){

        var isSubmit = true;
        if(!checkFormat())   isSubmit = false;

        if(isSubmit)    callback();
    });
});