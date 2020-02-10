package edu.neu.csye6200.ca;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.logging.Logger;

/**
 * A sample abstract application class
 * @author Harshit
 *
 */
public abstract class CAApp implements ActionListener, WindowListener {
    protected JFrame frame = null;

    // Used for logging MAApp events in console.
    private static Logger log = Logger.getLogger(CAApp.class.getName());

    public CAApp() {
        initGUI();
    }


    public void initGUI() {
        frame = new JFrame();
        frame.setTitle("CrystalGrowth");

        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame.DISPOSE_ON_CLOSE)

        // Permit the app to hear about the window opening
        frame.addWindowListener(this);

        frame.setLayout(new BorderLayout());

        frame.add(getMainPanel(), BorderLayout.NORTH);

    }

    public abstract JPanel getMainPanel();


    /**
     * A method that uses the Swing dispatch thread to show the UI. This
     * prevents concurrency problems during component initialization.
     */

    public void showUI() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                frame.setVisible(true); // The UI is built, so display it;

            }
        });

    }

    /**
     * Shut down the application
     */
    public void exit() {
        log.info("Crystal Simulation Closed... Bye !!");
        frame.dispose();
        System.exit(0);
    }
}

