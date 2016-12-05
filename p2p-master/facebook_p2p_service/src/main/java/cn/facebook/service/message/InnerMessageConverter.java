package cn.facebook.service.message;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import cn.facebook.utils.MessageConstants;

public class InnerMessageConverter implements MessageConverter{
	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		MapMessage message = session.createMapMessage();

		@SuppressWarnings("all")
		Map<String, Object> map = (Map) object;

		message.setObject("title", map.get(MessageConstants.EmailMessageTitle));
		message.setObject("content", map.get(MessageConstants.MessageContent));
		message.setObject("to", map.get(MessageConstants.EmailMessageTo));
		message.setObject("cc", map.get(MessageConstants.EmailMessageCC));
		message.setObject("type", map.get(MessageConstants.MessageType));
		message.setObject("phone", map.get(MessageConstants.SMSNumbers));

		return message;
	}
	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		return message;
	}
}
