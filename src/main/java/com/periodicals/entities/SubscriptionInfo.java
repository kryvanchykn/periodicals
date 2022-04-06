package com.periodicals.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * Entity class for subscription table in database(only data needed to display the table with subscriptions)
 */

public class SubscriptionInfo implements Serializable {
    private String magazineName;
    private String magazineCategory;
    private Date startDate;
    private Date endDate;

    public String getMagazineName() {
        return magazineName;
    }

    public void setMagazineName(String magazineName) {
        this.magazineName = magazineName;
    }

    public String getMagazineCategory() {
        return magazineCategory;
    }

    public void setMagazineCategory(String magazineCategory) {
        this.magazineCategory = magazineCategory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionInfo that = (SubscriptionInfo) o;
        return Objects.equals(magazineName, that.magazineName) && Objects.equals(magazineCategory, that.magazineCategory) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(magazineName, magazineCategory, startDate, endDate);
    }

    @Override
    public String toString() {
        return "SubscriptionInfo{" +
                ", magazineName='" + magazineName + '\'' +
                ", magazineCategory='" + magazineCategory + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
