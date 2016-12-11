package View;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.*;

import Controller.Controller;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.*;
import java.net.*;

/**
 * @category View
 * 
 * This panel imports robots from the designated server and displays them for
 * the user one at a time. Each robot panel displayed contains an import button
 * for importing the script to the local directory.
 */
public class OptionsMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** The robot librarian's host name */
    private final String hostName = "gpu0.usask.ca";

    /** The port of the robot librarian's host */
    private final int port = 20001;

    /** The request to send to the server to get robots */
    private final String request = "{ \"list-request\" : { \"data\" : \"brief\" }}";

    /** Initializes Options menu. */
    public OptionsMenuPanel() {

        // Calls the super constructor
        super();

        // Set the size of the options menu panel
        setSize(1400, 800);

        // Set the layout to be a grid layout 2 by 2
        setLayout(new GridLayout(2, 2));

        // Initialize the string for holding server data
        String serverData = "";

        try {
            // Connect to robot librarian server
            Socket socket = new Socket(hostName, port);

            // Initialize output stream for sending messages to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Initialize input stream for receiving answers from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send request to server
            out.println(request);

            // Read server response into data string
            serverData = in.readLine();

            // Modify server's response for use with gson parser
            serverData = "{ \"robots\":" + serverData + "}";

            // Close the socket
            socket.close();

            // Create json element by parsing the server data
            JsonElement jElement = new JsonParser().parse(serverData);

            // Get the element as an object
            JsonObject jObject = jElement.getAsJsonObject();

            // Convert json object to array containing each robot's script
            JsonArray briefInfo = jObject.getAsJsonArray("robots");

            // Initialize variables for storing robot information
            String robotTeam = "";
            String robotClass = "";
            String robotName = "";
            int robotMatches = 0;
            int robotWins = 0;
            int robotLosses = 0;

            // Initialize array of robot panels, one for each server-provided robot
            JPanel[] robotPanels = new JPanel[briefInfo.size()];

            // For each robot script
            for (int i = 0; i < briefInfo.size(); i++) {
                // Parse the script
                JsonObject curObj =
                        briefInfo.get(i).getAsJsonObject().get("script").getAsJsonObject();

                // Get robot information and store in variables
                robotTeam = curObj.get("team").getAsString();
                robotClass = curObj.get("class").getAsString();
                robotName = curObj.get("name").getAsString();
                robotMatches = curObj.get("matches").getAsInt();
                robotWins = curObj.get("wins").getAsInt();
                robotLosses = curObj.get("losses").getAsInt();

                // Create a new robot panel using the current robot's given information
                robotPanels[i] = new RobotInfoPanel(briefInfo.get(i).getAsJsonObject(), robotTeam,
                        robotClass, robotName, robotMatches, robotWins, robotLosses);

                // Add the current robot panel to the OptionMenuPanel
                this.add(robotPanels[i]);

                // Set the robot panel as visible
                robotPanels[i].setVisible(true);
            }
        } catch (IOException e) {
            // Throw an error if the host is invalid
            System.err.println("Could not find host. Robot Librarian is disconnected from system.");
        } finally {
            // Revalidate and repaint the OptionsMenuPanel
            revalidate();
            repaint();
        }
    }

    /**
     * JPanel that displays information on a robot to be imported from the robot librarian server.
     */
    private class RobotInfoPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        /** The JSON object representing the current robot */
        private JsonObject script;

        /** The name of the robot */
        private String name;

        /**
         * Creates a robot info panel for displaying the given robot, as well as importing the
         * robot's script to a local directory.
         * 
         * @param robotScript The robot's full script
         * @param team The robot creator's team
         * @param robotClass The class of the robot (scout, sniper, tank)
         * @param robotName The robot's name
         * @param matches The number of matches the robot has played
         * @param wins The number of wins the robot has
         * @param losses The number of losses the robot has
         */
        public RobotInfoPanel(JsonObject robotScript, String team, String robotClass,
                String robotName, int matches, int wins, int losses) {
            super();

            // Set the grid layout of the info panel
            setLayout(new GridLayout(8, 1));

            // Set the panel's size
            setSize(100, 100);

            // Set the robot's script
            script = robotScript;

            // Set the robotInfoPanel's name
            name = robotName;

            // Add a label for each stat and display it
            add(new JLabel("Team: " + team));
            add(new JLabel("Class: " + robotClass));
            add(new JLabel("Name: " + robotName));
            add(new JLabel("Matches: " + matches));
            add(new JLabel("Wins: " + wins));
            add(new JLabel("Losses: " + losses));

            // Add a border to this RobotInfoPanel
            this.setBorder(BorderFactory.createLineBorder(Color.black));

            // Add an import button
            JButton importButton = new JButton("Import");

            // Add the action listener to the import button to allow for importing the script
            importButton.addActionListener(new ImportButtonListener(importButton));

            // Add the button to the OptionMenuPanel
            add(importButton);

            // Revalidate the panel
            revalidate();
        }

        /**
         * The action listener for the import button, which imports the robot's script to the
         * src/Model/scripts/ directory when pressed.
         */
        private class ImportButtonListener implements ActionListener {

            // The actual button
            private JButton button;

            /**
             * Creates an import button listener for importing a script
             * 
             * @param _button A reference to the button for disabling on import
             */
            public ImportButtonListener(JButton _button) {
                super();
                button = _button;
            }

            /** Imports the robot script to a local folder and disables the button */
            public void actionPerformed(ActionEvent e) {

                // Disable the button
                button.setText("Imported!");
                button.setEnabled(false);

                // Write the script to a local file
                try {
                    Writer writer = new FileWriter("src/Model/scripts/" + name + ".json");
                    writer.write(script.toString());
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    ;
                }
            }
        }
    }

    /** Gets the instance of the controller, starting the system */
    public static void main(String args[]) {
        Controller.getInstance();
    }
}
