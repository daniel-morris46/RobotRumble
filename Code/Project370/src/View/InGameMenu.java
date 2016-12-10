package View;

import Model.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
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
	
	/**@public The panel for hiding the game while switching players */
	public JPanel standByPanel;
	
	/**@public The panel for the player wins screen */
    public JPanel winPanel;
    
    /**@public The label for the player wins screen */
    public JLabel winnerMessage;
	
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
        this.setVisible(true);
        this.setBounds(0, 0, 1250, 1000);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
       
        
        JPanel buttonPanel = new JPanel();
        
        actionToggleButton = new JButton("*");	//Creating action toggle button and adding it to panel
        actionToggleButton.addActionListener(new ActionToggleButtonListener());
        actionToggleButton.setSize(20,20);
        actionToggleButton.setVisible(true);
        
        leftButton = new JButton("<");			//Creating left button and adding it to panel
        leftButton.addActionListener(new LeftButtonListener());
        leftButton.setSize(20,20);
        leftButton.setVisible(true);
        
        rightButton = new JButton(">");			//Creating right button and adding it to panel
        rightButton.addActionListener(new RightButtonListener());
        rightButton.setSize(20,20);
        rightButton.setVisible(true);

        actionButton = new JButton("Move");		//Creating action button and adding it to panel
        actionButton.addActionListener(new ActionButtonListener());
        actionButton.setSize(20,20);
        actionButton.setVisible(true);
        
        endPlayButton = new JButton("End Play");//Creating end play button and adding it to panel
        endPlayButton.addActionListener(new EndPlayButtonListener());
        endPlayButton.setSize(20,20);
        endPlayButton.setVisible(true);
        
        forfeitButton = new JButton("Forfeit");	//Creating forfeit button and adding it to panel
        forfeitButton.addActionListener(new ForfeitButtonListener());
        forfeitButton.setSize(20,20);
        forfeitButton.setVisible(true);
        
        exitButton = new JButton("Exit");		//Creating exit button and adding it to panel
        exitButton.addActionListener(new ExitButtonListener());
        exitButton.setSize(20,20);
        exitButton.setVisible(true);
        
        standByPanel = new JPanel();
        JButton nextButton = new JButton("Next Player");
        nextButton.addActionListener(new NextPlayerButtonListener());
        standByPanel.add(nextButton);

        winPanel = new JPanel();
        winnerMessage = new JLabel("");
        winnerMessage.setFont(winnerMessage.getFont().deriveFont(72.0f));
        JButton winnerExitButton = new JButton("Exit");
        winnerExitButton.setPreferredSize(new Dimension(450, 200));
        winnerExitButton.setFont(winnerExitButton.getFont().deriveFont(50.0f));
        winnerExitButton.addActionListener(new WinnerExitButtonListener());
        winPanel.add(winnerMessage);
        winPanel.add(winnerExitButton);
        
        gamePanel = new InGameMenuPanel(b.getSize(), b.Teams);
        gamePanel.reDraw(b);
        buttonPanel.add(actionToggleButton, BorderLayout.NORTH);
        buttonPanel.add(leftButton, BorderLayout.NORTH);
        buttonPanel.add(rightButton, BorderLayout.NORTH);
        buttonPanel.add(actionButton, BorderLayout.NORTH);
        buttonPanel.add(endPlayButton, BorderLayout.NORTH);
        buttonPanel.add(forfeitButton, BorderLayout.NORTH);
        buttonPanel.add(exitButton, BorderLayout.NORTH);
        
        
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);
        gamePanel.setBackground(Color.CYAN);
        
        add(standByPanel);
        add(winPanel);
        add(gamePanel);
        
        standByPanel.setVisible(false);
        winPanel.setVisible(false);
        gamePanel.setVisible(true);
        
        
        setVisible(true);						//Set the game panel as visible
    }
	
	/** Tells the controller to either shoot or move */
	private class ActionToggleButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if(actionToggle == true){
    			actionToggle = false;
    			actionButton.setText("Shoot");
    			Controller.getInstance().G_ShootMode();
    		} else {
    			actionToggle = true;
    			actionButton.setText("Move");
    			Controller.getInstance().G_MoveMode();
    		}
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
		    
    		for(int i = 0; i < 3; i++){
    		    Controller.getInstance().gameBoard.getTeams()[Controller.getInstance().gameBoard.getCurrentTeam()].getTeamOfRobot()[i].setHealth(0);
    		}
    		
    		endPlayButton.doClick();
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
    		if(actionToggle == true){					//Tell controller to move
    			Controller.getInstance().G_Move();
    		} else {									//Otherwise, tell controller to attack
    			Controller.getInstance().G_Attack();
    		}
    		
    		gamePanel.repaint();
    	}
    }
	
	/** Tells the controller to end the current play and hide board for player change*/
	private class EndPlayButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    		Board board = Controller.getInstance().gameBoard;
    		
    		boolean prev = board.getTeams()[board.getCurrentTeam()].isHuman();
    		actionToggle = true;
    		Controller.getInstance().G_endPlay();
    		actionButton.setText("Move");
    		boolean next = board.getTeams()[board.getCurrentTeam()].isHuman();
    		
    		RobotTeam aliveTeam = Controller.getInstance().gameBoard.getTeams()[0];
    		int deadCounter = 0;
            for(int i = 0; i < Controller.getInstance().gameBoard.getTeamAmount(); i++){
                if(!Controller.getInstance().gameBoard.getTeams()[i].isAlive()){
                    deadCounter += 1;
                }else{
                    aliveTeam = Controller.getInstance().gameBoard.getTeams()[i];
                }
            }
            
            if(deadCounter == Controller.getInstance().gameBoard.getTeamAmount() - 1){
                gamePanel.setVisible(false);
                if(aliveTeam.getColour() == Color.red){
                    winnerMessage.setText("Red Team Wins!");
                }else if(aliveTeam.getColour() == Color.orange){
                    winnerMessage.setText("Orange Team Wins!");
                }else if(aliveTeam.getColour() == Color.yellow){
                    winnerMessage.setText("Yellow Team Wins!");
                }else if(aliveTeam.getColour() == Color.green){
                    winnerMessage.setText("Green Team Wins!");
                }else if(aliveTeam.getColour() == Color.blue){
                    winnerMessage.setText("Blue Team Wins!");
                }else if(aliveTeam.getColour() == Color.magenta){
                    winnerMessage.setText("Purple Team Wins!");
                }
                
                winPanel.setVisible(true);
                gamePanel.reDraw(Controller.getInstance().gameBoard);
                revalidate();
            }else if(prev && next){
				gamePanel.setVisible(false);
				standByPanel.setVisible(true);
				gamePanel.reDraw(Controller.getInstance().gameBoard);
				revalidate();
    		}
    	}
	}
    
	/** Tells the controller to show the board for the next player */
	private class NextPlayerButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
			
    		standByPanel.setVisible(false);
    		gamePanel.setVisible(true);
    		gamePanel.reDraw(Controller.getInstance().gameBoard);
			revalidate();
			
    	}
	}
	
	/** Tells the controller to show the board for the next player */
    private class WinnerExitButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            winPanel.setVisible(false);
            Controller.getInstance().gameMenu.setVisible(true);
            Controller.getInstance().inGameMenu.setVisible(false);
        }
    }
	
	
	public static void main(String[] args){
		Controller.getInstance();
	}
    
    
}
