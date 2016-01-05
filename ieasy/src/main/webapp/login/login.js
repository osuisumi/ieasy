var loginClick = null;
var loginEnter = null;
$(function() {
	var cookieUser = "ieasy_user";
	$('#icheck input').iCheck({
		checkboxClass : 'icheckbox_minimal-blue'
	});
	initCookie();

	// 判断登陆的错误次数是否大于需要输入验证码的次数
	$.post("/ieasy/system/login/login_error.do", null, function(result) {
		if (result.status) {
			$("#valid_box").show();
		}
	}, 'json');

	// 点击验证码获取新的验证码
	$("#valid_img").click(function() {
		$(this).attr("src", "/ieasy/validCodeServlet?d=" + new Date().getTime());
	});

	// 回车登录
	var doc = $(document).bind("keydown", loginEnter = function(e) {
		if (e.keyCode == 13) {
			login();
		}
	});
	// 点击登陆
	var l = $("button[name=login]").bind("click", loginClick = function() {
		login();
	});

	// 登陆（一系列的验证）
	function login() {
		var account = $("input[name=account]").val();
		if (undefined == account || "" == account) {
			infoNotify("账号不能为空！", "warning");
			return;
		}
		// 如果验证码区域不为隐藏状态，则判断是否有输入验证码
		if ($("#valid_box").is(":hidden") == false) {
			if (undefined == $("input[name=validCode]").val()
					|| $("input[name=validCode]").val() == "") {
				infoNotify("验证码不能为空！", "warning");
				return;
			}
		}
		l.unbind("click");
		doc.unbind("keydown");
		submitNow();
	}

	// 提交数据
	function submitNow() {
		
		$("#tx_img").attr("src", "/ieasy/images/sys_img/loading3.gif");
		
		infoNotify("<span id='login_icon'><img src='/ieasy/login/loading1.gif'>正在验证...</span>", "want");
		
		var data = $.webapp.serializeObject("#login-form");
		$.post("/ieasy/system/login/login.do", data, function(result) {
			if (result.status) {
				infoNotify("<span id='login_icon'><img src='/ieasy/login/loading1.gif'>"+result.msg+"</span>", "success");
				ajaxRet(result);
				$("input[name=validCode]").val("");
			} else {
				doc.bind("keydown", loginEnter);
				l.bind("click", loginClick);
				// 如果登陆次数>=3则需要输入验证码
				if (undefined != result.obj && result.obj.v)
					$("#valid_box").show();
				
				infoNotify(result.msg, "error");
				$("#tx_img").attr("src", "/ieasy/login/profilepicture.jpg");
			}
		}, 'json').error(function() {
			l.bind("click", loginClick);
			doc.bind("keydown", loginEnter);
			infoNotify("请求登录发生错误，请与管理员联系！", "error");
			return;
		});
	}

	// 异步数据返回出来结果
	function ajaxRet(result) {
		if ($("#remember").is(":checked")) {
			setCookie(cookieUser, $("input[name=account]").val()+"[#]"+$("input[name=password]").val());
		} else {
			removeCookie(cookieUser);
		}
		loadImg("#tx_img", result.obj.tx);

		//window.location.replace("/ieasy/admin/index/adminIndex.do") ;
		//setTimeout("window.location.replace('/ieasy/admin/index/adminIndex.do')",500);
	}

	// 预加载图片
	function loadImg(id, imgPath) {
		if (undefined != imgPath && "" != imgPath) {
			imgPath = "/ieasy" + imgPath;
		}
		var temp_img = new Image();
		$(temp_img).bind('load', function() {
			$(id).attr("src", temp_img.src);
			setTimeout("window.location.replace('/ieasy/admin/index/adminIndex.do')",1500);
		});
		$(temp_img).bind('error', function() {
			//$(id).attr("alt", "图片加载失败").attr("src", "/ieasy/" + imgPath);
			$(id).attr("alt", "图片加载失败").attr("src", "/ieasy/login/profilepicture.jpg");
			setTimeout("window.location.replace('/ieasy/admin/index/adminIndex.do')",500);
		});
		temp_img.src = imgPath;
	}

	function infoNotify(msg, level) {
		var info = $(".not-registered");
		if ("want" == level) {
			info.html(msg).css({
				"background" : "#009933",
				"color" : "#fff"
			});
		} else if ("error" == level) {
			info.html(msg).css({
				"background" : "#B91D47",
				"color" : "#fff"
			});
		} else if ("success" == level) {
			info.html(msg).css({
				"background" : "#5CB811",
				"color" : "#fff"
			});
		} else if ("warning" == level) {
			info.html(msg).css({
				"background" : "#FF9933",
				"color" : "#fff"
			});
		}
	}

	function initCookie() {
		var u = $.cookie(cookieUser);
		if (undefined != u && "" != u) {
			var splits = u.split("[#]");
			$("input[name=account]").val(splits[0]);
			$("input[name=password]").val(splits[1]);
			$('#icheck input').iCheck('check');
		} else {
			$("input[name=account]").val("");
			$("input[name=password]").val("");
			$('#icheck input').iCheck('uncheck');
		}
	}
	function setCookie(name, value) {
		$.cookie(name, value, {
			expires : 30,
			path : '/'
		});
	}
	function removeCookie(name) {
		$.cookie(name, null, {
			expires : -1,
			path : '/'
		});
	}
});