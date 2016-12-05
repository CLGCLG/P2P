package cn.facebook.service.creditor.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.creditor.ICreditor4SqlDAO;
import cn.facebook.dao.creditor.ICreditorDAO;
import cn.facebook.domain.creditor.CreditorModel;
import cn.facebook.service.creditor.ICreditorService;
import cn.facebook.util.constant.ClaimsType;

@Service
@Transactional
public class CreditorServiceImpl implements ICreditorService {

	@Autowired
	private ICreditorDAO creditorDao;
	
	@Autowired
	private ICreditor4SqlDAO creditor4SqlDao;
	@Override
	public void save(CreditorModel cm) {
		creditorDao.save(cm);
	}
	@Override
	public void addMultiple(List<CreditorModel> cms) {
		for (CreditorModel c:cms){
			creditorDao.save(c);
		}
		
	}
	@Override
	public List<CreditorModel> findCreditorList(Map<String, Object> map) {
		return creditor4SqlDao.findCreditorList(map);
	}
	@Override
	public Object[] findCreditorListSum(Map<String, Object> map) {
		return creditor4SqlDao.findCreditorListSum(map);
	}
	@Override
	public void checkCreditor(String ids) {
		//1、根据id查询债权
		String[] str = ids.split(",");
		//2、修改债权状态
		for (String st:str) {
			CreditorModel cm = creditorDao.findOne(Integer.parseInt(st.trim()));
			cm.setDebtStatus(ClaimsType.CHECKED);
		}
	}

}
