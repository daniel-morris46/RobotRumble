package Model;

import java.awt.Color;
import java.util.*;

public class RobotTeam {

    /** Boolean value for if the robot team is human. Human => true. AI => false. */
    boolean isHuman;

    /** The array of all robots in the team. */
    Robot teamOfRobot[];

    /** The colour of the team. */
    Color colour;

    /** The index number of the team. */
    int number;

    LinkedList<Robot> mailBoxdata;

    LinkedList<Integer> turnNumber;

    /**
     * Constructs a robot team given a boolean for if the team is human, the colour, and the index
     * number of the team.
     */
    public RobotTeam(boolean isHmn, Color teamColour, int teamNumber) {
        isHuman = isHmn;
        colour = teamColour;
        teamOfRobot = new Robot[3];
        mailBoxdata = new LinkedList<Robot>();
        turnNumber = new LinkedList<Integer>();
        teamOfRobot[0] = new Robot(1, teamNumber); // scout
        teamOfRobot[1] = new Robot(2, teamNumber); // sniper
        teamOfRobot[2] = new Robot(3, teamNumber); // tank

    }

    /** Returns the answer to the question: "Is the team still alive?" */
    public boolean isAlive() {
        int deadCounter = 0;
        for (int i = 0; i < 3; i++) {
            if (!teamOfRobot[i].isAlive()) {
                deadCounter += 1;
            }
        }

        if (deadCounter == 3) {
            return false;
        }
        return true;
    }


    public int getNumAliveRobots() {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            if (teamOfRobot[i].isAlive()) {
                counter++;
            }
        }
        return counter;
    }

    public void addMailbox(Robot r, int play, int numberofteam) {

        if (!turnNumber.isEmpty()) {
            for (int p = 0; p < turnNumber.size(); p++) {
                if (mailBoxdata.get(p) == r) {
                    turnNumber.set(p, play + numberofteam * 2);
                } else {
                    mailBoxdata.add(r);
                    turnNumber.add(play + numberofteam * 2);
                }
            }
        } else {
            mailBoxdata.add(r);
            turnNumber.add(play + numberofteam * 2);
        }


        for (int j = 0; j < turnNumber.size(); j++) {
            if (turnNumber.get(j) == play) {
                turnNumber.remove(j);
                mailBoxdata.remove(j);
            }
        }

        for (int i = 0; i < 3; i++) {
            teamOfRobot[0].getMailBoxdata(mailBoxdata);
        }
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
        RobotTeam rt1 = new RobotTeam(true, Color.red, 1);
        RobotTeam rt2 = new RobotTeam(true, Color.red, 1);
        RobotTeam rt3 = new RobotTeam(true, Color.red, 1);

        Robot r = new Robot(2, 0);
        Robot r1 = new Robot(1, 0);
        Robot r2 = new Robot(3, 0);

        rt1.addMailbox(r, 1, 1);
        rt1.addMailbox(r1, 1, 1);
        rt1.addMailbox(r2, 1, 1);

        System.out.println(rt1.mailBoxdata.size() + "size");
        for (int i = 0; i < rt1.mailBoxdata.size(); i++) {
            System.out.println(rt1.mailBoxdata.get(i).health + "health");
        }
    }

}
