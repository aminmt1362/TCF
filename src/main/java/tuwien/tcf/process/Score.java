/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.process;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuwien.tcf.helper.Utils;
import tuwien.tcf.model.TableModel;

/**
 *
 * @author amin
 */
@Service
public final class Score {

    @Autowired
    GroundTruth groundTruth;

    @Autowired
    UserTables userTables;
    
    private Integer ScoringValue = 0;

    public void calculateScoring() {
        cellScoring();
    }

    public void cellScoring() {
        Logger.getLogger(Score.class.getName()).log(Level.INFO, "Start calculating Cell Scoring");

        // get first userTable table and find the same in groundTruth
        // Start comparing cells
        List<TableModel> gt = groundTruth.getGroundTruthTables();
        for (TableModel userTable : userTables.getUserTables()) {
            String fileName = userTable.getFileID();
            Integer fileId = userTable.getTableCounter();
            Predicate<TableModel> predicate = c-> c.getFileID().equals(fileName);
            TableModel gtTable = gt.stream().filter(predicate).findFirst().get();
            if(gtTable == null) {
                ScoringValue += 0;
                return;
            }
            
            // Compare row cells
            Utils.compareJsonRows(gtTable.getRows(), userTable.getRows());
            
            System.out.println();
            
        }
    }

    public void columnScoring() {
        Logger.getLogger(Score.class.getName()).log(Level.INFO, "Start calculating Column Scoring");
    }

    public void rowScoring() {
        Logger.getLogger(Score.class.getName()).log(Level.INFO, "Start calculating Row Scoring");
    }

    public void structureScoring() {
        Logger.getLogger(Score.class.getName()).log(Level.INFO, "Start calculating Structure Scoring");
    }

}
