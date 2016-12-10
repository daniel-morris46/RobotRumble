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
    int currentRobot[];
    
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
    
    /**Amount of plays have happend.*/
    int playAmount;
    
    /**The list of possible targets for the current robot.*/
    LinkedList<Hex> targetList;
    
    /**The current selected hex on the board.*/
    Hex currentHex;
    
    boolean gameMode = true;

	/** @public Constructs a game board of a given size and number of players. */
    public Board(int boardSize, int numberOfTeams){
        this(boardSize, numberOfTeams, new Color[] {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta});
    }
    
    /** @public Constructs a game board given a size and number of players. */
    public Board(int boardSize, int numberOfTeams, Color inputColourList[]) {
        //colourList = new String[] {"Red", "Orange", "Yellow", "Green", "Blue", "Purple"};
        currentTeam = 0;
        currentRobot = new int[numberOfTeams];
        for(int i = 0; i < numberOfTeams; i++){
            if(i != 0){
                currentRobot[i] = 2;
            }else{
                currentRobot[i] = 0;
            }
        }
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
        
        // for setting the starting locations of the robots and the colours for the starting hexs
        for(int i = 0; i < 3; i++){
            if(numberOfTeams == 2){
                Teams[0].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), 0));
                Teams[1].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, 0));
                
                this.getHex(-(boardSize - 1), 0).setColour(Teams[0].getColour());
                this.getHex(boardSize - 1, 0).setColour(Teams[1].getColour());
            }else if(numberOfTeams == 3){
                Teams[0].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), 0));
                Teams[1].teamOfRobot[i].setPosition(this.getHex(0, boardSize -1));
                Teams[2].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, -(boardSize - 1)));
                
                this.getHex(-(boardSize - 1), 0).setColour(Teams[0].getColour());
                this.getHex(0, boardSize -1).setColour(Teams[1].getColour());
                this.getHex(boardSize - 1, -(boardSize - 1)).setColour(Teams[2].getColour());
            }else if(numberOfTeams == 6){
                Teams[0].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), 0));
                Teams[1].teamOfRobot[i].setPosition(this.getHex(-(boardSize - 1), boardSize -1));
                Teams[2].teamOfRobot[i].setPosition(this.getHex(0, boardSize - 1));
                Teams[3].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, 0));
                Teams[4].teamOfRobot[i].setPosition(this.getHex(boardSize - 1, -(boardSize -1)));
                Teams[5].teamOfRobot[i].setPosition(this.getHex(0, -(boardSize - 1)));
                
//                this.getHex(-(boardSize - 1), 0).setColour(Teams[0].getColour());
//                this.getHex(-(boardSize - 1), boardSize -1).setColour(Teams[1].getColour());
//                this.getHex(0, boardSize - 1).setColour(Teams[2].getColour());
//                this.getHex(boardSize - 1, 0).setColour(Teams[3].getColour());
//                this.getHex(-boardSize - 1, -(boardSize -1)).setColour(Teams[4].getColour());
//                this.getHex(0, -(boardSize - 1)).setColour(Teams[5].getColour());
                
                
            }
            
            
            //setting the fog of war
            this.updateHexColours();
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
        return currentRobot[currentTeam];
    }

    public void setCurrentRobot(int currentRobot) {
        this.currentRobot[currentTeam] = currentRobot;
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
    
    public int getPlayamunt() {
        return playAmount;
    }
    
    public void setPlayamunt(int p) {
        playAmount = p;
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
    
    public boolean isGameMode() {
		return gameMode;
	}

	public void setGameMode(boolean gameMode) {
		this.gameMode = gameMode;
		updateHexColours();
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
    	 for (int x = -(size-1); x <= size-1; x++) {
    		 for (int y = -(size-1); y <= size-1; y++) {
                 //checking if that hex is within range
    			 int isInRangeCounter = 0;
    			 for(int i = 0; i < 3; i++){
    				 if(getHex(x, y) != null) {
    					 if(!isOutOfRange(getHex(x, y), h  ,range)){
    						 isInRangeCounter += 1;
                     	}
    				 }
                 }
              
    			 if(isInRangeCounter == 3){
                     //if the hex has robots in it
                     if(getHex(x, y) != null && !(getHex(x, y).getOcc().isEmpty())){
                        	targetList.add(getHex(x, y));
                        }
                 }

            }
        }        
    }
    
    public void addToMailbox(RobotTeam rt) {
    	for (int i = 0; i < targetList.size(); i++) {
    		if (targetList.get(i).getOcc().size() != 0) {
    			for (int j = 0; j < targetList.get(i).getOcc().size(); j++) {
    				rt.addMailbox(targetList.get(i).getOcc().get(j), playAmount, teamAmount);
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
            if(!h.listOfOccupants.get(i).isAlive()){
                //h.listOfOccupants.get(i).setPosition(null);
                h.removeOcc(h.listOfOccupants.get(i));
            }
        }
    }
    
    public  void firstRobot() {
    	if (!targetList.isEmpty()) {
    		currentHex = targetList.getFirst();
    	}
    }

    public  void nextRobot() {
    	if (!targetList.isEmpty()) {
	    	if(targetList.indexOf(currentHex) != targetList.indexOf(targetList.getLast())){
	            currentHex = targetList.get(targetList.indexOf(currentHex) + 1);
	    	} else if (targetList.indexOf(currentHex) == targetList.indexOf(targetList.getLast())) {
	    		currentHex = targetList.getFirst();
	    	}
    	}
    }

    public  void prevRobot() {
    	if (!targetList.isEmpty()) {
	    	if(targetList.indexOf(currentHex) != 0){
	            currentHex = targetList.get(targetList.indexOf(currentHex) - 1);
	    	} else if (targetList.indexOf(currentHex) == 0) {
	    		currentHex = targetList.getLast();
	    	}
    	}
    }

    LinkedList<Hex> getTargetList() {
        return targetList;
    }
    
    private void addTotargetList(Hex h) {
        targetList.addLast(h);
    }
    
    public void clearTargetlist() {
        targetList.removeAll(targetList);
    }
    
    public void updateHexColours() {
        
        
        
        int isInRangeCounter = 0;
        
        
        
        for(int x = -(getSize() - 1); x < getSize(); x++){
            for(int y = -(getSize() - 1); y < getSize(); y++){
                if(getHex(x, y) != null){
                    
                    for(int i = 0; i < 3; i++){
                        if(this.getTeams()[this.getCurrentTeam()].getTeamOfRobot()[i].isAlive()){
                            if(isOutOfRange(getHex(x, y), this.getTeams()[this.getCurrentTeam()].getTeamOfRobot()[i].getPosition(), this.getTeams()[this.getCurrentTeam()].getTeamOfRobot()[i].getRange())){
                                isInRangeCounter += 1;
                            }
                        }
                        
                    }
                    
                  
                    
                    if(isInRangeCounter == this.getTeams()[this.getCurrentTeam()].getNumAliveRobots()){
                        getHex(x, y).setColour(Color.gray);
                    }else{
                        getHex(x, y).setColour(Color.white);
                    }
                    
                    isInRangeCounter = 0;
                
                    
                }
            }
        }
                
    }
    
    /** Returns true if hex1 is out of range of hex2 given the specified range. */
    private boolean isOutOfRange(Hex hex1, Hex hex2, int range) {
        //LinkedList<Hex> rangeList = new LinkedList<Hex>();
        
        
        if(hex2.getPositionX() < hex1.getPositionX() + (range + 1) && hex2.getPositionX() > hex1.getPositionX() - (range + 1)){
            if(hex2.getPositionY() < hex1.getPositionY() + (range + 1) && hex2.getPositionY() > hex1.getPositionY() - (range + 1)){
                if(hex2.getPositionX() + hex2.getPositionY() > (hex1.getPositionX() + hex1.getPositionY() + -(range+1)) && hex2.getPositionX() + hex2.getPositionY() < (hex1.getPositionX() + hex1.getPositionY() + range+1)){
                    //System.out.println("Hex: (" + hex1.getPositionX() + ", " + hex1.getPositionY() + ") is  in range of (" + hex2.getPositionX() + ", " + hex2.getPositionY() + ")");
                    return false;
                    
                }
            }
        }
        return true;
        
        
//        //looping through every hex on the board
//        for (int y = size-1; y >= -(size-1); y--) {
//            for (int x = -(size-1); x <= size-1; x++) {
//                //checking if that hex is within range
//                if (x < hex1.getPositionX() + (range + 1) && x > hex1.getPositionX() - (range + 1) && y < hex1.getPositionY() + (range + 1) && y > hex1.getPositionY() - (range + 1)) {
//                    //additional check to see if the hex is within range
//                    if(x + y > -(range+1) && x + y < (range+1)){          
//
//                            rangeList.add(getHex(x, y));
//                            System.out.println("HELLO");
//                        
//                    }
//                }
//            }
//        }
//        if(rangeList.contains(hex2)){
//            rangeList.clear();
//            return false;
//        }else{
//            rangeList.clear();
//            return true;
//        }
    }
    
    public void updateMovementColours(Robot curRobot, int x, int y, Board board, int cur) {
    	try {
	    	if (board.isGameMode()) {
		    	 if(curRobot.getAbsDirection() == 0){ 
		    		 updateHexColours();
		    		 if (board.getHex(x + 1, y) != null) {
		    			 board.getHex(x + 1, y).setColour(Color.green); 
		    		 }
		         } else if (curRobot.getAbsDirection() == 1){
		        	 updateHexColours();
		        	 if (board.getHex(x + 1, y - 1) != null) {
		        		 board.getHex(x + 1, y - 1).setColour(Color.green);
		        	 }
		         } else if (curRobot.getAbsDirection() == 2){
		        	 updateHexColours();
		        	 if (board.getHex(x, y - 1) != null) {
		        		 board.getHex(x, y - 1).setColour(Color.green);
		        	 }
		         } else if (curRobot.getAbsDirection() == 3){
		        	 updateHexColours();
		        	 if (board.getHex(x - 1, y) != null) {
		        		 board.getHex(x - 1, y).setColour(Color.green);
		        	 }
		         } else if (curRobot.getAbsDirection() == 4){
		        	 updateHexColours();
		        	 if (board.getHex(x - 1, y + 1) != null) {
		        		 board.getHex(x - 1, y + 1).setColour(Color.green);
		        	 }
		         } else if (curRobot.getAbsDirection() == 5){
		        	 updateHexColours();
		        	 if (board.getHex(x, y + 1) != null) {
		        		 board.getHex(x, y + 1).setColour(Color.green);
		        	 }
		         }
	    	}
    	} catch (Exception e) {
    		
    	}
    }
    
    public void initializeScripts(Boolean[] isHuman, String[] scoutPaths, String[] sniperPaths, String[] tankPaths){
    	for(int i = 0; i < isHuman.length; i++){
    		Teams[i].isHuman = isHuman[i];
    		
    		if(!isHuman[i]){
    			Teams[i].getTeamOfRobot()[0].filePath = scoutPaths[i];
    			Teams[i].getTeamOfRobot()[1].filePath = sniperPaths[i];
    			Teams[i].getTeamOfRobot()[2].filePath = tankPaths[i];
    		}
    	}
    }
    
    public void updateTargetColours(Board board) {
    	updateHexColours();
    	for (int i = 0; i < targetList.size(); i++) {
    		targetList.get(i).setColour(Color.yellow);
    	}
    	if (!targetList.isEmpty() && !board.gameMode) {
	    	board.currentHex.setColour(Color.red);
	    }
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
