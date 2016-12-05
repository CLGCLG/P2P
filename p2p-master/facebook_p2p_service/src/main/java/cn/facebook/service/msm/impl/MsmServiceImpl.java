package cn.facebook.service.msm.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import cn.facebook.service.msm.MsmService;
import cn.facebook.utils.MessageConstants;

@Service
public class MsmServiceImpl implements MsmService{

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	public void sendMsg(String phone, String content) {
		Map<String, Object> msg = new HashMap<String, Object>();
		
		msg.put(MessageConstants.MessageType, MessageConstants.SMSMessage);
		msg.put(MessageConstants.SMSNumbers, phone);
		msg.put(MessageConstants.MessageContent, content);
		jmsTemplate.convertAndSend(msg);
		
	}

}
