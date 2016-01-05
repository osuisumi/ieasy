package com.ieasy.module.common.web.action;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.system.web.form.LoginSession;

@Controller
@RequestMapping("/admin/index")
public class IndexAction extends BaseController {

	@RequestMapping("/adminIndex")
	public String adminIndex(HttpSession session) {
		LoginSession user = (LoginSession) session.getAttribute(Const.USER_SESSION) ;
		if(null==user) {
			return "redirect:/common/errors/noLogin.jsp" ;
		}
		return Const.ADMIN_INDEX + "index" ;
	}
	
}
