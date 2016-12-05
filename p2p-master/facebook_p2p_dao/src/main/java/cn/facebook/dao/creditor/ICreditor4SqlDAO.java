package cn.facebook.dao.creditor;

import java.util.List;
import java.util.Map;

import cn.facebook.domain.creditor.CreditorModel;

public interface ICreditor4SqlDAO {

	List<CreditorModel> findCreditorList(Map<String, Object> map);

	Object[] findCreditorListSum(Map<String, Object> map);

}
