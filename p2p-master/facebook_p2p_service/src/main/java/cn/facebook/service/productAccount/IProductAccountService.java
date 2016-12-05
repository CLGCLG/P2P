package cn.facebook.service.productAccount;

import org.springframework.data.domain.Page;

import cn.facebook.domain.ProductAccount;
import cn.facebook.domain.accountLog.AccountLog;
import cn.facebook.domain.productAcount.FundingNotMatchedModel;
import cn.facebook.domain.userAccount.UserAccountModel;

public interface IProductAccountService {

	void addProductAccount(UserAccountModel userAccountModel, ProductAccount pa, AccountLog accountlog,
			FundingNotMatchedModel fnmm);



	Page<ProductAccount> findProductAccountByPage(int currentPage, int i, int uid, int status, String startDate,
			String endDate);;
}
