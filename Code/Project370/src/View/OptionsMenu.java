package View;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import Controller.Controller;

public class OptionsMenu extends JFrame{

	/**@public The panel where all images will be drawn*/
	public OptionsMenuPanel optionsPanel;
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * Public constructor.
	 * Constructs the frame for the options menu.
	 */
	public OptionsMenu() {
        super("Import robots");
        setLayout(new GridLayout(2,1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
        setSize(1400, 1000);
        optionsPanel = new OptionsMenuPanel();
        add(optionsPanel);
        optionsPanel.setVisible(true);
        setVisible(true);
        
        JButton backButton  = new JButton("Back");
        backButton.addActionListener(new BackListener());
        add(backButton);
        revalidate();
    }
	
    /** 
     * The action listener for the back button.
     */
    private class BackListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		Controller.getInstance().gameMenu.setVisible(true);
    		Controller.getInstance().optionsMenu.setVisible(false);
    	}
    }
	
	public static void main(String args[]){
		new OptionsMenu();
	}
}