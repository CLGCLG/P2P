package cn.facebook.poi.utils;

import java.util.List;

//这个接口定义了数据如何封装
public interface PoiHandler<T> {

	public T invoke(List<Object> list);
}
