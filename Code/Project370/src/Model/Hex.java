package Model;

import java.util.LinkedList;

public class Hex {
    
    int positionX;
    int positionY;
    
    LinkedList<Robot> listOfOccupants;
    
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

}

