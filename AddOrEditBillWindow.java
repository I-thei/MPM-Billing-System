import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;


@SuppressWarnings("serial")
public class AddOrEditBillWindow extends JPanel implements ActionListener {
	int initial;
	int padding = 25;
	int compPadding = 10;
	int compHeight = 25;
	final int WIDTH = 260;
	final int HEIGHT = 260;

	int row, id;
	boolean elec, add;
	DataModel model;
	DefaultTableModel tableModel;
	ArrayList<String[]> data;

	String unit = "";
	String action, add_edit_command, cancel_delete_command;
	String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September","October", "November", "December" };
	String[] entry, tableEntry;
	JLabel date_l, kW_or_cm_l;

	JFrame f;

	JButton add_or_edit, cancel;

	JTextField input;

	UtilDateModel utilModel;
	Properties p;
	JDatePanelImpl datePanel;
	JDatePickerImpl datePicker;

	StoreBillWindow sbw;

	public AddOrEditBillWindow(StoreBillWindow sbw, boolean elec, String action, String[] entry) {

		this.sbw = sbw;
		this.elec = elec;
		this.action = action;

		String addoredit = "";
		String isDelete = "Cancel";
		String type;

		if(elec){ 
			type = "Elec";
			unit = "Reading kW";
			this.model = sbw.mw.e_model;
			this.tableModel = sbw.e_tableModel;
			this.data = sbw.e_data;
			this.row = sbw.e_row;
		} else { 
			type = "Water"; 
			unit = "Cubic Meters";
			this.model = sbw.mw.w_model;
			this.tableModel = sbw.w_tableModel;
			this.data = sbw.w_data;
			this.row = sbw.w_row;
		}
 
		switch (action){
			case "add":
				id = model.getNextRowNum();
				addoredit = "Add";
				this.add = true;
				add_edit_command = "Add " + type;
				break;
			case "edit":
				id = Integer.parseInt(data.get(row)[0]);
				addoredit = "Set";
				isDelete = "Delete";
				cancel_delete_command = "Delete " + type;
				add_edit_command = "Edit" + type;
				break;
		}

		this.initial = setInitial();

		f = new JFrame(add_edit_command + " Bill");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		date_l = new JLabel("Date");
		date_l.setBounds(padding, padding, 100, compHeight);

		kW_or_cm_l = new JLabel(unit);
		kW_or_cm_l.setBounds(padding, padding + compPadding * 2 + compHeight * 2, 100, compHeight);

		utilModel = new UtilDateModel();
		utilModel.setDate(utilModel.getYear(), utilModel.getMonth(), utilModel.getDay());
		utilModel.setSelected(true);

		p = new Properties();
		p.put("text.month", "Month");
		p.put("text.today", "Today");
		p.put("text.year", "Year");

		datePanel = new JDatePanelImpl(utilModel, p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setBounds(padding, padding + compHeight + compPadding, 200, compHeight);

		input = new JTextField(10);
		input.setBounds(padding, padding + compPadding * 3 + compHeight * 3, 200, compHeight);
		if(entry != null)input.setText(entry[3]);

		add_or_edit = new JButton(addoredit);
		add_or_edit.setBounds(padding, HEIGHT - padding * 2 - compHeight - compPadding, 80, compHeight);
		add_or_edit.addActionListener(this);
		add_or_edit.setActionCommand(type);

		cancel = new JButton(isDelete);
		cancel.setBounds(WIDTH - padding - 80 - compPadding, HEIGHT - padding * 2 - compHeight - compPadding, 80, compHeight);
		cancel.addActionListener(this);
		cancel.setActionCommand(cancel_delete_command);

		add(date_l);
		add(kW_or_cm_l);
		add(input);
		add(datePicker);
		add(add_or_edit);
		add(cancel);

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}


	public void setDateText() {
		String date = datePicker.getJFormattedTextField().getText();
		String[] dateComp = date.split("-");
		datePicker.getJFormattedTextField().setText(months[Integer.parseInt(dateComp[1])] + " " + dateComp[2] + ", " + dateComp[0]);
	}

	public String[] getTableEntry(String[] entry){
		return new String[] {entry[0], entry[2], entry[3], entry[entry.length - 1]};
	}

	public String[] getElecValues(){
		int in = Integer.parseInt(input.getText());
		return new String[]{
			Integer.toString(id), 
			sbw.store_id, 
			datePicker.getJFormattedTextField().getText(), 
			Integer.toString(in - initial), 
			input.getText(),
			Double.toString(sbw.mw.e_kwh), 
			getElectricityAmount()};
	}

	public int setInitial(){
		int out = 0;
		if(elec && data != null){
			for( String[] d : data) {
				if(Integer.parseInt(d[4]) > 0) {
					out = Integer.parseInt(d[4]);
				} else {
					out = 0;
				}
			}
		}
		return out;
	}

	public String[] getWaterValues(){
		return new String[] {
			Integer.toString(id), 
			sbw.store_id, datePicker.getJFormattedTextField().getText(), 
			input.getText(), Double.toString(sbw.mw.w_firstTenCubic), 
			Double.toString(sbw.mw.w_remainingCubic), 
			Double.toString(sbw.mw.w_echarge), 
			Double.toString(sbw.mw.w_evat), 
			Double.toString(sbw.mw.w_maintenancecharge), 
			getWaterAmount()};
	}

	public String getElectricityAmount() {
		int in = Integer.parseInt(input.getText());
  		Double amount = (in - initial) * sbw.mw.e_kwh;
		setInitial();

 		return String.format("%.2f", amount);
 	}
 
 	public String getWaterAmount() {
 		Double cubicm = Double.parseDouble(input.getText());
 		Double amount = sbw.mw.w_firstTenCubic;

 		if (cubicm > 10) amount += (cubicm - 10) * sbw.mw.w_remainingCubic;
 		amount += amount * (sbw.mw.w_echarge / 100) + sbw.mw.w_maintenancecharge;
 		amount += amount * (sbw.mw.w_evat / 100);

 		return String.format("%.2f", amount);
 	}

	public void doAction(String action, String type){
		String[] entry = null;
		String[] table_entry = null;

		try { 
			if(type.equals("elec")){entry = getElecValues();
			} else{ entry = getWaterValues(); }
			table_entry = getTableEntry(entry);
		} catch(Exception e){}

		switch(action){
			case "add":
				model.add(entry);
				tableModel.addRow(table_entry);
				data.add(entry);
				break;

			case "edit":
				model.edit(id, entry);
				for(int i = 1; i <= 3; i++){ tableModel.setValueAt(table_entry[i], row, i);}
				data.set(row,entry);
				break;

			case "delete":
				model.remove(Integer.parseInt(data.remove(row)[0]));
				tableModel.removeRow(row);
				break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String actioncommand = ae.getActionCommand();
		if(actioncommand.startsWith("W") || actioncommand.startsWith("E")){
			try { Integer.parseInt(input.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Enter valid " + unit);
				return;
			}
		}
		switch(actioncommand){
			case "Delete Water":
				doAction("delete", "water");
				sbw.w_delete.setEnabled(false);
				break;

			case "Delete Elec":
				doAction("delete", "elec");
				sbw.e_delete.setEnabled(false);
				break;

			case "Water":
				doAction(action, "water");
				break;

			case "Elec":
				doAction(action, "elec");
				break;

		}
		sbw.revalidate();
		f.dispose();
	}

}
