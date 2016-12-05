package cn.facebook.service.admin;

import cn.facebook.domain.admin.Admin;

public interface IAdminService {

	public Admin login(String username,String password);
}
