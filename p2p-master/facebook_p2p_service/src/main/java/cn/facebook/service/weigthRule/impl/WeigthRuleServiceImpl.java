package cn.facebook.service.weigthRule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.weightRule.IWeightRuleDAO;
import cn.facebook.domain.matchManagement.WeigthRule;
import cn.facebook.service.weigthRule.IWeigthRuleService;


@Service
@Transactional
public class WeigthRuleServiceImpl implements IWeigthRuleService {

	@Autowired
	private IWeightRuleDAO weightRuleDao;

	@Override
	public WeigthRule findByWeigthType(int i) {
		return weightRuleDao.findByWeigthType(i);
	}

}
