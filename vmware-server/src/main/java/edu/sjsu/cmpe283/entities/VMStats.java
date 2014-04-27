package edu.sjsu.cmpe283.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class VMStats {
    @Id
    private String id;
}
