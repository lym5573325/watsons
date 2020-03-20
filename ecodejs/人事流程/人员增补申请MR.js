var zblbfield = WfForm.convertFieldNameToId("typeofrequisition");//增补类别
var zprsfield = WfForm.convertFieldNameToId("zprs");//招聘人数
var thlxfield = WfForm.convertFieldNameToId("thlxreplacementstyle");//替换类型
var bmjzbfield_sqr = WfForm.convertFieldNameToId("thlxreplacementstyle");//部门及组别——申请人
var bmjzbfield_bthr = WfForm.convertFieldNameToId("thlxreplacementstyle");//部门及组别——被替换人
var bmjzbfield = WfForm.convertFieldNameToId("thlxreplacementstyle");//部门及组别


/**
 * 根据增补类别控制招聘人数
 * 1.如果选择"替换",招聘人数为1切只读
 */
function changeZprsByZblb(){
    var zblb = WfForm.getFieldValue(zblbfield);
    if(zblb == "0"){
        //招聘人数==1，且只读
        WfForm.changeFieldValue(zprsfield,{value:"1"});
        WfForm.changeFieldAttr(zprsfield,1);
    }else{
        //清空，必填
        WfForm.changeFieldValue(zprsfield,{value:""});
        WfForm.changeFieldAttr(zprsfield,3);
    }
}

/**
 * 根据增补类别控制部门及组别
 * 1.默认为申请人的部门及组别
 * 2.如果选择"替换",部门及组别为被替换人的部门及组别
 */
function changeBmjzbByZblb(){
    var zblb = WfForm.getFieldValue(zblbfield);
    var bmjzb = "";
    var bmjzbspan = "";
    if(zblb == "0"){
        bmjzb = WfForm.getFieldValue(bmjzbfield_bthr);
        bmjzbspan = WfForm.getBrowserShowName(bmjzbfield_bthr);
    }else{
        bmjzb = WfForm.getFieldValue(bmjzbfield_sqr);
        bmjzbspan = WfForm.getBrowserShowName(bmjzbfield_sqr);
    }
    WfForm.changeFieldValue("bmjzbfield", { value: bmjzb, specialobj:[ {id:bmjzb,name:bmjzbspan} ] });
}

jQuery(document).ready(function(){

    changeZprsByZblb();
    changeBmjzbByZblb();

    WfForm.bindFieldChangeEvent(zblbfield, function(obj,id,value){
        changeZprsByZblb();
        //清空替换类型
        WfForm.changeFieldValue(thlxfield,{value:""});
    });

    WfForm.bindFieldChangeEvent(zblbfield+","+bmjzbfield_bthr+","+bmjzbfield_sqr, function(obj,id,value){
        changeBmjzbByZblb();
    });

});
