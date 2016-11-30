package Model;

import java.awt.Color;
import java.util.*;

public class Board {
    
    /** The 2D array of hex objects representing the board.*/
    public Hex hexBoard[][];

    /** The array of 2, 3, or 6 teams playing the game.*/
    public RobotTeam[] Teams;
    
    /** The index of the current team.*/
    int currentTeam;
    
    /**The index of the current robot.*/
    int currentRobot;
    
    /**The side length of the board.*/
    int size;
    
    /**The number of teams playing the game.*/
    int teamAmount;
    
    /**The spectator of the game.*/
    Spectator spect;
    
    /**The array of colours assigned to each robot team.*/
    //Color[] colourList;
    
    /**The index of the current target in the target list.*/
    int currentTarget;
    
    /**The list of possible targets for the current robot.*/
    LinkedList<Hex> targetList;
    
    /**The current selected hex on the board.*/
    Hex currentHex;

    /** @public Constructs a game board of a given size and number of players. */
    public Board(int boardSize, int numberOfTeams){
        this(boardSize, numberOfTeams, new Color[] {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta});
    }
    
    /** @public Constructs a game board given a size and number of players. */
    public Board(int boardSize, int numberOfTeams, Color inputColourList[]) {
        //colourList = new String[] {"Red", "Orange", "Yellow", "Green", "Blue", "Purple"};
        currentTeam = 0;
        currentRobot = 0;
        size = boardSize;
        teamAmount = numberOfTeams;
        spect = new Spectator();
        Teams = new RobotTeam[numberOfTeams];
        targetList = new LinkedList<Hex>();
        for(int i = 0; i < numberOfTeams; i++){
            Teams[i] = new RobotTeam(true, inputColourList[i], i);
        }
        
        
        hexBoard = new Hex[boardSize * 2 - 1][boardSize * 2 - 1];
        
        //initializing the board
        for (int x = -(boardSize-1); x <= boardSize-1; x++) {
            for (int y = -(boardSize-1); y <= boardSize-1; y++) {
                if ((x + y <= boardSize-1) && (x + y >= (-boardSize+1))) {
                    hexBoard[x + boardSize - 1][y + boardSize - 1] = new Hex(x, y);
                }
            }
        }
        
        // for setting the starting locations of the robots
        for(int i = 0; i < 3; i++){
            if(numberOfTeams == 2){
                Teams[0].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), 0));
                Teams[1].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, 0));
            }else if(numberOfTeams == 3){
                Teams[0].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), 0));
                Teams[1].teamOfRobot[i].setPosition(this.getHex(0, boardSize -1));
                Teams[2].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, -(boardSize - 1)));
            }else if(numberOfTeams == 6){
                Teams[0].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), 0));
                Teams[1].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), boardSize -1));
                Teams[2].teamOfRobot[i].setPosition(this.getHex(0, boardSize - 1));
                Teams[3].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, 0));
                Teams[4].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, -(boardSize -1)));
                Teams[5].teamOfRobot[i].setPosition(this.getHex(0, -(boardSize - 1)));
            }
        }
        
     // for setting the starting rotations of the robots
        for(int i = 0; i < 3; i++){
            if(numberOfTeams == 2){
                Teams[0].teamOfRobot[i].absDirection = 0;
                Teams[1].teamOfRobot[i].absDirection = 3;
            }else if(numberOfTeams == 3){
                Teams[0].teamOfRobot[i].absDirection = 0;
                Teams[1].teamOfRobot[i].absDirection = 2;
                Teams[2].teamOfRobot[i].absDirection = 4;
            }else if(numberOfTeams == 6){
                Teams[0].teamOfRobot[i].absDirection = 0;
                Teams[1].teamOfRobot[i].absDirection = 1;
                Teams[2].teamOfRobot[i].absDirection = 2;
                Teams[3].teamOfRobot[i].absDirection = 3;
                Teams[4].teamOfRobot[i].absDirection = 4;
                Teams[5].teamOfRobot[i].absDirection = 5;     
            }
        }
        currentHex = getHex(0, 0);
    }
    
    public RobotTeam[] getTeams() {
        return Teams;
    }

    public void setTeams(RobotTeam[] teams) {
        Teams = teams;
    }

    public int getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(int currentTeam) {
        this.currentTeam = currentTeam;
    }

    public int getCurrentRobot() {
        return currentRobot;
    }

    public void setCurrentRobot(int currentRobot) {
        this.currentRobot = currentRobot;
    }

    public int getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(int currentTarget) {
        this.currentTarget = currentTarget;
    }

    public int getTeamAmount() {
        return teamAmount;
    }

    public void setTeamAmount(int teamAmount) {
        this.teamAmount = teamAmount;
    }

    public Hex[][] getHexBoard() {
        return hexBoard;
    }

    public int getSize() {
        return size;
    }

    public Spectator getSpect() {
        return spect;
    }

//    public Color[] getColourList() {
//        return colourList;
//    }

    public void setTargetList(LinkedList<Hex> targetList) {
        this.targetList = targetList;
    }

    public void setCurrentHex(Hex currentHex) {
        this.currentHex = currentHex;
    }

    public Spectator getSpectator() {
        return spect;
    }

    public Hex getHex(int x, int y) {
        return hexBoard[x + size - 1][y + size - 1];
        
  
    }
    /**
     * Searches a certain range around a given hex for robots. The targetList of the board will be set
     * to the list of robots that the search finds.
     * 
     * @param h the hex that is to be searched around
     * @param range the range that the search should view
     * 
     */
    public void search(Hex h, int range) {
        
        targetList.clear();
        //looping through every hex on the board
        for (int y = size-1; y >= -(size-1); y--) {
            for (int x = -(size-1); x <= size-1; x++) {
                //checking if that hex is within range
                if (x < h.getPositionX() + (range + 1) && x > h.getPositionX() - (range + 1) && y < h.getPositionY() + (range + 1) && y > h.getPositionY() - (range + 1)) {
                    //additional check to see if the hex is within range
                    if(x + y > -(range+1) && x + y < (range+1)){          
                        //if the hex has robots in it
                        if(getHex(x, y) != null && !(getHex(x, y).getOcc().isEmpty())){
                            targetList.add(getHex(x, y));
                        }
                    }
                }
            }
        }        
    }
    
    public Hex getCurrentHex() {
        return currentHex;
    }

    public void addHexOcc(Hex h, Robot r) {
        h.addOcc(r);
    }

    public void removeHexOcc(Hex h, Robot r) {
        h.removeOcc(r);
    }

    public void damageHex(Hex h, int damage) {
        for (int i = 0; i < h.listOfOccupants.size(); i ++ ) {
            h.listOfOccupants.get(i).setHealth(h.listOfOccupants.get(i).getHealth() - damage);
        }
    }
    
    private void firstRobot() {
        currentHex = targetList.getFirst();
    }

    private void nextRobot() {
    	if(targetList.indexOf(currentHex) != targetList.indexOf(targetList.getLast())){
            currentHex = targetList.get(targetList.indexOf(currentHex) + 1);
    	}
    }

    private void prevRobot() {
    	if(targetList.indexOf(currentHex) != 0){
            currentHex = targetList.get(targetList.indexOf(currentHex) - 1);
    	}
    }

    private LinkedList<Hex> getTargetList() {
        return targetList;
    }
    
    private void addTotargetList(Hex h) {
        targetList.addLast(h);
    }
    
    private void clearTargetlist() {
        targetList.removeAll(targetList);
    }


    public static void main(String[] args) {
        Color[] testColourList = new Color[] {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta};
        Board myBoard = new Board(5, 2, testColourList);
        for(int i = -4; i <= 4; i++){
            for(int j = -4; j < 4; j++){
                if(myBoard.getHex(i, j) != null){
                    System.out.print(myBoard.getHex(i, j).toString());
                }
            }
            System.out.print("\n");
        }
        
        Robot robot1 = new Robot(2, 2);
        Robot robot2 = new Robot(2, 3);
        System.out.println("Placing robot 1 at position 1, 3");
        myBoard.getHex(1, 3).addOcc(robot1);
        
        System.out.println("Placing robot 2 at position -1, -1");
        myBoard.getHex(-1, -1).addOcc(robot2);
        
        
        System.out.println("Searching from hex (0, 0) with radius 0");
        myBoard.search(myBoard.getHex(0, 0), 0);
        System.out.println(myBoard.targetList.toString());
        
        System.out.println("Searching from hex (0, 0) with radius 1");
        myBoard.search(myBoard.getHex(0, 0), 1);
        System.out.println(myBoard.targetList.toString());
        
        System.out.println("Searching from hex (0, 0) with radius 2");
        myBoard.search(myBoard.getHex(0, 0), 2);
        System.out.println(myBoard.targetList.toString());
        
        System.out.println("Searching from hex (0, 0) with radius 3");
        myBoard.search(myBoard.getHex(0, 0), 3);
        System.out.println(myBoard.targetList.toString());
        
        System.out.println("Searching from hex (0, 0) with radius 4");
        myBoard.search(myBoard.getHex(0, 0), 4);
        System.out.println(myBoard.targetList.toString());
        
        System.out.println("Searching from hex (0, 0) with radius 9000");
        myBoard.search(myBoard.getHex(0, 0), 9000);
        System.out.println(myBoard.targetList.toString());
        
        System.out.println("Searching from hex (0, 0) with radius -7");
        myBoard.search(myBoard.getHex(0, 0), -7);
        System.out.println(myBoard.targetList.toString());
        
        System.out.println("Initial health of robot 1:");
        System.out.println(myBoard.getHex(1, 3).listOfOccupants.getFirst().getHealth());
        System.out.println("Damaging robot 1 by 1:");
        myBoard.damageHex(myBoard.getHex(1, 3), 1);
        System.out.println(myBoard.getHex(1, 3).listOfOccupants.getFirst().getHealth());
        
    }
}
