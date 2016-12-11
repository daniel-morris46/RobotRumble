package View;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.*;

import Controller.Controller;

/**
 * Pair Programming Done by Brandon and Kevin
 */

/**
 * @category View
 * 
 * This class contains the GUI for the main entry point of the program. This menu moves the
 * player between the options, the game, and exiting the application.
 */
public class GameMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the game menu, the system's entry point
     * 
     * @throws IOException
     * @throws FontFormatException
     */
    public GameMenu() {

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Import a new font to use for the text
        Font newFont = null;
        try {
            newFont = Font.createFont(Font.PLAIN,
                    getClass().getResource("/View/resources/ARCADECLASSIC.TTF").openStream());
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Set the Graphics for the new font
        GraphicsEnvironment gFont = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gFont.registerFont(newFont);

        newFont = newFont.deriveFont(15f);

        // Set re-sizeable and max frame
        this.setVisible(true);
        this.setBounds(0, 0, 1250, 1000);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);


        // Initialize the menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
        menuPanel.setSize(400, 400);
        menuPanel.setVisible(true);
        menuPanel.setBackground(Color.CYAN);
        this.add(menuPanel);

        // Initialize the play button and add it to the menu panel
        JButton playButton;
        playButton = new JButton("Play");
        playButton.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 80));
        playButton.setSize(1000, 250);
        playButton.setPreferredSize(new Dimension(1000, 250));
        playButton.setVisible(true);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.addActionListener(new PlayListener());

        menuPanel.add(playButton);
        menuPanel.add(Box.createVerticalGlue());
        // Initialize the option button and add it to the menu panel
        JButton optionButton;
        optionButton = new JButton("Options");
        optionButton.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 80));
        optionButton.setSize(1000, 250);
        optionButton.setPreferredSize(new Dimension(1000, 250));
        optionButton.setVisible(true);
        optionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionButton.addActionListener(new OptionListener());

        menuPanel.add(optionButton);
        menuPanel.add(Box.createVerticalGlue());
        // Initialize the exit button and add it to the menu panel
        JButton exitButton;
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 80));
        exitButton.setSize(1000, 250);
        exitButton.setPreferredSize(new Dimension(1000, 250));
        exitButton.setVisible(true);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(new ExitListener());

        menuPanel.add(exitButton);

        this.setVisible(true);
    }

    /**
     * The action listener for the play button
     * 
     * This takes the player to the PlayMenu to create a game
     */
    private class PlayListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.getInstance().gameMenu.setVisible(false);
            Controller.getInstance().playMenu.setVisible(true);

        }
    }

    /**
     * The action listener for the options button
     * 
     * This takes the player to the OptionsMenu to select options
     */
    private class OptionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.getInstance().gameMenu.setVisible(false);
            Controller.getInstance().optionsMenu.setVisible(true);
        }
    }

    /**
     * The action listener for the exit button
     * 
     * This exits the application when clicked
     */
    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /** Gets an instance of the controller, starting the system */
    public static void main(String[] args) {
        Controller.getInstance();
    }
}
