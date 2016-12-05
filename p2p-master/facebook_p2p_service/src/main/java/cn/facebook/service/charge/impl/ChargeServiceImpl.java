package cn.facebook.service.charge.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.userAccount.IUserAccountDAO;
import cn.facebook.service.charge.IChargeService;
import cn.facebook.utils.HttpClientUtil;

@Service
@Transactional
public class ChargeServiceImpl implements IChargeService{

	@Autowired
	private IUserAccountDAO userAccountDao;
	
	@Override
	public boolean charge(double money, String bankCardNum, int userid) {
		// 1.调用httpclient向银行发送请求,进行充值操作
		// 参数map就是发送post请求时的参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("money", money);
		map.put("banCardNum", bankCardNum);
		
		String returnValue = HttpClientUtil.visitWebService(map, "bank_url");
		boolean flag = Boolean.parseBoolean(returnValue);
		if (flag) {
			userAccountDao.updateAccount(money, userid);
			return true;
		}
		return false;
	}
}
