/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author amin
 */
public final class JsonCompare {

    private final Map<String, List<String>> json1 = new LinkedHashMap<>();
    private final Map<String, List<String>> json2 = new LinkedHashMap<>();
    Integer cellCounter = 0;

    private String globalLastKey = "";

    /**
     * Compares two tables and return a complete result of comparisons
     *
     * @param t1 Ground truth table
     * @param t2 User Imported Table
     * @return 
     */
    public CompareResult compareJsonRows(JsonObject t1, JsonObject t2) {

        CompareResult compareResult = new CompareResult();

        Set<Map.Entry<String, JsonElement>> t1Entries = t1.entrySet();
        JsonElement rows = t1.get("rows");

        globalLastKey = "rows";

        Map<String, List<String>> gt = new LinkedHashMap<>();
        
        cellCounter = 0;
        
        extractCells(rows, gt);
        compareResult.setGroundTruthTable(gt);
        compareResult.setGroundTruthTableCellCounter(cellCounter);
        
        cellCounter = 0;

        JsonElement rows2 = t2.get("rows");

        globalLastKey = "rows";
        Map<String, List<String>> ut = new LinkedHashMap<>();
        extractCells(rows2, ut);
        
        compareResult.setUserTable(ut);
        compareResult.setUserTableCounter(cellCounter);

        return compareResult;
    }
    
    /**
     * Compares columns of rows from two json tables
     * @param t1
     * @param t2 
     */
    private void compareColumnJsonRows(JsonObject t1, JsonObject t2) {
        
    }

    /**
     * Extracts all cells from json object and puts into an hashmap
     *
     * @param jsonElement
     * @param map
     */
    private void extractCells(JsonElement jsonElement, Map<String, List<String>> map) {

        // Check whether jsonElement is JsonObject or not
        if (jsonElement.isJsonObject()) {
            Set<Entry<String, JsonElement>> ens = ((JsonObject) jsonElement).entrySet();
            if (ens != null) {
                // Iterate JSON Elements with Key values
                for (Entry<String, JsonElement> en : ens) {
                    System.out.println(en.getKey() + " : ");
                    //String val = 
                    globalLastKey = en.getKey();
                    extractCells(en.getValue(), map);

                    //json1.put(en.getKey(), val);
                }
            }
        } // Check whether jsonElement is Arrary or not
        else if (jsonElement.isJsonArray()) {
            JsonArray jarr = jsonElement.getAsJsonArray();
            // Iterate JSON Array to JSON Elements
            this.add(jsonElement.toString(), map);

            for (JsonElement je : jarr) {
                extractCells(je, map);
            }
        } // Check whether jsonElement is NULL or not
        else if (jsonElement.isJsonNull()) {
            // print null
            System.out.println("null");
        } // Check whether jsonElement is Primitive or not
        else if (jsonElement.isJsonPrimitive()) {
            // print value as String
            cellCounter = cellCounter + 1;
            this.add(jsonElement.getAsString(), map);
            System.out.println(jsonElement.getAsString());
        }
    }

    private void add(String newvalue, Map<String, List<String>> cmap) {
        List<String> currentValue = cmap.get(globalLastKey);
        if (currentValue == null) {
            currentValue = new ArrayList<String>();
            cmap.put(globalLastKey, currentValue);
        }
        currentValue.add(newvalue);
    }
}
