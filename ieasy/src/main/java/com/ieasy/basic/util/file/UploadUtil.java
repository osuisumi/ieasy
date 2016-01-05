package com.ieasy.basic.util.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ieasy.basic.util.ConfigUtil;
import com.ieasy.basic.util.StringUtil;

public class UploadUtil {

	private static Logger logger = Logger.getLogger(UploadUtil.class) ;
	
	public static UploadForm upload(MultipartHttpServletRequest multipartRequest) {
		return upload(multipartRequest, null, null) ;
	}
	
	public static UploadForm upload(MultipartHttpServletRequest multipartRequest, String uploadDir) {
		return upload(multipartRequest, uploadDir, null) ;
	}
	
	public static UploadForm upload(MultipartHttpServletRequest multipartRequest, String uploadDir, String newName) {
		uploadDir = (null==uploadDir?"":uploadDir) ;
		
		UploadForm uf = new UploadForm() ;
		try {
			uploadDir =  StringUtil.filePath(File.separator + ConfigUtil.get("uploadDir") + uploadDir) + File.separator ;
			String savePath = StringUtil.filePath(multipartRequest.getSession().getServletContext().getRealPath(uploadDir) + File.separator) ;
			String web_root = multipartRequest.getScheme()+"://"+multipartRequest.getServerName()+":"+multipartRequest.getServerPort() ;
			
			List<MultipartFile> files = multipartRequest.getFiles(ConfigUtil.get("uploadFieldName"));
			if (files == null || files.size() < 1) {
				throw new RuntimeException("没有发生上传的文件！请检查") ;
			}
			
			for (int i = 0; i < files.size(); i++) {
				MultipartFile multipartFile = files.get(i);
				String orginalName = multipartFile.getOriginalFilename();
				if(null == newName || "".equals(newName.trim())) {
					newName = System.currentTimeMillis() + "." + FilenameUtils.getExtension(orginalName) ;
				} else if("orginalName".equals(newName.trim())){
					newName = orginalName ;
				} else {
					newName = newName + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(orginalName) ;
				}
				FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(savePath + newName)) ;
				
				uf.setContentType(multipartFile.getContentType()) ;
				uf.setOrginalName(orginalName) ;
				uf.setNewName(newName) ;
				uf.setSize(multipartFile.getSize()) ;
				uf.setUploadDir(uploadDir) ;
				uf.setSavePath(savePath + newName) ;
				uf.setWeb_url(StringUtil.urlPath(web_root+multipartRequest.getContextPath()+uploadDir+newName)) ;
				uf.setMsg("文件上传成功！") ;
				uf.setStatus(true) ;
				
			}
		} catch (IOException e) {
			logger.error("文件删除异常：" + e.getMessage()) ;
		}
		
		return uf;
	}
	
}
