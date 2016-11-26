package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Model.Board;
import Model.Hex;
import Model.Robot;
import Model.RobotTeam;

/**
 * Specialized JPanel that contains a list of walkable hexagons, a list of robots,
 * and a reference to the current robot. This can be repainted to update the board.
 * 
 * @author Brandon
 *
 */
public class InGameMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private static final int PIXELBORDER = 50;
    
    private static final int SIDELENGTH = 30;
    
    private int boardSize;
    
    private RobotTeam[] robotTeams;
    
    private Robot[] robots;
    
	/** @category Hex Variables */
	
	/** @private the side length of each hexagon in pixels */ 
	private int s = 0;
	
	/** @private t = The vertical gap between corner points */
	private int t = 0;
	
	/** @private r = The horizontal gap between corner points */
	private int r = 0;
	
	/** @private h = Two times the vertical gap */
	private int h = 0;
	
    
    /** @public 2D array of booleans representing walkable hexagons */
//		private Boolean[][] walkable;
	
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
//			walkable = new Boolean[size * 2][size * 2];
//			
//			for(int i = 0; i < size; i++)
//				for(int j = 0; j < size; j++)
//					walkable[i][j] = false;
		
	}
	
	@Override
	protected void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		super.paintComponent(g2);
		
		if(currentHexes != null){
			for(int i = 0; i < currentHexes.length; i++){
			
				for(int j = 0; j < currentHexes[i].length; j++){
					if(currentHexes[i][j] != null)
						drawHex(currentHexes[i][j].getPositionX() + boardSize, currentHexes[i][j].getPositionY() + boardSize, g2);
					//drawHex(5,5, g2);
				}
			}
		}
		
		drawHex(5,5, g2);
		
//		for(int i = 0; i < numberOfRobots; i++){		//Draw each robot on the board
//			drawRobot(robot[i]);
//		}
	}
	
	/** Private reference to the current hex list for painting the component */
	private Hex[][] currentHexes;
	
	public void reDraw(Hex[][] listOfHexes, int currentRobot, Hex selectedHex){
		Graphics2D swag = (Graphics2D) getGraphics();
		
		currentHexes = listOfHexes;
		drawHex(5,5, swag);
		//for(int i = 0; i < listOfHexes.length; i++){
			
			//for(int j = 0; j < listOfHexes[i].length; j++){
				//drawHex(listOfHexes[i][j].getPositionX() + boardSize, listOfHexes[i][j].getPositionY() + boardSize);
				//drawHex(5,5);
			//}
			
		//}
		
//		for(int i = 0; i < boardSize * 2 - 1; i++){		// i = 0, 1, ... boardSize * 2 - 1    			
//														//Hexagons to initialize will depend on which sections are being made
//			if(i < boardSize){							//If we are before the halfway mark
//														//j = boardSize - 1 ... boardSize * 2
//				for(int j = boardSize - (i + 1); j < (boardSize * 2) - 1 ; j++){
//					drawHex(i,j);
//				}
//    		} else {
//    				
//				for(int j = (i / boardSize) - 1; j < (boardSize * 2) - (i % boardSize) - 2; j++){
//        			drawHex(i,j);
//    			}
//			}
//		}
		
		//repaint();
    }
    		
    		
//    		try{											//Try to load image files for robots into the game
//				String address = "/student/ddm855/Downloads/370PeerProgramming/src/View/game_pieces";
//				robot[0].image = ImageIO.read(new File(address + "/red_scout.png"));
//				robot[1].image = ImageIO.read(new File(address + "/red_sniper.png"));
//				robot[2].image = ImageIO.read(new File(address + "/red_tank.png"));
//			} catch (IOException e){
//				System.out.println(e.toString());
//			}
	
	/**
	 * Gets the current hex color to draw based on the board size
	 * 
	 * @param x The hex's x value
	 * @param y The hex's y value
	 * 
	 * @return The color to paint the hex
	 */
	private Color hexColor(int x, int y){
		Color color = Color.white;				//Default hex is white
		
		if(x == boardSize && y == 1)		//West side is red
			color = Color.red;
		else if(x == 1 && y == boardSize)		//North-West side is orange
			color = Color.orange;
		else if (x == 1 && y == boardSize * 2 - 1)
			color = Color.yellow;
		else if (x == boardSize && y == boardSize * 2 - 1)
			color = Color.green;
		else if (x == boardSize * 2 - 1 && y == 1)
			color = Color.pink;
		else if (x == boardSize * 2 - 1 && y == boardSize)
			color = Color.blue;
		
		return color;
	}
	
	/**
	 * Private function for drawing a hexagon on a Graphics2D object
	 * 
	 * @param i The array x-position of the hexagon
	 * @param j The array y-position of the hexagon
	 */
	private void drawHex(int i, int j, Graphics2D g){
			//Graphics2D g = (Graphics2D) getGraphics();
			
	    	int x = i * (s + t);
	    	int y = j * h + i * (h / 2);
	    	Polygon poly = createHex(y, x);
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
    	int y = robotHex.getPositionX() * (s + t);
    	int x = robotHex.getPositionY() * h + robotHex.getPositionX() * (h / 2);
    	
    	x += PIXELBORDER;											//Current x equals the xPos + the border size
    	y += PIXELBORDER;											//Current y equals the yPos + the border size
    	//g.drawImage(toDraw.image, x + r / 2, y + s / 2, gamePanel);
	}	
    
    private Polygon createHex(int xPos, int yPos){

    	int x = xPos + PIXELBORDER;		//Current x equals the xPos + the border size
    	int y = yPos + PIXELBORDER;		//Current y equals the yPos + the border size
    	
    	int[] cx, cy;					//Arrays for x and y point coordinates
    	
    	cx = new int[] {x, x, x+r, x+r+r, x+r+r, x+r};			//Begins with the bottom left point and generates clockwise
    	cy = new int[] {y+t, y+s+t, y+s+t+t, y+s+t, y+t, y};	//Begins with the bottom left point and generates clockwise
    	
    	return new Polygon(cx, cy, 6);	//Returns the created hexagon
    }
    
    private BufferedImage getRobotImage(Robot robot){
    	String imagePath = "";
    	
    	RobotTeam team = robotTeams[robot.getTeam()];
		
		imagePath = team.getColour() + "_";
		
		for(int i = 0; i < 3; i++){
			if(team.getTeamOfRobot()[i] == robot){
				switch(i){
				case 0:
					imagePath += "SCOUT.png";
					break;
				case 1:
					imagePath += "SNIPER.png";
					break;
				case 2:
					imagePath += "TANK.png";
					break;
				}
			}
		}
		
		try{
			BufferedImage robotImage = ImageIO.read(new File(imagePath));
			return robotImage;
		} catch (IOException e){
			System.err.println(e);
		}
			
		return null;
    }
    
    
    
    
    
    
    public static void main(String args[]){
    	JFrame testFrame = new JFrame("IN-GAME-MENU-PANEL-TEST");
    	testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	testFrame.setVisible(true);
    	testFrame.setSize(1000, 1000);
    	
    	Board swag = new Board(5, 1);
    	InGameMenuPanel testPanel = new InGameMenuPanel(5,swag.Teams);
    	testPanel.setSize(1000, 1000);
    	testFrame.add(testPanel);
    	testPanel.setVisible(true);
    	testPanel.reDraw(swag.getHexBoard(), swag.getCurrentRobot(), swag.getHexBoard()[0][0]);
    }
}
