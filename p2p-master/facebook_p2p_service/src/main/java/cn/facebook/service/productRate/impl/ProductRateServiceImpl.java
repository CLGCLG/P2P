package cn.facebook.service.productRate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.product.IProductRateDAO;
import cn.facebook.domain.ProductEarningRate;
import cn.facebook.service.productRate.IProductRateService;

@Service
@Transactional
public class ProductRateServiceImpl implements IProductRateService {

	@Autowired
	private IProductRateDAO productRateDao;
	@Override
	public List<ProductEarningRate> findByPid(String pid) {
		List<ProductEarningRate> list = productRateDao.findByOne(Integer.parseInt(pid));
		return list;
	}

}
