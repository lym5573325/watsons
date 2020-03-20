var cw6field = WfForm.convertFieldNameToId("cw6");//Any additional payment claims 是否有其他尚未支付的报销费用（员工）


//提醒内容
var alertContent = "为了不影响您的正常离职，请到财务部处理报销，如有问题请联系财务相关同事。";

WfForm.bindFieldChangeEvent(cw6field, function(obj,id,value){
    if(value==0){
        alert(alertContent);
    }
});

WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
    if(WfForm.getFieldValue(cw6field)=="0"){
        alert(alertContent);
    }else{
        callback(); //继续提交需调用callback，不调用代表阻断
    }
});


