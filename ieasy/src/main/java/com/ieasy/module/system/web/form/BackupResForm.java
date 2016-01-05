package com.ieasy.module.system.web.form;

import java.io.Serializable;
import java.util.Date;

import com.ieasy.basic.model.PageHelper;
import com.ieasy.basic.util.file.FileUtils;

public class BackupResForm extends PageHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private String backup_name ;				//备份文件的名称
	
	private String status ;						//文件备份状态（0：待定，1：成功，2：失败）
	
	private String size ;						//备份文件的大小
	
	private String db_script ;					//数据库脚步名称
	
	private String backup_type ;				//备份的路径（0:手动备份、1:计划备份）
	
	private Date backup_time ;					//备份时间

	public String getDb_script() {
		return db_script;
	}

	public void setDb_script(String db_script) {
		this.db_script = db_script;
	}

	public String getBackup_name() {
		return backup_name;
	}

	public void setBackup_name(String backup_name) {
		this.backup_name = backup_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = FileUtils.formetFileSize(Long.parseLong(size));
	}

	public String getBackup_type() {
		return backup_type;
	}

	public void setBackup_type(String backup_type) {
		this.backup_type = backup_type;
	}

	public Date getBackup_time() {
		return backup_time;
	}

	public void setBackup_time(Date backup_time) {
		this.backup_time = backup_time;
	}


}
