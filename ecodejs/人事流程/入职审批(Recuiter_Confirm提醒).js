var zlyz1field = WfForm.convertFieldNameToId("zlyz1");
var zlyz2field = WfForm.convertFieldNameToId("zlyz2");
var zlyz3field = WfForm.convertFieldNameToId("zlyz3");
var zlyz4field = WfForm.convertFieldNameToId("zlyz4");
var zlyz5field = WfForm.convertFieldNameToId("zlyz5");
var zlyz6field = WfForm.convertFieldNameToId("zlyz6");
var zlyz7field = WfForm.convertFieldNameToId("zlyz7");
var zlyz8field = WfForm.convertFieldNameToId("zlyz8");
var zlyz9field = WfForm.convertFieldNameToId("zlyz9");
var zlyz10field = WfForm.convertFieldNameToId("zlyz10");

jQuery(document).ready(function(){

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
        if(WfForm.getFieldValue(zlyz1field)!=""
            ||WfForm.getFieldValue(zlyz2field)!=""
            ||WfForm.getFieldValue(zlyz3field)!=""
            ||WfForm.getFieldValue(zlyz4field)!=""
            ||WfForm.getFieldValue(zlyz5field)!=""
            ||WfForm.getFieldValue(zlyz6field)!=""
            ||WfForm.getFieldValue(zlyz7field)!=""
            ||WfForm.getFieldValue(zlyz8field)!=""
            ||WfForm.getFieldValue(zlyz9field)!=""
            ||WfForm.getFieldValue(zlyz10field)!=""){
            WfForm.showConfirm("该员工资料尚不齐全，请确认是否批准通过；如不批准可选择“退回”，请员工进行资料补充。（是否继续提交批准）",function(){
                callback();
            },function(){
                return;
            },{
                title:"是否继续提交", //弹确认框的title，仅PC端有效
                okText:"是", //自定义确认按钮名称
                cancelText:"否" //自定义取消按钮名
            });
        }else{
            callback();
        }
    });
})