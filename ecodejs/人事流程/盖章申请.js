
var gzlxxlfield = WfForm.convertFieldNameToId("gzlxxl");//盖章类型小类


/**
 * 根据class属性显示、隐藏数据
 * type:  1==>显示  其他==>隐藏
 * classpro:class属性
 */
function showOrHidden(type,classpro){
    //alert("showOrHiddenDt3:"+type);
    if(type=="1"){//显示
        jQuery(classpro).each(function(){
            jQuery(this).show();
        })
    }else{
        //隐藏
        jQuery(classpro).each(function(){
            jQuery(this).hide();
        })
    }
}

function showOrHiddenByGzlxxl(){
    if(WfForm.getFieldValue(gzlxxlfield)=="0"){
        showOrHidden("1",".gzlxxlDiv");
    }else{
        showOrHidden("0",".gzlxxlDiv");
    }

}

jQuery(document).ready(function(){

    showOrHiddenByGzlxxl();
    WfForm.bindFieldChangeEvent(gzlxxlfield,function(obj,id,value){
        showOrHiddenByGzlxxl();
    });
});