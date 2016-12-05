package cn.facebook.action.charge;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.facebook.action.common.BaseAction;
import cn.facebook.action.filter.GetHttpResponseHeader;
import cn.facebook.cache.BaseCacheService;
import cn.facebook.domain.Product;
import cn.facebook.domain.ProductAccount;
import cn.facebook.domain.accountLog.AccountLog;
import cn.facebook.domain.bankCardInfo.BankCardInfo;
import cn.facebook.domain.matchManagement.WeigthRule;
import cn.facebook.domain.productAcount.ExpectedReturn;
import cn.facebook.domain.productAcount.FundingNotMatchedModel;
import cn.facebook.domain.userAccount.UserAccountModel;
import cn.facebook.service.bankcardInfo.IBankCardInfoService;
import cn.facebook.service.charge.IChargeService;
import cn.facebook.service.expectedReturn.IExpectedReturnService;
import cn.facebook.service.product.IProductService;
import cn.facebook.service.productAccount.IProductAccountService;
import cn.facebook.service.userAccount.IUserAccountService;
import cn.facebook.service.weigthRule.IWeigthRuleService;
import cn.facebook.utils.BigDecimalUtil;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.FundsFlowType;
import cn.facebook.utils.InvestStatus;
import cn.facebook.utils.InvestTradeType;
import cn.facebook.utils.RandomNumberUtil;
import cn.facebook.utils.Response;
import cn.facebook.utils.TimestampUtils;

@Namespace("/charges")
@Controller
@Scope("prototype")
public class ChargesAction extends BaseAction {

	@Autowired
	private BaseCacheService baseCacheService;
	@Autowired
	private IBankCardInfoService bankCardInfoService;
	@Autowired
	private IChargeService chargeService;
	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IWeigthRuleService weigthRuleService;
	@Autowired
	private IProductAccountService productAccountService;
	@Autowired
	private IExpectedReturnService expectedReturnService;
	
	@Action("ProductAccountBuying")
	public void ProductAccountBuying() {
		this.getResponse().setCharacterEncoding("utf-8");
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		try {
			if (StringUtils.isBlank(token)) {
				this.getResponse().getWriter()
						.write(Response.build().setStatus(FrontStatusConstants.NULL_TOKEN).toJSON());
				return;
			}
			Map<String, Object> hmap = baseCacheService.getHmap(token);
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter()
						.write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
			}
			String _currentPage = this.getRequest().getParameter("currentPage");
			if (StringUtils.isBlank(_currentPage)) {
				this.getResponse().getWriter()
						.write(Response.build().setStatus(FrontStatusConstants.PARAM_VALIDATE_FAILED).toJSON());
				return;
			}
			int currentPage = Integer.parseInt(_currentPage);
			int status = Integer.parseInt(this.getRequest().getParameter("status"));
			String startDate = this.getRequest().getParameter("startDate");
			String endDate = this.getRequest().getParameter("endDate");
			
			int uid = (int) hmap.get("id");
			
			Page<ProductAccount> page = productAccountService.findProductAccountByPage(currentPage, 2,uid,status,startDate,endDate);
			this.getResponse().getWriter().write(Response.build().setStatus("1").setData(page.getContent())
					.setTotal("" + page.getTotalElements()).toJSON());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Action("addMayTake")
	public void addMayTake() {
		//0、获取token
		String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
		Map<String, Object> hmap = baseCacheService.getHmap(token);
		try {
			if (hmap == null || hmap.size() == 0) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
				return;
			}
			int userid = (int)hmap.get("id");
			String pProductId = this.getRequest().getParameter("pProductId");//购买产品id
			String pAmount = this.getRequest().getParameter("pAmount");//购买金额
			String pDeadline = this.getRequest().getParameter("pDeadline");//投资期限
			String pExpectedAnnualIncome = this.getRequest().getParameter("pExpectedAnnualIncome"); //年利率
			String pMonthInterest = this.getRequest().getParameter("pMonthInterest"); //每月赢取利息
			String pMonthlyExtractInterest = this.getRequest().getParameter("pMonthlyExtractInterest"); //每月提取利息
			
			// 本次投资总本息
			String endInvestTotalMoney = BigDecimalUtil.endInvestTotalMoney(pAmount, pDeadline, pExpectedAnnualIncome, pMonthlyExtractInterest);
			// 本次投资总利息
			BigDecimal mayInterrestIncome = BigDecimalUtil.sub(endInvestTotalMoney, pAmount);
			
//---------------用户账户表--t_account-----执行更改操作----------信息封装
			UserAccountModel uam = userAccountService.findByUserId(userid);
			
			BigDecimal _balance = BigDecimalUtil.sub(uam.getBalance(), Double.parseDouble(pAmount)); //账户可用余额
			BigDecimal _inverstmentW = BigDecimalUtil.add(uam.getInverstmentW(), Double.parseDouble(pAmount)); //总计待收本金
			BigDecimal _interestTotal = BigDecimalUtil.add(uam.getInterestTotal(), mayInterrestIncome.doubleValue());//总计待收利息
			BigDecimal _recyclingInterest = BigDecimalUtil.add(uam.getRecyclingInterest(), Double.parseDouble(pAmount));//月取总额
			BigDecimal _inverstmentA = BigDecimalUtil.add(uam.getInverstmentA(), Double.parseDouble(pAmount));//已投资总额
			
			UserAccountModel userAccountModel = new UserAccountModel();
			
			userAccountModel.setId(uam.getId());
			userAccountModel.setBalance(_balance.doubleValue());
			userAccountModel.setInverstmentW(_inverstmentW.doubleValue());
			userAccountModel.setInterestTotal(_interestTotal.doubleValue());
			userAccountModel.setRecyclingInterest(_recyclingInterest.doubleValue());
			userAccountModel.setInterestA(_inverstmentA.doubleValue());
//---------------用户账户表--t_account------------信息封装	
			
			
//---------------用户投资表---t_product_Account----执行插入操作--------------	
			//查询到的产品
			Product product = productService.findById(Integer.parseInt(pProductId));
			//要封装的产品
			ProductAccount pa = new ProductAccount();
			
			Date date = new Date();//开始时间
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, Integer.parseInt(pDeadline));//结束日期
			String randomNum = RandomNumberUtil.randomNumber(date);//随机id
			
			pa.setpProductId(product.getProId());
			pa.setpProductName(product.getProductName());
			pa.setpUid((long) userid);
			pa.setpSerialNo("TZNO"+randomNum);//投资编号
			pa.setpBeginDate(date);
			pa.setpEndDate(c.getTime());
			pa.setpAmount(Double.parseDouble(pAmount));
			pa.setpDeadline(Integer.parseInt(pDeadline));
			pa.setpExpectedAnnualIncome(Double.parseDouble(pExpectedAnnualIncome));
			pa.setpMonthInterest(Double.parseDouble(pMonthInterest));
			pa.setpMonthlyExtractInterest(Double.parseDouble(pMonthlyExtractInterest));
			pa.setpAvailableBalance(_balance.doubleValue());
			pa.setpEndInvestTotalMoney(_inverstmentW.doubleValue());
			pa.setpStatus(InvestStatus.WAIT_TO_MATCH);
			pa.setaCurrentPeriod(1);
//---------------用户投资表---t_product_Account------------------				
			
//-----------------交易流水记录日志表   t_account_log-------插入数据-----------------
			AccountLog accountlog = new AccountLog();
			
			accountlog.setaUserId(userid);
			accountlog.setaMainAccountId(userid);
			accountlog.setaCurrentPeriod(1);
			accountlog.setaReceiveOrPay(InvestTradeType.PAY);
			accountlog.setaTransferSerialNo("LSNO"+randomNum);
			accountlog.setaDate(date);
			accountlog.setaType(FundsFlowType.INVEST_TYPE);
			accountlog.setaTransferStatus(FundsFlowType.INVEST_SUCCESS);
			accountlog.setaBeforeTradingMoney(uam.getBalance());
			accountlog.setaAmount(Double.parseDouble(pAmount));
			accountlog.setaAfterTradingMoney(_balance.doubleValue());
			accountlog.setaDescreption("月取计划TZNO"+randomNum);

//-----------------交易流水记录日志表   t_account_log------------------------

			
//----------------------权重表----t_weighrule-------------------------	
			WeigthRule wr = weigthRuleService.findByWeigthType(124);
			
//----------------------权重表----t_weighrule-------------------------			
			
//------------------匹配资金表---------t_funding_not_matched-------------------插入操作-------------------
			FundingNotMatchedModel fnmm = new FundingNotMatchedModel();
			fnmm.setUserId(userid);
			fnmm.setfNotMatchedMoney(Double.parseDouble(pAmount));//	fNotMatchedMoney	待匹配金额
			fnmm.setfFoundingType(124);//	fFoundingType	资金类型
			fnmm.setfFoundingWeight(wr.getWeigthValue());//	fFoundingWeight	资金
			fnmm.setfIsLocked(FundsFlowType.FUND_NOT_LOCK);//	fIsLocked	是否锁定
//------------------匹配资金表---------t_funding_not_matched--------------------------------------
			
			//----------操作----------------
			productAccountService.addProductAccount(userAccountModel, pa, accountlog, fnmm);
			// 8.预期收益操作
			for (int i = 0; i < Integer.parseInt(pDeadline); i++) {
				ExpectedReturn er = new ExpectedReturn();
				// 封装数据
				// 1. 用户id
				er.setUserId(uam.getId());
				// 2. 产品id
				er.setProductId((int) (product.getProId()));
				// 3. 投资记录id
				er.setInvestRcord(pa.getpId());
				// 4. 收益日期 当前月份+1
				er.setExpectedDate(TimestampUtils.nextMonth(date.getYear(), date.getMonth(), i));
				// 5. 收益金额、-----从请求参数中获取
				er.setExpectedMoney(Double.parseDouble(pMonthInterest));
				// 6. 创建日期 new Date()
				er.setCreateDate(date);
				expectedReturnService.add(er);
			}
			// 发送短信，发送邮件
			System.out.println("完成理财产品购买操作");
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//充值操作
	@Action("charge")
	public void charge() {
		//0、获取token
				String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
				Map<String, Object> hmap = baseCacheService.getHmap(token);
				try {
					if (hmap == null || hmap.size() == 0) {
						this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
						return;
					}
//					1.	在action中获取请求参数---充值金额
					String chargeMoney = this.getRequest().getParameter("chargeMoney");
					double money = Double.parseDouble(chargeMoney); 
//					2.	获取银行帐户信息
					int userid = (int) hmap.get("id");
					BankCardInfo bankCardInfo = bankCardInfoService.findByUserId(userid);
					String bankCardNum = bankCardInfo.getBankCardNum();
//					3.	向银行服务器发送请求
					boolean falg = chargeService.charge(money, bankCardNum, userid);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}
