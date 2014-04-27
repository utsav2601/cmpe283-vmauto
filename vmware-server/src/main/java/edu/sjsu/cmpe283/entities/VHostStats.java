package edu.sjsu.cmpe283.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VHostStats {
    @Id
    private String id;
    
}
