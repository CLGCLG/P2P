package cn.facebook.poi.test;
import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.facebook.poi.utils.PoiHandler;
import cn.facebook.poi.utils.PoiUtils;
import cn.facebook.poi.vo.User;

public class UserPoiTest {

	public static void main(String[] args) throws IOException {
		
		File file = new File("d:/a.xlsx");
		
		PoiUtils<User> pu = new PoiUtils<User>();
		
		List<User> users = pu.readExcel(file, new PoiHandler<User>() {

			public User invoke(List<Object> list) {
				User user = new User();
				user.setName(String.valueOf(list.get(0)));
				user.setAge((int)Double.parseDouble(String.valueOf(list.get(1))));
				user.setSex(String.valueOf(list.get(2)));
				return user;
			}
		});
		System.out.println(users);
	}
}
