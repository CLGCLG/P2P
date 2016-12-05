package cn.facebook.activemq.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

public class ConsumerMessageListener implements MessageListener {

	public void onMessage(Message msg) {
		System.out.println(msg);
	}

}
