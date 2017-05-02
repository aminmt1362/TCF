/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.process;


import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import tuwien.tcf.model.TableModel;


/**
 *
 * @author amin
 */
@Service
public class UserTables {
    private List<TableModel> userTables = new ArrayList<>();
    
    public void addUserTable(TableModel userTable) {
        this.getUserTables().add(userTable);
    }

    /**
     * @return the userTables
     */
    public List<TableModel> getUserTables() {
        return userTables;
    }
    
    /**
     * Delete all tables
     */
    public void deleteAll() {
        this.userTables.clear();
    }
}
