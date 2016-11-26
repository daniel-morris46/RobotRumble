package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Model.Robot;

/**
 * Specialized JPanel that contains a list of walkable hexagons, a list of robots,
 * and a reference to the current robot. This can be repainted to update the board.
 * 
 * @author Brandon
 *
 */
public class InGameMenuPanel {
	
	    private static final long serialVersionUID = 1L;
	    
	    private int boardSize;
	    
	    /** @public 2D array of booleans representing walkable hexagons */
		private Boolean[][] walkable;
		
		/** @public Constructs a game board with the given size */
		public InGameMenuPanel(int size){
			boardSize = size;
			walkable = new Boolean[size * 2][size * 2];
			
			for(int i = 0; i < size; i++)
				for(int j = 0; j < size; j++)
					walkable[i][j] = false;
			
			robot = new Robot[numberOfRobots];				//Create array of robots with the given number of robots
			
										
			for(int i = 0; i < numberOfRobots; i++){		//Initialize each robot in the array
				robot[i] = new Robot(boardSize - 1, boardSize - 1);
			}
			
			try{											//Try to load image files for robots into the game
				String address = "/student/ddm855/Downloads/370PeerProgramming/src/View/game_pieces";
				robot[0].image = ImageIO.read(new File(address + "/red_scout.png"));
				robot[1].image = ImageIO.read(new File(address + "/red_sniper.png"));
				robot[2].image = ImageIO.read(new File(address + "/red_tank.png"));
			} catch (IOException e){
				System.out.println(e.toString());
			}
			
			currentRobotIndex = 0;							//Initialize the current robot to be the first team's first robot
			currentRobot = robot[currentRobotIndex];
		}
    	
		@Override
    	public void paintComponent(Graphics g){
    		Graphics2D g2 = (Graphics2D) g;
    		
    		super.paintComponent(g2);
    		
    		
    		for(int i = 0; i < boardSize * 2 - 1; i++){		// i = 0, 1, ... boardSize * 2 - 1    			
    														//Hexagons to initialize will depend on which sections are being made
    			if(i < boardSize){							//If we are before the halfway mark
    														//j = boardSize - 1 ... boardSize * 2
    				for(int j = boardSize - (i + 1); j < (boardSize * 2) - 1 ; j++){
	    				walkable[i][j] = true;
	        			drawHex(i,j,g2);
	        			//System.out.println("I: " + i + "  J: " + j);
	    			}
    			} else {									//Otherwise, we are halfway or past
    														//j = i / boardSize ... (boardSize * 2) - (i % boardSize - 1)
    				for(int j = (i / boardSize) - 1; j < (boardSize * 2) - (i % boardSize) - 2; j++){
    					walkable[i][j] = true;
	        			drawHex(i,j,g2);
	    			}
    			}
    		}
    		
    		for(int i = 0; i < numberOfRobots; i++){		//Draw each robot on the booard
    			drawRobot(robot[i], g2);
    			
    		}
    	}
    	
    	public void reDraw(Robot currentRobot){
    		
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
    	private void drawHex(int i, int j, Graphics2D g){
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
	    	int y = toDraw.currentX * (s + t);
	    	int x = toDraw.currentY * h + toDraw.currentX * (h / 2);
	    	
	    	x += PIXELBORDER;											//Current x equals the xPos + the border size
	    	y += PIXELBORDER;											//Current y equals the yPos + the border size
	    	g.drawImage(toDraw.image, x + r / 2, y + s / 2, gamePanel);
    	}

    }	
}
