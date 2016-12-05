package cn.facebook.dao.product;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.facebook.domain.Product;

public interface IProductDAO extends JpaRepository<Product, Long>{

}
