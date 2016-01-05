<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var boundx, boundy, x, y, w, h, filedata ;
	
	$(function() {
		$("#import_user_data").euploadify({
			required: true, border: false, fileTypeExts : '*.jpg; *.png',
		 	swf: $.webapp.root+'/script/plugins/uploadify/uploadify.swf',
		 	uploader: $.webapp.root+'/fileAction/doNotNeedAuth_Upload.do',
	        onUploadSuccess: function(file, data, response) {
	        	filedata = $.parseJSON(data) ;
	        	$("input[name=emp_pic_path]").val(filedata.web_url) ;
	        	
	        	$("#empTxCropPic").html("<img src='"+filedata.web_url+"'>") ;
	    		$("#empTxPicView").html("<img src='"+filedata.web_url+"'>") ;
	    		
	    		$("#empTxPicView img").Jcrop({
	    			setSelect: [0, 0, 120, 150],
	    			onChange: updatePreview,
	    			onSelect: updatePreview,
	    			//aspectRatio: 120 / 150
	    		},function() {
	    			var bounds = this.getBounds();
	    		    boundx = bounds[0];
	    		    boundy = bounds[1];
	    		});
	        }
		});
	});
	
	function updatePreview(c) {
		x=c.x; y=c.y; w=c.w; h=c.h;
		//console.info(c.x+","+c.y+","+c.w+","+c.h) ;
		if (parseInt(c.w) > 0) {
			var rx = 120 / c.w;
			var ry = 150 / c.h;
			
			$("#empTxCropPic img").css({
				width: Math.round(rx * boundx) + 'px',
	          	height: Math.round(ry * boundy) + 'px',
	          	marginLeft: '-' + Math.round(rx * c.x) + 'px',
	          	marginTop: '-' + Math.round(ry * c.y) + 'px'
	        });
		}
	}
	
	function finishPic($d) {
		$("#emp_tx_view").attr("class", "img") ;
		$("#emp_tx_view").attr("src", filedata.web_url) ;
		$("input[name=txPicPath]").val(filedata.uploadDir+filedata.newName) ;
	}
	
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north', border:false" style="overflow: hidden;">
		<div id="import_user_data" style="border:1px solid red;"></div>
	</div>
	<div id="layout_dg" data-options="region:'center', border:false" style="border-top:1px solid #ccc;">
		<div style="float:left;margin-left:5px;">
			<input type="hidden" name="emp_pic_path">
			<p id="empTxCropPic"></p>
			<p style="margin:0 auto ;text-align: center;">
				<a onClick="" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-pictures'">确定选择</a>
			</p>
		</div>
		
		<div id="empTxPicView"></div>
	</div>
</div>

