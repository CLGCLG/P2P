package cn.facebook.service.bankcardInfo;

import cn.facebook.domain.bankCardInfo.BankCardInfo;

public interface IBankCardInfoService {

	BankCardInfo findByUserId(int userid);

	void save(BankCardInfo bankCardInfo);


}
