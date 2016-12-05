package cn.facebook.service.bank;

import java.util.List;

import cn.facebook.domain.bankCardInfo.Bank;

public interface IBankService {

	List<Bank> findAll();

}
