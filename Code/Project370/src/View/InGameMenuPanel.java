package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import Controller.Controller;
import Model.Board;
import Model.Hex;
import Model.Robot;
import Model.RobotTeam;

/**
 * @category View
 * 
 * InGameMenuPanel is a specialized JPanel for displaying the game's state
 * by drawing the board and it's robots, as well as displaying information
 * for hexes and providing fog of war. This updates as the game is played.
 */
public class InGameMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** @private The side length of each hex in pixels */
    private static final int SIDELENGTHFIVE = 38;
    
    /** @private The side length of each hex in pixels */
    private static final int SIDELENGTHSEVEN = 29;
    
    /** @private Size of the board to draw */
    private int boardSize;
    
    /** @private Reference to list of all robot teams */
    private RobotTeam[] robotTeams;
    
    /** @private Reference to list of all robots */
    private Robot[] robots;
  
    /** @private reference to the current hex list for painting the component */
	private Hex[][] currentHexes;
	
	/** @private The JPanel for displaying teams of robots */
	public JPanel teamDisplayPanel;
	
	/** @private The JPanel for displaying teams of robots */
	private JPanel hexDisplayPanel;
	
	/** @private The JLabel for displaying the current team's name */
	private JLabel currentTeamLabel;
	
	/** @private the side length of each hexagon in pixels */ 
	private int s = 0;
	
	/** @private t = The vertical gap between corner points */
	private int t = 0;
	
	/** @private r = The horizontal gap between corner points */
	private int r = 0;
	
	/** @private h = Two times the vertical gap */
	private int h = 0;
	
	/**
	 * @public Constructs a game board with the given size
	 * 
	 * @param size The size of the board
	 * @param teams The list of robot teams
	 */
	public InGameMenuPanel(int size, RobotTeam[] teams){
		super();
		boardSize = size;											//Set size of board
		robotTeams = teams;											//Set the reference to the teams
		setLayout(new BorderLayout());								//Set the panel layout
		
														//Set re-sizeable and max frame
        this.setVisible(true);
        this.setBounds(0, 0, 1250, 1000);
		
		JLabel currentTeamLabel = new JLabel("INSERT TEAM NAME");	//Create the current team label 
		add(currentTeamLabel, BorderLayout.NORTH);					//Add the current team label
		
		JTextPane actionLog = new JTextPane();						//Add the debug log text pane
		actionLog.setText("The game/debug log can go here :)");		//Set the text
		actionLog.setSize(11,1);									//Set the actionLog size
		add(actionLog, BorderLayout.EAST);							//Add the actionLog to the panel
		actionLog.setEditable(false);								//Set the text as uneditable
		
		hexDisplayPanel = new JPanel();								//Create the hex display panel
		hexDisplayPanel.setLayout(new GridLayout(10,2));			//Set the layout for the panel
		hexDisplayPanel.setSize(1,10);								//Set the size of the panel
		add(hexDisplayPanel, BorderLayout.EAST);					//Add the hex display panel
		
		teamDisplayPanel = new JPanel();							//Create the team display panel
		teamDisplayPanel.setLayout(new GridLayout(3,1));			//Set the layout for the team panel
		teamDisplayPanel.setSize(1,10);								//Set the size of the panel
		add(teamDisplayPanel, BorderLayout.WEST);					//Add the team display panel
		
		robots = new Robot[teams.length * 3];						//Create robot array
		
		for(int i = 0; i < teams.length; i++){						//Get reference to each robot
			Robot curRobots[] = teams[i].getTeamOfRobot();
			
			for(int j = 0; j < 3; j++){
				robots[i * 3 + j] = curRobots[j];
			}
		}
		
		if(size == 7)
			s = SIDELENGTHSEVEN;									//s = the side length of each hex in pixels
		else
			s = SIDELENGTHFIVE;
		
    	t = (int) (s / 2);											//t = s * sin(30)
    	r = (int) (s * Math.cos(Math.PI / 6));						//r = s * cos(30)
    	h = 2 * r;													//h = 2 * r
	}
	
	@Override
	/** Paints the board and draws all robots in the game 
	 * 
	 *@param g The graphics component to draw on
	 */
	protected void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;								//Casts g to Graphics2D
		
		super.paintComponent(g2);									//Calls the super() paintComponent on g2
		
		if(currentHexes != null){									//If the current list of hexes is not null
			
			for(int i = 0; i < currentHexes.length; i++){			//For each hex row of hexes
			
				for(int j = 0; j < currentHexes[i].length; j++){	//For each hex in the current row
					
					if(currentHexes[i][j] != null)					//If hex[i][j] exists
																	//Draw the hex and add the board size to account for coordinate offset
						drawHex(currentHexes[i][j], g2);
				}
			}
			
		}
		
		for(int i = 0; i < robots.length; i++){						//Draw each robot on the board if they are alive
			if(robots[i].isAlive()){
		         drawRobot(robots[i], g2);
			}
		}
		
		Board board = Controller.getInstance().gameBoard;
		drawTeamPanel(board.Teams[board.getCurrentTeam()]);
		drawHexPanel(board);
	}
	
	/**
	 * Draws the given board
	 * 
	 * @param board The board to draw
	 */
	public void reDraw(Board board){
		
		currentHexes = board.hexBoard;			//Set the reference to the current list of hexes
		
		repaint();								//Repaint the board
	}
	
	/**
	 * Draws the panel containing information on the current team playing
	 * 
	 * @param currentTeam The current team to display
	 */
	private void drawTeamPanel(RobotTeam currentTeam){
		
		if(!currentTeam.isHuman())				
			return;
		
		Robot currentRobot;
		JLabel curLabel;
		JLabel curStats;
		String statString = "";
		teamDisplayPanel.removeAll();			//Clear the current display
		
		for(int i = 0; i < 3; i++){				//For each robot, make a label and add robot stats
			currentRobot = currentTeam.getTeamOfRobot()[i];
			curLabel = new JLabel(new ImageIcon(getRobotImage(currentRobot) ) );
			
			statString = "<html> Movement: " + currentRobot.getMovementCur() + "/" + currentRobot.getMovementMax() + "<br>";
			statString += " Health: " + currentRobot.getHealth() + "<br>";
			statString += " Range: " + currentRobot.getRange() + "<br>";
			statString += " Damage: " + currentRobot.getDamage() + "</html>";
			curStats = new JLabel(statString);
			curStats.setSize(50,50);
			teamDisplayPanel.add(curLabel);		//Add the robot label to the display panel
			teamDisplayPanel.add(curStats);		//Add the stat label to the display panel
		}
		
		teamDisplayPanel.revalidate();
		teamDisplayPanel.repaint();
	}
	
	/**
	 * Draws the panel containing information on the current target hex
	 * 
	 * @param board The board containing the current hex
	 */
	private void drawHexPanel(Board board){
												
		if(board.getCurrentHex() == null){		//If there's no current hex, return
			return;
		}
		
		hexDisplayPanel.removeAll();			//Clear the current display
		
		Hex cur = board.getCurrentHex();		//Get the current hex
		
		Robot currentRobot;
		JLabel curLabel;
		JLabel curStats;
		String statString = "";
		
		
		if(cur.getOcc().size() > 0){			//If the hex has occupants
			Iterator<Robot> iterator = cur.getOcc().iterator();
									
			while(iterator.hasNext()){			//Create an iterator for the occupants
				currentRobot = iterator.next();
												//Adds the current robot's stats to a label
				statString = "<html> Movement: " + currentRobot.getMovementCur() + "/" + currentRobot.getMovementMax() + "<br>";
				statString += " Health: " + currentRobot.getHealth() + "<br>";
				statString += " Range: " + currentRobot.getRange() + "<br>";
				statString += " Damage: " + currentRobot.getDamage() + "</html>";
				curStats = new JLabel(statString);
				curStats.setSize(100,50);
				curLabel = new JLabel(new ImageIcon(getRobotImage(currentRobot) ) );
				hexDisplayPanel.add(curLabel);	//Adds the robot's image to hex display
				hexDisplayPanel.add(curStats);	//Adds the robot's stats to hex display
			}
		}
		
		hexDisplayPanel.revalidate();			//Revalidates the panel
		hexDisplayPanel.repaint();				//Repaints the panel
	}


	/**
	 * Gets the current hex color to draw based on the board size
	 * 
	 * @param x The hex's x value
	 * @param y The hex's y value
	 * 
	 * @return The color to paint the hex
	 */
	private Color hexColor(int x, int y){
        return Controller.getInstance().gameBoard.hexBoard[x-1][y-1].getColour();
	}
	
	/**
	 * Private function for drawing a hexagon on a Graphics2D object
	 * 
	 * The hex to draw
	 * @param g The Graphics2D to draw on
	 */
	private void drawHex(Hex hex, Graphics2D g){
			//Graphics2D g = (Graphics2D) getGraphics();
			
			int y = (hex.getPositionY() + boardSize) * (s + t);		//Get x and y coordinates to draw at
			int x = (hex.getPositionX() + boardSize) * h + (hex.getPositionY() + boardSize) * (h / 2);
			
	    	Polygon poly = createHex(x, y);							//Create the polygon for the hex
	    															//Set the color of the hex accounting for hex offset
	    	g.setColor(hexColor(hex.getPositionX() + boardSize, hex.getPositionY() + boardSize));
	    	
	    	g.fillPolygon(poly);									//Fill the polygon
	    	
	    	g.setColor(Color.black);								//Set color to black
	    	
	    	g.drawPolygon(poly);									//Outline Polygon

	}
	
	/**
	 * Private function for drawing a robot on the Graphics2D object
	 * 
	 * @param toDraw The robot to draw
	 * @param g	The Graphics2D object to draw on
	 */
	private void drawRobot(Robot toDraw, Graphics2D g){
		Hex robotHex = toDraw.getPosition();
		
		if(robotHex.getColour() == Color.GRAY){						//If the robot is on a hex hidden by fog of war
			return;													//Do not draw the robot
		}
		
    	int y = (robotHex.getPositionY() + boardSize) * (s + t);	//Accounts for the board coordinate offset and centers Y
    																//Accounts for the board coordinate offset and centers X
    	int x = (robotHex.getPositionX() + boardSize) * h + (robotHex.getPositionY() + boardSize) * (h / 2);

    	g.drawImage(getRobotImage(toDraw), x + r / 2, y + s / 2, this);	//Draws the robot 
	}	
    
	/**
	 * Creates a hexagonal polygon at the given x
	 * and y positions
	 * 
	 * @param xPos The x coordinate to draw at
	 * @param yPos The y coordinate to draw at
	 * 
	 * @return The polygon representing a hexagon
	 */
    private Polygon createHex(int xPos, int yPos){
    	
    	int[] cx, cy;											//Arrays for x and y point coordinates
    	
    	cx = new int[] {xPos, xPos, xPos+r, xPos+r+r, xPos+r+r, xPos+r};//Begins with the bottom left point and generates clockwise
    	cy = new int[] {yPos+t, yPos+s+t, yPos+s+t+t, yPos+s+t, yPos+t, yPos};	//Begins with the bottom left point and generates clockwise
    	
    	return new Polygon(cx, cy, 6);							//Returns the created hexagon
    }
    
    /**
     * Loads the correct image file from the resource folder
     * based on the type of robot
     * 
     * @param robot The robot that needs an image
     * 
     * @return The robot's image
     */
    private BufferedImage getRobotImage(Robot robot){
    	String imagePath = "/View/resources/";				//Creates initial directory
    	
    	RobotTeam team = robotTeams[robot.getTeam()];		//Gets the robot's team
		
    	if(team.getColour() == Color.red){					//Get the robot's color
            imagePath += "red_";
    	}else if(team.getColour() == Color.orange){
            imagePath += "orange_";
        }else if(team.getColour() == Color.yellow){
            imagePath += "yellow_";
        }else if(team.getColour() == Color.green){
            imagePath += "green_";
        }else if(team.getColour() == Color.blue){
            imagePath += "blue_";
        }else if(team.getColour() == Color.magenta){
            imagePath += "purple_";
        }
		
		switch(robot.getType()){		//Adds the rest of the directory path based on robot type
		case 1:
			imagePath += "scout.png";
			break;
		case 2:
			imagePath += "sniper.png";
			break;
		case 3:
			imagePath += "tank.png";
			break;
		}

		try{									//Tries to load and return the image from the path
			BufferedImage robotImage = ImageIO.read(getClass().getResourceAsStream(imagePath) );
			return robotImage;
		} catch (IOException e){				//Throws an exception if there is an error reading
			System.err.println(e);
		} catch (IllegalArgumentException e){	//Throws an error if the path is invalid
			System.err.println(e);
		}
			
		return null;							//Return null
    }
 

    
    public static void main(String args[]){
    	JFrame testFrame = new JFrame("IN-GAME-MENU-PANEL-TEST");
    	testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	testFrame.setSize(1000, 1000);
    	
    	Board testBoard = new Board(5, 1);
    	InGameMenuPanel testPanel = new InGameMenuPanel(testBoard.getSize(), testBoard.Teams);
    	testPanel.setSize(1000, 1000);
    	testFrame.add(testPanel);
    	testPanel.setVisible(true);
    	testPanel.reDraw(testBoard);
    	
    	testFrame.setVisible(true);
    }
}