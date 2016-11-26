package Controller;

import Model.*;
import View.*;

public class Controller {

	private static Controller instance = null;
	
    public Board gameBoard;
    
    int boardSize;
    
    int numberOfPlayers;
        
    public PlayMenu playMenu;
    
    public GameMenu gameMenu;
    
    public InGameMenu inGameMenu;
    
    private Controller() {
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
    
    public void M_quitGame(){
        System.exit(0);
    }
    
    public void S_pauseGame(){
        gameBoard.getSpectator().pausePlay();
    }
    
    public void S_fastForwardGame(){
        gameBoard.getSpectator().changeTime();
    }

    public void S_fogOfWarSwitch(){
        gameBoard.getSpectator().changeFogOfWar();
    }
    
    public void G_turnLeft(){
        int currentRotation = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getAbsDirection();
        if(currentRotation == 0){
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentTeam()].setAbsDirection(5);
        }else{
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentTeam()].setAbsDirection(currentRotation - 1);
        }
        
        inGameMenu.gamePanel.reDraw(gameBoard.hexBoard, gameBoard.getCurrentRobot(), gameBoard.getCurrentHex());
    }
    
    public void G_turnRight(){
        int currentRotation = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()].getAbsDirection();
        if(currentRotation == 5){
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentTeam()].setAbsDirection(0);
        }else{
            gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentTeam()].setAbsDirection(currentRotation + 1);
        }
    }
    
    public void G_Move(){
        Robot curRobot = gameBoard.Teams[gameBoard.getCurrentTeam()].getTeamOfRobot()[gameBoard.getCurrentRobot()];
        int curX = curRobot.getPosition().getPositionX();
        int curY = curRobot.getPosition().getPositionY();
        
        
        if(curRobot.getAbsDirection() == 0){
            // if the robot is not on the right side of the hex
            if(curX < gameBoard.getSize() - 1 && curX + curY < gameBoard.getSize() - 1){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX + 1, curY).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX + 1, curY));
                System.out.println("Robot " + gameBoard.getCurrentRobot() + " from team " + gameBoard.getCurrentTeam() + "moved from (" + curX + "," + curY + ") to (" + (curX + 1) + "," + curY + ")");
                return;
            }
        }else if(curRobot.getAbsDirection() == 1){
            // if the robot is not on the bottom right side of the hex
            if(curY > -(gameBoard.getSize() - 1) && curX < gameBoard.getSize() - 1){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX + 1, curY - 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX + 1, curY - 1));
                System.out.println("Robot " + gameBoard.getCurrentRobot() + " from team " + gameBoard.getCurrentTeam() + "moved from (" + curX + "," + curY + ") to (" + (curX + 1) + "," + (curY - 1) + ")");
                return;
            }
        }else if(curRobot.getAbsDirection() == 2){
            // if the robot is not on the bottom left side of the hex
            if(curX + curY > -(gameBoard.getSize() - 1) && curY > -(gameBoard.getSize() - 1) ){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX, curY - 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX, curY - 1));
                System.out.println("Robot " + gameBoard.getCurrentRobot() + " from team " + gameBoard.getCurrentTeam() + "moved from (" + curX + "," + curY + ") to (" + curX + "," + (curY - 1) + ")");
                return;
            }
        }else if(curRobot.getAbsDirection() == 3){
            // if the robot is not on the left side of the hex
            if(curX > -(gameBoard.getSize() - 1) && curX + curY > -(gameBoard.getSize() - 1)){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX - 1, curY).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX - 1, curY));
                System.out.println("Robot " + gameBoard.getCurrentRobot() + " from team " + gameBoard.getCurrentTeam() + "moved from (" + curX + "," + curY + ") to (" + (curX - 1) + "," + curY + ")");
                return;
            }
        }else if(curRobot.getAbsDirection() == 4){
            // if the robot is not on the top left side of the hex
            if(curY < gameBoard.getSize() - 1 && curX > -(gameBoard.getSize() - 1)){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX - 1, curY - 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX - 1, curY - 1));
                System.out.println("Robot " + gameBoard.getCurrentRobot() + " from team " + gameBoard.getCurrentTeam() + "moved from (" + curX + "," + curY + ") to (" + (curX - 1) + "," + (curY + 1) + ")");
                return;
            }
        }else if(curRobot.getAbsDirection() == 5){
            // if the robot is not on the top right side of the hex
            if(curX + curY < gameBoard.getSize() - 1 && curY < gameBoard.getSize() - 1){
                curRobot.getPosition().removeOcc(curRobot);
                gameBoard.getHex(curX, curY + 1).addOcc(curRobot);
                curRobot.setPosition( gameBoard.getHex(curX, curY + 1));
                System.out.println("Robot " + gameBoard.getCurrentRobot() + " from team " + gameBoard.getCurrentTeam() + "moved from (" + curX + "," + curY + ") to (" + curX + "," + (curY + 1) + ")");
                return;
            }
        }
        //if the robot did not move
        System.out.println("Robot " + gameBoard.getCurrentRobot() + " from team " + gameBoard.getCurrentTeam() + "tried to move off of the board.");
        
    }
    
    public static void main(String[] args) {
        Controller game = new Controller();
        game.boardSize = 5;
        game.numberOfPlayers = 2;
        game.gameBoard = new Board(5, 2);

    }
        
}

