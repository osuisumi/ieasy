package com.ieasy.basic.util.file;

public class UploadForm {
	
	private String uploadDir;					//文件上传的目录
	
	private String orginalName;					//原文件名
	
	private String newName;						//新文件名
	
	private String savePath;					//文件保存的绝对路径
	
	private String web_url ;					//访问文件的URL地址

	private String contentType;					//文件类型

	private Long size;							//文件大小
	
	private boolean status = false ;			//文件是否上传成功
	
	private String msg ;						//提示消息
	
	public Object obj ;							//其他信息

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getOrginalName() {
		return orginalName;
	}

	public void setOrginalName(String orginalName) {
		this.orginalName = orginalName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public UploadForm() {
		super();
	}

	public UploadForm(String uploadDir, String orginalName, String newName,
			String savePath, String web_url, String contentType, Long size,
			boolean status, String msg, Object obj) {
		super();
		this.uploadDir = uploadDir;
		this.orginalName = orginalName;
		this.newName = newName;
		this.savePath = savePath;
		this.web_url = web_url;
		this.contentType = contentType;
		this.size = size;
		this.status = status;
		this.msg = msg;
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "UploadForm [uploadDir=" + uploadDir + ", orginalName="
				+ orginalName + ", newName=" + newName + ", savePath="
				+ savePath + ", web_url=" + web_url + ", contentType="
				+ contentType + ", size=" + size + ", status=" + status
				+ ", msg=" + msg + ", obj=" + obj + "]";
	}

}
