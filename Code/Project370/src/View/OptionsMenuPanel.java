package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.*;

import Model.Robot;

import javax.swing.JPanel;


public class OptionsMenuPanel extends JPanel{
	
	
	
	
	/** @public Initializes Options menu. */
	public OptionsMenuPanel(){
		super();
		
		
		//Send JSON list-request to librarian

		
		//for (/*all robots returned*/){
			
			//create robot info panel with information
		//}
		
		
				
	
	}
	
//	public String parse(){
		
//		JsonElement jelement = new JsonParser().parse(json);
//	    JsonObject  jobject = jelement.getAsJsonObject();
//	    jobject = jobject.getAsJsonObject("data");
//	    JsonArray jarray = jobject.getAsJsonArray("translations");
//	    jobject = jarray.get(0).getAsJsonObject();
//	    String result = jobject.get("translatedText").toString();
//	    return result;
//	}
	
	
	//info panel class for display
	private class RobotInfoPanel extends JPanel{
		
		Robot robot;
		
		/** @public Initializes Options menu. */
		public RobotInfoPanel(String team, String roboClass, String name, int matches, int wins, int losses){
			super();
		}
		
		private class ImportButtonListener implements ActionListener{
	    	public void actionPerformed(ActionEvent e){
	    		//imports robot tied to info panel
	    		//imports full, and saves to file locally
	    	}
	    }
		
	}

	
	private class CreateTeamButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		//creates team from selected robots
    	}
    }
	
	
	
}




