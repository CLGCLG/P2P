package cn.facebook.service.bankcardInfo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.bankcardInfo.IBankCardInfoDAO;
import cn.facebook.domain.bankCardInfo.BankCardInfo;
import cn.facebook.service.bankcardInfo.IBankCardInfoService;

@Service
@Transactional
public class BankCardInfoServiceImpl implements IBankCardInfoService {

	@Autowired
	private IBankCardInfoDAO bankCardInfoDao;
	@Override
	public BankCardInfo findByUserId(int userid) {
		// TODO Auto-generated method stub
		return bankCardInfoDao.findByUserId(userid);
	}
	@Override
	public void save(BankCardInfo bankCardInfo) {
		bankCardInfoDao.save(bankCardInfo);
	}

}
