import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public enum DataModel {
  Stores(new String[]{"ID", "Section", "Name", "Holder"}),
  Electric(new String[]{"ID", "Store ID", "Date", "kWh", "Rate", "Amount"}),
  Water(new String[]{"ID", "Store ID", "Date", "Cubic meters", "Rate", "Amount"}),
  Settings(new String[]{"ID", "Value"});
  
  private final static String DIRECTORY = "";
  private final static String FILENAME = "data.xlsx";
  private static XSSFWorkbook workbook = null;

  private final static String ECHARGE = "12";
  private final static String EVAT = "12";

  private String[] attributes;
  private ArrayList<String[]> data;
  private int length;

  private DataModel(String[] a) {
    attributes = a;
    length = a.length;
    data = new ArrayList<String[]>();
  }

  public static boolean initialize() {
    try {
      FileInputStream in = new FileInputStream(new File(DIRECTORY + FILENAME));
      workbook = new XSSFWorkbook(in);
      Sheet  sheet = workbook.getSheetAt(0);
      return true;

    } catch (Exception e) {
      try {
        workbook = new XSSFWorkbook();
        System.out.println("Workbook");
        for (DataModel d : DataModel.values()) {
          Sheet sheet = workbook.createSheet(d.name());

          Row dataRow = sheet.createRow(0);
          String[] attrs = d.getAttributes();
          int i = 0;
          for (String s : attrs) {
            dataRow.createCell(i).setCellValue(s);
            i++;
          } 

          if (d.name() == "Settings") {
            Row echarge = sheet.createRow(1);
            Row evat = sheet.createRow(2);
            echarge.createCell(0).setCellValue("1");
            echarge.createCell(1).setCellValue(ECHARGE);
            evat.createCell(0).setCellValue("2");
            evat.createCell(1).setCellValue(EVAT);
          }
        }
        _write();

        System.out.println("File not found. Created new sheet.");
        e.printStackTrace();
        
        return true;
      } catch (Exception ie) {
        System.out.println("Can't create file.");
        ie.printStackTrace();

        return false;
      }
    }
  }

  public static boolean _write(){
    try {
      FileOutputStream out = new FileOutputStream(new File(DIRECTORY+FILENAME));
      workbook.write(out);
      out.close();
    }
    catch(Exception e){
        e.printStackTrace();
        return false;
    }
    return true;
  }

  private static boolean _checkInstance() {
    if (workbook == null) return initialize();
    else return true;
  }

  private void _getData() {
    if (data.size() > 1) return;
    Sheet sheet = workbook.getSheet(name());
    Iterator<Row> ri = sheet.iterator();
    ri.next();
    while (ri.hasNext()) {
      Row row = ri.next();
      Iterator<Cell> ci = row.iterator();
      String[] _row = new String[length];

      int i = 0;
      while (ci.hasNext()) {
        Cell cell = ci.next();
        _row[i] = cell.toString();
        i++;
      }
      if(!isRowEmpty(row)) data.add(_row);
    }
    return;
  }

  public boolean _edit(int id, String[] s) {
    if (_checkInstance()) _getData();
    Sheet sheet = workbook.getSheet(name());
    Row temp = sheet.createRow(id);
    for (int i = 0; i < length; i++) {
      temp.createCell(i);
      Cell cell = temp.getCell(i);
      if (s.length < length) cell.setCellValue("");
      else cell.setCellValue(s[i]);
    }
    _write();
    return true;
  }

  public boolean add(String[] s) {
    int id = workbook.getSheet(name()).getLastRowNum() + 1;
    String[] temp = new String[s.length + 1];
    temp[0] = Integer.toString(id);
    System.arraycopy(s, 0, temp, 1, s.length);
    data.add(s);
    return _edit(id, s);
  }

  public boolean edit(int id, String[] s) {
    int _id = Integer.parseInt(data.get(id)[0]);
    data.set(id, s);
    return _edit(_id, s);
  }

  public boolean remove(int id) {
    if (name() == "Stores" || name() == "Settings") return false;
    int _id = Integer.parseInt(data.remove(id)[0]);
    return _edit(_id, new String[]{"-1"});
  }

  public ArrayList<String[]> getAll() {
    if (_checkInstance()) _getData();
    return data;
  }

  public String[] getAttributes() {
    return attributes;
  }

  public static boolean close() {
    _checkInstance();
    try {
      workbook.close();

      return true;
    } catch (Exception e) {
      System.out.println("Failed to close file");
      e.printStackTrace();

      return false;
    }
  }

  public static boolean isRowEmpty(Row row) {
    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
        Cell cell = row.getCell(c);
        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && !cell.getStringCellValue().equals(""))
            return false;
    }
    return true;
}

  public int getNextRowNum() {
    int id = workbook.getSheet(name()).getLastRowNum() + 1;
    return id;
  }
}
