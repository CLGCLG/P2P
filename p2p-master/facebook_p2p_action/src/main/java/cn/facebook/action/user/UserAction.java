package cn.facebook.action.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import cn.facebook.action.common.BaseAction;
import cn.facebook.action.filter.GetHttpResponseHeader;
import cn.facebook.cache.BaseCacheService;
import cn.facebook.domain.user.UserModel;
import cn.facebook.service.user.IUserService;
import cn.facebook.utils.CommonUtil;
import cn.facebook.utils.ConfigurableConstants;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.ImageUtil;
import cn.facebook.utils.MD5Util;
import cn.facebook.utils.Response;
import cn.facebook.utils.SMSUtils;
import cn.facebook.utils.TokenUtil;

@Controller
@Namespace("/user")
@Scope("prototype")
public class UserAction extends BaseAction implements ModelDriven<UserModel>{

	@Autowired
	private BaseCacheService baseCacheService;
	@Autowired
	private IUserService userService;
	
	private UserModel user = new UserModel();
	
	@Override
	public UserModel getModel() {
		return user;
	}
	@Action("verifiRealName")
	public void verifiRealName(){
		//0、获取token
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
			}
		//1、获取请求参数：姓名，身份证号
		String realName = this.getRequest().getParameter("realName");
		String identity = this.getRequest().getParameter("identity");
		//2、判断身份证号的真实性
		// .....
		//NAME_UNMATCH_PAPER  姓名证件不匹配
		System.out.println(realName+"-------"+identity);
		UserModel um = userService.findByIdentity(identity);
		if(um!=null){
			//3、判断用户是否已经绑定用户
			if (um.getRealNameStatus() == 1){
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SOMEONE_USE_NOW).toJSON());
				return;
			}
		}
				//4、没有绑定进行绑定操作
				int userid = (int) hmap.get("id");
				userService.updateRealNameStatus(identity,realName,userid);
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
				//5、响应
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Action("addPhone")
	public void addPhone(){
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
		String phoneCode = this.getRequest().getParameter("phoneCode");
		//2、判断验证码是否正确
		String _phoneCode = baseCacheService.get(phone);
				if (!_phoneCode.equals(phoneCode)) {
					   this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD).toJSON());
					   return;
					}
				UserModel um = userService.findByPhone(phone);
				//3、判断用户是否已经绑定用户
				if (um.getPhoneStatus() == 1){
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.MOBILE_ALREADY_REGISTER).toJSON());
					return;
				}
				//4、没有绑定进行绑定操作
				int userid = (int) hmap.get("id");
				userService.updatePhoneStatus(phone,userid);
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
				//5、响应
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	//生成验证码，存到redis,发送到手机
	@Action("sendMessage")
	public void sendMessage(){
		//1、获得令牌，
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		
			try {
				//如果没有令牌，则说明没有登录
				if(hmap == null || hmap.size() == 0) {
				     this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				     return;
				}
				//2、获得手机号码
				String phone = this.getRequest().getParameter("phone");
				//3、获得验证码
				String phoneCode = RandomStringUtils.randomNumeric(4);
				//4、发送的信息
				String sendMsg = "P2P手机认证操作，请在30分钟内录入验证码:" + phoneCode;
				System.out.println(sendMsg);
				//5、发送
				//SMSUtils.SendSms(phone, sendMsg);
				//6、存到redis中
				baseCacheService.set(phone, phoneCode);
				baseCacheService.expire(phone, 3 * 60);
				
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Action("userSecureDetailed")
	public void userSecureDetailed(){
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		int userid = (int) hmap.get("id");
		UserModel um = userService.findById(userid);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phoneStatus", um.getPhoneStatus());
		map.put("realNameStatus", um.getRealNameStatus());
		map.put("payPwdStatus", um.getPayPwdStatus());
		map.put("emailStatus", um.getEmailStatus());
		map.put("username", um.getUsername());
		map.put("phone", um.getPhone());
		list.add(map);
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Action("userSecure")
	public void userSecure(){
		//String token = this.getRequest().getHeader("token");
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		int userid = (int) hmap.get("id");
		UserModel um = userService.findById(userid);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phoneStatus", um.getPhoneStatus());
		map.put("realNameStatus", um.getRealNameStatus());
		map.put("payPwdStatus", um.getPayPwdStatus());
		map.put("emailStatus", um.getEmailStatus());
		list.add(map);
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Action("logout")
	public void logout(){
		String token = this.getRequest().getHeader("token");
		baseCacheService.del(token);
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Action("login")
	public void login(){
//		a.	获取请求参数
		String username = this.getRequest().getParameter("username");
		String password = this.getRequest().getParameter("password");
		String signUuid = this.getRequest().getParameter("signUuid");
		String signCode = this.getRequest().getParameter("signCode");
//		b.	校验
	    try {
	    	if(signCode.isEmpty()||username.isEmpty()||password.isEmpty()||signCode.isEmpty()){
	    	  this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
	    	  return;
	    	}
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
		}
//		c.	判断验证码
	    String _signCode = baseCacheService.get(signUuid);
	    	 try {
	    		 if (!_signCode.equalsIgnoreCase(signCode)){
	    			 this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD).toJSON());
	    			 return;
	    		   }
	    		 } catch (IOException e) {
				e.printStackTrace();
			 }
	    //------begin----如果输入的是电话号码处理
	    	 boolean flag = CommonUtil.isMobile(username);
	    	 if(flag) {
	    		 UserModel userModel = userService.findByPhone(username);
	    		 username = userModel.getUsername();
	    	 } 
	    //------end----如果输入的是电话号码处理
//		d.	调用service通过username,password判断用户是否存在
	    String pwd = MD5Util.md5(username.toLowerCase()+password.toLowerCase());
	    UserModel um = userService.login(username,pwd);
			try {
				if (um == null) {
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
					return;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
//		e.	将用户存储到redis中(就是以前类似于session操作)  generateUserToken该方法根据用户名生成token，并把他放入redis中。
			String token = generateUserToken(um.getUsername());
//		f.	向浏览器响应数据
			
			try {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("userName", um.getUsername());
				data.put("id", um.getId());
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(data).setToken(token).toJSON());
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	// 注册操作
		@Action("signup")
		public void regist() {
			// 1.使用模型驱动完成请求参数封装.
			// 2.有些参数封装不上，例如验证码----必须手动获取

			// 3.完成添加用户操作
			String pwd = MD5Util.md5(user.getUsername().toLowerCase()+user.getPassword().toLowerCase());
			user.setPassword(pwd);
			boolean flag = userService.addUser(user);

			// 4.响应数据
			try {
				if (flag) {
					String token = generateUserToken(user.getUsername());
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("username", user.getUsername());
					data.put("id", user.getId());
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(data).setToken(token).toJSON());
				} else {
					this.getResponse().getWriter()
							.write(Response.build().setStatus(FrontStatusConstants.REGISTER_LOSED).toJSON());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	// 验证验证码是否正确
	@Action("codeValidate")
	public void codeValidate() {
		// 1.得到请求参数
		String signUuid = this.getRequest().getParameter("signUuid");
		String signCode = this.getRequest().getParameter("signCode");
		try {
			if (StringUtils.isBlank(signUuid)) {
				this.getResponse().getWriter()
						.write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
				return;
			}
			if (StringUtils.isBlank(signCode)) {
				this.getResponse().getWriter()
						.write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
				return;
			}
			// 2.判断验证码是否正确
			String _signCode = baseCacheService.get(signUuid);
			if (StringUtils.isBlank(_signCode)) {
				this.getResponse().getWriter()
						.write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
				return;
			}
			// 3.响应数据到浏览器
			if (signCode.equalsIgnoreCase(_signCode)) {
				// 正确
				this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
			} else {
				// 不正确
				this.getResponse().getWriter()
						.write(Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD).toJSON());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Action("validatePhone")
	public void validatePhone(){
		String phone = this.getRequest().getParameter("phone");
		UserModel user = userService.findByPhone(phone);
			try {
				if(user == null){
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
				} else {
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.MOBILE_ALREADY_REGISTER).toJSON());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Action("validateUserName")
	public void validateUserName(){
		String username = this.getRequest().getParameter("username");
		UserModel user = userService.findByUsername(username);
			try {
				if(user == null){
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
				} else {
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//生成验证码
	@Action("validateCode")
	public void validateCode(){
		String tokenUuid = this.getRequest().getParameter("tokenUuid");
		String value = baseCacheService.get(tokenUuid);
		
		if(StringUtils.isNotBlank(value)){
			String str = ImageUtil.getRundomStr();
			
			baseCacheService.set(tokenUuid, str);//key是uuid,value是生成的验证码
			baseCacheService.expire(tokenUuid, 3*60);
			
			try {
				ImageUtil.getImage(str, this.getResponse().getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}
	//生成uuid
	@Action("uuid")
	public void uuid(){
		String uuid = UUID.randomUUID().toString();
		
		baseCacheService.set(uuid, uuid);
		baseCacheService.expire(uuid, 60 * 3);
		
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setUuid(uuid).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String generateUserToken(String userName) {
		try {
			// 生成令牌
			String token = TokenUtil.generateUserToken(userName);// 这个加密操作得到的token中包含了用户名。这个加密是可逆的，也就是说可以从token中解出用户名。

			// 根据用户名获取用户
			UserModel user = userService.findByUsername(userName);
			// 将用户信息存储到map中。
			Map<String, Object> tokenMap = new HashMap<String, Object>();
			tokenMap.put("id", user.getId());
			tokenMap.put("userName", user.getUsername());
			tokenMap.put("phone", user.getPhone());
			tokenMap.put("userType", user.getUserType());
			tokenMap.put("payPwdStatus", user.getPayPwdStatus());
			tokenMap.put("emailStatus", user.getEmailStatus());
			tokenMap.put("realName", user.getRealName());
			tokenMap.put("identity", user.getIdentity());
			tokenMap.put("realNameStatus", user.getRealNameStatus());
			tokenMap.put("payPhoneStatus", user.getPhoneStatus());

			baseCacheService.del(token);
			baseCacheService.setHmap(token, tokenMap); // 将信息存储到redis中,也就说，登录时，redis中存储的是以用户名加密后的信息为键，以用户信息为值，存储的map

			// 获取配置文件中用户的生命周期，如果没有，默认是30分钟
			String tokenValid = ConfigurableConstants.getProperty("token.validity", "30");
			tokenValid = tokenValid.trim();
			baseCacheService.expire(token, Long.valueOf(tokenValid) * 60);

			return token;
		} catch (Exception e) {
			e.printStackTrace();
			return Response.build().setStatus("-9999").toJSON();
		}
	}
}
