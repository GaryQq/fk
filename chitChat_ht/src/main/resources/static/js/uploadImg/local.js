﻿/*疯狂体育专家推荐 H5公用方法*/

var FK = FK || {};

FK.config = {};
FK.config = {
    source: '',
    // newversion: '1.0',
    sid: '36001800000',
    mac: '',
    ajaxUrl: '/api/zjtj.action?accessSecretData=',
    ajaxUrls: ''
}

/************** 本地存储 **************/
FK.storage = {
    // 临时存储
    session: function(name, val) { // 存储、读取
        var len = arguments.length;

        if (len > 1) {
            if (!name || !val) {
                return false;
            }
            try {
                sessionStorage.setItem(name, val);
            } catch (e) {
                FK.cookie.setCookie(name, val, 'd1'); //cookie存365天（永久）
            }
        } else {
            if (!name) {
                return false;
            }
            var dataStr;
            try {
                sessionStorage.setItem('cookieTest', 'test'); //看是否支持存储
                dataStr = sessionStorage.getItem(name);
            } catch (e) {
                dataStr = FK.cookie.getCookie(name);
            }
            return dataStr;
        }
    },

    retSessionObj: function(name) { // 将值转成对象，如果不存在返回false
        if (!name) {
            return false;
        }
        var dataStr = sessionStorage.getItem(name);
        if (dataStr) {
            return JSON.parse(dataStr);
        } else {
            return false;
        }
    },

    clearSession: function(name) { // 删除
        if (!name) {
            return false;
        }
        try {
            sessionStorage.setItem('cookieTest', 'test'); //看是否支持存储
            sessionStorage.removeItem(name);
        } catch (e) {
            FK.cookie.delCookie(name);
        }
    },

    // 永久存储
    local: function(name, val) { // 存储、读取
        var len = arguments.length;

        if (len > 1) {
            try {
                localStorage.setItem(name, val);
            } catch (e) {
                FK.cookie.setCookie(name, val, 'd365'); //cookie存365天（永久）
            }

        } else {
            var dataStr = '';
            try {
                localStorage.setItem('cookieTest', 'test'); //看是否支持存储
                dataStr = localStorage.getItem(name);
            } catch (e) {
                dataStr = FK.cookie.getCookie(name)
            }

            return dataStr;
        }
    },

    retLocalObj: function(name) { // 将值转成对象，如果不存在返回false
        if (!name) {
            return false;
        }
        var dataStr = '';
        try {
            localStorage.setItem('cookieTest', 'test'); //看是否支持存储
            dataStr = localStorage.getItem(name);
        } catch (e) {
            dataStr = unescape(FK.cookie.getCookie(name))

        }

        //alert(dataStr)
        if (dataStr && dataStr != 'null') {
            var result = "";
            try {
                result = JSON.parse(dataStr);
            } catch (e) {
                result = false
            }
            // alert(dataStr)
            return result
        } else {
            return false;
        }
    },

    clearLocal: function(name) { // 删除
        if (!name) {
            return false;
        }
        try {
            localStorage.setItem('cookieTest', 'test'); //看是否支持存储
            localStorage.removeItem(name);
        } catch (e) {
            FK.cookie.delCookie(name);
        }

    },

    // 存储所有
    clearAll: function() {
        sessionStorage.clear();
        localStorage.clear();
    }
}

/************** cookie **************/
FK.cookie = {
    //读取cookies
    getCookie: function(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) {
            if (!arr[2]) {
                return null;
            } else if (arr[2] != 'null') {
                return unescape(arr[2]);
            } else {
                return null;
            };
        } else {
            return null;
        }

    },
    //删除cookies
    delCookie: function(name) {
        document.cookie = name + "=" + null + ";expires=" + 0 + ";path=/";
    },
    //设置Cookie
    setCookie: function(name, value, time) {
        var strsec = this.getsec(time);
        var exp = new Date();
        exp.setTime(exp.getTime() + strsec * 1);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";
    },
    //时间转换
    getsec: function(str) {
        var str1 = str.substring(1, str.length) * 1;
        var str2 = str.substring(0, 1);
        if (str2 == "s") {
            return str1 * 1000;
        } else if (str2 == "h") {
            return str1 * 60 * 60 * 1000;
        } else if (str2 == "d") {
            return str1 * 24 * 60 * 60 * 1000;
        }
    }
}

/************** 插件 **************/
FK.plugin = {};
// 对话框－动态插入内容
FK.plugin.dialog = function(opt, blo) {
    $('#maskWin,.layerWap').remove(); //连续出现提示框 先清除之前的提示框
    var options = $.extend({
        title: "提示", // 标题
        className: "", // 外框的className，layerWap layerB（A）
        content: "", // 内容可以是html
        width: "", // 设置高度，默认为auto
        height: "auto", // 设置高度，默认是auto
        zIndex: 10001, // z轴位置，默认10001,
        isBg: true, // 是否要黑色背景
        isTitle: true, // 是否要标题
        isClose: true, // 是否要关闭按钮
        autoClose: false, // 是否自动关闭
        autoCloseTime: 500, // 自动关闭时间
        opacity: .3, // 透明度
        confirmBtn: false, // 是否需要确定按钮
        repealBtn: false, // 是否需要取消按钮
        confirmTip: "确定", // 确定按钮文字
        repealTip: "取消", // 取消按钮文字
        onClose: function() {}, // 关闭时回调函数
        confirmCallBack: function() {}, // 确定回调函数
        repealCallBack: function() {} // 取消时回调函灵敏
    }, opt || {});

    var createDialog = function() {
        var o = options,
            dialogHTML = "",
            dialogBox = $("<div class='" + o.className + "'>"),
            stopCode = false;

        dialogBox.css({
            "width": o.width,
            "height": o.height,
            "zIndex": o.zIndex
        });
        $("body").append(dialogBox);

        // 是否创建标题
        if (o.isTitle) {
            dialogHTML += "<h2>" + o.title + "</h2>";
        }
        dialogHTML += "<div class='layerCon'>" + o.content + "</div>"; // pack-box
        dialogHTML += "<div class='layerBtn' id='diaBtn'></div>";
        dialogBox.html(dialogHTML);

        // 是否创建按钮
        var btnCls = '';
        btnCls = (o.confirmBtn && o.repealBtn) ? 'inp_a' : 'inp_b';
        if (o.repealBtn) {
            $("#diaBtn").append("<input type='button' id='repBtn' class='" + btnCls + "' value='" + o.repealTip + "'>");
        }
        if (o.confirmBtn) {
            $("#diaBtn").append("<input type='button' class='" + btnCls + "' id='conBtn' value='" + o.confirmTip + "'>");
        }


        // 是否要mask层
        if (o.isBg) {
            addMaskWin();
        }

        // 确定、取消回调函数
        $("#conBtn").click(function(res) {
            if (o.confirmCallBack) {
                stopCode = o.confirmCallBack();
                if (stopCode) { // 如果回调true终止程序
                    return false;
                }
            }

            closeDialog();
        });

        $("#repBtn").click(function(e) {
            if (o.repealCallBack) {
                stopCode = o.repealCallBack();
                if (stopCode) { // 如果回调true终止程序
                    return false;
                }
            }
            closeDialog();
            e.stopPropagation();
        });

        // 关闭层
        $(".close").click(function(e) {
            closeDialog();
            e.stopPropagation();
        })

        dialogPos(dialogBox);

        $(window).resize(function(event) {
            dialogPos(dialogBox);
            window.removeEventListener("touchmove", clearMove, false);
        });

        window.addEventListener("touchmove", clearMove, false);

        if (o.autoClose) {
            // setTimeout(function() {
            //     closeDialog();
            // }, o.autoCloseTime);
            closeDialog();
        }
    }

    var clearMove = function(e) {
        var e = event || e;
        if (e && e.preventDefault) {
            // e.preventDefault();
        } else {
            window.event.returnValue = false;
        }
        return false;
    }

    // 设置弹层位置
    var dialogPos = function(oBox) {
        var nWidth = oBox.width() / 2;
        var nHeight = oBox.height() / 2;
        var nScrollT = $(window).scrollTop();

        oBox.css({
            "position": "absolute",
            "top": ($(window).height() - oBox.height()) / 2 + nScrollT + "px",
            "left": "50%",
            "margin-left": "-" + nWidth + "px",
            // "margin-top": "-" + (nHeight-nScrollT) + "px",
            "z-index": "9999"
        }).fadeIn();
    }

    // 横竖屏旋转
    function updateLayout(event) {

        // 判断浏览器是否支持orientation属性
        if (event.orientation) {
            if (event.orientation == 'portrait') {
                alert("portrait"); //竖屏
            } else if (event.orientation == 'landscape') {
                alert("landscape"); //横评
            }
        }
    }

    // 创建遮照层
    var addMaskWin = function() {
        var overlayCss = {
                position: 'fixed',
                zIndex: '999',
                top: '0px',
                left: '0px',
                bottom: '0px',
                height: '100%',
                width: '100%',
                backgroundColor: '#000',
                filter: 'alpha(opacity=60)',
                opacity: 0.6
            },
            overlayCss2 = { //for ie 6
                position: 'absolute',
                height: $(window).height()
            };
        var overlay = $('<div id="maskWin" class="daskbg" />');
        $('body').append(overlay.css(overlayCss));
        $('#Overlay').animate({
            backgroundColor: overlayCss.backgroundColor,
            opacity: overlayCss.opacity
        }, 0);
    }

    // 关闭Dialog窗口并执行回调
    var closeDialog = function() {
        var o = options;
        var dialogBox = $(".layerWap");
        var isClose = false

        $("#maskWin").remove();
        dialogBox.remove();
        isClose = true;

        /*if(o.isBg){
         $("#maskWin").remove();
         dialogBox.remove();
         isClose = true;
         }
         else{
         dialogBox.remove();
         isClose = true;
         }*/

        window.removeEventListener("touchmove", clearMove, false);

        return isClose;
    }

    createDialog();

    return {
        closeDialog: closeDialog,
        options: options
    }
}

FK.plugin.messageDialog = function(msg) {
        var $dom = $(".widng");
        if (!$dom.get(0)) {
            $dom = $('<div class="widng"><div class="tsk">' +
                '<p>提示</p>' +
                '<p>' + msg + '</p></div></div>');
            $('body').append($dom);
        } else {
            $dom.find('.qptx').html(msg);
        }
        $(".widng").fadeIn(50, function() {
            var time = setTimeout(function() {
                clearTimeout(time);
                $(".widng").fadeOut(80);
            }, 1500);
        });
    }
    //页面效果模块(玩法推荐筛选)
FK.effect = FK.effect || {};
FK.effect.slideLeftHide = $.fn.slideLeftHide = function(speed, selector, callback) { //滑出
    this.animate({
        left: '100%'
    }, speed, callback)
    setTimeout(function() {
        $(selector).css("display", "none")
    }, speed)
}
FK.effect.slideLeftShow = $.fn.slideLeftShow = function(speed, selector, callback) {
    $(selector).css("display", "block")
    this.animate({
        left: 0
    }, speed, callback);
}

// 对话框－固定内容加载
FK.plugin.dialogStatic = function(oId) {
    var oId = $(oId);

    oId.css("position", "absolute")
        .css("top", ($(window).height() - oId.height()) / 2 + $(window).scrollTop())
        .css("left", ($(window).width() - oId.width()) / 2)
        .css("z-index", "9999").fadeIn();

    var dialogPos = function() {
        oId.css("position", "absolute")
            .css("top", ($(window).height() - oId.height()) / 2 + $(window).scrollTop())
            .css("left", ($(window).width() - oId.width()) / 2)
            .css("z-index", "9999");

        // window.addEventListener("touchmove", clearMove, false);
    }

    var clearMove = function(e) {
        var e = event || e;
        if (e && e.preventDefault) {
            e.preventDefault();
        } else {
            window.event.returnValue = false;
        }
        return false;
    }

    dialogPos();

    // dialog居中并随滚动条滚动而改变位置
    $(window).resize(function(event) {
        dialogPos();
        window.removeEventListener("touchmove", clearMove, false);
    });

    $(window).scroll(function(event) {
        dialogPos();
        window.removeEventListener("touchmove", clearMove, false);
    });

    $("#maskWinStatic").fadeIn();

    return {
        clearMove: clearMove
    }
}

/************** 业务模块 **************/
// 呼叫客服
FK.business = FK.business || {};

FK.business.callPhone = function(tel) {
        var oPlugin = FK.plugin;

        oPlugin.dialog({
            className: "layerWap layerB",
            content: "<p class='alignCenter'>" + tel + "</p>",
            repealBtn: true,
            confirmBtn: true,
            repealTip: "取消",
            confirmTip: "呼叫",
            confirmCallBack: function() {
                location.href = "tel:" + tel;
            }
        });
    }
    // 获取url中的参数
FK.business.queryUrl = function(url, key) {
    url = url.replace(/^[^?=]*\?/ig, '').split('#')[0]; // 去除网址与hash信息
    var json = {};

    url.replace(/(^|&)([^&=]+)=([^&]*)/g, function(a, b, key, value) {
        try {
            key = decodeURIComponent(key);
        } catch (e) {}

        try {
            value = decodeURIComponent(value);
        } catch (e) {}

        if (!(key in json)) {
            json[key] = /\[\]$/.test(key) ? [value] : value;
        } else if (json[key] instanceof Array) {
            json[key].push(value);
        } else {
            json[key] = [json[key], value];
        }
    });
    return key ? json[key] : json;
}

// 是否登录  false 未登录、true已登录
FK.business.isLogin = function() {
        if (FK.session.local('userId')) {
            return true
        }
        return false;

    }
    //判断是否在微信内置浏览器访问
FK.business.is_weixn = function() {

        var ua = navigator.userAgent.toLowerCase();

        if (ua.match(/MicroMessenger/i) == "micromessenger") {

            return true;

        } else {

            return false;

        }

    }
    // 判断是否为ios
FK.business.is_ios = function() {

        var ua = navigator.userAgent;

        if (!!ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {

            return true;

        } else {

            return false;

        }

    }
    //H5判断是否登录
FK.business.login = function() {

    if (FK.storage.local("userId") || FK.storage.session("userId")) {
        return true;

    } else {
        return false;
    }
}
FK.business.isCardID = function(sId) {
        var aCity = {
            11: "北京",
            12: "天津",
            13: "河北",
            14: "山西",
            15: "内蒙古",
            21: "辽宁",
            22: "吉林",
            23: "黑龙江",
            31: "上海",
            32: "江苏",
            33: "浙江",
            34: "安徽",
            35: "福建",
            36: "江西",
            37: "山东",
            41: "河南",
            42: "湖北",
            43: "湖南",
            44: "广东",
            45: "广西",
            46: "海南",
            50: "重庆",
            51: "四川",
            52: "贵州",
            53: "云南",
            54: "西藏",
            61: "陕西",
            62: "甘肃",
            63: "青海",
            64: "宁夏",
            65: "新疆",
            71: "台湾",
            81: "香港",
            82: "澳门",
            91: "国外"
        }
        var iSum = 0;
        var info = "";
        if (!/^\d{17}(\d|x)$/i.test(sId)) return false;
        sId = sId.replace(/x$/i, "a");
        if (aCity[parseInt(sId.substr(0, 2))] == null) return false;
        sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2));
        var d = new Date(sBirthday.replace(/-/g, "/"));
        if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())) return false;
        for (var i = 17; i >= 0; i--) iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
        if (iSum % 11 != 1) return false;
        //aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女");//此次还可以判断出输入的身份证号的人性别
        return true;

    }
    /***************** 名医挂号订单详情弹框 *****************/
FK.plugin.dialogDetail = function(opt) {
    var ops = $.extend({
        id: "tx_wind1",
        contentClass: 'wind_txt1',
        btnClass: 'windbtn_block',
        resize: false
    }, opt);
    var $dom,
        $shadow,
        iTimerID,
        addClass;
    var initHandle = {
        _self: this,
        initDialog: function() {
            //var ops = this._self;
            $dom = $('#dialog_con');
            if (!$dom.get(0)) {
                var title = ops.resize ? '<h2>提示</h2>' : '';
                if (ops.btnClass == 'windbtn_block') {
                    addClass = 'wind_twobtn';
                }
                $dom = $('<div class="' + ops.id + '" id="dialog_con" style="position:fixed;z-index:999;">' + title + '<p class="' + ops.contentClass + '"></p><div class="' + ops.btnClass + ' ' + addClass + '"></div></div>');
                $("body").append($dom);
            }
            $dom.show();
        },
        shadow: function() {
            $shadow = $('.wind_bg');
            if (!$shadow.get(0)) {
                $shadow = $("<div class='wind_bg'></div>");
                $("body").append($shadow);
            }
            $shadow.show();
        },
        open: function(msg, btnMsg, sec) {
            clearTimeout(iTimerID);
            this.initDialog();
            this.shadow();
            $("." + ops.contentClass).html(msg);
            $("." + ops.btnClass).html(btnMsg);
            $dom.css({
                "top": ($(window).height() - $dom.height()) / 2,
                "fontFamily": "Helvetica"
            });
            if (typeof(sec) == 'number') {
                iTimerID = setTimeout(function() {
                    $shadow.hide();
                    $dom.hide();
                }, sec);
            }
        },
        close: function() {
            $("#dialog_con").hide();
            $('.wind_bg').hide();
        }
    };
    return initHandle;
};

/****************方案购买***********/
//emoneys: 金钱 、orderid：订单号、logname：登录账号、code：彩种、agengsid：代理人账号、coupon_id：优惠券、coupon_type:优惠券类型
FK.business.buyPlan = function(emoneys, orderid, logname, code, agengsid) {
        if (!agengsid) {
            agengsid = sessionStorage.getItem("income");
            if (!agengsid) {
                agengsid = "";
            }
        }
        if (emoneys !== "0" || emoneys == "") {
            if (!logname) {
                // FK.plugin.dialog({
                //     className: "layerWap layerB",
                //     content: '未登录，请去登陆',
                //     title: "",
                //     confirmBtn: true,
                //     repealBtn: true,
                //     confirmTip: "确定",
                //     repealTip: "取消",
                //     confirmCallBack: function(){
                 var this_back_url = window.location.href;
                    sessionStorage.jumpUrl = this_back_url;
                window.location.href = "login.jsp";
                //     },
                //     repealCallBack: function() {

                //     }
                // });
            } else {
                if (orderid == "" || !orderid) {
                    FK.plugin.dialog({
                        className: "layerWap layerB",
                        content: '订单号码错误',
                        title: "",
                        confirmBtn: true,
                        confirmTip: "确定",
                        confirmCallBack: function() {
                            window.location.href = "rechargeList.jsp";
                        }
                    });
                } else {
                    var verifyData = {
                        "serviceName": "expertService",
                        "methodName": "verifyIsBuyPlanByUserNameErAgintOrderId",
                        "parameters": {
                            "loginUserName": logname,
                            "er_agint_order_id": orderid
                        }
                    };
                    verifyData = JSON.stringify(verifyData);
                    $.ajax({
                        url: FK.config.ajaxUrl + verifyData,
                        type: "POST",
                        dataType: "json",
                        async: false,
                        success: function(rdata) {
                            if (code == "" || !code) {
                                FK.plugin.dialog({
                                    className: "layerWap layerB",
                                    content: '彩种ID为空',
                                    title: "错误",
                                    confirmBtn: true,
                                    confirmTip: "确定",
                                    confirmCallBack: function() {

                                    }
                                });
                            }
                            var isbuy = rdata.result.code;
                            if (isbuy == "0000") {
                                window.location.href = 'plans.jsp?emoney=' + emoneys + '&planid=' + orderid + '&lotid=' + code;
                            } else if (isbuy == "0400") {
                                var postPayData = {
                                    "serviceName": "commonExpertService",
                                    "methodName": "buyPlan",
                                    "parameters": {
                                        "loginUserName": logname,
                                        "erAgintOrderId": orderid,
                                        "clientType": "3",
                                        "lotteryClassCode": code,
                                        "orderSource": "36001800000",
                                        "payType": "0",
                                        "agentUserName": agengsid
                                            //"coupon_id":coupon_id,
                                            //"coupon_type":coupon_type
                                    }
                                };
                                postPayData = JSON.stringify(postPayData);
                                $.ajax({
                                    url: FK.config.ajaxUrl + postPayData,
                                    type: "POST",
                                    dataType: "json",
                                    success: function(data) {
                                        var vCode = data.result.code;
                                        if (vCode == "0000") {
                                            window.location.href = 'plans.jsp?emoney=' + emoneys + '&planid=' + orderid + '&lotid=' + code;
                                        } else if (vCode == "0301") { //不足
                                            FK.plugin.dialog({
                                                className: "layerWap layerB",
                                                content: '余额不足，请去充值',
                                                title: "",
                                                confirmBtn: true,
                                                repealBtn: true,
                                                confirmTip: "去充值",
                                                repealTip: "取消",
                                                confirmCallBack: function() {
                                                    var quBiao=localStorage.getItem('clintType');
                                                    if(quBiao){
                                                        window.location.href = "http://cp.recharge.com/czlb";
                                                    }else{
                                                        window.location.href = "recharge.jsp";
                                                    }
                                                },
                                                repealCallBack: function() {

                                                }
                                            });
                                        } else if (vCode == "0201" || vCode == "0202") {
                                            FK.plugin.dialog({
                                                className: "layerWap layerB",
                                                content: '该方案不存在或已过期',
                                                title: "",
                                                confirmBtn: true,
                                                confirmTip: "确定",
                                                confirmCallBack: function() {

                                                }
                                            });
                                        } else if (vCode == "0302") {
                                            FK.plugin.dialog({
                                                className: "layerWap layerB",
                                                content: '未登录，请先去登陆',
                                                title: "",
                                                confirmBtn: true,
                                                confirmTip: "确定",
                                                confirmCallBack: function() {
                                                     var this_back_url = window.location.href;
                                                    sessionStorage.jumpUrl = this_back_url;
                                                    window.location.href = "login.jsp"
                                                }
                                            });
                                        } else {
                                            FK.plugin.dialog({
                                                className: "layerWap layerB",
                                                content: '您提交的太频繁，请稍后再试',
                                                title: "",
                                                confirmBtn: true,
                                                confirmTip: "确定",
                                                confirmCallBack: function() {

                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        } else {
            window.location.href = 'plans.jsp?emoney=' + emoneys + '&planid=' + orderid + '&lotid=' + code;
        }
    }
    /*********获取当前元素的属性********/
FK.business.experty_data = function(selector, key) {
        return $(selector).attr(key);
    },
    /*********选择购买***********/
    //selector:事件主题   index：false或者true(首页)
    FK.business.selector = function(selector, index) {
        var _this = FK.business;
        $(selector).click(function() {
                var that = this;
                var emoneys = $(that).val().replace(/[^0-9\.]/ig, '') ? $(that).val().replace(/[^0-9\.]/ig, '') : '0',
                    logname = FK.storage.session('userName'),
                    orderid = _this.experty_data(that, 'orderid'),
                    agengsid = '',
                    code = _this.experty_data(that, 'lotteryClassCode');
                if ($(that).val() == '免费') {
                    window.location.href = 'plans.jsp?emoney=' + emoneys + '&planid=' + orderid + '&lotid=' + code;

                    return;
                }
                if (!logname) {
                    var this_back_url = window.location.href;
                    sessionStorage.jumpUrl = this_back_url;
                    window.location.href = 'login.jsp'
                };
                if (index) { //判断是否是首页
                    agengsid = FK.business.queryUrl(window.location.href, 'asid') ? FK.business.queryUrl(window.location.href, 'asid') : '';
                } else {
                    agengsid = FK.storage.session('income') ? FK.storage.session('income') : '';
                }
                FK.business.isBuy(logname, orderid, emoneys, code, agengsid);
            })
            // console.log($(this).val().replace(/[^0-9\.]/ig, ''));   
    }
    // 判断是否购买过
FK.business.isBuy = function(logname, orderid, emoneys, code, agengsid) {
    var verifyData = {
        "serviceName": "expertService",
        "methodName": "verifyIsBuyPlanByUserNameErAgintOrderId",
        "parameters": {
            "loginUserName": logname,
            "er_agint_order_id": orderid
        }
    }

    verifyData = JSON.stringify(verifyData);
    $.ajax({
        url: FK.config.ajaxUrl + verifyData,
        type: "POST",
        dataType: "json",
        async: false,
        success: function(rdata) {
            var isbuy = rdata.result.code ? rdata.result.code : rdata.resultCode;
            if (isbuy == "0000") {
                window.location.href = 'plans.jsp?emoney=' + emoneys + '&planid=' + orderid + '&lotid=' + code;

            } else if (isbuy == "0400") {
                FK.plugin.dialog({
                    className: "layerWap layerB",
                    content: '您将支付余额<em class="maTeng">'+emoneys+'元</em>',
                    title: "",
                    confirmBtn: true,
                    repealBtn: true,
                    confirmTip: "确认支付",
                    repealTip: "取消",
                    confirmCallBack: function() {
                        FK.business.buyPlan(emoneys, orderid, logname, code, agengsid);

                    }, // 确定回调函数
                    repealCallBack: function() {

                    }
                })
            } else if (isbuy == "9999") {
                 var this_back_url = window.location.href;
                sessionStorage.jumpUrl = this_back_url;
                window.location.href = 'login.jsp'
            }

        }
    })
}