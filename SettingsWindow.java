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
	int compWidth = 100;
	int buttonWidth = 80;
	final int WIDTH = 450;
	final int HEIGHT = 260;

	JFrame f;

	JLabel elec, water, elec1, elec2, elec3, waterl;

	JTextField elec1t, elec2t, elec3t, watert;

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

		elec1 = new JLabel("Range 1");
		elec1.setBounds(padding, padding + compHeight + compPadding, compWidth, compHeight);

		elec2 = new JLabel("Range 2");
		elec2.setBounds(padding, padding + compHeight * 2 + compPadding * 2, compWidth, compHeight);

		elec3 = new JLabel("Range 3");
		elec3.setBounds(padding, padding + compHeight * 3 + compPadding * 3, compWidth, compHeight);

		water = new JLabel("Water");
		water.setBounds(WIDTH / 2, padding, compWidth, compHeight);

		elec1t = new JTextField(Integer.toString(mw.elec1), 3);
		elec1t.setBounds(padding + compPadding + compWidth, padding + compHeight + compPadding, compWidth / 2,
				compHeight);

		elec2t = new JTextField(Integer.toString(mw.elec2), 3);
		elec2t.setBounds(padding + compPadding + compWidth, padding + compHeight * 2 + compPadding * 2, compWidth / 2,
				compHeight);

		elec3t = new JTextField(Integer.toString(mw.elec3), 3);
		elec3t.setBounds(padding + compPadding + compWidth, padding + compHeight * 3 + compPadding * 3, compWidth / 2,
				compHeight);
		
		waterl = new JLabel("Per Cubic Meter");
		waterl.setBounds(WIDTH/2, padding + compHeight + compPadding, compWidth, compHeight);

		watert = new JTextField(Integer.toString(mw.water), 3);
		watert.setBounds(WIDTH / 2 + compWidth + compPadding, padding + compHeight + compPadding, compWidth / 2,
				compHeight);

		add(set);
		add(cancel);
		add(elec);
		add(elec1);
		add(elec2);
		add(elec3);
		add(elec1t);
		add(elec2t);
		add(elec3t);
		add(water);
		add(waterl);
		add(watert);

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equalsIgnoreCase("Set")) {

			try {

				mw.setSettings(Integer.parseInt(elec1t.getText()), Integer.parseInt(elec2t.getText()),
						Integer.parseInt(elec3t.getText()));
				f.dispose();
			} catch (Exception e) {

				JOptionPane.showMessageDialog(null, "Enter valid inputs");
			}
		} else if (ae.getActionCommand().equalsIgnoreCase("Cancel")) {

			f.dispose();
		}
	}
}
