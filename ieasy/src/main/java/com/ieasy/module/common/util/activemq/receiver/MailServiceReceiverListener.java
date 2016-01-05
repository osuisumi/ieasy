package com.ieasy.module.common.util.activemq.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.ieasy.basic.util.mail.MailUtil;
import com.ieasy.basic.util.mail.MailVO;

/**
 * 邮件发送
 * 异步消息服务监听
 * 类描述： 
 * 创建者： 杨浩泉
 * 项目名称： infos
 * 创建时间： 2014-2-28 下午10:54:03
 * 版本号： v1.0
 */
public class MailServiceReceiverListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				MailVO mail = (MailVO) msg.getObject() ;
				try {
					MailUtil.send(mail) ;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
