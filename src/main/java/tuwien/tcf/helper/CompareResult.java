/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amin
 */
public final class CompareResult {

    private Map<String, List<String>> groundTruthTable = new LinkedHashMap<>();
    private Map<String, List<String>> userTable = new LinkedHashMap<>();
    private Integer groundTruthTableCellCounter = 0;
    private Integer userTableCounter = 0;

    /**
     * @return the groundTruthTable
     */
    public Map<String, List<String>> getGroundTruthTable() {
        return groundTruthTable;
    }

    /**
     * @param groundTruthTable the groundTruthTable to set
     */
    public void setGroundTruthTable(Map<String, List<String>> groundTruthTable) {
        this.groundTruthTable = groundTruthTable;
    }

    /**
     * @return the userTable
     */
    public Map<String, List<String>> getUserTable() {
        return userTable;
    }

    /**
     * @param userTable the userTable to set
     */
    public void setUserTable(Map<String, List<String>> userTable) {
        this.userTable = userTable;
    }

    /**
     * @return the groundTruthTableCellCounter
     */
    public Integer getGroundTruthTableCellCounter() {
        return groundTruthTableCellCounter;
    }

    /**
     * @param groundTruthTableCellCounter the groundTruthTableCellCounter to set
     */
    public void setGroundTruthTableCellCounter(Integer groundTruthTableCellCounter) {
        this.groundTruthTableCellCounter = groundTruthTableCellCounter;
    }

    /**
     * @return the userTableCounter
     */
    public Integer getUserTableCounter() {
        return userTableCounter;
    }

    /**
     * @param userTableCounter the userTableCounter to set
     */
    public void setUserTableCounter(Integer userTableCounter) {
        this.userTableCounter = userTableCounter;
    }
    
    
}
