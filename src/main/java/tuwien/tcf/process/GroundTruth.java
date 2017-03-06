/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.process;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuwien.tcf.AppProperties;
import tuwien.tcf.helper.Utils;
import tuwien.tcf.model.RCell;
import tuwien.tcf.model.RowCell;
import tuwien.tcf.model.RowData;
import tuwien.tcf.model.Table;

/**
 *
 * @author amin
 */
@Service
public class GroundTruth {

    private final AppProperties properties;

    private Map<String, List<Table>> groundTruthTables = new HashMap<>();
    private List<JsonNode> groundTruthNodes = new ArrayList<>();
    private List<JSONObject> groundTruthObjects = new ArrayList<>();
    private List<JsonObject> groundTruthGObjects = new ArrayList<>();

    @Autowired
    public GroundTruth(AppProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    private void loadAllTables() throws IOException {
        // Read Groundtruth Files
        try (Stream<Path> paths = Files.walk(Paths.get(this.properties.getGroundTruthPath()))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        String jsonTxt = Utils.readFile(filePath.toString(), StandardCharsets.UTF_8);
                        System.out.println("Processing file " + filePath.toString());

                        ObjectMapper mapper = new ObjectMapper();
                        JsonFactory factory = mapper.getFactory(); // since 2.1 use mapper.getFactory() instead
                        com.fasterxml.jackson.core.JsonParser jp = factory.createParser(jsonTxt);
                        JsonNode actualObj = mapper.readTree(jp);

                        JsonParser parser = new JsonParser();

                        JsonElement jelement = parser.parse(jsonTxt);
                        JsonObject jsobj = jelement.getAsJsonObject();
                        getGroundTruthGObjects().add(jsobj);
                        
                        this.groundTruthNodes.add(actualObj);

                        JSONObject json = new JSONObject(jsonTxt);
                        this.groundTruthObjects.add(json);
                        /*Table newTable = mapJsonToObject(json);
                        List<Table> tables = this.groundTruthTables.get(newTable.getFileID());

                        // GET LIST OF ALL TABLES, REPLACE IT WITH NEWLY ADDED TABLE INTO the list of tables of one file.
                        if (tables != null && tables.size() > 0) {
                            tables.add(newTable);
                            this.groundTruthTables.replace(newTable.getFileID(), tables);
                        } else {
                            tables = new ArrayList<>();
                            tables.add(newTable);
                            this.groundTruthTables.put(newTable.getFileID(), tables);
                        }
                        
                         */
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(GroundTruth.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(GroundTruth.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(GroundTruth.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
        }
        printGroundTruhTables();
    }

    /**
     * Convert JSONObject into table model
     *
     * @param jsonObject
     * @return
     */
    private Table mapJsonToObject(JSONObject jsonObject) {
        Table retTable = new Table();
        retTable.setFileID(jsonObject.getString("fileid"));
        retTable.setHeader(jsonObject.getString("header"));
        JSONArray jsona = jsonObject.getJSONArray("rows");
        for (Object object : jsona) {
            JSONObject tableRow = (JSONObject) object;
            Iterator<?> keys = tableRow.keys();

            RowData rowData = new RowData();
            RowCell col;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                col = new RowCell();
                col.setKey(key);

                if (tableRow.get(key) instanceof JSONArray) {

                }
                col.setValue(tableRow.getString(key));
                rowData.addRowColumn(col);
            }
            retTable.addRowData(rowData);
        }

        return retTable;
    }

    private void printGroundTruhTables() {

        for (JSONObject groundTruthNode : groundTruthObjects) {
            System.out.println(groundTruthNode.toString());

            HashMap hm = new HashMap();
            Map<String, String> list = parse(groundTruthNode, hm);

            Set set = hm.entrySet();

            Iterator i = set.iterator();

            // Display elements
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                System.out.print(me.getKey() + ": ");
                System.out.println(me.getValue());
            }

//            Iterator<Entry<String, JsonNode>> nodes = groundTruthNode.fields();
//            Iterator<String> nodeNames = groundTruthNode.fieldNames();
//            while(nodeNames.hasNext()) {
//                System.out.println(nodeNames.next());
//            }
//            while (nodes.hasNext()) {
//                Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
//
//                JsonNode jn = entry.getValue();
//                jn.fields();
//                System.out.println("key --> " + entry.getKey() + " value-->" + entry.getValue());
//            }
        }

    }

    private void printGroundTruthNodes() {
        for (JsonNode groundTruthNode : groundTruthNodes) {
            HashMap hm = new HashMap();
            Map<String, String> list = parseN(groundTruthNode, hm);

        }
    }

    public Map<String, String> parseN(JsonNode json, Map<String, String> out) throws JSONException {

        Iterator<Entry<String, JsonNode>> nodes = json.fields();

        while (nodes.hasNext()) {
            Entry<String, JsonNode> node = nodes.next();
            String key = node.getKey();
            JsonNode newNode = node.getValue();
            Iterator<Entry<String, JsonNode>> newNodes = newNode.fields();
            newNodes.hasNext();
            Entry<String, JsonNode> newmNode = newNodes.next();

        }

        return out;
    }

    public Map<String, String> parse(JSONObject json, Map<String, String> out) throws JSONException {
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String val = null;
            if (json.getJSONObject(key) instanceof JSONObject) {
                JSONObject value = json.getJSONObject(key);
                parse(value, out);
            } else {
                val = json.getString(key);
            }

            if (val != null) {
                out.put(key, val);
            }
        }
        return out;
    }

    /**
     * @return the groundTruthGObjects
     */
    public List<JsonObject> getGroundTruthGObjects() {
        return groundTruthGObjects;
    }
}
