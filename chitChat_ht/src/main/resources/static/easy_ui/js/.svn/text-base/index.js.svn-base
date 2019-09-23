//在右边center区域打开菜单，新增tab
function addTab(text, url) {
	if ($("#tabs").tabs('exists', text)) {
		$('#tabs').tabs('select', text);
		var tab = $('#tabs').tabs('getSelected');
		/* tab.panel('refresh', url); */
		$('#tabs').tabs('update', {
			tab : tab,
			options : {
				content : createFrame(url)
			}
		});

	} else {
		$('#tabs').tabs('add', {
			title : text,
			closable : true,
			content : createFrame(url)
		});
	}
}
// 在右边center区域打开菜单，关闭tab
function closeTab(menu, type) {
	var curTabTitle = $(menu).data("tabTitle");
	var tabs = $("#tabs");

	if (type === "close") {
		tabs.tabs("close", curTabTitle);
		return;
	}

	var allTabs = tabs.tabs("tabs");
	var closeTabsTitle = [];

	$.each(allTabs, function() {
		var opt = $(this).panel("options");
		if (opt.closable && opt.title != curTabTitle && type === "Other") {
			closeTabsTitle.push(opt.title);
		} else if (opt.closable && type === "All") {
			closeTabsTitle.push(opt.title);
		}
	});

	for (var i = 0; i < closeTabsTitle.length; i++) {
		tabs.tabs("close", closeTabsTitle[i]);
	}
}

function createFrame(url) {
	var frame = '<iframe id="mainFrame" name="mainFrame" scrolling="auto" frameborder="0"  src="'
			+ url + '" style=\"width:100%;height:98.5%;\"></iframe>';
	return frame;
}

function Wraper() {
}
Wraper.prototype = {
	openTab : function(text, url) {
		addTab(text, url);
	},
	closeTab : function(text) {
		var tabs = $("#tabs");
		tabs.tabs("close", text);
	},
	closeAndReloadTab : function(closeText, reloadText) {
		var tabs = $("#tabs");
		tabs.tabs("close", closeText);
		var tab = tabs.tabs("getTab", reloadText);
		var url = $(tab.panel('options').content).attr('src');
		if (tab != null) {
			$('#tabs').tabs('update', {
				tab : tab,
				options : {
					content : createFrame(url)
				}
			});
		}
	}
};
window.framework = new Wraper();

$(function() {
	// 绑定tabs的右键菜单
	$("#tabs").tabs({
		onContextMenu : function(e, title) {
			e.preventDefault();
			$('#tabsMenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			}).data("tabTitle", title);
		}
	});

	// 实例化menu的onClick事件
	$("#tabsMenu").menu({
		onClick : function(item) {
			closeTab(this, item.name);
		}
	});

	// 菜单点击事件改变点击菜单样式
	$(".menu-ul").click(function() {
		var selectedItem = $(".menu-ul-li-selected");
		if (selectedItem != null && selectedItem.length > 0) {
			$.each(selectedItem, function(n, value) {
				$(value).removeClass("menu-ul-li-selected");
				$(value).removeClass("datagrid-row-selected");
				$(value).addClass("menu-ul-li");
			});
		}
		$(this).removeClass("menu-ul-li");
		$(this).addClass("menu-ul-li-selected");
		$(this).addClass("datagrid-row-selected");
	});
});
