/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.WuzzafDataSet;

import com.company.resources.JobResources;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 *
 * @author MIDO
 */
@Component
@ApplicationPath("/api")
public class WuzzafAppConf extends ResourceConfig {
    public WuzzafAppConf(){
        register(JobResources.class);
    }
    
    
}
