package Model;

import java.awt.Color;

public class RobotTeam {

    /** Boolean value for if the robot team is human. Human => true.   AI => false. */
    boolean isHuman;
    
    /** The array of all robots in the team. */
    Robot teamOfRobot[];
    
    /** The colour of the team. */
    Color colour;
    
    /** The index number of the team. */
    int number;
    
    /** Constructs a robot team given a boolean for if the team is human, the colour, and the index number of the team. */
    public RobotTeam(boolean isHmn, Color teamColour, int teamNumber) {
        isHuman = isHmn;
        colour = teamColour;
        teamOfRobot = new Robot[3];
        teamOfRobot[0] = new Robot(1, teamNumber); //scout
        teamOfRobot[1] = new Robot(2, teamNumber); //sniper
        teamOfRobot[2] = new Robot(3, teamNumber); //tank

    }
    
    /** Returns the answer to the question: "Is the team still alive?"*/
    public boolean isAlive(){
        for(int i = 0; i < 3; i++){
            if(!teamOfRobot[i].isAlive()){
                return false;
            }
        }
        return true;
    }

    public boolean isHuman() {
        return isHuman;
    }
    
    public void setHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }
    
    public Robot[] getTeamOfRobot() {
        return teamOfRobot;
    }

    public void setTeamOfRobot(Robot[] teamOfRobot) {
        this.teamOfRobot = teamOfRobot;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public int getNumber() {
        return number;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
