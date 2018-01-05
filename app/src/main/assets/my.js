
var popupValues=[
   {
       //对应地震级别的listview 顺序号点击menus 里存放的是对应地震级别 案例名称的内容页（html页面）
       index:0,
       menus:['140121','140181']

   },
   {
    index:1,
    menus:['c','d']
   },
   {
    index:2,
    menus:['e','f']
   },
   {
    index:3,
    menus:['g','h']
   },
  
];
$(document).ready(
    
    function(){
        // var curId=  $.getUrlParam("id");
        var param=getUrlParam("id");
        //alert(param);
        if(param==null){
            //设个默认值要是不显示尴尬了
            param=0;
        }
        for(var i=0;i<popupValues.length;i++){
            var obj=popupValues[i];
            if(obj.index==param){
                var content=obj.menus;
                for(var j=0;j<content.length;j++){
                        //btngroupDiv
                    var btncontentstr='<button  onclick="showPage('+content[i]+')"   class="btn btn-default btn-info">'+content[j]+'</button>';
                    $(".btngroupDiv").append(btncontentstr);
                }
            }
           
        }
        //初始化按钮内容按照 地震级别的内容进行初始化
        hideFramepage();
    }
);

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
  }
  /*隐藏用于显示地震内容的framecontent*/
  function hideFramepage(){
    $('.row').show();
    $('.container').hide();
}
/*显示对应的地震内容 */
function showPage(titlename){
    var url='page/'+titlename+'.htm';
    $('#pageframe').attr('src',url);
    $('.row').hide();
    $('.container').show();

  
}
 