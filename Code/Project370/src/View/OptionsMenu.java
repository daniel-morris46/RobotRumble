package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import Controller.Controller;

public class OptionsMenu extends JFrame {

    /** @public The panel where all images will be drawn */
    public OptionsMenuPanel optionsPanel;

    private static final long serialVersionUID = 1L;


    /**
     * Public constructor. Constructs the frame for the options menu.
     */
    public OptionsMenu() {
        super("Import robots");
        // Import a new font to use for the text
        Font newFont = null;
        try {
            newFont = Font.createFont(Font.PLAIN,
                    getClass().getResource("/View/resources/ARCADECLASSIC.TTF").openStream());
        } catch (FontFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Set the Graphics for the new font
        GraphicsEnvironment gFont = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gFont.registerFont(newFont);

        newFont = newFont.deriveFont(15f);


        setLayout(new GridLayout(2, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set re-sizeable and max frame
        this.setVisible(true);
        this.setBounds(0, 0, 1250, 1000);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        optionsPanel = new OptionsMenuPanel();
        optionsPanel.setBackground(Color.CYAN);

        add(optionsPanel);

        optionsPanel.setVisible(true);
        setVisible(true);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("ARCADECLASSIC", Font.PLAIN, 150));
        backButton.setSize(1000, 250);
        backButton.setPreferredSize(new Dimension(1000, 350));
        backButton.addActionListener(new BackListener());
        add(backButton);
        revalidate();
    }

    /**
     * The action listener for the back button.
     */
    private class BackListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.getInstance().gameMenu.setVisible(true);
            Controller.getInstance().optionsMenu.setVisible(false);
        }
    }

    public static void main(String args[]) {
        new OptionsMenu();
    }
}
