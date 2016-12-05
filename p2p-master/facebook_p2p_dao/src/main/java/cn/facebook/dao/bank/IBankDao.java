package cn.facebook.dao.bank;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.facebook.domain.bankCardInfo.Bank;

public interface IBankDao extends JpaRepository<Bank, Integer> {

}
