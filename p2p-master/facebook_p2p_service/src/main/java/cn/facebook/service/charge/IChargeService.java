package cn.facebook.service.charge;

public interface IChargeService {

	boolean charge(double money, String bankCardNum, int userid);

}
