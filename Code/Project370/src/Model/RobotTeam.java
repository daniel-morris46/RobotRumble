package Model;

public class RobotTeam {

    boolean isHuman;
    
    Robot teamOfRobot[];
    
    String colour;
    
    int number;
    
    public RobotTeam(boolean isHmn, String teamColour, int teamNumber) {
        isHuman = isHmn;
        colour = teamColour;
        teamOfRobot = new Robot[3];
        teamOfRobot[0] = new Robot(1, teamNumber); //scout
        teamOfRobot[1] = new Robot(2, teamNumber); //sniper
        teamOfRobot[2] = new Robot(3, teamNumber); //tank

    }
    
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

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getNumber() {
        return number;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
