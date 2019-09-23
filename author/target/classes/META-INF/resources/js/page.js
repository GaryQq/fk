var pageCreat = pageCreat || {};
//分页
function Page(opt){
    this.options = $.extend({
        pageObj:"page",
        pageId:"#page",
        activeName:"cur",
        data:{},
        pageSpace:5,
        pageNow:1,
        lastPage:0,
        ajaxUrl: "",
        insCallback: function(){}
    },opt||{});
    this.tempPageSpace = this.options.pageSpace;
    this.sendUrl = this.options.ajaxUrl;
};
Page.prototype = {
    creatPageInfo: function(pageNow,lastPage){
        var op = this.options;
        var pageHtml = "";
        var num = Math.ceil(op.pageSpace/2);
        pageHtml += '<a href="javascript:void(0);" onclick="'+op.pageObj+'.jumpPage(1)">首页</a>';
        pageHtml += '<a href="javascript:void(0);" onclick="'+op.pageObj+'.prevPage('+(pageNow-1)+')">上一页</a>';

        if((pageNow - num) <= 1){
            for(var i=1; i<= op.pageSpace; i++){
                if(i === pageNow){
                    pageHtml += "<a href='javascript:void(0);' class='"+ op.activeName +"'>" + i + "</a>";
                }
                else{
                    pageHtml += "<a href='javascript:void(0);' onclick='"+ op.pageObj +".jumpPage(" + i + ")'>" + i + "</a>";
                }
            }
        }
        else if((lastPage-pageNow) <= num){
            for(var i=lastPage-op.pageSpace+1; i<= lastPage; i++){
                if(i === pageNow){
                    pageHtml += "<a href='javascript:void(0);' class='"+ op.activeName +"'>" + i + "</a>";
                }
                else{
                    pageHtml += "<a href='javascript:void(0);' onclick='"+ op.pageObj +".jumpPage(" + i + ")'>" + i + "</a>";
                }
            }
        }
        else{
            for(var i=1; i<= op.pageSpace; i++){
                if(i === num){
                    pageHtml += "<a href='javascript:void(0);' class='"+ op.activeName +"'>" + (pageNow - num + i) + "</a>";
                }
                else{
                    pageHtml += "<a href='javascript:void(0);' onclick='"+ op.pageObj +".jumpPage(" + (pageNow - num + i) + ")'>" + (pageNow - num + i) + "</a>";
                }
            }
        }
        pageHtml += '<a href="javascript:void(0);" onclick="'+op.pageObj+'.nextPage('+(pageNow+1)+')">下一页</a>';
        pageHtml += '<a href="javascript:void(0);" onclick="'+op.pageObj+'.jumpPage('+lastPage+')">尾页</a>';
        $(op.pageId).html(pageHtml);
    },

    loadRecordList: function(pages,idata){
        var _this = this;
        var op = this.options;
        if(idata){
            op.data = idata;
        }
        _this.curPage = pages;
        op.data.pageNum = pages;
        $.ajax({
            url:op.ajaxUrl,
            type:"post",
            dataType:"json",
            data:op.data,
            success:function(msg){
                var size = msg.data.size;
                if (size == 0) {
                    $(".page").hide();
                }
                op.insCallback(msg);
            }

        })
    },
    nextPage: function(page){
        var op = this.options;
        if(page > op.lastPage){
            return false;
        }
        this.loadRecordList(page);
    },
    prevPage: function(page){
        if(page < 1){
            return false;
        }
        this.loadRecordList(page);
    },
    jumpPage: function(page){
        var op = this.options;
        if(page > op.lastPage || page < 1){
            return false;
        }
        this.loadRecordList(page);
    }
}
pageCreat.page = function(option){
    return new Page(option);
}