var imgUp=(function () {
    var images=null;
    var index='';//创建盒子的id
	var delParent;
	var defaults = {
		fileType : ["jpg","png","bmp","jpeg","gif"],   // 上传文件的类型
		fileSize : 1024 * 1024 * 5              // 上传文件的大小 5M
	};
    //提示框
    var diaglog = function(msg) {
        FK.plugin.dialog({
            className: "layerWap layerB",
            content: msg,
            title: "",
            confirmBtn: true,
            confirmTip: "确定"
        })
    };
    //上传图片转换成url链接
	function priceUrl (element) {
        var imgs=''
        $.ajax({
            url:'/upload/uploadImage.do',
            type: "post",
            data: new FormData($('#'+element+'')[0]),
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            beforeSend:function(){
            	//$('#loading').css('display','block');
			},
            success:function(data){
            	var data=JSON.parse(data);
                if(data.code=='0000'){
                    imgs=data.filePath;

                    var img=localStorage.getItem('imgs');
                    if(img){
                        images=img.split(",");
                    }else{
                        images=new Array;
                    }
                    images.push(imgs);
                    //alert(images)
                    localStorage.setItem('imgs',images);
                    $('#loading').css('display','none');
                }else{
                    diaglog('图片上传失败,请重新上传')
                }
            }
        })
		return imgs
    };
	/*点击图片的文本框*/
    var subImg=function (even,element,num) {
    	var iurl='';
        var idFile = even.attr("id");
        var file = document.getElementById(idFile);
        var imgContainer = even.parents(".z_photo"); //存放图片的父亲元素
        var fileList = file.files; //获取的图片文件
        var len=fileList.length;
        if(len>1){
            diaglog('请选择一张图片!');
            return false;
        }
        var input = $(this).parent();//文本框的父亲元素
        var imgArr = [];
        //遍历得到的图片文件
        var numUp = imgContainer.find(".up-section").length;
        var totalNum = numUp + fileList.length;  //总的数量
        if(fileList.length > num || totalNum > num ){
            diaglog("上传图片数目不可以超过"+num+"个，请重新选择");
        }
        else if(numUp < num){
            fileList = validateUp(fileList,element).arrFiles;
            iurl=validateUp(fileList,element).imgUrl;
            // for(var i = 0;i<fileList.length;i++){
            //     var imgUrl = window.URL.createObjectURL(fileList[i]);
            //     imgArr.push(imgUrl);
            //     var $section = $("<section class='up-section fl loading' id="+numUp+">");
            //     imgContainer.append($section);
            //     var $span = $("<span class='up-span'>");
            //     $span.appendTo($section);
            //     var $img0 = $("<img class='close-upimg'>").on("click",function(event){
            //         event.preventDefault();
            //         event.stopPropagation();
            //         $(".mask-content").show();
            //         delParent = $(this).parent();
            //         index=$(this).parent().index();
            //     });
            //     $img0.attr("src","http://www.fkhongdan.com/images/event/a7.png").appendTo($section);
            //     var $img = $("<img class='up-img up-opcity'>");
            //     $img.attr("src",imgArr[i]);
            //     $img.appendTo($section);
            //     var $p = $("<p class='img-name-p'>");
            //     $p.html(fileList[i].name).appendTo($section);
            //     var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
            //     $input.appendTo($section);
            //     var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
            //     $input2.appendTo($section);
            // }
        }
        setTimeout(function(){
            $(".up-section").removeClass("loading");
            $(".up-img").removeClass("up-opcity");
        },450);
        numUp = imgContainer.find(".up-section").length;
        if(numUp >= num){
            even.parent().parent('.z_file').hide();
        }
        return iurl;
    }




		
	function validateUp(files,element){
			var arrFiles = [];//替换的文件数组
			var imgUrl='';
			for(var i = 0, file; file = files[i]; i++){
				//获取文件上传的后缀名
				var newStr = file.name.split("").reverse().join("");
				if(newStr.split(".")[0] != null){
						var type = newStr.split(".")[0].split("").reverse().join("");
						if(jQuery.inArray(type, defaults.fileType) > -1){
							// 类型符合，可以上传
							if (file.size >= defaults.fileSize) {
								// alert(file.size);
                                //diaglog('您这个"'+ file.name +'"文件大小过大');
                                diaglog('图片大小超过5M啦，请裁剪后上传');
							} else {
								// 在这里需要判断当前所有文件中
								arrFiles.push(file);
                                imgUrl=priceUrl(element);
							}
						}else{
                            diaglog('您这个"'+ file.name +'"上传类型不符合');
						}
					}else{
                    diaglog('您这个"'+ file.name +'"没有类型, 无法识别');
					}

			}
			$(".z_photo").delegate(".close-upimg","click",function(){
				$(".works-mask").show();
				delParent = $(this).parent();
			});

			$(".wsdel-ok").click(function(){
				$(".mask-content").hide();
				var numUp = delParent.siblings().length;
				if(numUp < 10){
					delParent.siblings(".z_file").show();
				}
				delParent.remove();
				var scimg=localStorage.getItem('imgs');
				var zhimg=scimg.split(',');
				var len=zhimg.length-1;
				var schimg=zhimg.splice(len-index,1);
				localStorage.setItem('imgs',zhimg)
			});

			$(".wsdel-no").click(function(){
				$(".mask-content").hide();
			});
			return {
                arrFiles:arrFiles,
                imgUrl:imgUrl
			};
		};

	return{
        subImg:subImg
	}
})();
