package edu.neu.csye6200.ca;
import java.util.logging.Logger;

/**
 * @author Harshit
 *         ClassName : CARule
 *         Description : CARule determines the state of the cell
 *         depending on the neighboring cell.
 *         Valuable Outcome : CACellState of the next cell which will
 *         be useful to determine the state of next cell.
 */


/*
Using ENUM for naming the rules because a new rule can be added easily without disturbing the code
 */

enum Rules{
SIMPLEICE, MEDIUMICE, EXTREMEICE
}

public class CARule extends  CACell {

    private Rules rules; // for the rule names


    // For Logging application process to the console so that we can find errors while the application is running.
    private static Logger log = Logger.getLogger(CACell.class.getName());


    public CARule(Rules rules, CACrystal crystal, CACellState caCellState) {

        super(crystal, caCellState); //calling the super class
        this.rules = rules;

    }


    // Function which navigates to different rules.
    public CACellState getNextCellState() {

        if (rules == Rules.SIMPLEICE) {
            return getSimpleIceState();

        } else if (rules == Rules.MEDIUMICE) {
            return getMediumIceState();

        } else if (rules == Rules.EXTREMEICE) {
            return getExtremeIceState();

        } else {
            return getCellState();
        }
    }


    /*
     * RuleName --> SimpleIce
     * Rule Description --> If the cell has atleast 1 neighboring cells that are Frozen, cell should become Frozen.
     * This follows a very basic crystal structure where it just forms a new cell if there is a frozen state anywhere around.
     * Reference: Stephen Wolfram: A New Kind of Science
     * Colors : Frozen (white), Vapour (black)
     */

    public CACellState getSimpleIceState() {
        if (getNeighborsCount(CACellState.FROZEN) > 0) {

            return CACellState.FROZEN;

        } else {
            return getCellState();
        }

    }


    /*
     * RuleName --> MediumIce
     * Rule Description --> If the cell has only 1 neighboring cells that are Frozen, cell should become Frozen.
     * This rule displays the most similar crystal form. It takes into account the neighbours and checks if there is
     * only one neighbour that is Frozen and not more than that.
     * Reference: Stephen Wolfram: A New Kind of Science
     * Colors : Frozen (white), Vapour (black)
     */

    public CACellState getMediumIceState() {
        if (getNeighborsCount(CACellState.FROZEN) == 1) {

            return CACellState.FROZEN;

        } else {
            return getCellState();
        }


    }


    /*
     * RuleName --> ExtremeIce
     * Rule Description --> Case1 - If the cell is in Vapour state and
     * has 1 Frozen neighbors, the cell becomes Frozen.
     * Case2 - If the cell is Frozen and has 4 or more Frozen neighbours it goes to Melting state.
     * Case3 - Any Melting Cell goes to Vapour state.
     * This follows a complex algorithm where we take into account the heat generated when a cell is added to the crystal.
     * So when there are 4 or more Frozen neighbours surrounding a cell, they produce enough heat to melt that
     * cell. So it goes into melting state which eventually evaporates and becomes a vapour.
     * Colors : Frozen (white), Vapour (black), Melting (red)
     */
    public CACellState getExtremeIceState() {


        if (getCellState().equals(CACellState.VAPOUR) && getNeighborsCount(CACellState.FROZEN) == 1 && !(getCellState().equals(CACellState.MELTING))) {
            return CACellState.FROZEN;
        } else if (getCellState().equals(CACellState.FROZEN) && getNeighborsCount(CACellState.FROZEN) >= 4) {
            return CACellState.MELTING;
        } else if (getCellState().equals(CACellState.MELTING)) {
            return CACellState.VAPOUR;
        } else {
            return getCellState();
        }


    }
}



