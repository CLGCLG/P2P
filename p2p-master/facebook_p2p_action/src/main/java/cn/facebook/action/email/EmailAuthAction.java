package cn.facebook.action.email;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.facebook.action.common.BaseAction;
import cn.facebook.action.filter.GetHttpResponseHeader;
import cn.facebook.cache.BaseCacheService;
import cn.facebook.domain.user.UserModel;
import cn.facebook.service.email.IEmailService;
import cn.facebook.service.user.IUserService;
import cn.facebook.utils.EmailUtils;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.Response;
import cn.facebook.utils.SecretUtil;

@Namespace("/emailAuth")
@Controller
@Scope("prototype")
public class EmailAuthAction extends BaseAction{

	@Autowired
	private BaseCacheService baseCacheService;
	@Autowired
	private IUserService userServcie;
	@Autowired
	private IEmailService emailService;
	
	
	// 激活邮箱，并修改邮箱状态
		@Action("emailactivation")
		public void emailactivation() {
			this.getResponse().setContentType("text/html;charset=utf-8");
			// 1.得到请求参数
			String us = this.getRequest().getParameter("us");
			// 2.将请求参数解密得到userid
			try {
				String userid = SecretUtil.decode(us.trim());
				// 3.查询用户
				UserModel um = userServcie.findById(Integer.parseInt(userid));
				// 4.修改状态
				if (um != null) {
					// um.setEmailStatus(1);
					userServcie.updateEmailStatus(Integer.parseInt(userid));
					this.getResponse().getWriter().write("激活成功");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				this.getResponse().getWriter().write("激活失败");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	
	//绑定邮箱，并发送邮寄
	@Action("auth")
	public void sendEmail(){
		//1、获取token，判断是否登录
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
			}
		//2、得到请求参数,邮箱
			String email = this.getRequest().getParameter("email");
		//3、校验邮箱
			if (EmailUtils.checkEmail(email)) {
				int userid = (int) hmap.get("id");
				String enc = SecretUtil.encrypt(String.valueOf(userid));//对userid进行加密，该加密算法是可逆的
				String username = (String) hmap.get("userName");
				String content = EmailUtils.getMailCapacity(email, enc, username);// 获取邮件内容
				System.out.println(content);
				//4、发邮件
				emailService.sendEmail(email, content);
				//5、绑定邮箱
				// 绑定邮箱操作---应该先查询用户是否已经绑定邮箱
				
				userServcie.addEmail(email, userid);
				// 3.响应
				this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
				return;
			}else{
				this.getResponse().getWriter().write(Response.build().setStatus("174").toJSON());
				return;
			}
		
		}catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
