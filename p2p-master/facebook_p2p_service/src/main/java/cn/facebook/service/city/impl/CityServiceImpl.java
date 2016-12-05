package cn.facebook.service.city.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.facebook.dao.city.ICityDAO;
import cn.facebook.domain.city.City;
import cn.facebook.service.city.ICityService;

@Service
@Transactional
public class CityServiceImpl implements ICityService {

	@Autowired
	private ICityDAO cityDao;
	@Override
	public List<City> findProvince() {
		return cityDao.findByParentCityAreaNumIsNull();
	}
	@Override
	public List<City> findByParentCityAreaNum(String cityAreaNum) {
		// TODO Auto-generated method stub
		return cityDao.findByParentCityAreaNum(cityAreaNum);
	}

}
