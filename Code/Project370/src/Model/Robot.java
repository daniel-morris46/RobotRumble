package Model;

public class Robot {
    
    int health;
    
    int movementMax;
    
    int movementCur;
    
    int range;
    
    Hex position;
    
    int type; // scout = 1, sniper = 2, tank = 3
    
    int damage;
    
    int team;
    
    Interpreter robotInterpreter;
    
    int absDirection;
    
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
