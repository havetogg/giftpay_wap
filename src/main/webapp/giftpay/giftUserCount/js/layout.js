(function (doc, win) {
  var docEl = doc.documentElement,
    resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
    recalc = function () {
      var clientWidth = docEl.clientWidth;
      if (!clientWidth) return;
      docEl.style.fontSize = 10 * (clientWidth / 750) + 'px';
    };
 
  if (!doc.addEventListener) return;
  win.addEventListener(resizeEvt, recalc, false);
  doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);
var TipShow = function (msg, duration) {
    var timeoutId = -1;
    var $backdropObj = $(".loading-backdrop");
    if (!$backdropObj[0]) {
        htmlStr = "<div class='loading-backdrop'>" +
            "<div class='loading-wrapper'>" +
            "<div class='loading-content'>" +
            msg +
            "</div></div></div>";
        $("body").append(htmlStr);
        $(".loading-backdrop").addClass('visible');
        if (typeof duration == "number" && duration > 0) {
            if (timeoutId > 0) {
                clearTimeout(timeoutId);
                delete timeoutId;
            }
            timeoutId = setTimeout(function () {
                TipHide()
            }, duration);
        }
    } else {
        $(".loading-content")[0].innerText = msg;
        $(".loading-backdrop").addClass('visible');
        if (typeof duration == "number" && duration > 0) {
            if (timeoutId > 0) {
                clearTimeout(timeoutId);
                delete timeoutId;
            }

            timeoutId = setTimeout(function () {
                TipHide()
            }, duration);
        }
    }
};
var TipHide = function () {
    $(".loading-backdrop").removeClass('visible');
};
function checkMobile(sMobile){
    if(!(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(sMobile))){
        return false;
    }
    else{
        return true;
    }
}
//异步菊花
function loading(state, callback) {
    if (state == "start") {
        $("body").prepend('<div id="loading"><div id="loading_bg"></div><div id="loading_div"><div id="loading_pic"></div></div></div>');
    } else if (state == "stop") {
        $("#loading").remove();
    }

}