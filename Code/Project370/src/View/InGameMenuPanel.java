package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controller.Controller;
import Model.Board;
import Model.Hex;
import Model.Robot;
import Model.RobotTeam;

/**
 * @category View
 * 
 * Specialized JPanel that contains a list of walkable 
 * hexagons, a list of robots, and a reference to the 
 * current robot. This can be repainted to update the board.
 */
public class InGameMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** @private The side length of each hex in pixels */
    private static final int SIDELENGTH = 30;
    
    /** @private Size of the board to draw */
    private int boardSize;
    
    /** @private Reference to list of all robot teams */
    private RobotTeam[] robotTeams;
    
    /** @private Reference to list of all robots */
    private Robot[] robots;
  
    /** @private reference to the current hex list for painting the component */
	private Hex[][] currentHexes;
	
	/** @private The JPanel for displaying teams of robots */
	private JPanel teamDisplayPanel;
	
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
	
	/** @public Constructs a game board with the given size */
	public InGameMenuPanel(int size, RobotTeam[] teams){
		super();
		boardSize = size;
		robotTeams = teams;
		setLayout(new BorderLayout());
		robots = new Robot[teams.length * 3];
		JLabel currentTeamLabel = new JLabel("INSERT TEAM NAME");
		add(currentTeamLabel, BorderLayout.NORTH);
		teamDisplayPanel = new JPanel();
		teamDisplayPanel.setLayout(new GridLayout(3,1));
		teamDisplayPanel.setSize(1,10);
		add(teamDisplayPanel, BorderLayout.WEST);
		
		
		for(int i = 0; i < teams.length; i++){
			Robot curRobots[] = teams[i].getTeamOfRobot();
			
			for(int j = 0; j < 3; j++){
				robots[i * 3 + j] = curRobots[j];
			}
		}
		
		s = SIDELENGTH;							//s = the side length of each hex in pixels
    	t = (int) (s / 2);						//t = s * sin(30)
    	r = (int) (s * Math.cos(Math.PI / 6));	//r = s * cos(30)
    	h = 2 * r;								//
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
		
		for(int i = 0; i < robots.length; i++){						//Draw each robot on the board
			drawRobot(robots[i], g2);
		}
		
		Board board = Controller.getInstance().gameBoard;
		drawTeamPanel(board.Teams[board.getCurrentTeam()]);
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
	
	private void drawTeamPanel(RobotTeam currentTeam){
		
		if(!currentTeam.isHuman())
			return;
		
		Robot currentRobot;
		JLabel curLabel;
		JLabel curStats;
		String statString = "";
		teamDisplayPanel.removeAll();
		
		for(int i = 0; i < 3; i++){
			currentRobot = currentTeam.getTeamOfRobot()[i];
			curLabel = new JLabel(new ImageIcon(getRobotImage(currentRobot) ) );
			statString = "M: " + currentRobot.getMovementCur() + "/" + currentRobot.getMovementMax() + "  ";
			statString += "H: " + currentRobot.getHealth() + "  ";
			statString += "R: " + currentRobot.getRange();
			curStats = new JLabel(statString);
			teamDisplayPanel.add(curLabel);
			teamDisplayPanel.add(curStats);
		}
		
		teamDisplayPanel.revalidate();
		teamDisplayPanel.repaint();
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
		
    	if(team.getColour() == Color.red){
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
    	
		//imagePath += team.getColour().toLowerCase() + "_";	//Adds the robot's team color to the directory path
		
		switch(robot.getType()){							//Adds the rest of the directory path based on robot type
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

		try{												//Tries to load and return the image from the path
			BufferedImage robotImage = ImageIO.read(getClass().getResourceAsStream(imagePath) );
			return robotImage;
		} catch (IOException e){							//Throws an exception if there is an error reading
			System.err.println(e);
		} catch (IllegalArgumentException e){				//Throws an error if the path is invalid
			System.err.println(e);
		}
			
		return null;										//Return null
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