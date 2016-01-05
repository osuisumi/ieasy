package com.ieasy.module.common.executors;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;

public class ResourceBackupJob implements Job {
	
	private static Logger logger = Logger.getLogger(ResourceBackupJob.class);

	private JobKey jobKey ;
	
	private IDBUtilsHelper dbutil ;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			
			SchedulerContext schCtx = context.getScheduler().getContext();  
			ApplicationContext appCtx = (ApplicationContext)schCtx.get("applicationContext");
			dbutil = (IDBUtilsHelper) appCtx.getBean("dbutil") ;
			jobKey = context.getJobDetail().getKey();
			dbutil.getQr().update("update ieasy_sys_st t set t.run_count=t.run_count+1 WHERE t.task_code=?", new Object[]{jobKey.getName()}) ;
			
			executor() ;
			
		} catch (SchedulerException | SQLException e) {
			logger.error("发生异常[SchedulerContext无法获取Spring的上下文]：", e) ;
			e.printStackTrace();
		}
	}

	private void executor() {
		logger.debug("执行定时作业[资源定时备份]" + " 运行时间：" + DateUtils.getSysDateTimeStr() + " 任务代码：" + jobKey.getName()) ;
	}

}
