package cn.facebook.jms;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;

public class MessageReceiver implements MessageListener{
	@Autowired
	private MessageSenderFactory factory;
	@Override
	public void onMessage(Message m) {
		MapMessage mm = (MapMessage) m;
		try {
			//根据mm类型，获取具体的消息处理，完成消息发送
			processMeaage(mm);
			//设置应答机制
			mm.acknowledge();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processMeaage(MapMessage mm) throws JMSException {
		String type = mm.getString("type");
		MessageSender messageSender = factory.getMessageSender(type);
		messageSender.send(mm);
	}
}
