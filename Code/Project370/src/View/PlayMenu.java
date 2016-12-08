package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Controller.Controller;
import Model.Board;

public class PlayMenu extends JFrame {
	
	private int boardSize = 5;
	
	private int numPlayers = 2;
	
	public JComboBox<Integer> numberOfPlayers;
	
	public JComboBox<String>[] playerTypes;
	
	public JComboBox<String>[] playerColors;
	
	public JComboBox<String>[] scoutScripts;
	
	public JComboBox<String>[] sniperScripts;

	public JComboBox<String>[] tankScripts;
	
	private String[] scoutScriptPaths;
	private String[] sniperScriptPaths;
	private String[] tankScriptPaths;
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		
		new PlayMenu("Choose board options.");
	
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayMenu(String title) {
    	super(title);
    	
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	this.setSize(1000, 400);
    	this.setVisible(true);
    	
    	//Initialize the menu panel
    	JPanel menuPanel;
    	menuPanel = new JPanel(new FlowLayout());
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
    		playerTypes[i].addActionListener(new PlayerTypeListener());
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
    	
    	try{
	    	loadScripts();
	    	
	    	JPanel scoutPanel = new JPanel(new GridLayout(7,1, 50, 25));
	    	JPanel sniperPanel = new JPanel(new GridLayout(7,1, 50, 25));
	    	JPanel tankPanel = new JPanel(new GridLayout(7,1, 50, 25));
	    	
	    	scoutPanel.add(new JLabel("Scout"));
	    	sniperPanel.add(new JLabel("Sniper"));
	    	tankPanel.add(new JLabel("Tank"));
	    	
	    	for (int i = 0; i < 6; i++){
	    		scoutPanel.add(scoutScripts[i]);
	    		sniperPanel.add(sniperScripts[i]);
	    		tankPanel.add(tankScripts[i]);
	    	}
	    	
	    	menuPanel.add(scoutPanel);
	    	menuPanel.add(sniperPanel);
	    	menuPanel.add(tankPanel);
    	} catch (FileNotFoundException e){
    		e.printStackTrace();
    		return;
    	}
    	
    	revalidate();
    }

	/** The action listener for the play button
     * 
     *  This takes the player to the PlayMenu to create a game
     */
    private class BeginListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    		//Controller.getInstance().inGameMenu = new InGameMenu(newBoard);
    		String[] scout = new String[numPlayers];
    		String[] sniper = new String[numPlayers];
    		String[] tank = new String[numPlayers];
    		Boolean[] humans = new Boolean[numPlayers];
    		
    		if(scoutScriptPaths != null && sniperScriptPaths != null && tankScriptPaths != null){
	    		for(int i = 0; i < numPlayers; i++){
	    			if(playerTypes[i].getSelectedIndex() == 0){	//AI
	    				humans[i] = false;
	    				scout[i] = scoutScriptPaths[scoutScripts[i].getSelectedIndex()];
	        			sniper[i] = sniperScriptPaths[sniperScripts[i].getSelectedIndex()];
	        			tank[i] = tankScriptPaths[tankScripts[i].getSelectedIndex()];
	    			} else {
	    				humans[i] = true;
	    				scout[i] = "";
	        			sniper[i] = "";
	        			tank[i] = "";
	    			}
	    		}
    		}
    		
    		
    		Board newBoard = new Board(boardSize, numPlayers, parseColors());
    		
    		/**
    		 * 
    		 * THE BOARD NEEDS TO GET THE SCRIPTS FOR EACH AI PLAYER
    		 * 
    		 * 
    		 * 
    		 */
    		Controller.getInstance().gameBoard = newBoard;
    		Controller.getInstance().inGameMenu.dispose();
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
        	
        	updateComboBoxes();
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
        	
        	updateComboBoxes();
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
    		updateComboBoxes();
    	}
    }
    /**
     * The action listener for the player type combo box. 
     */
    private class PlayerTypeListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		updateComboBoxes();
    	}
    }
    
    /**
     * Updates the number of players and displays player types and colors appropriately
     */
    private void updateComboBoxes(){
    	numPlayers = Integer.parseInt( numberOfPlayers.getSelectedItem().toString() );
		
		for (int i = 0; i < 6; i++){
    		if(i < numPlayers){
    			playerTypes[i].setVisible(true);
    			playerColors[i].setVisible(true);
    			scoutScripts[i].setVisible(true);
    			sniperScripts[i].setVisible(true);
    			tankScripts[i].setVisible(true);
    		} else {
    			playerTypes[i].setVisible(false);
    			playerColors[i].setVisible(false);
    			scoutScripts[i].setVisible(false);
    			sniperScripts[i].setVisible(false);
    			tankScripts[i].setVisible(false);
    		}
    		
    		if(playerTypes[i].getSelectedItem().toString() == "Human"){
    			scoutScripts[i].setVisible(false);
    			sniperScripts[i].setVisible(false);
    			tankScripts[i].setVisible(false);
    		}
    	}
    }

    @SuppressWarnings("unchecked")
	private void loadScripts() throws FileNotFoundException{
    	List<String> scouts = new LinkedList<String>();
    	List<String> scoutFiles = new LinkedList<String>();
    	List<String> snipers = new LinkedList<String>();
    	List<String> sniperFiles = new LinkedList<String>();
    	List<String> tanks = new LinkedList<String>();
    	List<String> tankFiles = new LinkedList<String>();
    	
    	File folder = new File("src/Model/scripts");
    	File[] robotScripts = folder.listFiles();
    	
    	for (int i = 0; i < robotScripts.length; i++){
    		
    		try {
				BufferedReader buffer = new BufferedReader(new FileReader(robotScripts[i]));
				JsonObject curObj = new JsonParser().parse(buffer).
									getAsJsonObject().get("script").getAsJsonObject();
				
				String type = curObj.get("class").getAsString();
				
				switch(type){
					case("Scout"):
						scouts.add(curObj.get("name").getAsString());
						scoutFiles.add(robotScripts[i].getPath());
						break;
					case("Sniper"):
						snipers.add(curObj.get("name").getAsString());
						sniperFiles.add(robotScripts[i].getPath());
						break;
					case("Tank"):
						tanks.add(curObj.get("name").getAsString());
						tankFiles.add(robotScripts[i].getPath());
						break;
				}
				
			} finally {
				;
			}
    	}
    	
    	scoutScriptPaths = new String[scoutFiles.size()];
    	sniperScriptPaths = new String[sniperFiles.size()];
    	tankScriptPaths = new String[tankFiles.size()];
    	
    	for(int i = 0; i < scoutFiles.size(); i++){
    		scoutScriptPaths[i] = scoutFiles.get(i);
    	}
    	
    	for(int i = 0; i < sniperFiles.size(); i++){
    		sniperScriptPaths[i] = sniperFiles.get(i);
    	}
    	
    	for(int i = 0; i < tankFiles.size(); i++){
    		tankScriptPaths[i] = tankFiles.get(i);
    	}
		
		String[] scoutNames = new String[scouts.size()];
		String[] sniperNames = new String[snipers.size()];
		String[] tankNames = new String[tanks.size()];
		
		scoutScripts = new JComboBox[6];
		sniperScripts = new JComboBox[6];
		tankScripts = new JComboBox[6];
		
		for(int i = 0; i < scouts.size(); i++){
    		scoutNames[i] = scouts.get(i);
    	}
    	
    	for(int i = 0; i < snipers.size(); i++){
    		sniperNames[i] = snipers.get(i);
    	}
    	
    	for(int i = 0; i < tanks.size(); i++){
    		tankNames[i] = tanks.get(i);
    	}
		
		for(int i = 0; i < 6; i++){
			scoutScripts[i] = new JComboBox<String>(scoutNames);
			sniperScripts[i] = new JComboBox<String>(sniperNames);
			tankScripts[i] = new JComboBox<String>(tankNames);

			if( i < numPlayers){
				scoutScripts[i].setVisible(true);
				sniperScripts[i].setVisible(true);
				tankScripts[i].setVisible(true);
			} else {
				scoutScripts[i].setVisible(false);
				sniperScripts[i].setVisible(false);
				tankScripts[i].setVisible(false);
			}
		}
    }

}