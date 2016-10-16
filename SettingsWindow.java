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
	final int HEIGHT = 300;

	JFrame f;

	JLabel elec, water, elec1, water1, water2, water3, water4, water5;

	JTextField elec1t, elec2t, elec3t, watert1, watert2, watert3, watert4, watert5;

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

		elec = new JLabel("Electrcity");
		elec.setBounds(padding, padding, compWidth, compHeight);

		elec1 = new JLabel("kwh:");
		elec1.setBounds(padding, padding + compHeight + compPadding, compWidth, compHeight);

		water = new JLabel("Water");
		water.setBounds(WIDTH / 2, padding, compWidth, compHeight);

		elec1t = new JTextField(Double.toString(mw.elec1), 5);
		elec1t.setBounds(padding + compPadding + compWidth, padding + compHeight + compPadding, compWidth / 2,
				compHeight);

		water1 = new JLabel("First 10 Cubic/m");
		water1.setBounds(WIDTH / 2, padding + compHeight + compPadding, compWidth, compHeight);

		water2 = new JLabel("Remaining Cubic/m");
		water2.setBounds(WIDTH / 2, padding + compHeight * 2 + compPadding * 2, compWidth, compHeight);

		water3 = new JLabel("Environmental Charge %");
		water3.setBounds(WIDTH / 2, padding + compHeight * 3 + compPadding * 3, compWidth, compHeight);

		water4 = new JLabel("E-VAT");
		water4.setBounds(WIDTH / 2, padding + compHeight * 4 + compPadding * 4, compWidth, compHeight);

		water5 = new JLabel("Maintenance Charge");
		water5.setBounds(WIDTH / 2, padding + compHeight * 5 + compPadding * 5, compWidth, compHeight);

		watert1 = new JTextField(Double.toString(mw.water1), 5);
		watert1.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight + compPadding, compWidth / 2,
				compHeight);

		watert2 = new JTextField(Double.toString(mw.water2), 5);
		watert2.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 2 + compPadding * 2,
				compWidth / 2, compHeight);

		watert3 = new JTextField(Double.toString(mw.water3), 5);
		watert3.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 3 + compPadding * 3,
				compWidth / 2, compHeight);

		watert4 = new JTextField(Double.toString(mw.water4), 5);
		watert4.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 4 + compPadding * 4,
				compWidth / 2, compHeight);
		
		watert5 = new JTextField(Double.toString(mw.water5), 3);
		watert5.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight * 5 + compPadding * 5,
				compWidth / 2, compHeight);

		add(set);
		add(cancel);
		add(elec);
		add(elec1);
		add(elec1t);
		add(water);
		add(water1);
		add(water2);
		add(water3);
		add(water4);
		add(watert1);
		add(watert2);
		add(watert3);
		add(watert4);

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equalsIgnoreCase("Set")) {

			try {

				mw.setSettings(Double.parseDouble(elec1t.getText()), Double.parseDouble(watert1.getText()),
						Double.parseDouble(watert2.getText()), Double.parseDouble(watert3.getText()),
						Double.parseDouble(watert4.getText()), Double.parseDouble(watert5.getText()));
				f.dispose();
			} catch (Exception e) {

				JOptionPane.showMessageDialog(null, "Enter valid inputs");
			}
		} else if (ae.getActionCommand().equalsIgnoreCase("Cancel")) {

			f.dispose();
		}
	}
}
