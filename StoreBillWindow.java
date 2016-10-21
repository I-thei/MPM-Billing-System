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

import javax.swing.*;
import javax.swing.table.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings({ "serial", "rawtypes" })
public class StoreBillWindow extends JPanel implements ActionListener {

	int e_row = -1;
	int w_row = -1;
	int compWidth = 250;
	int compHeight = 25;
	int compPadding = 10;
	int padding = 25;
	int tableHeight = 200;
	int buttonWidth = 200;
	static final int WIDTH = 780;
	static final int HEIGHT = 560;

	int e_id, w_id;
	String store_id, store_section, store_name, store_holder;

	ArrayList<String[]> e_data, w_data;

	DefaultTableModel e_tableModel = new DefaultTableModel();
	DefaultTableModel w_tableModel = new DefaultTableModel(); 

	JLabel idl, namel, sectionl, holderl, e_records, w_records;
	JLabel[] labels = new JLabel[4];

	JTable e_table, w_table;

	JFrame f;

	JButton generate, e_add, e_delete, w_add, w_delete;
	JButton[] e_buttons = new JButton[3];
	JButton[] w_buttons = new JButton[3];

	JComboBox report_type;

	String[] e_columnNames = { "#", "Date", "kWh", "Amount" };
	String[] w_columnNames = { "#","Date", "Cubic Meters", "Amount" };
	String[] report_types = { "Monthly", "Yearly" };

	MainWindow mw;

	@SuppressWarnings("unchecked")
	public StoreBillWindow(MainWindow mw, String[] store_data) {

		this.mw = mw;

		e_id = mw.e_model.getNextRowNum();
		w_id = mw.w_model.getNextRowNum();

		store_id = store_data[0];
		store_name = store_data[1];
		store_section = store_data[2];
		store_holder = store_data[3];

		f = new JFrame(store_name + " Electricity And Water Bills");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		idl = new JLabel("Store ID: " + store_id);
		namel = new JLabel("Store: " + store_name);
		sectionl = new JLabel("Section: " + store_section);
		holderl = new JLabel("Holder: " + store_holder);

		labels[0] = idl;
		labels[1] = namel;
		labels[2] = sectionl;
		labels[3] = holderl;

		int ii = 0;
		int jj = 0;
		for (int i = 0; i < labels.length; i++) {
			if (i % 2 == 0) {
				labels[i].setBounds(padding, padding + compPadding * 0 + compHeight * ii - compPadding, compWidth,
						compHeight);
				ii++;
			} else {
				labels[i].setBounds(padding + compPadding + compWidth,
						padding + compPadding * 0 + compHeight * jj - compPadding, compWidth, compHeight);
				jj++;
			}
			add(labels[i]);
		}

		e_records = new JLabel("Electricity");
		e_records.setBounds(padding, padding + compPadding * 1 + compHeight * 2, compWidth, compHeight);
		add(e_records);

		w_records = new JLabel("Water");
		w_records.setBounds(padding, padding + compPadding * 3 + compHeight * 9, compWidth, compHeight);
		add(w_records);

		e_tableModel.setColumnIdentifiers(e_columnNames);
		w_tableModel.setColumnIdentifiers(w_columnNames);

		e_table = new JTable() {
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};
		e_table.setModel(e_tableModel);
		e_table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				e_row = table.rowAtPoint(p);
				if(me.getClickCount() == 2 && e_row != -1) createEditWindow(e_data, e_row, "elec");
				e_delete.setEnabled(true);
			}
		});

		w_table = new JTable() {
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};
		w_table.setModel(w_tableModel);
		w_table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				w_row = table.rowAtPoint(p);
				if(me.getClickCount() == 2 && w_row != -1) createEditWindow(w_data, w_row, "water");
				w_delete.setEnabled(true);
			}
		});

		int e_table_y = padding + compHeight * 3 + compPadding * 1;
		int w_table_y = padding + compHeight * 10 + compPadding * 3;

		JScrollPane e_tablesp = new JScrollPane(e_table);
		e_tablesp.setBounds(padding, e_table_y, WIDTH - padding * 2 - compPadding - buttonWidth, compHeight * 6);

		JScrollPane w_tablesp = new JScrollPane(w_table);
		w_tablesp.setBounds(padding, w_table_y, WIDTH - padding * 2 - compPadding - buttonWidth, compHeight * 6);

		add(e_tablesp);
		add(w_tablesp);

		report_type = new JComboBox(report_types);
		report_type.setBounds(padding, HEIGHT - padding * 2 - compHeight, compWidth, compHeight);
		add(report_type);

		generate = new JButton("Generate Report");
		generate.setBounds(padding + compPadding + compWidth, HEIGHT - padding * 2 - compHeight, compWidth, compHeight);
		add(generate);

		e_add = new JButton(" Add ");
		e_add.setBounds(WIDTH - padding + compPadding / 2 - buttonWidth, e_table_y, buttonWidth - 10, compHeight);
		e_add.addActionListener(this);
		add(e_add);

		e_delete = new JButton(" Delete ");
		e_delete.setBounds(WIDTH - padding + compPadding / 2 - buttonWidth, e_table_y + compHeight + compPadding , buttonWidth - 10, compHeight);
		e_delete.addActionListener(this);
		e_delete.setEnabled(false);
		add(e_delete);

		w_add = new JButton("Add");
		w_add.setBounds(WIDTH - padding + compPadding / 2 - buttonWidth, w_table_y, buttonWidth - 10, compHeight);
		w_add.addActionListener(this);
		add(w_add);

		w_delete = new JButton("Delete");
		w_delete.setBounds(WIDTH - padding + compPadding / 2 - buttonWidth, w_table_y + compHeight + compPadding , buttonWidth - 10, compHeight);
		w_delete.addActionListener(this);
		w_delete.setEnabled(false);
		add(w_delete);

		for(String[] e : mw.e_data){
			if (e[1].equals(store_id)){
					String[] entry = {e[0], e[2], e[3], e[5]};
					e_tableModel.addRow(entry);
			}
		}

		for(String[] w : mw.w_data){
			if (w[1].equals(store_id)){
				String[] entry = {w[0],w[2], w[3], w[9]};
				w_tableModel.addRow(entry);
			} 
		}

		e_data  = new ArrayList<>();
		for(String[] e : mw.e_data) if(e[1].equals(store_id)) e_data.add(e);

		w_data = new ArrayList<>();
		for(String[] w : mw.w_data) if(w[1].equals(store_id))w_data.add(w);
		
		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	public void createEditWindow(ArrayList<String[]> data, int row, String s){
		boolean bool = false;
		if (s.equals("elec")){bool = true;}
			new AddOrEditBillWindow(this, bool, "edit", data.get(row)).input.requestFocus();
	}

	public void deleteSelectedRows(JTable table, DefaultTableModel tableModel, DataModel model, ArrayList<String[]> data){
		int[] selected = e_table.getSelectedRows();
		int row = selected[0];
		for (int i : selected) {
			model.remove(Integer.parseInt(data.remove(row)[0]));
			tableModel.removeRow(row);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(" Add ")) {
			new AddOrEditBillWindow(this, true, "add", null).input.requestFocus();

		} else if (e.getActionCommand().equals("Add")) {
			new AddOrEditBillWindow(this, false, "add", null).input.requestFocus();
		
		} else if (e.getActionCommand().equals(" Delete ")) {
			deleteSelectedRows(e_table, e_tableModel, mw.e_model, e_data);
			e_delete.setEnabled(false);

		} else if (e.getActionCommand().equals("Delete")) {
			deleteSelectedRows(w_table, w_tableModel, mw.w_model, w_data);
			w_delete.setEnabled(false);
		}


		revalidate();
		updateUI();
	}
	
	
}
