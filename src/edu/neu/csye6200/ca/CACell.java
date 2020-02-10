package edu.neu.csye6200.ca;
import java.util.logging.Logger;

/**
 * @author Harshit
 *         ClassName : CACell (Abstract)
 *         Description : Contains CACellState in each CACell and
 *         determines Neighbor Cell Count for the desired state.
 *         Valuable Outcome: Gets the Neighbor Count based on the
 *         CACellState for which the Rule is looking for and forms
 *         as a base class for CARule. Also contains abstract methods
 *         which are overridden by CARule which gives the next Cell
 *         State on the neighbors.
 */


/*
Using ENUM for naming the cellstates because a new state can be added easily without disturbing the code
 */

enum CACellState{

   FROZEN , VAPOUR, MELTING;

}
public abstract class CACell {
    private CACellState cellState; //Stores the CACellState for each CACell Object.
    public CACrystal crystal; //To determine a cells particular region in the crystal
    public static int cellcount = 0; // used for intiallizing cellstates
    private int CellPosX; // stores the cell x coordinate i.e rows position
    private int CellPosY; // stores the cell y coordinate i.e columns position
   // private int temperature; //stores the temperature of the cell



    // For Logging application process to the console.
    private static Logger log = Logger.getLogger(CACell.class.getName());

    // Default constructor
    public CACell() {

    }

    //intializing cellstates from CARule

    public CACell(CACrystal crystal, CACellState cellState) {

        // initializing the current crystal to which the cell belongs
        this.crystal = crystal;
          this.cellState = cellState;


        cellcount++;
    }

    /*
     * Implementation is provided by extending class (CARule)
     */
    public abstract CACellState getNextCellState();


    /*
     * Sets the cell's current state if the new state is different
     * from the current state.
     */
    public void setState(CACellState state) {
        if (cellState != state)
            cellState = state;
    }


    /*
     * Function to calculate the number of neighbors with a particular state for the
     * current cell. State to look after is provided as Input.
     */

    public int getNeighborsCount(CACellState state) {

        int neighbours = 0;
        try{
            int colstartPos, colendPos;

            for(int rowcounter = -1; rowcounter < 2; rowcounter++){
                colstartPos = rowcounter == 0 ? -1 : (CellPosX % 2 == 0? -1 : 0);
                colendPos = rowcounter == 0 ? 3 : 2;
                for(int colcounter = colstartPos; colcounter < colstartPos + colendPos; colcounter++)
                {
                    if(isNeighbour(rowcounter, colcounter, state))
                        neighbours++;
                }

            }
        } catch (Exception e){
           log.severe("Exception occured while getting neighbour: " + e.toString());
            neighbours = 0 ;
        }
        return neighbours;

    }

    private boolean isNeighbour(int rowCounter, int colCounter, CACellState state){
        if (CellPosX + rowCounter >= 0 && CellPosX + rowCounter <getCrystal().getCrystalRows() && CellPosY + colCounter >= 0 && CellPosY + colCounter < getCrystal().getCrystalColumns()) {
            if (getCrystal().getCellAt(CellPosX + rowCounter, CellPosY + colCounter).getCellState().compareTo(state)==0){

                if (!(rowCounter == 0 && colCounter == 0))
                    return true;
            }
        }
        return false;

        }









    //Getters and setters

    /**
     * @return the cellState
     */
    public CACellState getCellState() {
        return cellState;
    }

    /**
     * @return the cellPosX
     */

    public int getCellPosX() {
        return CellPosX;
    }


    /**
     * @param cellPosX the cellPosX to set
     */

    public void setCellPosX(int cellPosX) {
        CellPosX = cellPosX;
    }

    /**
     * @return the cellPosY
     */

    public int getCellPosY() {
        return CellPosY;
    }


    /**
     * @param cellPosY the cellPosY to set
     */

    public void setCellPosY(int cellPosY) {
        CellPosY = cellPosY;
    }


    /**
     * @param cellState the cellState
     */

    public void setCellState(CACellState cellState) {
        this.cellState = cellState;
    }


    /**
     * @return the crystal
     */

    public CACrystal getCrystal() {
        return crystal;
    }

    /**
     * @param crystal the crystal to set
     */

    public void setCrystal(CACrystal crystal) {
        this.crystal = crystal;
    }



    /**
     * @return the crystal
     */
    public static int getCellcount() {
        return cellcount;
    }


    /**
     * @param cellcount the cellcount to set
     */
    public static void setCellcount(int cellcount) {
        CACell.cellcount = cellcount;
    }

}


