import javax.swing.*;
import java.awt.*;

public class AddStore extends JPanel {
				
	int initHeight = 30;
	int compHeight = 25;
	static final int WIDTH = 400;
	static final int HEIGHT = 300;

	JLabel section, holder, name, id;
	JTextField holdertf, nametf;
	JComboBox sectionsircb;
	JButton Add, cancel;

	public AddStore() {

		initHeight = 30;		
			
		this.setLayout(null);
		this.setSize(new Dimension(WIDTH, HEIGHT));

		JFrame f = new JFrame();
		f.setTitle("Add Store");
		f.setSize(WIDTH,HEIGHT);

		Add = new JButton("Add");
		Add.setBounds(20, HEIGHT - 70, 75, 25);
		add(Add);

		cancel = new JButton("Cancel");
		cancel.setBounds(20 + 75 + 20, HEIGHT - 70, 75, 25);
		add(cancel);

		section = new JLabel("Section: ");

		holder = new JLabel("Store Holder: ");

		name = new JLabel("Store Name: ");

		id = new JLabel("ID: ");

		f.setResizable(false);
		f.add(this);
		f.setVisible(true);

	}

}