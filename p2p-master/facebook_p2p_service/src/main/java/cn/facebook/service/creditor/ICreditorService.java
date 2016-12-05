package cn.facebook.service.creditor;

import java.util.List;
import java.util.Map;

import cn.facebook.domain.creditor.CreditorModel;

public interface ICreditorService {

	void save(CreditorModel cm);

	void addMultiple(List<CreditorModel> cms);

	List<CreditorModel> findCreditorList(Map<String, Object> map);

	Object[] findCreditorListSum(Map<String, Object> map);

	void checkCreditor(String ids);

}
