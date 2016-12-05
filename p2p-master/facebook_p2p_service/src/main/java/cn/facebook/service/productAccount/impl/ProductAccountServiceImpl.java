package cn.facebook.service.productAccount.impl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.accountLog.IAccountLogDAO;
import cn.facebook.dao.fundingNotMatched.IFundingNotMatchedDAO;
import cn.facebook.dao.productAccount.IProductAccountDAO;
import cn.facebook.dao.userAccount.IUserAccountDAO;
import cn.facebook.domain.ProductAccount;
import cn.facebook.domain.accountLog.AccountLog;
import cn.facebook.domain.productAcount.FundingNotMatchedModel;
import cn.facebook.domain.userAccount.UserAccountModel;
import cn.facebook.service.productAccount.IProductAccountService;
@Service
public class ProductAccountServiceImpl implements IProductAccountService{
	@Autowired
	private IProductAccountDAO productAccountDao;
	@Autowired
	private IAccountLogDAO accountLogDao;
	@Autowired
	private IFundingNotMatchedDAO fundingNotMatchedModelDao;
	@Autowired
	private IUserAccountDAO userAccountDao;
	@Transactional
	@Override
	public void addProductAccount(UserAccountModel userAccountModel, ProductAccount pa, AccountLog accountlog,
			FundingNotMatchedModel fnmm) {
		// 修改UserAccountModel表中数据
				userAccountDao.updateUserAccountById(userAccountModel.getBalance(), userAccountModel.getInverstmentW(), userAccountModel.getInterestTotal(),
						userAccountModel.getRecyclingInterest(), userAccountModel.getInverstmentA(), userAccountModel.getId());
				// 保存ProductAccount
				productAccountDao.save(pa);
				accountlog.setpId(pa.getpId());
				accountLogDao.save(accountlog);
				fnmm.setfInvestRecordId(pa.getpId());
				fundingNotMatchedModelDao.save(fnmm);
	}
	@Override
	public Page<ProductAccount> findProductAccountByPage(int page, int currentCount,final int uid,final int status,final String startDate,
			final String endDate) {
		Pageable pa = new PageRequest(page-1, currentCount);
		//Page<ProductAccount> p = productAccountDao.findAll(pa);
		Page<ProductAccount> p = productAccountDao.findAll(new Specification<ProductAccount>() {

			@Override
			public Predicate toPredicate(Root<ProductAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Path<Date> pBeginDate = root.get("pBeginDate");
				Path<Long> userId = root.get("pUid");
				Path<String> pStatus = root.get("pStatus");
				
				List<Predicate> list = new ArrayList<Predicate>();
				Predicate p1 = cb.equal(userId, uid);
				list.add(p1);
				Predicate p2 = cb.equal(pStatus, status);
				list.add(p2);
				
				try {
					if (StringUtils.isNotBlank(startDate)) {
						Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
						Predicate p3 = cb.greaterThanOrEqualTo(pBeginDate, start);
						list.add(p3);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					if (StringUtils.isNotBlank(endDate)) {
						Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
						Predicate p4 = cb.lessThanOrEqualTo(pBeginDate, end);
						list.add(p4);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				query.where(list.toArray(new Predicate[list.size()]));
				return null;
			}
			
		},pa);
		return p;
	}

}
