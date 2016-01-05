package com.ieasy.module.common.st;

import java.util.List;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;

public interface IScheduleTaskService {

	public ScheduleTaskForm add(ScheduleTaskForm form) ;
	
	public Msg delete(ScheduleTaskForm form) ;
	
	public Msg delete(String id) ;
	
	public Msg update(ScheduleTaskForm form) ;
	
	public ScheduleTaskForm get(String id) ;
	
	public ScheduleTaskForm get(ScheduleTaskForm form) ;
	
	public DataGrid datagrid(ScheduleTaskForm form) ;
	
	public List<ScheduleTaskForm> find(ScheduleTaskForm form) ;
	
}
