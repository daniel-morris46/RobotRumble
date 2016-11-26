package View;

import Model.*;

import javax.swing.*;

public class InGameMenu extends JFrame{
	
	/**@public The panel where all images will be drawn*/
	public InGameMenuPanel gamePanel;
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * Public constructor.
	 * Constructs the frame and board using the given hexes per edge.
	 * 
	 * @param size The size of the board
	 */
	public InGameMenu(Board b) {
        super("370 ROBOT RUMBLE");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(1400,  1000);
        gamePanel = new InGameMenuPanel(b.getSize(), b.Teams);
        add(gamePanel);
        gamePanel.setVisible(true);
        gamePanel.remakeBoard(b);
    }
	
	public void closeBoard(){
		dispose();
	}

    
    /**
     * This private class represents a robot to be used for testing, containing an image, coordinates, and a direction.
     * 
     * The robot also has functionality for moving forwards one space, turning left and turning right.
     * @author Brandon
     *
     */
//    
//    private class Robot{
//    	
//    	/** @public The robot's x position */
//    	public int currentX;
//    	
//    	/** @public The robot's y position */
//    	public int currentY;
//    	
//    	/** @public The robot's direction */
//    	public int direction = 0;
//    	
//    	/** @public The robot's image */
//    	public BufferedImage image;
//    	
//    	/** Constructs a robot at the given x and y position */
//    	public Robot(int xPos, int yPos){
//        	
//    		currentX = xPos;
//    		currentY = yPos;
//    		
//    		System.out.println("Current Robot Position: x = " + currentX + " y = " + currentY);
//    	}
//    	
//    	/** Moves the robot one space forward based on the current direction */
//    	public void Move() {
//    		int x = currentX;
//    		int y = currentY;
//    		
//    		switch(direction){
//    		case 0:
//    			y+= 1;
//    			break;
//    		case 1:
//    			x += 1;
//    			break;
//    		case 2:
//    			x += 1;
//    			y -= 1;
//    			break;
//    		case 3:
//    			y -= 1;
//    			break;
//    		case 4:
//    			x -= 1;
//    			break;
//    		case 5:
//    			x -= 1;
//    			y += 1;
//    			break;
//    		default:
//    			;
//    		}
//    		try{
//    			if (gamePanel.walkable[x][y] == true){		//If the target coordinate is walkable
//        			currentX = x;							//Move the robot
//        			currentY = y;
//        			repaint();								//Repaint the scene
//    			}
//    		} catch (NullPointerException e){				//If any exceptions are thrown, output "Invalid move"
//    			System.out.println("Invalid move");
//    		} catch (ArrayIndexOutOfBoundsException e){
//    			System.out.println("Invalid move");
//    		}
//    	}
//    		
//    	/** Rotates the robot one increment counter-clockwise. */
//    	public void TurnLeft(){
//    		if(direction > 0){
//    			direction -= 1;
//    		} else {
//    			direction = 5;
//    		}
//    	}
//    	/** Rotates the robot one increment clockwise. */
//    	public void TurnRight(){
//    		if(direction < 5){
//    			direction += 1;
//    		} else {
//    			direction = 0;
//    		}
//    	}
//    }

	public static void main(String[] args){
		JFrame testFrame = new JFrame("IN-GAME-MENU-PANEL-TEST");
    	testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	testFrame.setVisible(true);
    	testFrame.setSize(1200, 1000);
    	testFrame.setResizable(false);
    	
    	Board swag = new Board(7, 6);
    	InGameMenuPanel testPanel = new InGameMenuPanel(swag.getSize(), swag.Teams);
    	testPanel.setSize(1000, 1000);
    	testFrame.add(testPanel);
    	testPanel.setVisible(true);
    	testPanel.reDraw(swag.getHexBoard(), swag.getCurrentRobot(), swag.getHexBoard()[0][0]);
    }
    
    
    
}
