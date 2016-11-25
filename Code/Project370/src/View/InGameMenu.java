package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class InGameMenu extends JFrame{
	
	/**@private Singleton instance for the class, constructed with a size of 5 by default */
	private static InGameMenu INSTANCE = new InGameMenu(5);
	
	/**@private Gap between board and top of frame in pixels*/
	private int PIXELBORDER = 100;
	
	/**@public The size of the board in hex's per edge*/
	public int boardSize;
	
	/**@public The number of robots in the game*/
	public int numberOfRobots = 3;
	
	/**@public The current robot */
	public Robot currentRobot;
	
	/**@public The panel where all images will be drawn*/
	public DrawingPanel gamePanel;
	
	/** @category Buttons */
	
	public JButton leftButton;
	public JButton rightButton;
	public JButton forfeitButton;
	public JButton exitButton;
	public JButton actionToggleButton;
	public JButton actionButton;
	public JButton endPlayButton;
	
	public boolean actionToggle = true;
	
	
	/** @category Hex Variables */
	
	/** @private the side length of each hexagon in pixels */ 
	private int s = 0;
	
	/** @private t = The vertical gap between corner points */
	private int t = 0;
	
	/** @private r = The horizontal gap between corner points */
	private int r = 0;
	
	/** @private h = Two times the vertical gap */
	private int h = 0;
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * Private constructor to make use of the singleton pattern.
	 * Constructs the frame and board using the given hexes per edge.
	 * 
	 * @param size The size of the board
	 */
	private InGameMenu(int size) {
        super("C4 Super Robot Rumble 370");
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        boardSize = size;
        setVisible(true);
        setSize(1400,  1000);
        setBySide(30);
        remakeBoard(size);
    }

	/**
	 * Creates the game board with the given size, as well as all required buttons
	 * and their action listeners.
	 * 
	 * @param size The size of the board
	 */
	public void remakeBoard(int size){
		if(gamePanel != null)
			remove(gamePanel);
		
		gamePanel = new DrawingPanel(size);
        gamePanel.setSize(500, 500);
        
        actionToggleButton = new JButton("*");	//Creating action toggle button and adding it to panel
        actionToggleButton.addActionListener(new ActionToggleButtonListener());
        gamePanel.add(actionToggleButton);
        actionToggleButton.setVisible(true);
        
        leftButton = new JButton("<");			//Creating left button and adding it to panel
        leftButton.addActionListener(new LeftButtonListener());
        gamePanel.add(leftButton);
        leftButton.setVisible(true);
        
        rightButton = new JButton(">");			//Creating right button and adding it to panel
        rightButton.addActionListener(new RightButtonListener());
        gamePanel.add(rightButton);
        rightButton.setVisible(true);

        actionButton = new JButton("Move");		//Creating action button and adding it to panel
        actionButton.addActionListener(new ActionButtonListener());
        gamePanel.add(actionButton);
        actionButton.setVisible(true);
        
        endPlayButton = new JButton("End Play");//Creating end play button and adding it to panel
        endPlayButton.addActionListener(new EndPlayButtonListener());
        gamePanel.add(endPlayButton);
        endPlayButton.setVisible(true);
        
        forfeitButton = new JButton("Forfeit");	//Creating forfeit button and adding it to panel
        forfeitButton.addActionListener(new ForfeitButtonListener());
        gamePanel.add(forfeitButton);
        forfeitButton.setVisible(true);
        
        exitButton = new JButton("Exit");		//Creating exit button and adding it to panel
        exitButton.addActionListener(new ExitButtonListener());
        exitButton.setVisible(true);
        gamePanel.add(exitButton);
        
        this.add(gamePanel);					//Add the game panel to the JFrame
        gamePanel.setVisible(true);				//Set the game panel as visible
        gamePanel.repaint();					//Repaint the panel
	}
	
	public static InGameMenu Instance(){
		return INSTANCE;
	}
	
	/**
	 * BUTTON LISTENERS
	 */
	
	/**
	 * Toggles between moving and shooting
	 *
	 */
	private class ActionToggleButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    		InGameMenu menu = InGameMenu.Instance();
    		if(menu.actionToggle == true){
    			menu.actionToggle = false;
    			menu.actionButton.setText("Shoot");
    		} else {
    			menu.actionToggle = true;
    			menu.actionButton.setText("Move");
    		}
    	}
    }
	
	/**
	 * Rotates the current robot left
	 *
	 */
	private class LeftButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		gamePanel.robot[gamePanel.currentRobotIndex].TurnLeft();
    	}
    }
	
	/**
	 * Rotates the current robot right
	 *
	 */
	private class RightButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		gamePanel.robot[gamePanel.currentRobotIndex].TurnRight();
    	}
    }
	
	/**
	 * Forfeits the game
	 *
	 */
	private class ForfeitButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		//TELL CONTROLLER TO FORFEIT
    	}
    }
	
	/**
	 * Exits the game
	 *
	 */
	private class ExitButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		GameMenu menu = new GameMenu();
    		setVisible(false);
    	}
    }
	
	/**
	 * Either moves or attacks with the current robot
	 *
	 */
	private class ActionButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if(InGameMenu.Instance().actionToggle == true){				//MOVING
    			gamePanel.robot[gamePanel.currentRobotIndex].Move();
    		} else {													//ATTACKING
    			//TELL CONTROLLER TO ATTACK THE CURRENT TARGET HEX
    		}
    	}
    }
	
	/**
	 * The listener for the end play button, which will
	 * cycle to the next current robot to play.
	 *
	 */
	private class EndPlayButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if(gamePanel.currentRobotIndex < numberOfRobots - 1){
    			gamePanel.currentRobotIndex += 1;
    		} else {
    			gamePanel.currentRobotIndex = 0;
    		}
    		
    		currentRobot = gamePanel.robot[gamePanel.currentRobotIndex];
    		//TELL CONTROLLER TO END THE CURRENT PLAY AND CHANGE THE CURRENT ROBOT
    	}
    }
    
	public static void main(String[] args){
    	InGameMenu swag = InGameMenu.Instance();		//Instantiate InGameMenu
    }
    
    
    /**
     * BOARD CONSTRUCTION FUNCTIONS
     */
	
	
	/**
	 * Sets the hexagon variables by 
	 * @param sideLength
	 */
    private void setBySide(int sideLength){
    	s = sideLength;							//s = the side length of each hex in pixels
    	t = (int) (s / 2);						//t = s * sin(30)
    	r = (int) (s * Math.cos(Math.PI / 6));	//r = s * cos(30)
    	h = 2 * r;								//
    }
    
    private Polygon createHex(int xPos, int yPos){

    	int x = xPos + PIXELBORDER;		//Current x equals the xPos + the border size
    	int y = yPos + PIXELBORDER;		//Current y equals the yPos + the border size
    	
    	int[] cx, cy;					//Arrays for x and y point coordinates
    	
    	cx = new int[] {x, x, x+r, x+r+r, x+r+r, x+r};			//Begins with the bottom left point and generates clockwise
    	cy = new int[] {y+t, y+s+t, y+s+t+t, y+s+t, y+t, y};	//Begins with the bottom left point and generates clockwise
    	
    	return new Polygon(cx, cy, 6);	//Returns the created hexagon
    }
    
    /**
     * This private class represents a robot to be used for testing, containing an image, coordinates, and a direction.
     * 
     * The robot also has functionality for moving forwards one space, turning left and turning right.
     * @author Brandon
     *
     */
    private class Robot{
    	
    	/** @public The robot's x position */
    	public int currentX;
    	
    	/** @public The robot's y position */
    	public int currentY;
    	
    	/** @public The robot's direction */
    	public int direction = 0;
    	
    	/** @public The robot's image */
    	public BufferedImage image;
    	
    	/** Constructs a robot at the given x and y position */
    	public Robot(int xPos, int yPos){
        	
    		currentX = xPos;
    		currentY = yPos;
    		
    		System.out.println("Current Robot Position: x = " + currentX + " y = " + currentY);
    	}
    	
    	/** Moves the robot one space forward based on the current direction */
    	public void Move() {
    		int x = currentX;
    		int y = currentY;
    		
    		switch(direction){
    		case 0:
    			y+= 1;
    			break;
    		case 1:
    			x += 1;
    			break;
    		case 2:
    			x += 1;
    			y -= 1;
    			break;
    		case 3:
    			y -= 1;
    			break;
    		case 4:
    			x -= 1;
    			break;
    		case 5:
    			x -= 1;
    			y += 1;
    			break;
    		default:
    			;
    		}
    		try{
    			if (gamePanel.walkable[x][y] == true){		//If the target coordinate is walkable
        			currentX = x;							//Move the robot
        			currentY = y;
        			repaint();								//Repaint the scene
    			}
    		} catch (NullPointerException e){				//If any exceptions are thrown, output "Invalid move"
    			System.out.println("Invalid move");
    		} catch (ArrayIndexOutOfBoundsException e){
    			System.out.println("Invalid move");
    		}
    	}
    		
    	/** Rotates the robot one increment counter-clockwise. */
    	public void TurnLeft(){
    		if(direction > 0){
    			direction -= 1;
    		} else {
    			direction = 5;
    		}
    	}
    	/** Rotates the robot one increment clockwise. */
    	public void TurnRight(){
    		if(direction < 5){
    			direction += 1;
    		} else {
    			direction = 0;
    		}
    	}
    }
   
    /**
     * Specialized JPanel that contains a list of walkable hexagons, a list of robots,
     * and a reference to the current robot. This can be repainted to update the board.
     * 
     * @author Brandon
     *
     */
    public class DrawingPanel extends JPanel{

	    private static final long serialVersionUID = 1L;
	    
	    /** @public 2D array of booleans representing walkable hexagons */
		public Boolean[][] walkable;
		
		/** @public Array of robots to be used in the game */
		public Robot[] robot;
		
		/** @public Integer reference to the current robot to be moved */
		public int currentRobotIndex;
		
		/** @public Constructs a game board with the given size */
		public DrawingPanel(int size){
			boardSize = size;
			walkable = new Boolean[size * 2][size * 2];
			
			for(int i = 0; i < size; i++)
				for(int j = 0; j < size; j++)
					walkable[i][j] = false;
			
			robot = new Robot[numberOfRobots];			//Create array of robots with the given number of robots
			
										
			for(int i = 0; i < numberOfRobots; i++){	//Initialize each robot in the array
				robot[i] = new Robot(boardSize - 1, boardSize - 1);
			}
			
			try{										//Try to load image files for robots into the game
				robot[0].image = ImageIO.read(new File("C:/Users/Brandon/Desktop/370Project/src/View/game_pieces/red_scout.png"));
				robot[1].image = ImageIO.read(new File("C:/Users/Brandon/Desktop/370Project/src/View/game_pieces/red_sniper.png"));
				robot[2].image = ImageIO.read(new File("C:/Users/Brandon/Desktop/370Project/src/View/game_pieces/red_tank.png"));
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
