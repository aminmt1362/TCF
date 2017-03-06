/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    public void calculateScoring() {
        
    }
    
    public void cellScoring() {
        
    }
    
    public void columnScoring() {
        
    }
    
    public void rowScoring() {
        
    }
    
    public void structureScoring() {
        
    }
    
}
