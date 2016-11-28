package Model;

public class Spectator {

    /** Value for how fast the game is being showm. Either 0, 1, 2, or 4. */
    int time;
    
    /** Boolean for the fog of war toggle. */
    boolean fogOfWar;
    
    /** Constructs a spectator of time = 1 and fogOfWar = true. */
    public Spectator() {
        time = 1;
        fogOfWar = true;
    }
    
    /** Changes the speed of the game being played. */
    public void changeTime(){
        if(time != 4){
            time *= 2;
        }else{
            time = 1;
        }
    }
    
    /** Pauses / Plays the game by setting the time to zero or one. */
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
