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

	DataModel e_model, w_model;

	DefaultTableModel e_tableModel = new DefaultTableModel();
	DefaultTableModel w_tableModel = new DefaultTableModel();

	JLabel idl, namel, sectionl, holderl, e_records, w_records;
	JLabel[] labels = new JLabel[4];

	JTable e_table, w_table;

	JFrame f;

	JButton generate, e_add, e_edit, e_delete, w_add, w_edit, w_delete;
	JButton[] e_buttons = new JButton[3];
	JButton[] w_buttons = new JButton[3];

	JComboBox report_type;

	String[] e_columnNames = { "Date", "kWh", "Amount" };
	String[] w_columnNames = { "Date", "Cubic Meters", "Amount" };
	String[] report_types = { "Monthly", "Yearly" };

	ArrayList<String[]> e_data = new ArrayList<>();
	ArrayList<String[]> w_data = new ArrayList<>();

	MainWindow mw;

	@SuppressWarnings("unchecked")
	public StoreBillWindow(MainWindow mw, String[] store_data) {

		this.mw = mw;

		e_model = DataModel.Electric;
		w_model = DataModel.Water;

		if(e_data.size() > 0) { e_id = Integer.parseInt(e_data.get(e_data.size()-1)[0]) + 1;}
		else e_id = 1;

		if(w_data.size() > 0) { e_id = Integer.parseInt(w_data.get(w_data.size()-1)[0]) + 1;}
		else w_id = 1;

		e_data = mw.e_data;
		w_data = mw.w_data;

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
		e_edit = new JButton(" Edit ");
		e_delete = new JButton(" Delete ");
		e_buttons[0] = e_add;
		e_buttons[1] = e_edit;
		e_buttons[2] = e_delete;

		for (int i = 0; i < e_buttons.length; i++) {
			e_buttons[i].setBounds(WIDTH - padding + compPadding / 2 - buttonWidth,
					e_table_y + compHeight * i + compPadding * i, buttonWidth - 10, compHeight);
			e_buttons[i].addActionListener(this);
			add(e_buttons[i]);
		}

		w_add = new JButton("Add");
		w_edit = new JButton("Edit");
		w_delete = new JButton("Delete");
		w_buttons[0] = w_add;
		w_buttons[1] = w_edit;
		w_buttons[2] = w_delete;

		for (int i = 0; i < e_buttons.length; i++) {
			w_buttons[i].setBounds(WIDTH - padding + compPadding / 2 - buttonWidth,
					w_table_y + compHeight * i + compPadding * i, buttonWidth - 10, compHeight);
			w_buttons[i].addActionListener(this);
			add(w_buttons[i]);
		}

		for (String[] e : e_data){
			if(e[1].equals(store_id)){
				String[] entry = {e[2], e[3], e[5]};
				e_tableModel.addRow(entry);
			}
		}

		for (String[] w : w_data){
			if(w[1].equals(store_id)){
				String[] entry = {w[2], w[3], w[5]};
				w_tableModel.addRow(entry);
			}
		}

		checkLists();

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}


	public void checkLists() {

		if (e_data.size() <= 0) {

			e_buttons[1].setEnabled(false);
			e_buttons[2].setEnabled(false);
		} else {

			e_buttons[1].setEnabled(true);
			e_buttons[2].setEnabled(true);
		}

		if (w_data.size() <= 0) {

			w_buttons[1].setEnabled(false);
			w_buttons[2].setEnabled(false);
		} else {

			w_buttons[1].setEnabled(true);
			w_buttons[2].setEnabled(true);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(" Add ")) {
			new AddOrEditBillWindow(this, true, true, null);

		} else if (e.getActionCommand().equals(" Edit ")) {
			if (e_row != -1) {
				new AddOrEditBillWindow(this, true, false, e_data.get(e_row));
			} else {
				JOptionPane.showMessageDialog(null, "Select a row first.");
			}

		} else if (e.getActionCommand().equals(" Delete ")) {
			if (e_row != -1) {
				e_model.remove((Integer) e_tableModel.getValueAt(e_row, 0));
				e_data.remove(e_row);
				e_tableModel.removeRow(e_row);
			} else {
				JOptionPane.showMessageDialog(null, "Select a row first.");
			}

		} else if (e.getActionCommand().equals("Add")) {
			new AddOrEditBillWindow(this, false, true, null);

		} else if (e.getActionCommand().equals("Edit")) {
			if (w_row != -1) {
				new AddOrEditBillWindow(this, false, false, w_data.get(w_row));
			} else {
				JOptionPane.showMessageDialog(null, "Select a row first.");
			}

		} else if (e.getActionCommand().equals("Delete")) {
			if (w_row != -1) {
				w_model.remove((Integer) w_tableModel.getValueAt(w_row, 0));
				w_data.remove(w_row);
				w_tableModel.removeRow(w_row);
			} else {

				JOptionPane.showMessageDialog(null, "Select a row first.");
			}
		}

		revalidate();
		updateUI();
		checkLists();
	}
	
	
}
