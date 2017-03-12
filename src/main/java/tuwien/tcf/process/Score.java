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

    private Integer ScoringValue = 0;

    public void calculateScoring() {
        cellScoring();
    }

    /**
     * Calculates the cell scoring 
     * @return 
     */
    public Integer cellScoring() {
        LOGGER.log(Level.INFO, "Start calculating Cell Scoring");

        Integer cellScore = 0;
        // get first userTable table and find the same in groundTruth
        // Start comparing cells
        List<TableModel> gt = groundTruth.getGroundTruthTables();
        for (TableModel userTable : userTables.getUserTables()) {
            String fileName = userTable.getFileID();
            Integer tableCounter = userTable.getTableCounter();
            Predicate<TableModel> predicate = c -> (c.getFileID().equals(fileName));
            Predicate<TableModel> prediate2 = c -> c.getTableCounter().equals(tableCounter);
            try {
                TableModel gtTable = gt.stream().filter(predicate).filter(prediate2).findFirst().get();
                if (gtTable == null) {
                    ScoringValue += 0;
                    return 0;
                }

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

                cellScore = calcCellScore / maxScore;

//            Utils.compareJsonRows(gtTable.getRows(), userTable.getRows());
            } catch (Exception ee) {
                LOGGER.log(Level.SEVERE, ee.getMessage());
            }
            System.out.println("cell scoring result is:" + cellScore);

        }
        this.ScoringValue = this.ScoringValue + cellScore;
        return cellScore;
    }

    public void columnScoring() {
        LOGGER.log(Level.INFO, "Start calculating Column Scoring");
    }

    public void rowScoring() {
        LOGGER.log(Level.INFO, "Start calculating Row Scoring");
    }

    public void structureScoring() {
        LOGGER.log(Level.INFO, "Start calculating Structure Scoring");
    }

}
