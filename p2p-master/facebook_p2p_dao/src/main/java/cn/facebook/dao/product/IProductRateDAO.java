package cn.facebook.dao.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.facebook.domain.ProductEarningRate;

public interface IProductRateDAO extends JpaRepository<ProductEarningRate, Integer>{
	
	List<ProductEarningRate> findByProductId(int parseInt);
	
	@Modifying
	@Query("delete from ProductEarningRate per where per.productId=?1")
	void deleteByProId(int proId);
	
	@Modifying
	@Query("from ProductEarningRate per where per.productId=?1")
	List<ProductEarningRate> findByOne(int pid);
}
