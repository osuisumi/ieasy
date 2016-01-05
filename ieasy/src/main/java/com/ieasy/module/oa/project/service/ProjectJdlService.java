package com.ieasy.module.oa.project.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.NumberUtil;
import com.ieasy.basic.util.StringUtil;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.module.oa.project.entity.ProjectCenter;
import com.ieasy.module.oa.project.entity.ProjectDevWorkTime;
import com.ieasy.module.oa.project.web.form.ProjectDevWorkTimeForm;
import com.ieasy.module.oa.project.web.form.ProjectJdlForm;
import com.ieasy.module.system.entity.PersonEntity;
import com.ieasy.module.system.web.form.PersonForm;

/**
 * 计算人员的稼动率
 * 稼动率公式=（项目天数*系数）/21
 * 分母为可变的，根据到部门类型的时间或理部门类型的时间变动
 * 如：员工
 * 		到部门的类型为新增，到部门的时间为：2014-05-12
 * 		进入项目工作的时间周期为：2014-05-20~2014-07-06
 * 		离开部门时间：2014-07-22
 * 		公式计算结果：
 * 				项目工作天数（2014-05-20~2014-05-31=9天）* 系数（1） / 根据到部门类型的时间或理部门类型的时间变动，[2014-05-12~2014-05-31]=15天
 * 				5月份的稼动率：结果：(9*1)/15=0.6
 * 
 * 				项目工作天数（2014-06-01~2014-06-30=21天）* 系数（1） / 根据到部门类型的时间或理部门类型的时间变动，[2014-06-01~2014-06-30]=21天
 * 				6月份的稼动率：结果：(21*1)/21=1
 * 
 * 				项目工作天数（2014-07-01~2014-07-06=5天）* 系数（1） / 根据到部门类型的时间或理部门类型的时间变动，[2014-07-01~2014-07-22]=16天
 * 				7月份的稼动率：结果：(5*1)/16=0.3
 * @author Administrator
 *
 */
@Service @Transactional
public class ProjectJdlService implements IProjectJdlService {

	private Log LOG = LogFactory.getLog(ProjectJdlService.class) ;
	
	@Inject
	private IDBUtilsHelper dbutil;

	@Inject
	private IBaseDao<ProjectCenter> basedaoProject;

	@Inject
	private IBaseDao<PersonEntity> basedaoPerson;

	@Inject
	private IBaseDao<ProjectDevWorkTime> basedaoProjectDevWorkTime;

	@Override
	public DataGrid dagagrid(ProjectJdlForm form) {
		return this.jdlHandler(form) ;
	}
	
	/**
	 * 根据人员信息获取对于的项目工作周期
	 * 处理人员的工作周期形成稼动率
	 * @param form
	 * @return
	 */
	private DataGrid jdlHandler(ProjectJdlForm form) {
		//查询的年份，如果为空则查询当年
		int searchYear = (form.getSearchYear()>0?form.getSearchYear():DateUtils.getCurrentYear()) ;
		form.setSearchYear(searchYear) ;
		
		//返回客户的稼动率数据
		List<ProjectJdlForm> jdls = new ArrayList<ProjectJdlForm>() ;
		List<Map<String, Object>> footers = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> footerMap = new HashMap<String, Object>() ;
		footerMap.put("total_work_days", 0) ;
		footerMap.put("total_quot_work_days", 0) ;
		footerMap.put("m1", 0) ;
		footerMap.put("m2", 0) ;
		footerMap.put("m3", 0) ;
		footerMap.put("m4", 0) ;
		footerMap.put("m5", 0) ;
		footerMap.put("m6", 0) ;
		footerMap.put("m7", 0) ;
		footerMap.put("m8", 0) ;
		footerMap.put("m9", 0) ;
		footerMap.put("m10", 0) ;
		footerMap.put("m11", 0) ;
		footerMap.put("m12", 0) ;
		footerMap.put("annualJdl", 0) ;
		footerMap.put("bzts1", 0) ;
		footerMap.put("bzts2", 0) ;
		footerMap.put("bzts3", 0) ;
		footerMap.put("bzts4", 0) ;
		footerMap.put("bzts5", 0) ;
		footerMap.put("bzts6", 0) ;
		footerMap.put("bzts7", 0) ;
		footerMap.put("bzts8", 0) ;
		footerMap.put("bzts9", 0) ;
		footerMap.put("bzts10", 0) ;
		footerMap.put("bzts11", 0) ;
		footerMap.put("bzts12", 0) ;
		footerMap.put("totalBzts", 0) ;
		
		
		/**开始处理每个人员的稼动率***********************************************************************************/
		List<PersonForm> personList = this.find(form) ;
		for (PersonForm person : personList) {
			//开始组装人员稼动率数据
			ProjectJdlForm jdl = new ProjectJdlForm() ;
			jdl.setPerson_id(person.getId()) ;
			jdl.setPerson_num(person.getNum()) ;
			jdl.setPerson_name(person.getName()) ;
			jdl.setEmp_enter_date(person.getEnterDate()) ;
			jdl.setEmp_dimission_date(person.getDimissionDate()) ;
			jdl.setEmp_status(person.getEmpState()) ;
			jdl.setOrg_id(person.getOrg_id()) ;
			jdl.setOrg_name(person.getOrg_name()) ;
			jdl.setDbmType(person.getDbmType()) ;
			jdl.setDbmDate(person.getDbmDate()) ;
			jdl.setLbmType(person.getLbmType()) ;
			jdl.setLbmDate(person.getLbmDate()) ;
			jdl.setSearchYear(searchYear) ;
			
			/**
			 * 每月的标准工作天数
			 */
			showByMonthStandardDay(person, jdl, footerMap) ;
			
			/**
			 * 稼动率计算
			 */
			forPersonJdlHandler(person, jdl, footerMap) ;
			
			/**
			 * 个人年度稼动率
			 */
			sumAnnualJdl(person, jdl, footerMap) ;
			
			/**
			 * 统计页脚数据
			 */
			totalFooter(person, jdl, footerMap) ;
			
			
			jdls.add(jdl) ;										//组装好的数据添加到集合
		}
		/**结束处理每个人员的稼动率***********************************************************************************/
		
		
		
		//统计当前月份的稼动率，如：第一开发部，一月份所有人的工作天数总和/一月份的标准工作天数
		if(null != footerMap.get("m1") && ((Integer)footerMap.get("m1")) != 0) {
			footerMap.put("m1", NumberUtil.percent(((Integer)footerMap.get("m1")).floatValue() / ((Integer)footerMap.get("bzts1")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m2") && ((Integer)footerMap.get("m2")) != 0) {
			footerMap.put("m2", NumberUtil.percent(((Integer)footerMap.get("m2")).floatValue() / ((Integer)footerMap.get("bzts2")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m3") && ((Integer)footerMap.get("m3")) != 0) {
			footerMap.put("m3", NumberUtil.percent(((Integer)footerMap.get("m3")).floatValue() / ((Integer)footerMap.get("bzts3")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m4") && ((Integer)footerMap.get("m4")) != 0) {
			footerMap.put("m4", NumberUtil.percent(((Integer)footerMap.get("m4")).floatValue() / ((Integer)footerMap.get("bzts4")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m5") && ((Integer)footerMap.get("m5")) != 0) {
			footerMap.put("m5", NumberUtil.percent(((Integer)footerMap.get("m5")).floatValue() / ((Integer)footerMap.get("bzts5")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m6") && ((Integer)footerMap.get("m6")) != 0) {
			footerMap.put("m6", NumberUtil.percent(((Integer)footerMap.get("m6")).floatValue() / ((Integer)footerMap.get("bzts6")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m7") && ((Integer)footerMap.get("m7")) != 0) {
			footerMap.put("m7", NumberUtil.percent(((Integer)footerMap.get("m7")).floatValue() / ((Integer)footerMap.get("bzts7")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m8") && ((Integer)footerMap.get("m8")) != 0) {
			footerMap.put("m8", NumberUtil.percent(((Integer)footerMap.get("m8")).floatValue() / ((Integer)footerMap.get("bzts8")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m9") && ((Integer)footerMap.get("m9")) != 0) {
			footerMap.put("m9", NumberUtil.percent(((Integer)footerMap.get("m9")).floatValue() / ((Integer)footerMap.get("bzts9")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m10") && ((Integer)footerMap.get("m10")) != 0) {
			footerMap.put("m10", NumberUtil.percent(((Integer)footerMap.get("m10")).floatValue() / ((Integer)footerMap.get("bzts10")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m11") && ((Integer)footerMap.get("m11")) != 0) {
			footerMap.put("m11", NumberUtil.percent(((Integer)footerMap.get("m11")).floatValue() / ((Integer)footerMap.get("bzts11")).floatValue(), 0)) ;
		}
		if(null != footerMap.get("m12") && ((Integer)footerMap.get("m12")) != 0) {
			footerMap.put("m12", NumberUtil.percent(((Integer)footerMap.get("m12")).floatValue() / ((Integer)footerMap.get("bzts12")).floatValue(), 0)) ;
		}
		
		footers.add(footerMap) ;
		
		DataGrid dg = new DataGrid() ;
		dg.setRows(jdls) ;
		dg.setFooter(footers) ;
		return dg ;
	}
	
	/**
	 * 个人年度稼动率
	 * @param person
	 * @param jdl
	 */
	public void sumAnnualJdl(PersonForm person, ProjectJdlForm jdl, Map<String, Object> footerMap) {
		int totalBzts = jdl.getBzts1() + jdl.getBzts2() + jdl.getBzts3() + jdl.getBzts4() + jdl.getBzts5() + jdl.getBzts6() + jdl.getBzts7() + jdl.getBzts8() + jdl.getBzts9() + jdl.getBzts10() + jdl.getBzts11() + jdl.getBzts12();
		float annualJdl = (((Integer)jdl.getTotal_quot_work_days()).floatValue() / ((Integer)totalBzts).floatValue()) ;
		jdl.setAnnualJdl(NumberUtil.percent(annualJdl, 0)) ;
		//LOG.debug("个人年度稼动率：" + NumberUtil.percent(annualJdl, 0) + "==" +((Integer)jdl.getTotal_quot_work_days()).floatValue() + "/"+((Integer)totalBzts).floatValue() + "===" +annualJdl + "==" + totalBzts) ;
		
		//个人年总标准工作天数
		jdl.setTotalBzts(totalBzts) ;
	}
	
	/**
	 * 统计页脚数据
	 * @param person
	 * @param jdl
	 * @param footerMap
	 */
	public void totalFooter(PersonForm person, ProjectJdlForm jdl, Map<String, Object> footerMap) {
		
		if(null != footerMap.get("total_work_days")) footerMap.put("total_work_days", (((Integer)footerMap.get("total_work_days"))+jdl.getTotal_work_days())) ;
		if(null != footerMap.get("total_quot_work_days")) footerMap.put("total_quot_work_days", (((Integer)footerMap.get("total_quot_work_days"))+jdl.getTotal_quot_work_days())) ;
		
		//统计标准工作天数
		if(null != footerMap.get("totalBzts")) footerMap.put("totalBzts", (((Integer)footerMap.get("totalBzts"))+jdl.getTotalBzts())) ;
		
		//统计乘与系数后的工作天数
		if((Integer)footerMap.get("total_quot_work_days") != 0) {
			float totalAnnualJdl = ((Integer)footerMap.get("total_quot_work_days")).floatValue() / ((Integer)footerMap.get("totalBzts")).floatValue() ;
			if(null != footerMap.get("annualJdl")) footerMap.put("annualJdl", NumberUtil.percent(NumberUtil.formatNum(totalAnnualJdl), 0)) ;
		}
		
		
		//统计每月标准工作天数
		if(null != footerMap.get("bzts1")) footerMap.put("bzts1", (((Integer)footerMap.get("bzts1"))+jdl.getBzts1())) ;
		if(null != footerMap.get("bzts2")) footerMap.put("bzts2", (((Integer)footerMap.get("bzts2"))+jdl.getBzts2())) ;
		if(null != footerMap.get("bzts3")) footerMap.put("bzts3", (((Integer)footerMap.get("bzts3"))+jdl.getBzts3())) ;
		if(null != footerMap.get("bzts4")) footerMap.put("bzts4", (((Integer)footerMap.get("bzts4"))+jdl.getBzts4())) ;
		if(null != footerMap.get("bzts5")) footerMap.put("bzts5", (((Integer)footerMap.get("bzts5"))+jdl.getBzts5())) ;
		if(null != footerMap.get("bzts6")) footerMap.put("bzts6", (((Integer)footerMap.get("bzts6"))+jdl.getBzts6())) ;
		if(null != footerMap.get("bzts7")) footerMap.put("bzts7", (((Integer)footerMap.get("bzts7"))+jdl.getBzts7())) ;
		if(null != footerMap.get("bzts8")) footerMap.put("bzts8", (((Integer)footerMap.get("bzts8"))+jdl.getBzts8())) ;
		if(null != footerMap.get("bzts9")) footerMap.put("bzts9", (((Integer)footerMap.get("bzts9"))+jdl.getBzts9())) ;
		if(null != footerMap.get("bzts10")) footerMap.put("bzts10", (((Integer)footerMap.get("bzts10"))+jdl.getBzts10())) ;
		if(null != footerMap.get("bzts11")) footerMap.put("bzts11", (((Integer)footerMap.get("bzts11"))+jdl.getBzts11())) ;
		if(null != footerMap.get("bzts12")) footerMap.put("bzts12", (((Integer)footerMap.get("bzts12"))+jdl.getBzts12())) ;
		
	}
	
	
	/**
	 * 计算并显示每月的标准天数（每月的工作天数，排除周六日）
	 * 标准天数为可变，根据到部门或离部门类型，变动
	 * @param person
	 * @param jdl
	 */
	public void showByMonthStandardDay(PersonForm person, ProjectJdlForm jdl, Map<String, Object> footerMap) {
		int selectYear = jdl.getSearchYear() ;					//指定查询的年份
		
		int startMonth = 1 ;									//开始的月份
		int endMonth = 12 ;										//结束的月份
		
		Date startDate = null ;									//开始的日期
		Date endDate = null ;									//结束的日期
		
		//到部门
		if(null != person.getDbmType()) {
			String dbmType = person.getDbmType() ;
			Date dbmDate = person.getDbmDate() ;
			if(null != dbmDate) {
				Integer year = DateUtils.getYear(dbmDate) ;
				if(selectYear == year) {
					//到部门类型(1:在职,2:转入,3:新增,4:试用,5:停薪留职返回,6:返聘)
					if("转入".equals(dbmType) || "新增".equals(dbmType) || "试用".equals(dbmType) || "停薪留职返回".equals(dbmType) || "返聘".equals(dbmType)) {
						Integer month = DateUtils.getMonth(dbmDate) ;
						startMonth = month ;
						startDate = dbmDate ;
						//LOG.debug(dbmType + "==" + selectYear + "=="+ year +"=="+DateUtils.formatYYYYMMDD(dbmDate));
					}
				}
			}
		}
		
		//离部门
		if(null != person.getLbmType()) {
			String lbmType = person.getLbmType() ;
			Date lbmDate = person.getLbmDate() ;
			if(null != lbmDate) {
				Integer year = DateUtils.getYear(lbmDate) ;
				if(selectYear == year) {
					//离部门日期(1:转出-到开发部,2:转出-到非开发部,3:离职,4:停薪留职)
					if("转出-到非开发部".equals(lbmType) || "离职".equals(lbmType) || "停薪留职".equals(lbmType)) {
						Integer month = DateUtils.getMonth(lbmDate) ;
						endMonth = month ;
						endDate = lbmDate ;
						//LOG.debug(lbmType + "==" + selectYear + "=="+ year +"=="+DateUtils.formatYYYYMMDD(lbmDate));
					}
				}
			}
		}
		
		//根据startMonth和endMonth进行循环
		for(int i=startMonth; i<=endMonth; i++) {
			String forMonth = selectYear +"-"+ ((i<10)?"0"+i:i+"") +"-"+ "01" ;	
			int monthWorkDay = 0 ;
			
			if(null != startDate && null == endDate) {
				if(i == startMonth) {
					monthWorkDay = DateUtils.getWorkingDay(startDate, DateUtils.getLastDayOfMonth(forMonth)) ;
				} else {
					monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;
				}
			}
			
			if(null == startDate && null != endDate) {
				if(i == endMonth) {
					monthWorkDay = DateUtils.getWorkingDay(DateUtils.getFirstDayOfMonth(forMonth), endDate) ;
				} else {
					monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;
				}
			}
			
			if(null != startDate && null != endDate) {
				if(i == startMonth) {
					monthWorkDay = DateUtils.getWorkingDay(startDate, DateUtils.getLastDayOfMonth(forMonth)) ;
				}
				if(i > startMonth && i < endMonth) {
					monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;
				}
				if(i == endMonth) {
					monthWorkDay = DateUtils.getWorkingDay(DateUtils.getFirstDayOfMonth(forMonth), endDate) ;
				} 
			}
			
			if(null == startDate && null == endDate) {
				monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;
			}
			
			setMonthStandardDay(i, jdl, monthWorkDay) ;
		}
	}
	
	/**
	 * 人员稼动率计算
	 * @param person
	 * @param jdl
	 */
	private void forPersonJdlHandler(PersonForm person, ProjectJdlForm jdl, Map<String, Object> footerMap) {
		String sql = "select " +
				"t.person_num, t.person_name, t.work_startDate, t.work_endDate, t.project_id proj_id, t.proj_name, t.work_status, " +
				"p.proj_quot " +
				"from ieasy_oa_project_dev_worktime t " +
				"left join ieasy_oa_project_center p ON(p.id=t.project_id) " +
				"where t.person_id=?" ;
		List<ProjectDevWorkTimeForm> workTimeList = this.basedaoProjectDevWorkTime.listSQL(sql, new Object[]{person.getId()}, ProjectDevWorkTimeForm.class, false) ;
		//根据日期排序，从小到大
		Collections.sort(workTimeList, new Comparator<ProjectDevWorkTimeForm>() {  
            public int compare(ProjectDevWorkTimeForm arg0, ProjectDevWorkTimeForm arg1) {  
                long wsd = arg0.getWork_startDate().getTime();  
                long wed = arg1.getWork_startDate().getTime();  
                if (wsd > wed) {  
                    return 1;  
                } else {  
                    return -1;  
                }  
            }  
        });
		/**以上根据人员编号查询人员的所有工作时间***********************************************************************************************************/
		
		
		
		/**开始处理人员的工作时间***********************************************************************************************************/
		LOG.debug(person.getNum() + "," + person.getName() + ",年份：" + jdl.getSearchYear()+",到部门状态："+person.getDbmType()+"["+person.getDbmDate()+"],离部门状态："+person.getLbmType()+"["+person.getLbmDate()+"]") ;
		
		//人员每一个项目的工作天数累加
		int totalWorkDay = 0 ;
		//人员每一个项目的工作天数*系数
		int totalWorkDayQuot = 0 ;
		
		for (ProjectDevWorkTimeForm devWork : workTimeList) {
			
			Date startDate = devWork.getWork_startDate() ;			//工作的开始日期
			Date endDate = devWork.getWork_endDate() ;				//工作的结束日期
			int startYear = DateUtils.getYear(startDate) ;			//工作的开始年份
			int endYear = DateUtils.getYear(endDate) ;				//工作的结束年份
			int selectYear = jdl.getSearchYear() ;					//指定查询的年份
			
			//（年份排除）排除开始和结束年份都小于指定年份
			if(startYear<selectYear && endYear<selectYear) {
				continue ;
			}
			//（年份排除）排除开始日期大于指定年份
			if(startYear>selectYear) {
				continue ;
			}
			
			
			
			/**
			 * 根据判断是否重置工作的开始和结束日期
			 * 1、如果工作开始年份小于指定年份，则重置工作开始日期为1月1号，等于则按原日期。
			 * 2、如果工作结束年份大于指定年份，则重置工作结束日期为指定年份的最后一天，等于则按原日期
			 */
			if(startYear<selectYear) {
				startDate = DateUtils.getFirstDayOfYear(selectYear) ;
			}
			if(endYear>selectYear) {
				endDate = DateUtils.getLastDayOfYear(selectYear) ;
			} 
			
			int proj_workday = DateUtils.getWorkingDay(startDate, endDate) ;
			int proj_workdayQuot = NumberUtil.setScale(DateUtils.getWorkingDay(startDate, endDate) * devWork.getProj_quot()) ;
			
			//累加每年项目工作天数
			totalWorkDay += proj_workday ;
			//累加每年项目工作天数*系数（四舍五入取整）
			totalWorkDayQuot += proj_workdayQuot ;
			
			
			LOG.debug("\t原始日期：" + devWork.getWork_startDate() + "==" + devWork.getWork_endDate() + 
					  "\t重置日期：" + DateUtils.formatYYYYMMDD(startDate) + "==" + DateUtils.formatYYYYMMDD(endDate) +
					  "\t项目总天数：" + proj_workday + "==系数后天总天数：" + proj_workdayQuot +
					  "\t状态：" + devWork.getWork_status() +
					  "\t系数[" + devWork.getProj_quot() + "]," + devWork.getProj_name());
			
			/**
			 * *********************************************************************************************************************
			 * 开始设置1~12月的稼动率数据（根据开始月份~结束月份）
			 */
			int startMonth = DateUtils.getMonth(startDate) ;											//开始的月份
			int endMonth = DateUtils.getMonth(endDate) ;												//结束的月份
			
			for(int i=startMonth; i<=endMonth; i++) {
				float sum_jdl = 0 ;																		//计算稼动率(项目工作天数*系数)/每月标准工作天数
				int workday = 0 ;
				float workdayQuot = 0 ;
				
				String forMonth = selectYear +"-"+ ((i<10)?"0"+i:i+"") +"-"+ "01" ;						//组装日期，每月的1月1号
				
				Date firstDayOfMonth = DateUtils.getFirstDayOfMonth(forMonth) ;							//第一天
				Date lastDayOfMonth = DateUtils.getLastDayOfMonth(forMonth) ;							//最后一天
				
				
				//第一个月
				if(i == startMonth && i != endMonth) {
					String sd = DateUtils.formatYYYYMMDD(startDate) ;
					String ed = DateUtils.formatYYYYMMDD(lastDayOfMonth) ;
					workday = DateUtils.getWorkingDay(sd, ed) ;											//工作天数
					workdayQuot = workday * devWork.getProj_quot() ;									//系数后的工作天数（四舍五入取整）
					int monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;							//当月（足月）的标准工作天数
					
					//根据人员的状态判断是否重置当月的标准工作天数
					if(null != person.getDbmType() && !"".equals(person.getDbmType())) {
						String dbmType = person.getDbmType() ;
						Date dbmDate = person.getDbmDate() ;
						if("转入".equals(dbmType) || "新增".equals(dbmType) || "试用".equals(dbmType) || "停薪留职返回".equals(dbmType) || "返聘".equals(dbmType)) {
							//LOG.debug((selectYear+"-"+i)+"==="+(DateUtils.getYear(dbmDate)+"-"+DateUtils.getMonth(dbmDate)));
							if((selectYear+"-"+i).equals((DateUtils.getYear(dbmDate)+"-"+DateUtils.getMonth(dbmDate)))) {
								//重置-当月（足月）的标准工作天数，（到部门的开始日期~到最后一天）
								monthWorkDay = DateUtils.getWorkingDay(dbmDate, DateUtils.getLastDayOfMonth(dbmDate)) ;
							}
						}
					}
					
					
					sum_jdl = (workdayQuot/monthWorkDay) ;							//计算稼动率(项目工作天数*系数)/每月标准工作天数
					
					LOG.debug("\t\t第一个月    ：" + sd +"=="+ ed +"\t工作天数："+ workday +"   乘与系数后工作天数："+ workdayQuot + "   当月标准工作天数："+monthWorkDay + "\t稼动率：("+workday+"*"+devWork.getProj_quot()+")/"+monthWorkDay+"="+sum_jdl) ;
				}
				//中间的月份
				if(i > startMonth && i < endMonth) {
					String sd = DateUtils.formatYYYYMMDD(firstDayOfMonth) ;
					String ed = DateUtils.formatYYYYMMDD(lastDayOfMonth) ;
					workday = DateUtils.getWorkingDay(sd, ed) ;											//工作天数
					workdayQuot = workday * devWork.getProj_quot() ;									//系数后的工作天数（四舍五入取整）
					int monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;							//当月（足月）的标准工作天数
					
					
					sum_jdl = (workdayQuot/((Integer)monthWorkDay).floatValue()) ;												//计算稼动率(项目工作天数*系数)/每月标准工作天数
					
					LOG.debug("\t\t中间的月份：" + sd +"=="+ ed +"\t工作天数："+ workday +"   乘与系数后工作天数："+ workdayQuot + "   当月标准工作天数："+monthWorkDay + "\t稼动率：("+workday+"*"+devWork.getProj_quot()+")/"+monthWorkDay+"="+sum_jdl) ;
				}
				//最后一个月
				if(i != startMonth && i == endMonth) {
					String sd = DateUtils.formatYYYYMMDD(firstDayOfMonth) ;
					String ed = DateUtils.formatYYYYMMDD(endDate) ;
					workday = DateUtils.getWorkingDay(sd, ed) ;											//工作天数
					workdayQuot = workday * devWork.getProj_quot() ;									//系数后的工作天数（四舍五入取整）
					int monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;							//当月（足月）的标准工作天数
					
					
					//根据人员的状态判断是否重置当月的标准工作天数
					if(null != person.getLbmType() && !"".equals(person.getLbmType())) {
						String lbmType = person.getLbmType() ;
						Date lbmDate = person.getLbmDate() ;
						if("转出-到非开发部".equals(lbmType) || "离职".equals(lbmType) || "停薪留职".equals(lbmType)) {
							//LOG.debug((selectYear+"-"+i)+"==="+(DateUtils.getYear(lbmDate)+"-"+DateUtils.getMonth(lbmDate)));
							if((selectYear+"-"+i).equals((DateUtils.getYear(lbmDate)+"-"+DateUtils.getMonth(lbmDate)))) {
								//重置-当月（足月）的标准工作天数，（到部门的1号~到离部门日期）
								monthWorkDay = DateUtils.getWorkingDay(firstDayOfMonth, lbmDate) ;
							}
						}
					}
					
					sum_jdl = (workdayQuot/monthWorkDay) ;												//计算稼动率(项目工作天数*系数)/每月标准工作天数
					
					LOG.debug("\t\t最后一个月：" + sd +"=="+ ed +"\t工作天数："+ workday +"   乘与系数后工作天数："+ workdayQuot + "   当月标准工作天数："+monthWorkDay + "\t稼动率：("+workday+"*"+devWork.getProj_quot()+")/"+monthWorkDay+"="+sum_jdl) ;
				}
				//开始和结束在同一个月
				if(i == startMonth && i == endMonth) {
					String sd = DateUtils.formatYYYYMMDD(startDate) ;
					String ed = DateUtils.formatYYYYMMDD(endDate) ;
					workday = DateUtils.getWorkingDay(sd, ed) ;											//工作天数
					workdayQuot = workday * devWork.getProj_quot() ;				//系数后的工作天数（四舍五入取整）
					int monthWorkDay = DateUtils.getMonthWorkDay(forMonth) ;							//当月（足月）的标准工作天数
					
					/**开始重置标准工作天数************/
					//根据人员的状态判断是否重置当月的标准工作天数（条件：到部门类型 != null  && 离部门类型 ==null）
					if((null != person.getDbmType() && !"".equals(person.getDbmType())) && null == person.getLbmType()) {
						String dbmType = person.getDbmType() ;
						Date dbmDate = person.getDbmDate() ;
						if("转入".equals(dbmType) || "新增".equals(dbmType) || "试用".equals(dbmType) || "停薪留职返回".equals(dbmType) || "返聘".equals(dbmType)) {
							if((selectYear+"-"+i).equals((DateUtils.getYear(dbmDate)+"-"+DateUtils.getMonth(dbmDate)))) {
								//重置-当月（足月）的标准工作天数，（到部门的开始日期~到最后一天）
								monthWorkDay = DateUtils.getWorkingDay(dbmDate, DateUtils.getLastDayOfMonth(dbmDate)) ;
							}
						}
					}
					//根据人员的状态判断是否重置当月的标准工作天数（条件：离部门类型 != null  && 到部门类型 ==null）
					if((null != person.getLbmType() && !"".equals(person.getLbmType())) && null == person.getDbmType()) {
						String lbmType = person.getLbmType() ;
						Date lbmDate = person.getLbmDate() ;
						if("转出-到非开发部".equals(lbmType) || "离职".equals(lbmType) || "停薪留职".equals(lbmType)) {
							if((selectYear+"-"+i).equals((DateUtils.getYear(lbmDate)+"-"+DateUtils.getMonth(lbmDate)))) {
								//重置-当月（足月）的标准工作天数，（到部门的1号~到离部门日期）
								monthWorkDay = DateUtils.getWorkingDay(firstDayOfMonth, lbmDate) ;
							}
						}
					}
					//根据人员的状态判断是否重置当月的标准工作天数（条件：离部门类型 != null  && 到部门类型 ==null）
					if((null != person.getDbmType() && !"".equals(person.getDbmType())) && (null != person.getLbmType() && !"".equals(person.getLbmType()))) {
						String dbmType = person.getDbmType() ;
						Date dbmDate = person.getDbmDate() ;
						
						String lbmType = person.getLbmType() ;
						Date lbmDate = person.getLbmDate() ;
						
						if(("转入".equals(dbmType) || "新增".equals(dbmType) || "试用".equals(dbmType) || "停薪留职返回".equals(dbmType) || "返聘".equals(dbmType)) && ("转出-到非开发部".equals(lbmType) || "离职".equals(lbmType) || "停薪留职".equals(lbmType))) {
							if((selectYear+"-"+i).equals((DateUtils.getYear(dbmDate)+"-"+DateUtils.getMonth(dbmDate)))&&(selectYear+"-"+i).equals((DateUtils.getYear(lbmDate)+"-"+DateUtils.getMonth(lbmDate)))) {
								//重置-当月（足月）的标准工作天数，（到部门的1号~到离部门日期）
								monthWorkDay = DateUtils.getWorkingDay(dbmDate, lbmDate) ;
							}
						}
					}
					/**结束重置标准工作天数************/
					
					sum_jdl = (workdayQuot/monthWorkDay) ;							//计算稼动率(项目工作天数*系数)/每月标准工作天数
					
					
					LOG.debug("\t\t开始和结束在同一个月：" + sd +"=="+ ed +"\t工作天数："+ workday +"   乘与系数后工作天数："+ workdayQuot + "   当月标准工作天数："+monthWorkDay + "\t稼动率：("+workday+"*"+devWork.getProj_quot()+")/"+monthWorkDay+"="+sum_jdl) ;
				}
				
				setMonthData(i, jdl, sum_jdl, workdayQuot, footerMap) ;
			}
			/**********结束设置1~12月的稼动率数据***********************************************************************************************************************************************************/
			
		}
		jdl.setTotal_work_days(totalWorkDay) ;
		jdl.setTotal_quot_work_days(totalWorkDayQuot) ;
		
		jdl.setM1(NumberUtil.formatNum(jdl.getM1())) ;
		jdl.setM2(NumberUtil.formatNum(jdl.getM2())) ;
		jdl.setM3(NumberUtil.formatNum(jdl.getM3())) ;
		jdl.setM4(NumberUtil.formatNum(jdl.getM4())) ;
		jdl.setM5(NumberUtil.formatNum(jdl.getM5())) ;
		jdl.setM6(NumberUtil.formatNum(jdl.getM6())) ;
		jdl.setM7(NumberUtil.formatNum(jdl.getM7())) ;
		jdl.setM8(NumberUtil.formatNum(jdl.getM8())) ;
		jdl.setM9(NumberUtil.formatNum(jdl.getM9())) ;
		jdl.setM10(NumberUtil.formatNum(jdl.getM10())) ;
		jdl.setM11(NumberUtil.formatNum(jdl.getM11())) ;
		jdl.setM12(NumberUtil.formatNum(jdl.getM12())) ;
		
		/**结束处理人员的工作时间***********************************************************************************************************/
		
	}
	
	/**
	 * 填充每月的稼动率数据
	 * @param month
	 */
	private void setMonthData(int month, ProjectJdlForm jdl, float sum_jdl, float workday, Map<String, Object> footerMap) {
		switch (month) {
		case 1: 
			jdl.setM1(jdl.getM1()+sum_jdl) ;
			
			//统计当前月份的稼动率，如：第一开发部，一月份所有人的工作天数总和/一月份的标准工作天数
			if(null != footerMap.get("m1")) footerMap.put("m1", (((Integer)footerMap.get("m1"))+((Float)workday).intValue())) ;
			break;
		case 2: 
			jdl.setM2(jdl.getM2()+sum_jdl) ;
			if(null != footerMap.get("m2")) footerMap.put("m2", (((Integer)footerMap.get("m2"))+((Float)workday).intValue())) ;
			break;
		case 3: 
			jdl.setM3(jdl.getM3()+sum_jdl) ;
			if(null != footerMap.get("m3")) footerMap.put("m3", (((Integer)footerMap.get("m3"))+((Float)workday).intValue())) ;
			break;
		case 4: 
			jdl.setM4(jdl.getM4()+sum_jdl) ;
			if(null != footerMap.get("m4")) footerMap.put("m4", (((Integer)footerMap.get("m4"))+((Float)workday).intValue())) ;
			break;
		case 5: 
			jdl.setM5(jdl.getM5()+sum_jdl) ;
			if(null != footerMap.get("m5")) footerMap.put("m5", (((Integer)footerMap.get("m5"))+((Float)workday).intValue())) ;
			break;
		case 6: 
			jdl.setM6(jdl.getM6()+sum_jdl) ;
			if(null != footerMap.get("m6")) footerMap.put("m6", (((Integer)footerMap.get("m6"))+((Float)workday).intValue())) ;
			break;
		case 7: 
			jdl.setM7(jdl.getM7()+sum_jdl) ;
			if(null != footerMap.get("m7")) footerMap.put("m7", (((Integer)footerMap.get("m7"))+((Float)workday).intValue())) ;
			break;
		case 8: 
			jdl.setM8(jdl.getM8()+sum_jdl) ;
			if(null != footerMap.get("m8")) footerMap.put("m8", (((Integer)footerMap.get("m8"))+((Float)workday).intValue())) ;
			break;
		case 9: 
			jdl.setM9(jdl.getM9()+sum_jdl) ;
			
			if(null != footerMap.get("m9")) footerMap.put("m9", (((Integer)footerMap.get("m9"))+((Float)workday).intValue())) ;
			break;
		case 10: 
			jdl.setM10(jdl.getM10()+sum_jdl) ;
			if(null != footerMap.get("m10")) footerMap.put("m10", (((Integer)footerMap.get("m10"))+((Float)workday).intValue())) ;
			break;
		case 11: 
			jdl.setM11(jdl.getM11()+sum_jdl) ;
			if(null != footerMap.get("m11")) footerMap.put("m11", (((Integer)footerMap.get("m11"))+((Float)workday).intValue())) ;
			break;
		case 12: 
			jdl.setM12(jdl.getM12()+sum_jdl) ;
			if(null != footerMap.get("m12")) footerMap.put("m12", (((Integer)footerMap.get("m12"))+((Float)workday).intValue())) ;
			break;
		default:
			break;
		}
	}
	
	/**
	 * 填充每月的标准天数
	 * @param month
	 */
	private void setMonthStandardDay(int month, ProjectJdlForm jdl, int monthWorkDay) {
		switch (month) {
		case 1: 
			jdl.setBzts1(monthWorkDay) ;
			break;
		case 2: 
			jdl.setBzts2(monthWorkDay) ;
			break;
		case 3: 
			jdl.setBzts3(monthWorkDay) ;
			break;
		case 4: 
			jdl.setBzts4(monthWorkDay) ;
			break;
		case 5: 
			jdl.setBzts5(monthWorkDay) ;
			break;
		case 6: 
			jdl.setBzts6(monthWorkDay) ;
			break;
		case 7: 
			jdl.setBzts7(monthWorkDay) ;
			break;
		case 8: 
			jdl.setBzts8(monthWorkDay) ;
			break;
		case 9: 
			jdl.setBzts9(monthWorkDay) ;
			break;
		case 10: 
			jdl.setBzts10(monthWorkDay) ;
			break;
		case 11: 
			jdl.setBzts11(monthWorkDay) ;
			break;
		case 12: 
			jdl.setBzts12(monthWorkDay) ;
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> get_jdl_dept_report(ProjectJdlForm form) {
		Map<String, Object> data = new HashMap<String, Object>() ;
		
		List<Map<String, Object>> jdlList = new ArrayList<Map<String, Object>>() ;
		List<Map<String, Object>> reportList = new ArrayList<Map<String, Object>>() ;
		
		if(null != form && !"".equals(form.getDept_ids().trim())) {
			String[] dept_ids = form.getDept_ids().split(",") ;
			for (String dept_id : dept_ids) {
				//设置条件
				form.setOrg_id(dept_id) ;//部门
				//不计算稼动率的人员ID,form.getExclude_person_ids(),form中有值
				
				try {
					//取得部门名称
					Map<String, Object> org = this.dbutil.getQr().query("select id, name from ieasy_sys_org where id=?", new MapHandler(), new Object[]{dept_id}) ;
					
					/**
					 * datagrid
					 */
					DataGrid d = this.dagagrid(form) ;
					List<Map<String, Object>> footer = (List<Map<String, Object>>) d.getFooter() ;
					Map<String, Object> map = footer.get(0) ;
					map.put("org_id", org.get("id")) ;
					map.put("org_name", org.get("name")) ;
					jdlList.add(map) ;
					data.put("rows", jdlList) ;
					
					
					/**
					 * 图表
					 */
					Map<String, Object> reportMap = new HashMap<String, Object>() ;
					reportMap.put("name", org.get("name")) ;
					int[] obj = new int[]{
							Integer.parseInt(map.get("m1").toString().replace("%", "")), 
							Integer.parseInt(map.get("m2").toString().replace("%", "")), 
							Integer.parseInt(map.get("m3").toString().replace("%", "")), 
							Integer.parseInt(map.get("m4").toString().replace("%", "")), 
							Integer.parseInt(map.get("m5").toString().replace("%", "")), 
							Integer.parseInt(map.get("m6").toString().replace("%", "")), 
							Integer.parseInt(map.get("m7").toString().replace("%", "")), 
							Integer.parseInt(map.get("m8").toString().replace("%", "")), 
							Integer.parseInt(map.get("m9").toString().replace("%", "")), 
							Integer.parseInt(map.get("m10").toString().replace("%", "")), 
							Integer.parseInt(map.get("m11").toString().replace("%", "")), 
							Integer.parseInt(map.get("m12").toString().replace("%", ""))
					} ;
					reportMap.put("data", obj);
					reportList.add(reportMap) ;
					data.put("report_data", reportList) ;
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return data ;
	}
	
	/**
	 * 查询人员信息
	 * @param form
	 * @return
	 */
	private List<PersonForm> find(ProjectJdlForm form) {
		if(null != form.getSort()) {
			String[] personSorts = new String[]{"num", "name", "sex", "archivesStatus", "empState"};
			if(StringUtil.arrayToString(personSorts).contains(form.getSort())) {
				SystemContext.setSort("e."+form.getSort()) ; SystemContext.setOrder(form.getOrder()) ;
			}
		}
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "e.id, e.name, e.num, e.sex, e.birth, "+
					 "e.empState, e.empType, e.empLevel, e.enterDate, e.dimissionDate, e.changeDate, " +
				 	 "e.dbmType, e.dbmDate, e.lbmType, e.lbmDate, " +
					 "o.id as org_id, o.name as org_name, " +
					 "position.id as position_id, position.name as position_name " +
				 	 "from ieasy_sys_person e " +
				 	 "left join ieasy_sys_org o ON(e.org_id=o.id) " +
				 	 "left join ieasy_sys_position position ON(e.position_id=position.id) " +
				 	 "where 1=1 and e.num<>? ";
		
		//如果查询的年份为当前年，排除人员离职日期小于当前年，如今年为2014，离职日期为2013年的不考虑
		/*if(form.getSearchYear()== DateUtils.getCurrentYear()) {
			sql += " AND (DATE_FORMAT(e.dimissionDate,'%Y')=:dd OR e.dimissionDate IS NULL)" ;
			alias.put("dd", DateUtils.getCurrentYear()) ;
		}*/
		
		//条件,入职时间小于等于指定年份,离职时间等于指定年份
		if(form.getSearchYear()!=-1) {
			sql += " AND ((DATE_FORMAT(e.enterDate,'%Y')<=:ed AND e.dimissionDate IS NULL) OR DATE_FORMAT(e.dimissionDate,'%Y')=:dd)" ;
			alias.put("dd", form.getSearchYear()) ;
			alias.put("ed", form.getSearchYear()) ;
		}
		
		sql = addWhere(sql, form, alias) ;
		return this.basedaoPerson.listSQL(sql, new Object[]{"ROOT"}, alias, PersonForm.class, false) ;
	}
	
	private String addWhere(String sql, ProjectJdlForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getPerson_id() != null && !"".equals(form.getPerson_id().trim())) {
				sql += " and e.id =:person_id";
				params.put("person_id", form.getPerson_id().trim());
			}
			if (form.getPerson_num() != null && !"".equals(form.getPerson_num().trim())) {
				sql += " and e.num like :num";
				params.put("num", "%%" + form.getPerson_num().trim() + "%%");
			}
			if (form.getPerson_name() != null && !"".equals(form.getPerson_name().trim())) {
				sql += " and e.name like :name";
				params.put("name", "%%" + form.getPerson_name().trim() + "%%");
			}
			//生产部门稼动率
			if (form.isProduct_dept_jdl()) {
				try {
					//需排除的人员ID
					List<String> exclude_person_ids = new ArrayList<String>() ;
					if(null != form.getExclude_person_ids() && !"".equals(form.getExclude_person_ids().trim())) {
						exclude_person_ids = Arrays.asList(form.getExclude_person_ids().split(","));
					}
					
					//查询生产部门的所有人员ID
					List<Object> ids = new ArrayList<Object>() ;
					List<Map<String, Object>> person_ids = this.dbutil.getQr().query("" +
							"select p.id from ieasy_sys_person p where p.org_id in(" +
							"	select o.id from ieasy_sys_org o where o.id IN(" +
							"		SELECT cc.id FROM `ieasy_sys_org` cc WHERE cc.sumJdl=1" +
							"	)" +
							")",
							new MapListHandler()) ;
					for (Map<String, Object> map : person_ids) {
						if(!exclude_person_ids.isEmpty()) {
							//排除不需要计算的人员ID
							if(exclude_person_ids.contains(map.get("id"))) {
								map.remove("id") ;
								continue ;
							}
						}
						ids.add(map.get("id")) ;
					}
					sql += " and e.id in(:inIds)" ;
					params.put("inIds", (ids.isEmpty()?"":ids)) ;
					
				} catch (SQLException e) {
					LOG.error("生产部门稼动率，需查询组织机构计算稼动率的部门。异常", e) ;
				}
			}
			
			//按部门查询
			if (form.getOrg_id() != null && !"".equals(form.getOrg_id().trim())) {
				try {
					//需排除的人员ID
					List<String> exclude_person_ids = new ArrayList<String>() ;
					if(null != form.getExclude_person_ids() && !"".equals(form.getExclude_person_ids().trim())) {
						exclude_person_ids = Arrays.asList(form.getExclude_person_ids().split(","));
					}
					
					List<Object> ids = new ArrayList<Object>() ;
					
					//获取父节点下的子节点，包括自己（组织机构）
					Map<String, Object> getOrgIds = this.dbutil.getQr().query("select queryOrgChildren('"+form.getOrg_id()+"') as child", new MapHandler()) ;
					String org_id = getOrgIds.get("child").toString().replace("$,", "") ;
					
					//根据部门ID查询人员
					Map<String, Object> pids = new HashMap<String, Object>() ;
					pids.put("orgIds", org_id.trim().split(",")) ;
					List<Object[]> person_ids = this.basedaoPerson.listSQL("select p.id from ieasy_sys_person p where p.org_id IN(:orgIds)", pids) ;
					
					for(int i=0;i<person_ids.size(); i++) {
						if(!exclude_person_ids.isEmpty()) {
							//排除不需要计算的人员ID
							if(!exclude_person_ids.contains(person_ids.get(i))) {
								ids.add(person_ids.get(i)) ;
							}
						} else {
							ids.add(person_ids.get(i)) ;
						}
					}
					
					sql += " and e.id in(:inIds)" ;
					params.put("inIds", (ids.isEmpty()?"":ids)) ;
				} catch (SQLException e) {
					LOG.error("调用机构树状递归函数发生错误", e) ;
				}
			}
		}
		return sql;
	}

}
