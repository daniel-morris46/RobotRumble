package Model;

public class Spectator {

    int time;
    
    boolean fogOfWar;
    
    public Spectator() {
        time = 1;
        fogOfWar = true;
    }
    
    public void changeTime(){
        if(time != 4){
            time *= 2;
        }else{
            time = 1;
        }
    }
    
    public void pausePlay(){
        if(time != 0){
            time = 0;
        }else{
            time = 1;
        }
    }
    
    public void changeFogOfWar(){
        fogOfWar = !fogOfWar;
    }
    
    public boolean getFogOfWar() {
        return fogOfWar;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
