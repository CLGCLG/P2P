package cn.facebook.jms.impl;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import cn.facebook.jms.MessageSender;

@Component("emailSender")
public class EmailSender implements MessageSender {

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${mail.default.from}")
	private String from;
	
	@Override
	public void send(Message message) {
		
		System.out.println("发送邮件=============================================");
		// 1.创建一个MimeMessage
				MapMessage mapmessage=(MapMessage) message;
				MimeMessage mm = mailSender.createMimeMessage();
				
				// 3.设置相关属性
				
					try {
						// 2.得到一个设置邮件相关信息的对象
						MimeMessageHelper helper = new MimeMessageHelper(mm, true);
						helper.setFrom(from); // 从哪发送邮件
						helper.setTo(mapmessage.getString("to")); // 发送到哪
						helper.setSubject("P2P邮件激活"); // 标题
						helper.setText(mapmessage.getString("content"), true);// 内容
						// 发送邮件
						mailSender.send(mm);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				
	}

}
