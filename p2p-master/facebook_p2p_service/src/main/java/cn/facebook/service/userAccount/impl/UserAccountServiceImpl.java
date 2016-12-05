package cn.facebook.service.userAccount.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.userAccount.IUserAccountDAO;
import cn.facebook.domain.userAccount.UserAccountModel;
import cn.facebook.service.userAccount.IUserAccountService;

@Service
@Transactional
public class UserAccountServiceImpl implements IUserAccountService{

	@Autowired
	private IUserAccountDAO userAccountDao;
	@Override
	public UserAccountModel findByUserId(int id) {
		return userAccountDao.findByUserId(id);
	}

}
