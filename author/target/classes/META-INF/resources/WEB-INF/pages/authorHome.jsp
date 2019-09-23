<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<head>
    <meta charset="UTF-8">
    <title>
        我的主页
    </title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="http://www.fengkuang.cn/css/reset.css">
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/homepage.css">
</head>


<body style="background:#F8F8F8;">
<jsp:include page="common/header.jsp"/>
<div class="content layout">
    <div class="left">
        <ul>
            <li class="on">
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
        <div class="top">
            <div class="cont"><span>
                    <em id="postCount">0</em>
                    <b>发表数</b>
                </span><span>
                    <em id="readCount">0</em>
                    <b>累计阅读量</b>
                </span><span>
                    <em id="orderCount">0</em>
                    <b>累计订阅量</b>
                </span>
            </div>
            <div class="news">
                <p id="toNoticeList" style="cursor: pointer">
                    <em>公告</em>
                    <span>
                        <b>更多</b>
                        <img src="http://www.fengkuang.cn/fbht/images/jiantou.png" alt="">
                    </span>
                </p>
                <div id="toNoticeDetail" style="cursor: pointer">
                    <span>疯狂号自律组织公约</span>
                    <em>
                        <b>2019-01-25</b>
                        <img src="http://www.fengkuang.cn/fbht/images/jiantou.png" alt="">
                    </em>
                </div>
            </div>
        </div>
        <div class="publish">
            <h3>最新文章</h3>
            <div style="cursor:pointer;" id="toCreateArticle">发表</div>
        </div>
        <div class="tittle">
            <span>文章标题</span>
            <span>阅读量</span>
            <span>收入（元）</span>
            <span>发表时间</span>
        </div>
        <ul id="articleList">

        </ul>
        <div class="noData" style="display: none;color:#333;">
            还没发过文章，快去发一篇~
        </div>
    </div>
</div>
<jsp:include page="common/footer.jsp"/>
</body>
</html>


<script type="text/javascript">

    $(function () {
        $.ajax({
            datatype: "json",
            type: "post",
            url: "<%=basePath%>article/list",
            data: {},
            success: function (msg) {
                if (msg.code == "0000") {
                    var dataArray = msg.data.list;
                    var length = msg.data.size;
                    if (length == 0) {
                        $("#articleList").hide();
                        $(".noData").show();
                    } else {
                        var appendStr = "";
                        for (var i = 0; i < length; i++) {
                            appendStr += "<li>\n" +
                                "                <em><a href='<%=basePath%>article/" + dataArray[i].ID + "/detail'>" + dataArray[i].TITLE + "</a></em>\n" +
                                "                <em>" + dataArray[i].READ_NUMBER_REAL + "</em>\n" +
                                "                <em>" + dataArray[i].PAY_AMOUNT + "</em>\n" +
                                "                <em>" + dataArray[i].CREATE_TIME + "</em>\n" +
                                "            </li>"
                        }
                        $("#articleList").append(appendStr);
                    }
                } else {
                    alert(msg.message);
                }
            }
        });

        $.ajax({
            datatype: "json",
            type: "post",
            url: "<%=basePath%>author/summary",
            data: {},
            success: function (msg) {
                if (msg.code == '0000') {
                    var data = msg.data;
                    $("#postCount").text(data.INFO_COUNT);
                    $("#readCount").text(data.READ_COUNT);
                    $("#orderCount").text(data.ORDER_COUNT);
                }
            }
        });
    });

    $("#toCreateArticle").click(function () {
        $(location).attr("href", "<%=basePath%>article/edit");
    });

    $("#toNoticeList").click(function () {
        $(location).attr("href", "<%=basePath%>article/toNoticeList");
    });

    $("#toNoticeDetail").click(function () {
        $(location).attr("href", "<%=basePath%>article/toNoticeDetail");
    });
</script>