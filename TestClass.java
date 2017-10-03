//For testing

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestClass {

	@SuppressWarnings("resource")
	public TestClass(String s) {
		
		File f = new File("res/files/" + s + ".xlsx");
		
		try {
			
			FileInputStream fis = new FileInputStream(f);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext()) {
				
				Row row = rowIterator.next();
				
				Iterator<Cell> cellIterator = row.cellIterator();
				
				while(cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					
					System.out.print(cell.toString() + " ");
				}

				System.out.println("");
			}
		} catch (FileNotFoundException e) {

			System.out.println("File Not Found");
			e.printStackTrace();
		} catch (IOException e) {

			System.out.println("Input/Output Error");
			e.printStackTrace();
		}
		
	}
}
