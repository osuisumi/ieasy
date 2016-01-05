package com.ieasy.module.system.web.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;

/**
 * @程序编写者：杨浩泉
 * @日期：2013-6-30
 * @类说明：数据源控制器
 */
@Controller
@RequestMapping("/admin/system/run")
public class SystemRunAction extends BaseController {
	
	@RequestMapping("/running_UI.do")
	public String running_UI() {
		return Const.SYSTEM + "running_UI" ;
	}
	
	@RequestMapping("/doNotNeedAuth_Run.do")
	public @ResponseBody Map<String, Object> doNotNeedAuth_run() {
		Map<String, Object> map = new HashMap<String, Object>() ;
		
		return map ;
	}

}

