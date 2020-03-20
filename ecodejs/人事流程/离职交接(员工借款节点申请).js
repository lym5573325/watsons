var cw5field = WfForm.convertFieldNameToId("cw5");//Cash Advance 财务借款


//提醒内容
var alertContent = "为了不影响您的正常离职，请到财务部归还借款，如有问题请联系财务相关同事。";

WfForm.bindFieldChangeEvent(cw5field, function(obj,id,value){
    if(value==0){
        alert(alertContent);
    }
});

WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
    if(WfForm.getFieldValue(cw5field)=="0"){
        alert(alertContent);
    }else{
        callback(); //继续提交需调用callback，不调用代表阻断
    }
});


