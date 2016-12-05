package cn.facebook.dao.bankcardInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.facebook.domain.bankCardInfo.BankCardInfo;

public interface IBankCardInfoDAO extends JpaRepository<BankCardInfo, Integer>{

	BankCardInfo findByUserId(int userid);

}
