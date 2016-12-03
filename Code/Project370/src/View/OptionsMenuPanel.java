package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.*;

import Model.Robot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;
import java.net.*;


public class OptionsMenuPanel extends JPanel{
	
	private final String hostName = "gpu0.usask.ca";
	private final String request = "{ \"list-request\" : { \"data\" : \"brief\" }}";
	private final int port = 20001;
	
	/** @public Initializes Options menu. */
	public OptionsMenuPanel(){
		super();
		
		String serverData = "";
		
		try {
			Socket socket = new Socket(hostName, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(request);
			
			serverData = in.readLine();
			System.out.println(serverData);
			serverData = "{ \"robots\":" + serverData + "}";
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Finished");
		
		//PARSE serverData
		
		JsonElement jElement = new JsonParser().parse(serverData);
		
		JsonObject jObject = jElement.getAsJsonObject();
		JsonArray jArray = jObject.getAsJsonArray("robots");
		
		//Send JSON list-request to librarian

		
		//for (/*all robots returned*/){
			
			//create robot info panel with information
		//}
	}
	
	public static void main(String args[]){
		JFrame testFrame = new JFrame("IN-GAME-MENU-PANEL-TEST");
    	testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	testFrame.setVisible(true);
    	testFrame.setSize(1200, 1000);
    	testFrame.setResizable(false);
    	
    	testFrame.add(new OptionsMenuPanel());
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




