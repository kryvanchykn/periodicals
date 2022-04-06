package com.periodicals.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class CategoryLocalization implements Serializable {
    private  int id;
    private Map<Integer, String> names;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, String> getNames() {
        return names;
    }

    public void setNames(Map<Integer, String> names) {
        this.names = names;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryLocalization that = (CategoryLocalization) o;
        return id == that.id && Objects.equals(names, that.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, names);
    }

    @Override
    public String toString() {
        return "CategoryLocalization{" +
                "id=" + id +
                ", names=" + names +
                '}';
    }
}
