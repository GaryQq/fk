<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

%>

<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="http://www.fengkuang.cn/css/reset.css">
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/index.css">
    <script src="http://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <style>
        a.foota,a.foota:hover,a.foota:active,a.foota:visited{
            outline-width: 0;
            text-decoration: none;
            color: #999;
        }
    </style>
</head>
<body>
<div class="header">
    <div class="top layout">
        <div class="logo">
            <img src="http://www.fengkuang.cn/fbht/images/logo.png" alt="">
        </div>
        <div class="dlq">
            <div class="tab">
                <span id="loginByPass" class="on" style="cursor: pointer">密码登录</span>
                <span id="loginByAuthCode" style="cursor: pointer">验证码登录</span>
            </div>
            <div class="yzmdl" style="display: none">
                <input type="text" placeholder="请输入手机号" class="phone">
                <input type="text" placeholder="请输入验证码" class="yzm">
                <span style="cursor: pointer" attr="true" id="sendyzm">发送验证码</span>
            </div>
            <div class="zhdl">
                <input type="text" placeholder="请输入账号" class="passName">
                <input type="password" placeholder="请输入密码" class="passWord">
            </div>
            <div class="login" style="cursor: pointer">
                登录
            </div>
            <div class="line">
                <span>还没有账号？</span>
                <em style="cursor: pointer"><a style="color:#FFF;" href="<%=basePath%>/author/toApply">申请入驻</a></em>
            </div>
            <div class="xieyi">
                <i>
                    <input type="checkbox" checked="checked" id="agreeCheck">
                </i>
                <span>登录代表同意</span>
                <em style="cursor: pointer">《疯狂号用户协议》</em>
            </div>
        </div>
    </div>
</div>
<div class="content">
    <ul class="layout">
        <li>
            <img src="http://www.fengkuang.cn/fbht/images/zhinneg.png" alt="">
            <p>人工智能+体育</p>
            <div>
                <span>人工智能AI集</span>
                <em>简单写作 智能推荐</em>
            </div>
        </li>
        <li>
            <img src="http://www.fengkuang.cn/fbht/images/ziyouren.png" alt="">
            <p>体育专业自由人</p>
            <div>
                <span>用专业的知识创造价值</span>
                <em>实现财富和人生自由</em>
            </div>
        </li>
        <li>
            <img src="http://www.fengkuang.cn/fbht/images/ruzhu.png" alt="">
            <p>如何入驻</p>
            <div>
                <span>填写基本信息即可入驻</span>
                <em>疯狂体育APP注册作者可直接登录</em>
            </div>
        </li>
    </ul>
</div>
<div class="foot">
    <p class="layout">
        <a class="foota" href="http://www.fengkuang.cn/gf/aboutus.shtml" target="_blank">关于疯狂体育</a> | 
        <a class="foota" href="http://www.zthwan.com/yonghuxieyi.shtml" target="_blank">用户协议</a> |
        <a class="foota" href="https://weibo.com/u/6083248053" target="_blank">官方微博</a> | 
        <a class="foota" href="http://www.fengkuang.cn/gf/crazyapp.shtml" target="_blank">客户端下载</a> | 
        <a class="foota" href="mailto:service@fengkuang.cn">联系邮箱</a> | 联系电话：400-818-0518
    </p>
</div>
</body>
</html>

<script type="text/javascript">
    $("#loginByPass").click(function () {
        $(this).addClass("on");
        $("#loginByAuthCode").removeClass("on");
        $(".zhdl").hide();
        $(".yzmdl").show();
    });

    $("#loginByAuthCode").click(function () {
        $(this).addClass("on");
        $("#loginByPass").removeClass("on");
        $(".zhdl").show();
        $(".yzmdl").hide();
    });


    $(".login").click(function () {
        var loginTypeFlag;
        var passName;
        var password;
        if ('none' == $(".yzmdl").css("display")) {
            // 账号密码登录
            loginTypeFlag = 0;
            passName = $.trim($(".passName").val());
            password = $.trim($(".passWord").val());

            if (!passName) {
                alert("请输入账号！！！");
                return false;
            }

            if (!password) {
                alert("请输入密码！！！");
                return false;
            }

        } else if ('none' == $(".zhdl").css("display")) {
            // 验证码登录
            loginTypeFlag = 1;
            passName = $.trim($(".phone").val());
            password = $.trim($(".yzm").val());

            if (!passName) {
                alert("请输入手机号！！！");
                return false;
            }

            if (!password) {
                alert("请输入验证码！！！");
                return false;
            }

            if (!(/^1(3|4|5|7|8)\d{9}$/.test(passName))) {
                alert("手机号格式不正确！！！");
                return false;
            }

            if (!(/^\d{6}$/.test(password))) {
                alert("验证码格式不正确！！！");
                return false;
            }
        }

        if (!$("#agreeCheck").is(":checked")) {
            alert("请先同意用户协议！！！");
            return false;
        }

        $.ajax({
            type: "post",
            url: "<%=basePath%>author/login",
            data: {"loginTypeFlag": loginTypeFlag, "passName": passName, "password": password},
            success: function (msg) {
                if ("0000" == msg.code) {
                    window.location.href = "<%=basePath%>author/dispatch";
                } else {
                    alert(msg.message);
                }
            }
        });


    });

    $("#sendyzm").click(function () {
        var getatr = $(this).attr("attr");
        var passName = $.trim($(".phone").val());

        if (!passName) {
            alert("请输入手机号！！！");
            return false;
        }

        if (!(/^1(3|4|5|7|8)\d{9}$/.test(passName))) {
            alert("手机号格式不正确！！！");
            return false;
        }
        if (getatr == 'true') {
            $.ajax({
                type: "post",
                url: "<%=basePath%>author/authCode",
                data: {"phoneNum": passName},
                success: function (msg) {
                    if ("0000" == msg.code) {
                    } else {
                        alert(msg.message);
                    }
                }
            });
            $(this).attr("attr", 'false')
            loadtime("#sendyzm");
        } else {
            return false;
        }
    });

    function loadtime(ele) {
        var $id = $(ele)
        var secd = 60;
        $id.text(secd + "s");
        var timer = setInterval(function () {
            if (secd == 0) {
                secd = 0;
                clearInterval(timer);
                $id.attr("attr", 'true').text("发送验证码");
            } else {
                secd--;
                $id.text(secd + "s");
            }
        }, 1000)
    }

</script>