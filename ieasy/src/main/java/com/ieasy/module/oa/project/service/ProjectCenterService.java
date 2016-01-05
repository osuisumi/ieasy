package com.ieasy.module.oa.project.service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.ConfigUtil;
import com.ieasy.basic.util.FreemarkerUtil;
import com.ieasy.basic.util.HtmlUtil;
import com.ieasy.basic.util.StringUtil;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.basic.util.mail.MailVO;
import com.ieasy.basic.util.springmvc.RealPathResolver;
import com.ieasy.module.common.service.BaseService;
import com.ieasy.module.common.st.IScheduleTaskService;
import com.ieasy.module.common.st.ScheduleLinkOperEntity;
import com.ieasy.module.common.st.ScheduleTaskEntity;
import com.ieasy.module.common.st.ScheduleTaskForm;
import com.ieasy.module.common.util.activemq.send.ISendMailService;
import com.ieasy.module.oa.project.entity.ProjectApprove;
import com.ieasy.module.oa.project.entity.ProjectCenter;
import com.ieasy.module.oa.project.entity.ProjectDevWorkTime;
import com.ieasy.module.oa.project.web.form.ProjectApproveForm;
import com.ieasy.module.oa.project.web.form.ProjectCenterForm;
import com.ieasy.module.oa.project.web.form.ProjectDevWorkTimeForm;
import com.ieasy.module.system.entity.PersonEntity;
import com.ieasy.module.system.web.form.LoginUser;
import com.ieasy.module.system.web.form.PersonForm;

@Service
@Transactional
public class ProjectCenterService extends BaseService implements IProjectCenterService {

	private static Log LOG = LogFactory.getLog(ProjectCenterService.class);

	@Inject
	private IDBUtilsHelper dbutil;

	@Inject
	private IBaseDao<ProjectCenter> basedaoProject;

	@Inject
	private IBaseDao<PersonEntity> basedaoPerson;

	@Inject
	private IBaseDao<ProjectDevWorkTime> basedaoProjectDevWorkTime;

	@Inject
	private IProjectDevWorkTimeService projectDevWorkTimeService;
	
	@Inject
	private IBaseDao<ProjectApprove> basedaoProjectApprove;

	@Inject
	private IBaseDao<ScheduleLinkOperEntity> basedaoScheduleLink;

	@Inject
	private IScheduleTaskService scheduleService ;
	
	@Inject
	private ISendMailService sendMessage ;
	
	@Autowired
	private RealPathResolver realPathResolver ;
	
	private FreemarkerUtil futil ; 
	private String outPath ;
	
	@Autowired(required=true)
	public ProjectCenterService(String ftlPath, String outPath) {
		if(null == futil) {
			this.outPath = outPath ;
			futil = FreemarkerUtil.getInstance(ftlPath) ;
		}
	}

	@Override
	public Msg createProject(ProjectCenterForm form) {
		Msg msg = new Msg() ;
		Map<String, Object> map = new HashMap<String, Object>() ;
		
		try {
			String api = form.getProj_approve_person_ids(); // 获取审批人ID
			String apn = form.getProj_approve_person_names(); // 获取审批人名称
			if (null == api || "".equals(api.trim())) {
				return new Msg(false, "项目审批人不能为空！");
			}

			ProjectCenter entity = new ProjectCenter();
			BeanUtils.copyNotNullProperties(form, entity);
			entity.setCreated(new Date());
			entity.setCreateName(super.getCurrentUser().getEmp_name());
			ProjectCenter project = this.basedaoProject.add(entity);
			
			ProjectCenterForm f = new ProjectCenterForm() ;
			BeanUtils.copyNotNullProperties(entity, f);

			//项目审批人设
			form.setId(project.getId());
			int setApproveHandler = setApproveHandler(form, api, apn);

			//项目开发人员作业日期设置
			setDevsWorkDate(project, form.getDevelops_worktime());

			
			map.put("project", f) ;
			map.put("approve_status", setApproveHandler) ;
			msg.setObj(map) ;
			msg.setStatus(true) ;
			msg.setMsg("项目新建成功！") ;
		} catch (RuntimeException e) {
			LOG.error("新建项目发生异常", e);
			return new Msg(false, "新建项目发生异常");
		}

		return msg ;
	}

	/**
	 * 项目审批人设置
	 * 
	 * @param form
	 * @param entity
	 * @param api
	 *            审批人的id
	 * @param apn
	 *            审批人的名称
	 */
	private int setApproveHandler(ProjectCenterForm form, String api, String apn) throws RuntimeException {
		int status = 0 ;
		try {
			String[] apis = api.split(",");
			String[] apns = apn.split(",");

			// A1、如果项目新建人不在项目审批人中，则加入到项目审批人中
			if (!StringUtil.arrayToString(apis).contains(super.getCurrentUser().getEmp_id())) {
				apis = ArrayUtils.add(apis, 0, super.getCurrentUser().getEmp_id());
				apns = ArrayUtils.add(apns, 0, super.getCurrentUser().getEmp_name());
			}

			// A2、重置项目审批人信息
			form.setProj_approve_person_ids(StringUtil.arrayToString(apis));
			form.setProj_approve_person_names(StringUtil.arrayToString(apns));

			if (null != apis && apis.length > 0) {
				Map<String, Object> alias = new HashMap<String, Object>();
				alias.put("project_id", form.getId());
				alias.put("person_ids", apis);
				// 1、删除数据库记录中不包含在apis中的审批人
				this.basedaoProjectApprove.executeSQL("delete from ieasy_oa_project_approve where person_id not in(:person_ids) and project_id=:project_id", alias);

				for (String personId : apis) {
					// 2、判断apis中的审批人是否已在数据库中，如果不在则添加，在则不做任何操作
					BigInteger count = this.basedaoProjectApprove.countSQL("select ap.id from ieasy_oa_project_approve where project_id=? and person_id=?", new Object[] { form.getId(), personId }, false);
					if (count.intValue() == 0) {
						PersonEntity person = this.basedaoPerson.load(PersonEntity.class, personId);

						ProjectApprove pa = new ProjectApprove();
						pa.setPerson_num(person.getNum());
						pa.setPerson_name(person.getName());
						pa.setPerson(person);

						pa.setProj_num(form.getProj_num());
						pa.setProj_name(form.getProj_name());
						pa.setProjectCenter(new ProjectCenter(form.getId()));

						// A3、判断状态是否为确认审批
						if (form.getProj_status() == 1) {

							// 确认我自己的审批，审批状态为1
							if (this.getCurrentUser().getEmp_id().equals(person.getId())) {
								pa.setStatus(1);
							}

							if (apis.length > 1) {
								setProjectStatus(1, form.getId());
							} else { 
								// 判断是否审批人只有自己，如果只有自己，项目的状态变更为办理中（2）
								status = 2 ;
								setProjectStatus(2, form.getId());
							}
						} else {
							// 提交状态为保存
							setProjectStatus(0, form.getId());
						}
						this.basedaoProjectApprove.add(pa);
					}
				}
			}

		} catch (RuntimeException e) {
			LOG.error("项目审批人设置发生异常", e);
			throw new RuntimeException("项目审批人设置发生错误！");
		}
		return status;
	}

	/**
	 * 稼动率相关，设置开发人员进入项目的开始和结束日期 客户端传入开发人员进项目的开始和结束日期的数据形式，如下 人员ID,人员编号,人员名称,项目中的角色,开始日期,结束日期|人员ID,人员编号,人员名称,项目中的角色,开始日期,结束日期
	 * 
	 * @param form
	 * @throws RuntimeException
	 */
	private void setDevsWorkDate(ProjectCenter project, String develops_worktime) throws RuntimeException {
		try {
			if (null != develops_worktime && !"".equals(develops_worktime.trim())) {
				String[] members = develops_worktime.split("\\|");
				for (String m : members) {
					String[] ms = m.split(",");
					String person_id = ms[0]; 				// 人员ID
					String person_num = ms[1]; 				// 人员编号
					String person_name = ms[2]; 			// 人员姓名
					String proj_role = ms[3]; 				// 人员在项目中的角色(稼动率范畴)
					String work_startDate = ms[4]; 			// 人员进项目的开始时间(稼动率范畴)
					String work_endDate = ms[5]; 			// 人员进项目的结束时间(稼动率范畴)
					LOG.debug(person_id + "-" + person_num + "-" + person_name + "-" + proj_role + "-" + work_startDate + "-" + work_endDate);

					ProjectDevWorkTimeForm devWrokTimeForm = new ProjectDevWorkTimeForm();
					devWrokTimeForm.setPerson_id(person_id);
					devWrokTimeForm.setPerson_num(person_num);
					devWrokTimeForm.setPerson_name(person_name);
					devWrokTimeForm.setProj_id(project.getId());
					devWrokTimeForm.setProj_num(project.getProj_num());
					devWrokTimeForm.setProj_name(project.getProj_name());
					devWrokTimeForm.setProj_role(proj_role);
					devWrokTimeForm.setWork_startDate(DateUtils.formatYYYYMMDD(work_startDate));
					devWrokTimeForm.setWork_endDate(DateUtils.formatYYYYMMDD(work_endDate));
					
					devWrokTimeForm.setWork_status(1) ;	//开发人员的状态为进行中
					this.projectDevWorkTimeService.add(devWrokTimeForm);
				}
			}
		} catch (RuntimeException e) {
			LOG.error("设置开发人员作业周期发生异常", e);
			throw new RuntimeException("设置开发人员作业周期错误！");
		}
	}

	/** 以上为新建项目范围************************************************************************************************************************************************************************ */

	@Override
	public Msg depeteProject(ProjectCenterForm form) {
		try {
			//删除项目定时器
			this.deleteProjectTime(form.getId()) ;
			//删除开发人员定时器
			this.deleteProjectDevTime(form.getId()) ;
			
			// 根据projectId删除开发人作业周期表中的所有记录
			this.basedaoProject.executeSQL("delete from ieasy_oa_project_dev_worktime where project_id=?", form.getId());
			// 根据projectId删除项目审批人员数据
			this.basedaoProjectApprove.executeSQL("delete from ieasy_oa_project_approve where project_id=?", form.getId());
			
			//删除项目
			this.basedaoProject.delete(ProjectCenter.class, form.getId());
			
		} catch (RuntimeException e) {
			LOG.error("项目销毁失败", e);
			return new Msg(false, "项目销毁失败");
		}
		return new Msg(true, "项目所有信息已销毁");
	}

	@Override
	public Msg updateProject(ProjectCenterForm form) throws RuntimeException {
		Msg msg = new Msg() ;
		Map<String, Object> map = new HashMap<String, Object>() ;
		try {
			// 如果项目的状态是（0：立项中，或者1：审批中）需要设置审批人，如果项目的状态是办理中的话，追加项目审批人无效
			if (form.getProj_status() == 0 || form.getProj_status() == 1) {
				/** 项目审批人设置 */
				String api = form.getProj_approve_person_ids(); // 获取审批人ID
				String apn = form.getProj_approve_person_names(); // 获取审批人名称
				if (null == api || "".equals(api.trim())) {
					return new Msg(false, "项目审批人不能为空！");
				}
				setApproveHandler(form, api, apn);
			}

			ProjectCenter entity = this.basedaoProject.load(ProjectCenter.class, form.getId());
			ProjectCenterForm pf = new ProjectCenterForm() ;
			BeanUtils.copyNotNullProperties(entity, pf) ;
			
			BeanUtils.copyNotNullProperties(form, entity, new String[]{"created", "createName"});
			entity.setModifyDate(new Date());
			entity.setModifyName(super.getCurrentUser().getEmp_name());
			this.basedaoProject.update(entity);
			
			// 如果项目的状态是（3：挂起，或者4：已办结）不可设置开发人的作业日期
			//if (entity.getProj_status() != 3 && entity.getProj_status() != 4) {
			/** 项目开发人员作业日期设置 */
			//	devsWorkDateHandler(entity, form.getDevelops_worktime());
			//}

			// 不管项目状态是3：挂起，或者4：已办结,都可以设置开发人的作业日期
			/** 项目开发人员作业日期设置 */
			devsWorkDateHandler(entity, form.getDevelops_worktime());

			map.put("oldProject", pf) ;
			msg.setStatus(true) ;
			msg.setMsg("修改项目信息成功！") ;
			msg.setObj(map) ;
		} catch (RuntimeException e) {
			LOG.error("修改项目信息发生异常", e);
			msg.setStatus(false) ;
			msg.setMsg("修改项目信息发生异常") ;
		}

		return msg;
	}

	/**
	 * 稼动率相关，设置开发人员进入项目的开始和结束日期 客户端传入开发人员进项目的开始和结束日期的数据形式，如下 作业周期ID，人员ID，人员编号，人员姓名，项目角色，作业开始时间，作业结束时间，平时加班，周末加班，节假日加班，作业状态|作业周期ID，人员ID，人员编号，人员姓名，项目角色，作业开始时间，作业结束时间，平时加班，周末加班，节假日加班，作业状态
	 * 
	 * @param form
	 * @throws RuntimeException
	 */
	private void devsWorkDateHandler(ProjectCenter project, String develops_worktime) throws RuntimeException {
		try {
			if (null != develops_worktime && !"".equals(develops_worktime.trim())) {
				
				String[] members = develops_worktime.split("\\|");
				for (String m : members) {
					String[] ms = m.split(",");
					String workTimeId = ms[0]; 				// 人员ID
					String person_id = ms[1]; 				// 人员ID
					String person_num = ms[2]; 				// 人员编号
					String person_name = ms[3]; 			// 人员姓名
					String proj_role = ms[4]; 				// 人员在项目中的角色(稼动率范畴)
					String work_startDate = ms[5]; 			// 人员进项目的开始时间(稼动率范畴)
					String work_endDate = ms[6]; 			// 人员进项目的结束时间(稼动率范畴)
					String normalHour = ms[7]; 				// 人员在项目中的作业状态(稼动率范畴)
					String weekendHour = ms[8]; 			// 人员在项目中的作业状态(稼动率范畴)
					String holidaysHour = ms[9]; 			// 人员在项目中的作业状态(稼动率范畴)
					String work_status = ms[10]; 			// 人员在项目中的作业状态(稼动率范畴)

					ProjectDevWorkTimeForm devWrokTimeForm = new ProjectDevWorkTimeForm();
					devWrokTimeForm.setPerson_id(person_id);
					devWrokTimeForm.setPerson_num(person_num);
					devWrokTimeForm.setPerson_name(person_name);
					devWrokTimeForm.setProj_id(project.getId());
					devWrokTimeForm.setProj_num(project.getProj_num());
					devWrokTimeForm.setProj_name(project.getProj_name());
					devWrokTimeForm.setProj_role(proj_role);
					devWrokTimeForm.setWork_startDate(DateUtils.formatYYYYMMDD(work_startDate));
					devWrokTimeForm.setWork_endDate(DateUtils.formatYYYYMMDD(work_endDate));
					devWrokTimeForm.setNormalHour(Float.parseFloat(normalHour));
					devWrokTimeForm.setWeekendHour(Float.parseFloat(weekendHour));
					devWrokTimeForm.setHolidaysHour(Float.parseFloat(holidaysHour));
					devWrokTimeForm.setWork_status(Integer.parseInt(work_status));

					if (null == workTimeId || "undefined".equals(workTimeId.trim()) || "".equals(workTimeId.trim())) {
						// 添加新的开发人员
						this.projectDevWorkTimeService.add(devWrokTimeForm);
					} else {
						// 修改已存在的人员
						devWrokTimeForm.setId(workTimeId);
						updateDevsWork(devWrokTimeForm);
					}
				}
			} else {
				this.basedaoProjectDevWorkTime.executeSQL("delete from ieasy_oa_project_dev_worktime where project_id=?", project.getId());
			}
		} catch (RuntimeException e) {
			LOG.error("修改开发人员作业周期发生异常", e);
			throw new RuntimeException("修改开发人员作业周期发生错误！");
		}
	}

	private void updateDevsWork(ProjectDevWorkTimeForm form) throws RuntimeException {
		try {
			if (null != form.getId() && !"".equals(form.getId().trim())) {
				String sql = "update ieasy_oa_project_dev_worktime set " + "proj_role=?, work_startDate=?, work_endDate=?, " + "normalHour=?, weekendHour=?, holidaysHour=?, " + "modifyDate=now(), modifyName=?, proj_num=?, work_status=? where id=?";

				this.basedaoProjectDevWorkTime.executeSQL(sql, new Object[] { form.getProj_role(), form.getWork_startDate(), form.getWork_endDate(), form.getNormalHour(), form.getWeekendHour(), form.getHolidaysHour(), this.getCurrentUser().getEmp_name(), form.getProj_num(), form.getWork_status(), form.getId() });
			}
		} catch (RuntimeException e) {
			LOG.error("修改已存在的开发人员作业周期发生异常", e);
			throw new RuntimeException("修改已存在的开发人员作业周期发生错误！");
		}
	}

	public ProjectCenterForm getProjectDetail(String project_id) {
		ProjectCenterForm form = new ProjectCenterForm() ;
		form.setId(project_id) ;
		return this.getProjectDetail(form) ;
	}
	@Override
	public ProjectCenterForm getProjectDetail(ProjectCenterForm form) {
		try {
			Map<String, Object> alias = new HashMap<String, Object>();
			String sql = "select " + 
					"p.id, p.proj_num, p.proj_name, p.proj_start_time, p.proj_end_time, p.proj_owner_dept_id, p.proj_owner_dept_name, " + 
					"p.proj_partake_dept_ids, p.proj_partake_dept_names, p.proj_level, " + 
					"p.proj_owner_id, p.proj_owner_name, p.proj_creator_id, p.proj_creator_name, p.proj_manager_ids, p.proj_manager_names, " + 
					"p.proj_viewer_member_ids, p.proj_viewer_member_names, p.proj_develop_engine_ids, p.proj_develop_engine_names, " + 
					"p.proj_testing_engine_ids, p.proj_testing_engine_names, p.proj_sqa_member_ids, p.proj_sqa_member_names, p.proj_operation_ids, p.proj_operation_names, " + 
					"p.proj_bak, p.proj_description, p.proj_attach_path, p.proj_status, p.proj_type, p.distinguish, " + 
					
					"p.proj_budget_amount1, p.proj_budget_amount2, p.proj_budget_amount3, p.proj_budget_amount4, " + 
					"p.proj_budget_amount5, p.proj_budget_amount6, p.proj_budget_amount7, p.proj_budget_amount8, p.proj_budget_amount9, " + 
					
					"p.proj_quot, p.proj_shouzhu, p.proj_zyfw, p.proj_gm, p.proj_buglv, p.proj_bjzry, p.proj_yjtrzry, p.proj_bjscx, p.proj_clrl, p.proj_cclrl, p.proj_ydscx, " + 
					"p.proj_htpjzt, p.proj_cwjszt, p.proj_cwydwcjssj, p.proj_gjjb, p.proj_xmjsnf, p.proj_cwwcjssj, p.proj_jxzt, " + 
					
					"p.createName " + "from ieasy_oa_project_center p " + "where 1=1";
			sql = addWhere(sql, form, alias);

			form = (ProjectCenterForm) this.basedaoProject.queryObjectSQL(sql, alias, ProjectCenterForm.class, false);

			/**
			 * 获取审批人员，及是否显示提交审批按钮
			 */
			StringBuffer bufStrPersonIds = new StringBuffer();
			StringBuffer bufStrPersonNames = new StringBuffer();
			
			LoginUser cu = this.getCurrentUser() ;
			// 判断当前用户是否在审批列表中,是否已审批,如果不在或已审批则不显示提交审批按钮
			List<ProjectApproveForm> approveList = this.basedaoProjectApprove.listSQL("select t.person_id, t.person_name, t.status from ieasy_oa_project_approve t where t.project_id=?", form.getId(), ProjectApproveForm.class, false);
			if (null != approveList && approveList.size() > 0) {
				for (ProjectApproveForm pa : approveList) {
					
					// 当前登陆人员的ID
					if(null != cu) {
						if (cu.getEmp_id().equals(pa.getPerson_id()) && pa.getStatus() == 0) {
							form.setSubmitApprove(true);
							bufStrPersonIds.append(pa.getPerson_id()).append(",");
							bufStrPersonNames.append(pa.getPerson_name()).append(",");
							continue;
						}
					}
					if (pa.getStatus() == 1) {
						bufStrPersonIds.append(pa.getPerson_id()).append(",");
						bufStrPersonNames.append("<font style=\"color:red\">" + pa.getPerson_name() + "</font>").append(",");
					} else {
						bufStrPersonIds.append(pa.getPerson_id()).append(",");
						bufStrPersonNames.append(pa.getPerson_name()).append(",");
					}
				}
			}
			
			if(null != bufStrPersonIds && !"".equals(bufStrPersonIds.toString())) {
				form.setProj_approve_person_ids(bufStrPersonIds.deleteCharAt(bufStrPersonIds.length() - 1).toString());
				form.setProj_approve_person_names(bufStrPersonNames.deleteCharAt(bufStrPersonNames.length() - 1).toString());
			}

			return form;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("获取项目信息失败", e);
			throw new RuntimeException("获取项目信息失败");
		}
	}

	@Override
	public List<Map<String, Object>> getProjectStatusCount(ProjectCenterForm form) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		Object[] param = null ;
		StringBuffer sqlBuf = new StringBuffer() ;
		sqlBuf.append("select p.proj_status, count(p.proj_status) status_count from ieasy_oa_project_center p ") ;
		
		//根据项目创建者的ID进行统计查询
		if(null!=form && null!=form.getProj_owner_id() && !"".equals(form.getProj_owner_id())) {
			sqlBuf.append("where p.proj_owner_id=? ") ;
			param = new Object[]{form.getProj_owner_id()} ;
		}
		
		sqlBuf.append("group by p.proj_status order by p.proj_status asc") ;
		
		try {
			maps = this.dbutil.getQr().query(sqlBuf.toString(), new MapListHandler(), param);
		} catch (SQLException e) {
			LOG.error("根据项目状态获取数量异常", e) ;
		}
		return maps;
	}
	
	@Override
	public List<Map<String, Object>> getProjectIdTypeCount(ProjectCenterForm form) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		Object[] param = null ;
		StringBuffer sqlBuf = new StringBuffer() ;
		sqlBuf.append("select p.distinguish, count(p.distinguish) count from ieasy_oa_project_center p GROUP BY p.distinguish") ;
		
		try {
			maps = this.dbutil.getQr().query(sqlBuf.toString(), new MapListHandler(), param);
		} catch (SQLException e) {
			LOG.error("根据项目状态获取数量异常", e) ;
		}
		return maps;
	}

	@Override
	public List<Map<String, Object>> getProjectShouZhuCount(ProjectCenterForm form) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		Object[] param = null ;
		StringBuffer sqlBuf = new StringBuffer() ;
		sqlBuf.append("select p.proj_shouzhu, count(p.proj_shouzhu) count from ieasy_oa_project_center p GROUP BY p.proj_shouzhu order by p.proj_shouzhu desc") ;
		
		
		try {
			maps = this.dbutil.getQr().query(sqlBuf.toString(), new MapListHandler(), param);
		} catch (SQLException e) {
			LOG.error("根据项目状态获取数量异常", e) ;
		}
		return maps;
	}

	@Override
	public List<Map<String, Object>> getProjectCWHSZTCount(ProjectCenterForm form) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		Object[] param = null ;
		StringBuffer sqlBuf = new StringBuffer() ;
		sqlBuf.append("select p.proj_cwjszt, count(p.proj_cwjszt) count from ieasy_oa_project_center p GROUP BY p.proj_cwjszt order by p.proj_cwjszt desc") ;
		
		try {
			maps = this.dbutil.getQr().query(sqlBuf.toString(), new MapListHandler(), param);
		} catch (SQLException e) {
			LOG.error("根据项目状态获取数量异常", e) ;
		}
		return maps;
	}

	@Override
	public DataGrid findProjectListByStatus(ProjectCenterForm form) {
		if (null == form.getSort()) {
			SystemContext.setSort("p.created");
		} else {
			SystemContext.setSort("p." + form.getSort());
			SystemContext.setOrder(form.getOrder());
		}
		Pager<ProjectCenterForm> pager = this.find(form);

		DataGrid dg = new DataGrid();
		dg.setTotal(pager.getTotal());
		dg.setRows(changeModel(pager.getDataRows()));

		return dg;
	}

	private List<ProjectCenterForm> changeModel(List<ProjectCenterForm> listforms) {
		List<ProjectCenterForm> list = new ArrayList<ProjectCenterForm>();
		if (null != listforms && listforms.size() > 0) {
			for (ProjectCenterForm pf : listforms) {
				//获取审批人员，及是否显示提交审批按钮
				StringBuffer bufStrPersonIds = new StringBuffer();
				StringBuffer bufStrPersonNames = new StringBuffer();
				// 当前登陆人员的ID
				String personId = this.getCurrentUser().getEmp_id();
				// 判断当前用户是否在审批列表中,是否已审批,如果不在或已审批则不显示提交审批按钮
				List<ProjectApproveForm> approveList = this.basedaoProjectApprove.listSQL("select t.person_id, t.person_name, t.status from ieasy_oa_project_approve t where t.project_id=?", pf.getId(), ProjectApproveForm.class, false);
				if (null != approveList && approveList.size() > 0) {
					for (ProjectApproveForm pa : approveList) {
						if (personId.equals(pa.getPerson_id()) && pa.getStatus() == 0) {
							pf.setSubmitApprove(true);
							bufStrPersonIds.append(pa.getPerson_id()).append(",");
							bufStrPersonNames.append(pa.getPerson_name()).append(",");
							continue;
						}
						if (pa.getStatus() == 1) {
							bufStrPersonIds.append(pa.getPerson_id()).append(",");
							bufStrPersonNames.append("<font color='red'>" + pa.getPerson_name() + "</font>").append(",");
						} else {
							bufStrPersonIds.append(pa.getPerson_id()).append(",");
							bufStrPersonNames.append(pa.getPerson_name()).append(",");
						}
					}
				}
				
				if(null != pf.getProj_start_time() && null != pf.getProj_end_time()) {
					pf.setCyc(DateUtils.getWorkingDay(pf.getProj_start_time(), pf.getProj_end_time())) ;
				}
				
				if(null != bufStrPersonIds && !"".equals(bufStrPersonIds.toString())) {
					pf.setProj_approve_person_ids(bufStrPersonIds.deleteCharAt(bufStrPersonIds.length() - 1).toString());
					pf.setProj_approve_person_names(bufStrPersonNames.deleteCharAt(bufStrPersonNames.length() - 1).toString());
				}

				list.add(pf);
			}
		}
		return list;
	}

	private Pager<ProjectCenterForm> find(ProjectCenterForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " + 
				"p.id, p.proj_num, p.proj_name, p.proj_start_time, p.proj_end_time, p.proj_owner_dept_id, " + 
				"p.proj_partake_dept_ids, p.proj_partake_dept_names, p.proj_level, " + 
				"p.proj_owner_id, p.proj_owner_name, p.proj_creator_id, p.proj_creator_name, p.proj_manager_ids, p.proj_manager_names, " + 
				"p.proj_viewer_member_ids, p.proj_viewer_member_names, p.proj_develop_engine_ids, p.proj_develop_engine_names, " + 
				"p.proj_testing_engine_ids, p.proj_testing_engine_names, p.proj_sqa_member_ids, p.proj_sqa_member_names, " + 
				"p.proj_bak, p.proj_description, p.proj_attach_path, p.proj_status, p.proj_type, p.distinguish, " + 
				
				"p.proj_budget_amount1, p.proj_budget_amount2, p.proj_budget_amount3, p.proj_budget_amount4, " + 
				"p.proj_budget_amount5, p.proj_budget_amount6, p.proj_budget_amount7, p.proj_budget_amount8, p.proj_budget_amount9, " + 
				
				"p.proj_quot, p.proj_shouzhu, p.proj_zyfw, p.proj_gm, p.proj_buglv, p.proj_bjzry, p.proj_yjtrzry, p.proj_bjscx, p.proj_clrl, p.proj_cclrl, p.proj_ydscx, " + 
				"p.proj_htpjzt, p.proj_cwjszt, p.proj_cwydwcjssj, p.proj_gjjb, p.proj_xmjsnf, p.proj_cwwcjssj, p.proj_jxzt, " + 
				
				"p.createName, " +
				"dept.name proj_owner_dept_name " +
				"from ieasy_oa_project_center p " +
				"left join ieasy_sys_org dept ON(dept.id=p.proj_owner_dept_id) " +
				"where 1=1";
		sql = addWhere(sql, form, alias);
		return this.basedaoProject.findSQL(sql, alias, ProjectCenterForm.class, false);
	}

	private String addWhere(String sql, ProjectCenterForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getId() != null && !"".equals(form.getId().trim())) {
				sql += " and p.id=:id";
				params.put("id", form.getId());
			}
			if (form.getProj_num() != null && !"".equals(form.getProj_num().trim())) {
				sql += " and p.proj_num like :proj_num";
				params.put("proj_num", "%%" + form.getProj_num() + "%%");
			}
			if (form.getProj_name() != null && !"".equals(form.getProj_name().trim())) {
				sql += " and p.proj_name like :proj_name";
				params.put("proj_name", "%%" + form.getProj_name() + "%%");
			}
			if (form.getProj_level() != null && !"".equals(form.getProj_level().trim())) {
				sql += " and p.proj_level like :proj_level";
				params.put("proj_level", "%%" + form.getProj_level() + "%%");
			}
			if (form.getProj_status() != null) {
				sql += " and p.proj_status=:proj_status";
				params.put("proj_status", form.getProj_status());
			}
			if (form.getProj_owner_dept_id() != null && !"".equals(form.getProj_owner_dept_id().trim())) {
				try {
					Map<String, Object> map = this.dbutil.getQr().query("select queryOrgChildren('"+form.getProj_owner_dept_id()+"') as child", new MapHandler()) ;
					String child = map.get("child").toString().replace("$,", "") ;
					sql += " and p.proj_owner_dept_id IN(:inIds)" ;
					params.put("inIds", child.trim().split(",")) ;
				} catch (SQLException e) {
					LOG.error("调用机构树状递归函数发生错误", e) ;
				}
			}
			if (form.getProj_owner_dept_name() != null && !"".equals(form.getProj_owner_dept_name().trim())) {
				sql += " and p.proj_owner_dept_name like :proj_owner_dept_name";
				params.put("proj_owner_dept_name", "%%" + form.getProj_owner_dept_name() + "%%");
			}
			if (form.getProj_owner_id() != null && !"".equals(form.getProj_owner_id().trim())) {
				sql += " and p.proj_owner_id=:proj_owner_id";
				params.put("proj_owner_id", form.getProj_owner_id());
			}
			if (form.getProj_owner_name() != null && !"".equals(form.getProj_owner_name().trim())) {
				sql += " and p.proj_owner_name like :proj_owner_name";
				params.put("proj_owner_name", "%%" + form.getProj_owner_name() + "%%");
			}
			if (form.getProj_viewer_member_ids() != null && !"".equals(form.getProj_viewer_member_ids().trim())) {
				sql += " and p.proj_viewer_member_ids like :proj_viewer_member_ids";
				params.put("proj_viewer_member_ids", "%%" + form.getProj_viewer_member_ids() + "%%");
			}
			if (form.getProj_viewer_member_names() != null && !"".equals(form.getProj_viewer_member_names().trim())) {
				sql += " and p.proj_viewer_member_names like :proj_viewer_member_names";
				params.put("proj_viewer_member_names", "%%" + form.getProj_viewer_member_names() + "%%");
			}
			if (form.getProj_manager_ids() != null && !"".equals(form.getProj_manager_ids().trim())) {
				sql += " and p.proj_manager_ids like :proj_manager_ids";
				params.put("proj_manager_ids", "%%" + form.getProj_manager_ids() + "%%");
			}
			if (form.getProj_manager_names() != null && !"".equals(form.getProj_manager_names().trim())) {
				sql += " and p.proj_manager_names like :proj_manager_names";
				params.put("proj_manager_names", "%%" + form.getProj_manager_names() + "%%");
			}
			if (form.getProj_develop_engine_ids() != null && !"".equals(form.getProj_develop_engine_ids().trim())) {
				sql += " and p.proj_develop_engine_ids like :proj_develop_engine_ids";
				params.put("proj_develop_engine_ids", "%%" + form.getProj_develop_engine_ids() + "%%");
			}
			if (form.getProj_develop_engine_names() != null && !"".equals(form.getProj_develop_engine_names().trim())) {
				sql += " and p.proj_develop_engine_names like :proj_develop_engine_names";
				params.put("proj_develop_engine_names", "%%" + form.getProj_develop_engine_names() + "%%");
			}
			if (form.getProj_testing_engine_ids() != null && !"".equals(form.getProj_testing_engine_ids().trim())) {
				sql += " and p.proj_testing_engine_ids like :proj_testing_engine_ids";
				params.put("proj_testing_engine_ids", "%%" + form.getProj_testing_engine_ids() + "%%");
			}
			if (form.getProj_testing_engine_names() != null && !"".equals(form.getProj_testing_engine_names().trim())) {
				sql += " and p.proj_testing_engine_names like :proj_testing_engine_names";
				params.put("proj_testing_engine_names", "%%" + form.getProj_testing_engine_names() + "%%");
			}
			if (form.getProj_sqa_member_ids() != null && !"".equals(form.getProj_sqa_member_ids().trim())) {
				sql += " and p.proj_sqa_member_ids like :proj_sqa_member_ids";
				params.put("proj_sqa_member_ids", "%%" + form.getProj_sqa_member_ids() + "%%");
			}
			if (form.getProj_sqa_member_names() != null && !"".equals(form.getProj_sqa_member_names().trim())) {
				sql += " and p.proj_sqa_member_names like :proj_sqa_member_names";
				params.put("proj_sqa_member_names", "%%" + form.getProj_sqa_member_names() + "%%");
			}
			if (form.getMy_dev_project_personId() != null && !"".equals(form.getMy_dev_project_personId().trim())) {
				try {
					//根据用户ID查询ieasy_oa_project_dev_worktime表，去除重复的project_id
					//在根据查询到的project_id 来 IN project
					List<Map<String, Object>> projectDevWorkList = this.dbutil.getQr().query("select DISTINCT t.project_id from ieasy_oa_project_dev_worktime t where t.person_id='"+form.getMy_dev_project_personId()+"'",  new MapListHandler()) ;
					List<Object> proj = new ArrayList<Object>() ;
					for (Map<String, Object> map : projectDevWorkList) {
						proj.add(map.get("project_id")) ;
					}
					if(null != projectDevWorkList) {
						if(projectDevWorkList.isEmpty()) {
							proj.add("") ;
						}
						sql += " and p.id IN(:inIds)" ;
						params.put("inIds", proj) ;
					}
				} catch (SQLException e) {
					LOG.error("根据用户ID查询ieasy_oa_project_dev_worktime表，异常", e) ;
				}
			}
			if (form.getDistinguish() != null && !"".equals(form.getDistinguish().trim())) {
				sql += " and p.distinguish=:distinguish";
				params.put("distinguish", form.getDistinguish());
			}
			if (form.getProj_htpjzt() != null && !"".equals(form.getProj_htpjzt().trim())) {
				sql += " and p.proj_htpjzt=:proj_htpjzt";
				params.put("proj_htpjzt", form.getProj_htpjzt());
			}
			if (form.getProj_shouzhu() != null && !"".equals(form.getProj_shouzhu().trim())) {
				sql += " and p.proj_shouzhu=:proj_shouzhu";
				params.put("proj_shouzhu", form.getProj_shouzhu());
			}
			if (form.getProj_jxzt() != null && !"".equals(form.getProj_jxzt().trim())) {
				sql += " and p.proj_jxzt=:proj_jxzt";
				params.put("proj_jxzt", form.getProj_jxzt());
			}
			if (form.getProj_cwjszt() != null && !"".equals(form.getProj_cwjszt().trim())) {
				sql += " and p.proj_cwjszt=:proj_cwjszt";
				params.put("proj_cwjszt", form.getProj_cwjszt());
			}
			
			// 按项目的开始日期进行范围查询
			if (form.getProj_begin_startDate() != null) {
				sql += " and date_format(p.proj_start_time,'%Y-%m-%d')>= date_format(:proj_begin_startDate,'%Y-%m-%d') ";
				params.put("proj_begin_startDate", form.getProj_begin_startDate());
			}
			if (form.getProj_begin_endDate() != null && !"".equals(form.getProj_begin_endDate())) {
				sql += " and date_format(p.proj_start_time,'%Y-%m-%d')<= date_format(:proj_begin_endDate,'%Y-%m-%d') ";
				params.put("proj_begin_endDate", form.getProj_begin_endDate());
			}
			// 按项目的结束日期进行范围查询
			if (form.getProj_finish_startDate() != null) {
				sql += " and date_format(p.proj_end_time,'%Y-%m-%d')>= date_format(:proj_finish_startDate,'%Y-%m-%d') ";
				params.put("proj_finish_startDate", form.getProj_finish_startDate());
			}
			if (form.getProj_finish_endDate() != null && !"".equals(form.getProj_finish_endDate())) {
				sql += " and date_format(p.proj_end_time,'%Y-%m-%d')<= date_format(:proj_finish_endDate,'%Y-%m-%d') ";
				params.put("proj_finish_endDate", form.getProj_finish_endDate());
			}
		}
		return sql;
	}

	@Override
	public void setProjectStatus(int status, String id) throws ServiceException {
		try {
			ProjectCenter entity = this.basedaoProject.load(ProjectCenter.class, id);
			entity.setProj_status(status);
			this.basedaoProject.update(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("修改项目状态异常");
		}
	}
	
	@Override
	public Msg changeProjectStatus(String projectId, Integer status) throws ServiceException {
		try {
			ProjectCenter entity = this.basedaoProject.load(ProjectCenter.class, projectId);
			entity.setProj_status(status);
			this.basedaoProject.update(entity);
			
			return new Msg(true, "状态已转换！") ;
		} catch (Exception e) {
			return new Msg(false, "状态转换失败！") ;
		}
	}

	@Override
	public Msg approve(String personId, String projectId) {
		Msg msg = new Msg(true, "审批成功") ;
		try {
			this.basedaoProjectApprove.updateByHql("update ProjectApprove set status=1 where person.id=? and projectCenter.id=?", new Object[] { personId, projectId });
			
			Map<String, Object> map = new HashMap<String, Object>() ;
			
			//查询审批列表中是否都有已审批了，如果都已审批，则讲项目的状态设置为（办理中2）
			List<ProjectApproveForm> approveList = this.basedaoProjectApprove.listSQL("select t.person_id, t.person_name, t.status from ieasy_oa_project_approve t where t.project_id=? and t.status=0", projectId, ProjectApproveForm.class, false);
			
			if(approveList.size() == 0) {
				//项目的审批列表中的人员都已审批，则将项目的状态设置为（办理中2）
				this.setProjectStatus(2, projectId) ;
				map.put("status", 2) ;
				
				/**开始设置定时器**************************/
				this.setProjectTimer(projectId) ;
			} else {
				//项目的审批列表中还有人未审批的，则将项目的状态设置为（审批中1）
				this.setProjectStatus(1, projectId) ;
				map.put("status", 1) ;
			}
			msg.setObj(map) ;
		} catch (Exception e) {
			return new Msg(false, "审批失败！");
		}
		return msg ;
	}
	
	@Override
	public Msg activated(String projectId) {
		Msg msg = new Msg(true, "激活成功") ;
		try {
			
			//修改项目状态
			this.setProjectStatus(2, projectId) ;
			
			//设置定时器
			this.setProjectTimer(projectId) ;
		} catch (Exception e) {
			return new Msg(false, "激活失败！");
		}
		return msg;
	}
	
	
	@Override
	public Msg sleeping(String projectId) {
		Msg msg = new Msg(true, "挂起成功") ;
		
		//记录退出的人员ID，用于邮件发送
		List<Map<String, String>> devList = new ArrayList<Map<String, String>>() ;
		Map<String, String> devMap = new HashMap<String, String>() ;
		
		try {
			//修改项目的结束时间，为今天日期
			this.dbutil.getQr().update("update ieasy_oa_project_center t set t.proj_end_time=date_format(NOW(),'%Y-%m-%d') where t.id='"+projectId+"'") ;
			
			//修改项目状态
			this.setProjectStatus(3, projectId) ;
			
			//删除开的人员定时器
			this.deleteProjectDevTime(projectId) ;
			//删除项目定时器
			this.deleteProjectTime(projectId) ;
			
			//项目挂起，先查询是否有人未退出项目的。如果有，获取人员的ID，并修改开发人员的工作状态为退出项目，和人员的工作状态为待机
			List<Map<String, Object>> map = this.dbutil.getQr().query("select t.id, t.person_id from ieasy_oa_project_dev_worktime t where t.work_status=1 and t.project_id='"+projectId+"'", new MapListHandler()) ;
			
			//修改开发人员的工作状态为退出项目，如果开发人员的结束时间大于今天，则将结束日期修改为今天的日期
			this.dbutil.getQr().update("update ieasy_oa_project_dev_worktime t set " +
					"t.work_status=0, " +
					"t.work_endDate=CASE WHEN date_format(t.work_endDate,'%Y-%m-%d')>date_format(NOW(),'%Y-%m-%d') THEN date_format(NOW(),'%Y-%m-%d') ELSE t.work_endDate END " +
					"where t.project_id=?", new Object[]{projectId}) ;
			//修改人员的工作状态为待机
			for (Map<String, Object> m : map) {
				devMap.put(m.get("id").toString(), m.get("person_id").toString()) ;
				this.dbutil.getQr().update("update ieasy_sys_person t set t.byProjectWorkStatus=1 where t.id='"+m.get("person_id")+"'") ;
			}
			
			
			devList.add(devMap) ;
			msg.setObj(devList) ;
		} catch (Exception e) {
			e.printStackTrace();
			return new Msg(false, "挂起失败！");
		}
		return msg;
	}
	
	@Override
	public Msg finish(String projectId) {
		Msg msg = new Msg(true, "已结束") ;
		//记录退出的人员ID，用于邮件发送
		List<Map<String, String>> devList = new ArrayList<Map<String, String>>() ;
		Map<String, String> devMap = new HashMap<String, String>() ;
				
		try {
			//修改项目的结束时间，为今天日期
			this.dbutil.getQr().update("update ieasy_oa_project_center t set t.proj_end_time=date_format(NOW(),'%Y-%m-%d') where t.id='"+projectId+"'") ;
			
			//修改状态
			this.setProjectStatus(4, projectId) ;
			
			//删除项目定时器
			this.deleteProjectTime(projectId) ;
			//删除开的人员定时器
			this.deleteProjectDevTime(projectId) ;
			
			//项目办结，先查询是否有人未退出项目的。如果有，获取人员的ID，并修改开发人员的工作状态为退出项目，和人员的工作状态为待机
			List<Map<String, Object>> map = this.dbutil.getQr().query("select t.id, t.person_id from ieasy_oa_project_dev_worktime t where t.work_status=1 and t.project_id='"+projectId+"'", new MapListHandler()) ;
			
			//修改开发人员的工作状态为退出项目，如果开发人员的结束时间大于今天，则将结束日期修改为今天的日期
			this.dbutil.getQr().update("update ieasy_oa_project_dev_worktime t set " +
					"t.work_status=0, " +
					"t.work_endDate=CASE WHEN date_format(t.work_endDate,'%Y-%m-%d')>date_format(NOW(),'%Y-%m-%d') THEN date_format(NOW(),'%Y-%m-%d') ELSE t.work_endDate END " +
					"where t.project_id=?", new Object[]{projectId}) ;
			//修改人员的工作状态为待机
			for (Map<String, Object> m : map) {
				devMap.put(m.get("id").toString(), m.get("person_id").toString()) ;
				this.dbutil.getQr().update("update ieasy_sys_person t set t.byProjectWorkStatus=1 where t.id='"+m.get("person_id")+"'") ;
			}
			
			devList.add(devMap) ;
			msg.setObj(devList) ;
		} catch (Exception e) {
			e.printStackTrace() ;
			return new Msg(false, "办结失败！");
		}
		return msg;
	}

	/**
	 * 项目审批，项目的创建者必须先自己审批后其他的审批人员才可以看到需审批的项目
	 * 通过proj_status项目状态来进行判断，如果为0则项目只是立项，其他审批人员无法看到项目
	 * 项目创建人提交审批后，proj_status为1或其他的状态，其他审批人员则可以看到项目
	 */
	@Override
	public DataGrid get_myApprove_datagrid(ProjectApproveForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "a.id approve_id, a.status approve_status, a.project_id id, " + 
					 "p.proj_name, p.proj_owner_dept_name, p.proj_level, p.proj_start_time, p.proj_end_time, p.proj_status " + 
					 "from ieasy_oa_project_approve a " +
					 "left join ieasy_oa_project_center p ON(p.id=a.project_id) " +
					 "where a.status=? and a.person_id=? and p.proj_status<>0" ;
		if(null != form) {
			if(form.getProj_num()!=null && !"".equals(form.getProj_num())) {
				sql += " and a.proj_num like :proj_num" ;
				alias.put("proj_num", "%%" + form.getProj_num() + "%%") ;
			}
			if(form.getProj_name()!=null && !"".equals(form.getProj_name())) {
				sql += " and a.proj_name like :proj_name" ;
				alias.put("proj_name", "%%" + form.getProj_name() + "%%") ;
			}
		}
		Pager<ProjectCenterForm> findSQL = this.basedaoProjectApprove.findSQL(sql, new Object[]{form.getStatus(), form.getPerson_id()}, alias, ProjectCenterForm.class, false) ;
		
		List<ProjectCenterForm> forms = new ArrayList<ProjectCenterForm>() ;
		List<ProjectCenterForm> dataRows = findSQL.getDataRows() ;
		for (ProjectCenterForm pf : dataRows) {
			if(null != pf.getProj_start_time() && null != pf.getProj_end_time()) {
				pf.setCyc(DateUtils.getWorkingDay(pf.getProj_start_time(), pf.getProj_end_time())) ;
			}
			forms.add(pf) ;
		}
		
		DataGrid dg = new DataGrid() ;
		dg.setRows(forms) ;
		dg.setTotal(findSQL.getTotal()) ;
		
		return dg;
	}

	
	/********************************************定时作业开始*********************************************************/
	/**
	 * 项目的定时器
	 * @param projectId
	 */
	public void setProjectTimer(String projectId) {
		ProjectCenterForm pcf = (ProjectCenterForm) this.basedaoProject.queryObjectSQL("select t.id, t.proj_name, t.proj_end_time, t.proj_num from ieasy_oa_project_center t where t.id=?", new Object[]{projectId}, ProjectCenterForm.class, false) ;
		if(null != pcf) {
			//项目邮件提醒，项目的结束时间-3天作为定时时间
			String date1 = notifyDate(pcf.getProj_end_time()) + " 23:30:30" ;
			//项目结束邮件，项目的结束时间作为定时的时间
			String date2 = DateUtils.formatYYYYMMDD(pcf.getProj_end_time()) + " 23:30:30" ;
			//将日期转换为cron表达式
			String timerCrom1 = DateUtils.convertDateToCron(date1) ;
			String timerCrom2 = DateUtils.convertDateToCron(date2) ;
			
			//LOG.debug("项目提醒，时间：" + date1 + "   " + timerCrom1);
			//LOG.debug("项目结束，时间：" + date2 + "   " + timerCrom2);
			
			//根据项目ID查询定时，看是否有该定时器，如果存在则删除
			deleteProjectTime(projectId);
			
			//新建（项目到期提前邮件提醒）定时器
			ScheduleTaskForm stf = new ScheduleTaskForm() ;
			stf.setTask_name("[项目提醒-项目作业时间即将到期]" + pcf.getProj_name()) ;
			stf.setTask_remark("[项目提醒-项目作业时间即将到期]编号："+pcf.getProj_num()+"，项目："+pcf.getProj_name()) ;
			stf.setTask_enable("Y") ;
			stf.setTask_datetime(DateUtils.formatYYYYMMDD_HHMMSS(date1)) ;
			stf.setCron_expression(timerCrom1) ;
			stf.setTask_job_class("com.ieasy.module.common.executors.ProjectJob") ;
			stf.setCreated(new Date()) ;
			stf.setCreateName("系统") ;
			ScheduleTaskForm saveStf = this.scheduleService.add(stf) ;
			
			if(saveStf != null) {
				//保存关联操作
				ScheduleTaskEntity ste = new ScheduleTaskEntity() ;
				ste.setId(saveStf.getId()) ;
				ScheduleLinkOperEntity sloe = new ScheduleLinkOperEntity() ;
				sloe.setLink1(projectId) ; 
				sloe.setLink2("提醒") ; 
				sloe.setLink3("PROJECT") ; 
				sloe.setSchedule(ste) ;
				this.basedaoScheduleLink.add(sloe) ;
			}
			
			//新建（项目到期邮件通知）定时器
			ScheduleTaskForm stf2 = new ScheduleTaskForm() ;
			stf2.setTask_name("[项目通知-项目作业时间已到期]" + pcf.getProj_name()) ;
			stf2.setTask_remark("[项目通知-项目作业时间已到期，项目状态变更为结束状态]编号："+pcf.getProj_num()+"，项目："+pcf.getProj_name()) ;
			stf2.setTask_enable("Y") ;
			stf2.setTask_datetime(DateUtils.formatYYYYMMDD_HHMMSS(date2)) ;
			stf2.setCron_expression(timerCrom2) ;
			stf2.setTask_job_class("com.ieasy.module.common.executors.ProjectJob") ;
			stf2.setCreated(new Date()) ;
			stf2.setCreateName("系统") ;
			ScheduleTaskForm saveStf2 = this.scheduleService.add(stf2) ;
			if(saveStf2 != null) {
				//保存关联操作
				ScheduleTaskEntity ste = new ScheduleTaskEntity() ;
				ste.setId(saveStf2.getId()) ;
				ScheduleLinkOperEntity sloe = new ScheduleLinkOperEntity() ;
				sloe.setLink1(projectId) ; 
				sloe.setLink2("退出") ;
				sloe.setLink3("PROJECT") ; 
				sloe.setSchedule(ste) ;
				this.basedaoScheduleLink.add(sloe) ;
			}
			
			
			setProjectDevTimer(projectId) ;
		}
		 
	}
	
	/**
	 * 根据项目ID查询定时，看是否有该定时器，如果存在则删除
	 * @param projectId
	 */
	public void deleteProjectTime(String projectId) {
		try {
			List<Map<String, Object>> mapLink = this.dbutil.getQr().query("select t.id, t.st_id from ieasy_sys_st_link t where t.link1=? and t.link3='PROJECT'", new MapListHandler(), new Object[]{projectId}) ;
			for (Map<String, Object> map : mapLink) {
				if (mapLink != null) {
					// 删除关联操作记录
					this.dbutil.getQr().update("delete from ieasy_sys_st_link where id=?", new Object[] { map.get("id") });
					// 删除定时
					this.scheduleService.delete(map.get("st_id").toString());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setProjectDevTimer(String projectId) {
		List<ProjectDevWorkTimeForm> devList = this.basedaoProjectDevWorkTime.listSQL("" +
				"select " +
				"t.person_id, t.person_num, t.person_name, t.project_id proj_id, t.proj_name, t.work_startDate, " +
				"t.work_endDate, t.work_status " +
				"from ieasy_oa_project_dev_worktime t " +
				"where t.project_id=? and t.work_status=1", 
				new Object[]{projectId}, ProjectDevWorkTimeForm.class, false) ;
		
		//根据人员ID和项目ID查询，看是否已有该定时器，有的话删除，在新建
		deleteProjectDevTime(projectId) ;
		
		if(null != devList && !devList.isEmpty()) {
			//用于备注
			StringBuffer info = new StringBuffer() ;
			info.append("-项目编号："+devList.get(0).getProj_name()+"#") ;
			
			//合并相同日期
			Set<String> dateGroup = new HashSet<String>() ;
			for (ProjectDevWorkTimeForm d : devList) {
				dateGroup.add(DateUtils.formatYYYYMMDD(d.getWork_endDate())) ;
				info.append(d.getPerson_num()+"-"+d.getPerson_name()+",");
			}
			
			for (String end_date : dateGroup) {
				//开发人员到期提醒，项目的结束时间-3天作为定时时间
				String date1 = notifyDate(DateUtils.formatYYYYMMDD(end_date)) + " 23:30:30" ;
				//开发人员到期时间，项目的结束时间作为定时的时间
				String date2 = end_date + " 23:30:30" ;
				//将日期转换为cron表达式
				String timerCrom1 = DateUtils.convertDateToCron(date1) ;
				String timerCrom2 = DateUtils.convertDateToCron(date2) ;
				
				//LOG.debug("生成定时器：[提醒-开发人员作业时间即将结束]，时间：" + date1 + "   " + timerCrom1);
				//LOG.debug("生成定时器：[通知-开发人员作业时间结束        ]，时间：" + date2 + "   " + timerCrom2);
				
				//新建（项目到期提前邮件提醒）定时器
				//[提醒-开发人员作业时间即将结束]
				ScheduleTaskForm stf = new ScheduleTaskForm() ;
				stf.setTask_name("[人员提醒-开发人员作业时间即将结束]") ;
				stf.setTask_remark("[人员提醒-开发人员作业时间即将结束]"+info) ;
				stf.setTask_enable("Y") ;
				stf.setTask_datetime(DateUtils.formatYYYYMMDD_HHMMSS(date1)) ;
				stf.setCron_expression(timerCrom1) ;
				stf.setTask_job_class("com.ieasy.module.common.executors.ProjectDevExitJob") ;
				stf.setCreated(new Date()) ;
				stf.setCreateName("系统") ;
				ScheduleTaskForm saveStf = this.scheduleService.add(stf) ;
				if(saveStf != null) {
					//保存关联操作
					ScheduleTaskEntity ste = new ScheduleTaskEntity() ;
					BeanUtils.copyNotNullProperties(saveStf, ste) ;
					
					ScheduleLinkOperEntity sloe = new ScheduleLinkOperEntity() ;
					sloe.setLink1(projectId) ;
					sloe.setLink2("DEV") ;
					sloe.setLink3("提醒") ;
					sloe.setSchedule(ste) ;
					this.basedaoScheduleLink.add(sloe) ;
				}
				
				
				//新建（项目到期邮件通知）定时器
				//[通知-开发人员作业时间结束]
				ScheduleTaskForm stf2 = new ScheduleTaskForm() ;
				stf2.setTask_name("[人员通知-开发人员作业时间结束]") ;
				stf2.setTask_remark("[人员通知-开发人员作业时间结束]") ;
				stf2.setTask_enable("Y") ;
				stf2.setTask_datetime(DateUtils.formatYYYYMMDD_HHMMSS(date2)) ;
				stf2.setCron_expression(timerCrom2) ;
				stf2.setTask_job_class("com.ieasy.module.common.executors.ProjectDevExitJob") ;
				stf2.setCreated(new Date()) ;
				stf2.setCreateName("系统") ;
				ScheduleTaskForm saveStf2 = this.scheduleService.add(stf2) ;
				if(saveStf2 != null) {
					//保存关联操作
					ScheduleTaskEntity ste = new ScheduleTaskEntity() ;
					BeanUtils.copyNotNullProperties(saveStf2, ste) ;
					ScheduleLinkOperEntity sloe = new ScheduleLinkOperEntity() ;
					sloe.setLink1(projectId) ;
					sloe.setLink2("DEV") ;
					sloe.setLink3("退出") ;
					sloe.setSchedule(ste) ;
					this.basedaoScheduleLink.add(sloe) ;
				}
			}
		}
	}
	
	/**
	 * 根据开发人员ID和项目ID查询，看是否已有该定时器，有的话删除，在新建
	 * 作业状态（1：进行中，0：已结束）
	 * @param d
	 */
	public void deleteProjectDevTime(String projectId) {
		try {
			List<Map<String, Object>> mapLink = this.dbutil.getQr().query("select t.id, t.st_id from ieasy_sys_st_link t where t.link1=? and t.link2='DEV'", new MapListHandler(), new Object[]{projectId}) ;
			for (Map<String, Object> map : mapLink) {
				if (mapLink != null) {
					// 删除关联操作记录
					this.dbutil.getQr().update("delete from ieasy_sys_st_link where id=?", new Object[] { map.get("id") });
					// 删除定时
					this.scheduleService.delete(map.get("st_id").toString());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 提醒日期
	 * 根据项目的结束日期来减3天，如果结束日期为星期一，则减4天
	 * @param date 项目的结束日期
	 * @return date
	 */
	private static String notifyDate(Date date) {
		int j = -3 ;
		int weekday = DateUtils.getWeekDay(date) ;
		if(weekday == 1) {
			j = -4 ;
		}
		return (DateUtils.formatYYYYMMDD(DateUtils.dateAdd(DateUtils.formatYYYYMMDD(date), j))) ; 
	}

	/**
	 * 刷新项目定时器
	 */
	@Override
	public void refProjectTimer(ProjectCenterForm form) {
		
		Map<String, Object> alias = new HashMap<String, Object>() ;
		
		String sql = "select p.id, p.proj_name from ieasy_oa_project_center p where 1=1 " ;
		sql = addWhere(sql, form, alias) ;
		
		List<ProjectCenterForm> pcf = this.basedaoProject.listSQL(sql, alias, ProjectCenterForm.class, false) ;
		for (ProjectCenterForm p : pcf) {
			this.deleteProjectTime(p.getId()) ;
			this.deleteProjectDevTime(p.getId()) ;
			this.setProjectTimer(p.getId()) ;
		}
	}
	
	
	
	/**************************************邮件数据-邮件发送**************************************************************************/
	/**
	 * 定时调用，项目即将到期提醒，项目结束通知
	 * @param taskCode
	 */
	public void sendEmailProjectInfoForTimer(String taskCode) {
		LOG.info("定时调用：【项目即将到期提醒，项目结束通知】，任务代码："+taskCode) ;
		try {
			Map<String, Object> taskMap = this.dbutil.getQr().query("select l.link1, l.link2 from ieasy_sys_st s left join ieasy_sys_st_link l ON(l.st_id=s.id) where s.task_code=?", new MapHandler(), new Object[]{taskCode}) ;
			if(null != taskMap) {
				Map<String,Object> model = new HashMap<String,Object>() ;
				
				String project_id = taskMap.get("link1").toString() ;
				String type = taskMap.get("link2").toString() ;
				String title = "提醒-项目作业时间即将到期" ;
				if(type.equals("退出")) {
					title = "通知-项目作业时间结束" ;
				}
				
				//项目信息
				ProjectCenterForm p = this.getProjectDetail(project_id) ;
				p.setProj_approve_person_names(HtmlUtil.HtmltoText(p.getProj_approve_person_names())) ;
				
				
				
				//开发人员，筛选出为退出项目的人员
				ProjectDevWorkTimeForm pdwf = new ProjectDevWorkTimeForm() ;
				pdwf.setProj_id(project_id) ;
				pdwf.setWork_status(1) ;
				List<ProjectDevWorkTimeForm> devList = this.projectDevWorkTimeService.listDev(pdwf) ;
				
				if(type.equals("退出")) {
					//设置项目的状态为退出状态
					this.finish(project_id) ;
					p.setProj_status(4) ;
				}
				
				//获取开发人员的邮件地址
				List<String> devPersonIds = new ArrayList<String>() ;
				for (ProjectDevWorkTimeForm d : devList) {
					devPersonIds.add(d.getPerson_id()) ;
				}
				//项目经理
				if(null != p.getProj_manager_ids() && p.getProj_manager_ids().length() > 0) {
					String[] split = p.getProj_manager_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//SQA人员
				if(null != p.getProj_sqa_member_ids() && p.getProj_sqa_member_ids().length() > 0) {
					String[] split = p.getProj_sqa_member_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//可查看人员（参与人员）
				if(null != p.getProj_viewer_member_ids() && p.getProj_viewer_member_ids().length() > 0) {
					String[] split = p.getProj_viewer_member_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//项目审批人员
				if(null != p.getProj_approve_person_ids() && p.getProj_approve_person_ids().length() > 0) {
					String[] split = p.getProj_approve_person_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//项目创建人
				devPersonIds.add(p.getProj_creator_id()) ;
				
				Map<String, Object> alias = new HashMap<String, Object>() ;
				alias.put("inIds", devPersonIds) ;
				List<Object[]> listSQL = this.basedaoPerson.listSQL("select t.email from ieasy_sys_person t where t.id IN(:inIds)", alias) ;
				/**结束******************************/
				
				
				
				//收件人地址（开发人员的邮件地址）
				String to = StringUtil.joinString(listSQL, ",") ;
				//抄送人地址
				String cc = to ;
				
				
				/**组装模板数据*********************/
				String html_name = p.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
				
				model.put("title", title) ;
				model.put("project", p) ;
				model.put("devList", devList) ;
				model.put("date", DateUtils.formatYYYYMMDD(new Date())) ;
				model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
				model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
				
				
				//邮件内容
				String templateToString = futil.templateToString(model, "/project.ftl") ;
				//生成HTML
				futil.generate(model, "/project.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
				
				MailVO mvo = new MailVO() ;
				mvo.setRecipientTO(to) ;
				mvo.setRecipientCC(cc) ;
				mvo.setSubject(title) ;
				mvo.setContent(templateToString) ;
				try {
					this.sendMessage.sendMail(mvo) ;
				} catch (Exception e) {
					LOG.error("邮件发送失败", e) ;
				}
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 定时调用,开发人员即将到期提醒,开发人员到期退出提醒
	 * @param taskCode
	 */
	public void sendEmailProjectDevInfoForTimer(String taskCode) {
		LOG.info("定时调用：【开发人员即将到期提醒,开发人员到期退出提醒】，任务代码："+taskCode) ;
		try {
			Map<String, Object> taskMap = this.dbutil.getQr().query("select l.link1, l.link2, l.link3 from ieasy_sys_st s left join ieasy_sys_st_link l ON(l.st_id=s.id) where s.task_code=?", new MapHandler(), new Object[]{taskCode}) ;
			if(null != taskMap) {
				Map<String,Object> model = new HashMap<String,Object>() ;
				
				String project_id = taskMap.get("link1").toString() ;
				String type = taskMap.get("link3").toString() ;
				String title = "提醒-开发人员作业时间即将到期" ;
				if(type.equals("退出")) {
					title = "通知-开发人员作业时间结束" ;
				}
				
				//今天日期
				String now = DateUtils.formatYYYYMMDD(new Date()) ;
				
				//项目信息
				ProjectCenterForm p = this.getProjectDetail(project_id) ;
				p.setProj_approve_person_names(HtmlUtil.HtmltoText(p.getProj_approve_person_names())) ;
				
				
				//开发人员，筛选出为退出项目的人员
				ProjectDevWorkTimeForm pdwf = new ProjectDevWorkTimeForm() ;
				pdwf.setProj_id(project_id) ;
				pdwf.setWork_status(1) ;
				List<ProjectDevWorkTimeForm> devList = this.projectDevWorkTimeService.listDev(pdwf) ;
				
				//提醒人员的名单
				List<ProjectDevWorkTimeForm> remind = new ArrayList<ProjectDevWorkTimeForm>() ;
				//通知结束人员的名单
				List<ProjectDevWorkTimeForm> notify = new ArrayList<ProjectDevWorkTimeForm>() ;
				//退出的开发人员ID，需修改状态
				List<String> exitMemberIds = new ArrayList<String>() ;
				
				if(type.equals("提醒")) {
					//即将到期人员
					for (ProjectDevWorkTimeForm d : devList) {
						LOG.debug(d.getPerson_name()+"=="+d.getWork_startDate()+"=="+d.getWork_endDate()+"--"+notifyDate(d.getWork_endDate())) ;
						//判断今天日期是否与结束日期减去3天后相等，相等则为即将到期人员
						if(now.equals(notifyDate(d.getWork_endDate()))) {
							remind.add(d) ;
						}
					}
				}
				
				if(type.equals("退出")) {
					//今天退出人员
					for (ProjectDevWorkTimeForm d : devList) {
						LOG.debug(d.getPerson_name()+"=="+d.getWork_startDate()+"=="+d.getWork_endDate()) ;
						//判断今天日期是否与结束日期相等，相等则为到期人员
						if(now.equals(DateUtils.formatYYYYMMDD(d.getWork_endDate()))) {
							notify.add(d) ;
							exitMemberIds.add("\'"+d.getPerson_id()+"\'") ;
						}
					}
				}
				
				//将今天退出的开发人员设置为退出状态，并将人员的状态设置为待机
				if(exitMemberIds.size() > 0) {
					LOG.debug("退出的人员："+StringUtil.joinString(exitMemberIds, ","));
					this.dbutil.getQr().update("update ieasy_oa_project_dev_worktime t set t.work_status=0 where t.person_id in("+StringUtil.joinString(exitMemberIds, ",")+")") ;
					this.dbutil.getQr().update("update ieasy_sys_person t set t.byProjectWorkStatus=1 where t.id in("+StringUtil.joinString(exitMemberIds, ",")+")") ;
				}
				
				/**获取开发人员的邮件地址*********************/
				List<String> devPersonIds = new ArrayList<String>() ;
				for (ProjectDevWorkTimeForm d : devList) {
					devPersonIds.add(d.getPerson_id()) ;
				}
				//项目经理
				if(null != p.getProj_manager_ids() && p.getProj_manager_ids().length() > 0) {
					String[] split = p.getProj_manager_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//SQA人员
				if(null != p.getProj_sqa_member_ids() && p.getProj_sqa_member_ids().length() > 0) {
					String[] split = p.getProj_sqa_member_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//可查看人员（参与人员）
				if(null != p.getProj_viewer_member_ids() && p.getProj_viewer_member_ids().length() > 0) {
					String[] split = p.getProj_viewer_member_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//项目审批人员
				if(null != p.getProj_approve_person_ids() && p.getProj_approve_person_ids().length() > 0) {
					String[] split = p.getProj_approve_person_ids().split(",") ;
					for (int i = 0; i < split.length; i++) {
						devPersonIds.add(split[i]) ;
					}
				}
				//项目创建人
				devPersonIds.add(p.getProj_creator_id()) ;
				
				Map<String, Object> alias = new HashMap<String, Object>() ;
				alias.put("inIds", devPersonIds) ;
				List<Object[]> listSQL = this.basedaoPerson.listSQL("select t.email from ieasy_sys_person t where t.id IN(:inIds)", alias) ;
				/**结束******************************/
				
				
				
				//收件人地址（开发人员的邮件地址）
				String to = StringUtil.joinString(listSQL, ",") ;
				//抄送人地址
				String cc = to ;
				
				
				/**组装模板数据*********************/
				String html_name = p.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
				
				model.put("title", title) ;
				model.put("project", p) ;
				model.put("devList", devList) ;
				model.put("remind", remind) ;
				model.put("notify", notify) ;
				model.put("date", DateUtils.formatYYYYMMDD(new Date())) ;
				model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
				model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
				
				//邮件内容
				String templateToString = futil.templateToString(model, "/dev_membert.ftl") ;
				//生成HTML
				futil.generate(model, "/dev_membert.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
				
				MailVO mvo = new MailVO() ;
				mvo.setRecipientTO(to) ;
				mvo.setRecipientCC(cc) ;
				mvo.setSubject(title) ;
				mvo.setContent(templateToString) ;
				try {
					this.sendMessage.sendMail(mvo) ;
				} catch (Exception e) {
					LOG.error("邮件发送失败", e) ;
				}
				
			}
		} catch (SQLException e) {
			LOG.error("邮件发送失败", e) ;
		}
	}
	
	
	
	/**
	 * 发送邮件，项目状态变更
	 * @param project_id
	 */
	public void sendMailByProjectStatus(String project_id, List<Map<String, String>> devIdsList) {
		Map<String,Object> model = new HashMap<String,Object>() ;
		
		String title = "通知-项目状态变更" ;
		
		//项目信息
		ProjectCenterForm p = this.getProjectDetail(project_id) ;
		
		//开发人员，筛选出为退出项目的人员
		ProjectDevWorkTimeForm pdwf = new ProjectDevWorkTimeForm() ;
		pdwf.setProj_id(project_id) ;
		pdwf.setWork_status(1) ;
		List<ProjectDevWorkTimeForm> devList = this.projectDevWorkTimeService.listDev(pdwf) ;
		
		/**获取人员的邮件地址*********************/
		List<String> devPersonIds = new ArrayList<String>() ;
		for (ProjectDevWorkTimeForm d : devList) {
			devPersonIds.add(d.getPerson_id()) ;
		}
		//项目经理
		if(null != p.getProj_manager_ids() && p.getProj_manager_ids().length() > 0) {
			String[] split = p.getProj_manager_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//SQA人员
		if(null != p.getProj_sqa_member_ids() && p.getProj_sqa_member_ids().length() > 0) {
			String[] split = p.getProj_sqa_member_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//可查看人员（参与人员）
		if(null != p.getProj_viewer_member_ids() && p.getProj_viewer_member_ids().length() > 0) {
			String[] split = p.getProj_viewer_member_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//项目审批人员
		if(null != p.getProj_approve_person_ids() && p.getProj_approve_person_ids().length() > 0) {
			String[] split = p.getProj_approve_person_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//项目创建人
		devPersonIds.add(p.getProj_creator_id()) ;
		
		//项目的状态为挂起或结束触发
		if(null != devIdsList && devIdsList.size() > 0) {
			for (Map<String, String> map : devIdsList) {
				for (String key : map.keySet()) {
					devPersonIds.add(map.get(key).toString()) ;
				}
			}
		}
		
		Map<String, Object> alias = new HashMap<String, Object>() ;
		alias.put("inIds", devPersonIds) ;
		List<Object[]> listSQL = this.basedaoPerson.listSQL("select t.email from ieasy_sys_person t where t.id IN(:inIds)", alias) ;
		/**结束******************************/
		
		
		
		/**项目的状态为挂起或结束触发**********************************/
		//退出人员
		List<ProjectDevWorkTimeForm> exitPerson = null ;
		
		if(null != devIdsList && devIdsList.size() > 0) {
			List<String> devIdsParams = new ArrayList<String>() ;
			for (Map<String, String> map : devIdsList) {
				for (String key : map.keySet()) {
					devIdsParams.add(key) ;
				}
			}
			Map<String, Object> alias1 = new HashMap<String, Object>() ;
			alias1.put("inIds", (devIdsParams.isEmpty()?"":devIdsParams)) ;
			exitPerson = this.basedaoPerson.listSQL("select t.person_name, t.work_startDate, t.work_endDate from ieasy_oa_project_dev_worktime t where t.id IN(:inIds)", alias1, ProjectDevWorkTimeForm.class, false) ;
		}
		/***结束***********************************************/
		
		
		//收件人地址（开发人员的邮件地址）
		String to = StringUtil.joinString(listSQL, ",") ;
		
		/**组装模板数据*********************/
		String html_name = p.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
		
		//参与人员
		String viwer = p.getProj_viewer_member_names() ;
		
		model.put("title", title) ;
		model.put("project", p) ;
		model.put("devList", devList) ;
		model.put("exitPerson", exitPerson) ;
		model.put("viwer", StringUtil.splitString((null != viwer?viwer:""), ",")) ;
		model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
		model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
		
		
		//邮件内容
		String templateToString = futil.templateToString(model, "/project_status.ftl") ;
		//生成HTML
		futil.generate(model, "/project_status.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
		
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO(to) ;
		mvo.setSubject(title) ;
		mvo.setContent(templateToString) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
		}
	}
	
	
	/**
	 * 发送邮件，项目时间变更
	 * @param project_id
	 */
	public void sendMailByProjectDate(String project_id) {
		Map<String,Object> model = new HashMap<String,Object>() ;
		
		String title = "通知-项目起止日期变更" ;
		
		//项目信息
		ProjectCenterForm p = this.getProjectDetail(project_id) ;
		
		//开发人员，筛选出为退出项目的人员
		ProjectDevWorkTimeForm pdwf = new ProjectDevWorkTimeForm() ;
		pdwf.setProj_id(project_id) ;
		pdwf.setWork_status(1) ;
		List<ProjectDevWorkTimeForm> devList = this.projectDevWorkTimeService.listDev(pdwf) ;
		
		/**获取开发人员的邮件地址*********************/
		List<String> devPersonIds = new ArrayList<String>() ;
		for (ProjectDevWorkTimeForm d : devList) {
			devPersonIds.add(d.getPerson_id()) ;
		}
		//项目经理
		if(null != p.getProj_manager_ids() && p.getProj_manager_ids().length() > 0) {
			String[] split = p.getProj_manager_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//SQA人员
		if(null != p.getProj_sqa_member_ids() && p.getProj_sqa_member_ids().length() > 0) {
			String[] split = p.getProj_sqa_member_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//可查看人员（参与人员）
		if(null != p.getProj_viewer_member_ids() && p.getProj_viewer_member_ids().length() > 0) {
			String[] split = p.getProj_viewer_member_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//项目审批人员
		if(null != p.getProj_approve_person_ids() && p.getProj_approve_person_ids().length() > 0) {
			String[] split = p.getProj_approve_person_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//项目创建人
		devPersonIds.add(p.getProj_creator_id()) ;
		
		Map<String, Object> alias = new HashMap<String, Object>() ;
		alias.put("inIds", devPersonIds) ;
		List<Object[]> listSQL = this.basedaoPerson.listSQL("select t.email from ieasy_sys_person t where t.id IN(:inIds)", alias) ;
		/**结束******************************/
		
		
		//收件人地址（开发人员的邮件地址）
		String to = StringUtil.joinString(listSQL, ",") ;
		
		/**组装模板数据*********************/
		String html_name = p.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
		
		//参与人员
		String viwer = p.getProj_viewer_member_names() ;
		
		model.put("title", title) ;
		model.put("project", p) ;
		model.put("devList", devList) ;
		model.put("viwer", StringUtil.splitString((null != viwer?viwer:""), ",")) ;
		model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
		model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
		
		
		//邮件内容
		String templateToString = futil.templateToString(model, "/project_status.ftl") ;
		//生成HTML
		futil.generate(model, "/project_status.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
		
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO(to) ;
		mvo.setSubject(title) ;
		mvo.setContent(templateToString) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
		}
	}
	
	
	/**
	 * 项目开发人员变更，发送邮件
	 * 人员日期变更（发送邮件）
	 * 人员退出（发送邮件）
	 * 人员新增（发送邮件）
	 * @param project_id
	 * @param sendStatus 发送的类型（exit人员退出，change（新增，日期变更））
	 */
	public void sendMailByProjectDevStatus(String project_id, String sendStatus, List<String> changeIds) {
		Map<String,Object> model = new HashMap<String,Object>() ;
		
		String title = "通知-开发人员变更" ;
		if("exit".equals(sendStatus)) {
			title = "通知-开发人员退出" ;
		}
		
		//项目信息
		ProjectCenterForm p = this.getProjectDetail(project_id) ;
		
		//开发人员，筛选出为退出项目的人员
		ProjectDevWorkTimeForm pdwf = new ProjectDevWorkTimeForm() ;
		pdwf.setProj_id(project_id) ;
		pdwf.setWork_status(1) ;
		List<ProjectDevWorkTimeForm> devList = this.projectDevWorkTimeService.listDev(pdwf) ;
		
		
		//变更的人员
		List<ProjectDevWorkTimeForm> changeMember = new ArrayList<ProjectDevWorkTimeForm>() ;
		Map<String, Object> alias1 = new HashMap<String, Object>() ;
		alias1.put("project_id", project_id) ;
		alias1.put("inIds", changeIds) ;
		changeMember = this.basedaoPerson.listSQL("select t.person_name, t.work_startDate, t.work_endDate from ieasy_oa_project_dev_worktime t where t.project_id=:project_id and t.id IN(:inIds)", alias1, ProjectDevWorkTimeForm.class, false) ;
		
		/**获取开发人员的邮件地址*********************/
		List<String> devPersonIds = new ArrayList<String>() ;
		for (ProjectDevWorkTimeForm d : devList) {
			devPersonIds.add(d.getPerson_id()) ;
		}
		//项目经理
		if(null != p.getProj_manager_ids() && p.getProj_manager_ids().length() > 0) {
			String[] split = p.getProj_manager_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//SQA人员
		if(null != p.getProj_sqa_member_ids() && p.getProj_sqa_member_ids().length() > 0) {
			String[] split = p.getProj_sqa_member_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//可查看人员（参与人员）
		if(null != p.getProj_viewer_member_ids() && p.getProj_viewer_member_ids().length() > 0) {
			String[] split = p.getProj_viewer_member_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//项目审批人员
		if(null != p.getProj_approve_person_ids() && p.getProj_approve_person_ids().length() > 0) {
			String[] split = p.getProj_approve_person_ids().split(",") ;
			for (int i = 0; i < split.length; i++) {
				devPersonIds.add(split[i]) ;
			}
		}
		//项目创建人
		devPersonIds.add(p.getProj_creator_id()) ;
		
		//变人员的邮件地址
		for (String changeId : changeIds) {
			devPersonIds.add(changeId) ;
		}
		
		Map<String, Object> alias = new HashMap<String, Object>() ;
		alias.put("inIds", devPersonIds) ;
		List<Object[]> listSQL = this.basedaoPerson.listSQL("select t.email from ieasy_sys_person t where t.id IN(:inIds)", alias) ;
		/**结束******************************/
		
		
		//收件人地址（开发人员的邮件地址）
		String to = StringUtil.joinString(listSQL, ",") ;
		//抄送人地址
		String cc = to ;
		
		/**组装模板数据*********************/
		String html_name = p.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
		
		//参与人员
		String viwer = p.getProj_viewer_member_names() ;
		
		model.put("title", title) ;
		model.put("project", p) ;
		model.put("changeInfo", ("exit".equals(sendStatus)?"退出人员":"变更人员")) ;
		model.put("changeMember", changeMember) ;
		model.put("devList", devList) ;
		model.put("viwer", StringUtil.splitString((null != viwer?viwer:""), ",")) ;
		model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
		model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
		
		
		//邮件内容
		String templateToString = futil.templateToString(model, "/project_dev_change.ftl") ;
		//生成HTML
		futil.generate(model, "/project_dev_change.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
		
		
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO(to) ;
		mvo.setRecipientCC(cc) ;
		mvo.setSubject(title) ;
		mvo.setContent(templateToString) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
		}
	}
	
	
	/**
	 * 发送邮件，事务通知，项目审批
	 */
	public void sendMailByApproveAffair(String[] person_ids, String project_id) {
		Map<String,Object> model = new HashMap<String,Object>() ;
		
		Map<String, Object> alias = new HashMap<String, Object>() ;
		alias.put("inIds", person_ids) ;
		List<Object[]> person_mails = this.basedaoPerson.listSQL("select t.email from ieasy_sys_person t where t.id IN(:inIds)", alias) ;
		ProjectCenterForm p = this.getProjectDetail(project_id) ;
		
		String to = StringUtils.join(person_mails, ",") ;
		System.out.println("审批人员Email："+to);
		
		
		/**组装模板数据*********************/
		String html_name = p.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
		
		model.put("title", "事务通知") ;
		model.put("project", p) ;
		model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
		model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
		
		//邮件内容
		String templateToString = futil.templateToString(model, "/approve_affair.ftl") ;
		//生成HTML
		futil.generate(model, "/approve_affair.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
				
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO(to) ;
		mvo.setSubject("事务通知") ;
		mvo.setContent(templateToString) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
		}
	}

	@Override
	public Msg noApproveSendMail(ProjectCenterForm form) {
		Map<String,Object> model = new HashMap<String,Object>() ;
		
		PersonEntity person = this.basedaoPerson.load(PersonEntity.class, form.getProj_creator_id()) ;
		ProjectCenter project = this.basedaoProject.load(ProjectCenter.class, form.getId()) ;
		
		/**组装模板数据*********************/
		String html_name = project.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
		
		model.put("title", "项目审批不通过") ;
		model.put("person", person) ;
		model.put("project", project) ;
		model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
		model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
		
		
		//邮件内容
		String templateToString = futil.templateToString(model, "/noApprove.ftl") ;
		//生成HTML
		futil.generate(model, "/noApprove.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
		
		
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO(person.getEmail()) ;
		mvo.setSubject("项目审批不通过") ;
		mvo.setContent(templateToString) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
			return new Msg(false, "邮件发送失败！");
		}
		return new Msg(true, "邮件已发送！");
	}

	@Override
	public Msg sendMailApprove(ProjectCenterForm form) {
		Map<String,Object> model = new HashMap<String,Object>() ;
		
		PersonEntity person = this.basedaoPerson.load(PersonEntity.class, form.getProj_creator_id()) ;
		ProjectCenter project = this.basedaoProject.load(ProjectCenter.class, form.getId()) ;
		
		String p_sql = "select p.email from ieasy_sys_person p left join ieasy_oa_project_approve ap ON(p.id=ap.person_id) where ap.project_id=? and ap.person_id<>?" ;
		List<PersonForm> pl = this.basedaoPerson.listSQL(p_sql, new Object[]{form.getId(), form.getProj_creator_id()}, PersonForm.class, false) ;
		StringBuffer tos = new StringBuffer() ;
		for (PersonForm p : pl) {
			tos.append(p.getEmail()+",") ;
		}
		
		
		/**组装模板数据*********************/
		String html_name = project.getProj_num()+"_"+System.currentTimeMillis()+".html" ;
		
		model.put("title", "项目数据已修改") ;
		model.put("person", person) ;
		model.put("project", project) ;
		model.put("domain", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath")) ;
		model.put("link", this.realPathResolver.getServerRoot()+ConfigUtil.get("contextPath") + this.outPath + "/" + html_name) ;
		
		
		//邮件内容
		String templateToString = futil.templateToString(model, "/approve_project_edit.ftl") ;
		//生成HTML
		futil.generate(model, "/approve_project_edit.ftl", this.realPathResolver.get(this.outPath)+"/"+html_name) ;
		
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO((tos.length()>0?tos.deleteCharAt(tos.length()-1).toString():"")) ;
		mvo.setRecipientCC(person.getEmail()) ;
		mvo.setSubject("项目数据已修改") ;
		mvo.setContent(templateToString) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
			return new Msg(false, "邮件发送失败！");
		}
		return new Msg(true, "邮件已发送！");
	}

}
