package cn.facebook.service.user;

import cn.facebook.domain.user.UserModel;

public interface IUserService {

	public UserModel findByUsername(String username);
	
	public UserModel findByPhone(String phone);

	public boolean addUser(UserModel user);

	public UserModel login(String username, String pwd);

	public UserModel findById(int userid);

	public void updatePhoneStatus(String phone, int userid);

	public UserModel findByIdentity(String identity);

	public void updateRealNameStatus(String identity, String realName, int userid);

	public void addEmail(String email, int userid);

	public void updateEmailStatus(int parseInt);
}
