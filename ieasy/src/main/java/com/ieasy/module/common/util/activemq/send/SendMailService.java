package com.ieasy.module.common.util.activemq.send;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.ieasy.basic.util.mail.MailVO;

@Component
public class SendMailService implements ISendMailService {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	public void sendMail(final MailVO mail) {
		//创建消息
		MessageCreator messageCreator = new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage();
				message.setObject(mail);
				return message;
			}
		};

		try {
			// 发送消息
			this.jmsTemplate.send(messageCreator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
