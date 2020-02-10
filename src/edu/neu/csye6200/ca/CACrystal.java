package edu.neu.csye6200.ca;

import java.util.logging.Logger;

/**
 * @author Harshit
 *         ClassName : CACrystal
 *         Description : Holds 2D Array of CACell and
 *         contains methods to create next crystal shapes.
 *         Valuable Output : Creates future CACrystals by
 *         utilizing methods in CARule using current CACrystal.
 *
 */

public class CACrystal{

    private Rules rules; // holds rules
    public CACell[][] cells; //holds the cells which contain the state
    private int crystalRows; // the rows of the crystal grid
    private int crystalColumns; //the columns of the crystal grid
    private boolean isLocked;



    // For Logging application process to the console.
    private static Logger log = Logger.getLogger(CACrystal.class.getName());


    //Default Constructor
    public CACrystal(){

    }

    //Creating a CACrystal object using the values of previousCrystal shape
    public CACrystal(CACrystal previousCrystal){
        rules = previousCrystal.rules;
        crystalRows = previousCrystal.crystalRows;
        crystalColumns = previousCrystal.crystalColumns;
        cells = new CACell[crystalRows][crystalColumns];
        CreateGrid();

    }

   // Initialize the current CACrystal in the contructor
    public CACrystal( Rules rules, int crystalRows , int  crystalColumns){
        this.rules = rules;
        this.crystalRows = crystalRows;
        this.crystalColumns = crystalColumns;
        this.cells = new CACell[crystalRows][crystalColumns];

        CreateGrid();

    }


    //Function to load the grid with cells
    public void CreateGrid(){
        for(int i =0; i<crystalRows; i++){
            for (int j=0; j<crystalColumns; j++){
                CACell caCell = new CARule(this.rules,this, CACellState.VAPOUR); //the Cells are initialized based on the rule
                caCell.setCellPosX(i);
                caCell.setCellPosY(j);
                caCell.setCrystal(this);
                this.cells[i][j] = caCell;



            }
        }
        //We are making some cell active depending on the rules.

        if(this.rules == Rules.EXTREMEICE) {
            this.cells[25][41].setCellState(CACellState.FROZEN);
             this.cells[25][42].setCellState(CACellState.FROZEN);
        }
        else{
            this.cells[25][41].setCellState(CACellState.FROZEN);
        }



    }

    /*
     * A function which creates next Crystal based on previous Crystals
     * attributes.
     */
    public CACrystal createNewCrystal(){
        //First create a copy of current Crystal
        CACrystal newCrystal = new CACrystal(this);
        CACellState[][] newCellStates;

        try {

            // Calling nextCellStates using previousCrystal Object to determine current state.
            newCellStates = nextCellStates();

            // Looping through each cell to determine the neighbors state and deciding the cell's state based on the Rules.

            for (int i = 0; i < getCrystalRows(); i++) {
                for (int j = 0; j < getCrystalColumns(); j++) {
                    newCrystal.getCellAt(i, j).setState(newCellStates[i][j]);
                }
            }
        }catch (Exception e){
            log.severe("Exception occured while forming the Crystal : " + e.toString());
        }
        return newCrystal;
    }




    // A function which acts as a helper to get the location of the cell at specific location
    public CACell getCellAt(int row, int col) {
        if ((row < 0) || (row >= getCrystalRows())) {
            throw new RuntimeException("Invalid Row");
        }
        if ((col < 0) || (col >= getCrystalColumns())) {
            throw new RuntimeException("Invalid Column");
        }
        return cells[row][col];
    }




    /*
     * Next Cell States are calculated looping through each cell in the Crystal, then
     * we find the FROZEN/VAPOUR/MELTING neighbors and based on the rule, the state is
     * decided.
     */

    public CACellState[][] nextCellStates() {

        CACellState[][] nextStates = new CACellState[getCrystalRows()][getCrystalColumns()];

        for (int i = 0; i < getCrystalRows(); i++) {
            for (int j = 0; j < getCrystalColumns(); j++) {
                nextStates[i][j] = getCellAt(i, j).getNextCellState();

            }
        }

        return nextStates;
    }




    //Getters and setters

    /**
     * @return the rules
     */
    public Rules getRules() {
        return rules;
    }



    /**
     * @param rules the rules to set
     */
    public void setRules(Rules rules) {
        this.rules = rules;
    }


    /**
     * @return the crystalRows
     */
    public int getCrystalRows() {
        return crystalRows;
    }


    /**
     * @param crystalRows the crystalRows to set
     */
    public void setCrystalRows(int crystalRows) {
        this.crystalRows = crystalRows;
    }


    /**
     * @return the crystalColumns
     */
    public int getCrystalColumns() {
        return crystalColumns;
    }


    /**
     * @param crystalColumns the crystalColumns to set
     */
    public void setCrystalColumns(int crystalColumns) {
        this.crystalColumns = crystalColumns;
    }



    /**
     * @return the isLocked
     */
    public boolean isLocked() {
        return isLocked;
    }


    /**
     * @param isLocked the isLocked to set
     */
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

}
