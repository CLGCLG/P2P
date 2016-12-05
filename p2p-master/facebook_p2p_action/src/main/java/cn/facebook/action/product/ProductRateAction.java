package cn.facebook.action.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import cn.facebook.domain.ProductEarningRate;
import cn.facebook.service.productRate.IProductRateService;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.Response;

@Controller
@Namespace("/productRate")
@Scope("prototype")
public class ProductRateAction extends BaseAction implements ModelDriven<ProductEarningRate>{

	private ProductEarningRate per = new ProductEarningRate();
	@Override
	public ProductEarningRate getModel() {
		return per;
	}
	
	@Autowired
	private BaseCacheService baseCacheService;
	@Autowired
	private IProductRateService productRateService;
	
	@Action("yearInterest")
	public void yearInterest(){
		//0、获取token
				String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
				Map<String, Object> hmap = baseCacheService.getHmap(token);
				try {
					if (hmap == null || hmap.size() == 0) {
						this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
						return;
					}
					String pid = this.getRequest().getParameter("pid");
					List<ProductEarningRate> lper = productRateService.findByPid(pid);
					
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (ProductEarningRate per:lper) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("endMonth", per.getMonth());
						map.put("incomeRate", per.getIncomeRate());
						list.add(map);
					}
					
					this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).toJSON());
					return;
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
	
	}
	
}
