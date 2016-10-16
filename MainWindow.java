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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	public static String[] sectionList = { "ALL SECTIONS", "1ST FLOOR", "2ND FLOOR",
    "MEAT", "FOOD", "CHICKEN", "FISH",	"VEGETABLE", "FRUIT", "GROCERY",
    "SPECIAL", "MOBILE" };

	int WIDTH = 800;
	int HEIGHT = 600;
	int compWidth = 120;
	int compHeight = 32;
	int compPadding = 10;
	int padding = 25;
	
	int lastId = 1;
	double evat = 12;
	double echarge = 12;
	
 	DataModel model, e_model, w_model;
	ArrayList<String[]> data, e_data, w_data;
	DefaultTableModel tableModel;

	@SuppressWarnings("rawtypes")
	JButton add, settings;
	JComboBox sections;
	JTextField searchfield;
	JTable list;
	JScrollPane tablesp;
	JLabel sectionsl;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
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
    model = DataModel.Stores;
	e_model = DataModel.Electric;
	w_model = DataModel.Water;

    e_data = e_model.getAll();
	w_data = w_model.getAll();
    data = model.getAll(); 

    if(data.size() > 0) lastId = Integer.parseInt(data.get(data.size()-1)[0]) + 1;
    	tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(model.getAttributes());

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		int i = 1;

		add = new JButton("Add Store");
		add.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i,
      compWidth, compHeight);
		add.addActionListener(this);
		i++;

		settings = new JButton("Settings");
		settings.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i,
      compWidth, compHeight);
		settings.addActionListener(this);
		i++;

		sectionsl = new JLabel("Sections: ");
		sectionsl.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i,
      compWidth, compHeight);
		i++;

		sections = new JComboBox(sectionList);
		sections.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i,
      compWidth, compHeight);

		searchfield = new JTextField("", 255);
		searchfield.setBounds(padding, padding, WIDTH - padding * 2, compHeight);
		searchfield.addActionListener(this);


		for(String[] d : data){
			tableModel.addRow(d);
		}

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
				int column = table.columnAtPoint(p);
				if (me.getClickCount() == 2 && row != -1) {
					createBillWindow(row);
				}
			}
		});

		list.setModel(tableModel);
		MatteBorder b = new MatteBorder(1, 1, 1, 1, Color.BLACK);
		list.setBorder(b);

		tablesp = new JScrollPane(list);
		tablesp.setBounds(padding, padding + compPadding + compHeight,
      WIDTH - padding * 2 - compWidth - compPadding,
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
			new AddStoreWindow(this, lastId);
		} else if (e.getActionCommand().equalsIgnoreCase("Settings")) {
			new SettingsWindow(this);
		} else if (e.getSource().getClass().toString().equals("class javax.swing.JTextField")) {
		}
	}

	@SuppressWarnings({ "resource", "unused" })
	public StoreBillWindow createBillWindow(int row) {
		return new StoreBillWindow(this, data.get(row));
	}

	public void setSettings(double v, double c) {
    evat = v;
    echarge = c;
	}

}
