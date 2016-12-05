package cn.facebook.dao.productAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.facebook.domain.ProductAccount;


public interface IProductAccountDAO extends JpaRepository<ProductAccount, Integer>,JpaSpecificationExecutor<ProductAccount> {

}
