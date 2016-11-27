package View;

import Model.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Controller.Controller;

public class InGameMenu extends JFrame{
	
	/**@public The panel where all images will be drawn*/
	public InGameMenuPanel gamePanel;
	
	private static final long serialVersionUID = 1L;

	/** @category Buttons */
	
	public JButton leftButton;
	public JButton rightButton;
	public JButton forfeitButton;
	public JButton exitButton;
	public JButton actionToggleButton;
	public JButton actionButton;
	public JButton endPlayButton;
	
	public boolean actionToggle = true;
	
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
        gamePanel.remakeBoard(b);
        add(gamePanel);
        gamePanel.setVisible(true);
        
        setSize(500, 500);
        
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
        
        setVisible(true);						//Set the game panel as visible
    }
	
	public void closeBoard(){
		dispose();
	}
	/**
	 * BUTTON LISTENERS
	 */
	private class ActionToggleButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    		
    		if(actionToggle == true){
    			actionToggle = false;
    			actionButton.setText("Shoot");
    		} else {
    			actionToggle = true;
    			actionButton.setText("Move");
    		}
    	}
    }
	
	private class LeftButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		Controller.getInstance().G_turnLeft();

    		//TELL CONTROLLER TO ROTATE CURRENT ROBOT LEFT OR CYCLE THROUGH TARGET TO LEFT
    	}
    }
	
	private class RightButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		Controller.getInstance().G_turnRight();
    		//TELL CONTROLLER TO ROTATE CURRENT ROBOT RIGHT OR CYCLE THROUGH TARGET TO RIGHT
    	}
    }
	
	private class ForfeitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
    		//TELL BOARD TO FORFEIT
    	}
    }
	
	private class ExitButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		Controller.getInstance().gameMenu.setVisible(true);
    		Controller.getInstance().inGameMenu.setVisible(false);
    	}
    }
	
	private class ActionButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if(actionToggle == true){				//MOVING
    			//TELL CONTROLLER TO MOVE CURRENT ROBOT
    		} else {													//ATTACKING
    			//TELL CONTROLLER TO ATTACK THE CURRENT TARGET HEX
    		}
    	}
    }
	
	private class EndPlayButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		//TELL CONTROLLER TO END THE CURRENT PLAY AND CHANGE THE CURRENT ROBOT
    	}
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
