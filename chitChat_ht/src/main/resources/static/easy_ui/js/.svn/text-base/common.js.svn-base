$(function() {
	window['Common'] = {};

	// 定义14个主题
	Common.themes = new Array("black", "bootstrap", "default", "gray",
			"metro", "metro-blue", "metro-gray", "metro-green", "metro-orange",
			"metro-red", "ui-cupertino", "ui-dark-hive", "ui-pepper-grinder",
			"ui-sunny");
	// 更换主题
	Common.switchTheme = function(theme) {
		var tmpTheme;
		if (theme != null) {
			tmpTheme = theme;
		} else {
			var rndNum = parseInt(14 * Math.random());
			tmpTheme = Common.themes[rndNum];
		}
		var linkHref = $("#skin").attr("href");
		var linkHrefSpit = linkHref.split("/");
		linkHrefSpit[linkHrefSpit.length - 2] = tmpTheme;
		var linkHref = '';
		for (var i = 1; i < linkHrefSpit.length; i++) {
			linkHref += '/' + linkHrefSpit[i]
		};

		$("#skin").attr("href", linkHref);
		console.info("switchTheme:" + linkHref);
		// 记录一下cookie,防止刷新就回到原来的css路径
		$.cookie("css_skin", tmpTheme, {
			path : "/"
		});
	};

	// 如果cookie不为空的时候就读取cookie的路径
	if ($.cookie("css_skin") != null) {
		var theme = $.cookie("css_skin");
		Common.switchTheme(theme);
	}

	// 显示错误日志
	Common.showMsg = function(title, msg) {
		$.messager.show({
			title : title,
			msg : msg,
			showType : 'slide',
			style : {
				right : '',
				top : '',
				bottom : -document.body.scrollTop
						- document.documentElement.scrollTop
			}
		});
	};

	// 判断两个输出框是否填写一致
	$.extend($.fn.validatebox.defaults.rules, {
		/* 必须和某个字段相等 */
		equalTo : {
			validator : function(value, param) {
				return $(param[0]).val() == value;
			},
			message : '字段不匹配'
		}
	});

});

/** 日期控件添加时间样式 */
function myformatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

function myparser(s) {
	if (!s)
		return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	var d = parseInt(ss[2], 10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
		return new Date(y, m - 1, d);
	} else {
		return new Date();
	}
}

function formatter(time) {
	var date = new Date(time);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	month = month < 10 ? '0' + month : month;
	day = day < 10 ? '0' + day : day;
	hour = hour < 10 ? '0' + hour : hour;
	minute = minute < 10 ? '0' + minute : minute;
	second = second < 10 ? '0' + second : second;
	return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":"
			+ second;
}