package cn.facebook.productTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:config/applicationContext.xml")
public class ProductTest {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Test
	public void sendMsg() {
		jmsTemplate.convertAndSend("Hello World");
	}
}
