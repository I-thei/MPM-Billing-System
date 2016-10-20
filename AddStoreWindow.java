import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddStoreWindow extends JPanel implements ActionListener {

	int store_id;
	int padding = 30;
	int compPadding = 20;
	int compWidth = 180;
	int compHeight = 25;
	final int WIDTH = 500;
	final int HEIGHT = 300;

	JLabel section, holder, name, id, iddisp;
	JLabel[] labels = new JLabel[4];

	JTextField holdertf, nametf;

	@SuppressWarnings("rawtypes")
	JComboBox sectionsircb;

	JButton add, cancel;

	JFrame f;

	MainWindow mw;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AddStoreWindow(MainWindow mw, int store_id) {

		this.store_id = store_id;
		this.mw = mw;

		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		f = new JFrame("Add Store");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);

		add = new JButton("Add");
		add.setBounds(padding, HEIGHT - 70, compWidth, compHeight);
		add.addActionListener(this);

		cancel = new JButton("Cancel");
		cancel.setBounds(padding + compWidth + compPadding, HEIGHT - 70, compWidth, compHeight);
		cancel.addActionListener(this);

		section = new JLabel("Section: ");
		holder = new JLabel("Store Holder: ");
		name = new JLabel("Store Name: ");
		id = new JLabel("ID: ");
		labels[0] = id;
		labels[1] = section;
		labels[2] = name;
		labels[3] = holder;

		for (int i = 0; i < labels.length; i++) {
			labels[i].setBounds(padding, padding + compPadding * i + compHeight * i, compWidth, compHeight);
			add(labels[i]);
		}

		iddisp = new JLabel(Integer.toString(store_id));
		iddisp.setBounds(padding + compWidth + compPadding, padding, compWidth, compHeight);

		String[] sectionList = new String[MainWindow.sectionList.length - 1];
		System.arraycopy(MainWindow.sectionList, 1, sectionList, 0, MainWindow.sectionList.length - 1);
		sectionsircb = new JComboBox(sectionList);
		sectionsircb.setBounds(padding + compWidth + compPadding, padding + compPadding + compHeight, compWidth + 40, compHeight);
		String sectionl = String.valueOf(mw.sections.getSelectedItem());
		if(!sectionl.equals("ALL SECTIONS")) sectionsircb.setSelectedItem(sectionl);

		nametf = new JTextField(25);
		nametf.setBounds(padding + compWidth + compPadding, padding + compPadding * 2 + compHeight * 2, compWidth + 40, compHeight);

		holdertf = new JTextField(25);
		holdertf.setBounds(padding + compWidth + compPadding, padding + compPadding * 3 + compHeight * 3, compWidth + 40, compHeight);

		add(add);
		add(cancel);
		add(iddisp);
		add(sectionsircb);
		add(holdertf);
		add(nametf);

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equalsIgnoreCase("Cancel")) {
			f.dispose();
		} else if (ae.getActionCommand().equalsIgnoreCase("Add")) {
			String[] entry = {Integer.toString(store_id), sectionsircb.getSelectedItem().toString(), nametf.getText(), holdertf.getText()};
			mw.model.add(entry);
			mw.tableModel.addRow(entry);
			mw.revalidate();
			mw.lastId++;
			f.dispose();
		}
	}

}