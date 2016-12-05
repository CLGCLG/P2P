package cn.facebook.dao.accountLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.facebook.domain.accountLog.AccountLog;


public interface IAccountLogDAO extends JpaRepository<AccountLog, Integer> {

	@Query("select fnm.fFoundingWeight,um.username,pa.pSerialNo, pa.pProductName,al.aDate,pa.pDeadline,al.aCurrentPeriod,fnm.fNotMatchedMoney,fnm.fFoundingType from UserModel um,  ProductAccount pa,  AccountLog al, FundingNotMatchedModel fnm  where um.id=pa.pUid  and pa.pId=al.pId  and fnm.fInvestRecordId=al.pId and pa.pStatus='10901' and fnm.fFoundingType in (124,125,126,127)")
	List<Object[]> finWaitMoneyList();

	@Query("select count(1),sum(fnm.fNotMatchedMoney) from UserModel um,  ProductAccount pa,  AccountLog al, FundingNotMatchedModel fnm  where um.id=pa.pUid  and pa.pId=al.pId  and fnm.fInvestRecordId=al.pId and pa.pStatus='10901' and fnm.fFoundingType in (124,125,126,127)")
	List<Object[]> finWaitMoneySum();

	

}
