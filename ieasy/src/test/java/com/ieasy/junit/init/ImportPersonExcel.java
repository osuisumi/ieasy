package com.ieasy.junit.init;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.junit.Test;

import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.DBUtilsHelper;
import com.ieasy.basic.util.poi.excel.ExcelUtil;
import com.ieasy.junit.BasicJunitTest;
import com.ieasy.module.system.service.IPersonService;
import com.ieasy.module.system.web.form.PersonForm;

public class ImportPersonExcel extends BasicJunitTest {
	
	@Inject
	private IPersonService personService ;
	
	@Inject
	private DBUtilsHelper db ;
	
	//人员Excel数据
	String path = "com/ieasy/junit/init/person.xlsx" ;
	
	@Test
	public void testImportPersonData(){
		Map<String, Object> query = null ;
		
		
		ExcelUtil instance = ExcelUtil.getInstance() ;
		ArrayList<ArrayList<Object>> sheetRowsByPath = instance.getSheetRowsByClasspath(path, null, 1, null, 31, null, true) ;
		
		for (ArrayList<Object> a : sheetRowsByPath) {
			String id = (String) a.get(0) ;
			String bysj = (String) a.get(1) ;
			String dbmDate = (String) a.get(2) ;
			String dbmType = (String) a.get(3) ;
			String email = (String) a.get(4) ;
			String lbmDate = (String) a.get(5) ;
			String lbmType = (String) a.get(6) ;
			String rzsj = (String) a.get(7) ;
			String sex = (String) a.get(8) ;
			String name = (String) a.get(9) ;
			String org_id = (String) a.get(10) ;
			String position = (String) a.get(11) ;
			
			
			if(id.length() == 3) {id="0"+id;}
			
			PersonForm p = new PersonForm() ;
			p.setNum(id) ;
			p.setName(name) ;
			p.setSex(sex) ;
			p.setEmail(email) ;
			p.setEmpLevel("高级职员") ;
			p.setEmpType("正式工") ;
			p.setCreated(new Date()) ;
			p.setCreateName("系统初始化") ;
			
			p.setEnterDate(DateUtils.formatYYYYMMDD(rzsj)) ;
			p.setChangeDate(DateUtils.formatYYYYMMDD(rzsj)) ;
			
			if(null!=bysj) {
				p.setBysj(DateUtils.formatYYYYMMDD(bysj)) ;
			}
			
			//设置到部门类型(1:在职,2:转入,3:新增,4:试用,5:停薪留职返回,6:返聘)
			if("3".equals(dbmType)) {
				p.setDbmType("在职") ;
				p.setDbmDate(DateUtils.formatYYYYMMDD(rzsj)) ;
			} else if("1".equals(dbmType)) {
				p.setDbmType("新增") ;
				p.setDbmDate(DateUtils.formatYYYYMMDD(rzsj)) ;
			}else if("6".equals(dbmType)) {
				p.setDbmType("返聘") ;
				p.setDbmDate(DateUtils.formatYYYYMMDD(dbmDate)) ;
			} 
			
			System.out.println(name + "==" + p.getDbmType() + "==" + DateUtils.formatYYYYMMDD(p.getDbmDate()) );
			
			if(null == lbmType) {
				//如果离部门类型为空，则类型和日期都为空
				p.setLbmType(null) ;
				p.setLbmDate(null) ;
			} else {
				if("2".equals(lbmType)) {
					p.setLbmType("转出-到开发部") ;
					p.setLbmDate(DateUtils.formatYYYYMMDD(lbmDate)) ;
				} else if("1".equals(lbmType)) {
					p.setLbmType("转出-到非开发部") ;
					p.setLbmDate(DateUtils.formatYYYYMMDD(lbmDate)) ;
				} else if("3".equals(lbmType)) {
					p.setLbmType("离职") ;
					p.setLbmDate(DateUtils.formatYYYYMMDD(lbmDate)) ;
				} else if("4".equals(lbmType)) {
					p.setLbmType("停薪留职") ;
					p.setLbmDate(DateUtils.formatYYYYMMDD(lbmDate)) ;
				} else {
					p.setLbmType(null) ;
					p.setLbmDate(null) ;
				}
			}
			
			
			
			if(null != lbmType && lbmType.equals("3")) {
				p.setEmpState("离职") ;
				p.setDimissionDate(DateUtils.formatYYYYMMDD(lbmDate)) ;
			} else {
				p.setEmpState("在职") ;
			}
			
			p.setCreateAccount(1) ;
			
			try {
				//关联infos数据库的org表
				Map<String, Object> infos_org = this.db.getQr().query("select id,fullname from infos.infox_sysmgr_org_dept t where t.id=?", new MapHandler(), new Object[]{org_id}) ;
				
				query = this.db.getQr().query("select id, name from ieasy_sys_org where name like '%"+infos_org.get("fullname").toString()+"%'", new MapHandler()) ;
				if(null != query) {
					p.setOrg_id((String)query.get("id")) ;
					p.setOrg_name((String)query.get("name")) ;
					
				}
				
				query = this.db.getQr().query("select id, name from ieasy_sys_position where name=?", new MapHandler(), new Object[]{position}) ;
				System.out.println(query+"=="+position);
				p.setPosition_id((String)query.get("id")) ;
				p.setPosition_name((String)query.get("name")) ;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println(
					name + "==" + 
							p.getDbmType() + "==" + DateUtils.formatYYYYMMDD(p.getDbmDate()) +
							p.getLbmType() + "==" + p.getLbmDate() 
					);
			//this.personService.add(p) ;
			
		}
		System.out.println(sheetRowsByPath.size()+"条");
	}
}
