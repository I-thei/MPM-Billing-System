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

	String sectionl;

	JLabel section, holder, name, id, iddisp;
	JLabel[] labels = new JLabel[4];

	JTextField holdertf, nametf;

	@SuppressWarnings("rawtypes")
	JComboBox sectionsircb;

	JButton add, cancel;

	JFrame f;

	MainWindow mw;
	StoreBillWindow sbw;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AddStoreWindow(MainWindow mw, StoreBillWindow sbw, int store_id) {

		this.store_id = store_id;
		this.mw = mw;
		this.sbw = sbw;

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
		sectionl = String.valueOf(mw.sections.getSelectedItem());
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

	public int indexStoreID(int id){
		int i = 0;
		if(mw.data != null){
			while(i < mw.data.size() - 1 && Integer.parseInt(mw.data.get(i)[0]) != id ){
				i++;
			}
		}
		return i;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String selected_section = String.valueOf(sectionsircb.getSelectedItem());
		String action = ae.getActionCommand();
		int row = indexStoreID(store_id); 

		switch(action) {
			case "Add":
				if(!sectionl.equals("ALL SECTIONS")) mw.sections.setSelectedItem(selected_section);
				String[] new_entry = {Integer.toString(store_id), selected_section, nametf.getText(), holdertf.getText()};
				mw.model.add(new_entry);
				mw.tableModel.addRow(new_entry);
				mw.lastId++;
				mw.revalidate();	
				break;

			case "Delete":
				mw.model.remove(store_id);
				mw.tableModel.removeRow(row);
				sbw.f.dispose();
				break;
			
			case "Set":
				String[] entry = {Integer.toString(store_id), selected_section, nametf.getText(), holdertf.getText()};
				mw.model.edit(store_id, entry);
				for(int i = 1; i <= 3; i++){ 
					mw.tableModel.setValueAt(entry[i], row, i);		
				}
				mw.data.set(row,entry);
				sbw.sectionl.setText("Section: " + selected_section);
				sbw.namel.setText("Store: " + nametf.getText());
				sbw.holderl.setText("Holder: " + holdertf.getText());
				sbw.revalidate();
			}
		mw.revalidate();
		f.dispose();
	}

}