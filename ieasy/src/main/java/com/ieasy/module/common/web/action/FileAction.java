package com.ieasy.module.common.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.basic.util.file.UploadForm;
import com.ieasy.basic.util.file.UploadUtil;
import com.ieasy.module.common.web.servlet.WebContextUtil;

/**
 * 公共的文件上传处理Action
 * 默认将文件上传到一个临时目录temp
 * 操作完之后再将文件拷贝到相应的目录
 * @author ibm-work
 *
 */
@Controller
@RequestMapping("/fileAction")
public class FileAction extends BaseController {
	
	@RequestMapping(value="/doNotNeedAuth_Upload.do")
	public @ResponseBody UploadForm doNotNeedAuth_Upload(MultipartHttpServletRequest multipartRequest, String uploadDir, String newName){
		//默认为临时目录
		uploadDir = (null==uploadDir?"/temp":uploadDir) ;
		return UploadUtil.upload(multipartRequest, uploadDir, newName) ;
	}

	@RequestMapping(value="/doNotNeedAuth_Delete.do")
	public @ResponseBody void doNotNeedAuth_deleteFile(String path){
		FileUtils.deleteFile(WebContextUtil.getRealPath(path)) ;
	}
	
}
