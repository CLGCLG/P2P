package cn.facebook.poi.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class PoiDemo {

	@Test
	public void readExcel() throws IOException {
		FileInputStream fis = new FileInputStream("d:/a.xlsx");//文件输出流
		
		XSSFWorkbook book = new XSSFWorkbook(fis);
		
		XSSFSheet sheet = book.getSheetAt(0);
		
		XSSFRow row0 = sheet.getRow(0);
		
		for (int i = 0;i < row0.getLastCellNum(); i++) {
			System.out.print(row0.getCell(i).getStringCellValue()+ "\t");
		}
		System.out.println();
		for (int i = 1; i<=sheet.getLastRowNum();i++) {
			XSSFRow row = sheet.getRow(i);
			for(int j = 0;j<row.getLastCellNum();j++) {
				XSSFCell cell = row.getCell(j);
				switch (cell.getCellType()){
				case XSSFCell.CELL_TYPE_STRING:
					System.out.print(cell.getStringCellValue()+"\t");
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out.print(cell.getNumericCellValue()+"\t");
					break;
				}
			}
			System.out.println();
		}
	}
	@Test
	public void writeExcel() throws IOException {
		FileOutputStream fos = new FileOutputStream("d:/a.xlsx");//文件输出流
		
		XSSFWorkbook book = new XSSFWorkbook();
		
		XSSFSheet sheet = book.createSheet();
		
		XSSFRow row0 = sheet.createRow(0);
		String[] title = {"姓名","年龄","性别"};
		for (int i = 0;i < title.length; i++) {
			XSSFCell cell = row0.createCell(i);
			cell.setCellValue(title[i]);
		}
		for (int i = 1; i<=3;i++) {
			XSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue("姓名" +i);
			row.createCell(1).setCellValue(20 +i);
			row.createCell(2).setCellValue("male");
		}
		book.write(fos);
		fos.close();
	}
}
