package cn.facebook.dao.userAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.facebook.domain.userAccount.UserAccountModel;

public interface IUserAccountDAO extends JpaRepository<UserAccountModel, Integer> {

	UserAccountModel findByUserId(int id);

	@Modifying
	@Query("update UserAccountModel um set um.total=um.total+?1,um.balance=um.balance+?1 where um.userId=?2")
	void updateAccount(double money, int userid);

	@Modifying
	@Query("update UserAccountModel ua set ua.balance=?1,ua.inverstmentW=?2,ua.interestTotal=?3,ua.recyclingInterest=?4,ua.inverstmentA=?5 where ua.id=?6")
	void updateUserAccountById(double balance, double inverstmentW, double interestTotal, double recyclingInterest,
			double inverstmentA, Integer id);
}
