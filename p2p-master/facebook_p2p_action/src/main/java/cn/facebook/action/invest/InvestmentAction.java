package cn.facebook.action.invest;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import cn.facebook.action.common.BaseAction;
import cn.facebook.action.filter.GetHttpResponseHeader;
import cn.facebook.cache.BaseCacheService;
import cn.facebook.domain.userAccount.UserAccountModel;
import cn.facebook.service.userAccount.IUserAccountService;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.Response;

@Controller
@Namespace("/investment")
@Scope("prototype")
public class InvestmentAction extends BaseAction implements ModelDriven<UserAccountModel>{
	
	private UserAccountModel uam = new  UserAccountModel();
	@Autowired
	private IUserAccountService userAccountService;
	
	@Autowired
	private BaseCacheService baseCacheService;
	
	@Override
	public UserAccountModel getModel() {
		return uam;
	}
	
	@Action("checkAccount")
	public void checkAccount(){
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
			}
			String account = this.getRequest().getParameter("account");
			int userid = (int) hmap.get("id");
			UserAccountModel userAccountModel = userAccountService.findByUserId(userid);
			if (Integer.parseInt(account) > userAccountModel.getBalance()) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_SUFFICIENT_FUNDS).toJSON());
				return;
			}
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
			return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
