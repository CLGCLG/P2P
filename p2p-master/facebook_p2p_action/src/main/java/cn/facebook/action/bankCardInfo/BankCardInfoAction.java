package cn.facebook.action.bankCardInfo;

import java.io.IOException;
import java.util.List;
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
import cn.facebook.domain.bankCardInfo.Bank;
import cn.facebook.domain.bankCardInfo.BankCardInfo;
import cn.facebook.domain.city.City;
import cn.facebook.domain.user.UserModel;
import cn.facebook.service.bank.IBankService;
import cn.facebook.service.bankcardInfo.IBankCardInfoService;
import cn.facebook.service.city.ICityService;
import cn.facebook.service.user.IUserService;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.Response;

@Controller
@Namespace("/bankCardInfo")
@Scope("prototype")
public class BankCardInfoAction extends BaseAction implements ModelDriven<BankCardInfo>{

	@Autowired
	private BaseCacheService baseCacheService;
	@Autowired
	private IBankCardInfoService bankCardInfoService;
	@Autowired
	private IBankService bankService;
	@Autowired
	private ICityService cityService;
	@Autowired
	private IUserService useService;
	
	private BankCardInfo bankCardInfo = new BankCardInfo();
	@Override
	public BankCardInfo getModel() {
		// TODO Auto-generated method stub
		return bankCardInfo;
	}
	//绑定银行卡操作
	@Action("addBankCardInfo")
	public void addBankCardInfo(){
		this.getResponse().setCharacterEncoding("utf-8");
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
				} 
			int userid = (int) hmap.get("id");
			bankCardInfo.setUserId(userid);
			bankCardInfoService.save(bankCardInfo);
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获取市区信息
	@Action("findCity")
	public void findCity(){
		this.getResponse().setCharacterEncoding("utf-8");
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
				} 
			String cityAreaNum = this.getRequest().getParameter("cityAreaNum");
			List<City> list = cityService.findByParentCityAreaNum(cityAreaNum);
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//绑定银行卡时获取当前用户信息
	@Action("findUserInfo")
	public void findUserInfo() {
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
				} 
			int userid = (int) hmap.get("id");
			UserModel userModel = useService.findById(userid);
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(userModel).toJSON());
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//查询所有省份信息
	@Action("findProvince")
	public void findProvince() {
		this.getResponse().setCharacterEncoding("utf-8");
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
				} 
			List<City> list = cityService.findProvince();
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//查询所有银行信息
	@Action("findAllBanks")
	public void findAllBanks() {
		this.getResponse().setCharacterEncoding("utf-8");
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
				} 
			List<Bank> banks = bankService.findAll();
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(banks).toJSON());
			return;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//根据用户名查询用户银行卡信息
	@Action("findBankInfoByUsername")
	public void findBankInfoByUsername() {
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
				} 
			int userid = (int) hmap.get("id");
			BankCardInfo bci = bankCardInfoService.findByUserId(userid);
			if (bci == null){
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_RESULT).toJSON());
				return;
			}
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(bci).toJSON());
			return;
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
