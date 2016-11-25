package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import View.InGameMenu.Robot;

public class InGameMenu extends JFrame{

	public DrawingPanel gamePanel;
	
	public JButton leftButton;
	public JButton rightButton;
	public JButton forfeitButton;
	public JButton exitButton;
	public JButton actionToggleButton;
	public JButton actionButton;
	public JButton endPlayButton;
	
	public boolean actionToggle = true;
	
    public InGameMenu(int size) {
        super("Super Robot Rumble 370");
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        setSize(1000, 1000);
        setVisible(true);
        
        remakeBoard(size);
    }
    
    public static void main(String[] args){
    	InGameMenu menu = new InGameMenu(5);
    }
    
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

	private class ActionToggleButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){

    	}
    }
	
	private class LeftButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    	}
    }
	
	private class RightButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    	}
    }
	
	private class ForfeitButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){

    	}
    }
	
	private class ExitButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    	}
    }
	
	private class ActionButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    	}
    }
	
	private class EndPlayButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    	}
    }
	
	 public class DrawingPanel extends JPanel{
		 
		 /** @public 2D array of booleans representing walkable hexagons */
		public Boolean[][] walkable;
			
		/** @public Array of robots to be used in the game */
		public Robot[] robot;
		
		/** @public Size of the board in side length */
		public int boardSize;
		
		/** @public Integer reference to the current robot to be moved */
		public int currentRobotIndex;
			
		public DrawingPanel(int size){
			boardSize = size;
			walkable = new Boolean[size * 2][size * 2];
			
			for(int i = 0; i < size; i++)
				for(int j = 0; j < size; j++)
					walkable[i][j] = false;
			
			robot = new Robot[18];			//Create array of robots with the given number of robots
			
			
			for(int i = 0; i < 18; i++){	//Initialize each robot in the array
				robot[i] = new Robot(boardSize - 1, boardSize - 1);
		}
	 }
	 
	 
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
//	    			if (gamePanel.walkable[x][y] == true){		//If the target coordinate is walkable
	        			currentX = x;							//Move the robot
	        			currentY = y;
	        			repaint();								//Repaint the scene
//	    			}
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
}
