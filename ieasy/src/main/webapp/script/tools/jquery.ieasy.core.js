
(function (window, $, undefined) {
	
	$.webapp = {}
	
	/**
	 * 跨浏览器获取视口大小
	 */
	$.webapp.getInner = function() {
		
		if (typeof window.innerWidth != 'undefined') {
			return {
				width : window.innerWidth,
				height : window.innerHeight
			}
		} else {
			return {
				width : document.documentElement.clientWidth,
				height : document.documentElement.clientHeight
			}
		}
	};
	
	/**
	 * 如果窗口大于浏览器宽度,返回浏览器宽度
	 * @returns
	 */
	$.webapp.getInnerDialogW = function(w) {
		if (typeof window.innerWidth != 'undefined') {
			return {
				width : (w>window.innerWidth?window.innerWidth:w)
			}
		} else {
			return {
				width : (w>document.documentElement.clientWidth?document.documentElement.clientWidth:w)
			}
		}
	};
	/**
	 * 如果窗口大于浏览器高度,返回浏览器高度
	 * @returns
	 */
	$.webapp.getInnerDialogH = function(h) {
		if (typeof window.innerWidth != 'undefined') {
			return {
				height : (h>parent.window.innerHeight?parent.window.innerHeight:h)
			}
		} else {
			return {
				height : (h>parent.document.documentElement.clientHeight?parent.document.documentElement.clientHeight:h)
			}
		}
	};


	/**
	 * 跨浏览器获取Style
	 * @param element
	 * @param attr
	 * @returns
	 */
	$.webapp.getStyle = function (element, attr) {
		if (typeof window.getComputedStyle != 'undefined') {//W3C
			return window.getComputedStyle(element, null)[attr];
		} else if (typeof element.currentStyle != 'undeinfed') {//IE
			return element.currentStyle[attr];
		}
	};
	
	/**
	 * 将表达序列号成对象
	 * @param formId 表单的ID $.webapp.serializeObject("#form")
	 * @param flag 是否包含空表单true是，false否
	 */
	$.webapp.serializeObject = function (formId, flag) {
		var o = {};
		$.each($(formId).serializeArray(), function(index) {
			if(undefined == flag || flag == false) {
				if (this['value'] != undefined && this['value'].length > 0) {
					if (o[this['name']]) {
						o[this['name']] = o[this['name']] + "," + this['value'];
					} else {
						o[this['name']] = this['value'];
					}
				}
			} else {
				if (o[this['name']]) {
					o[this['name']] = o[this['name']] + "," + this['value'];
				} else {
					o[this['name']] = this['value'];
				}
			}
		});
		return o;
	};
	
	/**
	 * 接受URL的参数
	 */
	$.webapp.request = {
		QueryString : function(val) {
			var uri = window.location.search;
			var re = new RegExp("" + val + "=([^&?]*)", "ig");
			return ((uri.match(re)) ? (uri.match(re)[0].substr(val.length + 1)) : null);
		}
	};
	
	/**
	 * 打开新的窗口
	 */
	$.webapp.open = function(url) {
		var width  = screen.availWidth-10;
		var height = screen.availHeight-50;
		var leftm  = 0;
		var topm   = 0;
		var args = "title=123,toolbar=0,location=0,maximize=1,directories=0,status=0,menubar=0,scrollbars=1, resizable=1,left=" + leftm+ ",top=" + topm + ", width="+width+", height="+height;
		var w = window.open(url,"",args);
		if(!w){
			alertify.error('发现弹出窗口被阻止，请更改浏览器设置，以便正常使用本功能！');
			return ;
		}
	} ;
	
	/**
	 * UEditor工具栏
	 */
	$.webapp.ue_simple = function(){
		return [[
		  		'fontset', '|', 'forecolor', 'backcolor',
		 		'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify','|', 
		 		'emotion', 'scrawl', '|', 'snapscreen', 'insertimage', 'attachment', '|', 'source', 'preview', 'fullscreen'
		 	]];
	};
	
})(window, jQuery);
