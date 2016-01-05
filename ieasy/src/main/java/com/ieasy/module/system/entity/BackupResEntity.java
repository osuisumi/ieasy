package com.ieasy.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 系统资源备份
 */
@Entity
@Table(name = "ieasy_sys_backup_res")
public class BackupResEntity extends ExtFieldEntity {

	private String backup_name ;				//备份文件的名称
	
	private int status ;						//文件备份状态（0:待定、1:成功，2：失败）
	
	private float size ;						//备份文件的大小
	
	private String db_script ;					//数据库脚步名称
	
	private int backup_type ;					//备份的路径（0:手动备份、1:计划备份）
	
	private Date backup_time ;					//的备份时间

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


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public int getBackup_type() {
		return backup_type;
	}

	public void setBackup_type(int backup_type) {
		this.backup_type = backup_type;
	}

	public Date getBackup_time() {
		return backup_time;
	}

	public void setBackup_time(Date backup_time) {
		this.backup_time = backup_time;
	}


}
