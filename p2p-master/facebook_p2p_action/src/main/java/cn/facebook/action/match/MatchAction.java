package cn.facebook.action.match;

import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.facebook.action.common.BaseAction;
import cn.facebook.service.match.IMatchService;
import cn.facebook.utils.Response;

@Namespace("/match")
@Controller
@Scope("/prototype")
public class MatchAction extends BaseAction{

	@Autowired
	private IMatchService matchService;
	
	@Action("startMatchByManually")
	public void startMatchByManually() {
		
		matchService.startMatch();
		try {
			this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
