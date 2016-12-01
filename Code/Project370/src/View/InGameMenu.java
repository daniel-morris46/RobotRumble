package View;

import Model.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Controller.Controller;

/**
 * @category View
 * 
 * This class handles all user interaction during
 * actual game play. It displays visuals using an
 * InGameMenuPanel and has buttons for various
 * robot actions.
 *
 */
public class InGameMenu extends JFrame{
	
	/**@public The panel where all images will be drawn*/
	public InGameMenuPanel gamePanel;
	
	/**@private JButton for rotating/cycling left */
	private JButton leftButton;
	
	/**@private JButton for rotating/cycling right */
	private JButton rightButton;
	
	/**@private JButton for forfeiting the game */
	private JButton forfeitButton;
	
	/**@private JButton for exiting the game */
	private JButton exitButton;
	
	/**@private JButton for toggling between moving and shooting */
	private JButton actionToggleButton;
	
	/**@private JButton for moving or shooting */
	private JButton actionButton;
	
	/**@private JButton for ending the current play */
	private JButton endPlayButton;
	
	/**@public True = moving, false = shooting */
	public boolean actionToggle = true;
	
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
        
        gamePanel = new InGameMenuPanel(b.getSize(), b.Teams);
        gamePanel.reDraw(b);
        add(gamePanel);
        gamePanel.setVisible(true);
        
        setSize(800, 800);
        
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
	
	/** Tells the controller to either shoot or move */
	private class ActionToggleButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if(actionToggle == true){
    			actionToggle = false;
    			actionButton.setText("Shoot");
    			Controller.getInstance().G_ToggleShooting();
    		} else {
    			actionToggle = true;
    			actionButton.setText("Move");
                Controller.getInstance().G_ToggleMoving();

    		}
    		gamePanel.repaint();
    	}
    }
	
	/** Tells the controller to either rotate or cycle through targets left */
	private class LeftButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		Controller.getInstance().G_turnRight();
    	}
    }
	
	/** Tells the controller to either rotate or cycle through targets right */
	private class RightButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		Controller.getInstance().G_turnLeft();
    	}
    }
	
	/** Tells the controller to forfeit the game for the current player */
	private class ForfeitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
    		//TELL BOARD TO FORFEIT
    	}
    }
	
	/** Tells the controller to exit the game completely */
	private class ExitButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		Controller.getInstance().gameMenu.setVisible(true);
    		Controller.getInstance().inGameMenu.setVisible(false);
    	}
    }
	
	/** Tells the controller to attack the current target hex */
	private class ActionButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if(actionToggle == true){				//MOVING
    			Controller.getInstance().G_Move();
    		} else {													//ATTACKING
    			//TELL CONTROLLER TO ATTACK THE CURRENT TARGET HEX
    		}
    		
    		gamePanel.repaint();
    	}
    }
	
	/** Tells the controller to end the current play */
	private class EndPlayButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    	    actionToggle = true;
            actionButton.setText("Move");
    		Controller.getInstance().G_endPlay();
    		Controller.getInstance().G_ToggleMoving();
            gamePanel.repaint();
    	}
	}
    
	public static void main(String[] args){
		JFrame testFrame = new JFrame("IN-GAME-MENU-PANEL-TEST");
    	testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	testFrame.setVisible(true);
    	testFrame.setSize(1200, 1000);
    	testFrame.setResizable(false);
    	
    	Board board = new Board(7, 6);
    	InGameMenuPanel testPanel = new InGameMenuPanel(board.getSize(), board.Teams);
    	testPanel.setSize(1000, 1000);
    	testFrame.add(testPanel);
    	testPanel.setVisible(true);
    	testPanel.reDraw(board);
	}
	
	
	
	
	
	
//    /**
//     * This private class represents a robot to be used for testing, containing an image, coordinates, and a direction.
//     * 
//     * The robot also has functionality for moving forwards one space, turning left and turning right.
//     * @author Brandon
//     *
//     */
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
//        		currentX = x;							//Move the robot
//        		currentY = y;
//        		repaint();								//Repaint the scene
//    			
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
    
    
}
