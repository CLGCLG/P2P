package cn.facebook.dao.expectedReturn;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.facebook.domain.productAcount.ExpectedReturn;


public interface IExpectedReturnDAO extends JpaRepository<ExpectedReturn, Integer> {

}
