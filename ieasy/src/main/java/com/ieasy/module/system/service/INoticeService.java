package com.ieasy.module.system.service;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.web.form.NoticeForm;

public interface INoticeService {
	
	public Msg add(NoticeForm form) ;
	
	public Msg delete(NoticeForm form) ;
	
	public Msg update(NoticeForm form) ;
	
	public NoticeForm get(NoticeForm form) ;
	
	public DataGrid datagrid(NoticeForm form) ;
	
	public Msg modifyAp(NoticeForm form) ;
}
