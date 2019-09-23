<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <script src="http://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
</head>
<body>
<div class="header">
    <div class="tab layout">
        <div class="logo">
            <h3>疯狂号</h3>
            <span>全球体育作者联盟</span>
        </div>
        <c:if test="${!empty userLogin}">
            <div class="info" style="cursor: pointer">
                <img src="${userLogin.imageUrl}" alt="">
                <span>${userLogin.authorName}</span>
                <em id="showEm"></em>
                <span id="logout">退出登录</span>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>

<style type="text/css">
    .info #logout {
        cursor: pointer;
        position: absolute;
        bottom: -30px;
        width: 133px;
        height: 43px;
        background: url(http://www.fengkuang.cn/fbht/images/leaveout.png) no-repeat;
        background-size: 100% 100%;
        line-height: 43px;
        color: #666;
        right: 0px;
        text-align: center;
        margin: 0;
        display: none;
        z-index: 999;
    }
</style>

<script type="text/javascript">
    $(".header .info").click(function () {
        $("#logout").toggle();
    });

    $("#logout").click(function () {
        $.ajax({
            type: "post",
            url: "/author/logout",
            data: {},
            success: function (msg) {
                if ("0000" == msg.code) {
                    window.location.href = "/author/dispatch";
                }
            }
        });
    });
</script>
