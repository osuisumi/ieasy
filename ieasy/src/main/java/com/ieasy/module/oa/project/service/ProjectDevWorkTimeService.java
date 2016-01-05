package com.ieasy.module.oa.project.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.NumberUtil;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.module.common.service.BaseService;
import com.ieasy.module.oa.project.entity.ProjectCenter;
import com.ieasy.module.oa.project.entity.ProjectDevWorkTime;
import com.ieasy.module.oa.project.web.form.ProjectDevWorkTimeForm;
import com.ieasy.module.system.entity.PersonEntity;

/**
 * 项目开发人员的作业起始时间和加班信息
 * 
 * @author Administrator
 * 
 */
@Service
@Transactional
public class ProjectDevWorkTimeService extends BaseService implements IProjectDevWorkTimeService {

	private Log log = LogFactory.getLog(getClass());
	
	@Inject
	private IDBUtilsHelper dbutil;

	@Inject
	private IBaseDao<ProjectDevWorkTime> basedaoProjectDevWorkTime;
	
	@Inject
	private IBaseDao<ProjectCenter> basedaoProject ;
	
	@Inject
	private IBaseDao<PersonEntity> basedaoPerson ;
	
	@Inject
	private IProjectCenterService projectService ;

	@Override
	public Msg add(ProjectDevWorkTimeForm form) {
		try {
			ProjectDevWorkTime entity = new ProjectDevWorkTime() ;
			BeanUtils.copyNotNullProperties(form, entity) ;
			
			if(form.getProj_id() != null) {
				entity.setProjectCenter(this.basedaoProject.load(ProjectCenter.class, form.getProj_id())) ;
			} else {
				return new Msg(false, "项目的ID不能为空") ;
			}
			
			if(form.getPerson_id() != null) {
				entity.setPerson(this.basedaoPerson.load(PersonEntity.class, form.getPerson_id())) ;
			}
			
			entity.setCreated(new Date()) ;
			entity.setCreateName(getCurrentUser().getEmp_name()) ;
			
			this.basedaoProjectDevWorkTime.add(entity) ;
			
			return new Msg(true, "添加成功");
		} catch (ServiceException e) {
			log.error("添加发生异常", e);
			return new Msg(false, "添加失败");
		}
	}

	@Override
	public Msg delete(ProjectDevWorkTimeForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					this.basedaoProjectDevWorkTime.delete(ProjectDevWorkTime.class, id) ;
				}
			}
			return new Msg(true, "删除成功");
		} catch (ServiceException e) {
			log.error("删除发生异常", e);
			return new Msg(false, "删除失败");
		}
	}
	
	@Override
	public Msg deleteByProjectId(String projectId) {
		try {
			this.basedaoProjectDevWorkTime.executeSQL("delete from ieasy_oa_project_dev_worktime where project_id=?", projectId) ;
			return new Msg(true, "删除成功");
		} catch (ServiceException e) {
			log.error("删除发生异常", e);
			return new Msg(false, "删除失败");
		}
	}

	
	/**
	 * 修改开发人员进入项目的开始和结束日期和加班信息
	 * 客户端传入开发人员进项目的开始和结束日期的数据形式，如下
	 * 作业周期ID，人员ID，人员编号，人员姓名，项目角色，作业开始时间，作业结束时间，平时加班，周末加班，节假日加班，作业状态|作业周期ID，人员ID，人员编号，人员姓名，项目角色，作业开始时间，作业结束时间，平时加班，周末加班，节假日加班，作业状态
	 * @param form
	 * @throws RuntimeException
	 */
	@Override
	public Msg update(ProjectDevWorkTimeForm form) {
		try {
			if(null != form.getId() && !"".equals(form.getId().trim())) {
				String sql = "update ieasy_oa_project_dev_worktime set " +
							 "proj_role=?, work_startDate=?, work_endDate=?, " +
							 "normalHour=?, weekendHour=?, holidaysHour=?, " +
							 "modifyDate=now(), modifyName=?, work_status=? where id=?" ;
				
				this.basedaoProjectDevWorkTime.executeSQL(sql, new Object[]{
						form.getProj_role(), form.getWork_startDate(), form.getWork_endDate(), 
						form.getNormalHour(), form.getWeekendHour(), form.getHolidaysHour(), 
						this.getCurrentUser().getEmp_name(), form.getWork_status(), form.getId()
				}) ;
			}

			return new Msg(true, "修改成功");
		} catch (ServiceException e) {
			log.error("修改发生异常", e);
			return new Msg(false, "修改失败");
		}
	}

	@Override
	public ProjectDevWorkTimeForm getProjectDetail(String id) {

		return null;
	}

	/**
	 * 计算人月
	 * 条件：开始日期~结束日期
	 * 每月的工作天数(排除周六日)
	 */
	private void handlerRY(ProjectDevWorkTimeForm pf) {
		Date s_d = pf.getWork_startDate() ;															//开始日期
		Date e_d = pf.getWork_endDate() ;															//结束日期
		
		int startYear = DateUtils.getYear(s_d) ;
		int endYear = DateUtils.getYear(e_d) ;
		int startMonth = DateUtils.getMonth(s_d) ;													//开始的月份
		int endMonth = DateUtils.getMonth(e_d) ;													//结束的月份
		
		float ry = 0f ;																				//人月累计结果
		float ext_ry = 0f ;																			//已消耗的人月
		
		log.debug(pf.getPerson_name());
		for(int i=startYear; i<=endYear; i++) {
			//相同年份
			if(startYear == endYear) {
				log.debug("同年===开始年：" + startYear + "开始月：" + startMonth + "==" + "结束年：" + endYear + "结束月：" + endMonth);
				for(int m=startMonth; m<=endMonth; m++) {
					String forMonth = startYear +"-"+ ((m<10)?"0"+m:m+"") +"-"+ "01" ;	
					Date firstDayMonth = DateUtils.getFirstDayOfMonth(forMonth) ;
					Date lastDayMonth = DateUtils.getLastDayOfMonth(forMonth) ;
					//每月的有效天数（排除周六日）
					float yxts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
					
					log.debug(DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==有效天数："+yxts);
					
					//开始月和结束月相等（只有一个月）
					if(m==startMonth && m==endMonth) {
						//工作天数（排除周六日）
						float gzts = ((Integer)DateUtils.getWorkingDay(s_d, e_d)).floatValue() ;
						ry += (gzts/yxts) ;
						
						//计算已消耗的人月
						if(DateUtils.compare_date(new Date(), e_d) == 0 || 1 == DateUtils.compare_date(new Date(), e_d)) {
							float cur_wd = ((Integer)DateUtils.getWorkingDay(s_d, e_d)).floatValue() ;
							ext_ry += (cur_wd/yxts) ;
						} else {
							float cur_wd = ((Integer)DateUtils.getWorkingDay(s_d, new Date())).floatValue() ;
							ext_ry += (cur_wd/yxts) ;
						}
						
						log.debug("\t实际日期：" + DateUtils.formatYYYYMMDD(s_d) + "==" + DateUtils.formatYYYYMMDD(e_d) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
					} else {
						//第一个月
						if(m==startMonth) {
							//工作天数（排除周六日）
							float gzts = ((Integer)DateUtils.getWorkingDay(s_d, lastDayMonth)).floatValue() ;
							ry += (gzts/yxts) ;
							
							//计算已消耗的人月,当前日期大于等于结束日期
							if(DateUtils.compare_date(new Date(), lastDayMonth) == 0 || 1 == DateUtils.compare_date(new Date(), lastDayMonth)) {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(s_d, lastDayMonth)).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							} else {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(s_d, new Date())).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							}
							
							log.debug("\t[第一个月]实际日期：" + DateUtils.formatYYYYMMDD(s_d) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						}
						//中间月
						if(m != startMonth && m != endMonth) {
							//工作天数（排除周六日）
							float gzts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
							ry += (gzts/yxts) ;
							
							//计算已消耗的人月,当前日期大于等于结束日期
							if(DateUtils.compare_date(new Date(), lastDayMonth) == 0 || 1 == DateUtils.compare_date(new Date(), lastDayMonth)) {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							} else {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(s_d, new Date())).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							}
							
							log.debug("\t[中间月]实际日期：" + DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						} 
						//最后一个月
						if(m == endMonth && m != startMonth) {
							//工作天数（排除周六日）
							float gzts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, e_d)).floatValue() ;
							ry += (gzts/yxts) ;
							
							//计算已消耗的人月,当前日期大于等于结束日期
							if(DateUtils.compare_date(new Date(), e_d) == 0 || 1 == DateUtils.compare_date(new Date(), e_d)) {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, e_d)).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							} else {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, new Date())).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							}
							
							log.debug("\t[最后一个月]实际日期：" + DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(e_d) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						}
					}
					
				}
			} else {	//不同年份
				if(i == startYear) {
					log.debug("不同年份#第一年===开始年：" + i + "开始月：" + startMonth + "==" + "结束年：" + endYear + "结束月：" + endMonth);
					for(int m=startMonth; m<=12; m++) {
						String forMonth = startYear +"-"+ ((m<10)?"0"+m:m+"") +"-"+ "01" ;	
						Date firstDayMonth = DateUtils.getFirstDayOfMonth(forMonth) ;
						Date lastDayMonth = DateUtils.getLastDayOfMonth(forMonth) ;
						//每月的有效天数（排除周六日）
						float yxts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
						
						log.debug(DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==有效天数："+yxts);
						
						//第一年的第一个月
						if(m==startMonth) {
							//工作天数（排除周六日）
							float gzts = ((Integer)DateUtils.getWorkingDay(s_d, lastDayMonth)).floatValue() ;
							ry += (gzts/yxts) ;
							
							//计算已消耗的人月,当前日期大于等于结束日期
							if(DateUtils.compare_date(new Date(), lastDayMonth) == 0 || 1 == DateUtils.compare_date(new Date(), lastDayMonth)) {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(s_d, lastDayMonth)).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							} else {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(s_d, new Date())).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							}
							
							log.debug("\t[第一年的第一个月]实际日期：" + DateUtils.formatYYYYMMDD(s_d) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						} else {
							//工作天数（排除周六日）
							float gzts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
							ry += (gzts/yxts) ;
							
							//计算已消耗的人月,当前日期大于等于结束日期
							if(DateUtils.compare_date(new Date(), lastDayMonth) == 0 || 1 == DateUtils.compare_date(new Date(), lastDayMonth)) {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							} else {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, new Date())).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							}
							
							log.debug("\t[中间月]实际日期：" + DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						} 
					}
				} else if(i != startYear && i != endYear) {
					log.debug("不同年份#中间年===开始年：" + i + "开始月：" + startMonth + "==" + "结束年：" + endYear + "结束月：" + endMonth);
					for(int m=1; m<=12; m++) {
						String forMonth = i +"-"+ ((m<10)?"0"+m:m+"") +"-"+ "01" ;	
						Date firstDayMonth = DateUtils.getFirstDayOfMonth(forMonth) ;
						Date lastDayMonth = DateUtils.getLastDayOfMonth(forMonth) ;
						//每月的有效天数（排除周六日）
						float yxts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
						
						log.debug(DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==有效天数："+yxts);
						
						//工作天数（排除周六日）
						float gzts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
						ry += (gzts/yxts) ;
						
						//计算已消耗的人月,当前日期大于等于结束日期
						if(DateUtils.compare_date(new Date(), lastDayMonth) == 0 || 1 == DateUtils.compare_date(new Date(), lastDayMonth)) {
							float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
							ext_ry += (cur_wd/yxts) ;
						} 
						
						log.debug("\t[中间年-中间月]实际日期：" + DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						
					}
				} else if(i == endYear) {
					log.debug("不同年份#最后年===开始年：" + i + "开始月：" + startMonth + "==" + "结束年：" + endYear + "结束月：" + endMonth);
					for(int m=1; m<=endMonth; m++) {
						String forMonth = i +"-"+ ((m<10)?"0"+m:m+"") +"-"+ "01" ;	
						Date firstDayMonth = DateUtils.getFirstDayOfMonth(forMonth) ;
						Date lastDayMonth = DateUtils.getLastDayOfMonth(forMonth) ;
						//每月的有效天数（排除周六日）
						float yxts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
						
						log.debug(DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==有效天数："+yxts);
						
						
						//最后一年的中间月
						if(m<endMonth) {
							//工作天数（排除周六日）
							float gzts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
							ry += (gzts/yxts) ;
							
							//计算已消耗的人月,当前日期大于等于结束日期
							if(DateUtils.compare_date(new Date(), lastDayMonth) == 0 || 1 == DateUtils.compare_date(new Date(), lastDayMonth)) {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, lastDayMonth)).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							} 
							
							log.debug("\t[最后一年的中间月]实际日期：" + DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(lastDayMonth) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						} else {
							//工作天数（排除周六日）
							float gzts = ((Integer)DateUtils.getWorkingDay(firstDayMonth, e_d)).floatValue() ;
							ry += (gzts/yxts) ;
							
							//计算已消耗的人月,当前日期大于等于结束日期
							if(DateUtils.compare_date(new Date(), e_d) == 0 || 1 == DateUtils.compare_date(new Date(), e_d)) {
								float cur_wd = ((Integer)DateUtils.getWorkingDay(firstDayMonth, e_d)).floatValue() ;
								ext_ry += (cur_wd/yxts) ;
							}
							
							log.debug("\t[最后一年的最后一个月]实际日期：" + DateUtils.formatYYYYMMDD(firstDayMonth) + "==" + DateUtils.formatYYYYMMDD(e_d) + "==工作天数：" + gzts + "==人月：" + (gzts/yxts));
						} 
					}
				}
				
			}
		}
		
		pf.setRy(NumberUtil.formatNum(ry)) ;
		pf.setExt_ry(NumberUtil.formatNum(ext_ry)) ;
	}
	
	@Override
	public DataGrid datagrid(ProjectDevWorkTimeForm form) {
		Map<String, Object> map = new HashMap<String, Object>() ;
		if(null == form.getSort()) {
			SystemContext.setSort("d.created") ;
		} else {
			SystemContext.setSort("d."+form.getSort()) ; SystemContext.setOrder(form.getOrder()) ;
		}
		try {
			List<ProjectDevWorkTimeForm> forms = new ArrayList<ProjectDevWorkTimeForm>() ;
			Pager<ProjectDevWorkTimeForm> pager = this.find(form) ;
			//根据工作状态
			Collections.sort(pager.getDataRows(), new Comparator<ProjectDevWorkTimeForm>() {  
	            public int compare(ProjectDevWorkTimeForm arg0, ProjectDevWorkTimeForm arg1) {  
	                int a1 = arg0.getWork_status();  
	                int a2 = arg1.getWork_status();  
	                if (a1 < a2) {  
	                    return 1;  
	                } else {  
	                    return -1;  
	                }  
	            }  
	        });
				
			int cycTotal = 0 ;								//开发周期
			int ext_cycTotal = 0 ;							//开发周期
			float total_ry = 0f ;							//统计人月
			float total_extRy = 0f ;						//统计已消耗人月
			float n = 0f ;									//平时加班
			float w = 0f ;									//周六日加班
			float h = 0f ;									//节假日加班
			float totalHour = 0f ;							//累计加班小时
			
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for(ProjectDevWorkTimeForm pf : pager.getDataRows()) {
					if(null != pf.getWork_startDate() && null != pf.getWork_endDate()) {
						//工作天数（排除周六日）
						int workingDay = DateUtils.getWorkingDay(pf.getWork_startDate(), pf.getWork_endDate()) ;
						
						n += pf.getNormalHour() ;
						w += pf.getWeekendHour() ;
						h += pf.getHolidaysHour() ;
						
						//已消耗的天数
						int ext_devCyc = 0 ;
						if(DateUtils.compare_date(new Date(), pf.getWork_endDate()) == 0 || 1 == DateUtils.compare_date(new Date(), pf.getWork_endDate())) {
							ext_devCyc = DateUtils.getWorkingDay(pf.getWork_startDate(), pf.getWork_endDate()) ;
						} else {
							ext_devCyc = DateUtils.getWorkingDay(pf.getWork_startDate(), new Date()) ;
						}
						
						//开发周期
						pf.setDevCyc(workingDay) ;
						//已消耗天数（消耗的开发周期）
						pf.setExt_devCyc(ext_devCyc) ;
						pf.setTotalHour(NumberUtil.formatNum(pf.getNormalHour() + pf.getWeekendHour() + pf.getHolidaysHour())) ;
						
						cycTotal += pf.getDevCyc() ;
						totalHour += pf.getTotalHour() ;
						ext_cycTotal += ext_devCyc ;
						
						//加班率
						pf.setJbl(NumberUtil.percent((pf.getNormalHour()+pf.getWeekendHour()+pf.getHolidaysHour())/(workingDay*8), 1));
						
						//处理人月
						this.handlerRY(pf) ;
						
						total_ry += pf.getRy() ;
						total_extRy += pf.getExt_ry() ;
					}
					forms.add(pf) ;
				}
			}
			
			//页脚
			map.put("devCyc", cycTotal) ;
			map.put("ext_devCyc", ext_cycTotal) ;
			map.put("normalHour", NumberUtil.formatNum(n)) ;
			map.put("weekendHour", NumberUtil.formatNum(w)) ;
			map.put("holidaysHour", NumberUtil.formatNum(h)) ;
			map.put("totalHour", NumberUtil.formatNum(totalHour)) ;
			/*总加班小时/(总工作天数*8)*/
			map.put("jbl", NumberUtil.percent(totalHour/(cycTotal*8), 1)) ;
			map.put("ry", NumberUtil.formatNum(total_ry)) ;
			map.put("ext_ry", NumberUtil.formatNum(total_extRy)) ;
			
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>() ;
			footerList.add(map) ;
			
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			dg.setFooter(footerList) ;
			
			return dg;
		} catch (Exception e) {
			log.error("查询项目开发人员起止日期信息失", e) ;
			throw new ServiceException("查询项目开发人员起止日期信息失", e) ;
		}
	}
	
	@Override
	public List<ProjectDevWorkTimeForm> listDev(ProjectDevWorkTimeForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "d.id, d.person_id, d.person_num, d.person_name, d.project_id as proj_id, d.proj_num, " +
					 "d.proj_name, d.proj_role, d.work_startDate, d.work_endDate, d.work_status, " +
					 "d.normalHour, d.weekendHour, d.holidaysHour, d.createName, d.created, d.modifyName, d.modifyDate, " +
					 "p.dbmType, p.dbmDate, p.lbmType, p.lbmDate, p.positionRecord, p.empState empStatus, " +
					 "pc.proj_quot, " +
					 "dept.name dept_name, " +
					 "pos.name positionName  " +
				 	 "from ieasy_oa_project_dev_worktime d " +
				 	 "left join ieasy_oa_project_center pc ON(pc.id=d.project_id) " +
				 	 "left join ieasy_sys_person p ON(p.id=d.person_id) " +
				 	 "left join ieasy_sys_org dept ON(dept.id=p.org_id) " +
				 	 "left join ieasy_sys_position pos ON(pos.id=p.position_id) " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		return this.basedaoProjectDevWorkTime.listSQL(sql, alias, ProjectDevWorkTimeForm.class, false) ;
	}
	
	private Pager<ProjectDevWorkTimeForm> find(ProjectDevWorkTimeForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "d.id, d.person_id, d.person_num, d.person_name, d.project_id as proj_id, d.proj_num, " +
					 "d.proj_name, d.proj_role, d.work_startDate, d.work_endDate, d.work_status, " +
					 "d.normalHour, d.weekendHour, d.holidaysHour, d.createName, d.created, d.modifyName, d.modifyDate, " +
					 "p.dbmType, p.dbmDate, p.lbmType, p.lbmDate, p.positionRecord, p.empState empStatus, " +
					 "pc.proj_quot, " +
					 "dept.name dept_name, " +
					 "pos.name positionName  " +
				 	 "from ieasy_oa_project_dev_worktime d " +
				 	 "left join ieasy_oa_project_center pc ON(pc.id=d.project_id) " +
				 	 "left join ieasy_sys_person p ON(p.id=d.person_id) " +
				 	"left join ieasy_sys_org dept ON(dept.id=p.org_id) " +
				 	 "left join ieasy_sys_position pos ON(pos.id=p.position_id) " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		return this.basedaoProjectDevWorkTime.findSQL(sql, alias, ProjectDevWorkTimeForm.class, false) ;
	}
	
	private String addWhere(String sql, ProjectDevWorkTimeForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getId() != null && !"".equals(form.getId().trim())) {
				sql += " and d.id=:id";
				params.put("id", form.getId());
			}
			if (form.getProj_id() != null && !"".equals(form.getProj_id().trim())) {
				sql += " and d.project_id=:project_id";
				params.put("project_id", form.getProj_id());
			}
			if (form.getPerson_id() != null && !"".equals(form.getPerson_id().trim())) {
				sql += " and d.person_id=:person_id";
				params.put("person_id", form.getPerson_id());
			}
			if (form.getWork_status() != null) {
				sql += " and d.work_status=:work_status";
				params.put("work_status", form.getWork_status());
			}
			//按开始日期进行范围查询
			if (form.getWork_begin_startDate() != null) {
				sql += " and date_format(d.work_startDate,'%Y-%m-%d')>= date_format(:work_begin_startDate,'%Y-%m-%d') ";
				params.put("work_start_time", form.getWork_begin_startDate());
			}
			if (form.getWork_begin_endDate() != null && !"".equals(form.getWork_begin_endDate())) {
				sql += " and date_format(d.work_startDate,'%Y-%m-%d')<= date_format(:work_start_time,'%Y-%m-%d') ";
				params.put("work_start_time", form.getWork_begin_endDate());
			}
			//按结束日期进行范围查询
			if (form.getWork_finish_startDate() != null) {
				sql += " and date_format(d.work_endDate,'%Y-%m-%d')>= date_format(:work_finish_startDate,'%Y-%m-%d') ";
				params.put("work_finish_startDate", form.getWork_finish_startDate());
			}
			if (form.getWork_finish_endDate() != null && !"".equals(form.getWork_finish_endDate())) {
				sql += " and date_format(d.work_endDate,'%Y-%m-%d')<= date_format(:work_finish_endDate,'%Y-%m-%d') ";
				params.put("work_finish_endDate", form.getWork_finish_endDate());
			}
		}
		return sql;
	}

	@Override
	public Msg exitProject(ProjectDevWorkTimeForm form) {
		try {
			//修改开发人员的工作状态为退出项目，如果开发人员的结束时间大于今天，则将结束日期修改为今天的日期
			this.dbutil.getQr().update("update ieasy_oa_project_dev_worktime t set " +
					"t.work_status=0, " +
					"t.work_endDate=CASE WHEN date_format(t.work_endDate,'%Y-%m-%d')>date_format(NOW(),'%Y-%m-%d') THEN date_format(NOW(),'%Y-%m-%d') ELSE t.work_endDate END " +
					"where t.id=? and t.project_id=?", new Object[]{form.getId(), form.getProj_id()}) ;
			
			//修改开发人员为退出项目状态
			//this.dbutil.getQr().update("update ieasy_oa_project_dev_worktime set work_startDate=?, work_endDate=?, work_status=0 where id=?", new Object[]{
			//		form.getWork_startDate(), new Date(), form.getId()
			//}) ;
			
			//修改人员的状态为待机状态
			this.dbutil.getQr().update("update ieasy_sys_person set byProjectWorkStatus=1 where id=?", new Object[]{
					form.getPerson_num()
			});
			return new Msg(true, "修改成功！") ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg(false, "修改失败！") ;
	}

	@Override
	public Msg modifySEDate(ProjectDevWorkTimeForm form) {
		try {
			//修改已退出人员的开始和结束时间
			this.dbutil.getQr().update("update ieasy_oa_project_dev_worktime set work_startDate=?, work_endDate=? where id=?", new Object[]{
					form.getWork_startDate(), form.getWork_endDate(), form.getId()
			}) ;
			return new Msg(true, "修改成功！") ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg(false, "修改失败！") ;
	}
	
	@Override
	public Msg deleteDevPerson(ProjectDevWorkTimeForm form) {
		try {
			//删除开发人员的数据
			this.dbutil.getQr().update("delete from ieasy_oa_project_dev_worktime where id=?", new Object[]{
					form.getId()
			}) ;
			//修改人员的状态为待机状态
			this.dbutil.getQr().update("update ieasy_sys_person set byProjectWorkStatus=1 where id=?", new Object[]{
					form.getPerson_id()
			});
			
			return new Msg(true, "删除成功！") ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg(false, "删除失败！") ;
	}

}
