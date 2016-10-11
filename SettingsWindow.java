import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SettingsWindow extends JPanel implements ActionListener {

	int padding = 25;
	int compPadding = 10;
	int compHeight = 25;
	final int WIDTH = 600;
	final int HEIGHT = 400;
	
	JFrame f;
	
	JButton set, cancel;
	
	MainWindow mw;
	
	public SettingsWindow(MainWindow mw) {

		this.mw = mw;
		
		f = new JFrame("Settings");
		f.setSize(WIDTH, HEIGHT);
		f.setLocationRelativeTo(null);
		
		// Stuff
		
		f.setResizable(false);
		f.add(this);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		
	}
}
