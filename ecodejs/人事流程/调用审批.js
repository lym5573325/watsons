var zjfield = WfForm.convertFieldNameToId("zj");//职级
var xzjfield = WfForm.convertFieldNameToId("xzj");//新职级
var bdqbmsysjbmfield = WfForm.convertFieldNameToId("bdqbmsysjbm");//变动前部门所有上级部门
var ygssfield = WfForm.convertFieldNameToId("ygss");//员工所属
var bdsxrqfield = WfForm.convertFieldNameToId("bdsxrq");//变动生效日期


function checkZj(){
    var xzj = WfForm.getFieldValue(xzjfield);
    var zj = WfForm.getFieldValue(zjfield);
    if(xzj!="" && zj!=""){
        if(parseInt(xzj)-parseInt(zj)>1){
            alert("公司规定调动只能升职一级，请确认您填写的级别正确！");
        }
    }
    return true;
}

/**
 * 部门属于HO-->员工所属=总部
 * 不么内部属于HO->员工所属=市场
 */
function geYgss(){
    var sysjbm = WfForm.getFieldValue(bdqbmsysjbmfield);
    if(sysjbm.indexOf(",27,")>=0){
        WfForm.changeFieldValue(ygssfield,{value:"0"});
    }else{
        WfForm.changeFieldValue(ygssfield,{value:"1"});
    }
}


WfForm.bindFieldChangeEvent(bdqbmsysjbmfield, function(obj,id,value){
    geYgss();
})


WfForm.bindFieldChangeEvent(zjfield+","+xzjfield, function(obj,id,value){
    checkZj();
})

/**
 * 变动生效日期不能大于必须在一个月以内
 * (Dept Approver_Out)节点
 */
WfForm.bindFieldChangeEvent(bdsxrqfield, function(obj,id,value){
    var bdxsrq = new Date(value.replace(/-/g,"/"));
    //一个月后
    var oneMonthAfter = new Date();
    oneMonthAfter.setMonth(oneMonthAfter.getMonth()+1);
    if(bdxsrq.getTime()>oneMonthAfter.getTime()){
        alert("变动生效日期不能超过一个月，请重新选择!");
        WfForm.changeFieldValue("" + bdsxrqfield, {
            value: "",
            specialobj: [
                {id: "", name: ""}
            ]
        });
    }
})


