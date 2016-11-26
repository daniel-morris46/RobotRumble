package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Model.Hex;
import Model.Robot;

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
	public InGameMenuPanel(int size){
		boardSize = size;

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
		
		
		for(int i = 0; i < boardSize * 2 - 1; i++){		// i = 0, 1, ... boardSize * 2 - 1    			
														//Hexagons to initialize will depend on which sections are being made
			if(i < boardSize){							//If we are before the halfway mark
														//j = boardSize - 1 ... boardSize * 2
				for(int j = boardSize - (i + 1); j < (boardSize * 2) - 1 ; j++){
        			drawHex(i,j);
        			//System.out.println("I: " + i + "  J: " + j);
    			}
			} else {									//Otherwise, we are halfway or past
														//j = i / boardSize ... (boardSize * 2) - (i % boardSize - 1)
				for(int j = (i / boardSize) - 1; j < (boardSize * 2) - (i % boardSize) - 2; j++){
        			drawHex(i,j);
    			}
			}
		}
		
		for(int i = 0; i < numberOfRobots; i++){		//Draw each robot on the board
			drawRobot(robot[i]);
		}
	}
	
	public void reDraw(Hex[] listOfHexes, Robot currentRobot, Hex selectedHex){
		
		for(int i = 0; i < boardSize * 2 - 1; i++){		// i = 0, 1, ... boardSize * 2 - 1    			
														//Hexagons to initialize will depend on which sections are being made
			if(i < boardSize){							//If we are before the halfway mark
														//j = boardSize - 1 ... boardSize * 2
				for(int j = boardSize - (i + 1); j < (boardSize * 2) - 1 ; j++){
					drawHex(i,j);
				}
    		} else {
    				
				for(int j = (i / boardSize) - 1; j < (boardSize * 2) - (i % boardSize) - 2; j++){
        			drawHex(i,j);
    			}
			}
		}
		
		repaint();
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
		
		if(x == boardSize - 1 && y == 0)		//West side is red
			color = Color.red;
		else if(x == 0 && y == boardSize - 1)		//North-West side is orange
			color = Color.orange;
		else if (x == 0 && y == boardSize * 2 - 2)
			color = Color.yellow;
		else if (x == boardSize - 1 && y == boardSize * 2 - 2)
			color = Color.green;
		else if (x == boardSize * 2 - 2 && y == 0)
			color = Color.pink;
		else if (x == boardSize * 2 - 2 && y == boardSize - 1)
			color = Color.blue;
		
		return color;
	}
	
	/**
	 * Private function for drawing a hexagon on a Graphics2D object
	 * 
	 * @param i The array x-position of the hexagon
	 * @param j The array y-position of the hexagon
	 * @param g The Graphics2D object to draw the hex on 
	 */
	private void drawHex(int i, int j){
			Graphics2D g = (Graphics2D) getGraphics();
			
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
	private void drawRobot(Robot toDraw){
    	//int y = toDraw.currentX * (s + t);
    	//int x = toDraw.currentY * h + toDraw.currentX * (h / 2);
    	
    	//x += PIXELBORDER;											//Current x equals the xPos + the border size
    	//y += PIXELBORDER;											//Current y equals the yPos + the border size
    	Graphics2D g = (Graphics2D) getGraphics();
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
}
