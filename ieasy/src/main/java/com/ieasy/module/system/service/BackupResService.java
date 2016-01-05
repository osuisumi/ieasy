/**
 * 
 */
package com.ieasy.module.system.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.util.ConfigUtil;
import com.ieasy.basic.util.MySQLUtil;
import com.ieasy.basic.util.SystemPath;
import com.ieasy.basic.util.compress.TarAndGzUtil;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.basic.util.samba.SambaUtil;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.BackupResEntity;
import com.ieasy.module.system.web.form.BackupResForm;

/**
 * 备份系统资源文件和数据库
 * 1、备份数据库
 * 		1.1备份当前数据库中的数据，生成*.sql文件在指定的临时目录中，文件名：2014-09-09[17-39-30]_数据库名称.sql
 * 		
 * 2、备份系统资源文件
 * 		2.1将需备份的目录拷贝到指定的临时目录中
 * 3、将数据库文件和系统资源文件一起压缩
 * 		压缩的目录结构：
 * 				2014-09-09[17-39-30]_数据库名称.sql
 * 				attached[附件目录]
 * 				other[附件目录]
 * 4、将压缩文件上传到FTP服务器或者共享目录
 * 5、记录相关信息到数据库
 * 6、恢复数据库和系统资源文件
 * 		6.1从FTP或共享目录中下载相关的备份文件
 * 		6.2备份文件解压缩
 * 		6.3删除现有的系统资源文件，再讲解压缩的资源文件拷贝到相应的目录中
 * 		6.4恢复数据库
 * @author 杨浩泉
 *
 */
@Service @Transactional
public class BackupResService implements IBackupResService {

	private static Logger log = Logger.getLogger(BackupResService.class) ;
	
	@Inject
	private IBaseDao<BackupResEntity> basedaoResBack ;
	
	@Inject
	private TaskExecutor taskExecutor;				
	
	@Inject
	private IDBUtilsHelper dbUtil ;
	
	/**
	 * 临时目录
	 */
	private String tempBackupPath = SystemPath.getSystmpPath() + SystemPath.getSeparator() + "backup" ;
	private String tempResumePath = SystemPath.getSystmpPath() + SystemPath.getSeparator() + "resume" ;

	@Override
	public Msg delete(BackupResForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					this.basedaoResBack.delete(BackupResEntity.class, id) ;
				}
				return new Msg(true, "删除成功！") ;
			}
		} catch (Exception e) {
			log.error("根据ID["+form.getIds()+"]删除备份数据信息失败===>异常信息：", e) ;
			return new Msg(false, "删除失败！") ;
		}
		return null;
	}

	@Override
	public BackupResForm get(String id) {
		try {
			String sql = "select " +
						 "t.id, t.backup_name, " +
						 "cast(t.size as char(100)) as size, " +
						 "(CASE t.backup_type WHEN 0 THEN '手动备份' ELSE '计划备份' END) as backup_type, " +
						 "(CASE t.status WHEN 0 THEN '待定' WHEN 1 THEN '成功' ELSE '失败' END) as status, " +
						 "t.backup_time, t.db_script " +
					 	 "from ieasy_sys_backup_res t " +
					 	 "where 1=1"; 
			
			return (BackupResForm)this.basedaoResBack.queryObjectSQL(sql, id, BackupResForm.class, false) ;
		} catch (Exception e) {
			e.printStackTrace() ;
			log.error("加载数据备份信息失败===>异常信息：", e) ;
			throw new ServiceException("加载数据备份信息异常：", e) ;
		}
	}

	@Override
	public DataGrid datagrid(BackupResForm form) {
		try {
			Pager<BackupResForm> pager = this.find(form) ;
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			e.printStackTrace() ;
			log.error("加载数据库备份列表信息失败===>异常信息：", e) ;
			throw new ServiceException("加载数据库备份列表信息异常：", e) ;
		}
	}
	
	private Pager<BackupResForm> find(BackupResForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "t.id, t.backup_name, " +
					 "cast(t.size as char(100)) as size, " +
					 "(CASE t.backup_type WHEN 0 THEN '手动备份' ELSE '计划备份' END) as backup_type, " +
					 "(CASE t.status WHEN 0 THEN '备份中' WHEN 1 THEN '成功' ELSE '失败' END) as status, " +
					 "t.backup_time, t.db_script " +
				 	 "from ieasy_sys_backup_res t " +
				 	 "where 1=1"; 
		sql = addWhere(sql, form, alias) ;
		return this.basedaoResBack.findSQL(sql, alias, BackupResForm.class, false) ;
	}
	
	private String addWhere(String sql, BackupResForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getBackup_time() != null && !"".equals(form.getBackup_time())) {
				sql += " and date_format(t.backup_time,'%Y-%m-%d')=date_format(:backup_time,'%Y-%m-%d')";
				params.put("backup_time", form.getBackup_time());
			}
		}
		return sql;
	}
	
	@Override
	public Msg backup(BackupResForm form) {
		try {
			//需要备份的文件目录
			String backupFiles = ConfigUtil.get("backupFile") ;
			if(null == backupFiles || "".equals(backupFiles.trim()))
				return new Msg(false, "未指定需要备份的文件目录，请配置conf文件") ;
			
			//保存记录到数据库
			BackupResEntity entity = new BackupResEntity() ;
			entity.setBackup_name("备份中") ;
			entity.setBackup_time(new Date()) ;
			entity.setSize(0) ;
			entity.setCreateName(form.getCreateName()) ;
			entity.setStatus(Integer.parseInt(form.getStatus().trim())) ;
			entity.setBackup_type(Integer.parseInt(form.getBackup_type().trim())) ;
			BackupResEntity retEntity = this.basedaoResBack.add(entity) ;
			
			//上传文件到服务器，异步上传，如果文件上传不成功，修改已保存到数据库中的记录状态为失败，最后删除压缩的归档文件
			this.taskExecutor.execute(new SambaPullThread(retEntity.getId(), form, backupFiles)) ;
			
			return new Msg(true, "数据备份中，请稍等!") ;
		} catch (Exception e) {
			log.error("备份数据失败，原因：", e) ;
			return new Msg(false, "备份数据失败！") ;
		}
	}

	private class SambaPullThread implements Runnable {
		private Serializable id ;
		private BackupResForm form ;
		private String backupFiles ;
		private File tarUtil ;
		private File tarGzUtil ;
		
		public SambaPullThread(Serializable id, BackupResForm form, String backupFiles) {
			super();
			this.id = id ;
			this.form = form ;
			this.backupFiles = backupFiles ;
		}
		
		@Override
		public void run() {
			synchronized(SambaPullThread.class) { 
					try {
						log.info("**************************备份系统资源开始**************************") ;
						
						//拷贝系统资源文件到指定的临时目录
						FileUtils.createDirectory(tempBackupPath) ;
						String[] backupDirs = backupFiles.split(",") ;
						for(int i=0; i<backupDirs.length; i++) {
							File file = new File(WebContextUtil.getAttachedPath(backupDirs[i])) ;
							if(file.exists()) {
								log.info("备份的目录：" + WebContextUtil.getAttachedPath(backupDirs[i])) ;
								FileUtils.copyDirectoryToDirectory(file, new File(tempBackupPath)) ;
							}
						}
						//文件日期名称
						String timeName = DateUtils.getSysDateStr() + "[" + DateUtils.formatDate(new Date(), "-", DateUtils.TYPE_TIME) + "]" ;
						
						//文件备份名称
						String filename = "未命名_" ;
						if(null != form.getBackup_name() && !"".equals(form.getBackup_name().trim())) {
							filename = form.getBackup_name() + "_" ;
						}
						filename += timeName ;
						
						//数据库备份脚本名称
						String db_name = timeName+"_"+ConfigUtil.get("database")+".sql" ;
						//归档文件名称
						String tar_name = SystemPath.getSystmpPath() + SystemPath.getSeparator() + filename + ".tar" ;
						
						
						//备份数据库，生成*.sql文件到指定的临时目录
						MySQLUtil msu = MySQLUtil.getInstance() ;	
						msu.setCfg(tempBackupPath, db_name, ConfigUtil.get("database"), ConfigUtil.get("username"), ConfigUtil.get("password")) ;
						msu.backup() ;
						
						//归档
						tarUtil = TarAndGzUtil.tarUtil(tempBackupPath, tar_name) ;
						//压缩
						tarGzUtil = TarAndGzUtil.tarGzUtil(tarUtil.getAbsolutePath(), tarUtil.getAbsolutePath() + ".gz") ;
						
						//开始上传备份文件到远程共享目录
						SambaUtil.putSmb("/" + tarGzUtil.getName() , tarGzUtil) ;
						
						//修改数据库记录的备份状态
						dbUtil.getQr().update("update ieasy_sys_backup_res set " +
								"backup_name=?, size=?, db_script=?, createName=?, backup_type=?," +
								"backup_time=?, status=? " +
								"where id=?", tarGzUtil.getName(), tarGzUtil.length(), db_name, form.getCreateName(), 0, new Date(), 1, id) ;
						
						log.info("**************************备份系统资源结束**************************") ;
					} catch (Exception e) {
						log.error("备份系统资源失败", e) ;
						try {
							log.info("备份文件上传失败，删除数据库记录") ;
							dbUtil.getQr().update("delete from ieasy_sys_backup_res where id=?", id);
						} catch (SQLException e1) { log.error("删除数据库记录失败", e) ; }
					} finally {
						log.info("删除backup目录==>"+tarGzUtil) ;
						FileUtils.deleteDirectory(tempBackupPath) ;	
						log.info("删除归档文件tar==>"+tarUtil) ;
						FileUtils.deleteFile(tarUtil) ;	
						log.info("删除压缩的备份文件==>"+tarGzUtil) ;
						FileUtils.deleteFile(tarGzUtil) ;	
						log.info("****************************************************************************\r\n") ;
					}
			}
		}
	}
	
	@Override
	public Msg resume(BackupResForm form) {
		try {
			log.info("**************************开始恢复备份的系统资源**************************") ;
			log.info("要恢复的系统资源文件：" + form.getBackup_name());
			log.info("要恢复的数据库脚本文件：" + form.getDb_script());
			
			log.info("开始从远程共享目录下载") ;
			FileUtils.createDirectory(tempResumePath) ;
			//下载文件
			SambaUtil.getSmb("/"+form.getBackup_name(), tempResumePath) ;
			
			//解压文件
			TarAndGzUtil.UnTarGzUtil(tempResumePath + "/" + form.getBackup_name(), tempResumePath) ;
			
			//拷贝文件到指定目录进行恢复
			String[] backupDirs = ConfigUtil.get("backupFile").split(",") ;
			for(int i=0; i<backupDirs.length; i++) {
				File file = new File(tempResumePath + SystemPath.getSeparator() + "backup" + backupDirs[i]) ;
				if(file.exists()) {
					log.info("恢复的目录：" + tempResumePath + SystemPath.getSeparator() + "backup" + SystemPath.getSeparator() + backupDirs[i]) ;
					FileUtils.copyDirectoryToDirectory(file, new File(WebContextUtil.getAttachedPath(null))) ;
				}
			}
			
			//恢复数据库
			MySQLUtil msu = MySQLUtil.getInstance() ;
			msu.setCfg(tempResumePath + SystemPath.getSeparator() + "backup", form.getDb_script(), ConfigUtil.get("database"), ConfigUtil.get("username"), ConfigUtil.get("password")) ;
			msu.resume() ;
			
			log.info("**************************恢复备份的系统资源结束**************************") ;
		} catch (IOException e) {
			log.debug("恢复系统资源发生错误", e) ;
			return new Msg(false, "恢复系统资源失败！") ;
		} catch (Exception e) {
			log.debug("发生错误", e) ;
			return new Msg(false, "恢复系统资源失败！") ;
		} finally {
			FileUtils.deleteDirectory(tempResumePath) ;	
			log.info("****************************************************************************\r\n") ;
		}
		
		return new Msg(true, "恢复数据库成功！") ;
	}
}
