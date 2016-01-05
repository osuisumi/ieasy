package com.ieasy.module.oa.project.service;

import java.util.Map;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.module.oa.project.web.form.ProjectJdlForm;

public interface IProjectJdlService {

	public DataGrid dagagrid(ProjectJdlForm form) ;
	
	/**
	 * 多部门汇总及图表
	 * @param form
	 * @return
	 */
	public Map<String, Object> get_jdl_dept_report(ProjectJdlForm form) ;
	
}
