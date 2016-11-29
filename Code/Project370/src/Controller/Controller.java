package Controller;

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

	private static Controller instance = null;
	
    public Board gameBoard;
    
    int boardSize;
    
    int numberOfPlayers;
        
    public PlayMenu playMenu;
    
    public GameMenu gameMenu;
    
    public InGameMenu inGameMenu;
    
    protected Controller() {
        gameBoard = new Board(5, 2);
        playMenu = new PlayMenu("Prepare board");
        playMenu.setVisible(false);
        gameMenu = new GameMenu();
        gameMenu.setTitle("Choose your board options");
        inGameMenu = new InGameMenu(gameBoard);
        inGameMenu.setVisible(false);
    }
    
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
        int currentRotation = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getAbsDirection();
        if(currentRotation == 0){
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(5);
        }else{
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(currentRotation - 1);
        }
        
        inGameMenu.gamePanel.reDraw(gameBoard);
    }
    
    /** Rotates the current robot right */
    public void G_turnRight(){
    	
        int currentRotation = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getAbsDirection();
        if(currentRotation == 5){
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(0);
        }else{
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].setAbsDirection(currentRotation + 1);
        }
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
    
    public static void main(String[] args) {
        Controller game = new Controller();
        game.boardSize = 5;
        //game.numberOfPlayers = 2;
        game.gameBoard = new Board(5, 2);

    }
        
}

