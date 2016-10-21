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

	int padding = 25;
	int compPadding = 10;
	int compHeight = 25;
	final int WIDTH = 260;
	final int HEIGHT = 260;

	boolean elec, add;

	String unit = "";
	String action, add_edit_command, cancel_delete_command;
	String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September","October", "November", "December" };
	String[] entry, tableEntry;
	JLabel date_l, kW_or_cm_l;

	JFrame f;

	JButton add_or_edit, cancel;

	JTextField input;

	UtilDateModel model;
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
			unit = "kW";
		} else { 
			type = "Water"; 
			unit = "Cubic Meters";
		}
 
		switch (action){
			case "add":
				addoredit = "Add";
				this.add = true;
				cancel_delete_command = "Cancel";
				add_edit_command = "Add " + type;
				break;
			case "edit":
				addoredit = "Set";
				isDelete = "Delete";
				cancel_delete_command = "Delete " + type;
				add_edit_command = "Edit " + type;
				break;
		}


		f = new JFrame(add_edit_command + " Bill");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		date_l = new JLabel("Date");
		date_l.setBounds(padding, padding, 100, compHeight);

		kW_or_cm_l = new JLabel(unit);
		kW_or_cm_l.setBounds(padding, padding + compPadding * 2 + compHeight * 2, 100, compHeight);

		model = new UtilDateModel();
		model.setDate(model.getYear(), model.getMonth(), model.getDay());
		model.setSelected(true);

		p = new Properties();
		p.put("text.month", "Month");
		p.put("text.today", "Today");
		p.put("text.year", "Year");

		datePanel = new JDatePanelImpl(model, p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setBounds(padding, padding + compHeight + compPadding, 200, compHeight);

		input = new JTextField(10);
		input.setBounds(padding, padding + compPadding * 3 + compHeight * 3, 200, compHeight);
		if(entry != null)input.setText(entry[3]);

		add_or_edit = new JButton(addoredit);
		add_or_edit.setBounds(padding, HEIGHT - padding * 2 - compHeight - compPadding, 80, compHeight);
		add_or_edit.addActionListener(this);
		add_or_edit.setActionCommand(add_edit_command);

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
		return new String[]{
			Integer.toString(sbw.e_id), 
			sbw.store_id, 
			datePicker.getJFormattedTextField().getText(), 
			input.getText(), 
			Double.toString(sbw.mw.e_kwh), 
			getElectricityAmount()};
	}

	public String[] getWaterValues(){
		return new String[] {
			Integer.toString(sbw.w_id), 
			sbw.store_id, datePicker.getJFormattedTextField().getText(), 
			input.getText(), Double.toString(sbw.mw.w_firstTenCubic), 
			Double.toString(sbw.mw.w_remainingCubic), 
			Double.toString(sbw.mw.w_echarge), 
			Double.toString(sbw.mw.w_evat), 
			Double.toString(sbw.mw.w_maintenancecharge), 
			getWaterAmount()};
	}

	public String getElectricityAmount() {
  		Double amount = Double.parseDouble(input.getText()) * sbw.mw.e_kwh;
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

	public void doAction(String action, String type, DataModel model, DefaultTableModel tableModel, ArrayList<String[]> data, int row, int id){
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
		if(actioncommand.startsWith("Add") || actioncommand.startsWith("Edit")){
			try { Integer.parseInt(input.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Enter valid " + unit);
				return;
			}
		}
		switch(actioncommand){
			case "Delete Water":
				doAction("delete", "water", sbw.mw.w_model, sbw.w_tableModel, sbw.w_data, sbw.w_row, sbw.e_id);
				break;

			case "Delete Elec":
				doAction("delete", "elec", sbw.mw.e_model, sbw.e_tableModel, sbw.e_data, sbw.e_row, sbw.w_id);
				break;

			case "Add Water":
				sbw.w_id = sbw.mw.w_model.getNextRowNum();
				doAction("add", "water", sbw.mw.w_model, sbw.w_tableModel, sbw.w_data, sbw.w_row, sbw.w_id);
				break;

			case "Edit Water":
				sbw.w_id = Integer.parseInt(sbw.w_data.get(sbw.w_row)[0]);
				doAction("edit", "water", sbw.mw.w_model, sbw.w_tableModel,  sbw.w_data, sbw.w_row, sbw.w_id);
				break;

			case "Add Elec":
				sbw.e_id = sbw.mw.e_model.getNextRowNum();
				doAction("add",  "elec", sbw.mw.e_model, sbw.e_tableModel, sbw.e_data, sbw.e_row, sbw.e_id);
				break;

			case "Edit Elec":
				sbw.e_id = Integer.parseInt(sbw.e_data.get(sbw.e_row)[0]);
				doAction("edit", "elec", sbw.mw.e_model, sbw.e_tableModel, sbw.e_data, sbw.e_row, sbw.e_id);
				break;
		}
		sbw.revalidate();
		f.dispose();
	}

}
