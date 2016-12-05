package cn.facebook.service.productRate;

import java.util.List;

import cn.facebook.domain.ProductEarningRate;

public interface IProductRateService {

	List<ProductEarningRate> findByPid(String pid);

}
