import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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

	DefaultTableModel e_model = new DefaultTableModel();
	DefaultTableModel w_model = new DefaultTableModel();

	JLabel idl, namel, sectionl, holderl, e_records, w_records;
	JLabel[] labels = new JLabel[4];

	JTable e_list, w_list;

	JFrame f;

	JButton generate, e_add, e_edit, e_delete, w_add, w_edit, w_delete;
	JButton[] e_buttons = new JButton[3];
	JButton[] w_buttons = new JButton[3];

	JComboBox report_type;

	String[] e_columnNames = { "Date", "kW/h", "Amount" };
	String[] w_columnNames = { "Date", "Cubic Meters", "Amount" };
	String[] report_types = { "Monthly", "Yearly" };

	ArrayList<String[]> e_data = new ArrayList<>();
	ArrayList<String[]> w_data = new ArrayList<>();

	MainWindow mw;

	@SuppressWarnings("unchecked")
	public StoreBillWindow(MainWindow mw, String[] data) {

		this.mw = mw;

		String id = data[0];
		String name = data[1];
		String section = data[2];
		String holder = data[3];

		f = new JFrame(name + " Electricity And Water Bills");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		idl = new JLabel("Store Number: " + id);
		namel = new JLabel("Store Name: " + name);
		sectionl = new JLabel("Store Section: " + section);
		holderl = new JLabel("Store Holder: " + holder);

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

		e_model.setColumnIdentifiers(e_columnNames);
		w_model.setColumnIdentifiers(w_columnNames);

		e_list = new JTable() {

			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};
		e_list.setModel(e_model);
		e_list.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				e_row = table.rowAtPoint(p);
			}
		});

		w_list = new JTable() {

			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};
		w_list.setModel(w_model);
		w_list.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				w_row = table.rowAtPoint(p);
			}
		});

		int e_list_y = padding + compHeight * 3 + compPadding * 1;
		int w_list_y = padding + compHeight * 10 + compPadding * 3;

		JScrollPane e_listsp = new JScrollPane(e_list);
		e_listsp.setBounds(padding, e_list_y, WIDTH - padding * 2 - compPadding - buttonWidth, compHeight * 6);

		JScrollPane w_listsp = new JScrollPane(w_list);
		w_listsp.setBounds(padding, w_list_y, WIDTH - padding * 2 - compPadding - buttonWidth, compHeight * 6);

		add(e_listsp);
		add(w_listsp);

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
					e_list_y + compHeight * i + compPadding * i, buttonWidth - 10, compHeight);
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
					w_list_y + compHeight * i + compPadding * i, buttonWidth - 10, compHeight);
			w_buttons[i].addActionListener(this);
			add(w_buttons[i]);
		}

		checkLists();

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	public String[][] convertList(ArrayList<String[]> data) {

		String[][] newData = new String[data.size()][e_columnNames.length];
		for (int i = 0; i < data.size(); i++) {

			newData[i] = data.get(i);
		}

		return newData;
	}

	public void addEntry(int bit, String[] entry) {

		switch (bit) {
		case 0:
			e_data.add(entry);
			e_model.addRow(entry);
			break;
		case 1:
			w_data.add(entry);
			w_model.addRow(entry);
			break;
		default:
			System.out.println("Invalid Case");
			break;
		}

		this.revalidate();
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

	public void deleteRow(int bit) {

		if (bit == 0) {

			e_data.remove(e_row);
			e_model.removeRow(e_row);
		} else {

			w_data.remove(w_row);
			w_model.removeRow(w_row);
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

				deleteRow(0);
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

				deleteRow(1);
			} else {

				JOptionPane.showMessageDialog(null, "Select a row first.");
			}
		}

		revalidate();
		updateUI();
		checkLists();
	}
}
