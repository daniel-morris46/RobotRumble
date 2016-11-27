package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Model.Board;
import Model.Hex;
import Model.Robot;
import Model.RobotTeam;
import Controller.Controller;

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
    
    private int currentRobotIndex;
    
	/** @category Hex Variables */
	
	/** @private the side length of each hexagon in pixels */ 
	private int s = 0;
	
	/** @private t = The vertical gap between corner points */
	private int t = 0;
	
	/** @private r = The horizontal gap between corner points */
	private int r = 0;
	
	/** @private h = Two times the vertical gap */
	private int h = 0;
	
	
	/** @category Buttons */
	
	
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
	protected void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		super.paintComponent(g2);
		
		if(currentHexes != null){
			
			for(int i = 0; i < currentHexes.length; i++){
			
				for(int j = 0; j < currentHexes[i].length; j++){
					
					if(currentHexes[i][j] != null)
						
						drawHex(currentHexes[i][j].getPositionX() + boardSize, currentHexes[i][j].getPositionY() + boardSize, g2);
				}
			}
			
		}
		
		for(int i = 0; i < robots.length; i++){		//Draw each robot on the board
			drawRobot(robots[i], g2);
		}
	}
	
	/** Private reference to the current hex list for painting the component */
	private Hex[][] currentHexes;
	
	public void reDraw(Hex[][] listOfHexes, int currentRobot, Hex selectedHex){
		
		currentHexes = listOfHexes;
		
		repaint();
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
		Color color = Color.white;				//Default hex is white
		                                //CHECK FOR WEIRD OFFSET
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
	    	Polygon poly = createHex(y, x);         //CHANGE THIS
	    	
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
    	int y = (robotHex.getPositionX() + boardSize) * (s + t);
    	int x = (robotHex.getPositionY() + boardSize) * h + (robotHex.getPositionX() + boardSize) * (h / 2);
    	
    	x += PIXELBORDER;											//Current x equals the xPos + the border size
    	y += PIXELBORDER;											//Current y equals the yPos + the border size
    	g.drawImage(getRobotImage(toDraw), x + r / 2, y + s / 2, this);
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
    	String imagePath = "/View/resources/";
    	
    	RobotTeam team = robotTeams[robot.getTeam()];
		
		imagePath += team.getColour().toLowerCase() + "_";
		
		for(int i = 0; i < 3; i++){     //USE ROBOT TYPE REFERENCE HERE
			if(team.getTeamOfRobot()[i] == robot){
				switch(i){
				case 0:
					imagePath += "scout.png";
					break;
				case 1:
					imagePath += "sniper.png";
					break;
				case 2:
					imagePath += "tank.png";
					break;
				}
			}
		}

		try{
			BufferedImage robotImage = ImageIO.read(getClass().getResourceAsStream(imagePath) );
			return robotImage;
		} catch (IOException e){
			System.err.println(e);
		} catch (IllegalArgumentException e){
			System.err.println(e);
		}
			
		return null;
    }
    
    /**
	 * Creates the game board with the given size, as well as all required buttons
	 * and their action listeners.
	 * 
	 * @param size The size of the board
	 */
	public void remakeBoard(Board board){
        reDraw(board.hexBoard, board.getCurrentRobot(), board.getCurrentHex());								//Repaint the panel
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
    	testPanel.remakeBoard(testBoard);
    	testPanel.reDraw(testBoard.getHexBoard(), testBoard.getCurrentRobot(), testBoard.getHexBoard()[0][0]);
    	
    	testFrame.setVisible(true);
    }
}