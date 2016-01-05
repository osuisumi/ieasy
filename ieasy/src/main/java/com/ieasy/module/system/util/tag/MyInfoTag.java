package com.ieasy.module.system.util.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class MyInfoTag extends TagSupport{

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		 System.out.println("我的个人信息....");
		return super.doStartTag();
	}

}
