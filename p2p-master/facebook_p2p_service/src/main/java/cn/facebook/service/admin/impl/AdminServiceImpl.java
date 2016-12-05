package cn.facebook.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.admin.IAdminDao;
import cn.facebook.domain.admin.Admin;
import cn.facebook.service.admin.IAdminService;

@Transactional
@Service
public class AdminServiceImpl implements IAdminService {

	@Autowired
	private IAdminDao adminDao;

	@Override
	public Admin login(String username, String password) {
		return adminDao.login(username, password);
	}
	
}
