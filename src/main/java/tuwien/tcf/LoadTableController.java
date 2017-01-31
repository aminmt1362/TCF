/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuwien.tcf;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author amin
 */
@RestController
@RequestMapping(value="/{userid}/tableprocessing")
public class LoadTableController {
       
    @RequestMapping(value="/importtable", method = POST)
    public HttpEntity<LoadTable> loadTable(
            @RequestBody LoadTable loadtable) {

       System.out.println(loadtable.getContent());
        //greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());

        return new ResponseEntity(HttpStatus.OK);
    }
}
