var cw1field = WfForm.convertFieldNameToId("cw1");//财务借款(有无)
var cw2field = WfForm.convertFieldNameToId("cw2");//是否有其他尚未支付的报销费用(有无)

//提醒内容
var alertContent = "员工已完成还款/处理报销，请确认。";
/*
WfForm.bindFieldChangeEvent(cw1field, function(obj,id,value){
    if(value==0){
      alert(alertContent);
    }
});

WfForm.bindFieldChangeEvent(cw2field, function(obj,id,value){
    if(value==0){
      alert(alertContent);
    }
});
*/
WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
    if(WfForm.getFieldValue(cw1field)=="0" || WfForm.getFieldValue(cw2field)=="0"){
        alert(alertContent);
    }else{
        callback(); //继续提交需调用callback，不调用代表阻断
    }
});


