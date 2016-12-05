package cn.facebook.action.admin;

import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.facebook.action.common.BaseAction;
import cn.facebook.domain.admin.Admin;
import cn.facebook.service.admin.IAdminService;

@Controller
@Namespace("/account")
@Scope("prototype")
public class AdminAction extends BaseAction {
	
	@Autowired
	private IAdminService adminService;
	
	@Action("login")
	public void login(){
		String username = this.getRequest().getParameter("username");
		String password =this.getRequest().getParameter("password");
		System.out.println(username+":"+password);
		Admin admin = adminService.login(username, password);
			try {
				if(admin != null){
					this.getResponse().getWriter().write("{\"status\":\"1\"}");
					return;
				} else {
					this.getResponse().getWriter().write("{\"status\":\"0\"}");
					return;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
