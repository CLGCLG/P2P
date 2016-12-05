package cn.facebook.action.verification;

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
import cn.facebook.service.user.IUserService;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.Response;

@Controller
@Namespace("/verification")
@Scope("prototype")
public class VerificationAction extends BaseAction{
	
	@Autowired
	private BaseCacheService baseCacheService;
	@Autowired
	private IUserService userService;
	
	//短信验证码验证方法
	@Action("validateSMS")
	public void validateSMS() {
		//0、获取token
				String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
				Map<String, Object> hmap = baseCacheService.getHmap(token);
				try {
					if (hmap == null || hmap.size() == 0) {
						this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
						return;
					}
				//1、获取请求参数：手机号，验证码
				String phone = this.getRequest().getParameter("phone");
				String phoneCode = this.getRequest().getParameter("code");
				//2、判断验证码是否正确
				String _phoneCode = baseCacheService.get(phone);
				if (!_phoneCode.equals(phoneCode)) {
						this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD).toJSON());
						return;
					}
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
	}
}
