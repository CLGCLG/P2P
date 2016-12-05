package cn.facebook.service.accountLog.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.accountLog.IAccountLogDAO;
import cn.facebook.domain.productAcount.WaitMatchMoneyModel;
import cn.facebook.service.accountLog.IAccountLogService;

@Service
@Transactional
public class AccountLogServiceImpl implements IAccountLogService{

	@Autowired
	private IAccountLogDAO accountLogDao;
	
	@Override
	public List<WaitMatchMoneyModel> findWaitMoneyList() {
		List<WaitMatchMoneyModel> wmms = null;
		List<Object[]> list = accountLogDao.finWaitMoneyList();
		
		if (list != null && list.size()>0) {
			wmms = new ArrayList<WaitMatchMoneyModel>();
			
			for (Object[] obj : list) {
				WaitMatchMoneyModel wmm = new WaitMatchMoneyModel();
				wmm.setFundWeight((Integer)obj[0]);
				wmm.setUserName((String) obj[1]);
				wmm.setpSerialNo((String) obj[2]);
				wmm.setProductName((String) obj[3]);
				wmm.setDate((Date) obj[4]);
				wmm.setDeadline((Integer) obj[5]);
				wmm.setCurrentMonth((int) obj[6]);
				wmm.setAmountWait((Double) obj[7]);
				wmm.setInvestType((Integer) obj[8]);

				if (wmm.getInvestType() == 124) {
					wmm.setInvestTypeDescrible("新增投资");
				}
				if (wmm.getInvestType() == 125) {
					wmm.setInvestTypeDescrible("回款再投资");
				}
				if (wmm.getInvestType() == 126) {
					wmm.setInvestTypeDescrible("到期结清");
				}
				if (wmm.getInvestType() == 127) {
					wmm.setInvestTypeDescrible("提前结清");
				}
				// 处理时间显示
				wmm.setDateDescrible(new SimpleDateFormat("yyyy-MM-dd").format(wmm.getDate()));

				wmms.add(wmm);
			}
		} 
		return wmms;
	}

	@Override
	public WaitMatchMoneyModel findWaitMoneySum() {
		List<Object[]> objs = accountLogDao.finWaitMoneySum();
		
		if (objs == null || objs.size() ==0) {
			return null;
		}
		WaitMatchMoneyModel wmm = new WaitMatchMoneyModel();
		wmm.setCount(Integer.parseInt(objs.get(0)[0].toString()));
		wmm.setSum(Double.parseDouble(objs.get(0)[1].toString()));
		return wmm;
	}

}
