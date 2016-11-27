package Model;

import java.util.*;

public class Board {
    public Hex hexBoard[][];

    public RobotTeam[] Teams;
    
    int currentTeam;
    int currentRobot;
    int currentTarget;
    int size;
    int teamAmount;
    
    Spectator spect;
    
    String[] colourList;
    
    LinkedList<Hex> targetList;

    Hex currentHex;

    public Board(int s, int t) {                // BETTER VARIABLE NAMES FOR s and t
        colourList = new String[6];             // MAKE COLOURS BE ABLE TO BE CHANGED
        colourList[0] = "RED";
        colourList[1] = "BLUE";
        colourList[2] = "ORANGE";
        colourList[3] = "PURPLE";
        colourList[4] = "YELLOW";
        colourList[5] = "GREEN";
        this.currentTeam = 0;
        this.currentRobot = 0;
        this.size = s;
        this.teamAmount = t;
        spect = new Spectator();
        Teams = new RobotTeam[t];
        targetList = new LinkedList<Hex>();
        for(int i = 0; i < t; i++){
            Teams[i] = new RobotTeam(true, colourList[i], i);
        }
        hexBoard = new Hex[s * 2 - 1][s * 2 - 1];
        for (int y = s-1; y >= (-s+1); y--) {               // CHANGE ORDER OF THIS TO MAKE EASIER TO READ
            for (int x = (-s+1); x <= s-1; x++) {
                if ((x + y <= s-1) && (x + y >= (-s+1))) {
                    hexBoard[x + s - 1][y + s - 1] = new Hex(x, y);
                }
            }
        }
        
        for(int i = 0; i < Teams.length; i++){
			Robot curRobots[] = Teams[i].getTeamOfRobot();
			
			for(int j = 0; j < 3; j++){
				curRobots[j].setPosition(getHex(0, 0));;
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

    public String[] getColourList() {
        return colourList;
    }

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

                                                                  // CHANGE hexBoard[x + size - 1][y + size - 1] to hexBoard.getHex(x, y)
    public void search(Hex h, int range) {
        for (int y = size-1; y >= -(size-1); y--) {
            for (int x = -(size-1); x <= size-1; x++) {
                if (x < h.getPositionX() + (range + 1) && x > h.getPositionX() - (range + 1) && y < h.getPositionY() + (range + 1) && y > h.getPositionY() - (range + 1)) {
                    if(x + y > -(range+1) && x + y < (range+1)){
                        if(hexBoard[x + size - 1][y + size - 1] != null && !(hexBoard[x + size - 1][y + size - 1].getOcc().isEmpty())){
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

    private LinkedList<RobotTeam> getTargetList() {
        return targetList;
    }
    
    private void addTotargetList(Hex h) {
        targetList.addLast(h);
    }
    
    private void clearTargetlist() {
        targetList.removeAll(targetList);
    }


    public static void main(String[] args) {
    
        //TESTING
        Board myBoard = new Board(5, 2);
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(myBoard.hexBoard[i][j] != null){
                    System.out.print("("+myBoard.hexBoard[i][j].positionX + "," + myBoard.hexBoard[i][j].positionY+")");
                }
            }
            System.out.print("\n");
        }
        
        Robot myRobot = new Robot(2, 2);
        
        
        myBoard.hexBoard[5][5].addOcc(myRobot);
        myBoard.hexBoard[4][5].addOcc(myRobot);
        myBoard.search(myBoard.hexBoard[4][4], 2);
        myBoard.firstRobot();
        System.out.println(myBoard.getCurrentHex().positionX + "," + myBoard.getCurrentHex().positionY );
        myBoard.nextRobot();
        System.out.println(myBoard.getCurrentHex().positionX + "," + myBoard.getCurrentHex().positionY );
        myBoard.damageHex(myBoard.getHex(1, 1), 1);
        System.out.println(myBoard.getHex(1, 1).listOfOccupants.getFirst().getHealth());
        
        // 4, 4 is 0, 0
        
        
        
        /*
        Hex h0 = new Hex(0, 0);
        Hex h1 = new Hex(0 ,0);
        Hex h2 = new Hex(0, 0);
        
        addTotargetList(h0);
        addTotargetList(h1);
        addTotargetList(h2);
        firstRobot();
        System.out.println(getCurrentHex().positionX + "," + getCurrentHex().positionY );
        nextRobot();
        System.out.println(getCurrentHex().positionX + "," + getCurrentHex().positionY );
        prevRobot();
        
        */
    }


}