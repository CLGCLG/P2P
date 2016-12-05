package cn.facebook.service.expectedReturn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.expectedReturn.IExpectedReturnDAO;
import cn.facebook.domain.productAcount.ExpectedReturn;
import cn.facebook.service.expectedReturn.IExpectedReturnService;

@Service
public class ExpectedReturnServiceImpl implements IExpectedReturnService {

	@Autowired
	private IExpectedReturnDAO expectedReturnDao;

	@Transactional
	@Override
	public void add(ExpectedReturn er) {

		expectedReturnDao.save(er);

	}
}
