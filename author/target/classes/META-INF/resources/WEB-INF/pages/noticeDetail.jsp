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
    <link rel="stylesheet" href="http://www.fengkuang.cn/fbht/css/noticeDetails.css">
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
        <div class="ggxq">
            <h3>
                公告详情
            </h3>
            <div>
                <em id="toNoticeList" style="cursor: pointer">
                    <i>返回全部</i>
                    <img src="http://www.fengkuang.cn/fbht/images/jiantou.png" alt="">
                </em>
            </div>
        </div>

        <ul class="ggxqnr">
            <li>
                <h4>疯狂号自律组织公约</h4>
            </li>
            <li>
                <div>
                    <span>疯狂号管理员 2019-01-25 09:30:00</span>
                </div>
            </li>
            <li>
                <p>第一章 总则</p>
                <p></p>
                <p>第一条</p>
                <p>为维护疯狂号良性、健康的创作环境，营造健康有序的网络空间，鼓励更多有价值的信息产生，疯狂号官方（以下简称“官方”）特制定本公约。</p>
                <p>第二条</p>
                <p>
                    疯狂号管理由官方和疯狂号自律组织（以下简称“自律组织”）共同完成。对于可明显识别的违法违规行为，由自律组织举报，交由官方处理；对存在较大争议，或涉及专业性较强的问题，官方和自律组织共同协商，由官方确定最终结果。</p>
                <p>第三条</p>
                <p>疯狂号自律组织成员应做到： 积极参与疯狂号管理事务，秉公办事，认真称职，为建立一个风清气正的网络空间贡献力量； 接受疯狂体育用户和官方监督，不得利用自律组织成员身份谋求不正当利益。</p>
                <p>第四条</p>
                <p>官方负责本制度的解释及实施。</p>
                <p></p>
                <p>第二章 自律组织</p>
                <p></p>
                <p>第五条</p>
                <p>自律组织成员在符合条件的主动报名者中产生。</p>
                <p>第六条</p>
                <p>自律组织成员申请条件：</p>
                <p>年满18周岁；</p>
                <p>已注册疯狂号；</p>
                <p>近30天内发布内容在10篇以上；</p>
                <p>疯狂号帐号无违规记录。</p>
                <p>第七条</p>
                <p>自律组织运作机制：</p>
                <p>
                    自律组织成员有权在阅读和观看疯狂号作者发布的图文、视频内容时，对涉嫌低俗、违背相关现行政策与法律法规、违反宪法确定的基本原则（参考以下第八、九、十、十一条规定）的内容及时截图举报，官方优先处理自律组织成员的举报；</p>
                <p>对于涉嫌违规的内容，系统从自律组织成员中随机选出一定数量，构成判定具体案例的评价委员会；</p>
                <p>评价委员会对涉嫌违规行为投票判定，若一方票数超过一半，完成判定；</p>
                <p>如果内容被判定严重违规，则会扣除帐号分值或者做封号处理。每个帐号拥有100分起始帐号分值，扣分可恢复。因「含有违法内容」被扣分的，分数不恢复。</p>
                <p>第八条</p>
                <p>被认定为低俗的内容种类包括但不限于：</p>
                <p>文字、图片、视频对性部位和性行为进行过度描述或展示；</p>
                <p>对性部位和性行为进行暗示或内涵表述：如展示形似性器官的物品，或形似性行为的动作；</p>
                <p>违背公序良俗的性观念：如性虐、恋物癖、捆绑等；</p>
                <p>低俗下流的艺术、声乐作品；</p>
                <p>文章含有露点、性诱导、性暗示图片，直接暴露和描写人体性部位；</p>
                <p>涉及古代后宫艳史、嫖妓野史、宫廷淫乱、帝王性癖好、猎奇秘闻类文章；</p>
                <p>含有性行为、性过程、性方式的文字描述</p>
                <p>表现或隐晦表现性行为，如偷拍野战、车震；</p>
                <p>过分裸露及含有不雅姿态的直播视频；</p>
                <p>单独截取的动物交配视频；</p>
                <p>语言低俗类视频包括改编低俗歌词、低俗二人转和民歌、语言粗俗下流等内容；</p>
                <p>低俗行为类包括美女捆绑、挠脚心、按摩敏感部位、模仿性动作等</p>
                <p>不良性癖好（性受虐癖、性施虐癖、恋物癖、捆绑等非正常性行为）。</p>
                <p>第九条</p>
                <p>被认定为违背相关现行政策与法律法规的内容种类包括但不限于：</p>
                <p>与现行国家极力倡导的大政方针唱反调；</p>
                <p>大肆抨击国家政要与行政单位；</p>
                <p>教唆、教授、组织他人进行违法乱纪的活动；</p>
                <p>其他有违现行法律法规的情况；</p>
                <p>含有境外媒体相关内容</p>
                <p>第十条</p>
                <p>被认定为违反宪法确定的基本原则的内容种类包括但不限于：</p>
                <p>危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</p>
                <p>损害国家荣誉和利益的；</p>
                <p>煽动民族仇恨、民族歧视，破坏民族团结的；</p>
                <p>破坏国家宗教政策，宣扬邪教和封建迷信的；</p>
                <p>散布谣言，扰乱社会秩序，破坏社会稳定的；</p>
                <p>散布淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；</p>
                <p>侮辱或者诽谤他人，侵害他人合法权益的；</p>
                <p>含有法律、行政法规禁止的其他内容的，如枪支弹药、管制刀具、毒品等买卖途径、制作方法；拆解展示的违法类文；</p>
                <p>含有境外媒体相关内容。</p>
                <p>第十一条</p>
                <p>其他涉嫌违反自律组织公约的内容包括但不限于以下几种：</p>
                <p>自媒体群媒体发布社会新闻，以及社会新闻的评论文章，如新闻爆料、如打砸抢烧、交通事故、火灾等突发事故、官司纠纷等；</p>
                <p>渲染社会负面现象，社会导向不好的文章。</p>
                <p>
                    存在自媒体八大乱象的行为：包括曲解政策，违背正确导向；无中生有，散布虚假信息；颠倒是非，歪曲党史国史；格调低俗，突破道德底线；惊悚诱导，标题党现象泛滥；抄袭盗图，版权意识淡薄；炫富享乐，宣扬扭曲价值观；题无禁区，挑战公序良俗等。</p>
                <p>第十二条</p>
                <p>自律组织成员退出机制：</p>
                <p>成员主动要求退出；</p>
                <p>成员出现以下任一情况即失去资格：</p>
                <p>利用自律组织成员身份谋求不正当利益；</p>
                <p>违反《疯狂号自律组织公约》等规章制度；</p>
                <p>妨碍疯狂号正常运营或不认同自律组织制度；</p>
                <p>疯狂号帐号未满足活跃度要求</p>
                <p>拥有或使用过1个以上的自律组织成员帐号。</p>
                <p>第十三条</p>
                <p>自律组织应在官方协助下，开展传播正能量活动。在节日、纪念日等时间，进行符合正旋律主题的宣传活动，弘扬中华民族传统文化，彰显社会主义核心价值观；</p>
                <p>官方将在推荐位和补贴上，予以自律组织成员一定倾斜。</p>
                <p></p>
                <p>第三章 附则</p>
                <p></p>
                <p>第十四条</p>
                <p>
                    官方可依照互联网发展的不同阶段，随着运营管理经验的丰富，出于创建一个风清气正网络空间的目的，不断完善本制度。一旦本协议的内容发生变动，疯狂号将在疯狂号操作后台运营规范栏公布修改后的协议内容，该公布行为视作疯狂号已通知用户修改内容，恕不另行通知。</p>
            </li>
        </ul>
    </div>
</div>
</div>
<jsp:include page="common/footer.jsp"/>
</body>
</html>

<script type="text/javascript">
    $("#toNoticeList").click(function () {
        $(location).attr("href", "<%=basePath%>article/toNoticeList");
    });
</script>