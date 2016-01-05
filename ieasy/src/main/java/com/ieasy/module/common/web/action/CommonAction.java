package com.ieasy.module.common.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ieasy.basic.util.cons.Const;

@Controller
@RequestMapping("/common")
public class CommonAction extends BaseController {
	
	@RequestMapping("/person_dbl_grid_UI")
	public String person_dbl_grid_UI(String inputIds, String inputNames, String grid, Model mode) {
		return Const.COMMON + "person_dbl_grid_UI" ;
	}
	
}
