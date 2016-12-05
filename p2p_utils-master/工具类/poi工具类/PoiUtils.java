package cn.facebook.poi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiUtils<T> {

	// 定义一个方法来读取excel文件中的内容
	public List<T> readExcel(File file, PoiHandler<T> handler) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		// 创建一个book
		XSSFWorkbook book = new XSSFWorkbook(fis);

		// 调用本类的一个方法获取List<List<Object>>
		List<List<Object>> list = readExcelToList(book);

		List<T> ts = new ArrayList<T>();

		for (List<Object> obj : list) {
			T t = handler.invoke(obj);
			ts.add(t);
		}

		return ts;
	}

	// 它就是将excel文件中每一行数据读取出来封装到List<Object>，将所有的行信息返回
	private List<List<Object>> readExcelToList(XSSFWorkbook book) {
		List<List<Object>> objs = new ArrayList<List<Object>>();

		XSSFSheet sheet = book.getSheetAt(0);

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			List<Object> obj = new ArrayList<Object>();// 用于装入一行数据
			XSSFRow _row = sheet.getRow(i);
			for (int j = 0; j < _row.getLastCellNum(); j++) {
				XSSFCell cell = _row.getCell(j);
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					obj.add(cell.getStringCellValue());
					break;

				case XSSFCell.CELL_TYPE_NUMERIC:
					obj.add(cell.getNumericCellValue());
					break;
				}
			}

			objs.add(obj);// 将一行信息封装到objs

		}

		return objs;
	}
}
