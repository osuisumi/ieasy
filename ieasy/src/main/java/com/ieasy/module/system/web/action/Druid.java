package com.ieasy.module.system.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ieasy.module.common.web.action.BaseController;


@Controller
@RequestMapping("/druid")
public class Druid extends BaseController {
	/**
	 * 转向到数据源监控页面
	 * 
	 * @return
	 */
	@RequestMapping("/druid.do")
	public String druid() {
		return "redirect:/druid/index.html";
	}

}

