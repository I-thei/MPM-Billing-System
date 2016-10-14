import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("serial")
public class MainWindow extends JPanel implements ActionListener {

	int elec1 = 0;
	int elec2 = 0;
	int elec3 = 0;
	int water;
	int tempid = 1;
	int compWidth = 120;
	int compHeight = 25;
	int compPadding = 10;
	int padding = 25;
	final int WIDTH = 800;
	final int HEIGHT = 600;

	JButton add, settings;

	@SuppressWarnings("rawtypes")
	JComboBox sections;

	JTextField searchfield;

	JTable list;

	JScrollPane tablesp;

	JLabel sectionsl;

	public static String[] sectionList = { "ALL SECTIONS", "1ST FLOOR", "2ND FLOOR", "MEAT", "FOOD", "CHICKEN", "FISH",
			"VEGETABLE", "FRUIT", "GROCERY", "SPECIAL", "MOBILE" };
	String[] columnNames = { "Store Number", "Store Name", "Store Section", "Store Owner" };

	ArrayList<String[]> data = new ArrayList<>();

	DefaultTableModel model = new DefaultTableModel();

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JFrame f = new JFrame("Marikina Public Market Electricity and Water Database");
		f.setSize(800, 600);

		f.add(new MainWindow());

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);
		f.setLocationRelativeTo(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MainWindow() {
		
		model.setColumnIdentifiers(columnNames);

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		int i = 1;

		add = new JButton("Add Store");
		add.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth, compHeight);
		add.addActionListener(this);

		i++;

		settings = new JButton("Settings");
		settings.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth,
				compHeight);
		settings.addActionListener(this);

		i++;

		sectionsl = new JLabel("Sections: ");
		sectionsl.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth,
				compHeight);

		i++;

		sections = new JComboBox(sectionList);
		sections.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth,
				compHeight);

		searchfield = new JTextField("", 255);
		searchfield.setBounds(padding, padding, WIDTH - padding * 2, compHeight);
		searchfield.addActionListener(this);

		list = new JTable() {

			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};
		list.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				// int column = table.columnAtPoint(p);
				if (me.getClickCount() == 2 && row != -1) {

					createBillWindow(row);
				}
			}
		});
		list.setModel(model);
		MatteBorder b = new MatteBorder(1, 1, 1, 1, Color.BLACK);
		list.setBorder(b);

		getData("Stores");
		

		tablesp = new JScrollPane(list);
		tablesp.setBounds(padding, padding + compPadding + compHeight, WIDTH - padding * 2 - compWidth - compPadding,
				HEIGHT - padding * 3 - compPadding * 2 - compHeight);

		add(add);
		add(settings);
		add(sections);
		add(sectionsl);
		add(searchfield);
		add(tablesp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equalsIgnoreCase("Add Store")) {

			new AddStoreWindow(this, tempid);
		} else if (e.getActionCommand().equalsIgnoreCase("Settings")) {

			new SettingsWindow(this);
		} else if (e.getSource().getClass().toString().equals("class javax.swing.JTextField")) {

			// Do search query
		}
	}

	public String[][] convertList(ArrayList<String[]> data) {

		String[][] newData = new String[data.size()][columnNames.length];
		for (int i = 0; i < data.size(); i++) {

			newData[i] = data.get(i);
		}

		return newData;
	}

	@SuppressWarnings("resource")
	public void getData(String filename) {
		
		File f = new File("res/files/" + filename + ".xlsx");
		
		try {
			
			FileInputStream fis = new FileInputStream(f);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			
			rowIterator.next();
			
			while(rowIterator.hasNext()) {
				
				Row row = rowIterator.next();
				
				Iterator<Cell> cellIterator = row.cellIterator();
				
				String[] entry = new String[4];
				
				int i = 0;
				
				while(cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					
					entry[i] = cell.toString();
					
					i++;
				}
				
				addEntry(entry);
			}
		} catch (FileNotFoundException e) {

			System.out.println("File Not Found");
			e.printStackTrace();
		} catch (IOException e) {

			System.out.println("Input/Output Error");
			e.printStackTrace();
		}
	}
	
	public void addEntry(String[] entry) {

		data.add(entry);
		model.addRow(entry);
		tempid++;
	}

	public void createBillWindow(int row) {

		new StoreBillWindow(this, data.get(row));
	}

	public void setSettings(int e1, int e2, int e3) {

		elec1 = e1;
		elec2 = e2;
		elec3 = e3;
	}
}