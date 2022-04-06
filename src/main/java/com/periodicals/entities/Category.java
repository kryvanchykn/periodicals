package com.periodicals.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity class for category table in database
 */

public class Category implements Serializable {
    private int id;
    private int LocaleId;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocaleId() {
        return LocaleId;
    }

    public void setLocaleId(int localeId) {
        LocaleId = localeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && LocaleId == category.LocaleId && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, LocaleId, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", LocaleId=" + LocaleId +
                ", name='" + name + '\'' +
                '}';
    }
}
