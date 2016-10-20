import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September","October", "November", "December" };

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

		String addoredit = "";
		String isDelete = "Cancel";

		switch (action){
			case "add":
				addoredit = "Add";
				this.add = true;
				break;
			case "edit":
				addoredit = "Set";
				if(elec) {isDelete = "Delete";}
					else {isDelete = " Delete ";}
				break;
		}

		f = new JFrame(addoredit + " Bill");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		date_l = new JLabel("Date");
		date_l.setBounds(padding, padding, 100, compHeight);

		if (elec) {
			unit = "kW";
		} else {unit = "Cubic Meters";}

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

		cancel = new JButton(isDelete);
		cancel.setBounds(WIDTH - padding - 80 - compPadding, HEIGHT - padding * 2 - compHeight - compPadding, 80, compHeight);
		cancel.addActionListener(this);

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

	@Override
	public void actionPerformed(ActionEvent ae) { //will fix this pa
		if (ae.getActionCommand().equalsIgnoreCase("Cancel")) {
			f.dispose();

		} else if (ae.getActionCommand().equalsIgnoreCase(" Delete ")){
				sbw.mw.w_model.remove(Integer.parseInt(sbw.w_data.remove(sbw.w_row)[0]));
				sbw.w_tableModel.removeRow(sbw.w_row);

		} else if (ae.getActionCommand().equalsIgnoreCase("Delete")){
				sbw.mw.e_model.remove(Integer.parseInt(sbw.e_data.remove(sbw.e_row)[0]));
				sbw.e_tableModel.removeRow(sbw.e_row);

		} else {

			try { Integer.parseInt(input.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Enter valid " + unit);
				return;
			}
			if (elec) {
				String[] e_entry = {Integer.toString(sbw.e_id), sbw.store_id, datePicker.getJFormattedTextField().getText(), input.getText(), Double.toString(sbw.mw.e_kwh), getElectricityAmount()};
				String[] entry = {e_entry[0],e_entry[2], e_entry[3], e_entry[5]};
				
				if(add){
					sbw.mw.e_model.add(e_entry);
					sbw.e_tableModel.addRow(entry);
					sbw.e_data.add(e_entry);
					sbw.e_id ++;

				} else {
					sbw.e_id = Integer.parseInt(sbw.e_data.get(sbw.e_row)[0]);
					e_entry = new String[]{Integer.toString(sbw.e_id), sbw.store_id, datePicker.getJFormattedTextField().getText(), input.getText(), Double.toString(sbw.mw.e_kwh), getElectricityAmount()};
					entry = new String[]{e_entry[0],e_entry[2], e_entry[3], e_entry[5]};
					sbw.mw.e_model.edit(sbw.e_id, e_entry);
					System.out.println(sbw.e_id + " " + entry[0]);
					sbw.e_tableModel.setValueAt(entry[1], sbw.e_row, 1);
					sbw.e_tableModel.setValueAt(entry[2], sbw.e_row, 2);
					sbw.e_tableModel.setValueAt(entry[3], sbw.e_row, 3);
					sbw.e_data.set(sbw.e_row, e_entry);
				}
			} else {
				String[] w_entry = {Integer.toString(sbw.w_id), sbw.store_id, datePicker.getJFormattedTextField().getText(), input.getText(), Double.toString(sbw.mw.w_firstTenCubic), Double.toString(sbw.mw.w_remainingCubic), Double.toString(sbw.mw.w_echarge), Double.toString(sbw.mw.w_evat), Double.toString(sbw.mw.w_maintenancecharge), getWaterAmount()};
				String[] entry = {w_entry[0],w_entry[2], w_entry[3], w_entry[9]};

				if(add){
					sbw.mw.w_model.add(w_entry);
					sbw.w_tableModel.addRow(entry);
					sbw.w_data.add(w_entry);
					sbw.w_id ++;
				} else {
					w_entry = new String[]{Integer.toString(sbw.w_id), sbw.store_id, datePicker.getJFormattedTextField().getText(), input.getText(), Double.toString(sbw.mw.w_firstTenCubic), Double.toString(sbw.mw.w_remainingCubic), Double.toString(sbw.mw.w_echarge), Double.toString(sbw.mw.w_evat), Double.toString(sbw.mw.w_maintenancecharge), getWaterAmount()};
					entry = new String[]{w_entry[0],w_entry[2], w_entry[3], w_entry[9]};
					sbw.w_id = Integer.parseInt(sbw.w_data.get(sbw.w_row)[0]);
					sbw.mw.w_model.edit(sbw.w_id, w_entry);
					sbw.w_tableModel.setValueAt(entry[1], sbw.w_row, 1);
					sbw.w_tableModel.setValueAt(entry[2], sbw.w_row, 2);
					sbw.w_tableModel.setValueAt(entry[3], sbw.w_row, 3);
					sbw.w_data.set(sbw.w_row, w_entry);
				}
			}
			sbw.revalidate();
		}
		f.dispose();
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

}
