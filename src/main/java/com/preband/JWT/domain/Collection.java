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
    private String collectionImageUrl;
    private int soldCount;
    private int stockCount;
    private double rating;
    private double price;
    private Date addDate;

    public Collection(Long id, String gameId, String gameName, String[] systemRequirements, String[] developerDetail, String[] gameTypes, String collectionImageUrl, int soldCount, int stockCount, double rating, double price, Date addDate) {
        this.id = id;
        this.gameId = gameId;
        this.gameName = gameName;
        this.systemRequirements = systemRequirements;
        this.developerDetail = developerDetail;
        this.gameTypes = gameTypes;
        this.collectionImageUrl = collectionImageUrl;
        this.soldCount = soldCount;
        this.stockCount = stockCount;
        this.rating = rating;
        this.price = price;
        this.addDate = addDate;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String[] getSystemRequirements() {
        return systemRequirements;
    }

    public void setSystemRequirements(String[] systemRequirements) {
        this.systemRequirements = systemRequirements;
    }

    public String[] getDeveloperDetail() {
        return developerDetail;
    }

    public void setDeveloperDetail(String[] developerDetail) {
        this.developerDetail = developerDetail;
    }

    public String[] getGameTypes() {
        return gameTypes;
    }

    public void setGameTypes(String[] gameTypes) {
        this.gameTypes = gameTypes;
    }

    public String getCollectionImageUrl() {
        return collectionImageUrl;
    }

    public void setCollectionImageUrl(String collectionImageUrl) {
        this.collectionImageUrl = collectionImageUrl;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
}



