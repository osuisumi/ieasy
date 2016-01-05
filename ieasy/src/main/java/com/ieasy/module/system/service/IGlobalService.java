package com.ieasy.module.system.service;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.entity.GlobalEntity;
import com.ieasy.module.system.web.form.GlobalForm;

public interface IGlobalService {
	
	public Msg add(GlobalForm form) ;
	
	public Msg delete(GlobalForm form) ;
	
	public Msg update(GlobalForm form) ;
	
	public GlobalForm get(String id) ;
	
	public DataGrid datagrid(GlobalForm form) ;
	
	public void init(GlobalEntity entity) ;
}
