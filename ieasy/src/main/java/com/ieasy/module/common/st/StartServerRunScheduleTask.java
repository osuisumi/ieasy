package com.ieasy.module.common.st;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class StartServerRunScheduleTask {
	
	private Logger logger = Logger.getLogger(StartServerRunScheduleTask.class) ;

	@Autowired
	private IScheduleTaskService taskSchedulerService ;
	
	@Autowired
	private SchedulerUtil scheduler ;
	
	/**
	 * 服务器启动加载所有定时任务(task_enable=Y)
	 */
	public void runingScheduleTask() {
		try {
			ScheduleTaskForm form = new ScheduleTaskForm() ;
			form.setTask_enable("Y") ;
			List<ScheduleTaskForm> tasks = this.taskSchedulerService.find(form) ;
			for (ScheduleTaskForm t : tasks) {
				this.scheduler.scheduler(t) ;
				logger.info("启动定时作业：" + t) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
