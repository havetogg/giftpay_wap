/**
 * Created by Administrator on 2017/6/6.
 */
$(function () {
    $(".flexslider").flexslider({
        animation: "slide", //String: Select your animation type, "fade" or "slide"图片变换方式：淡入淡出或者滑动
        slideshowSpeed: 4000, //展示时间间隔ms
        animationSpeed: 1100, //滚动时间ms
        touch: true //是否支持触屏滑动
    });
})