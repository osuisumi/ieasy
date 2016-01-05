package com.ieasy.module.system.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.IBackupResService;
import com.ieasy.module.system.web.form.BackupResForm;

@Controller
@RequestMapping("/admin/system/backup_res")
public class BackupResAction extends BaseController {
	
	@Inject
	private IBackupResService backupService ;
	
	@RequestMapping("/backup_res_main_UI.do")
	public String db_backup_main_UI() {
		return Const.SYSTEM + "backup_res_main_UI" ;
	}
	
	@RequestMapping("/backup_res_form.do")
	public String backup_res_form() {
		return Const.SYSTEM + "backup_res_form" ;
	}
	
	@RequestMapping("/doNotNeedAuth_backup.do")
	public @ResponseBody Msg doNotNeedAuth_backup(BackupResForm form) {
		return this.backupService.backup(form) ;
	}
	
	@RequestMapping("/resume.do")
	public @ResponseBody Msg resume(BackupResForm form) {
		return this.backupService.resume(form) ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public BackupResForm get(String id) throws Exception {
		return this.backupService.get(id) ;
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody Msg delete(BackupResForm form) {
		return this.backupService.delete(form) ;
	}
	
	
	@RequestMapping("/datagrid.do")
	public @ResponseBody DataGrid datagrid(BackupResForm form) {
		return this.backupService.datagrid(form) ;
	}
}
