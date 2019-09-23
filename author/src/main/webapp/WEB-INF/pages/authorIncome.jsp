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
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/income.css">
    <script src="/js/page.js"></script>
</head>


<body style="background: #F8F8F8;">
<jsp:include page="common/header.jsp"/>
<div class="content layout">
    <div class="left">
        <ul>
            <li>
                <em class="one"></em>
                <span><a href="/author/myHome">我的主页</a></span>
            </li>
            <li class="on">
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
                <select id="articleMonth">
                    <option value="全部文章">全部文章</option>
                </select>
            </h3>
            <div>
                <em>收入:</em>
                <span id="totalIncome">0元</span>
            </div>
        </div>
        <div class="tittle">
            <span>文章标题</span>
            <span>收入（元）</span>
            <span>收入时间</span>
        </div>
        <ul id="articleList">
        </ul>
        <div class="noData" style="display: none">
            还没有收入~
        </div>
        <div class="page">
            <span id="page">
                <a href="javascript:;" onclick="page_new.jumpPage(1)">首页</a>
                <a href="javascript:;" onclick="page_new.prevPage(3)">上一页</a>
                <a href="javascript:;" onclick="page_new.jumpPage(1)">1</a>
                <a href="javascript:;" onclick="page_new.jumpPage(2)">2</a>
                <a href="javascript:;" onclick="page_new.jumpPage(3)">3</a>
                <a href="javascript:;" class="cur">4</a>
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

<style type="text/css">
    #articleMonth {
        margin-top: 22px;
        border: none;
        width: 110px;
        line-height: 30px;
        height: 30px;
        font-size: 20px;
    }
</style>

<script type="text/javascript">

    var odata = {"pageNum": 1, "pageSize": 15, "month": $("#articleMonth").val()}
    var page_new = window["page_new"] = pageCreat.page({
        pageObj: "page_new",
        pageId: "#page",
        activeName: "cur",
        pageNow: "1",
        ajaxUrl: "<%=basePath%>author/income",
        data: odata,
        insCallback: function (msg) {
            if (msg.code == "0000") {
                var pageNow = msg.data.pageNum;
                page_new.options.lastPage = msg.data.pages;
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
                            "                <em>" + dataArray[i].DEDUCT_MONEY + "</em>\n" +
                            "                <em>" + dataArray[i].MODIFY_TIME + "</em>\n" +
                            "            </li>";
                    }
                    $("#articleList").empty().append(appendStr);

                    if (page_new.options.lastPage !== 0) {
                        if (page_new.options.lastPage <= page_new.options.pageSpace) {
                            page_new.options.pageSpace = page_new.options.lastPage;
                        } else {
                            page_new.options.pageSpace = page_new.tempPageSpace;
                        }
                        page_new.creatPageInfo(pageNow, page_new.options.lastPage);
                    } else {
                        $(page_new.pageId).html("");
                    }
                }
            } else {
                alert(msg.message);
            }
        }
    });

    page_new.loadRecordList(1);

    $("#pagestart").click(function () {
        var vals = $("#pageNums").val();
        page_new.jumpPage(vals);
    });

    $("#articleMonth").change(function () {
        odata = {"pageNum": 1, "pageSize": 15, "month": $("#articleMonth").val()}
        page_new.loadRecordList(1, odata);
        getTotalIncome();
    });

    $(function () {
        getTotalIncome();

        $(function () {
            $.ajax({
                datatype: "json",
                type: "post",
                url: "<%=basePath%>article/articleMonthes",
                data: {"status": "1"},
                success: function (msg) {
                    if (msg.code == '0000') {
                        var data = msg.data;
                        for (var i = 0; i < data.length; i++) {
                            $("#articleMonth").append("<option value=" + data[i] + ">" + data[i] + "</optinon>")
                        }
                    }
                }
            });
        });
    });

    var getTotalIncome = function () {
        $.ajax({
            datatype: "json",
            type: "post",
            url: "<%=basePath%>author/totalIncome",
            data: {
                "month": $("#articleMonth").val()
            },
            success: function (msg) {
                if (msg.code == "0000") {
                    $("#totalIncome").text(msg.data + "元");
                } else {
                    alert(msg.message);
                }
            }
        })
    }

</script>