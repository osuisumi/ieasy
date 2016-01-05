package com.ieasy.module.common.web.action;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.basic.util.mail.MailVO;
import com.ieasy.basic.util.spring.mail.MailForm;
import com.ieasy.module.common.util.activemq.send.ISendMailService;
import com.ieasy.module.common.web.servlet.WebContextUtil;

@Controller
@RequestMapping("/common/mail")
public class MailAction extends BaseController {
	
	private static Log LOG = LogFactory.getLog(MailAction.class);
	
	@Inject
	private ISendMailService sendMessage ;

	@RequestMapping("/mail_UI.do")
	public String mail_UI() {
		return Const.COMMON + "mail_UI" ;
	}
	
	
	@RequestMapping("/doNotNeedAuth_sendMail.do")
	@ResponseBody
	public Msg sendMail(MailForm form) throws Exception {
		MailVO mvo = new MailVO() ;
		mvo.setRecipientTO(form.getTo()) ;
		mvo.setRecipientCC(form.getCc()) ;
		mvo.setSubject(form.getSubject()) ;
		mvo.setContent(form.getCtx()) ;
		try {
			this.sendMessage.sendMail(mvo) ;
		} catch (Exception e) {
			LOG.error("邮件发送失败", e) ;
		}
		
		return new Msg(true, "邮件已发送！") ;
	}
	
	@RequestMapping("/doNotNeedAuth_deleteAttach.do")
	@ResponseBody
	public Msg doNotNeedAuth_deleteAttach(String attachPath) throws Exception {
		FileUtils.deleteFile(WebContextUtil.getRealPath(attachPath)) ;
		return new Msg(true) ;
	}
	
	
	
	
}
