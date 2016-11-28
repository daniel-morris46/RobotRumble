package Model;

import java.util.LinkedList;

public class Hex {
    
    /** The x coordinate of the hex.*/
    int positionX;

    /** The y coordinate of the hex.*/
    int positionY;
    
    /** The list of robots currently on the hex. */
    LinkedList<Robot> listOfOccupants;
    
    /** Constructs a hex given an x and y coordinate.*/
    public Hex(int x, int y) {
        
        positionX = x;
        positionY = y; 
        listOfOccupants = new LinkedList<Robot>();
        
    }
    
    public void addOcc(Robot r){
        listOfOccupants.add(r);
    }
    
    public void removeOcc(Robot r){
        listOfOccupants.remove(r);
    }
    
    public LinkedList<Robot> getOcc(){
        return listOfOccupants;
    }

    public int getPositionX() {
        return positionX;
    }
    public int getPositionY() {
        return positionY;
    }
    
    public String toString() {
        return "(" + positionX + "," + positionY + ")";
    }

}

