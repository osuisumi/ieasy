package com.ieasy.module.common.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.st.IScheduleTaskService;
import com.ieasy.module.common.st.ScheduleTaskForm;

@Controller
@RequestMapping("/admin/system/schedule")
public class ScheduleTaskAction extends BaseController {
	
	@Inject
	private IScheduleTaskService sts ;
	
	@RequestMapping("/schedule_task_main_UI.do")
	public String running() {
		return Const.SYSTEM + "schedule_task_main_UI" ;
	}
	
	@RequestMapping("/schedule_task_form_UI.do")
	public String schedule_task_form_UI(ScheduleTaskForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "schedule_task_form_UI" ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(ScheduleTaskForm form) throws Exception {
		Msg m = new Msg() ;
		if(null != this.sts.add(form)) {
			m.setMsg("定时作业创建成功！") ;
			m.setStatus(true) ;
		} else {
			m.setMsg("定时作业创建失败！") ;
			m.setStatus(false) ;
		}
		return m ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(ScheduleTaskForm form) throws Exception {
		return this.sts.delete(form) ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(ScheduleTaskForm form) throws Exception {
		return this.sts.update(form) ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public ScheduleTaskForm get(ScheduleTaskForm form) throws Exception {
		return this.sts.get(form) ;
	}
	
	@RequestMapping("/datagrid.do")
	public @ResponseBody DataGrid datagrid(ScheduleTaskForm form) {
		return this.sts.datagrid(form) ;
	}

}

