package View;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Controller.Controller;
import Model.Board;

/**
 * 
 * Pair Programming Done by Brandon and Kevin
 * 
 */


public class PlayMenu extends JFrame {
	
	/** The size of the board (default is 5) */
	private int boardSize = 5;
	
	/** The number of robot teams (default is 2) */
	private int numPlayers = 2;
	
	/** The ComboBox for storing the selected number of players */
	private JComboBox<Integer> numberOfPlayers;
	
	/** The ComboBox for storing the selected types of each player (HUMAN/AI) */
	private JComboBox<String>[] playerTypes;
	
	/** The ComboBox for storing the selected color for each player */
	private JComboBox<String>[] playerColors;
	
	/** The ComboBox for storing selected scout scripts */
	private JComboBox<String>[] scoutScripts;
	
	/** The ComboBox for storing selected sniper scripts */
	private JComboBox<String>[] sniperScripts;

	/** The ComboBox for storing selected tank scripts */
	private JComboBox<String>[] tankScripts;
	
	/** The array representing the path of each scout script */
	private String[] scoutScriptPaths;

	/** The array representing the path of each sniper script */
	private String[] sniperScriptPaths;

	/** The array representing the path of each tank script */
	private String[] tankScriptPaths;
	
	private static final long serialVersionUID = 1L;

	/**
	 * Gets an instance of the controller, starting the system
	 */
	public static void main(String[] args){
		Controller.getInstance();
	}
	
	/**
	 * Creates the play menu, providing the user with game configuration options
	 * such as board size, number of players, colors, and player types. As well,
	 * the user will be able to choose robot scripts for each AI team.
	 * 
	 * @param title The frame's title to display at the top
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayMenu(String title) {
    	super(title);
    	
    	GridBagConstraints c = new GridBagConstraints();
    	
    	
    	//Import a new font to use for the text
    	Font newFont = null;
		try {
			newFont = Font.createFont(Font.PLAIN, getClass().getResource("/View/resources/ARCADECLASSIC.TTF").openStream());
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		//Set the Graphics for the new font
    	GraphicsEnvironment gFont = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	gFont.registerFont(newFont);
    	
    	newFont = newFont.deriveFont(15f);
    	
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	
    	//Set re-sizeable and max frame
        this.setVisible(true);
        this.setBounds(0, 0, 1250, 1000);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	
    	//Initialize the play menu panel
    	JPanel playPanel;
    	playPanel = new JPanel(new GridBagLayout());
    	playPanel.setSize(400,  600);
    	playPanel.setVisible(true);
    	playPanel.setBackground(Color.CYAN);
    	playPanel.doLayout();
    	this.add(playPanel);
    	
    	//Initialize the begin button and add it to the menu panel
    	JButton beginButton;
    	beginButton = new JButton("Begin");
    	beginButton.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	beginButton.setSize(100, 250);
    	beginButton.setPreferredSize(new Dimension(150, 50));
    	beginButton.setVisible(true);
    	beginButton.addActionListener(new BeginListener());
    	playPanel.add(beginButton);
    	
    	//Initializing back button
    	JButton backButton;
    	backButton = new JButton("Back");
    	backButton.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	backButton.setSize(100, 250);
    	backButton.setPreferredSize(new Dimension(150, 50));
    	backButton.setVisible(true);
    	backButton.addActionListener(new BackListener());
    	playPanel.add(backButton);
    	
    	//Initialize the label for the board size
    	JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 50, 25));
    	JLabel boardSizeTitle = new JLabel("Board Size");
    	boardSizeTitle.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	buttonPanel.setBackground(Color.CYAN);
    	buttonPanel.add(boardSizeTitle);
  
    	//Initialize the five player board size
    	JRadioButton sizeFive = new JRadioButton("5");
    	sizeFive.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	sizeFive.setSize(100, 250);
    	sizeFive.setPreferredSize(new Dimension(150, 50));
    	sizeFive.setVisible(true);
    	sizeFive.addActionListener(new SizeFiveListener());
    	sizeFive.setBackground(Color.CYAN);
    	buttonPanel.add(sizeFive);
    	
    	//Initialize the seven player board size
    	JRadioButton sizeSeven = new JRadioButton("7");
    	sizeSeven.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	sizeSeven.setSize(100, 250);
    	sizeSeven.setPreferredSize(new Dimension(150, 50));
    	sizeSeven.setVisible(true);
    	sizeSeven.addActionListener(new SizeSevenListener());
    	sizeSeven.setBackground(Color.CYAN);
    	buttonPanel.add(sizeSeven);
    	
    	//Create a button group and add the five and seven player buttons
    	ButtonGroup sizeButtons = new ButtonGroup();
    	sizeButtons.add(sizeFive);
    	sizeButtons.add(sizeSeven);
    	sizeFive.setSelected(true);
    	playPanel.add(buttonPanel);
    	
    	//Initialize the label for the player number size
    	JPanel playerNumPanel = new JPanel(new GridLayout(2, 1, 50, 25));
    	JLabel playerNumTitle = new JLabel("Number of Players");
    	playerNumTitle.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	playerNumPanel.setBackground(Color.CYAN);
    	playerNumPanel.add(playerNumTitle);
    	
    	
    	
    	String[] numPossiblePlayers = {"2", "3"};
    	numberOfPlayers = new JComboBox(numPossiblePlayers);
    	numberOfPlayers.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	numberOfPlayers.setSize(100,100);
    	numberOfPlayers.addActionListener(new PlayerNumberListener());
    	
    	playerNumPanel.add(numberOfPlayers);
    	playPanel.add(playerNumPanel);
    	
    	
    	numberOfPlayers.setVisible(true);
    	
    	String[] playerTypeOptions = {"Computer", "Human"};
    	playerTypes = new JComboBox[6];
    	
    	
    	
    	JPanel playerPanel = new JPanel(new GridLayout(7,1, 50, 25));
    	playPanel.add(playerPanel);
    	JLabel playerTitle = new JLabel("Player Type    ");
    	playerPanel.add(playerTitle);
    	playerPanel.setBackground(Color.CYAN);
    	playerTitle.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	
    	
    	
    	for (int i = 0; i < 6; i++){
    		playerTypes[i] = new JComboBox(playerTypeOptions);
    		playerTypes[i].addActionListener(new PlayerTypeListener());
    		playerTypes[i].setSize(100, 100);
    		playerTypes[i].setFont(new Font("ARCADECLASSIC", Font.PLAIN, 25));
    		

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
    	playPanel.add(colorPanel);
    	colorPanel.setBackground(Color.CYAN);
    	colorPanel.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	
    	JLabel colorTitle = new JLabel("Team Color     ");
    	colorPanel.add(colorTitle);
    	colorTitle.setBackground(Color.CYAN);
    	colorTitle.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
    	
    	for (int i = 0; i < 6; i++){
    		playerColors[i] = new JComboBox(playerColorOptions);
    		playerColors[i].setFont(new Font("ARCADECLASSIC", Font.PLAIN, 25));
    		
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
	    	scoutPanel.setBackground(Color.CYAN);
	    	sniperPanel.setBackground(Color.CYAN);
	    	tankPanel.setBackground(Color.CYAN);
	    	
	    	JLabel scoutTitle = new JLabel("Scout");
	    	scoutPanel.add(scoutTitle);
	    	scoutTitle.setBackground(Color.CYAN);
	    	scoutTitle.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
	    	
	    	JLabel sniperTitle = new JLabel("Sniper");
	    	sniperPanel.add(sniperTitle);
	    	sniperTitle.setBackground(Color.CYAN);
	    	sniperTitle.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));
	    	
	    	JLabel tankTitle = new JLabel("Tank");
	    	tankPanel.add(tankTitle);
	    	tankTitle.setBackground(Color.CYAN);
	    	tankTitle.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 40));   	
	    
	    	
	    	for (int i = 0; i < 6; i++){
	    		scoutPanel.add(scoutScripts[i]);
	    		sniperPanel.add(sniperScripts[i]);
	    		tankPanel.add(tankScripts[i]);
	    		
	    	}
	    	
	    	playPanel.add(scoutPanel);
	    	playPanel.add(sniperPanel);
	    	playPanel.add(tankPanel);
	    	
	    	scoutPanel.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 30));
	    	sniperPanel.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 30));
	    	tankPanel.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 30));
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
        	DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel(numPossiblePlayers);
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
        	DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel(numPossiblePlayers);
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
		
    	//Display each combo box based on the number of players
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
    		
    		//If the current team's player type is human, we do not need scripts
    		if(playerTypes[i].getSelectedItem().toString() == "Human"){
    			scoutScripts[i].setVisible(false);
    			sniperScripts[i].setVisible(false);
    			tankScripts[i].setVisible(false);
    		}
    	}
    }

    /** Loads all scripts from the src/Model/scripts folder, sorts them based on
     *  their robot type and initializes the combo boxes for each team
     *  based on the player size and script amount.
     *  
     * @throws FileNotFoundException If there are no files found, throw an exception
     */
    @SuppressWarnings("unchecked")
	private void loadScripts() throws FileNotFoundException{
    	
    	//Initializing lists for holding robots
    	List<String> scouts = new LinkedList<String>();				
    	List<String> snipers = new LinkedList<String>();
    	List<String> tanks = new LinkedList<String>();
    	
    	//Initializing lists for holding robot scripts
    	List<String> scoutFiles = new LinkedList<String>();			
    	List<String> sniperFiles = new LinkedList<String>();
    	List<String> tankFiles = new LinkedList<String>();
    	
    	//Gets the directory of the scripts
    	File folder = new File("src/Model/scripts");				
    	
    	//Gets each file in the script directory
    	File[] robotScripts = folder.listFiles();					
    	
    	//For each robot script
    	for (int i = 0; i < robotScripts.length; i++){				
    		
    		try {		
    			//Try parsing it into a JSON object
				BufferedReader buffer = new BufferedReader(new FileReader(robotScripts[i]));
				JsonObject curObj = new JsonParser().parse(buffer).
									getAsJsonObject().get("script").getAsJsonObject();
				
				//Get the robot's type
				String type = curObj.get("class").getAsString();	
				
				switch(type){										
					case("Scout"):				
						//Add scout robots to the scout list
						scouts.add(curObj.get("name").getAsString());
						scoutFiles.add(robotScripts[i].getPath());
						break;
					case("Sniper"):									
						//Add sniper robots to the sniper list
						snipers.add(curObj.get("name").getAsString());
						sniperFiles.add(robotScripts[i].getPath());
						break;
					case("Tank"):									
						//Add tank robots to the tank list
						tanks.add(curObj.get("name").getAsString());
						tankFiles.add(robotScripts[i].getPath());
						break;
				}
				
			} finally {
				;
			}
    	}
    	
    	//Create an array for holding robot script paths
    	scoutScriptPaths = new String[scoutFiles.size()];	
    	sniperScriptPaths = new String[sniperFiles.size()];
    	tankScriptPaths = new String[tankFiles.size()];
    	
    	//Get file path for each scout
    	for(int i = 0; i < scoutFiles.size(); i++){			
    		scoutScriptPaths[i] = scoutFiles.get(i);
    	}
    	
    	//Get file path for each sniper
    	for(int i = 0; i < sniperFiles.size(); i++){		
    		sniperScriptPaths[i] = sniperFiles.get(i);
    	}
    	
    	//Get file path for each tank
    	for(int i = 0; i < tankFiles.size(); i++){			
    		tankScriptPaths[i] = tankFiles.get(i);
    	}
		
    	//Create arrays for holding robot names
		String[] scoutNames = new String[scouts.size()];	
		String[] sniperNames = new String[snipers.size()];	
		String[] tankNames = new String[tanks.size()];
		
		//Get name of each scout
		for(int i = 0; i < scouts.size(); i++){
    		scoutNames[i] = scouts.get(i);
    	}
    	
		//Get name of each sniper
    	for(int i = 0; i < snipers.size(); i++){
    		sniperNames[i] = snipers.get(i);
    	}
    	
    	//Get name of each tank
    	for(int i = 0; i < tanks.size(); i++){
    		tankNames[i] = tanks.get(i);
    	}

    	//Initialize combo box array of size 6, for each robot class for each team
    	scoutScripts = new JComboBox[6];							
    	sniperScripts = new JComboBox[6];
    	tankScripts = new JComboBox[6];
    	
    	//Initialize all six combo boxes for each class using the robot names
		for(int i = 0; i < 6; i++){
			scoutScripts[i] = new JComboBox<String>(scoutNames);
			sniperScripts[i] = new JComboBox<String>(sniperNames);
			tankScripts[i] = new JComboBox<String>(tankNames);
			
			scoutScripts[i].setFont(new Font("ARCADECLASSIC", Font.PLAIN, 25));
			sniperScripts[i].setFont(new Font("ARCADECLASSIC", Font.PLAIN, 25));
			tankScripts[i].setFont(new Font("ARCADECLASSIC", Font.PLAIN, 25));

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