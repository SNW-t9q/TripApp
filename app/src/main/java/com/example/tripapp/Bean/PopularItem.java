package com.example.tripapp.Bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PopularItem implements Serializable {
    @SerializedName("id") private String id;
    @SerializedName("address") private String address;
    @SerializedName("bed") private int bed;
    @SerializedName("dateTour") private String dateTour;
    @SerializedName("description") private String description;
    @SerializedName("distance") private String distance;
    @SerializedName("duration") private String duration;
    @SerializedName("price") private int price;
    @SerializedName("score") private double score;
    @SerializedName("tourCount") private String tourCount;
    @SerializedName("title") private String title;
    @SerializedName("pic") private List<String> pic;

    public PopularItem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getPic() {
        return pic;
    }
    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public String getDateTour() {
        return dateTour;
    }

    public void setDateTour(String dateTour) {
        this.dateTour = dateTour;
    }

    public String getTourCount() {
        return tourCount;
    }

    public void setTourCount(String tourCount) {
        this.tourCount = tourCount;
    }
}
