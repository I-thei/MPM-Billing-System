import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
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
	public static String[] sectionList = { "ALL SECTIONS", "1ST FLOOR", "2ND FLOOR", "MEAT", "FOOD", "CHICKEN", "FISH",	"VEGETABLE", "FRUIT", "GROCERY", "SPECIAL", "MOBILE" };

	int WIDTH = 800;
	int HEIGHT = 600;
	int compWidth = 120;
	int compHeight = 32;
	int compPadding = 10;
	int padding = 25;
	
	int lastId = 1;
	double e_kwh = 8.0;
	double w_firstTenCubic= 314.33;
	double w_remainingCubic = 31.47;
	double w_echarge = 12;
	double w_evat= 12;
	double w_maintenancecharge= 6;
	
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

		} catch (Exception e) {
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

    	lastId = model.getNextRowNum();
    	tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(model.getAttributes());

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		int i = 1;

		add = new JButton("Add Store");
		add.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth, compHeight);
		add.addActionListener(this);
		i++;

		settings = new JButton("Settings");
		settings.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth, compHeight);
		settings.addActionListener(this);
		i++;

		sectionsl = new JLabel("Sections: ");
		sectionsl.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth, compHeight);
		i++;

		sections = new JComboBox(sectionList);
		sections.setBounds(WIDTH - compWidth - padding, padding + compHeight * i + compPadding * i, compWidth, compHeight);
		sections.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent ie) {
            	updateTable(data);
        	}
    	});

		searchfield = new JTextField("", 255);
		searchfield.setBounds(padding, padding, WIDTH - padding * 2, compHeight);
		searchfield.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent f) {
        		searchfield.setText("");
        		searchfield.setForeground(Color.BLACK);
    		}
			public void focusLost(FocusEvent f) {
        		searchfield.setText("Search Store");
        		searchfield.setForeground(Color.GRAY);
    		}
		});	
		searchfield.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent k){
				int ke = k.getKeyCode();
				if(ke == KeyEvent.VK_ENTER || ke == KeyEvent.VK_BACK_SPACE) search(searchfield.getText());
			}

			public void keyTyped(KeyEvent k){
				search(searchfield.getText());
			}

			public void keyReleased(KeyEvent k){}
		});
		
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
		tablesp.setBounds(padding, padding + compPadding + compHeight, WIDTH - padding * 2 - compWidth - compPadding, HEIGHT - padding * 3 - compPadding * 2 - compHeight);

		updateTable(data);

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
			new AddStoreWindow(this, lastId).nametf.requestFocus();
		} else if (e.getActionCommand().equalsIgnoreCase("Settings")) {
			new SettingsWindow(this);
		} else if (e.getSource().getClass().toString().equals("class javax.swing.JTextField")) {
		}
	}

	@SuppressWarnings({ "resource", "unused" })
	public StoreBillWindow createBillWindow(int row) {
		return new StoreBillWindow(this, data.get(row));
	}

	public void setSettings(double w, double f, double r, double v, double c, double m) {
	    e_kwh = w;
	    w_firstTenCubic = f;
	    w_remainingCubic = r;
	    w_echarge = c;
	    w_evat = v;
	    w_maintenancecharge = m;
	}

	public void search(String search_item){
		ArrayList<String[]> temp_data = new ArrayList<String[]>();
		for(String[] d : data) if(d[2].toLowerCase().contains(search_item.toLowerCase())) temp_data.add(d);
		updateTable(temp_data);
		revalidate();
	}

	public void updateTable(ArrayList<String[]> data){	
		String s = String.valueOf(sections.getSelectedItem());
		tableModel.setRowCount(0);
		if(s.equals("ALL SECTIONS")) {
			for(String[] d : data) tableModel.addRow(d);

		} else {
			for(String[] d : data) if(d[1].equals(s)) tableModel.addRow(d);
		}
	}

}
