package edu.neu.csye6200.ca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author Harshit
 *        ClassName : CrystalMainApp
 *        Description : Main class for displaying the application.
 *        Valuable Output : Generates a UI and UI action elements.
 *        Uses Crystalset repeatitvely to generate consecutive crystals.
 */

public class CrystalMainApp extends  CAApp{
        // UI elements
        protected static JButton startButton, pauseButton, rewindButton, stopButton, createButton;
        protected static JPanel mainPanel, northPanel, nConfig, nPlay, statusPanel;
        private CACrystalSet caCrystalSet;
        protected static JComboBox<Rules> comboRules = null;
        protected static JComboBox<Integer> comboSleepTime = null;
        protected static JComboBox<Integer> comboGenLimit = null;;
        protected JLabel lblRules = null;
        protected JLabel lblSleepTime = null;
        protected JLabel lblGenLimit = null;
        protected JLabel lblGenCount = null;
        protected static JLabel genCount = null;
        protected static JLabel lblStatus = null;

        // Default Display region
        private static final int FRAME_WIDTH = 1500;
        private static final int FRAME_HEIGHT = 1500;
        private static final int BUTTONS_HEIGHT = 50;

        // For Logging application process to the console.
        private static Logger log = Logger.getLogger(CrystalMainApp.class.getName());

        public static void main(String[] args) {

            new CrystalMainApp(); // Instantiating the UI and bootstrapping the simulation
            log.info("Crystal Growth started...");
        }

        // Default Constructor
        public CrystalMainApp() {

            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            frame.setTitle("Crystal Growth");

            frame.setResizable(true);
            lblStatus.setText("Welcome to Mobile Automata Simulation World ... !!! ");

            showUI(); // Cause the Swing Dispatch thread to display the JFrame

        }


    /**
         * Create a main panel that will hold the bulk of our application display
         */
        @Override
        public JPanel getMainPanel() {

            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            /*
             * call to initialize and get the north Panel which contains all the user
             * interaction items
             */
            mainPanel.add(BorderLayout.NORTH, getnorthPanel());
            mainPanel.setVisible(true);
            return mainPanel;
        }

        /*
         * North Panel which contains nConfig and nPlay panels which helps in user
         * interaction
         */
        public JPanel getnorthPanel() {

            northPanel = new JPanel();

            northPanel.setLayout(new FlowLayout());
            northPanel.setBackground(Color.WHITE);
            northPanel.setPreferredSize(new Dimension(100, 50));

            nConfig = new JPanel();

            nConfig.setLayout(new FlowLayout());
            nConfig.setBackground(Color.WHITE);


            lblRules = new JLabel("Rules");
            nConfig.add(lblRules);
            final Rules rulesNames[] = { Rules.SIMPLEICE, Rules.MEDIUMICE, Rules.EXTREMEICE};
            comboRules = new JComboBox<Rules>(rulesNames);
            comboRules.setMaximumRowCount(3);
            comboRules.setEditable(false);
            comboRules.addActionListener(this);
            nConfig.add(comboRules);

            // Text Boxes
            lblSleepTime = new JLabel("Sleep Time");
            nConfig.add(lblSleepTime);
            final Integer slTimes[] = { 400, 600, 1000, 2000};
            comboSleepTime = new JComboBox<Integer>(slTimes);
            comboSleepTime.setMaximumRowCount(4);
            comboSleepTime.setEditable(false);
            comboSleepTime.addActionListener(this);
            nConfig.add(comboSleepTime);

            lblGenLimit = new JLabel("Generation Limit");
            nConfig.add(lblGenLimit);
            final Integer genLimits[] = {16, 20 , 22, 24};
            comboGenLimit = new JComboBox<Integer>(genLimits);
            comboGenLimit.setMaximumRowCount(4);
            comboGenLimit.setEditable(false);
            comboGenLimit.addActionListener(this);
            nConfig.add(comboGenLimit);

            // Create Button
            createButton = new JButton("Create");
            createButton.addActionListener(this);
            createButton.setFocusPainted(false);
            nConfig.add(createButton);

            nPlay = new JPanel();
            nPlay.setLayout(new FlowLayout());
            nPlay.setBackground(Color.WHITE);

            // Buttons
            startButton = new JButton("Start");
            startButton.addActionListener(this);
            startButton.setFocusPainted(false);
            nPlay.add(startButton);

            pauseButton = new JButton("Pause"); // Allow the app to hear about button pushes
            pauseButton.addActionListener(this);
            nPlay.add(pauseButton);

            stopButton = new JButton("Stop"); // Allow the app to hear about button
            stopButton.addActionListener(this);
            nPlay.add(stopButton);

            rewindButton = new JButton("Rewind"); // Allow the app to hear about button
            rewindButton.addActionListener(this);
            nPlay.add(rewindButton);


            // Labels
            lblGenCount = new JLabel("Generations : ", 4); // Allow the app to hear Input button pushes
            nPlay.add(lblGenCount);

            genCount = new JLabel("0", 4);
            nPlay.add(genCount);

            // Status Panel
            statusPanel = new JPanel();
            statusPanel.setLayout(new FlowLayout());
            statusPanel.setBackground(Color.LIGHT_GRAY);
            mainPanel.add(BorderLayout.SOUTH, statusPanel);

            // Labels
            lblStatus = new JLabel("", 10);
            nPlay.add(lblStatus);




            // Initially disabling till user creates a simulation
            for (Component component : nPlay.getComponents()) {
                component.setEnabled(false);
            }

            northPanel.add(nConfig);
            northPanel.add(nPlay);

            return northPanel;
        }

        @Override
        // The related actions for the buttons
        public void actionPerformed(ActionEvent ae) {

            if ("Create".equals(ae.getActionCommand())) { // Create event handler

                // Initialization

                if (Arrays.asList(mainPanel.getComponents()).contains(caCrystalSet))
                    mainPanel.remove(caCrystalSet);

                CACell.cellcount = 0;

                int rows = 80;
                int cols = 120;
                Rules rule = (Rules) comboRules.getSelectedItem();
                int maximumGen = (int) comboGenLimit.getSelectedItem();
                int sleepTime = (int) comboSleepTime.getSelectedItem();


                CACrystal caCrystal = new CACrystal(rule, rows, cols);
                caCrystalSet = new CACrystalSet(caCrystal, maximumGen, sleepTime);

                caCrystalSet.setLayout(new BorderLayout());

                // Setting preferred Sizes

                caCrystalSet.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));


                // Disabling nConfig till user is done with the simulation
                for (Component component : nConfig.getComponents()) {
                    component.setEnabled(false);
                }

                // Enabling nPlay till user is done with the simulation
                for (Component component : nPlay.getComponents()) {
                    component.setEnabled(true);
                }

                // Initially there is no need to rewind or pause as the simulation is not yet
                // started
                pauseButton.setEnabled(false);
                rewindButton.setEnabled(false);

                caCrystalSet.setBackground(Color.WHITE);
                mainPanel.add(BorderLayout.CENTER, caCrystalSet);
                frame.setVisible(true);
                lblStatus.setText("Simulation Region Created successfully...");


            } else if ("Start".equals(ae.getActionCommand())) { // Start button event

                lblStatus.setText("Simulation Started ... !!! ");
                log.info("Starting the Crystal simulation ... !!! ");

                // In start mode, you can pause/stop the simulation
                startButton.setEnabled(false);
                rewindButton.setEnabled(false);

                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);

                // Getting the next Region
                caCrystalSet.nextCrystal();

            } else if ("Pause".equals(ae.getActionCommand())) { // Pause button event
                lblStatus.setText("Simulation paused ... !!! ");
                // In pause mode you can rewind,start,stop the simulation
                pauseButton.setEnabled(false);

                rewindButton.setEnabled(true);
                startButton.setEnabled(true);
                stopButton.setEnabled(true);

                // Inform the thread to pause the simulation
                caCrystalSet.pauseThread();

            } else if ("Rewind".equals(ae.getActionCommand())) {
                lblStatus.setText("Simulation is rewinding ...");

                log.info("Simulation will now rewind...");

                rewindButton.setEnabled(false);
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);

                // Inform the thread to rewind the simulation
                caCrystalSet.rewindCrystal();

            } else if ("Stop".equals(ae.getActionCommand())) { // Stop button event
                lblStatus.setText("Simulation stopped . Thank you for using!!");
                log.info("Simulation stopped . Thank you for using the simulation ... !!");

                genCount.setText("0");
                caCrystalSet.stopThread();
                caCrystalSet.setVisible(false);

                // Enabling nConfig as the user is done/stopped the simulation
                for (Component component : nConfig.getComponents()) {
                    component.setEnabled(true);
                }

                // Disabling nPlay as user is done with the simulation
                for (Component component : nPlay.getComponents()) {
                    component.setEnabled(false);
                }

            }

        }

        // Default Window events

        @Override
        public void windowClosing(WindowEvent e) {
            log.warning("Window closed. Simulation is stopped...");
            System.exit(0);
        }

        @Override
        public void windowOpened(WindowEvent e) {
            log.info("Window Opened. Welcome to Crystal Growth Simulation World ....");

        }

        @Override
        public void windowClosed(WindowEvent e) {
            log.info("Window Closed...");

        }

        @Override
        public void windowIconified(WindowEvent e) {
            log.info("Window iconified...");

        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            log.info("Window Deiconified...");

        }

        @Override
        public void windowActivated(WindowEvent e) {
            log.info("Window Activated...");

        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            log.info("Window DeActivated...");

        }

}

