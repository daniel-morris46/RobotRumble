package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;


import Model.*;
import View.*;

public class Controller implements ActionListener{

    Board gameBoard;
    
    int boardSize;
    
    int numberOfPlayers;
        
    PlayMenu playMenu;
    
    GameMenu gameMenu;
    
    
    public Controller() {
        gameBoard = new Board(5, 2);
        playMenu = new PlayMenu("Prepare board", this, this.gameBoard);
        playMenu.setVisible(false);
        gameMenu = new GameMenu(this);
        gameMenu.setTitle("Choose your board options");
        
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionToggle = e.getActionCommand();
        
        if(actionToggle == "play"){
            playMenu = new PlayMenu("Prepare board", this, this.gameBoard);
            playMenu.setVisible(true);
            gameMenu.setVisible(false);
        }
        
        if(actionToggle == "options"){
            System.exit(0);
        }
        
        if(actionToggle == "exit"){
            System.exit(0);
        }
        
        if(actionToggle == "begin"){
            InGameMenu.Instance().setVisible(true);
            InGameMenu.Instance().remakeBoard(this.gameBoard.getSize());
        }
        
        if(actionToggle == "back"){
            gameMenu.setVisible(true);
            playMenu.setVisible(false);
        }
        
        if(actionToggle == "numOfPlayersChange"){
            if(this.playMenu.numberOfPlayers.getSelectedItem() == "2"){
                numberOfPlayers = 2;
                playMenu.numPlayers = 2;
            }else if(this.playMenu.numberOfPlayers.getSelectedItem() == "3"){
                numberOfPlayers = 3;
                playMenu.numPlayers = 3;
            }else if(this.playMenu.numberOfPlayers.getSelectedItem() == "6"){
                numberOfPlayers = 6;
                playMenu.numPlayers = 6;
            }
            this.gameBoard = new Board(boardSize, this.numberOfPlayers);
            playMenu.updatePlayerNumbers();
        }
        
        if(actionToggle == "sizeFive"){
            boardSize = 5;
            playMenu.boardSize = 5;
            playMenu.numPlayers = 3;        

            String[] numPossiblePlayers = {"2", "3"};
            DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
            playMenu.numberOfPlayers.setModel(model);
            playMenu.updatePlayerNumbers();

            this.gameBoard = new Board(boardSize, this.numberOfPlayers);
            
        }
        
        if(actionToggle == "sizeSeven"){
            boardSize = 7;
            playMenu.boardSize = 7;
            playMenu.numPlayers = 3;

            String[] numPossiblePlayers = {"3", "6"};
            DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
            playMenu.numberOfPlayers.setModel(model);
            playMenu.updatePlayerNumbers();

            this.gameBoard = new Board(boardSize, this.numberOfPlayers);
            
        }
        
        
            
//          String[] numPossiblePlayers = {"2", "3"};
//          DefaultComboBoxModel model = new DefaultComboBoxModel(numPossiblePlayers);
//          numberOfPlayers.setModel(model);
            
//          updatePlayerNumbers();

    }
        
}


