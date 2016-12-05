package cn.facebook.action.accountLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.facebook.action.common.BaseAction;
import cn.facebook.domain.productAcount.WaitMatchMoneyModel;
import cn.facebook.service.accountLog.IAccountLogService;
import cn.facebook.utils.FrontStatusConstants;
import cn.facebook.utils.Response;

@Namespace("/accountLog")
@Controller
@Scope("prototype")
public class AccountLogAction<accountLogService> extends BaseAction{

	@Autowired
	private IAccountLogService accountLogService;
	
	@Action("/selectWaitMoney")
	public void selectWaitMoney() {
		this.getResponse().setCharacterEncoding("utf-8");
		
		List<WaitMatchMoneyModel> list = accountLogService.findWaitMoneyList();
		WaitMatchMoneyModel wmm = accountLogService.findWaitMoneySum();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listMatch", list);
		map.put("waitMatchCount", wmm);
		
		try {
			this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(map).toJSON());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
