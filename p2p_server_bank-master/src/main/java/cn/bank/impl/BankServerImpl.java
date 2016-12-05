package cn.bank.impl;

import cn.bank.IBankServer;

public class BankServerImpl implements IBankServer {

	@Override
	public boolean charge(String param) {
		System.out.println("银行端接收到的数据:" + param);
		return true;
	}

	@Override
	public void show() {
		System.out.println("bank show ....");
	}

}
