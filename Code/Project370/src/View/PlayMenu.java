package View;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import Controller.Controller;
import Model.Board;

public class PlayMenu extends JFrame {
	
	private int boardSize = 5;
	
	private int numPlayers = 2;
	
	public JComboBox<Integer> numberOfPlayers;
	
	public JComboBox<String>[] playerTypes;
	
	public JComboBox<String>[] playerColors;
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		
		new PlayMenu("Choose board options.");
	
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayMenu(String title) {
    	super(title);
    	
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	this.setSize(400, 600);
    	this.setVisible(true);
    	
    	//Initialize the menu panel
    	JPanel menuPanel;
    	menuPanel = new JPanel();
    	menuPanel.setSize(400,  600);
    	menuPanel.setVisible(true);
    	menuPanel.doLayout();
    	this.add(menuPanel);
    	
    	//Initialize the begin button and add it to the menu panel
    	JButton beginButton;
    	beginButton = new JButton("Begin");
    	beginButton.setSize(50, 50);
    	beginButton.setVisible(true);
    	beginButton.addActionListener(new BeginListener());
    	menuPanel.add(beginButton);
    	
    	//Initializing back button
    	JButton backButton;
    	backButton = new JButton("Back");
    	backButton.setSize(50,50);
    	backButton.setVisible(true);
    	backButton.addActionListener(new BackListener());
    	menuPanel.add(backButton);
    	
    	//Initialize the label for the board size
    	JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 50, 25));
    	JLabel boardSizeTitle = new JLabel("Board Size");
    	buttonPanel.add(boardSizeTitle);
  
    	//Initialize the five player board size
    	JRadioButton sizeFive = new JRadioButton("5");
    	sizeFive.setSize(50, 50);
    	sizeFive.setVisible(true);
    	sizeFive.addActionListener(new SizeFiveListener());
    	buttonPanel.add(sizeFive);
    	
    	//Initialize the seven player board size
    	JRadioButton sizeSeven = new JRadioButton("7");
    	sizeSeven.setSize(50, 50);
    	sizeSeven.setVisible(true);
    	sizeSeven.addActionListener(new SizeSevenListener());
    	buttonPanel.add(sizeSeven);
    	
    	//Create a button group and add the five and seven player buttons
    	ButtonGroup sizeButtons = new ButtonGroup();
    	sizeButtons.add(sizeFive);
    	sizeButtons.add(sizeSeven);
    	sizeFive.setSelected(true);
    	menuPanel.add(buttonPanel);
    	
    	//Initialize the label for the player number size
    	JPanel playerNumPanel = new JPanel(new GridLayout(2, 1, 50, 25));
    	JLabel playerNumTitle = new JLabel("Number of Players");
    	playerNumPanel.add(playerNumTitle);
    	
    	
    	String[] numPossiblePlayers = {"2", "3"};
    	numberOfPlayers = new JComboBox(numPossiblePlayers);
    	numberOfPlayers.setSize(100,100);
    	numberOfPlayers.addActionListener(new PlayerNumberListener());
    	
    	playerNumPanel.add(numberOfPlayers);
    	menuPanel.add(playerNumPanel);
    	
    	numberOfPlayers.setVisible(true);
    	
    	String[] playerTypeOptions = {"Computer", "Human"};
    	playerTypes = new JComboBox[6];
    	
    	JPanel playerPanel = new JPanel(new GridLayout(7,1, 50, 25));
    	menuPanel.add(playerPanel);
    	JLabel playerTitle = new JLabel("Player Type");
    	playerPanel.add(playerTitle);
    	
    	for (int i = 0; i < 6; i++){
    		playerTypes[i] = new JComboBox(playerTypeOptions);
    		
    		playerTypes[i].setSize(100, 100);

    		playerPanel.add(playerTypes[i]);
    		if(i < numPlayers){
    			playerTypes[i].setVisible(true);
    		} else {
    			playerTypes[i].setVisible(false);
    		}
    		
    	}
    	
    	String[] playerColorOptions = {"Red", "Orange", "Yellow", "Green", "Blue", "Purple"};
    	playerColors = new JComboBox[6];
    	
    	JPanel colorPanel = new JPanel(new GridLayout(7,1, 50, 25));
    	menuPanel.add(colorPanel);
    	
    	JLabel colorTitle = new JLabel("Team Color");
    	colorPanel.add(colorTitle);
    	
    	for (int i = 0; i < 6; i++){
    		playerColors[i] = new JComboBox(playerColorOptions);
    		
    		playerColors[i].setSize(100, 100);
    		colorPanel.add(playerColors[i]);
    		
    		if(i < numPlayers){
    			playerColors[i].setVisible(true);
    		} else {
    			playerColors[i].setVisible(false);
    		}
    	}
    }

	/** The action listener for the play button
     * 
     *  This takes the player to the PlayMenu to create a game
     */
    private class BeginListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		//TELL BOARD TO CREATE GAME USING INPUTTED PARAMETERS
    		Board newBoard = new Board(boardSize, numPlayers, parseColors());
    		Controller.getInstance().gameBoard = newBoard;
    		Controller.getInstance().inGameMenu = new InGameMenu(newBoard);
    		setVisible(false);
    	}
    }
    
    /** Parses the colors from active combo boxes and returns the color list */
    private Color[] parseColors(){
    	Color[] colors = new Color[numPlayers];
    	
    	String curString = "";
    	for(int i = 0; i < numPlayers; i++){
    		curString = playerColors[i].getSelectedItem().toString();
    			
    		switch(curString){
	    		case "Red":
	    			colors[i] = Color.RED;
	    			break;
	    		case "Orange":
	    			colors[i] = Color.ORANGE;
	    			break;
	    		case "Yellow":
	    			colors[i] = Color.YELLOW;
	    			break;
	    		case "Green":
	    			colors[i] = Color.GREEN;
	    			break;
	    		case "Blue":
	    			colors[i] = Color.BLUE;
	    			break;
	    		case "Purple":
	    			colors[i] = Color.MAGENTA;
	    			break;
    		}
    	}
    	
    	return colors;
    }
    
    /** The action listener for the five player radio button
     * 
     *  This changes the number of players in the game to five.
     */
    private class SizeFiveListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		boardSize = 5;
    		
    		String[] numPossiblePlayers = {"2", "3"};
        	DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
        	numberOfPlayers.setModel(model);
        	
        	updatePlayerNumbers();
    	}
    }

    /** The action listener for the seven player radio button.
     * 
     *  This changes the number of players in the game to seven.
     */
    private class SizeSevenListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		boardSize = 7;
    		
    		String[] numPossiblePlayers = {"3", "6"};
        	DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
        	numberOfPlayers.setModel(model);
        	
        	updatePlayerNumbers();
    	}
    }

    /** 
     * The action listener for the back button.
     */
    private class BackListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		new GameMenu();
    		dispose();
    	}
    }
    
    /**
     * The action listener for the player number combo box.
     * 
     * This changes the number of players to 2, 3 or 6
     */
    private class PlayerNumberListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		updatePlayerNumbers();
    	}
    }
    
    /**
     * Updates the number of players and displays player types and colors appropriately
     */
    private void updatePlayerNumbers(){
    	numPlayers = Integer.parseInt( numberOfPlayers.getSelectedItem().toString() );
		
		for (int i = 0; i < 6; i++){
    		if(i < numPlayers){
    			playerTypes[i].setVisible(true);
    		} else {
    			playerTypes[i].setVisible(false);
    		}
    	}
		
		for (int i = 0; i < 6; i++){
    		if(i < numPlayers){
    			playerColors[i].setVisible(true);
    		} else {
    			playerColors[i].setVisible(false);
    		}
    	}
    }
}
