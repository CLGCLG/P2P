package cn.facebook.action.creditor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import cn.facebook.action.common.BaseAction;
import cn.facebook.domain.creditor.CreditorModel;
import cn.facebook.domain.creditor.CreditorSumModel;
import cn.facebook.service.creditor.ICreditorService;
import cn.facebook.util.constant.ClaimsType;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.RandomNumberUtil;
import cn.facebook.utils.Response;

@Controller
@Namespace("/creditor")
@Scope("prototype")
public class CreditorAction extends BaseAction implements ModelDriven<CreditorModel>{

	private CreditorModel cm = new CreditorModel();
	
	@Override
	public CreditorModel getModel() {
		return cm;
	}
	
	@Autowired
	private ICreditorService creditorService;
	
	@Action("checkCreditor")
	public void checkCreditor() {
		String ids = this.getRequest().getParameter("ids");
		creditorService.checkCreditor(ids);
		try {
			this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//债权查询
	@Action("getCreditorlist")
	public void getCreditorlist(){
			this.getResponse().setCharacterEncoding("utf-8");
		//1、获取请求参数
	
              String dDebtNo = this.getRequest().getParameter("dDebtNo");//标的编号
              String dContractNo = this.getRequest().getParameter("dContractNo"); //借款ID
              String dDebtTransferredDateStart = this.getRequest().getParameter("dDebtTransferredDateStart");//债权转入日期
              String dDebtTransferredDateEnd = this.getRequest().getParameter("dDebtTransferredDateEnd");//债权转出日期
              String dDebtStatus = this.getRequest().getParameter("dDebtStatus");//债权状态
              String dMatchedStatus = this.getRequest().getParameter("dMatchedStatus");//债权匹配状态
              String offsetnum = this.getRequest().getParameter("offsetnum");
              
              Map<String, Object> map = new HashMap<String, Object>();
              if (StringUtils.isNotEmpty(dDebtNo)) {
            	  map.put("dDebtNo", dDebtNo.trim());
              }
              if (StringUtils.isNotEmpty(dContractNo)) {
            	  map.put("dContractNo", dContractNo.trim());
              }
              if (StringUtils.isNotEmpty(dDebtTransferredDateStart)) {
            	  map.put("dDebtTransferredDateStart", dDebtTransferredDateStart.trim());
              }
              if (StringUtils.isNotEmpty(dDebtTransferredDateEnd)) {
            	  map.put("dDebtTransferredDateEnd", dDebtTransferredDateEnd.trim());
              }
              if (StringUtils.isNotEmpty(dDebtStatus)) {
            	  map.put("dDebtStatus", Integer.parseInt(dDebtStatus.trim()));
              }
              if (StringUtils.isNotEmpty(dMatchedStatus)) {
            	  map.put("dMatchedStatus", Integer.parseInt(dMatchedStatus.trim()));
              }
              if (StringUtils.isNotEmpty(offsetnum)) {
            	  map.put("offsetnum", Integer.parseInt(offsetnum.trim()));
              }
              //查询债券信息
              List<CreditorModel> list = creditorService.findCreditorList(map);
//              for (CreditorModel cm:list){
//            	  switch(cm.getDebtStatus()){
//            	  	case 11301: cm.setDebtStatusDesc("未审核");break;
//            	  	case 11302: cm.setDebtStatusDesc("已审核");break;
//            	  	case 11303: cm.setDebtStatusDesc("正常还款");break;
//            	  	case 11304: cm.setDebtStatusDesc("已结清");break;
//            	  	case 11305: cm.setDebtStatusDesc("提前结清");break;
//            	  	case 11306: cm.setDebtStatusDesc("结算失败");
//            	  }
//            	  switch(cm.getMatchedStatus()){
//            	  	case 11401: cm.setDebtStatusDesc("部分匹配");break;
//            	  	case 11402: cm.setDebtStatusDesc("完全匹配");break;
//            	  	case 11403: cm.setDebtStatusDesc("未匹配");
//            	  }
//            	  
//              }
              for (CreditorModel cm : list) {
      			if (cm.getDebtStatus() == 11301) {
      				cm.setDebtStatusDesc("未审核");
      			}
      			if (cm.getDebtStatus() == 11302) {
      				cm.setDebtStatusDesc("已审核");
      			}
      			if (cm.getDebtStatus() == 11303) {
      				cm.setDebtStatusDesc("正常还款");
      			}
      			if (cm.getDebtStatus() == 11304) {
      				cm.setDebtStatusDesc("已结清");
      			}
      			if (cm.getDebtStatus() == 11305) {
      				cm.setDebtStatusDesc("提前结清");
      			}
      			if (cm.getDebtStatus() == 11306) {
      				cm.setDebtStatusDesc("结算失败");
      			}

      			if (cm.getMatchedStatus() == 11401) {
      				cm.setMatchedStatusDesc("部分匹配");
      			}
      			if (cm.getMatchedStatus() == 11402) {
      				cm.setMatchedStatusDesc("完全匹配");
      			}
      			if (cm.getMatchedStatus() == 11403) {
      				cm.setMatchedStatusDesc("未匹配");
      			}

      		}
              
              
              //查询债券统计信息
              Object[] cmsSum = creditorService.findCreditorListSum(map);
              CreditorSumModel csum = new CreditorSumModel();
              csum.setdIdCount(Integer.parseInt(cmsSum[0].toString()));
              csum.setdDebtMoneySum(Double.parseDouble(cmsSum[1].toString()));
              csum.setdAvailableMoneySum(Double.parseDouble(cmsSum[2].toString()));
		//2、验证请求参数
		//3、调用service完成查询操作
		//4、响应数据到浏览器
             Map<String, Object> data = new HashMap<String, Object>();
             data.put("date", list);
             data.put("datasum", csum);
             try {
				this.getResponse().getWriter().write(Response.build().setStatus("1").setData(data).toJSON());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@Action("addCreditor")
	public void addCreditor(){
//		contractNo : undefined, // 借款Id（合同编号）
		try {
			if (StringUtils.isEmpty(cm.getContractNo())) {
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_CONTRACT_NO).toJSON());
					return;
			} 
//			debtorsName : undefined, // 债务人
			if (StringUtils.isEmpty(cm.getDebtorsName())) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_DEBTOR).toJSON());
				return;
			}
//			debtorsId : undefined, // 身份证号
			if (StringUtils.isEmpty(cm.getDebtorsId())) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_ID_CARD).toJSON());
				return;
			}
//			loanPurpose : undefined, // 借款用途
			if (StringUtils.isEmpty(cm.getLoanPurpose())) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_PURPOSE).toJSON());
				return;
			}
//			loanType : undefined, // 借款类型（标的类型）
			if (StringUtils.isEmpty(cm.getLoanType())) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_BORROWING_TYPE).toJSON());
				return;
			}
//			loanPeriod : undefined, // 原始期限（月）源
			if (StringUtils.isEmpty(cm.getLoanPeriod() + "" )) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_ORIGINAL_DEADLINE).toJSON());
				return;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//			loanStartDate : undefined, // 原始借款开始日期
			if (StringUtils.isEmpty(sdf.format(cm.getLoanStartDate()))) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_ORIGINAL_BORROWING_BEGIN_DATE).toJSON());
				return;
			}
//			loanEndDate : undefined, // 原始借款到期日期
			if (StringUtils.isEmpty(sdf.format(cm.getLoanEndDate()))) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_ORIGINAL_BORROWING_MATURITY_DATE).toJSON());
				return;
			}
//			repaymentStyle : 11601, // 还款方式 radius
			if (StringUtils.isEmpty(cm.getRepaymentStyle() + "" )) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_REPAYMENT_TYPE).toJSON());
				return;
			}
			if (StringUtils.isEmpty(cm.getRepaymenDate())) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_REPAYMENT_DATE).toJSON());
				return;
			}
//			repaymenMoney : undefined, // 期供金额（元）
			if (StringUtils.isEmpty(cm.getRepaymenMoney() + "")) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_REPAYMENT_AMOUNT).toJSON());
				return;
			}
//			debtMoney : undefined, // 债权金额（元）
			if (StringUtils.isEmpty(cm.getDebtMoney() + "" )) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_CLAIM_AMOUNT).toJSON());
				return;
			}
//			debtMonthRate : undefined, // 债权年化利率（%// ）
			if (StringUtils.isEmpty(cm.getDebtMoney() + "" )) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_CLAIM_YEAR_RATE).toJSON());
				return;
			}
//			debtTransferredMoney : undefined, // 债权转入金额
			if (StringUtils.isEmpty(cm.getDebtTransferredMoney() + "")) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_CLAIM_ROLL_IN_AMOUNT).toJSON());
				return;
			}
//			debtTransferredPeriod : undefined, // 债权可用期（月）
			if (StringUtils.isEmpty(cm.getDebtTransferredPeriod() + "")) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_CALIM_ROLL_IN_DATE).toJSON());
				return;
			}
//			debtTransferredDate : undefined, // 债权转入日期
			if (StringUtils.isEmpty(sdf.format(cm.getDebtTransferredDate()))) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_CALIM_ROLL_IN_DEADLINE).toJSON());
				return;
			}
//			debtRansferOutDate : undefined, // 债权转出日期
			if (StringUtils.isEmpty(sdf.format(cm.getDebtRansferOutDate()))) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_CALIM_ROLL_OUT_DATE).toJSON());
				return;
			}
//			creditor : undefined
			if (StringUtils.isEmpty(cm.getCreditor())) {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_DEBTEE).toJSON());
				return;
			}
			
			cm.setDebtNo("ZQNO" + RandomNumberUtil.randomNumber(new Date()));
			cm.setBorrowerId(1);
			cm.setDebtStatus(1);
			cm.setDebtStatus(ClaimsType.UNCHECKDE);
			cm.setMatchedMoney(0.0);
			cm.setMatchedStatus(ClaimsType.UNMATCH);
			cm.setDebtType(FrontStatusConstants.NULL_SELECT_OUTACCOUNT);
			cm.setAvailablePeriod(cm.getDebtTransferredPeriod());
			cm.setAvailableMoney(cm.getDebtTransferredMoney());
			
			creditorService.save(cm);
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
			return;
		} catch (IOException e) {
				e.printStackTrace();
				try {
					this.getResponse().getWriter().write(Response.build().setStatus("0").toJSON());
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

	}
}
