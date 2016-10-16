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

	boolean elec;

	String unit = "";
	String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	JLabel date_l, kW_or_cm_l;

	JFrame f;

	JButton add_or_edit, cancel;

	JTextField input;

	UtilDateModel model;
	Properties p;
	JDatePanelImpl datePanel;
	JDatePickerImpl datePicker;

	StoreBillWindow sbw;

	public AddOrEditBillWindow(StoreBillWindow sbw, boolean elec, boolean add, String[] entry) {

		this.sbw = sbw;
		this.elec = elec;

		String title = "";
		if (add) {
			title = "Add";
		} else {
			title = "Edit";
		}

		f = new JFrame(title + " Bill");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		date_l = new JLabel("Date");
		date_l.setBounds(padding, padding, 100, compHeight);

		if (elec) {

			unit = "kW";
		} else {

			unit = "Cubic Meters";
		}
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

		add_or_edit = new JButton(title);
		add_or_edit.setBounds(padding, HEIGHT - padding * 2 - compHeight - compPadding, 80, compHeight);
		add_or_edit.addActionListener(this);

		cancel = new JButton("Cancel");
		cancel.setBounds(WIDTH - padding - 80 - compPadding, HEIGHT - padding * 2 - compHeight - compPadding, 80,
				compHeight);
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
	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equalsIgnoreCase("Cancel")) {
			f.dispose();
		} else if (ae.getActionCommand().equalsIgnoreCase("Add") || ae.getActionCommand().equalsIgnoreCase("Edit")) {
			boolean isInt = true;
			try {
				Integer.parseInt(input.getText());
			} catch (NumberFormatException e) {
				isInt = false;
			}

			if (isInt) {
				if (elec) {
					String[] e_entry = {Integer.toString(sbw.e_id), sbw.store_id, datePicker.getJFormattedTextField().getText(), input.getText(),"RATE/ what to put?", "LOL AMOUNT" };
					sbw.e_model.add(e_entry);
					String[] entry = {e_entry[2], e_entry[3], e_entry[5]};
					sbw.e_tableModel.addRow(entry);
				} else {

					String[] w_entry = {Integer.toString(sbw.w_id), sbw.store_id, datePicker.getJFormattedTextField().getText(), input.getText(), "RATE/ what to put?", "LOL AMOUNT" };
					sbw.w_model.add(w_entry);
					String[] entry = {w_entry[2], w_entry[3], w_entry[5]};
					sbw.w_tableModel.addRow(entry);
				}

				sbw.checkLists();
				sbw.revalidate();
				f.dispose();
			} else {

				JOptionPane.showMessageDialog(null, "Enter valid " + unit);
			}
		}
	}

}
