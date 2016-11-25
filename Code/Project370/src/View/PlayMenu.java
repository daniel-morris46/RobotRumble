package View;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

import Model.Board;

public class PlayMenu extends JFrame {
	
	@SuppressWarnings("unused")
	public int boardSize = 5;
	
	public int numPlayers = 2;
	
	@SuppressWarnings("rawtypes")
	public JComboBox numberOfPlayers;
	
	@SuppressWarnings("rawtypes")
	public JComboBox[] playerTypes;
	
	@SuppressWarnings("rawtypes")
	public JComboBox[] playerColors;
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }
	
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayMenu(String title, ActionListener actionListener, Board gameBoard) {
    	super(title);
    	
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	this.setSize(400, 400);
    	this.setVisible(true);
    	
    	//Initialize the menu panel
    	JPanel menuPanel;
    	menuPanel = new JPanel();
    	menuPanel.setSize(400,  400);
    	menuPanel.setVisible(true);
    	menuPanel.doLayout();
    	this.add(menuPanel);
    	
    	//Initialize the begin button and add it to the menu panel
    	JButton beginButton;
    	beginButton = new JButton("Begin");
    	beginButton.setSize(50, 50);
    	beginButton.setVisible(true);
    	beginButton.addActionListener(actionListener);
    	beginButton.setActionCommand("begin");
    	menuPanel.add(beginButton);
    	
    	//Initializing back button
    	JButton backButton;
    	backButton = new JButton("Back");
    	backButton.setSize(50,50);
    	backButton.setVisible(true);
    	backButton.addActionListener(actionListener);
    	backButton.setActionCommand("back");
    	menuPanel.add(backButton);
    	
    	//Initialize the label for the board size
    	JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 50, 25));
    	JLabel boardSizeTitle = new JLabel("Board Size");
    	buttonPanel.add(boardSizeTitle);
    	
    	
    	
    	//Initialize the five player board size
    	JRadioButton sizeFive = new JRadioButton("5");
    	sizeFive.setSize(50, 50);
    	sizeFive.setVisible(true);
    	sizeFive.addActionListener(actionListener);
    	sizeFive.setActionCommand("sizeFive");
    	buttonPanel.add(sizeFive);
    	
    	
    	//Initialize the seven player board size
    	JRadioButton sizeSeven = new JRadioButton("7");
    	sizeSeven.setSize(50, 50);
    	sizeSeven.setVisible(true);
    	sizeSeven.addActionListener(actionListener);
    	sizeSeven.setActionCommand("sizeSeven");
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
    	
    	if(gameBoard.getSize() == 5){
    		numPossiblePlayers[0] = "2";
    		numPossiblePlayers[1]= "3";
    	} else {
    		numPossiblePlayers[0] = "3";
    		numPossiblePlayers[1]= "6";
    	}
    	
    	numberOfPlayers = new JComboBox(numPossiblePlayers);
    	numberOfPlayers.setSize(100,100);
    	numberOfPlayers.setActionCommand("numOfPlayersChange");
		numberOfPlayers.addActionListener(actionListener);
    	
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
    	//
  //    
//  	/** The action listener for the play button			this.gameBoard = new Board(boardSize, this.numberOfPlayers);

//       * 
//       *  This takes the player to the PlayMenu to create a game
//       */
//      private class BeginListener implements ActionListener{
//      	public void actionPerformed(ActionEvent e){
//      		InGameMenu.Instance().setVisible(true);
//      		InGameMenu.Instance().remakeBoard(boardSize);
//      		setVisible(false);
//      	}
//      }
  //    
//      /** The action listener for the five player radio button
//       * 
//       *  This changes the number of players in the game to five.
//       */
//      private class SizeFiveListener implements ActionListener{
//      	public void actionPerformed(ActionEvent e){
//      		boardSize = 5;
//      		
//      		String[] numPossiblePlayers = {"2", "3"};
//          	DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
//          	numberOfPlayers.setModel(model);
//          	
//          	updatePlayerNumbers();
//      	}
//      }
  //
//      /** The action listener for the seven player radio button.
//       * 
//       *  This changes the number of players in the game to seven.
//       */
//      private class SizeSevenListener implements ActionListener{
//      	public void actionPerformed(ActionEvent e){
//      		boardSize = 7;
//      		
//      		String[] numPossiblePlayers = {"3", "6"};
//          	DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
//          	numberOfPlayers.setModel(model);
//          	
//          	updatePlayerNumbers();
//      	}
//      }
  //
//      /** The action listener for the back button.
//       */
//      private class BackListener implements ActionListener{
//      	public void actionPerformed(ActionEvent e){
//      		GameMenu menu = new GameMenu();
//      		setVisible(false);
//      	}
//      }
  //    
//      /**
//       * The action listener for the player number combo box.
//       * 
//       * This changes the number of players to 2, 3 or 6
//       */
//      private class PlayerNumberListener implements ActionListener{
//      	public void actionPerformed(ActionEvent e){
//      		updatePlayerNumbers();
//      	}
//      }
  //    
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

//
//    
//	/** The action listener for the play button
//     * 
//     *  This takes the player to the PlayMenu to create a game
//     */
//    private class BeginListener implements ActionListener{
//    	public void actionPerformed(ActionEvent e){
//    		InGameMenu.Instance().setVisible(true);
//    		InGameMenu.Instance().remakeBoard(boardSize);
//    		setVisible(false);
//    	}
//    }
//    
//    /** The action listener for the five player radio button
//     * 
//     *  This changes the number of players in the game to five.
//     */
//    private class SizeFiveListener implements ActionListener{
//    	public void actionPerformed(ActionEvent e){
//    		boardSize = 5;
//    		
//    		String[] numPossiblePlayers = {"2", "3"};
//        	DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
//        	numberOfPlayers.setModel(model);
//        	
//        	updatePlayerNumbers();
//    	}
//    }
//
//    /** The action listener for the seven player radio button.
//     * 
//     *  This changes the number of players in the game to seven.
//     */
//    private class SizeSevenListener implements ActionListener{
//    	public void actionPerformed(ActionEvent e){
//    		boardSize = 7;
//    		
//    		String[] numPossiblePlayers = {"3", "6"};
//        	DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
//        	numberOfPlayers.setModel(model);
//        	
//        	updatePlayerNumbers();
//    	}
//    }
//
//    /** The action listener for the back button.
//     */
//    private class BackListener implements ActionListener{
//    	public void actionPerformed(ActionEvent e){
//    		GameMenu menu = new GameMenu();
//    		setVisible(false);
//    	}
//    }
//    
//    /**
//     * The action listener for the player number combo box.
//     * 
//     * This changes the number of players to 2, 3 or 6
//     */
//    private class PlayerNumberListener implements ActionListener{
//    	public void actionPerformed(ActionEvent e){
//    		updatePlayerNumbers();
//    	}
//    }
//    
   
    
    /**
     * Updates the number of players and displays player types and colors appropriately
     */
    public void updatePlayerNumbers(){
    	numPlayers = Integer.parseInt( numberOfPlayers.getSelectedItem().toString() );
		System.out.println(numPlayers);
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
