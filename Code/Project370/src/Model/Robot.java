package Model;

public class Robot {
    
    /** The current health of the robot.*/
    int health;
    
    /** The maximum movement distance of the robot.*/
    int movementMax;
    
    /** The current distance the robot has moved.*/
    int movementCur;
    
    /** The range of the robot.*/
    int range;
    
    /** The hex that the robot is currently on.*/
    Hex position;
    
    /** The type of the robot. scout = 1, sniper = 2, tank = 3 */
    int type; // scout = 1, sniper = 2, tank = 3
    
    /** The damage that this robot can deal with one shot.*/
    int damage;
    
    /** The index of the current team that the robot is on. */
    int team;
    
    /** The interpreter of the current robot. */
    Interpreter robotInterpreter;
    
    /** The direction of the robot. */
    int absDirection;
    
    /** Constructs a robot given a robot type and a team index. */
    public Robot(int robotType, int robotTeam) {
        health = robotType;
        movementMax = 4 - robotType;
        movementCur = 0;
        if(robotType == 1){
            range = 2;
        }else if(robotType == 2){
            range = 3;
        }else{
            range = 1;
        }
        position = new Hex(0, 0);
        type = robotType;
        damage = robotType;
        team = robotTeam;
        robotInterpreter = new Interpreter();
        absDirection = 0;
    }
    
    /** Returns the answer to the question: "Is the robot still alive?"*/
    public boolean isAlive(){
        if(health > 0){
            return true;
        }else{
            return false;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMovementMax() {
        return movementMax;
    }

    public int getMovementCur() {
        return movementCur;
    }

    public void setMovementCur(int movementCur) {
        this.movementCur = movementCur;
    }

    public int getRange() {
        return range;
    }

    public Hex getPosition() {
        return position;
    }

    public void setPosition(Hex position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getTeam() {
        return team;
    }

    public Interpreter getRobotInterpreter() {
        return robotInterpreter;
    }

    public int getAbsDirection() {
        return absDirection;
    }

    public void setAbsDirection(int absDirection) {
        this.absDirection = absDirection;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
