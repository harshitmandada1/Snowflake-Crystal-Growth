package edu.neu.csye6200.ca;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * @author Harshit
 *        ClassName : CACrystalSet
 *        Description: Stores CACrystalSets in a
 *        Map which are generated using rule provided by the user.
 *        Also helps in setting up Crystals in the UI.
 *        Valuble Output: CrystalRecord which cntains all the crystals
 *        This also acts as a JPanel where the simulation is done.
 */

public class CACrystalSet extends JPanel implements Runnable {

    private HashMap<Integer, CACrystal> CrystalRecord; // To store all the previous generations crystal forms
    private int generationsCount; // To keep track of all the generations


    private int genearationLimit; // Adding a UI functionality for the user to select the number of generations needed

    private int sleepTime; // Adding a UI functionality for the user to select the sleep time between generations

    private CACrystal previousCrystal; // Previous Crystal in the CrystalSet

    private Thread cellThread; // Thread which executes for the simulation


    private boolean completeFlag; //TO notify complete
    private boolean paused; // To pause the simulation
    private boolean stopped;// To stop the simulation.
    private boolean rewind; // To rewind the simulation.

 //Creating a Hexagonal Grid
    private static final long serialVersionUID = 1L;
    private static final double H = Math.sqrt(3) / 2;

    static class Hexagon {
        final int row;
        final int col;
        final double sideLength;
        public Hexagon(int r, int c, double a) {
            this.row = r;
            this.col = c;
            this.sideLength = a;
        }

        double getCenterX() {
            return 2 * H * sideLength * (col + (row % 2) * 0.5);
        }

        double getCenterY() {
            return 3 * sideLength / 2  * row;
        }

        void foreachVertex(BiConsumer<Double, Double> f) {
            double cx = getCenterX();
            double cy = getCenterY();
            f.accept(cx + 0, cy + sideLength);
            f.accept(cx - H * sideLength, cy + 0.5 * sideLength);
            f.accept(cx - H * sideLength, cy - 0.5 * sideLength);
            f.accept(cx + 0, cy - sideLength);
            f.accept(cx + H * sideLength, cy - 0.5 * sideLength);
            f.accept(cx + H * sideLength, cy + 0.5 * sideLength);
        }
    }



    // For Logging application process to the console.
    private static Logger log = Logger.getLogger(CrystalMainApp.class.getName());

   //Default Constructor
    public CACrystalSet(){

    }


    // Constructor to initialize the CACrystalSet
    public CACrystalSet(CACrystal caCrystal, int genearationLimit, int sleepTime) {

        // Initializing the properties of a CrystalSet
        initializeCrystalSet();
        this.previousCrystal = caCrystal;
        this.genearationLimit = genearationLimit;
        this.sleepTime = sleepTime;

        // Adding the Initial Region to the Map.
        addCrystalToMap(generationsCount, previousCrystal);


    }

    // Routine to initialize all the properties of a CrystalSet.
    private void initializeCrystalSet() {

        this.CrystalRecord = new HashMap<Integer, CACrystal>();
        this.generationsCount = 0;
        this.completeFlag = false;
        setOpaque(false);
    }

    // Function which gets triggered when the thread is executed to process the Simulation.
    @Override
    public void run() {

        // We check all the possible conditions before generating the Crystal.
        while (!paused && generationsCount != genearationLimit && !completeFlag && !stopped) {

            CACrystal nextCrystal;

            // To check if user is rewinding the simulation
            if (rewind && generationsCount > 0) {
                previousCrystal = getCrystalRecord().get(generationsCount - 1);  //If user is rewinding the simulation we remove each crystal form from the Hashmap and repaint
                removeRegionFromMap(generationsCount);
                generationsCount--;
                repaint(); // Paints the new state of the region using paintComponent.

            } else if (rewind && generationsCount == 0) { // To pause the simulation when user goes to initial state.
                paused = true;

            } else { // Forward simulation
                nextCrystal = previousCrystal.createNewCrystal();
                generationsCount++;
                addCrystalToMap(generationsCount, nextCrystal); // Once done, the Crystal is added to the MAP
                previousCrystal = nextCrystal;
                repaint(); // Paints the new state of the region using paintComponent.
            }

            CrystalMainApp.genCount.setText(generationsCount + "");


            try {
                Thread.sleep(this.sleepTime); // customized sleep time
            } catch (InterruptedException e) {
                log.severe("The thread execution was interrupted. Details : " + e.toString());
                break;
            }
        }

        // Buttons and Flags while simulating
        if (stopped) {
            stopped = false;
        } else if (generationsCount < genearationLimit && paused) {
            if (rewind && generationsCount == 0) {
                rewind = false;
                log.info("Simulation paused we reached the initial state...");
                CrystalMainApp.pauseButton.setEnabled(false);
                CrystalMainApp.startButton.setEnabled(true);

            } else if (rewind) {
                log.info("Simulation paused while user was rewinding...");
                CrystalMainApp.startButton.setEnabled(true);
                CrystalMainApp.rewindButton.setEnabled(true);
            } else {
                log.info("Simulation Paused...");
            }

        } else if (completeFlag) {
            if (generationsCount == genearationLimit) {


                log.info("Simulation completed Successfully...");
                CrystalMainApp.pauseButton.setEnabled(false);
                CrystalMainApp.startButton.setEnabled(false);

            } else {

                log.info("Simulation reached maximum generation Limit...");

                CrystalMainApp.pauseButton.setEnabled(false);
                CrystalMainApp.startButton.setEnabled(false);
            }
        }
    }


    // Function for next Crystals
    public void nextCrystal() {
        cellThread = new Thread(this, "CrystalSimThread"); // Starts a new Thread
        this.paused = false;
        this.rewind = false;
        cellThread.start(); // Calls run method of the thread
    }

    // Function to retrieve previous Crystals.
    public void rewindCrystal() {
        cellThread = new Thread(this, "CrystalSimThread"); // Starts a new Thread
        this.paused = false;
        this.rewind = true;
        cellThread.start(); // Calls run method of the thread
    }






    // Routine to update the colors or paint the state of the cell.
    public void paintComponent(Graphics g) {

        try {

            final int[] xs = new int[6];
            final int[] ys = new int[6];
            final Hexagon[][] grid = new Hexagon[previousCrystal.getCrystalRows()][previousCrystal.getCrystalColumns()];
            for(int row = 0; row < previousCrystal.getCrystalRows(); row++) {
                for (int col = 0; col < previousCrystal.getCrystalColumns(); col++) {
                    grid[row][col] = new Hexagon(row, col, 10);

                            final int[] i = {0};

                    grid[row][col].foreachVertex((x, y) -> {
                                xs[i[0]] = (int) ((double) x);
                                ys[i[0]] = (int) ((double) y);
                                i[0]++;
                            });
                           if (previousCrystal.getCellAt(row,col).getCellState() == CACellState.VAPOUR) {
                                g.setColor(Color.black);
                                g.fillPolygon(xs, ys, 6);
                            } else if (previousCrystal.getCellAt(row,col).getCellState() == CACellState.FROZEN){
                                g.setColor(Color.white);
                               g.fillPolygon(xs, ys, 6);
                            }else{
                               g.setColor(Color.red);
                               g.fillPolygon(xs, ys, 6);
                           }
                    g.drawPolygon(xs, ys, 6);



                        }
                    }

        } catch (Exception e) {
            log.severe("Some exception occurred while setting up graphics. Details : " + e.toString());
        }

    }

    // Pausing the thread when User enters.
    public void pauseThread() {
        paused = true;
    }

    // Going to the previous generation.
    public void rewindThread() {
        rewind = true;
    }

    // Terminating the simulation.
    public void stopThread() {
        stopped = true;
    }

    // Helper Method to add Crystals to the Map
    public void addCrystalToMap(int currentGen, CACrystal currentRegion) {
        CrystalRecord.put(currentGen, currentRegion);
    }

    // Helper Method to remove Crystals from the Map
    public void removeRegionFromMap(int currentGen) {
        CrystalRecord.remove(currentGen);
    }








  //Getters and Setters

    /**
     * @return the CrystalRecord
     */
        public HashMap<Integer, CACrystal> getCrystalRecord() {
        return CrystalRecord;
    }

    /**
     * @param crystalRecord the maRegionRecord to set
     */

    public void setCrystalRecord(HashMap<Integer, CACrystal> crystalRecord) {
        CrystalRecord = crystalRecord;
    }


    /**
     * @return the generationCount
     */

    public int getGenerationsCount() {
        return generationsCount;
    }


    /**
     * @param generationsCount the generationCount to set
     */

    public void setGenerationsCount(int generationsCount) {
        this.generationsCount = generationsCount;
    }

    /**
     * @return the GenLimit
     */

    public int getGenearationLimit() {
        return genearationLimit;
    }

    /**
     * @param genearationLimit to set
     */

    public void setGenearationLimit(int genearationLimit) {
        this.genearationLimit = genearationLimit;
    }


    /**
     * @return the SleepTime
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * @param sleepTime to set
     */

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * @return the CACrystal
     */

    public CACrystal getPreviousCrystal() {
        return previousCrystal;
    }

    /**
     * @param previousCrystal to set
     */


    public void setPreviousCrystal(CACrystal previousCrystal) {
        this.previousCrystal = previousCrystal;
    }


    /**
     * @return the CEllthread
     */



    public Thread getCellThread() {
        return cellThread;
    }


    /**
     * @param cellThread to set
     */

    public void setCellThread(Thread cellThread) {
        this.cellThread = cellThread;
    }

    /**
     * @return the completeFlag
     */

    public boolean isCompleteFlag() {
        return completeFlag;
    }


    /**
     * @param completeFlag the completeFlag to set
     */
    public void setCompleteFlag(boolean completeFlag) {
        this.completeFlag = completeFlag;
    }


    /**
     * @return the paused
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @param paused the paused to set
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * @return the stopped
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * @param stopped the stopped to set
     */
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }


    /**
     * @return the rewind
     */
    public boolean isRewind() {
        return rewind;
    }

    /**
     * @param rewind the rewind to set
     */

    public void setRewind(boolean rewind) {
        this.rewind = rewind;
    }

}









