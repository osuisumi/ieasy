package com.ieasy.module.common.executors;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import com.ieasy.module.oa.project.service.IProjectCenterService;

/**
 * 开发人员提醒或结束定时作业
 * @author Administrator
 *
 */
public class ProjectDevExitJob implements Job{
	
	private static Logger logger = Logger.getLogger(ProjectDevExitJob.class);

	private JobKey jobKey ;

	private ApplicationContext appCtx ;
	
	private IProjectCenterService projectService ;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			SchedulerContext schCtx = context.getScheduler().getContext();  
			jobKey = context.getJobDetail().getKey();
			
			appCtx = (ApplicationContext)schCtx.get("applicationContext");
			projectService = (IProjectCenterService)appCtx.getBean("projectCenterService") ;
			
			this.projectService.sendEmailProjectDevInfoForTimer(jobKey.getName()) ;
			
		} catch (SchedulerException e) {
			logger.error("发生异常[SchedulerContext无法获取Spring的上下文]：", e) ;
			e.printStackTrace();
		}
	}

}
