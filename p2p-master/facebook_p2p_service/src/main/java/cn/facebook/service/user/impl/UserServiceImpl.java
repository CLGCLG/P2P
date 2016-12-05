package cn.facebook.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.user.IUserDAO;
import cn.facebook.dao.userAccount.IUserAccountDAO;
import cn.facebook.domain.user.UserModel;
import cn.facebook.domain.userAccount.UserAccountModel;
import cn.facebook.service.user.IUserService;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDAO userDao;
	
	@Autowired
	private IUserAccountDAO userAccountDao;
	
	@Override
	public UserModel findByUsername(String username) {
		return userDao.findByUsername(username);
	}
	@Override
	public UserModel findByPhone(String phone) {
		return userDao.findByPhone(phone);
	}
	@Override
	public boolean addUser(UserModel user) {
		UserModel userModel = userDao.save(user);
		if (userModel == null) {
			return false;
		}
		UserAccountModel uam = new UserAccountModel();
		uam.setUserId(user.getId());
		UserAccountModel returvalue2 = userAccountDao.save(uam);
		if (returvalue2 == null) {
			return false;
		}
		return true;
	}
	@Override
	public UserModel login(String username, String pwd) {
		UserModel model = userDao.findByUsernameAndPassword(username,pwd);
		System.out.println(model);
		return model;
	}
	@Override
	public UserModel findById(int userid) {
		return userDao.findOne(userid);
	}
	@Override
	public void updatePhoneStatus(String phone, int userid) {
		userDao.updatePhoneStatus(phone,userid);
	}
	@Override
	public UserModel findByIdentity(String identity) {
		return userDao.findByIdentity(identity);
	}
	@Override
	public void updateRealNameStatus(String identity, String realName, int userid) {
		userDao.updateRealNameStatus(identity, realName,userid);
	}
	@Override
	public void addEmail(String email, int userid) {
		userDao.addEmail(email,userid);
	}
	@Override
	public void updateEmailStatus(int parseInt) {
		userDao.addEmailStatus(parseInt);
	}

}
