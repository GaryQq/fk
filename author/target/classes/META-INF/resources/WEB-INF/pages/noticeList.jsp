<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<head>
    <meta charset="UTF-8">
    <title>
        已发文章
    </title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="http://www.fengkuang.cn/css/reset.css">
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/notice.css">
    <script src="/js/page.js"></script>
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
            <li class="on">
                <em class="five"></em>
                <span><a href="/article/toNoticeList">官方公告</a></span>
            </li>
        </ul>
    </div>
    <div class="right">
        <h3>全部公告</h3>
        <ul>
            <li>
                <span><a href="/article/toNoticeDetail">疯狂号自律组织公约</a></span>
                <em>2019.01.25 09:30</em>
            </li>

        </ul>
        <div class="page">
            <span id="page">
                <a href="javascript:;" onclick="page_new.jumpPage(1)">首页</a>
                <a href="javascript:;" onclick="page_new.prevPage(3)">上一页</a>
                <a href="javascript:;" class="cur">1</a>
                <a href="javascript:;" onclick="page_new.nextPage(5)">下一页</a>
                <a href="javascript:;" onclick="page_new.jumpPage(4)">尾页</a>
            </span>
            转到<input id="pageNums" name="" type="text" onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');">页
            <a href="javascript:void(0)" id="pagestart">开始</a>

        </div>
    </div>
</div>
<jsp:include page="common/footer.jsp"/>
</body>
</html>

<script type="text/javascript">

</script>