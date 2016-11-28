package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
    
    /** Border from the */
    private static final int EDGEBORDER = 50;
    
    private static final int SIDELENGTH = 30;
    
    /** @private Size of the board to draw */
    private int boardSize;
    
    /** @private Reference to list of all robot teams */
    private RobotTeam[] robotTeams;
    
    /** @private Reference to list of all robots */
    private Robot[] robots;
  
    /** @private reference to the current hex list for painting the component */
	private Hex[][] currentHexes;
	
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
		robots = new Robot[teams.length * 3];
		
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
						drawHex(currentHexes[i][j].getPositionX() + boardSize, currentHexes[i][j].getPositionY() + boardSize, g2);
				}
			}
			
		}
		
		for(int i = 0; i < robots.length; i++){						//Draw each robot on the board
			drawRobot(robots[i], g2);
		}
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
	 * Gets the current hex color to draw based on the board size
	 * 
	 * @param x The hex's x value
	 * @param y The hex's y value
	 * 
	 * @return The color to paint the hex
	 */
	private Color hexColor(int x, int y){
		Color color = Color.white;							//Default hex is white
		                                //CHECK FOR WEIRD OFFSET
		if(x == boardSize && y == 1)						//West side is red
			color = Color.red;
		else if(x == 1 && y == boardSize)					//North-West side is orange
			color = Color.orange;
		else if (x == 1 && y == boardSize * 2 - 1)			//North-East side is yellow
			color = Color.yellow;
		else if (x == boardSize && y == boardSize * 2 - 1)	//East side is green
			color = Color.green;
		else if (x == boardSize * 2 - 1 && y == 1)			//South-West side is purple
			color = Color.MAGENTA;
		else if (x == boardSize * 2 - 1 && y == boardSize)	//South-East side is blue
			color = Color.blue;
		
		return color;
	}
	
	/**
	 * Private function for drawing a hexagon on a Graphics2D object
	 * 
	 * @param i The array x-position of the hexagon
	 * @param j The array y-position of the hexagon
	 * @param g The Graphics2D to draw on
	 */
	private void drawHex(int i, int j, Graphics2D g){
			//Graphics2D g = (Graphics2D) getGraphics();
			
	    	int y = i * (s + t);                    
	    	int x = j * h + i * (h / 2);
	    	Polygon poly = createHex(x, y);         //CHANGE THIS
	    	
	    	g.setColor(hexColor(i, j));
	    	
	    	g.fillPolygon(poly);
	    	
	    	g.setColor(Color.BLACK);
	    	
	    	g.drawPolygon(poly);

	}
	
	/**
	 * Private function for drawing a robot on the Graphics2D object
	 * 
	 * @param toDraw The robot to draw
	 * @param g	The Graphics2D object to draw on
	 */
	private void drawRobot(Robot toDraw, Graphics2D g){
		Hex robotHex = toDraw.getPosition();
    	int y = (robotHex.getPositionY() + boardSize) * (s + t);	//Accounts for the board coordinate offset and centers Y
    																//Accounts for the board coordinate offset and centers X
    	int x = (robotHex.getPositionX() + boardSize) * h + (robotHex.getPositionY() + boardSize) * (h / 2);
    	
    	x += EDGEBORDER;											//Handles the pixelborder's x influence
    	y += EDGEBORDER;											//Handles the pixelborder's y influence
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

    	int x = xPos + EDGEBORDER;								//Current x equals the xPos + the border size
    	int y = yPos + EDGEBORDER;								//Current y equals the yPos + the border size
    	
    	int[] cx, cy;											//Arrays for x and y point coordinates
    	
    	cx = new int[] {x, x, x+r, x+r+r, x+r+r, x+r};			//Begins with the bottom left point and generates clockwise
    	cy = new int[] {y+t, y+s+t, y+s+t+t, y+s+t, y+t, y};	//Begins with the bottom left point and generates clockwise
    	
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
		
		imagePath += team.getColour().toLowerCase() + "_";	//Adds the robot's team color to the directory path
		
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