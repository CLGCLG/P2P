package cn.facebook.service.product;

import java.util.List;

import cn.facebook.domain.Product;
import cn.facebook.domain.ProductEarningRate;

public interface IProductService {

	public List<Product> findAll();
	public Product findById(long parseLong);
	
	public List<ProductEarningRate> findRateByPid(String pid);
	
	public void modifyProduct(Product p);
}