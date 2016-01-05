package com.ieasy.module.common.web.init;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.UUIDHexGenerator;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.basic.util.poi.excel.ExcelUtil;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.oa.project.service.IProjectCenterService;
import com.ieasy.module.oa.project.web.form.ProjectCenterForm;

@Controller
@RequestMapping("/common/project_excel")
public class InitImportProjectExcel extends BaseController {
	
	@Inject
	private IProjectCenterService projectService ;
	
	@Inject
	private IDBUtilsHelper db ;
	
	@RequestMapping("/init_approve.do")
	public void init_approve(){
		try {
			List<Map<String, Object>> q = this.db.getQr().query("" +
					"select t.* from ieasy_oa_project_approve t " +
					"where t.proj_name not in('社内积分制开发','新建店服务器端数据导入工具开发')", 
					new MapListHandler()) ;
			for (Map<String, Object> m : q) {
				System.out.println(m);
				
				String sql = "insert into ieasy_oa_project_approve" +
						"(id,person_name,person_num,proj_name,proj_num,status,person_id,project_id) " +
						"values(?,?,?,?,?,?,?,?)" ;
				this.db.getQr().update(sql, new Object[]{
						UUIDHexGenerator.generator().toString(),
						"黄丽嫦",
						"0317",
						m.get("proj_name").toString(),
						m.get("proj_num").toString(),
						1,
						"402884834a03510b014a0351c4e4003f",
						m.get("project_id").toString(),
				}) ;
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//人员Excel数据
	String path = "com/ieasy/module/common/web/init/project.xlsx" ;

	@SuppressWarnings("unchecked")
	@RequestMapping("/init_import")
	public void adminIndex(HttpSession session) {
		Map<String, Object> query = null ;
		
		ExcelUtil instance = ExcelUtil.getInstance() ;
		ArrayList<ArrayList<Object>> sheetRowsByPath = instance.getSheetRowsByClasspath(path, null, 1, null, 31, null, true) ;
		
		for (ArrayList<Object> a : sheetRowsByPath) {
			String id = (String) a.get(0) ;
			String endDate = (String) a.get(1) ;
			String name = (String) a.get(2) ;
			String num = (String) a.get(3) ;
			String quot = (String) a.get(4) ;
			String startDate = (String) a.get(5) ;
			String status = (String) a.get(6) ;
			//String dept_id = (String) a.get(7) ;
			String emp_id = (String) a.get(8) ;
			//String leader = (String) a.get(9) ;
			String dept_name = (String) a.get(10) ;
			String createId = (String) a.get(11) ;
			//String createName = (String) a.get(12) ;
			String level = (String) a.get(13) ;
			
			String proj_bjscx = (String) a.get(14) ;
			String proj_bjzry = (String) a.get(15) ;
			String proj_buglv = (String) a.get(16) ;
			String proj_cclrl = (String) a.get(17) ;
			String proj_clrl = (String) a.get(18) ;
			String proj_gm = (String) a.get(19) ;
			String proj_type = (String) a.get(20) ;
			String proj_ydscx = (String) a.get(21) ;
			String proj_yjtrzry = (String) a.get(22) ;
			String proj_shouzhu = (String) a.get(23) ;
			String proj_taskScope = (String) a.get(24) ;
			String proj_xmpjzt = (String) a.get(25) ;
			String proj_cwydwcjssj = (String) a.get(26) ;
			String proj_cwwcjssj = (String) a.get(27) ;
			String proj_xmjsnf = (String) a.get(28) ;
			String proj_jxzt = (String) a.get(29) ;
			String proj_jiesuan = (String) a.get(30) ;
			
			if(null != emp_id && emp_id.length() == 3) {emp_id="0"+emp_id;}
			if(null != createId && createId.length() == 3) {createId="0"+createId;}
			
			
			ProjectCenterForm p = new ProjectCenterForm() ;
			p.setProj_num(num) ;
			p.setProj_name(name) ;
			p.setProj_start_time(DateUtils.formatYYYYMMDD(startDate)) ;
			p.setProj_end_time(DateUtils.formatYYYYMMDD(endDate)) ;
			p.setProj_quot(Float.parseFloat(quot)) ;
			p.setProj_status(1) ;//审批
			p.setDistinguish("合同项目") ;
			
			
			p.setProj_bjscx(proj_bjscx) ;
			p.setProj_bjzry(proj_bjzry) ;
			p.setProj_buglv(proj_buglv) ;
			p.setProj_cclrl(proj_cclrl) ;
			p.setProj_clrl(proj_clrl) ;
			p.setProj_gm(proj_gm) ;
			p.setProj_type(proj_type) ;
			p.setProj_ydscx(proj_ydscx) ;
			p.setProj_yjtrzry(proj_yjtrzry) ;
			p.setProj_zyfw(proj_taskScope) ;
			p.setProj_cwydwcjssj(proj_cwydwcjssj) ;
			p.setProj_cwwcjssj(proj_cwwcjssj) ;
			p.setProj_xmjsnf(proj_xmjsnf) ;
			
			if(null != proj_shouzhu) {
				if(proj_shouzhu.equals("1")) {
					p.setProj_shouzhu("已受注") ;
				} else {
					p.setProj_shouzhu("未受注") ;
				}
			} else {
				p.setProj_shouzhu("未受注") ;
			}
			if(null != proj_xmpjzt) {
				if(proj_xmpjzt.equals("1")) {
					p.setProj_htpjzt("已评审") ;
				} else {
					p.setProj_htpjzt("未评审") ;
				}
			} else {
				p.setProj_htpjzt("未评审") ;
			}
			if(null != proj_jxzt) {
				if(proj_jxzt.equals("0")) {
					p.setProj_jxzt("未结项") ;
				} else if(proj_jxzt.equals("1")) {
					p.setProj_jxzt("已结项") ;
				}
			} else{
				p.setProj_jxzt("未结项") ;
			}
			if(null != proj_jiesuan) {
				if(proj_jiesuan.equals("2")) {
					p.setProj_cwjszt("已结算") ;
				} else if(proj_jiesuan.equals("1")) {
					p.setProj_cwjszt("结算中") ;
				} else if(proj_jiesuan.equals("0")) {
					p.setProj_cwjszt("未结算") ;
				}
			} else {
				p.setProj_cwjszt("未结算") ;
			}
			
			if(null != level && !"".equals(level)) {
				p.setProj_level(level) ;
			}
			
			try {
				query = this.db.getQr().query("select id, name from ieasy_sys_person where num=?", new MapHandler(), new Object[]{"1300"}) ;
				p.setProj_creator_id(query.get("id").toString()) ;
				p.setProj_creator_name(query.get("name").toString()) ;
				
				p.setProj_approve_person_ids(p.getProj_creator_id()) ;
				p.setProj_approve_person_names(p.getProj_creator_name()) ;
				
				if(null == emp_id) {
					p.setProj_owner_id(p.getProj_creator_id()) ;
					p.setProj_owner_name(p.getProj_creator_name()) ;
				} else {
					query = this.db.getQr().query("select id, name from ieasy_sys_person where num=?", new MapHandler(), new Object[]{emp_id}) ;
					p.setProj_owner_id(query.get("id").toString()) ;
					p.setProj_owner_name(query.get("name").toString()) ;
					
					p.setProj_manager_ids(p.getProj_owner_id()) ;
					p.setProj_manager_names(p.getProj_owner_name()) ;
				
				}
				
				query = this.db.getQr().query("select id, name from ieasy_sys_org where name=?", new MapHandler(), new Object[]{dept_name}) ;
				p.setProj_owner_dept_id(query.get("id").toString()) ;
				p.setProj_owner_dept_name(query.get("name").toString()) ;
			
			
				System.out.println(id+"==" + p.getProj_name() +"==" + emp_id+"=="+level + "==" + p.getProj_status());
				
				Msg m = this.projectService.createProject(p) ;
				Map<String, Object> retMap = (Map<String, Object>) m.getObj() ;
				ProjectCenterForm pc = (ProjectCenterForm)retMap.get("project")  ;
				if(status.equals("1")) {
					this.projectService.setProjectStatus(2, pc.getId());
				} else if(status.equals("2")) {
					this.projectService.setProjectStatus(3, pc.getId());
				} else if(status.equals("3")) {
					this.projectService.setProjectStatus(4, pc.getId());
				}
				
				List<Map<String, Object>> workTimes = this.testLinkDb(id) ;
				for (Map<String, Object> map : workTimes) {
					Date WKstartDate = (Date) map.get("startDate") ;
					Date WKendDate = (Date) map.get("endDate") ;
					String WKproject_role = (String) map.get("project_role") ;
					Integer WKstatus = (Integer) map.get("status") ;
					String WKemp_id = (String) map.get("emp_id") ;
					System.out.println("\t\t" + WKstartDate+", "+WKendDate+", "+WKproject_role+", "+WKstatus+", "+WKemp_id);
					
					if(null != WKemp_id && WKemp_id.length() == 3) {WKemp_id="0"+WKemp_id;}
					query = this.db.getQr().query("select id, num, name from ieasy_sys_person where num=?", new MapHandler(), new Object[]{WKemp_id}) ;
					String person_id = query.get("id").toString() ;
					String person_num = query.get("num").toString() ;
					String person_name = query.get("name").toString() ;
					
					if(WKstatus == 4) {
						WKstatus = 0;	//待机状态
						//this.db.getQr().update("update ieasy_sys_person set byProjectWorkStatus=0 where num='"+person_num+"'") ;
					} else {
						//this.db.getQr().update("update ieasy_sys_person set byProjectWorkStatus=1 where num='"+person_num+"'") ;
						WKstatus=1;		//工作中
					}
					
					String hour = "SELECT SUM(t.normalHour) normalHour, SUM(t.weekendHour) weekendHour, SUM(t.holidaysHour) holidaysHour FROM infos.infox_project_overtime t WHERE t.EMP_ID=? and t.project_id=?" ;
					Map<String, Object> q_hour = this.db.getQr().query(hour, new MapHandler(), new Object[]{WKemp_id, id}) ;
					float n = 0f ;
					float w = 0f ;
					float h = 0f ;
					if(null != q_hour.get("normalHour")) {
						n = Float.parseFloat(q_hour.get("normalHour").toString()) ;
					}
					if(null != q_hour.get("weekendHour")) {
						w = Float.parseFloat(q_hour.get("weekendHour").toString()) ;
					}
					if(null != q_hour.get("holidaysHour")) {
						h = Float.parseFloat(q_hour.get("holidaysHour").toString()) ;
					}
					
					String sql = "insert into ieasy_oa_project_dev_worktime(" +
							"id,person_id,person_num,person_name,proj_num,project_id,proj_name," +
							"proj_role,work_startDate,work_endDate,work_status," +
							"normalHour,weekendHour,holidaysHour) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
					this.db.getQr().update(sql, new Object[]{
							UUIDHexGenerator.generator(),person_id,person_num,person_name,pc.getProj_num(),pc.getId(),
							pc.getProj_name(),WKproject_role,WKstartDate,WKendDate,WKstatus,
							n,w,h
					}) ;
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sheetRowsByPath.size()+"条");
	}
	
	public List<Map<String, Object>> testLinkDb(String projectId) throws SQLException {
		List<Map<String, Object>> list = this.db.getQr().query("select * from infos.infox_project_emp_working t where t.project_id=?", new MapListHandler(), new Object[]{projectId}) ;
		return list ;
	}
}
