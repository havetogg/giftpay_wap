/**
 * Created by Administrator on 2017/3/27.
 */
/*! Che300 09-12-2016 6:11:09 */
function getParam(a){var b=$(document).getUrlParam(a);return null!=b&&b.length>0?b:""}function getActivityEvalResult(a,b,c,d,e,f,g,h,i){var j=$(".act-bg"),k=($(".act-tip"),$(".act-wrap")),l=$("#buyPrice"),m=$("#sellPrice"),n=$("#starWrap"),o="8658630237756";j.fadeIn(150),$(".tool-tip,.loading").show(),j.on("click",function(){$(this).hide(),k.hide()}),g<=2?n.html('<li class="star"></li><li class="star"></li><li class="star"></li><li class="star"></li><li class="star"></li>'):g>2&&g<5?n.html('<li class="star"></li><li class="star"></li><li class="star"></li><li class="star"></li><li class="no-star"></li>'):n.html('<li class="star"></li><li class="star"></li><li class="star"></li><li class="no-star"></li><li class="no-star"></li>'),$.ajax({url:apiUrl+"/app/EvalResult/getBaseEvalPrice",dataType:"jsonp",data:{prov:a,city:b,brand:c,series:d,model:e,regDate:f,mile:g,deviceId:o},type:"get",success:function(a){$(".tool-tip,.loading").hide();var b=a.success.evalResult;l.text(b.dealer_buy_price),m.text(b.dealer_price),k.show()},error:function(a,b){$(".tool-tip,.loading").hide(),console.log(b)}})}function getParam(a){var b=$(document).getUrlParam(a);return null!=b&&b.length>0?b:""}function getEvalResult(){var a=$("#userArea").attr("data-provid"),b=$("#userArea").attr("data-cityid"),c=$("#userArea").val(),d=$("#userCar").attr("data-brandid"),e=$("#userCar").attr("data-seriesid"),f=$("#userCar").attr("data-modelid"),g=$("#userCar").val(),h=$("#userDate").attr("data-year"),i=$("#userDate").attr("data-month"),j=h+"-"+i,k=$("#userMile").val(),l=$("#userCar").attr("data-min"),m=$("#userCar").attr("data-max");if(d<=0||e<=0||f<=0||"璇烽€夋嫨杞﹀瀷"==g)return void showPop("缂哄皯鏉′欢","鏈€夋嫨杞﹀瀷");if(a<=0||b<=0)return void showPop("缂哄皯鏉′欢","鏈€夋嫨鎵€鍦ㄥ湴鍖�");if(0==h||0==i||"璇烽€夋嫨棣栨涓婄墝鏃堕棿"==$("#userDate").val())return void showPop("缂哄皯鏉′欢","鏈€夋嫨杞﹁締棣栨涓婄墝鏃堕棿");if(!validTwoDecimalNumber(k))return void showPop("閿欒鏉′欢","璇疯緭鍏ュぇ浜�0涓斿皬浜�100鐨勯噷绋嬫暟锛堟渶澶氫繚鐣欎袱浣嶅皬鏁帮級");var n=siteUrl+"estimate/result/";n+=a+"/"+b+"/"+d+"/"+e+"/"+f+"/"+j+"/"+k,window.from&&(n+="?from="+window.from),parseInt(b)>0&&sessionStorage.setItem("sld",b),sessionStorage.setItem("last_eval",'{"prov":'+a+',"city":'+b+',"areaName":"'+c+'","brand":'+d+',"series":'+e+',"model":'+f+',"regYear":'+h+',"regMonth":'+i+',"minYear":'+l+',"maxYear":'+m+',"modelName":"'+g+'","mile":'+k+"}"),1==getParam("activity")?getActivityEvalResult(a,b,d,e,f,j,k):location.href=n}function getRegDateList(a,b){var c,d=$(".date-list"),e=$("#userDate"),f=$("#showDate"),g="",h=new Date;for(e.val("璇烽€夋嫨棣栨涓婄墝鏃堕棿").attr({"data-year":0,"data-month":0}),c=b;c>=a;c--)if(c==h.getFullYear()){for(var i="",j=1;j<=h.getMonth()+1;j++)i+='<li data-month="'+j+'">'+j+"鏈�</li>";g+='<dd data-year="'+c+'"><div>'+c+'骞�</div><ul class="month-list clearfix">'+i+"</ul></dd>"}else g+='<dd data-year="'+c+'"><div>'+c+'骞�</div><ul class="month-list clearfix"><li data-month="1">1鏈�</li><li data-month="2">2鏈�</li><li data-month="3">3鏈�</li><li data-month="4">4鏈�</li><li data-month="5">5鏈�</li><li data-month="6">6鏈�</li><li data-month="7">7鏈�</li><li data-month="8">8鏈�</li><li data-month="9">9鏈�</li><li data-month="10">10鏈�</li><li data-month="11">11鏈�</li><li data-month="12">12鏈�</li></ul></dd>';d.html(g),e.click(function(){f.show(),$(".mask-bg").show()});var k,l,m=$(".date-list>dd"),n=$(".month-list>li"),o="";m.click(function(){k=$(this).attr("data-year"),$(this).hasClass("current")?$(this).removeClass("current"):$(this).addClass("current").siblings().removeClass("current"),o=$(this).children("div").text()}),n.click(function(){l=$(this).attr("data-month"),e.attr({"data-month":l,"data-year":k}),o+=" "+$(this).text(),e.val(o),closeAll()})}function getModelList(a,b,c){function d(a,b){var c="";if(a.length>0)for(var d=99999,f=0;f<a.length;f++)a[f].model_year!=d&&(d=a[f].model_year,c+="<dt>"+d+"娆�</dt>"),c+='<dd class="clearfix" data-min="'+a[f].min_reg_year+'" data-max="'+a[f].max_reg_year+'" data-modelid="'+a[f].model_id+'"><span>'+a[f].model_name+"</span><i>"+Number(a[f].model_price).toFixed(2)+"涓�</i></dd>";else c+="<dd>鏆傛棤杞﹀瀷</dd>";b.html(c),$(".tool-tip,.loading").hide(),e()}function e(){$(".model-list>dd").click(function(){
    var car=$('#userCar').val();
    var city=$('#c').val();
    var date=$('#date').val();
    var miles=$('#miles').val();
    if(!car){
        TipShow('请选择车型',1000);
        $('.start_val').show();
        $('.end_val').hide();
    }
    else if(!date){
        TipShow('请选择首次上牌时间',1000);
        $('.start_val').show();
        $('.end_val').hide();
    }
    else if(!miles){
        TipShow('请输入行驶里程',1000);
        $('.start_val').show();
        $('.end_val').hide();
    }
    else{
        $('.start_val').hide();
        $('.end_val').show();
    }
    var b=$(this).attr("data-modelid"),d=$("#userCar"),e=$(this).attr("data-min"),f=$(this).attr("data-max");d.attr({"data-brandid":c,"data-seriesid":a,"data-modelid":b,"data-min":e,"data-max":f}),d.val($(this).children("span").text().trim()),getRegDateList(e,f),$("#showModel,.mask-bg").hide()})}"Microsoft Internet Explorer"!=navigator.appName||"8."!=navigator.appVersion.match(/8./i)&&"9."!=navigator.appVersion.match(/9./i)?$.getJSON(metaDomain+"/meta/model/model_series"+a+".json"+assetVersion,function(a){d(a,b)}):$.ajax({type:"get",contentType:"application/json; charset=utf-8",url:baseUrl+"/service/QueryService.php",data:{classID:a,oper:"getCarModelList"},success:function(a){var c=jQuery.parseJSON(a);d(c,b)}})}function getSeriesList(a,b){function c(a,b){var c="";if(a&&a.length>0)for(var e=0;e<a.length;e++)c+='<dd data-seriesid="'+a[e].series_id+'">'+a[e].series_name+"</dd>";else c+='<dd data-seriesid="">鏆傛棤杞︾郴</dd>';b.html(c),$(".tool-tip,.loading").hide(),d()}function d(){$(".series-list>dd").click(function(){var b=$(this).attr("data-seriesid");$("#userCar");$(".tool-tip,.loading").show(),getModelList(b,$(".model-list"),a),$("#showSeries").hide(),$("#showModel").show(),$(document).scrollTop(0)})}"Microsoft Internet Explorer"!=navigator.appName||"8."!=navigator.appVersion.match(/8./i)&&"9."!=navigator.appVersion.match(/9./i)?$.getJSON(metaDomain+"/meta/series/series_brand"+a+".json",function(a){c(a,b)}):$.ajax({type:"get",contentType:"application/json; charset=utf-8",url:baseUrl+"/service/QueryService.php",data:{carBrand:a,oper:"getCarSeriesList"},success:function(a){var d=jQuery.parseJSON(a);c(d,b)}})}function scrollToInitial(a,b){a.click(function(){var a=$(this).attr("data-id"),c=$(this).parent().siblings("dl").find("dt[data-initial="+a+"]"),d=c.offset().top-45;b.text(a).fadeIn(100),setTimeout(function(){b.fadeOut(100)},200),$(document).scrollTop(d)})}function getCityList(a,b,c,d){var e=$("#userArea");0==c.length&&$.getJSON(metaUrl+"c2c/city_prov"+a+".json"+assetVersion,function(a){var b,c="";for(b=0;b<a.length;b++)c+='<li data-cityid="'+a[b].key+'">'+a[b].value+"</li>";d.html(c)}),$(document).on("click",".city-list>li",function(){e.attr({"data-cityid":$(this).attr("data-cityid"),"data-provid":a}),b!=$(this).text()?e.val(b+" "+$(this).text()):e.val($(this).text()),closeAll()})}function closeAll(){$(".panel-initial").hide(),$(".mask-bg").hide()}function validTwoDecimalNumber(a){var b=/^([0-9][0-9]*)+(.[0-9]{1,2})?$/,c=new RegExp(b);return!!(c.test(a)&&a<100&&a>0)&&c.test(a)}function showPop(a,b){var c=$(".tool-tip"),d=$(".poping");c.fadeIn(150),$("#pop-title").html(a),$("#pop-info").html(b),d.show()}$(function(){FastClick.attach(document.body);var a=$("head>title");"che300_pro"==getParam("from")&&a.text("缃戝敭杞︽簮"),$("a.switch_city").click(function(a){var b=$(location).attr("href");$.cookie("lastURL",b,{path:"/"})}),$(".ch-close").click(function(a){$.cookie("downApp","false",{path:"/"}),$(".ch-download").hide()}),$(".ch-link").click(function(a){$.cookie("downApp","false",{path:"/"})});var b=$(".tool-tip"),c=$(".poping");$("#pop-btn").click(function(){b.fadeOut(150),c.fadeOut(150)}),$("#goto").click(function(){$(document).scrollTop()>0&&$("body,html").scrollTop(0)}),function(){var a=$("#close-panel"),b=$(".panel-area");a.click(function(){b.hide()})}();var d=$("#back-top");$(document).scroll(function(){var a=$(document).scrollTop();a>400?d.fadeIn(150):d.fadeOut(150)}),d.click(function(){$(document).scrollTop(0)})}),function(){"use strict";function a(b,d){function e(a,b){return function(){return a.apply(b,arguments)}}var f;if(d=d||{},this.trackingClick=!1,this.trackingClickStart=0,this.targetElement=null,this.touchStartX=0,this.touchStartY=0,this.lastTouchIdentifier=0,this.touchBoundary=d.touchBoundary||10,this.layer=b,this.tapDelay=d.tapDelay||200,this.tapTimeout=d.tapTimeout||700,!a.notNeeded(b)){for(var g=["onMouse","onClick","onTouchStart","onTouchMove","onTouchEnd","onTouchCancel"],h=this,i=0,j=g.length;i<j;i++)h[g[i]]=e(h[g[i]],h);c&&(b.addEventListener("mouseover",this.onMouse,!0),b.addEventListener("mousedown",this.onMouse,!0),b.addEventListener("mouseup",this.onMouse,!0)),b.addEventListener("click",this.onClick,!0),b.addEventListener("touchstart",this.onTouchStart,!1),b.addEventListener("touchmove",this.onTouchMove,!1),b.addEventListener("touchend",this.onTouchEnd,!1),b.addEventListener("touchcancel",this.onTouchCancel,!1),Event.prototype.stopImmediatePropagation||(b.removeEventListener=function(a,c,d){var e=Node.prototype.removeEventListener;"click"===a?e.call(b,a,c.hijacked||c,d):e.call(b,a,c,d)},b.addEventListener=function(a,c,d){var e=Node.prototype.addEventListener;"click"===a?e.call(b,a,c.hijacked||(c.hijacked=function(a){a.propagationStopped||c(a)}),d):e.call(b,a,c,d)}),"function"==typeof b.onclick&&(f=b.onclick,b.addEventListener("click",function(a){f(a)},!1),b.onclick=null)}}var b=navigator.userAgent.indexOf("Windows Phone")>=0,c=navigator.userAgent.indexOf("Android")>0&&!b,d=/iP(ad|hone|od)/.test(navigator.userAgent)&&!b,e=d&&/OS 4_\d(_\d)?/.test(navigator.userAgent),f=d&&/OS [6-7]_\d/.test(navigator.userAgent),g=navigator.userAgent.indexOf("BB10")>0;a.prototype.needsClick=function(a){switch(a.nodeName.toLowerCase()){case"button":case"select":case"textarea":if(a.disabled)return!0;break;case"input":if(d&&"file"===a.type||a.disabled)return!0;break;case"label":case"iframe":case"video":return!0}return/\bneedsclick\b/.test(a.className)},a.prototype.needsFocus=function(a){switch(a.nodeName.toLowerCase()){case"textarea":return!0;case"select":return!c;case"input":switch(a.type){case"button":case"checkbox":case"file":case"image":case"radio":case"submit":return!1}return!a.disabled&&!a.readOnly;default:return/\bneedsfocus\b/.test(a.className)}},a.prototype.sendClick=function(a,b){var c,d;document.activeElement&&document.activeElement!==a&&document.activeElement.blur(),d=b.changedTouches[0],c=document.createEvent("MouseEvents"),c.initMouseEvent(this.determineEventType(a),!0,!0,window,1,d.screenX,d.screenY,d.clientX,d.clientY,!1,!1,!1,!1,0,null),c.forwardedTouchEvent=!0,a.dispatchEvent(c)},a.prototype.determineEventType=function(a){return c&&"select"===a.tagName.toLowerCase()?"mousedown":"click"},a.prototype.focus=function(a){var b;d&&a.setSelectionRange&&0!==a.type.indexOf("date")&&"time"!==a.type&&"month"!==a.type?(b=a.value.length,a.setSelectionRange(b,b)):a.focus()},a.prototype.updateScrollParent=function(a){var b,c;if(b=a.fastClickScrollParent,!b||!b.contains(a)){c=a;do{if(c.scrollHeight>c.offsetHeight){b=c,a.fastClickScrollParent=c;break}c=c.parentElement}while(c)}b&&(b.fastClickLastScrollTop=b.scrollTop)},a.prototype.getTargetElementFromEventTarget=function(a){return a.nodeType===Node.TEXT_NODE?a.parentNode:a},a.prototype.onTouchStart=function(a){var b,c,f;if(a.targetTouches.length>1)return!0;if(b=this.getTargetElementFromEventTarget(a.target),c=a.targetTouches[0],d){if(f=window.getSelection(),f.rangeCount&&!f.isCollapsed)return!0;if(!e){if(c.identifier&&c.identifier===this.lastTouchIdentifier)return a.preventDefault(),!1;this.lastTouchIdentifier=c.identifier,this.updateScrollParent(b)}}return this.trackingClick=!0,this.trackingClickStart=a.timeStamp,this.targetElement=b,this.touchStartX=c.pageX,this.touchStartY=c.pageY,a.timeStamp-this.lastClickTime<this.tapDelay&&a.preventDefault(),!0},a.prototype.touchHasMoved=function(a){var b=a.changedTouches[0],c=this.touchBoundary;return Math.abs(b.pageX-this.touchStartX)>c||Math.abs(b.pageY-this.touchStartY)>c},a.prototype.onTouchMove=function(a){return!this.trackingClick||((this.targetElement!==this.getTargetElementFromEventTarget(a.target)||this.touchHasMoved(a))&&(this.trackingClick=!1,this.targetElement=null),!0)},a.prototype.findControl=function(a){return void 0!==a.control?a.control:a.htmlFor?document.getElementById(a.htmlFor):a.querySelector("button, input:not([type=hidden]), keygen, meter, output, progress, select, textarea")},a.prototype.onTouchEnd=function(a){var b,g,h,i,j,k=this.targetElement;if(!this.trackingClick)return!0;if(a.timeStamp-this.lastClickTime<this.tapDelay)return this.cancelNextClick=!0,!0;if(a.timeStamp-this.trackingClickStart>this.tapTimeout)return!0;if(this.cancelNextClick=!1,this.lastClickTime=a.timeStamp,g=this.trackingClickStart,this.trackingClick=!1,this.trackingClickStart=0,f&&(j=a.changedTouches[0],k=document.elementFromPoint(j.pageX-window.pageXOffset,j.pageY-window.pageYOffset)||k,k.fastClickScrollParent=this.targetElement.fastClickScrollParent),h=k.tagName.toLowerCase(),"label"===h){if(b=this.findControl(k)){if(this.focus(k),c)return!1;k=b}}else if(this.needsFocus(k))return a.timeStamp-g>100||d&&window.top!==window&&"input"===h?(this.targetElement=null,!1):(this.focus(k),this.sendClick(k,a),d&&"select"===h||(this.targetElement=null,a.preventDefault()),!1);return!(!d||e||(i=k.fastClickScrollParent,!i||i.fastClickLastScrollTop===i.scrollTop))||(this.needsClick(k)||(a.preventDefault(),this.sendClick(k,a)),!1)},a.prototype.onTouchCancel=function(){this.trackingClick=!1,this.targetElement=null},a.prototype.onMouse=function(a){return!this.targetElement||(!!a.forwardedTouchEvent||(!a.cancelable||(!(!this.needsClick(this.targetElement)||this.cancelNextClick)||(a.stopImmediatePropagation?a.stopImmediatePropagation():a.propagationStopped=!0,a.stopPropagation(),a.preventDefault(),!1))))},a.prototype.onClick=function(a){var b;return this.trackingClick?(this.targetElement=null,this.trackingClick=!1,!0):"submit"===a.target.type&&0===a.detail||(b=this.onMouse(a),b||(this.targetElement=null),b)},a.prototype.destroy=function(){var a=this.layer;c&&(a.removeEventListener("mouseover",this.onMouse,!0),a.removeEventListener("mousedown",this.onMouse,!0),a.removeEventListener("mouseup",this.onMouse,!0)),a.removeEventListener("click",this.onClick,!0),a.removeEventListener("touchstart",this.onTouchStart,!1),a.removeEventListener("touchmove",this.onTouchMove,!1),a.removeEventListener("touchend",this.onTouchEnd,!1),a.removeEventListener("touchcancel",this.onTouchCancel,!1)},a.notNeeded=function(a){var b,d,e,f;if("undefined"==typeof window.ontouchstart)return!0;if(d=+(/Chrome\/([0-9]+)/.exec(navigator.userAgent)||[,0])[1]){if(!c)return!0;if(b=document.querySelector("meta[name=viewport]")){if(b.content.indexOf("user-scalable=no")!==-1)return!0;if(d>31&&document.documentElement.scrollWidth<=window.outerWidth)return!0}}if(g&&(e=navigator.userAgent.match(/Version\/([0-9]*)\.([0-9]*)/),e[1]>=10&&e[2]>=3&&(b=document.querySelector("meta[name=viewport]")))){if(b.content.indexOf("user-scalable=no")!==-1)return!0;if(document.documentElement.scrollWidth<=window.outerWidth)return!0}return"none"===a.style.msTouchAction||"manipulation"===a.style.touchAction||(f=+(/Firefox\/([0-9]+)/.exec(navigator.userAgent)||[,0])[1],!!(f>=27&&(b=document.querySelector("meta[name=viewport]"),b&&(b.content.indexOf("user-scalable=no")!==-1||document.documentElement.scrollWidth<=window.outerWidth)))||("none"===a.style.touchAction||"manipulation"===a.style.touchAction))},a.attach=function(b,c){return new a(b,c)},"function"==typeof define&&"object"==typeof define.amd&&define.amd?define(function(){return a}):"undefined"!=typeof module&&module.exports?(module.exports=a.attach,module.exports.FastClick=a):window.FastClick=a}(),!function(a){"function"==typeof define&&define.amd?define(["jquery"],a):a(jQuery)}(function(a){function b(a){return h.raw?a:encodeURIComponent(a)}function c(a){return h.raw?a:decodeURIComponent(a)}function d(a){return b(h.json?JSON.stringify(a):String(a))}function e(a){0===a.indexOf('"')&&(a=a.slice(1,-1).replace(/\\"/g,'"').replace(/\\\\/g,"\\"));try{return a=decodeURIComponent(a.replace(g," ")),h.json?JSON.parse(a):a}catch(b){}}function f(b,c){var d=h.raw?b:e(b);return a.isFunction(c)?c(d):d}var g=/\+/g,h=a.cookie=function(e,g,i){if(void 0!==g&&!a.isFunction(g)){if(i=a.extend({},h.defaults,i),"number"==typeof i.expires){var j=i.expires,k=i.expires=new Date;k.setTime(+k+864e5*j)}return document.cookie=[b(e),"=",d(g),i.expires?"; expires="+i.expires.toUTCString():"",i.path?"; path="+i.path:"",i.domain?"; domain="+i.domain:"",i.secure?"; secure":""].join("")}for(var l=e?void 0:{},m=document.cookie?document.cookie.split("; "):[],n=0,o=m.length;o>n;n++){var p=m[n].split("="),q=c(p.shift()),r=p.join("=");if(e&&e===q){l=f(r,g);break}e||void 0===(r=f(r))||(l[q]=r)}return l};h.defaults={},a.removeCookie=function(b,c){return void 0!==a.cookie(b)&&(a.cookie(b,"",a.extend({},c,{expires:-1})),!a.cookie(b))}}),jQuery.fn.extend({getUrlParam:function(a){a=escape(unescape(a));var b=new Array,c=null;if("#document"==$(this).attr("nodeName"))window.location.search.search(a)>-1&&(c=window.location.search.substr(1,window.location.search.length).split("&"));else if("undefined"!=$(this).attr("src")){var d=$(this).attr("src");if(d.indexOf("?")>-1){var e=d.substr(d.indexOf("?")+1);c=e.split("&")}}else{if("undefined"==$(this).attr("href"))return null;var d=$(this).attr("href");if(d.indexOf("?")>-1){var e=d.substr(d.indexOf("?")+1);c=e.split("&")}}if(null==c)return null;for(var f=0;f<c.length;f++)escape(unescape(c[f].split("=")[0]))==a&&b.push(c[f].split("=")[1]);return 0==b.length?null:1==b.length?b[0]:b}}),$(function(){var a=$("#userArea"),b=$("#userCar"),c=$("#userDate"),d=$("#userMile"),e=$("#userSubmit"),f=$("#showCity"),g=$("#showBrand"),h=$("#showSeries"),i=$("#showModel");$("#showDate");if("object"==typeof sessionStorage)try{sessionStorage.setItem("sessionStorage",1),sessionStorage.removeItem("sessionStorage")}catch(j){Storage.prototype._setItem=Storage.prototype.setItem,Storage.prototype.setItem=function(){}}a.click(function(){f.show(),$(".mask-bg").show()});var k=$(".prov-list>dd");if(k.click(function(){var a=$(this).children("ul").find("li"),b=$(this).children("ul"),c=$(this).attr("data-provid"),d=$(this).children("div").text();$(this).hasClass("current")?$(this).removeClass("current"):$(this).addClass("current").siblings().removeClass("current"),getCityList(c,d,a,b)}),sessionStorage.getItem("last_eval")){var l=$.parseJSON(sessionStorage.getItem("last_eval"));a.attr({"data-provid":l.prov,"data-cityid":l.city}).val(l.areaName),b.attr({"data-brandid":l.brand,"data-seriesid":l.series,"data-modelid":l.model,"data-min":l.minYear,"data-max":l.maxYear}).val(l.modelName),getRegDateList(l.minYear,l.maxYear),c.attr({"data-year":l.regYear,"data-month":l.regMonth}).val(l.regYear+"骞� "+l.regMonth+"鏈�"),d.val(l.mile)}else{var m=$("#locProvId").val(),n=$("#locCityId").val(),o=$("#locProvName").val(),p=$("#locCityName").val();""!=m&&""!=n&&""!=o&&""!=p&&a.attr({"data-provid":m,"data-cityid":n}).val(o+" "+p)}var q=$(".hot-city>dd");q.click(function(){a.attr({"data-provid":$(this).attr("data-provid"),"data-cityid":$(this).attr("data-cityid")}).val($(this).attr("data-prov")+" "+$(this).text()),closeAll()});var r=$("#close-city");r.click(function(){closeAll()});var s;b.click(function(){g.show(),$(".mask-bg").show()}),$(".brand-list>dd").click(function(){s=$(this).attr("data-brandid"),$(".tool-tip,.loading").show(),getSeriesList(s,$(".series-list")),g.hide(),h.show(),$(document).scrollTop(0)});var t=$("#close-brand");t.click(function(){closeAll()});var u=$("#close-series");u.click(function(){h.hide(),g.show()});var v=$("#close-model");v.click(function(){i.hide(),h.show()});var w=$("#close-date");w.click(function(){closeAll()}),scrollToInitial($("#brandInitList>li"),$("#brandInitPop")),c.click(function(){"璇烽€夋嫨杞﹀瀷"!=b.val()&&0!=b.attr("data-modelid")||showPop("鏃犳硶閫夋嫨","璇峰厛閫夋嫨杞﹀瀷")}),e.click(function(a){a.preventDefault(),getEvalResult()})});