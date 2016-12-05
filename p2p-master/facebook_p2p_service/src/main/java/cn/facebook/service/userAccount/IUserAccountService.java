package cn.facebook.service.userAccount;

import cn.facebook.domain.userAccount.UserAccountModel;

public interface IUserAccountService {

	UserAccountModel findByUserId(int id);
}
