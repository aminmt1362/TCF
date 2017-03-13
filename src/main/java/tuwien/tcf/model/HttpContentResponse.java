/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author amin
 */
public class HttpContentResponse extends ResourceSupport {

    private final String content;
    public static final String ContentApproved = "Table content Approved";
    public static final String ContentNotApproved = "Table format is not correct.";
    public static final String ScoreValue = "Score value is: %s!";

    @JsonCreator
    public HttpContentResponse(@JsonProperty("content") String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
