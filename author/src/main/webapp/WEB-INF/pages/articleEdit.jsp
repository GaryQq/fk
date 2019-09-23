<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<head>
    <meta charset="UTF-8">
    <title>
        发表文章
    </title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="http://www.fengkuang.cn/css/reset.css">
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/article.css">
    <!-- 配置文件 -->
    <script type="text/javascript" src="/js/UEditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="/js/UEditor/ueditor.all.js"></script>
</head>


<body style="background:#F8F8F8;">
<jsp:include page="common/header.jsp"/>
<div class="content layout">
    <div class="left">
        <ul>
            <li>
                <em class="one"></em>
                <span><a href="/author/myHome">我的主页</a></span>
            </li>
            <li>
                <em class="two"></em>
                <span><a href="/author/toIncome">我的收入</a></span>
            </li>
            <li>
                <em class="three"></em>
                <span><a href="/article/toList">已发文章</a></span>
            </li>
            <li>
                <em class="four"></em>
                <span><a href="/article/toDraftList">已存草稿</a></span>
            </li>
            <li>
                <em class="five"></em>
                <span><a href="/article/toNoticeList">官方公告</a></span>
            </li>
        </ul>
    </div>
    <div class="right">
        <input type="hidden" id="editFlag" value="${editFlag}">
        <input type="hidden" id="channelId" value="${article.channelId}">
        <input type="hidden" id="articleId" value="${article.id}">
        <input type="hidden" id="postId" value="${article.postId}">
        <input type="hidden" id="imageUrl" value="${article.imageUrl}">

        <h3>
            发表文章
        </h3>
        <ul style="position:relative;height: 880px;">
            <li>
                <span>频道选择</span>
                <em>
                    <select id="channelSelect">
                        <option value="0">频道选择</option>
                    </select>
                </em>
            </li>
            <li>
                <span>收费方式</span>
                <em>
                    <select id="payType">
                        <option value="1" <c:if test="${article.payType == 1}">selected</c:if>>收费</option>
                        <option value="0" <c:if test="${article.payType == 0}">selected</c:if>>免费</option>
                    </select>
                </em>
            </li>
            <li>
                <input type="text" placeholder="请输入标题（5-25字）" id="title" value="${article.title}">
                <p id="titleWordCount">0/25</p>
            </li>
            <script id="container" name="content" type="text/plain"></script>
            <div id="saveDraftDiv"
                 style='display:none;width:167px;height:30px;margin:-83.5px;color:#fff;z-index:9999;bottom:120px;left:50%;position:absolute;font-size:13px;line-height:30px;text-align: center;background-image:url("http://info.fengkuang.cn/images/2019/1/22/20191221548151249818_65.png");background-size:cover;'>
                30秒自动保存草稿
            </div>

            <li>
                <span>封面图</span>

                    <img src="${article.imageUrl}" id="upImageUrl" style="position: absolute;left: 60px;top: 20px;">
                    <input type="file" name="file" id="imageUpload" accept="image/png, image/jpeg, image/jpg, image/gif" style="position: absolute;left: 230px;top: 20px;width: 88px;">

            </li>
        </ul>
        <div style="margin-top:25px;">
            <div class="submit" id="submitInfo" flag="0" style="cursor: pointer">
                发表
            </div>
            <div class="draft" onclick="saveDraft(0)" style="cursor: pointer">
                保存草稿
            </div>
        </div>

    </div>
</div>
<jsp:include page="common/footer.jsp"/>
</body>
</html>

<style type="text/css">
    .right li select {
        position: absolute;
        top: 0;
        font-size: 14px;
        line-height: 36px;
        color: #1F2D3D;
        border: 1px solid #20A0FF;
        width: 100%;
        height: 100%;
    }

    .submit {
        display: inline-block;
        vertical-align: middle;
        margin-left: 40px;
        width: 175px;
        height: 40px;
        line-height: 40px;
        background: #D93635;
        color: #FFFFFF;
        font-size: 18px;
        text-align: center;
    }

    .draft {
        display: inline-block;
        vertical-align: middle;
        margin-left: 40px;
        width: 175px;
        height: 38px;
        line-height: 38px;
        font-size: 18px;
        text-align: center;
        border: 1px solid #999999;
        background: #fff;
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
    var submitFlag = 0;
    var lastChangeTime = 0;
    var updateTime = 0;
    var updateLastChangeTime = function () {
        lastChangeTime = new Date().getTime();
        updateTime++;
    }

    $("#payType, #channelSelect, #title").change(function () {
        updateLastChangeTime();
    });

    $("#title").keyup(function () {
        var title = $("#title").val();
        var str = $("#titleWordCount").text();

        var strTemp = title.length + "/25";
        if (str != strTemp) {
            $("#titleWordCount").text(strTemp);
        }

        if (title.length > 25) {
            $("#titleWordCount").css("color", "red");
        } else {
            $("#titleWordCount").css("color", "#999999");
        }
    });

    var ue = UE.getEditor('container', {
//        toolbars: [[
//            'fullscreen', 'source', '|', 'undo', 'redo', '|',
//            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|',
//            'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
//            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
//            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
//            'directionalityltr', 'directionalityrtl', 'indent', '|',
//            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
//            'touppercase', 'tolowercase', '|',
//            'link', 'unlink', 'anchor', '|',
//            'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
//            'simpleupload', 'insertimage', 'emotion', 'scrawl', 'insertframe', 'template', 'background', '|',
//            'horizontal', 'spechars', 'snapscreen', 'wordimage', '|',
//            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
//            'print', 'preview', 'searchreplace', 'drafts', 'help'
//        ]],
        autoHeightEnabled: false,
        autoFloatEnabled: true,
        initialFrameWidth: '100%',
        initialFrameHeight: 400,
        enableAutoSave: false
    });


    ue.ready(function () {
        ue.setContent('${article.content}');
    });

    ue.addListener("contentChange", function () {
        updateLastChangeTime();
    });

    /**
     * 自定义上传图片
     * @type {UE.Editor.getActionUrl}
     * @private
     */
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function (action) {
        if (action == 'uploadimage' || action == 'uploadscrawl') {
            return '<%=basePath%>article/uploadImage';//此处写自定义的图片上传路径
        }/* else if (action == 'uploadvideo') {
            return 'http://a.b.com/video.php';
        } */ else {
            return this._bkGetActionUrl.call(this, action);
        }
    }


    $(function () {
        $.ajax({
            dataType: "jsonp",
            jsonp: "callback",
            type: "post",
            url: "http://cpapi.fengkuang.cn/api/mobileClientApi.action?function=infoChannelTree",
            success: function (data) {
                var code = data.code;
                if ('0000' == code) {
                    var channelArray = data.data;
                    var opStr = "";
                    for (var i = 0; i < channelArray.length; i++) {
                        var channel = channelArray[i];
                        var id = channel.ID;
                        var name = channel.NAME;
                        if (id == $("#channelId").val()) {
                            opStr += "<option value='" + id + "' selected>" + name + "</option>"
                        } else {
                            opStr += "<option value='" + id + "'>" + name + "</option>"
                        }
                    }
                    $("#channelSelect").append(opStr);
                } else {
                    alert("请求频道列表异常！！！");
                    return false;
                }
            }
        });

        // 如果是修改已经发布过的文章，隐藏保存草稿按钮、停止自动保存草稿功能
        var editFlag = $("#editFlag").val();
        if (1 == editFlag) {
            $(".draft").hide();
        } else {
            // 定时器自动保存草稿
            getchange({
                type: "saveDraft",
                callback: function () {
                    if (updateTime > 1) {
                        var timeLeft = new Date().getTime() - lastChangeTime;
                        // 超过50秒未改动，不在保存草稿
                        if (timeLeft < 50000) {
                            var result = saveDraft(1);
                            if (result) {
                                // 超过5秒没修改，弹窗提示保存草稿成功
                                if (timeLeft > 5000) {
                                    $("#saveDraftDiv").fadeIn(2000, function () {
                                        $("#saveDraftDiv").fadeOut(2000)
                                    });
                                }
                            }
                        }
                    }
                }
            })
        }

        var title = $("#title").val();
        var str = title.length + "/25";
        $("#titleWordCount").text(str);

        var imageUrl = $("#imageUrl").val();
        if (!imageUrl) {
            $("#upImageUrl").attr("src","http://www.fengkuang.cn/fbht/images/aterimg.png");
        }
    });

    var checkParams = function () {
        var title = $.trim($("#title").val());
        var channelId = $("#channelSelect").val();
        var wordCount = ue.getContentLength(true);
        var imageUrl = $.trim($("#upImageUrl").attr("src"));

        if (channelId == 0) {
            alert("请选择频道！！！");
            return false;
        } else if (!title) {
            alert("请输入文章标题！！！")
            $("#title").focus();
            return false;
        } else if (title.length < 5) {
            alert("标题最少输入5个字！！！");
            $("#title").focus();
            return false;
        } else if (title.length > 25) {
            alert("标题最多输入25个字！！！");
            $("#title").focus();
            return false;
        } else if (wordCount < 50 || wordCount > 10000) {
            alert("文章内容长度必须在50-10000之间！！！");
            ue.focus();
            return false;
        } else if (!imageUrl || imageUrl == 'http://www.fengkuang.cn/fbht/images/aterimg.png') {
            alert("请上传封面图！！！");
            return false;
        }
        return true;
    };

    $("#submitInfo").click(function () {
        var submitFlag = $(this).attr("flag");
        if(submitFlag=="0"){
            $(this).attr("flag","1");
            var flag = checkParams();
            if (flag) {
                var title = $.trim($("#title").val());
                var content = ue.getContent();
                var channelId = $("#channelSelect").val();
                var payType = $("#payType").val();
                var wordCount = ue.getContentLength(true);
                var postId = $("#postId").val();
                var articleId = $("#articleId").val();
                var imageUrl = $.trim($("#upImageUrl").attr("src"));

                $.ajax({
                    datatype: "json",
                    type: "post",
                    url: "<%=basePath%>article/insert",
                    data: {
                        "title": title,
                        "payType": payType,
                        "channelId": channelId,
                        "content": content,
                        "wordCount": wordCount,
                        "postId": postId,
                        "id": articleId,
                        "status": 1,
                        "imageUrl": imageUrl
                    },
                    success: function (msg) {
                        if (msg.code == "0000") {
                            alert("发布成功！！！");
                            $("#submitInfo").attr("flag","0");
                            $(location).attr("href", "<%=basePath%>article/toList");
                        } else {
                            $("#submitInfo").attr("flag","0");
                            alert(msg.message);
                        }
                    }
                })
            }
        }else{
            return false;
        }
    });

    var timer = null;
    var getchange = function (opt) {
        var option = $.extend({
            type: "",//事件类型
            callback: function () {
            }
        }, opt || {});
        if (option.type == "clear") {
            clearInterval(timer);
        } else {
            timer = setInterval(function () {
                option.callback()
            }, 30000)
        }
    }

    var saveDraft = function (auto) {
        var datar = {msg: ""}
        var title = $.trim($("#title").val());
        var content = ue.getContent();
        var channelId = $("#channelSelect").val();
        var payType = $("#payType").val();
        var wordCount = ue.getContentLength(true);
        var postId = $("#postId").val();
        var articleId = $("#articleId").val();
        var imageUrl = $.trim($("#upImageUrl").attr("src"));

        var flag = false;
        if (auto == 0) {
            // 手动保存草稿
            flag = checkParams();
        } else if (channelId != 0 && title && title.length <= 25 && wordCount > 50 && wordCount < 10000) {
            // 自动保存
            flag = true;
        }
        // 保存草稿
        if (flag) {
            $.ajax({
                datatype: "json",
                type: "post",
                url: "<%=basePath%>article/insert",
                async: false,
                data: {
                    "title": title,
                    "payType": payType,
                    "channelId": channelId,
                    "content": content,
                    "wordCount": wordCount,
                    "postId": postId,
                    "id": articleId,
                    "status": 0,
                    "imageUrl": imageUrl
                },
                success: function (msg) {
                    if (msg.code == "0000") {
                        var article = msg.data;
                        $("#articleId").val(article.id);
                        if (auto == 0) {
                            alert("保存成功！！！");
                        } else {
                            datar.msg = true;
                        }
                    } else {
                        if (auto == 0) {
                            alert(msg.message);
                        } else {
                            return false;
                        }
                    }
                }
            })
        }
        return datar.msg;
    }

    $("#imageUpload").change(function () {
        var fileObj = document.getElementById("imageUpload").files[0]; // js 获取文件对象
        if (typeof (fileObj) == "undefined" || fileObj.size <= 0) {
            return;
        }

        if (fileObj.size > 5 * 1024 * 1024) {
            $("#imageUpload").val("");
            alert("封面图不能大于5M！！！");
            return false;
        }
        var fileName = fileObj.name;
        var extension = fileName.substr(fileName.lastIndexOf(".") + 1);
        if (extension != 'png' && extension != 'jpg' && extension != 'jepg' && extension != 'gif') {
            alert("只能上传png、jpg、jepg、gif格式的图片！");
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
                }
            }
        })
    });
</script>