import javax.swing.*;
import java.awt.*;

public class MPM extends JPanel {

	JButton add, settings;
	JComboBox sections;

	JTextField searchfield;
				
	static final int WIDTH = 800;
	static final int HEIGHT = 600;

	public test() {
		
		this.setLayout(null);
		this.setSize(new Dimension(800,600));
		this.setBackground(new Color(240,240,240));


		add = new JButton("Add Store");
		add.setBounds(WIDTH - 120 - 25, 45, 120, 25);

		settings = new JButton("Settings");
		settings.setBounds(WIDTH - 120 - 25, 45 + 25 + 10, 120, 25);

		String[] arr = {"ALL SECTIONS", "1ST FLOOR", "2ND FLOOR","MEAT", "FOOD", "CHICKEN",
				"FISH", "VEGETABLE", "FRUIT", "GROCERY", "SPECIAL","MOBILE" };

		sections = new JComboBox(arr);
		sections.setBounds(WIDTH - 120 - 25, 45 + 50 + 20, 120, 25);

		searchfield = new JTextField("",255);
		searchfield.setBounds(10, 10, WIDTH - 20, 25);

		add(add);
		add(settings);
		add(sections);
		add(searchfield);
	}

	public static void main(String[] args){


		JFrame f = new JFrame("Marikina Public Market Electricity and Water Database");
		f.setSize(800,600);

		f.add(new MPM());

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);
		AddStore a = new AddStore();

	}
}