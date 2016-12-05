package cn.facebook.dao.creditor.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.facebook.dao.creditor.ICreditor4SqlDAO;
import cn.facebook.domain.creditor.CreditorModel;

@Repository
public class ICreditor4SqlDAOImpl implements ICreditor4SqlDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<CreditorModel> findCreditorList(Map<String, Object> map) {
		String sql = "select a.* "
		             +"  from t_debt_info a, (select rownum rn, d_id from t_debt_info) b "
		             +" where 1=1 ";
		String dDebtNo = (String) map.get("dDebtNo");
		if (StringUtils.isNotBlank(dDebtNo)) {
			sql += " and d_debt_no='" + dDebtNo + "'";
		}
		String dContractNo = (String) map.get("dContractNo");
		if (StringUtils.isNotBlank(dDebtNo)) {
			sql += " and d_contract_No='" + dContractNo + "'";
		}
		
        String dDebtTransferredDateStart = (String) map.get("dDebtTransferredDateStart");
        String dDebtTransferredDateEnd = (String) map.get("dDebtTransferredDateEnd");
        
        if (StringUtils.isNotBlank(dDebtTransferredDateStart) && StringUtils.isNotBlank(dDebtTransferredDateEnd)) {
			sql += " and d_debt_Transferred_Date between to_date('" + dDebtTransferredDateStart
					+ "','yyyy-mm-dd') and to_date('" + dDebtTransferredDateEnd + "','yyyy-mm-dd')";
		}
        
        Integer dDebtStatus = (Integer) map.get("dDebtStatus");
		if (dDebtStatus != null && dDebtStatus != 0) {
			sql += " and d_debt_Status='" + dDebtStatus + "'";
		}
		Integer dMatchedStatus = (Integer) map.get("dMatchedStatus");
		if (dMatchedStatus != null && dMatchedStatus != 0) {
			sql += " and d_matched_Status='" + dMatchedStatus + "'";
		}
        int offsetnum = (int) map.get("offsetnum");
        int start = (offsetnum - 1) * 3;
		int end = start + 3;
		sql += " and a.d_id=b.d_id and b.rn>'" + start + "' and b.rn<='" + end + "'";
		if (StringUtils.isNotBlank(dDebtNo)) {
			sql += " and d_debt_no='" + dDebtNo + "'";
		}
		Query query = em.createNativeQuery(sql, CreditorModel.class);
		List<CreditorModel> list = query.getResultList();
		return list;                                                                     
	}

	@Override
	public Object[] findCreditorListSum(Map<String, Object> map) {
		String sql = "select count(d_id),count(d_debt_Money),count(d_available_Money)  "
	             +"  from t_debt_info a  "
	             +" where 1=1 ";
	  	String dDebtNo = (String) map.get("dDebtNo");
	  	if (StringUtils.isNotBlank(dDebtNo)) {
	  		sql += " and d_debt_no='" + dDebtNo + "'";
	  	}
	  	String dContractNo = (String) map.get("dContractNo");
	  	if (StringUtils.isNotBlank(dDebtNo)) {
	  		sql += " and d_contract_No='" + dContractNo + "'";
	  	}
	  	
	     String dDebtTransferredDateStart = (String) map.get("dDebtTransferredDateStart");
	     String dDebtTransferredDateEnd = (String) map.get("dDebtTransferredDateEnd");
	     
	     if (StringUtils.isNotBlank(dDebtTransferredDateStart) && StringUtils.isNotBlank(dDebtTransferredDateEnd)) {
	  		sql += " and d_debt_Transferred_Date between to_date('" + dDebtTransferredDateStart
	  				+ "','yyyy-mm-dd') and to_date('" + dDebtTransferredDateEnd + "','yyyy-mm-dd')";
	  	}
	     
	     Integer dDebtStatus = (Integer) map.get("dDebtStatus");
	  	if (dDebtStatus != null && dDebtStatus != 0) {
	  		sql += " and d_debt_Status='" + dDebtStatus + "'";
	  	}
	  	Integer dMatchedStatus = (Integer) map.get("dMatchedStatus");
	  	if (dMatchedStatus != null && dMatchedStatus != 0) {
	  		sql += " and d_matched_Status='" + dMatchedStatus + "'";
	  	}
	     
	  	Query query = em.createNativeQuery(sql);
	  	Object[] obj = (Object[]) query.getSingleResult();
	  	return obj; 
	  	}

}
