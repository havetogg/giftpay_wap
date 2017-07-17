<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>有礼付</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/app.css" rel="stylesheet">
    <link type="text/css" href="css/car300.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/eval.min.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript" src="js/car_share.js"></script>
    <script type="text/javascript" src="https://ssl-assets.che300.com/m/wap2.0/js/static/eval.min.js?v=369"></script>
    <style>
        .miles input::-webkit-input-placeholder {
            color: red !important;
            margin-top: 5px;
        }
    </style>
    <script>
        var siteUrl = "https://m.che300.com/";
        var baseUrl = "https://m.che300.com/";
        var assetVersion = "?v=1490093751";
        var assetUrl = "https://ssl-assets.che300.com/";
        var metaUrl = "https://ssl-meta.che300.com/meta/";
        var metaDomain = "https://ssl-meta.che300.com";
        var apiUrl = "https://dingjia.che300.com/";
        function sellCar() {
            window.location.href = './sellCarApply.jsp';
        }
     function toCarValue() {
        window.location.href='./sellCarApply.jsp';
        }
    </script>
</head>
<body style="background-color: #f0f0f4;position:fixed">
<div style="background-color: #fff;overflow-x:hidden;" class="zoomer">
    <div class="htmlTop">
        <%--<img src="img/left_arrow.png" alt="" onclick="goBack()">--%>
        快速估值
    </div>
    <div>
        <div class="carValuation_banner">
            <img src="img/banner_bg.png" alt="" width="100%">
            <div class="carValuation_banner_div1">
                <div class="carValuation_banner_div1">
                    <img src="img/ic1.png" alt="" width="81px" height="37px">
                    <label class="tittle">
                        车辆估值(万元)
                    </label>
                <label class="goSellCarBtn" onclick="sellCar()">
                    <img src="img/message_con.png" alt="">
                </label>
                </div>
            </div>
            <div class="carValuation_banner_div2" id="price">0.00</div>
        </div>
        <div class="carValuation_content">
            <ul>
                <li>
                    <label class="icons">
                        <img src="img/ic2.png" alt="" class="icon">
                    </label>
                    <label class="menuName">车辆型号</label>
                    <label class="subTittle">
                        <input class="input3" type="text" data-brandid="0" data-seriesid="0" data-modelid="0"
                               data-min="0" data-max="0" placeholder="请选择车型" name="userCar" id="userCar"
                               oninput="OnInput (event)" onpropertychange="OnPropChanged(event)">
                    </label>
                    <img src="img/rightArrow.png" alt="" class="arrow">
                </li>
                <li>
                    <label class="icons">
                        <img src="img/ic3.png" alt="" class="icon">
                    </label>
                    <label class="menuName">所在城市</label>
                    <label class="subTittle" id="index_location">
                        <input id="c" data-city="" type="text" placeholder="请选择所在城市" class="input3" readonly>
                    </label>
                    <img src="img/rightArrow.png" alt="" class="arrow">
                </li>
                <li>
                    <label class="icons">
                        <img src="img/ic4.png" alt="" class="icon">
                    </label>
                    <label class="menuName">首次上牌</label>
                    <label class="subTittle">
                        <input id="date" type="text" data-options='{"type":"month","beginYear":2000,"endYear":2017}'
                               placeholder="请选择上牌日期" class="input3" readonly oninput="OnInput (event)"
                               onpropertychange="OnPropChanged(event)">
                    </label>

                    <img src="img/rightArrow.png" alt="" class="arrow">
                </li>

                <li>
                    <label class="icons">
                        <img src="img/ic5.png" alt="" class="icon" style="top: 10px;">
                    </label>
                    <label class="menuName">行驶里程</label>
                    <label class="miles"><input id="miles" type="tel" placeholder="请输入公里数 " class="input2"
                                                oninput="OnInput (event)"
                                                onpropertychange="OnPropChanged(event)"></label>
                    <label class="arrow">万公里</label>
                </li>
            </ul>
            <div class="start_val">
                开始估值
            </div>
            <div class="end_val" onclick="startCul()">
                开始估值
            </div>
            <!--<div class="end_val">-->
            <!--重新估值-->
            <!--</div>-->
            <div class="jmt_center">
                <span class="sellCar_bottom_text">通过有礼付一键卖车渠道成功卖车，即奖<a>100元</a>无门槛加油红包。</span>
            </div>
        </div>

    </div>
    <!--品牌-->
    <section class="panel-initial" id="showBrand" style="display: none;overflow: scroll">
        <header class="panel-header">
            <a id="close-brand">关闭</a>
            <strong>选择车辆品牌</strong>
        </header>
        <section class="panel-main">
            <div class="initial-pop" id="brandInitPop"></div>
            <ul class="initial-list" id="brandInitList">
                <li data-id="A">A</li>
                <li data-id="B">B</li>
                <li data-id="C">C</li>
                <li data-id="D">D</li>
                <li data-id="F">F</li>
                <li data-id="G">G</li>
                <li data-id="H">H</li>
                <li data-id="J">J</li>
                <li data-id="K">K</li>
                <li data-id="L">L</li>
                <li data-id="M">M</li>
                <li data-id="N">N</li>
                <li data-id="O">O</li>
                <li data-id="Q">Q</li>
                <li data-id="R">R</li>
                <li data-id="S">S</li>
                <li data-id="T">T</li>
                <li data-id="W">W</li>
                <li data-id="X">X</li>
                <li data-id="Y">Y</li>
                <li data-id="Z">Z</li>
            </ul>
            <dl class="list brand-list">
                <dt data-initial="A">A</dt>
                <dd data-brandid="1">奥迪</dd>
                <dd data-brandid="3">阿尔法·罗密欧</dd>
                <dd data-brandid="2">阿斯顿·马丁</dd>
                <dt data-initial="B">B</dt>
                <dd data-brandid="20">保斐利</dd>
                <dd data-brandid="11">保时捷</dd>
                <dd data-brandid="6">别克</dd>
                <dd data-brandid="13">北京汽车</dd>
                <dd data-brandid="14">北汽制造</dd>
                <dd data-brandid="17">北汽威旺</dd>
                <dd data-brandid="156">北汽幻速</dd>
                <dd data-brandid="115">北汽绅宝</dd>
                <dd data-brandid="12">奔腾</dd>
                <dd data-brandid="9">奔驰</dd>
                <dd data-brandid="7">宝马</dd>
                <dd data-brandid="15">宝骏</dd>
                <dd data-brandid="144">宝龙</dd>
                <dd data-brandid="16">宾利</dd>
                <dd data-brandid="18">布加迪</dd>
                <dd data-brandid="5">本田</dd>
                <dd data-brandid="10">标致</dd>
                <dd data-brandid="8">比亚迪</dd>
                <dd data-brandid="167">北汽新能源</dd>
                <dd data-brandid="172">宝沃</dd>
                <dd data-brandid="499">比速</dd>
                <dt data-initial="C">C</dt>
                <dd data-brandid="24">昌河</dd>
                <dd data-brandid="22">长城</dd>
                <dd data-brandid="23">长安商用</dd>
                <dd data-brandid="21">长安轿车</dd>
                <dd data-brandid="497">成功</dd>
                <dt data-initial="D">D</dt>
                <dd data-brandid="31">DS</dd>
                <dd data-brandid="27">东南</dd>
                <dd data-brandid="33">东风</dd>
                <dd data-brandid="28">东风小康</dd>
                <dd data-brandid="32">东风风度</dd>
                <dd data-brandid="30">东风风神</dd>
                <dd data-brandid="26">东风风行</dd>
                <dd data-brandid="170">东风风光</dd>
                <dd data-brandid="25">大众</dd>
                <dd data-brandid="142">大迪</dd>
                <dd data-brandid="34">大通</dd>
                <dd data-brandid="29">道奇</dd>
                <dt data-initial="F">F</dt>
                <dd data-brandid="36">丰田</dd>
                <dd data-brandid="38">法拉利</dd>
                <dd data-brandid="35">福特</dd>
                <dd data-brandid="39">福田</dd>
                <dd data-brandid="40">福迪</dd>
                <dd data-brandid="37">菲亚特</dd>
                <dd data-brandid="41">飞驰商务车</dd>
                <dd data-brandid="162">福汽启腾</dd>
                <dt data-initial="G">G</dt>
                <dd data-brandid="47">GMC</dd>
                <dd data-brandid="48">光冈</dd>
                <dd data-brandid="44">广汽传祺</dd>
                <dd data-brandid="45">广汽吉奥</dd>
                <dd data-brandid="46">观致</dd>
                <dt data-initial="H">H</dt>
                <dd data-brandid="146">华普</dd>
                <dd data-brandid="52">华泰</dd>
                <dd data-brandid="147">华翔富奇</dd>
                <dd data-brandid="160">华颂</dd>
                <dd data-brandid="50">哈弗</dd>
                <dd data-brandid="56">哈飞</dd>
                <dd data-brandid="58">恒天</dd>
                <dd data-brandid="145">悍马</dd>
                <dd data-brandid="59">汇众</dd>
                <dd data-brandid="57">海格</dd>
                <dd data-brandid="51">海马</dd>
                <dd data-brandid="54">海马商用车</dd>
                <dd data-brandid="53">红旗</dd>
                <dd data-brandid="55">黄海</dd>
                <dd data-brandid="173">华泰新能源</dd>
                <dd data-brandid="495">汉腾</dd>
                <dt data-initial="J">J</dt>
                <dd data-brandid="61">Jeep</dd>
                <dd data-brandid="70">九龙</dd>
                <dd data-brandid="143">吉利</dd>
                <dd data-brandid="62">吉利全球鹰</dd>
                <dd data-brandid="63">吉利帝豪</dd>
                <dd data-brandid="65">吉利英伦</dd>
                <dd data-brandid="64">捷豹</dd>
                <dd data-brandid="68">江南</dd>
                <dd data-brandid="60">江淮</dd>
                <dd data-brandid="66">江铃</dd>
                <dd data-brandid="71">金旅客车</dd>
                <dd data-brandid="67">金杯</dd>
                <dd data-brandid="69">金龙联合</dd>
                <dt data-initial="K">K</dt>
                <dd data-brandid="74">克莱斯勒</dd>
                <dd data-brandid="157">凯翼</dd>
                <dd data-brandid="73">凯迪拉克</dd>
                <dd data-brandid="158">卡威</dd>
                <dd data-brandid="77">卡尔森</dd>
                <dd data-brandid="75">开瑞</dd>
                <dd data-brandid="76">科尼塞克</dd>
                <dt data-initial="L">L</dt>
                <dd data-brandid="82">兰博基尼</dd>
                <dd data-brandid="81">力帆</dd>
                <dd data-brandid="86">劳斯莱斯</dd>
                <dd data-brandid="87">林肯</dd>
                <dd data-brandid="85">猎豹</dd>
                <dd data-brandid="89">理念</dd>
                <dd data-brandid="88">莲花</dd>
                <dd data-brandid="90">路特斯</dd>
                <dd data-brandid="79">路虎</dd>
                <dd data-brandid="78">铃木</dd>
                <dd data-brandid="83">陆风</dd>
                <dd data-brandid="80">雷克萨斯</dd>
                <dd data-brandid="84">雷诺</dd>
                <dt data-initial="M">M</dt>
                <dd data-brandid="93">MG</dd>
                <dd data-brandid="94">MINI</dd>
                <dd data-brandid="98">摩根</dd>
                <dd data-brandid="96">玛莎拉蒂</dd>
                <dd data-brandid="99">美亚</dd>
                <dd data-brandid="97">迈凯伦</dd>
                <dd data-brandid="95">迈巴赫</dd>
                <dd data-brandid="92">马自达</dd>
                <dt data-initial="N">N</dt>
                <dd data-brandid="100">纳智捷</dd>
                <dt data-initial="O">O</dt>
                <dd data-brandid="102">欧宝</dd>
                <dd data-brandid="103">欧朗</dd>
                <dd data-brandid="101">讴歌</dd>
                <dt data-initial="Q">Q</dt>
                <dd data-brandid="106">启辰</dd>
                <dd data-brandid="105">奇瑞</dd>
                <dd data-brandid="107">庆铃</dd>
                <dd data-brandid="104">起亚</dd>
                <dt data-initial="R">R</dt>
                <dd data-brandid="108">日产</dd>
                <dd data-brandid="110">瑞麒</dd>
                <dd data-brandid="109">荣威</dd>
                <dt data-initial="S">S</dt>
                <dd data-brandid="116">Smart</dd>
                <dd data-brandid="111">三菱</dd>
                <dd data-brandid="118">世爵</dd>
                <dd data-brandid="117">双环</dd>
                <dd data-brandid="114">双龙</dd>
                <dd data-brandid="113">斯巴鲁</dd>
                <dd data-brandid="112">斯柯达</dd>
                <dd data-brandid="149">萨博</dd>
                <dd data-brandid="169">思铭</dd>
                <dd data-brandid="498">斯威</dd>
                <dd data-brandid="500">赛麟</dd>
                <dd data-brandid="501">陕汽通家</dd>
                <dt data-initial="T">T</dt>
                <dd data-brandid="120">TESLA</dd>
                <dd data-brandid="150">天马</dd>
                <dd data-brandid="166">腾势</dd>
                <dt data-initial="W">W</dt>
                <dd data-brandid="121">五菱</dd>
                <dd data-brandid="124">威兹曼</dd>
                <dd data-brandid="123">威麟</dd>
                <dd data-brandid="122">沃尔沃</dd>
                <dd data-brandid="163">五十铃</dd>
                <dt data-initial="X">X</dt>
                <dd data-brandid="155">夏利</dd>
                <dd data-brandid="130">新凯</dd>
                <dd data-brandid="151">新大地</dd>
                <dd data-brandid="148">新雅途</dd>
                <dd data-brandid="125">现代</dd>
                <dd data-brandid="128">西雅特</dd>
                <dd data-brandid="126">雪佛兰</dd>
                <dd data-brandid="127">雪铁龙</dd>
                <dt data-initial="Y">Y</dt>
                <dd data-brandid="131">一汽</dd>
                <dd data-brandid="135">依维柯</dd>
                <dd data-brandid="136">扬州亚星客车</dd>
                <dd data-brandid="134">永源</dd>
                <dd data-brandid="159">英致</dd>
                <dd data-brandid="132">英菲尼迪</dd>
                <dd data-brandid="133">野马</dd>
                <dt data-initial="Z">Z</dt>
                <dd data-brandid="139">中兴</dd>
                <dd data-brandid="137">中华</dd>
                <dd data-brandid="152">中客华北</dd>
                <dd data-brandid="140">中欧</dd>
                <dd data-brandid="154">中顺</dd>
                <dd data-brandid="138">众泰</dd>
                <dd data-brandid="168">知豆</dd>
            </dl>
        </section>
    </section>
    <!--车系-->
    <section class="panel-initial" id="showSeries">
        <header class="panel-header">
            <a id="close-series">返回</a>
            <strong>选择车系</strong>
        </header>
        <section class="panel-main">
            <dl class="list series-list"></dl>
        </section>
    </section>
    <!--车型-->
    <section class="panel-initial" id="showModel">
        <header class="panel-header">
            <a id="close-model">返回</a>
            <strong>选择车型</strong>
        </header>
        <section class="panel-main">
            <dl class="list model-list"></dl>
        </section>
    </section>
</div>
<link rel="stylesheet" href="css/mui.min.css"/>
<link rel="stylesheet" href="css/mui.picker.css"/>
<link rel="stylesheet" href="css/mui.poppicker.css"/>
<link rel="stylesheet" href="css/mui.dtpicker.css"/>
<script type="text/javascript" src="js/mui/mui.min.js"></script>
<script type="text/javascript" src="js/mui/mui.picker.min.js"></script>
<script type="text/javascript" src="js/mui/mui.poppicker.js"></script>
<script type="text/javascript" src="js/mui/city.data-3.js"></script>
<script type="text/javascript" src="js/mui/city.js"></script>
<script>
    var cityObj;
    function selectCity() {
        picker.show(SelectedItemsCallback)
    }
    //表单切换下一页
    $(".next").click(function () {
        $(".page1").hide();
        $(".page2").fadeIn();
    });

    //MUI 仿ios 选择框
    (function ($, doc) {
        $.init();
        $.ready(function () {
            //所在地区
            var cityPicker3 = new $.PopPicker({
                layer: 2
            });
            cityPicker3.setData(cityData3);
            var showCityPickerButton = document.getElementById('index_location');
            showCityPickerButton.addEventListener('tap', function (event) {
                cityPicker3.show(function (items) {
//                        doc.getElementById('s').innerText=(items[0] || {}).text;
                    var city = (items[1] || {}).text;
                    document.getElementById("c").value = city;
                    //返回 false 可以阻止选择框的关闭
                    //return false;
                });
            }, false);
        });
    })(mui, document);

    function initAllCity(){
        $.ajax({
            url: getRootPath() + "/wechat/carValuation/getAllCity.htm",
            dataType: "json",
            type: "post",
            success: function (data) {
                console.log(data);
                cityObj=data;

            }, error: function (res) {
                console.log(res.mess);
            }
        });
    }

    //       日期
    (function ($) {
        initAllCity();
        $.init();
        var btns = $('#date');
        btns.each(function (i, btn) {
            btn.addEventListener('tap', function () {
                var optionsJson = this.getAttribute('data-options') || '{}';
                var options = JSON.parse(optionsJson);
                var id = this.getAttribute('id');
                /*
                 * 首次显示时实例化组件
                 * 示例为了简洁，将 options 放在了按钮的 dom 上
                 * 也可以直接通过代码声明 optinos 用于实例化 DtPicker
                 */
                var picker = new $.DtPicker(options);
                picker.show(function (rs) {
                    /*
                     * rs.value 拼合后的 value
                     * rs.text 拼合后的 text
                     * rs.y 年，可以通过 rs.y.vaue 和 rs.y.text 获取值和文本
                     * rs.m 月，用法同年
                     * rs.d 日，用法同年
                     * rs.h 时，用法同年
                     * rs.i 分（minutes 的第二个字母），用法同年
                     */
                    document.getElementById("date").value = rs.text;
                    /*
                     * 返回 false 可以阻止选择框的关闭
                     * return false;
                     */
                    /*
                     * 释放组件资源，释放后将将不能再操作组件
                     * 通常情况下，不需要示放组件，new DtPicker(options) 后，可以一直使用。
                     * 当前示例，因为内容较多，如不进行资原释放，在某些设备上会较慢。
                     * 所以每次用完便立即调用 dispose 进行释放，下次用时再创建新实例。
                     */
                    picker.dispose();
                });
            }, false);
        });
    })(mui);
    function OnInput(event) {
        var car = $('#userCar').val();
        var city = $('#c').val();
        var date = $('#date').val();
        var miles = $('#miles').val();
        if (!car) {
            TipShow('请选择车型', 1000);
        }
        else if (!city) {
            TipShow('请选择所在城市', 1000);
        } else if (!date) {
            TipShow('请选择首次上牌时间', 1000);
        }
        else if (!miles) {
            TipShow('请输入行驶里程', 1000);
        }
        else {
            $('.start_val').hide();
            $('.end_val').show();
        }
    }
    function startCul() {
        var car = $('#userCar').val();
        var modelId = $('#userCar').attr("data-modelid");//modelId车型标识
        var city = "";
        $.each(cityObj.city_list,function(index,item){
            if($('#c').val().indexOf(item.city_name)>-1){
                city=item.city_id;
            }
        })
        var date = $('#date').val();
        var miles = $('#miles').val();
        var apiUrl = "https://dingjia.che300.com/";
        var h_speed = 0.1;//步长
        var totalValue = 0;
        var speedTime = 5;//毫秒
        if (!car) {
            TipShow('请选择车型', 1000);
        }
        else if (!city) {
            TipShow('请选择所在城市', 1000);
        } else if (!date) {
            TipShow('请选择首次上牌时间', 1000);
        }
        else if (!miles) {
            TipShow('请输入行驶里程', 1000);
        } else {
            loading("start");
            window.scrollTo(0,0);
            $.ajax({
                url: getRootPath() + "/wechat/carValuation/valuation.htm",
                data: {"modelId": modelId, "regDate": date, "mile": miles, "city": city,"status":"0"},
                dataType: "json",
                type: "post",
                success: function (data) {
                    loading("stop");
                    console.log(data);
                    $("#price").html(data.mess);
                    var h_count = data.mess;
                    var countUPs = setInterval(function countUp() {
                        if (h_count * 1 >= totalValue) {
                            totalValue += h_speed;
                            $('.carValuation_banner_div2').html(totalValue.toFixed(2));
                        }
                        else {
                            try {
                                $('.carValuation_banner_div2').html((h_count + '').toFixed(2));
                            } catch (e) {
                                $('.carValuation_banner_div2').html((h_count + ''));
                            }
                            clearInterval(countUPs);
                        }
                    }, speedTime);
                    $('.goSellCarBtn').show();
                }, error: function (res) {
                    console.log(res.mess);
                }
            });
        }
    }

</script>
</body>
</html>