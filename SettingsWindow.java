import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SettingsWindow extends JPanel implements ActionListener {

	int padding = 25;
	int compPadding = 10;
	int compHeight = 25;
	int compWidth = 200;
	int buttonWidth = 80;
	final int WIDTH = 760;
	final int HEIGHT = 350;

	JFrame f;

    JLabel e_kwh, w_firstTenCubic, w_remainingCubic, w_echarge, w_evat, w_maintenancecharge, waterLabel, electricLabel;
  	JTextField e_kwhInput, w_firstTenCubicInput, w_remainingCubicInput, w_echargeInput, w_evatInput, w_maintenancechargeInput;

	JButton set, cancel;

	MainWindow mw;

	public SettingsWindow(MainWindow mw) {

		this.mw = mw;

		f = new JFrame("Settings");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		this.setLayout(null);

		set = new JButton("Set");
		set.addActionListener(this);
		set.setBounds(padding, HEIGHT - padding * 2 - compPadding - compHeight, buttonWidth, compHeight);

		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setBounds(WIDTH - padding - buttonWidth - compPadding, HEIGHT - padding * 2 - compPadding - compHeight,
				buttonWidth, compHeight);

		electricLabel = new JLabel("Electricity:");
		electricLabel.setBounds(padding, padding, compWidth, compHeight);

		e_kwh = new JLabel("kwh:");
		e_kwh.setBounds(padding, padding + compHeight + compPadding, compWidth, compHeight);

		waterLabel = new JLabel("Water:");
		waterLabel.setBounds(WIDTH / 2, padding, compWidth, compHeight);

		e_kwhInput = new JTextField(Double.toString(mw.e_kwh), 5);
		e_kwhInput.setBounds(padding + compPadding + compWidth, padding + compHeight + compPadding, compWidth / 2,
				compHeight);

		w_firstTenCubic = new JLabel("First 10 Cubic/m");
		w_firstTenCubic.setBounds(WIDTH / 2, padding + compHeight + compPadding, compWidth, compHeight);

		w_remainingCubic = new JLabel("Remaining Cubic/m");
		w_remainingCubic.setBounds(WIDTH / 2, padding + compHeight * 2 + compPadding * 2, compWidth, compHeight);

		w_echarge = new JLabel("Environmental Charge %");
		w_echarge.setBounds(WIDTH / 2, padding + compHeight * 3 + compPadding * 3, compWidth, compHeight);

		w_evat = new JLabel("E-VAT");
		w_evat.setBounds(WIDTH / 2, padding + compHeight * 4 + compPadding * 4, compWidth, compHeight);

		w_maintenancecharge = new JLabel("Maintenance Charge");
		w_maintenancecharge.setBounds(WIDTH / 2, padding + compHeight * 5 + compPadding * 5, compWidth, compHeight);

		w_firstTenCubicInput = new JTextField(Double.toString(mw.w_firstTenCubic), 5);
		w_firstTenCubicInput.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight + compPadding, compWidth / 2,
				compHeight);

		w_remainingCubicInput = new JTextField(Double.toString(mw.w_remainingCubic), 5);
		w_remainingCubicInput.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 2 + compPadding * 2,
				compWidth / 2, compHeight);

		w_echargeInput = new JTextField(Double.toString(mw.w_echarge), 5);
		w_echargeInput.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 3 + compPadding * 3,
				compWidth / 2, compHeight);

		w_evatInput = new JTextField(Double.toString(mw.w_evat), 5);
		w_evatInput.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 4 + compPadding * 4,
				compWidth / 2, compHeight);
		
		w_maintenancechargeInput = new JTextField(Double.toString(mw.w_maintenancecharge), 3);
		w_maintenancechargeInput.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 5 + compPadding * 5,
				compWidth / 2, compHeight);

		add(set);
		add(cancel);
		add(waterLabel);
		add(electricLabel);
		add(e_kwh);
		add(e_kwhInput);
		add(w_firstTenCubic);
		add(w_remainingCubic);
		add(w_echarge);
		add(w_evat);
		add(w_maintenancecharge);
		add(w_firstTenCubicInput);
		add(w_remainingCubicInput);
		add(w_echargeInput);
		add(w_evatInput);
		add(w_maintenancechargeInput);

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equalsIgnoreCase("Set")) {
			try {
				mw.setSettings(
					Double.parseDouble(e_kwhInput.getText()), 
					Double.parseDouble(w_firstTenCubicInput.getText()),
					Double.parseDouble(w_remainingCubicInput.getText()), 
					Double.parseDouble(w_echargeInput.getText()),
					Double.parseDouble(w_evatInput.getText()), 
					Double.parseDouble(w_maintenancechargeInput.getText()));
				f.dispose();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Enter valid inputs");
			}
		} else if (ae.getActionCommand().equalsIgnoreCase("Cancel")) {
			f.dispose();
		}
	}
}