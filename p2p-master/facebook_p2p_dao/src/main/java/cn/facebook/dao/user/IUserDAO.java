package cn.facebook.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import cn.facebook.domain.user.UserModel;

public interface IUserDAO extends JpaRepository<UserModel, Integer>{

	UserModel findByUsername(String username);
	
	UserModel findByPhone(String phone);

	UserModel findByUsernameAndPassword(String username, String pwd);

	@Modifying
	@Query("update UserModel um set um.phone=?1,um.phoneStatus=1 where um.id=?2")
	void updatePhoneStatus(String phone, int userid);

	UserModel findByIdentity(String identity);
	
	@Modifying
	@Query("update UserModel um set um.identity=?1,um.realNameStatus=1,um.realName=?2 where um.id=?3")
	void updateRealNameStatus(String identity, String realName, int userid);

	@Modifying
	@Query("update UserModel um set um.email=?1 where um.id=?2")
	void addEmail(String email, int userid);

	@Modifying
	@Query("update UserModel um set um.emailStatus=1 where um.id=?1")
	void addEmailStatus(int parseInt);
}
