package cn.facebook.dao.fundingNotMatched;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.facebook.domain.productAcount.FundingNotMatchedModel;


public interface IFundingNotMatchedDAO extends JpaRepository<FundingNotMatchedModel, Integer> {

	@Query("select fnm from FundingNotMatchedModel fnm where fnm.fIsLocked=10901")
	List<FundingNotMatchedModel> findNotMatchedList();

	@Modifying
	@Query("update FundingNotMatchedModel fnm set fnm.fNotMatchedMoney=0,fnm.fIsLocked=10905,fnm.fMatchedMoney=?2 where fnm.fId=?1")
	void match(int getfId, Double getfNotMatchedMoney);

	

}
