package cn.facebook.action.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import cn.facebook.action.common.BaseAction;
import cn.facebook.domain.Product;
import cn.facebook.domain.ProductEarningRate;
import cn.facebook.service.product.IProductService;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.JsonMapper;
import cn.facebook.utils.ProductStyle;
import cn.facebook.utils.Response;

@Controller
@Namespace("/product")
@Scope("prototype")
public class ProductAction extends BaseAction implements ModelDriven<Product> {

	private Logger log = Logger.getLogger(ProductAction.class);
	private Product p = new Product();
	
	@Autowired
	private IProductService productService;
	@Override
	public Product getModel() {
		// TODO Auto-generated method stub
		return p;
	}

	@Action("findAllProduct")
	public void findAll() {
		this.getResponse().setCharacterEncoding("utf-8");
		List<Product> list = productService.findAll();
		try {
			if (list == null || list.size() == 0) {
				log.error("查询理财产品失败");
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).setData(list).toJSON());
				return;
			}
			changeStatusToChinese(list);
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
		} catch (IOException e) {
			log.error("服务器出问题....."+e.getMessage());
			log.info("普通信息");
			log.warn("警告信息");
			log.debug("开发阶段调试");
			log.error("");
			log.fatal("致命信息");
			try {
				this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SYSTEM_ERROE).setData(list).toJSON());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
	}
	@Action("findProductById")
	public void findProductById(){
		String pid = this.getRequest().getParameter("proId");
		Product p = productService.findById(Long.parseLong(pid));
		this.getResponse().setCharacterEncoding("utf-8");
		changeStatusToChinese(p);
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(p).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Action("findRates")
	public void findRatesByPid(){
		String pid = this.getRequest().getParameter("proId");
		
		List<ProductEarningRate> pers = productService.findRateByPid(pid);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ProductEarningRate per:pers) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("month", per.getMonth());
			map.put("incomeRate", per.getIncomeRate());
			list.add(map);
		}
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Action("modifyProduct")
	public void modifyProduct(){
		String proEarningRates = this.getRequest().getParameter("proEarningRates");
		Map<String, Object> map = new JsonMapper().fromJson(proEarningRates, Map.class);
		
		List<ProductEarningRate> list = new ArrayList<ProductEarningRate>();
		for (String key : map.keySet()){
			ProductEarningRate per = new ProductEarningRate();
			per.setProductId((int)p.getProId());
			per.setIncomeRate(Double.parseDouble(map.get(key).toString()));
			per.setMonth(Integer.parseInt(key));
			list.add(per);
		}
		p.setProEarningRate(list);
		productService.modifyProduct(p);
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 方法描述：将状态转换为中文
	 * 
	 * @param products
	 *            void
	 */
	private void changeStatusToChinese(List<Product> products) {
		if (null == products)
			return;
		for (Product product : products) {
			int way = product.getWayToReturnMoney();
			// 每月部分回款
			if (ProductStyle.REPAYMENT_WAY_MONTH_PART.equals(String.valueOf(way))) {
				product.setWayToReturnMoneyDesc("每月部分回款");
				// 到期一次性回款
			} else if (ProductStyle.REPAYMENT_WAY_ONECE_DUE_DATE.equals(String.valueOf(way))) {
				product.setWayToReturnMoneyDesc("到期一次性回款");
			}

			// 是否复投 isReaptInvest 136：是、137：否
			// 可以复投
			if (ProductStyle.CAN_REPEAR == product.getIsRepeatInvest()) {
				product.setIsRepeatInvestDesc("是");
				// 不可复投
			} else if (ProductStyle.CAN_NOT_REPEAR == product.getIsRepeatInvest()) {
				product.setIsRepeatInvestDesc("否");
			}
			// 年利率
			if (ProductStyle.ANNUAL_RATE == product.getEarningType()) {
				product.setEarningTypeDesc("年利率");
				// 月利率 135
			} else if (ProductStyle.MONTHLY_RATE == product.getEarningType()) {
				product.setEarningTypeDesc("月利率");
			}

			if (ProductStyle.NORMAL == product.getStatus()) {
				product.setStatusDesc("正常");
			} else if (ProductStyle.STOP_USE == product.getStatus()) {
				product.setStatusDesc("停用");
			}

			// 是否可转让
			if (ProductStyle.CAN_NOT_TRNASATION == product.getIsAllowTransfer()) {
				product.setIsAllowTransferDesc("否");
			} else if (ProductStyle.CAN_TRNASATION == product.getIsAllowTransfer()) {
				product.setIsAllowTransferDesc("是");
			}
		}
	}
	private void changeStatusToChinese(Product product) {
		List<Product> list1 = new ArrayList<Product>();
		list1.add(product);
		changeStatusToChinese(list1);
	}
	
}
