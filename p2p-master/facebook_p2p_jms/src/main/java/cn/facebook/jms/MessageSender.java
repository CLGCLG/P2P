package cn.facebook.jms;

import javax.jms.Message;

public interface MessageSender {

	public void send(Message message);
}
