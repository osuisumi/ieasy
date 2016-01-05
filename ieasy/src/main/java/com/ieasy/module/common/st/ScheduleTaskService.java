package com.ieasy.module.common.st;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.UUIDHexGenerator;
import com.ieasy.module.common.web.servlet.WebContextUtil;

@Service
@Transactional
public class ScheduleTaskService implements IScheduleTaskService {
	
	private Logger logger = Logger.getLogger(ScheduleTaskService.class) ;

	@Autowired
	private IBaseDao<ScheduleTaskEntity> basedaoSt ;
	
	@Autowired
	private SchedulerUtil schedulerUtil ;

	@Override
	public ScheduleTaskForm add(ScheduleTaskForm form) {
		try {
			String id = UUIDHexGenerator.generator() ;
			if(null == form.getTask_code() || form.getTask_code().equals("")) {
				form.setTask_code(id) ;
			}
			this.schedulerUtil.scheduler(form) ;
			
			ScheduleTaskEntity entity = new ScheduleTaskEntity() ;
			BeanUtils.copyProperties(form, entity, new String[]{"run_count"}) ;
			entity.setCreateName(WebContextUtil.getCurrentUser().getUser().getEmp_name()) ;
			entity.setCreated(new Date()) ;

			ScheduleTaskEntity e = this.basedaoSt.add(entity) ;
			ScheduleTaskForm stf = new ScheduleTaskForm() ;
			BeanUtils.copyNotNullProperties(e, stf) ;
			return stf;
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("创建失败===>异常信息：", e);
			return null ;
		}
	}

	@Override
	public Msg delete(ScheduleTaskForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					ScheduleTaskEntity entity = this.basedaoSt.load(ScheduleTaskEntity.class, id) ;
					ScheduleTaskForm task = new ScheduleTaskForm() ;
					BeanUtils.copyProperties(entity, task) ;
					this.schedulerUtil.deleteJob(task) ;
					this.basedaoSt.delete(entity) ;
				}
				return new Msg(true, "删除成功！") ;
			}
		} catch (Exception e) {
			logger.error("根据ID["+form.getIds()+"]删除失败===>异常信息：", e) ;
			return new Msg(false, "删除失败！") ;
		}
		return null;
	}
	
	@Override
	public Msg delete(String id) {
		try {
			if(null != id && !"".equals(id)) {
				ScheduleTaskEntity entity = this.basedaoSt.load(ScheduleTaskEntity.class, id) ;
				ScheduleTaskForm task = new ScheduleTaskForm() ;
				BeanUtils.copyProperties(entity, task) ;
				this.schedulerUtil.deleteJob(task) ;
				this.basedaoSt.delete(entity) ;
				return new Msg(true, "删除成功！") ;
			}
		} catch (Exception e) {
			logger.error("根据ID["+id+"]删除失败===>异常信息：", e) ;
			return new Msg(false, "删除失败！") ;
		}
		return null;
	}

	@Override
	public Msg update(ScheduleTaskForm form) {
		try {
			String currentEnable = null, cron_expression = null ;
			
			ScheduleTaskEntity entity = this.basedaoSt.load(ScheduleTaskEntity.class, form.getId());
			currentEnable = entity.getTask_enable() ; 
			cron_expression = entity.getCron_expression() ;
			
			BeanUtils.copyProperties(form, entity, new String[]{"created", "createName", "modifyDate"});
			entity.setModifyDate(new Date()) ;
			this.basedaoSt.update(entity);
			
			//判断任务修改前的状态和修改后的状态是否不相同，不相同则修改任务的状态
			if(!form.getTask_enable().equalsIgnoreCase(currentEnable)) {
				ScheduleTaskForm task = new ScheduleTaskForm() ;
				BeanUtils.copyProperties(entity, task) ;
				if("Y".equalsIgnoreCase(form.getTask_enable())) {
					this.schedulerUtil.resumeJob(task) ;
				} else {
					this.schedulerUtil.pauseJob(task) ;
				}
			}
			//判断任务修改前和修改后的触发时间是否一致，不一致则重新设定触发时间
			if(!form.getCron_expression().equalsIgnoreCase(cron_expression)) {
				//不管任务的状态是否启动，都设为启动
				entity.setTask_enable("Y") ;
				ScheduleTaskForm task = new ScheduleTaskForm() ;
				BeanUtils.copyProperties(entity, task) ;
				this.schedulerUtil.rescheduleJob(task) ;
			}
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			logger.error("修改失败===>异常信息：", e) ;
			return new Msg(false, "修改失败！") ;
		}
	}

	@Override
	public ScheduleTaskForm get(String id) {
		try {
			String sql = "select " +
						 "s.id, s.task_code, s.task_name, " +
						 "s.task_job_class, s.cron_expression, s.task_enable, s.task_remark, s.task_datetime, s.run_count," +
						 "s.created, s.createName, s.modifyDate, s.modifyName " +
						 "from ieasy_sys_st s " +
					 	 "where s.id='"+id+"'";
				
			return (ScheduleTaskForm)this.basedaoSt.queryObjectSQL(sql, ScheduleTaskForm.class, false) ;
		} catch (Exception e) {
			e.printStackTrace() ;
			logger.error("加载失败===>异常信息：", e) ;
			throw new ServiceException("加载异常：", e) ;
		}
	}
	
	@Override
	public ScheduleTaskForm get(ScheduleTaskForm form) {
		try {
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select " +
						 "s.id, s.task_code, s.task_name, " +
						 "s.task_job_class, s.cron_expression, s.task_enable, s.task_remark, s.task_datetime, s.run_count," +
						 "s.created, s.createName, s.modifyDate, s.modifyName, " +
						 "l.st_id, l.link1, l.link2, l.link3, l.link4, l.link5, l.link6 " +
						 "from ieasy_sys_st s " +
						 "left join ieasy_sys_st_link l ON(l.st_id=s.id) " +
					 	 "where 1=1 ";
			sql = addWhere(sql, form, alias) ;
			
			/*Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select " +
						 "s.id, s.task_code, s.task_name, " +
						 "s.task_job_class, s.cron_expression, s.task_enable, s.task_remark," +
						 "s.created, s.createName, s.modifyDate, s.modifyName " +
						 "from ieasy_sys_st s " +
					 	 "where s.id='"+form.getId()+"'";
			*/	
			
			return (ScheduleTaskForm)this.basedaoSt.queryObjectSQL(sql, alias, ScheduleTaskForm.class, false) ;
		} catch (Exception e) {
			e.printStackTrace() ;
			logger.error("加载失败===>异常信息：", e) ;
			throw new ServiceException("加载异常：", e) ;
		}
	}
	
	@Override
	public DataGrid datagrid(ScheduleTaskForm form) {
		
		if (null == form.getSort()) {
			SystemContext.setSort("s.task_datetime");
		} else {
			SystemContext.setSort("s." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
				"s.id, s.task_code, s.task_name, " +
				"s.task_job_class, s.cron_expression, s.task_enable, s.task_remark, s.task_datetime, s.run_count," +
				"s.created, s.createName, s.modifyDate, s.modifyName " +
				"from ieasy_sys_st s " +
				"where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		Pager<Object> pager = this.basedaoSt.findSQL(sql, alias, ScheduleTaskForm.class, false) ;
		
		
		DataGrid dg = new DataGrid() ;
		dg.setTotal(pager.getTotal());
		dg.setRows(pager.getDataRows());
		return dg;
	}

	@Override
	public List<ScheduleTaskForm> find(ScheduleTaskForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "s.id, s.task_code, s.task_name, " +
					 "s.task_job_class, s.cron_expression, s.task_enable, s.task_remark, s.run_count," +
					 "s.created, s.createName, s.modifyDate, s.modifyName, " +
					 "l.link1, l.link2, l.link3, l.link4, l.link5, l.link6 " +
					 "from ieasy_sys_st s " +
					 "left join ieasy_sys_st_link l ON(l.st_id=s.id) " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		
		return this.basedaoSt.listSQL(sql, alias, ScheduleTaskForm.class, false) ;
	}
	
	
	private String addWhere(String sql, ScheduleTaskForm form, Map<String, Object> params) {
		if (null != form) {
			if(form.getId() != null && !form.getId().trim().equals("")) {
				sql += " and s.id=:id";
				params.put("id", form.getId().trim());
			}
			if(form.getRun_count() != null) {
				sql += " and s.run_count=:run_count";
				params.put("run_count", form.getRun_count());
			}
			if(form.getTask_enable() != null && !form.getTask_enable().equals("")) {
				sql += " and s.task_enable=:task_enable";
				params.put("task_enable", form.getTask_enable());
			}
			if(form.getTask_name() != null && !form.getTask_name().trim().equals("")) {
				sql += " and s.task_name=:task_name";
				params.put("task_name", form.getTask_name().trim());
			}
			if (form.getTask_datetime_start() != null && !"".equals(form.getTask_datetime_start())) {
				sql += " and date_format(s.task_datetime,'%Y-%m-%d')>= date_format(:task_datetime,'%Y-%m-%d') ";
				params.put("task_datetime", form.getTask_datetime_start());
			}
			if (form.getTask_datetime_end() != null && !"".equals(form.getTask_datetime_end())) {
				sql += " and date_format(s.task_datetime,'%Y-%m-%d')<= date_format(:task_datetime,'%Y-%m-%d') ";
				params.put("task_datetime", form.getTask_datetime_end());
			}
			if(form.getLink1() != null && !form.getLink1().trim().equals("")) {
				sql += " and l.link1=:link1";
				params.put("link1", form.getLink1().trim());
			}
			if(form.getLink2() != null && !form.getLink2().trim().equals("")) {
				sql += " and l.link2=:link2";
				params.put("link2", form.getLink2().trim());
			}
			if(form.getLink3() != null && !form.getLink3().trim().equals("")) {
				sql += " and l.link3=:link3";
				params.put("link3", form.getLink3().trim());
			}
			if(form.getLink4() != null && !form.getLink4().trim().equals("")) {
				sql += " and l.link4=:link4";
				params.put("link4", form.getLink4().trim());
			}
		}
		return sql;
	}

	
}
