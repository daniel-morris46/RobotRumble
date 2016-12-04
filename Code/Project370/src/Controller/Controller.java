package Controller;

import java.awt.Color;

import Model.*;
import View.*;

/**
 * @category Controller
 * 
 * Handles all game logistics and operations for manipulating
 * the game. This contains all methods for telling robots
 * what to do, as well as spectator functions
 */
public class Controller {

	/** Singleton reference to the controller instance */
	private static Controller instance = null;
	
	/** The current game board to play on */
    public Board gameBoard;
    
    /** The size of the board */
    int boardSize;
    
    /** The number of players in the game */
    int numberOfPlayers;
        
    /** The play menu for playing and selecting options */
    public PlayMenu playMenu;
    
    /** The game menu for selecting board size and player amount */
    public GameMenu gameMenu;
    
    /** The in-game menu for updating the board's representation */
    public InGameMenu inGameMenu;
    
    /** Creates a controller and initializes the menus */
    protected Controller() {
        gameBoard = new Board(5, 2);
        playMenu = new PlayMenu("Prepare board");
        playMenu.setVisible(false);
        gameMenu = new GameMenu();
        gameMenu.setTitle("Choose your board options");
        inGameMenu = new InGameMenu(gameBoard);
        inGameMenu.setVisible(false);
    }
    
    /** Ensures the singleton property of this class and returns a ocontroller reference */
    public static Controller getInstance(){
    	if(instance == null){
    		instance = new Controller();
    	}
    	
    	return instance;
    }
    
    /** Quits the game from the menu */
    public void M_quitGame(){
        System.exit(0);
    }
    
    /** Pauses the game for spectators */
    public void S_pauseGame(){
        gameBoard.getSpectator().pausePlay();
    }
    
    /** Fast forwards the game for spectators */
    public void S_fastForwardGame(){
        gameBoard.getSpectator().changeTime();
    }

    /** Toggles the fog of war option for a spectator */
    public void S_fogOfWarSwitch(){
        gameBoard.getSpectator().changeFogOfWar();
    }
    
    /** Rotates the current robot left */
    public void G_turnLeft(){
    	Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
        int curX = curRobot.getPosition().getPositionX();
        int curY = curRobot.getPosition().getPositionY();
        
        if (gameBoard.isGameMode()) {
	        int currentRotation = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getAbsDirection();
	        if(currentRotation == 0){
	            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(5);
	        }else{
	            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(currentRotation - 1);
	        }
	        System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
	        if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
	            System.out.print("Scout");
	        }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
	            System.out.print("Sniper");
	        }else{
	            System.out.print("Tank");
	        }
	        System.out.println(" turned left.");
	        
	        gameBoard.updateMovementColours(curRobot, curX, curY, gameBoard, currentRotation);
        } else if (!gameBoard.isGameMode()) {
        	gameBoard.nextRobot();
        	gameBoard.updateTargetColours(gameBoard);
        }
        inGameMenu.gamePanel.reDraw(gameBoard);
    }
    
    /** Rotates the current robot right */
    public void G_turnRight(){
    	Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
        int curX = curRobot.getPosition().getPositionX();
        int curY = curRobot.getPosition().getPositionY();
        
        if (gameBoard.isGameMode()) {
	        int currentRotation = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getAbsDirection();
	        if(currentRotation == 5){
	            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(0);
	        }else{
	            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(currentRotation + 1);
	        }
	        System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
	        if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
	            System.out.print("Scout");
	        }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
	            System.out.print("Sniper");
	        }else{
	            System.out.print("Tank");
	        }
	        System.out.println(" turned right.");
	        
	        gameBoard.updateMovementColours(curRobot, curX, curY, gameBoard, currentRotation);
        } else if (!gameBoard.isGameMode()) {
        	gameBoard.prevRobot();
        	gameBoard.updateTargetColours(gameBoard);
        }
        inGameMenu.gamePanel.reDraw(gameBoard);
    }
    
    /** Ends the current robot's play */
    public void G_endPlay(){
        
        gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setMovementCur(0);
        
    	//Each team plays their first robot, then their second, then third
    	if(gameBoard.getCurrentTeam() >= gameBoard.getTeamAmount() - 1){
    		gameBoard.setCurrentTeam(0);
    		
    		if(gameBoard.getCurrentRobot() >= 2){
    			gameBoard.setCurrentRobot(0);
    		} else {
    			gameBoard.setCurrentRobot(gameBoard.getCurrentRobot() + 1);
    		}
    	} else {
    		gameBoard.setCurrentTeam(gameBoard.getCurrentTeam() + 1);
    	}
    	
    	Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
        int curX = curRobot.getPosition().getPositionX();
        int curY = curRobot.getPosition().getPositionY();
    	
        gameBoard.updateHexColours();
        gameBoard.clearTargetlist();
        gameBoard.updateMovementColours(curRobot, curX, curY, gameBoard, curRobot.getAbsDirection());
    	
    }
    
    /** Moves the current robot in it's current direction */
    public void G_Move(){
        Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
        int curX = curRobot.getPosition().getPositionX();
        int curY = curRobot.getPosition().getPositionY();
        
        
        if (curRobot.getMovementCur() == curRobot.getMovementMax()){
            System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
            if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
                System.out.print("Scout");
            }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
                System.out.print("Sniper");
            }else{
                System.out.print("Tank");
            }
            System.out.println(" tried to move when they had no moves left."); 
            return;
        }
        
        if(curRobot.getAbsDirection() == 0){
            // if the robot is not on the right side of the hex
            if(curX < gameBoard.getSize() - 1 && curX + curY < gameBoard.getSize() - 1){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX + 1, curY).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX + 1, curY));
                System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
                if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
                    System.out.print("Scout");
                }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
                    System.out.print("Sniper");
                }else{
                    System.out.print("Tank");
                }
                System.out.println(" moved from (" + curX + "," + curY + ") to (" + (curX + 1) + "," + curY + ")");
                curRobot.setMovementCur(curRobot.getMovementCur() + 1);
                gameBoard.updateHexColours();
                gameBoard.updateMovementColours(curRobot, curRobot.getPosition().getPositionX(), curRobot.getPosition().getPositionY(), gameBoard, curRobot.getAbsDirection());
                return;
            }
        }else if(curRobot.getAbsDirection() == 1){
            // if the robot is not on the bottom right side of the hex
            if(curY > -(gameBoard.getSize() - 1) && curX < gameBoard.getSize() - 1){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX + 1, curY - 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX + 1, curY - 1));
                System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
                if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
                    System.out.print("Scout");
                }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
                    System.out.print("Sniper");
                }else{
                    System.out.print("Tank");
                }
                System.out.println(" moved from (" + curX + "," + curY + ") to (" + (curX + 1) + "," + (curY - 1) + ")");   
                curRobot.setMovementCur(curRobot.getMovementCur() + 1);
                gameBoard.updateHexColours();
                gameBoard.updateMovementColours(curRobot, curRobot.getPosition().getPositionX(), curRobot.getPosition().getPositionY(), gameBoard, curRobot.getAbsDirection());
                return;
            }
        }else if(curRobot.getAbsDirection() == 2){
            // if the robot is not on the bottom left side of the hex
            if(curX + curY > -(gameBoard.getSize() - 1) && curY > -(gameBoard.getSize() - 1) ){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX, curY - 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX, curY - 1));
                System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
                if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
                    System.out.print("Scout");
                }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
                    System.out.print("Sniper");
                }else{
                    System.out.print("Tank");
                }
                System.out.println(" moved from (" + curX + "," + curY + ") to (" + curX + "," + (curY - 1) + ")");         
                curRobot.setMovementCur(curRobot.getMovementCur() + 1);
                gameBoard.updateHexColours();
                gameBoard.updateMovementColours(curRobot, curRobot.getPosition().getPositionX(), curRobot.getPosition().getPositionY(), gameBoard, curRobot.getAbsDirection());
                return;
            }
        }else if(curRobot.getAbsDirection() == 3){
            // if the robot is not on the left side of the hex
            if(curX > -(gameBoard.getSize() - 1) && curX + curY > -(gameBoard.getSize() - 1)){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX - 1, curY).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX - 1, curY));
                System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
                if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
                    System.out.print("Scout");
                }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
                    System.out.print("Sniper");
                }else{
                    System.out.print("Tank");
                }
                System.out.println(" moved from (" + curX + "," + curY + ") to (" + (curX - 1) + "," + curY + ")");          
                curRobot.setMovementCur(curRobot.getMovementCur() + 1);
                gameBoard.updateHexColours();
                gameBoard.updateMovementColours(curRobot, curRobot.getPosition().getPositionX(), curRobot.getPosition().getPositionY(), gameBoard, curRobot.getAbsDirection());
                return;
            }
        }else if(curRobot.getAbsDirection() == 4){
            // if the robot is not on the top left side of the hex
            if(curY < gameBoard.getSize() - 1 && curX > -(gameBoard.getSize() - 1)){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX - 1, curY + 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX - 1, curY + 1));
                System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
                if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
                    System.out.print("Scout");
                }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
                    System.out.print("Sniper");
                }else{
                    System.out.print("Tank");
                }
                System.out.println(" moved from (" + curX + "," + curY + ") to (" + (curX - 1) + "," + (curY + 1) + ")");          
                curRobot.setMovementCur(curRobot.getMovementCur() + 1);
                gameBoard.updateHexColours();
                gameBoard.updateMovementColours(curRobot, curRobot.getPosition().getPositionX(), curRobot.getPosition().getPositionY(), gameBoard, curRobot.getAbsDirection());
                return;
            }
        }else if(curRobot.getAbsDirection() == 5){
            // if the robot is not on the top right side of the hex
            if(curX + curY < gameBoard.getSize() - 1 && curY < gameBoard.getSize() - 1){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX, curY + 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX, curY + 1));
                System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
                if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
                    System.out.print("Scout");
                }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
                    System.out.print("Sniper");
                }else{
                    System.out.print("Tank");
                }
                System.out.println(" moved from (" + curX + "," + curY + ") to (" + curX + "," + (curY + 1) + ")");           
                curRobot.setMovementCur(curRobot.getMovementCur() + 1);
                gameBoard.updateHexColours();
                gameBoard.updateMovementColours(curRobot, curRobot.getPosition().getPositionX(), curRobot.getPosition().getPositionY(), gameBoard, curRobot.getAbsDirection());
                return;
            }
        }        
        //if the robot did not move
        System.out.print(gameBoard.Teams[gameBoard.getCurrentTeam()].getColour() + " ");
        if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 1){
            System.out.print("Scout");
        }else if(gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getType() == 2){
            System.out.print("Sniper");
        }else{
            System.out.print("Tank");
        }
        System.out.println(" tried to move off of the board.");         
    }
    
    public void G_ShootMode(){
        Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
        
        gameBoard.setGameMode(false);
        gameBoard.clearTargetlist();
        gameBoard.search(curRobot.getPosition(), curRobot.getRange());
        gameBoard.firstRobot();
        gameBoard.updateTargetColours(gameBoard);
        inGameMenu.gamePanel.reDraw(gameBoard);
    }
    
    public void G_MoveMode(){
    	Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
        int curX = curRobot.getPosition().getPositionX();
        int curY = curRobot.getPosition().getPositionY();
        
        gameBoard.setGameMode(true);
        gameBoard.updateMovementColours(curRobot, curX, curY, gameBoard, curRobot.getAbsDirection());
        inGameMenu.gamePanel.reDraw(gameBoard);
    }
    
    public void G_Attack(){
    	Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
    	gameBoard.damageHex(gameBoard.getCurrentHex(), curRobot.getDamage());
    }
    
    public static void main(String[] args) {
        Controller game = Controller.getInstance();
        game.boardSize = 5;
        //game.numberOfPlayers = 2;
        game.gameBoard = new Board(5, 2);
    }
        
}

