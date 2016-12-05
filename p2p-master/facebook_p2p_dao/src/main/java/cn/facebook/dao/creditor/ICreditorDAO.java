package cn.facebook.dao.creditor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.facebook.domain.creditor.CreditorModel;

public interface ICreditorDAO extends JpaRepository<CreditorModel, Integer> {

	@Query("select cm from CreditorModel cm where cm.debtStatus=11302 and cm.matchedStatus in (11403,11401)")
	List<CreditorModel> findNotMatchedList();

	@Modifying
	@Query("update CreditorModel cm set cm.matchedStatus=11402,cm.availableMoney=0,cm.matchedMoney=?2 where cm.id=?1")
	void match(Integer id, double availableMoney);

}
