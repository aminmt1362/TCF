/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Amin
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    @Value("${app.name}")
    private String name;
    
    @Value("${app.groundtruth.path}")
    private String groundTruthPath;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the groundTruthPath
     */
    public String getGroundTruthPath() {
        return groundTruthPath;
    }

    /**
     * @param groundTruthPath the groundTruthPath to set
     */
    public void setGroundTruthPath(String groundTruthPath) {
        this.groundTruthPath = groundTruthPath;
    }

    }
