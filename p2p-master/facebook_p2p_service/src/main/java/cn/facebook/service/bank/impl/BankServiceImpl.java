package cn.facebook.service.bank.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.bank.IBankDao;
import cn.facebook.domain.bankCardInfo.Bank;
import cn.facebook.service.bank.IBankService;

@Service
@Transactional
public class BankServiceImpl implements IBankService {

	@Autowired
	private IBankDao bankDao;
	@Override
	public List<Bank> findAll() {
		// TODO Auto-generated method stub
		return bankDao.findAll();
	}

}
