/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Amin
 */
@Service
public class MyService {

    private final SolrProperties properties;

    @Autowired
    public MyService(SolrProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void openConnection() {
        System.out.print(this.properties.getPort());
        // ...
    }
}
