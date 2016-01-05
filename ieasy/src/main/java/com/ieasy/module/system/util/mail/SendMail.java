package com.ieasy.module.system.util.mail;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ieasy.basic.util.FreemarkerUtil;
import com.ieasy.basic.util.mail.MailVO;
import com.ieasy.basic.util.springmvc.ContextHolderUtils;
import com.ieasy.module.common.util.activemq.send.ISendMailService;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.web.form.PersonForm;

@Component
public class SendMail {

	private static Log LOG = LogFactory.getLog(SendMail.class);
	
	@Inject
	private ISendMailService sendMessage ;
	
	private FreemarkerUtil futil ; 
	private String outPath ;
	
	@Autowired(required=true)
	public SendMail(String ftlPath, String outPath) {
		if(null == futil) {
			this.outPath = outPath ;
			futil = FreemarkerUtil.getInstance(ftlPath) ;
		}
	}
	
	/**
	 * 用户注册后，邮件发送账号和密码
	 * @param pf
	 * @param uf
	 */
	public void sendUserRegInfo(PersonForm pf) {
		Map<String, Object> root = new HashMap<String, Object>() ;
		
		/**组装模板数据*********************/
		HttpServletRequest request = ContextHolderUtils.getRequest() ;
		String domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		String html_name = pf.getNum()+"_reg"+"_"+System.currentTimeMillis()+".html" ;
		
		root.put("name", pf.getName()) ;
		root.put("account", pf.getAccount()) ;
		root.put("password", pf.getPassword()) ;
		root.put("domain", domain) ; 
		root.put("loginURL", domain + "/login.jsp") ; 
		root.put("link", domain + this.outPath + "/" + html_name) ;
		
		
		//邮件内容
		String templateToString = futil.templateToString(root, "/reg.ftl") ;
		//生成HTML
		futil.generate(root, "/reg.ftl", WebContextUtil.getRealPath(this.outPath)+"/"+html_name) ;
		
		//发送邮件
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO("yanghaoquan@whizen.com") ;
		mvo.setSubject("项目管理系统登陆账号") ;
		mvo.setContent(templateToString) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
		}
		
	}
	
}
