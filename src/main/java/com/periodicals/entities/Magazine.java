package com.periodicals.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * Entity class for magazine table in database
 */

public class Magazine implements Serializable {
    private int id;
    private int categoryId;
    private double price;
    private Date publicationDate;
    private String imageURL;
    private String name;
    private String description;
    private String publisher;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getPrice() {
        return price;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magazine magazine = (Magazine) o;
        return id == magazine.id && categoryId == magazine.categoryId && Double.compare(magazine.price, price) == 0 && Objects.equals(name, magazine.name) && Objects.equals(publisher, magazine.publisher) && Objects.equals(publicationDate, magazine.publicationDate) && Objects.equals(description, magazine.description) && Objects.equals(imageURL, magazine.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categoryId, price, publisher, publicationDate, description, imageURL);
    }


    @Override
    public String toString() {
        return "Magazine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", author='" + publisher + '\'' +
                ", publicationDate=" + publicationDate +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}

