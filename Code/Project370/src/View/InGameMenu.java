package View;

import javax.swing.*;

public class InGameMenu extends JFrame{

	
	public JButton leftButton;
	public JButton rightButton;
	public JButton forfeitButton;
	public JButton exitButton;
	public JButton actionToggleButton;
	public JButton actionButton;
	public JButton endPlayButton;
	
    public InGameMenu(int size) {
        super("Super Robot Rumble 370");
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        setSize(1000, 1000);
        setVisible(true);
    }
    
    public static void main(String[] args){
    	InGameMenu menu = new InGameMenu(5);
    }

}
