package game;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public HelpPanel() {
		setLayout(null); // Use absolute positioning

        // Set preferred size for the panel
        this.setPreferredSize(new Dimension(500, 500));
		
		ImageIcon helpimage = new ImageIcon(SpriteLoader.class.getResource("sprites/other/helppanel.png"));
		JLabel lblNewLabel = new JLabel(helpimage);
		lblNewLabel.setBounds(0, 0, 500, 500);
		add(lblNewLabel);


	}

}
