package com.example.compoint.model;

import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;

import java.math.BigDecimal;

public class Standup {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String language;
    private String imagePath;
    private UserEntity user;
    private int rating;
    public static Standup toModel(StandupEntity entity) {
        Standup standupModel = new Standup();
        standupModel.setId(entity.getId());
        standupModel.setName(entity.getName());
        standupModel.setDescription(entity.getDescription());
        standupModel.setPrice(entity.getPrice());
        standupModel.setLanguage(entity.getLanguage());
        standupModel.setImagePath(entity.getImagePath());
        standupModel.setRating(entity.getRating());
//        standupModel.setUser(UserEntity.toModel(entity.getUser()));

        return standupModel;
    }

    public static StandupEntity fromModel(Standup standup) {
        StandupEntity standupEntity = new StandupEntity();
        standupEntity.setId(standup.getId());
        standupEntity.setName(standup.getName());
        standupEntity.setDescription(standup.getDescription());
        standupEntity.setPrice(standup.getPrice());
        standupEntity.setLanguage(standup.getLanguage());
        standupEntity.setImagePath(standup.getImagePath());
        standupEntity.setRating(standup.getRating());
//        standupEntity.setUser(User.fromModel(standup.getUser()));

        return standupEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }
}
