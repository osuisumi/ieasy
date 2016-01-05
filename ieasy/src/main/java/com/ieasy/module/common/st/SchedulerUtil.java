package com.ieasy.module.common.st;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ieasy.basic.util.date.DateUtils;

@Component
public class SchedulerUtil {
	
	private Logger logger = Logger.getLogger(SchedulerUtil.class) ;

	@Autowired
	private Scheduler scheduler;

	/**
	 * 调度任务
	 * @param task
	 * @throws ClassNotFoundException
	 * @throws SchedulerException
	 */
	public void scheduler(ScheduleTaskForm task) throws ClassNotFoundException, SchedulerException {
		try {
			JobDetail jobDetail = JobBuilder.newJob(getClassByTask(task.getTask_job_class()))
					.withIdentity(task.getTask_code(), Scheduler.DEFAULT_GROUP).build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("cron_"+task.getTask_code(), Scheduler.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(task.getCron_expression())).build();
			
			//开始调度任务
			Date ft = scheduler.scheduleJob(jobDetail, trigger) ;
			
			logger.debug("定时作业生成：Cron表达式：["+task.getCron_expression()+"] 开始调度时间：["+DateUtils.formatYYYYMMDD_HHMMSS(ft)+"] 作业名称：["+task.getTask_name()+"] 任务代码：["+task.getTask_code()+"]") ;
		} catch (org.quartz.ObjectAlreadyExistsException e) {
			logger.error("发生异常", e) ;
			throw new org.quartz.ObjectAlreadyExistsException("该定时工作类已存在！");
		} catch (SchedulerException e) {
			logger.error("发生异常", e) ;
			throw new org.quartz.SchedulerException("任务调度失败！");
		} catch (ClassNotFoundException e) {
			logger.error("发生异常", e) ;
			throw new ClassNotFoundException("无法找到具体工作类！");
		}
	}
	
	/**
	 * 重新设置触发时间
	 * @param task
	 * @throws SchedulerException
	 */
	public void rescheduleJob(ScheduleTaskForm task) throws SchedulerException {
		try {
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("cron_"+task.getTask_code(), Scheduler.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(task.getCron_expression())).build();
			
			Date ft = scheduler.rescheduleJob(trigger.getKey(), trigger) ;
			
			logger.debug("重新设定任务触发时间：Cron表达式：["+task.getCron_expression()+"] 开始调度时间：["+DateUtils.formatYYYYMMDD_HHMMSS(ft)+"] 作业名称：["+task.getTask_name()+"] 任务代码：["+task.getTask_code()+"]") ;
		} catch (SchedulerException e) {
			logger.error("发生异常", e) ;
			throw new SchedulerException("重新设置触发时间失败！");
		}
	}
	
	/**
	 * 删除任务
	 * @param task
	 * @throws SchedulerException
	 */
	public void deleteJob(ScheduleTaskForm task) throws SchedulerException {
		try {
			boolean deleteJob = scheduler.deleteJob(new JobKey(task.getTask_code(), Scheduler.DEFAULT_GROUP)) ;
			logger.debug("删除定时作业：["+(deleteJob?"成功":"失败")+"] Cron表达式：["+task.getCron_expression()+"] 作业名称：["+task.getTask_name()+"] 任务代码：["+task.getTask_code()+"]") ;
		} catch (SchedulerException e) {
			logger.error("发生异常", e) ;
			throw new SchedulerException("删除任务失败！");
		}
	}
	
	/**
	 * 暂停任务
	 * @param task
	 * @throws SchedulerException
	 */
	public void pauseJob(ScheduleTaskForm task) throws SchedulerException {
		try {
			scheduler.pauseJob(new JobKey(task.getTask_code(), Scheduler.DEFAULT_GROUP)) ;
			logger.debug("暂停定时作业：Cron表达式：["+task.getCron_expression()+"] 作业名称：["+task.getTask_name()+"] 任务代码：["+task.getTask_code()+"]") ;
		} catch (SchedulerException e) {
			logger.error("发生异常", e) ;
			throw new SchedulerException("暂停任务失败！");
		}
	}
	
	/**
	 * 重新启动任务
	 * 如果某个Job设为停止，重启服务器后，该Job不会启动，通过手动操作，点击页面的启动，也无法启动，因为该Job在内存中消失了。
	 * 点击重新启动任务，如果该Job不在内存中，则重新创建该Job
	 * @param task
	 * @throws SchedulerException
	 */
	public void resumeJob(ScheduleTaskForm task) throws SchedulerException {
		try {
			JobKey jobKey = new JobKey(task.getTask_code(), Scheduler.DEFAULT_GROUP) ;
			
			//判读该Job是否存在（内存中），不存在则重新创建
			boolean checkExists = this.scheduler.checkExists(jobKey) ;
			if(!checkExists) {
				JobDetail jobDetail = JobBuilder.newJob(getClassByTask(task.getTask_job_class()))
						.withIdentity(task.getTask_code(), Scheduler.DEFAULT_GROUP).build();

				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("cron_"+task.getTask_code(), Scheduler.DEFAULT_GROUP)
						.withSchedule(CronScheduleBuilder.cronSchedule(task.getCron_expression())).build();
				
				//开始调度任务
				Date ft = scheduler.scheduleJob(jobDetail, trigger) ;
				logger.debug("重新启动定时作业[该JOB不存在内存中，将重新创建]：Cron表达式：["+task.getCron_expression()+"] 开始调度时间：["+DateUtils.formatYYYYMMDD_HHMMSS(ft)+"] 作业名称：["+task.getTask_name()+"] 任务代码：["+task.getTask_code()+"]") ;
				logger.debug("该Job["+jobKey+"]不存在内存中，将重新创建，触发时间["+task.getCron_expression()+"]");
			} else {
				scheduler.resumeJob(jobKey) ;
				logger.debug("重新启动定时作业：Cron表达式：["+task.getCron_expression()+"] 作业名称：["+task.getTask_name()+"] 任务代码：["+task.getTask_code()+"]") ;
			}
			
		} catch (SchedulerException e) {
			logger.error("发生异常", e) ;
			throw new SchedulerException("重新启动任务失败！");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Class<Job> getClassByTask(String taskClassName)
			throws ClassNotFoundException {
		return (Class<Job>) Class.forName(taskClassName);
	}

}
