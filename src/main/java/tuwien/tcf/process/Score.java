/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.process;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuwien.tcf.helper.CompareColumnResult;
import tuwien.tcf.helper.CompareResult;
import tuwien.tcf.helper.JsonCompare;
import tuwien.tcf.helper.Utils;
import tuwien.tcf.model.TableModel;

/**
 *
 * @author amin
 */
@Service
public final class Score {

    private final Logger LOGGER = Logger.getLogger(Score.class.getName());

    @Autowired
    GroundTruth groundTruth;

    @Autowired
    UserTables userTables;

    public void calculateScoring() {

        Integer scoreValue = 0;
        Integer maxScoreVal = groundTruth.getGroundTruthTables().size();
        Integer userTableFoundCounter = 0;

        List<TableModel> gt = groundTruth.getGroundTruthTables();
        for (TableModel userTable : userTables.getUserTables()) {
            String fileName = userTable.getFileID();
            Integer tableCounter = userTable.getTableCounter();
            Predicate<TableModel> predicate = c -> (c.getFileID().equals(fileName));
            Predicate<TableModel> prediate2 = c -> c.getTableCounter().equals(tableCounter);
            try {

                TableModel gtTable = gt.stream().filter(predicate).filter(prediate2).findFirst().get();

                Double cellScore = cellScoring(gtTable, userTable);

                Double columnScore = columnScoring(gtTable, userTable);
                
                Double rowScore = rowScoring(gtTable, userTable);

                Double structureScore = structureScoring(gtTable, userTable);
                
                userTableFoundCounter++;
            } catch (Exception ee) {
                LOGGER.log(Level.SEVERE, ee.getMessage());
                // A whole table is missing
                maxScoreVal--;
            }
        }

    }

    /**
     * Calculates the cell scoring
     *
     * @param gtTable GroundTruth table
     * @param userTable User imported table
     * @return
     */
    public Double cellScoring(TableModel gtTable, TableModel userTable) {
        LOGGER.log(Level.INFO, "Start calculating Cell Scoring");

        Double cellScore;
//        // get first userTable table and find the same in groundTruth
//        // Start comparing cells
//        List<TableModel> gt = groundTruth.getGroundTruthTables();
//        for (TableModel userTable : userTables.getUserTables()) {
//            String fileName = userTable.getFileID();
//            Integer tableCounter = userTable.getTableCounter();
//            Predicate<TableModel> predicate = c -> (c.getFileID().equals(fileName));
//            Predicate<TableModel> prediate2 = c -> c.getTableCounter().equals(tableCounter);
//            try {
//        TableModel gtTable = gt.stream().filter(predicate).filter(prediate2).findFirst().get();

        // Compare row cells
        JsonCompare jc = new JsonCompare();
        CompareResult compareResult = jc.compareJsonRows(gtTable.getRows(), userTable.getRows());

        // MAX SCORE BASED ON ALL GT CELLS
        Integer maxScore = compareResult.getGroundTruthTableCellCounter();

        // SUBSTRACT MISSING CELLS
        Integer calcCellScore = maxScore - (compareResult.getGroundTruthTableCellCounter() - compareResult.getUserTableCounter());

        // Compare CEll Content
        Iterator<Entry<String, List<String>>> it = compareResult.getGroundTruthTable().entrySet().iterator();
        Set<Entry<String, List<String>>> userTableCells = compareResult.getUserTable().entrySet();

        while (it.hasNext()) {
            Entry<String, List<String>> gtcell = it.next();

            Predicate<Entry<String, List<String>>> gtSpecificCell = c -> c.getKey().equals(gtcell.getKey());

            try {
                Entry<String, List<String>> userCell = userTableCells.stream().filter(gtSpecificCell).findAny().get();

                for (String cell : gtcell.getValue()) {
                    if (!userCell.getValue().contains(cell)) {
                        calcCellScore--;
                    }
                }
            } catch (Exception ee) {
                // A whole row was not found hence to remove all cells from scoring  
                calcCellScore = calcCellScore - gtcell.getValue().size();

            }
        }

        cellScore = Double.parseDouble(calcCellScore.toString()) / maxScore;

////            Utils.compareJsonRows(gtTable.getRows(), userTable.getRows());
//            } catch (Exception ee) {
//                LOGGER.log(Level.SEVERE, ee.getMessage());
//            }
//            System.out.println("cell scoring result is:" + cellScore);
//
//        }
        return cellScore;
    }

    /**
     * Calculates the column scoring and returns the score value
     *
     * @param gtTable ground truth table
     * @param userTable user imported table
     */
    public Double columnScoring(TableModel gtTable, TableModel userTable) {
        LOGGER.log(Level.INFO, "Start calculating Column Scoring");

        Integer columnScore = 0;

        JsonCompare jc = new JsonCompare();

        CompareColumnResult compareResult = jc.compareColumnJsonRows(gtTable.getRows(), userTable.getRows());

        Integer maxScore = compareResult.getGroundTruthTableCellCounter();

        Integer calcColumnScore = maxScore - (compareResult.getGroundTruthTableCellCounter() - compareResult.getUserTableCounter());

        Iterator<Entry<String, String>> it = compareResult.getGroundTruthTable().entrySet().iterator();
        Set<Entry<String, String>> userTableCells = compareResult.getUserTable().entrySet();

        while (it.hasNext()) {
            Entry<String, String> gtcell = it.next();

            Predicate<Entry<String, String>> gtSpecificCell = c -> c.getKey().equals(gtcell.getKey());

            try {
                Entry<String, String> userCell = userTableCells.stream().filter(gtSpecificCell).findAny().get();

                if (!gtcell.getKey().equals(userCell.getKey())) {
                    calcColumnScore--;
                }

//                for (String cell : gtcell.getKey()) {
//                    if (!userCell.getValue().contains(cell)) {
//                        calcColumnScore--;
//                    }
//                }
            } catch (Exception ee) {
                // A whole row was not found hence to remove all cells from scoring  
                calcColumnScore--;

            }
        }

        return Double.parseDouble(calcColumnScore.toString()) / maxScore;
    }

    /**
     * Calculates the row scoring of user table and returns a double value of
     * the score between 0 and 1
     *
     * @param gtTable : Ground truth table
     * @param userTable : user table
     * @return
     */
    public Double rowScoring(TableModel gtTable, TableModel userTable) {
        LOGGER.log(Level.INFO, "Start calculating Row Scoring");

        Double rowScore;
//        // get first userTable table and find the same in groundTruth

        // Compare row cells
        JsonCompare jc = new JsonCompare();
        CompareResult compareResult = jc.compareJsonRows(gtTable.getRows(), userTable.getRows());

        // MAX SCORE BASED ON ALL GT CELLS
        Integer maxScore = 0;

        // SUBSTRACT MISSING CELLS
//        Integer calcCellScore = maxScore - (compareResult.getGroundTruthTableCellCounter() - compareResult.getUserTableCounter());

        // Compare CEll Content
        Iterator<Entry<String, List<String>>> it = compareResult.getGroundTruthTable().entrySet().iterator();
        Set<Entry<String, List<String>>> userTableCells = compareResult.getUserTable().entrySet();

        // Compare row count
        Integer subtractIncorrectSizeOfRows = 0;
        while (it.hasNext()) {
            Entry<String, List<String>> gtcell = it.next();
            
            if(gtcell.getValue().size() > maxScore) {
                maxScore = gtcell.getValue().size();
            }

            Predicate<Entry<String, List<String>>> gtSpecificCell = c -> c.getKey().equals(gtcell.getKey());

            try {
                Entry<String, List<String>> userCell = userTableCells.stream().filter(gtSpecificCell).findAny().get();

                // Subtract any difference
                if (gtcell.getValue().size() != userCell.getValue().size()) {
//                    if (Math.abs(gtcell.getValue().size() - userCell.getValue().size()) > subtractIncorrectSizeOfRows) {
                        subtractIncorrectSizeOfRows += Math.abs(gtcell.getValue().size() - userCell.getValue().size());
//                    }
                }

            } catch (Exception ee) {
                // A column was not found

            }
        }
        
        

        rowScore = Double.valueOf((maxScore - subtractIncorrectSizeOfRows)) / maxScore;

        return rowScore;
    }

    /**
     * Calculates the structure scoring of Ground truth table with the user user table and returns a score value
     * @param gtTable ground truth table
     * @param userTable user table
     * @return Double score value
     */
    public Double structureScoring(TableModel gtTable, TableModel userTable) {
        LOGGER.log(Level.INFO, "Start calculating Structure Scoring");
        
        Double rowScore;
//        // get first userTable table and find the same in groundTruth

        // Compare row cells
        JsonCompare jc = new JsonCompare();
        CompareResult compareResult = jc.compareJsonRows(gtTable.getRows(), userTable.getRows());

        // MAX SCORE BASED ON ALL GT CELLS
        Integer maxScore = compareResult.getGroundTruthTable().size();
        
        

        // SUBSTRACT MISSING CELLS
//        Integer calcCellScore = maxScore - (compareResult.getGroundTruthTableCellCounter() - compareResult.getUserTableCounter());

        // Calculate maxscore
        Iterator<Entry<String, List<String>>> it = compareResult.getGroundTruthTable().entrySet().iterator();
         while (it.hasNext()) {
            Entry<String, List<String>> gtcell = it.next();
            maxScore += gtcell.getValue().size();
         }
        
        
        Set<Entry<String, List<String>>> userTableCells = compareResult.getUserTable().entrySet();

        // Compare row count
        Integer subtractIncorrectSizeOfRows = 0;
        Integer mainKeySize = 0;
        while (it.hasNext()) {
            Entry<String, List<String>> gtcell = it.next();
            mainKeySize = gtcell.getValue().size();
            
            if(gtcell.getValue().size() > maxScore) {
                maxScore = gtcell.getValue().size();
            }

            Predicate<Entry<String, List<String>>> gtSpecificCell = c -> c.getKey().equals(gtcell.getKey());

            try {
                Entry<String, List<String>> userCell = userTableCells.stream().filter(gtSpecificCell).findAny().get();

                // Subtract any difference
                if (gtcell.getValue().size() != userCell.getValue().size()) {
//                    if (Math.abs(gtcell.getValue().size() - userCell.getValue().size()) > subtractIncorrectSizeOfRows) {
                        maxScore -= Math.abs(gtcell.getValue().size() - userCell.getValue().size());
//                    }
                }

            } catch (Exception ee) {
                // A main key was not found
                maxScore -= mainKeySize;
            }
        }
        
        

        rowScore = Double.valueOf((maxScore - subtractIncorrectSizeOfRows)) / maxScore;

        return rowScore;
    }

}
