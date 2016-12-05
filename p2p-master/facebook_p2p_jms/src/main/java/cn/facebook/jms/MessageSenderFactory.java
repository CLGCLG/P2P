package cn.facebook.jms;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Component
public class MessageSenderFactory {
	
	@Autowired
	@Qualifier("emailSender")
	private MessageSender emailSender;
	
	@Resource(name="msmSender")
	private MessageSender msmSender;
	public MessageSender getMessageSender(String type) {
		if("email".equals(type)) {
			return emailSender;
		} else if ("sms".equals(type)) {
			return msmSender;
		} else {
			return null;
		}
	}
}
