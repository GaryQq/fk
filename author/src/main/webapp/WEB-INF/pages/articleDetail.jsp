<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<head>
    <meta charset="UTF-8">
    <title>
        文章详情
    </title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="http://www.fengkuang.cn/css/reset.css">
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/articleDetails.css">
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
        <div class="publish">
            <h3>
                文章详情
            </h3>
            <div>
                <em id="editArticle" style="cursor: pointer">
                    <img src="http://www.fengkuang.cn/fbht/images/bianji.png" alt="">
                    <i>编辑</i>
                </em>
                <span id="delArticle" style="cursor: pointer">
                    <img src="http://www.fengkuang.cn/fbht/images/shanchu.png" alt="">
                    <i>删除</i>
                </span>
            </div>
        </div>

        <ul class="wzxq">
            <input type="hidden" id="postId" value="${article.postId}">
            <input type="hidden" id="articleId" value="${article.id}">
            <li>
                <h4>${article.title}</h4>
            </li>
            <li>
                <div>
                    <span>
                        ${article.author.authorName}
                        <fmt:formatDate value="${article.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </span>
                    <b>
                        <c:if test="${article.payType == 1}">
                            收费文章
                        </c:if>
                        <c:if test="${article.payType == 0}">
                            免费文章
                        </c:if>
                    </b>
                    <%--<i>英超</i>--%>
                </div>
            </li>
            <li>
            </li>
            <div class="contentDiv">
                ${article.content}
            </div>
        </ul>
    </div>
</div>
<jsp:include page="common/footer.jsp"/>
</body>
</html>

<style type="text/css">
    .right li select {
        position: absolute;
        top: 0;
        left: 10px;
        font-size: 14px;
        line-height: 36px;
        color: #1F2D3D;
    }

    .contentDiv {
        width: 100%;
        height: 550px;
        font-size: 16px;
        margin-top: 20px;
        line-height: 27px;
        color: #555555;
        text-indent: 3%;
        overflow-y: auto;
        padding: 10px;
    }
</style>


<script type="text/javascript">

    var postId = $("#postId").val();
    var id = $("#articleId").val();

    $("#editArticle").click(function () {
        $.ajax({
            datatype: "json",
            type: "post",
            url: "<%=basePath%>article/" + postId + "/buyCount",
            data: {},
            success: function (msg) {
                if (msg.code == "0000") {
                    $(location).attr("href", "<%=basePath%>article/" + postId + "/edit")
                } else {
                    alert(msg.message);
                }
            }
        });
    });

    $("#delArticle").click(function () {
        if (confirm("确定删除？")) {
            $.ajax({
                datatype: "json",
                type: "post",
                url: "<%=basePath%>article/" + postId + "/delete",
                data: {},
                success: function (msg) {
                    if (msg.code == "0000") {
                        alert("删除成功！！！");
                        $(location).attr("href", "<%=basePath%>article/toList");
                    } else {
                        alert(msg.message);
                    }
                }
            });
        }
    });


</script>