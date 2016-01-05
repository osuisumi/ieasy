package com.ieasy.module.system.service;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.web.form.BackupResForm;

public interface IBackupResService {
	
	public Msg backup(BackupResForm form) ;
	
	public Msg resume(BackupResForm form) ;
	
	public Msg delete(BackupResForm form) ;
	
	public BackupResForm get(String id) ;
	
	public DataGrid datagrid(BackupResForm form) ;
	
}
