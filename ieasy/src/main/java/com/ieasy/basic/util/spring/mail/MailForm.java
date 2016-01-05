package com.ieasy.basic.util.spring.mail;

import com.ieasy.basic.util.ConfigUtil;
import com.ieasy.basic.util.HtmlUtil;


public class MailForm {
	
	private String from ;
	
	private String to ;
	
	private String cc ;
	
	private String bcc ;
	
	private String attachments ;
	
	private String subject ;
	
	private String ctx ;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getCtx() {
		return ctx;
	}

	public void setCtx(String ctx) {
		this.ctx = ctx;
	}

	public String getFrom() {
		if(null == this.from || "".equals(from)) {
			this.from = ConfigUtil.get("mail.from") ;
		}
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		if(null != to) {
			to = HtmlUtil.filterAllWhite(to) ;
		}
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		if(null != cc) {
			cc = HtmlUtil.filterAllWhite(cc) ;
		}
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		if(null != bcc) {
			bcc = HtmlUtil.filterAllWhite(bcc) ;
		}
		this.bcc = bcc;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
}
