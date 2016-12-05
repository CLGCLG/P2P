package cn.facebook.action.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;

import cn.facebook.action.common.BaseAction;
import cn.facebook.action.filter.GetHttpResponseHeader;
import cn.facebook.cache.BaseCacheService;
import cn.facebook.domain.userAccount.UserAccountModel;
import cn.facebook.service.userAccount.IUserAccountService;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.Response;

@Controller
@Namespace("/account")
@Scope("prototype")
public class PersonCenterAction extends BaseAction {

	@Autowired
	private BaseCacheService baseCacheService;
	@Autowired
	private IUserAccountService userAccountService;
	
	
	private Logger log = Logger.getLogger(PersonCenterAction.class);
	
	@Action("accountHomepage")
	public void accountHomepage() {
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		
			try {
				if (StringUtils.isEmpty(token)) {
					log.error("token为空");
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_TOKEN).toJSON());
					return;
				}
				Map<String, Object> hmap = baseCacheService.getHmap(token);
				if (hmap == null || hmap.size() == 0) {
					log.error("用户没有登录");
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
					return;
				}
				int id = (int) hmap.get("id");
				UserAccountModel uam = userAccountService.findByUserId(id);
				List<JSONObject> list = new ArrayList<JSONObject>();
				
				if(uam != null) {
					JSONObject obj = new JSONObject();
					obj.put("u_total", uam.getTotal());
					obj.put("u_balance", uam.getBalance());
					obj.put("u_interest_a", uam.getInterestA());
					list.add(obj);
				} else {
					JSONObject obj = new JSONObject();
					obj.put("u_total", 0);
					obj.put("u_balance", 0);
					obj.put("u_interest_a", 0);
					list.add(obj);
				}
				log.info("查询用户账号信息success");
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
				return;
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				try {
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SYSTEM_ERROE).toJSON());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
		}
}
