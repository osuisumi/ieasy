package com.ieasy.module.common.web.servlet;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ieasy.basic.util.ConfigUtil;
import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.module.system.service.IDictService;
import com.ieasy.module.system.service.IGlobalService;
import com.ieasy.module.system.service.IMenuService;
import com.ieasy.module.system.service.IOrgService;
import com.ieasy.module.system.service.IPositionService;

@WebServlet(loadOnStartup=1, urlPatterns="/initServlet")
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(InitServlet.class) ;

	@Override
	public void init() throws ServletException {
		//获取spring的工厂
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		WebContextUtil.setWac(wac);
		//获取上下文
		WebContextUtil.setSc(this.getServletContext()) ;
		//获取附件上传的根目录
		WebContextUtil.setUploadDir(File.separator+ConfigUtil.get("uploadDir")) ;
		super.init();
		
		initialized() ;
	}
	
	public void initialized() {
		log.info("####################  开始--》初始化系统数据  ######################") ;
		log.info("开始--》初始化系统全局参数") ;
		initSystemGlobal() ;
		log.info("开始--》初始化系统菜单树") ;
		generateMenuTree() ;
		log.info("开始--》生成组织机构JSON文件") ;
		generateOrgTree() ;
		log.info("开始--》生成岗位JSON文件") ;
		generatePosition() ;
		log.info("开始--》生成数据字典JSON文件") ;
		generateDict() ;
		log.info("####################  结束--》初始化系统数据  ######################") ;
	}
	
	/**
	 * 初始化系统全局参数
	 */
	public void initSystemGlobal() {
		IGlobalService global = (IGlobalService) WebContextUtil.getBean("globalService") ;
		WebContextUtil.setGlobal(global.get("SYSTEM_GLOBAL")) ;
		log.debug("完成--》初始化系统全局参数") ;
	}
	
	/**
	 * 生成JSON文件
	 * 初始化系统菜单树
	 */
	public void generateMenuTree() {
		IMenuService menu = (IMenuService) WebContextUtil.getBean("menuService") ;
		FileUtils.WriteJSON(WebContextUtil.getSc().getRealPath("/static_res") +File.separator+ "menu.tree.json", menu.tree(null)) ;
		log.debug("完成--》初始化系统菜单树") ;
	}
	
	/**
	 * 生成JSON文件
	 * 生成组织机构JSON文件,static_res/org.tree.json
	 */
	public void generateOrgTree() {
		IOrgService org = (IOrgService) WebContextUtil.getBean("orgService") ;
		org.tree(null) ;
		log.debug("完成--》生成组织机构JSON文件,org.tree.json") ;
	}
	
	/**
	 * 生成JSON文件
	 * 生成岗位JSON文件,static_res/position.tree.json
	 */
	public void generatePosition() {
		IPositionService pos = (IPositionService) WebContextUtil.getBean("positionService") ;
		pos.tree(null) ;
		log.debug("完成--》生成岗位JSON文件,position.tree.json") ;
	}
	
	/**
	 * 生成JSON文件
	 * 生成数据字典JSON文件,static_res/dict.tree.json
	 */
	public void generateDict() {
		IDictService dict = (IDictService) WebContextUtil.getBean("dictService") ;
		dict.tree(null, false) ;
		dict.tree(null, true) ;
		
		//系统启动初始化数据字典,将数据字典保存到上下文
		WebContextUtil.setDicts(dict.generateAttrMaps()) ;
		
		log.debug("完成--》生成数据字典JSON文件,dict.tree.json") ;
	}
	
}
