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

	int WIDTH = 400;
	int HEIGHT = 240;

	int padding = 32;
	int compHeight = 32;
	int compWidth = 200;
	int buttonWidth = 80;

	JFrame f;

  JLabel echarge, evat;
  JTextField echargeInput, evatInput;

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
		set.setBounds(padding, HEIGHT - padding * 2 - compHeight,
      (WIDTH - padding * 2) / 2 - padding, compHeight);

		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setBounds(WIDTH  - (WIDTH - padding * 2) / 2,
      HEIGHT - padding * 2 - compHeight, 
      (WIDTH - padding * 2) / 2 - padding, compHeight);

		echarge = new JLabel("Environmental Charge %");
		echarge.setBounds(padding, padding, compWidth, compHeight);

		evat = new JLabel("E-VAT %");
		evat.setBounds(padding, padding + compHeight, compWidth, compHeight);

		echargeInput = new JTextField(Double.toString(mw.echarge), 3);
		echargeInput.setBounds(padding + compWidth, padding,
				compWidth * 3 / 4, compHeight);

		evatInput = new JTextField(Double.toString(mw.evat), 3);
		evatInput.setBounds(padding + compWidth, padding + compHeight,
				compWidth * 3 / 4, compHeight);

		add(set);
		add(cancel);
		add(echarge);
		add(evat);
		add(echargeInput);
		add(evatInput);

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equalsIgnoreCase("Set")) {
			try {
				mw.setSettings(Double.parseDouble(echargeInput.getText()), Double.parseDouble(evatInput.getText()));
				f.dispose();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Enter valid inputs");
			}
		} else if (ae.getActionCommand().equalsIgnoreCase("Cancel")) {
			f.dispose();
		}
	}
}
