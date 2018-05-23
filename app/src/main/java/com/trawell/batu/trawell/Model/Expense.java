package com.trawell.batu.trawell.Model;

/**
 * Created by Batuhan Islek on 18.05.2018.
 */
public class Expense {
    private String type;
    private String comment;
    private Double price;
    private String location;
    private String date;

    public Expense() { }

    public Expense(String type, String comment, Double price, String location, String date) {
        this.type = type;
        this.comment = comment;
        this.price = price;
        this.location = location;
        this.date = date;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
