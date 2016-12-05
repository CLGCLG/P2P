package cn.facebook.jms.impl;

import javax.jms.Message;

import org.springframework.stereotype.Component;

import cn.facebook.jms.MessageSender;

@Component("msmSender")
public class MsmSender implements MessageSender {

	@Override
	public void send(Message message) {
		System.out.println("发送短信");
		/*
		//2、获得手机号码
		String phone = message.get;
		//3、获得验证码
		String phoneCode = RandomStringUtils.randomNumeric(4);
		//4、发送的信息
		String sendMsg = "P2P手机认证操作，请在30分钟内录入验证码:" + phoneCode;
		System.out.println(sendMsg);
		//5、发送
		SMSUtils.SendSms(phone, sendMsg);
		*/
	}

}
