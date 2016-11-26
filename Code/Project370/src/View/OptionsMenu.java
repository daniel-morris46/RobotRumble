package View;

import javax.swing.JFrame;

public class OptionsMenu extends JFrame{
	
	
	
	/**@public The panel where all images will be drawn*/
	public OptionsMenuPanel gamePanel;
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * Public constructor.
	 * Constructs the frame for the options menu.
	 */
	public OptionsMenu() {
        super("370 ROBOT RUMBLE");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(1400,  1000);
    }
}
