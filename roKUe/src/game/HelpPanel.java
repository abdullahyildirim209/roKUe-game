package game;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public HelpPanel() {
		
		JLabel lblNewLabel = new JLabel("This is a help panel");
		// timer durduruyor thats good
		// ama player hala hareket ediyor thats bad
		//Update: player hareket etmesi işini düzelttim
		//help yapınca keyboard arrow pressleri false yapıyor
		//enemy ailarda işe yarar mı ama idk
		add(lblNewLabel);

	}

}
