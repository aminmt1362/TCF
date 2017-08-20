/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author amin
 */
public class Utils {

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void compareJsonRows(JsonObject t1, JsonObject t2) {
        Set<Map.Entry<String, JsonElement>> t1Entries = t1.entrySet();
        Iterator t1EntriesIterator = t1Entries.iterator();
        while (t1EntriesIterator.hasNext()) {
            Map.Entry me = (Map.Entry) t1EntriesIterator.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
            JsonElement j2element = t2.get(me.getKey().toString());
            
            j2element.getAsString();
        }
    }
    
    
    
    
    // GET ALL FIELDS AS WELL AS ARRAYS IN A FLAT LIST
}
