package com.periodicals.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity class for user table in database
 */

public class User implements Serializable {
    private Integer id;
    private String login;
    private String password;
    private String email;
    private String phone;
    private double balance;
    private Integer roleId;


    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Double getBalance() {
        return balance;
    }

    public Integer getRoleId() {
        return roleId;
    }




    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone) && roleId == user.roleId && Objects.equals(balance, user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, phone, balance, roleId);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", balance=" + balance +
                ", roleId=" + roleId +
                '}';
    }
}
