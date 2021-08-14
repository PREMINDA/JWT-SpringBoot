package com.preband.JWT.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


public class Collection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,updatable = false)
    private Long id;
    private String gameId;
    private String gameName;
    private String[] systemRequirements;
    private String[] developerDetail;
    private String[] gameTypes;
    private int soldCount;
    private int stockCount;
    private double rating;
    private double price;
    private Date addDate;
}
