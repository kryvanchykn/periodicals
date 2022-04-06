package com.periodicals.entities;

import java.io.Serializable;

/**
 * Entity class for locale table in database
 */

public enum Locale implements Serializable {
    EN(1),
    UA(2);

    private final int id;
    private String priceSign;
    private double exchangeRate;

    Locale(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getPriceSign() {
        return priceSign;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setPriceSign(String priceSign) {
        this.priceSign = priceSign;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public static Locale localeById(int localeId) {
        for (Locale value : Locale.values()) {
            if (localeId == value.id) {
                return value;
            }
        }
        return null;
    }
}

