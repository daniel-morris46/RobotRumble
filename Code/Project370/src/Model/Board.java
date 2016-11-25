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

    int currentHex;

    public Board(int s, int t) {
        colourList = new String[6];
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
        for (int y = s-1; y >= (-s+1); y--) {
            for (int x = (-s+1); x <= s-1; x++) {
                if ((x + y <= s-1) && (x + y >= (-s+1))) {
                    hexBoard[x + s - 1][y + s - 1] = new Hex(x, y);
                }
            }
        }
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

    public void setCurrentHex(int currentHex) {
        this.currentHex = currentHex;
    }

    public Spectator getSpectator() {
        return spect;
    }

    public Hex getHex(int x, int y) {
        return hexBoard[x + size - 1][y + size - 1];
        
  
    }

    public void search(Hex h, int range) {
        
        
        for (int y = size-1; y >= -(size-1); y--) {
            for (int x = -(size-1); x <= size-1; x++) {
                if (x < h.getPositionX() + (range + 1) && x > h.getPositionX() - (range + 1) && y < h.getPositionY() + (range + 1) && y > h.getPositionY() - (range + 1)) {
                    if(x + y > -(range+1) && x + y < (range+1)){
                        //System.out.print("("+x+","+y+")");
                        
                        if(hexBoard[x + size - 1][y + size - 1] != null && !(hexBoard[x + size - 1][y + size - 1].getOcc().isEmpty())){;
                            targetList.add(getHex(x, y));
                            
                        }
                    }
                }
            
            }
            //System.out.print("\n");
        }
        /*
        
        for (int y = -(size-1); y <= size-1; y++) {
            for (int x = -(size-1); x <= size-1; x++) {
                if (x < h.getPositionX() + (range + 1) && x > h.getPositionX() - (range + 1) && y < h.getPositionY() + (range + 1) && y > h.getPositionY() - (range + 1)) {
                    if(x + y > -(range+1) && x + y < (range+1)){
                        //System.out.print("("+x+","+y+")");
                        
                        if(hexBoard[x + size - 1][y + size - 1] != null && !(hexBoard[x + size - 1][y + size - 1].getOcc().isEmpty())){;
                            targetList.add(getHex(x, y));
                            
                        }
                    }
                }
            
            }
            //System.out.print("\n");
        }
        
        */
        
    }
    
    public Hex getCurrentHex() {
        if (targetList.isEmpty()) {
            System.out.println("Error: ");
        }
        return targetList.get(currentHex);
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
        currentHex = 0;
    }

    private void nextRobot() {
        currentHex++;
    }

    private void prevRobot() {
        currentHex--;
    }

    private LinkedList<RobotTeam> getTargetList() {
        LinkedList<RobotTeam> targetList = null;
        return targetList;
    }
    
    private void addTotargetList(Hex h) {
        targetList.addLast(h);
    }
    
    private void clearTargetlist() {
        targetList.removeAll(targetList);
    }


    public static void main(String[] args) {
        
//      for (int y = 4; y >= -4; y--) {
//          for (int x = -4; x <= 4; x++) {
//              System.out.print("("+x+","+y+")");
//          }
//          System.out.print("\n");
//      }
//      System.out.print("\n");
        
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
