<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<head>
    <meta charset="UTF-8">
    <title>申请入驻</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="http://www.fengkuang.cn/css/reset.css">
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/sqrz.css">
</head>
<body>
<jsp:include page="common/header.jsp"/>
<div class="content layout">
    <form action="user/applyForAuthor" method="post">
        <input type="hidden" id="introHidden" value="${author.introduction}">
        <input type="hidden" id="authorStatus" value="${author.status}">
        <input type="hidden" id="userName" value="${author.userName}">

        <p class="name">
            <em>作者名称</em>
            <input type="text" name="" id="authorName" placeholder="请输入不超过10个字，请勿包含政治，色情，广告等违规信息"
                   value="${author.authorName}">
        </p>
        <p class="introduction">
            <em>作者简介</em>
            <textarea type="text" name="" id="introduction" placeholder="请输入不超过300个字，请勿包含政治，色情，广告等违规信息"></textarea>
        </p>
        <p class="photo">
            <em>作者头像</em>
            <img src="${author.imageUrl}" id="upImageUrl">
            <input type="file" name="file" id="imageUpload" accept="image/png, image/jpeg, image/jpg">
            <%--<span>上传图片</span>--%>
            
        </p>
        <p class="idNumber">
            <em>身份证号</em>
            <input type="text" name="" id="idCard" placeholder="请输入真实身份证号码" value="${author.idCard}">
        </p>
        <p class="idNumber">
            <em>联系邮箱</em>
            <input type="text" name="" id="email" placeholder="请填写有效邮箱" value="${author.email}">
        </p>
        <p class="phone">
            <em>手机号</em>
            <input type="text" name="" id="mobile" placeholder="请输入手机号" value="${author.mobile}">
            <i class="delClass" id="sendyzm" style="cursor: pointer" attr="true">发送验证码</i>
            <b class="delClass">验证码</b>
            <input type="text" id="authCode" placeholder="请输入验证码" class="yzm delClass">
        </p>

        <input type="button" id="submitBtn" class="submitBtn" value="提交" >
        <div id="topTip" style=" display: none;
    margin: 58px 0 0 91px;
    height: 40px;
    line-height: 40px;
    color: #D93635;
    font-size: 16px;
">您已提交申请，我们会尽快为您审核！</div>
    </form>

</div>
<jsp:include page="common/footer.jsp"/>
</body>
</html>
<style type="text/css">
    #submitBtn {
        margin: 58px 0 0 90px;
        width: 175px;
        height: 40px;
        line-height: 40px;
        background: #D93635;
        color: #FFFFFF;
        font-size: 18px;
        text-align: center;
    }

    #upImageUrl {
        display: inline-block;
        vertical-align: top;
        width: 130px;
        height: 130px;
        margin-left: 20px;
    }
</style>


<script type="text/javascript">

    $(function () {
        // userName 为空， 说明没有登录，直接申请
        if (!$("#userName").val()) {
            $("#upImageUrl").attr("src","http://www.fengkuang.cn/fbht/images/aterimg.png");
        } else {
            $(".delClass").hide();
            $("#topTip").hide();
            var intro = $("#introHidden").val();
            if (intro) {
                $("#introduction").val(intro);
            }

            var status = $("#authorStatus").val();
            var btnStr;
            if ('' != status && '-1' != status) {
                if (0 == status) {
                    $("#topTip").text("您已提交申请，我们会尽快为您审核！");
                    btnStr = "审核中";
                } else if (-2 == status) {
                    $("#topTip").text("您的账号已被封禁，请联系客服！");
                    btnStr = "已封禁"
                }
                $("#topTip").show();
                $("#authorName").attr("disabled", "disabled");
                $("#introduction").attr("disabled", "disabled");
                $("#idCard").attr("disabled", "disabled");
                $("#mobile").attr("disabled", "disabled");
                $("#email").attr("disabled", "disabled");
                $("#submitBtn").val(btnStr).attr("disabled", "disabled").css({
                    background: "rgba(217,54,53,1)",
                    opacity: "0.5",
                    margin: "58px 16px 0 90px",
                    float: "left"
                });
                $("#imageUpload").hide();
            } else {
                $("#upImageUrl").attr("src","http://www.fengkuang.cn/fbht/images/aterimg.png");
                $("#authorName").val("");
            }

        }

    });
    var idCardRegex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    var mobileRegex = /^[1][3,4,5,7,8][0-9]{9}$/;
    var emailRegex = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;
    var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");

    $("#submitBtn").click(function () {
        var authorName = $.trim($("#authorName").val());
        var introduction = $.trim($("#introduction").val());
        var imageUrl = $.trim($("#upImageUrl").attr("src"));
        var idCard = $.trim($("#idCard").val());
        var mobile = $.trim($("#mobile").val());
        var email = $.trim($("#email").val());
        var authCode = $.trim($("#authCode").val());

        if (!authorName) {
            alert("请输入作者名称！！！");
            return false;
        } else if (!introduction) {
            alert("请输入作者简介！！！");
            return false;
        } else if (!imageUrl || imageUrl == 'http://www.fengkuang.cn/fbht/images/aterimg.png') {
            alert("请上传头像！！！");
            return false;
        } else if (!idCard) {
            alert("请输入身份证号！！！");
            return false;
        } else if (!mobile) {
            alert("请输入手机号码！！！");
            return false;
        } else if (!email) {
            alert("请输入联系邮箱！！！");
            return false;
        }
        if (!$("#userName").val()) {
            if (!authCode) {
                alert("请输入验证码！！！");
                return false;
            }
            if (!(/^\d{6}$/.test(authCode))) {
                alert("验证码格式不正确！！！");
                return false;
            }

        }

        if(pattern.test(authorName)){
            alert("作者名称不能包含标点符号和特殊字符！！！");
            return false;
        }

        if (authorName.length > 10) {
            alert("请输入10个字以内名称！！！")
            return false;
        }

        if (introduction.length > 300) {
            alert("请输入300个字以内简介！！！")
            return false;
        }

        if (!idCardRegex.test(idCard)) {
            alert("身份证号码格式错误！！！");
            return false;
        }

        if (!mobileRegex.test(mobile)) {
            alert("手机号码格式错误！！！");
            return false;
        }

        if (!emailRegex.test(email)) {
            alert("联系邮箱格式错误！！！");
            return false;
        }



        // 参数验证通过，ajax调用接口
        $.ajax({
            datatype: "json",
            type: "post",
            url: "<%=basePath%>author/apply",
            data: {
                "userName": $("#userName").val(),
                "authorName": authorName,
                "introduction": introduction,
                "imageUrl": imageUrl,
                "idCard": idCard,
                "mobile": mobile,
                "email": email,
                "authCode": authCode,
                "source": 2
            },
            success: function (msg) {
                if (msg.code == "0000") {
                    alert("提交成功，请等待审核！！！");
                    $(location).attr("href", "<%=basePath%>author/dispatch")
                } else if (msg.code == "0013") {
                    if (confirm("您已通过审核，点击[确定]进入主页！！！")) {
                        $(location).attr("href", "<%=basePath%>author/dispatch")
                    }
                } else if (msg.code == "0012") {
                    if (confirm("您已提交过审核，点击[确定]查看审核状态！！！")) {
                        $(location).attr("href", "<%=basePath%>author/dispatch")
                    }
                } else {
                    alert(msg.message);
                }
            }
        });
    });

    $("#imageUpload").change(function () {
        var fileObj = document.getElementById("imageUpload").files[0]; // js 获取文件对象
        if (typeof (fileObj) == "undefined" || fileObj.size <= 0) {
            return;
        }

        if (fileObj.size > 512 * 1024) {
            $("#imageUpload").val("");
            alert("头像不能大于512KB！！！");
            return false;
        }
        var fileName = fileObj.name;
        var extension = fileName.substr(fileName.lastIndexOf(".") + 1);
        if (extension != 'png' && extension != 'jpg' && extension != 'jepg') {
            alert("只能上传png、jpg、jepg格式的图片！");
            return false;
        }

        var formFile = new FormData();
        formFile.append("upfile", fileObj); //加入文件对象   file为传递给后台的参数，可以模仿form中input的name值。

        //第一种  XMLHttpRequest 对象
        //var xhr = new XMLHttpRequest();
        //xhr.open("post", "/Admin/Ajax/VMKHandler.ashx", true);
        //xhr.onload = function () {
        //    alert("上传完成!");
        //};
        //xhr.send(formFile);

        //第二种 ajax 提交
        var data = formFile;
        $.ajax({
            url: "<%=basePath%>article/uploadImage",
            data: data,
            type: "post",
            dataType: "json",
            cache: false,//上传文件无需缓存
            processData: false,//用于对data参数进行序列化处理 这里必须false
            contentType: false, //必须
            success: function (msg) {
                if (msg.state == 'SUCCESS') {
                    $("#upImageUrl").attr("src", msg.url);
                    $("#upImageUrl").show();
                }
            },
        })
    });

    $("#sendyzm").click(function(){
        var getatr = $(this).attr("attr");
        var passName = $.trim($("#mobile").val());

        if (!passName) {
            alert("请输入手机号！！！");
            return false;
        }

        if (!(/^1(3|4|5|7|8)\d{9}$/.test(passName))) {
            alert("手机号格式不正确！！！");
            return false;
        }
        if(getatr=='true'){
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
            $(this).attr("attr",'false')
            loadtime("#sendyzm");
        }else{
            return false;
        }
    });

    function loadtime(ele){
        var $id = $(ele)
        var secd = 60;
        $id.text(secd + "s");
        var timer = setInterval(function(){
            if(secd==0){
                secd = 0;
                clearInterval(timer);
                $id.attr("attr",'true').text("发送验证码");
            }else{
                secd--;
                $id.text(secd + "s");
            }
        },1000)
    }

    $("#mobile").keydown(function(){
        $("#sendyzm").addClass("active");
    });

</script>