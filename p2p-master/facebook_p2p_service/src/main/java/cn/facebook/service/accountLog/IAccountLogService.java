package cn.facebook.service.accountLog;

import java.util.List;

import cn.facebook.domain.productAcount.WaitMatchMoneyModel;

public interface IAccountLogService {

	List<WaitMatchMoneyModel> findWaitMoneyList();

	WaitMatchMoneyModel findWaitMoneySum();

}
