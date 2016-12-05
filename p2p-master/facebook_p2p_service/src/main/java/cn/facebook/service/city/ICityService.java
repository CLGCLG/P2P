package cn.facebook.service.city;

import java.util.List;

import cn.facebook.domain.city.City;

public interface ICityService {

	List<City> findProvince();

	List<City> findByParentCityAreaNum(String cityAreaNum);

}
