package com.periodicals.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Entity class for magazine table in database(with fields in different languages)
 */

public class MagazineLocalization implements Serializable {
    private int id;
    private int categoryId;
    private double price;
    private Date publicationDate;
    private String imageURL;
    private Map<Integer, String> names;
    private Map<Integer, String> publishers;
    private Map<Integer, String> descriptions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Map<Integer, String> getNames() {
        return names;
    }

    public void setNames(Map<Integer, String> names) {
        this.names = names;
    }

    public Map<Integer, String> getPublishers() {
        return publishers;
    }

    public void setPublishers(Map<Integer, String> publishers) {
        this.publishers = publishers;
    }

    public Map<Integer, String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<Integer, String> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MagazineLocalization that = (MagazineLocalization) o;
        return id == that.id && categoryId == that.categoryId && Double.compare(that.price, price) == 0 && Objects.equals(publicationDate, that.publicationDate) && Objects.equals(imageURL, that.imageURL) && Objects.equals(names, that.names) && Objects.equals(publishers, that.publishers) && Objects.equals(descriptions, that.descriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, price, publicationDate, imageURL, names, publishers, descriptions);
    }

    @Override
    public String toString() {
        return "MagazineLocalization{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", publicationDate=" + publicationDate +
                ", imageURL='" + imageURL + '\'' +
                ", names=" + names +
                ", publishers=" + publishers +
                ", descriptions=" + descriptions +
                '}';
    }
}
