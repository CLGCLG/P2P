package cn.facebook.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.product.IProductDAO;
import cn.facebook.dao.product.IProductRateDAO;
import cn.facebook.domain.Product;
import cn.facebook.domain.ProductEarningRate;
import cn.facebook.service.product.IProductService;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductDAO productDao;
	
	@Autowired
	private IProductRateDAO productRateDao;
	@Override
	public List<Product> findAll() {
		return productDao.findAll();
	}
	@Override
	public Product findById(long parseLong) {
		// TODO Auto-generated method stub
		return productDao.findOne(parseLong);
	}
	@Override
	public List<ProductEarningRate> findRateByPid(String pid) {
		// TODO Auto-generated method stub
		return productRateDao.findByProductId(Integer.parseInt(pid));
	}
	@Override
	public void modifyProduct(Product p) {
		productRateDao.deleteByProId((int) p.getProId());
		productRateDao.save(p.getProEarningRate());
		productDao.save(p);
	}

}
