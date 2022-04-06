package com.periodicals.dao.daoInterfaces;

import com.periodicals.entities.User;
import com.periodicals.exceptions.DBException;

import java.util.List;

public interface UserDao{
    /**
     * Check if there are users with the same login in user table in database
     * @param login login of user
     */
    boolean isLoginUnique(String login) throws DBException;

    /**
     * Hash password and get user from user table in database. If no user returns, then wrong
     * login or password was entered and function return null object.
     * Otherwise, return user object
     * @param login login of user
     * @param password original(not hashed) password in char array
     * @return null if there isn't user with this login and password
     * or user object
     */
    User logIn(String login, String password) throws DBException;

    /**
     * Create new user in user table in database.
     * @param user new user
     */
    void signUp(User user) throws Exception;

    /**
     * Get all users from user table in database
     * @return list of users
     */
    List<User> findAllUsers() throws DBException;

    /**
     * Get user by login
     * @param login login of user
     * @return null if there isn't user with this login
     * or user object
     */
    User findUserByLogin(String login) throws DBException;

    /**
     * Change user role_id in user table in database
     * @param userId id of user
     * @param newRoleId new role id
     */
    void changeRole(int userId, int newRoleId) throws DBException;

    /**
     * Get balance of user by id from user table in database
     * @param userId id of user
     * @return 0 if there isn't user with this id
     * or user balance
     */
    double getBalance(int userId) throws DBException;

    /**
     * Update balance of user by id. Add to balance the amount of money in user table in database
     * @param userId id of user
     * @param amountOfMoney amount of money that is added to user current balance
     * @return new balance of user
     */
    double replenishBalance(int userId, double amountOfMoney) throws DBException;

    /**
     * Update balance of user by id. Reduce the balance by the amount of money in user table in database
     * @param userId id of user
     * @param amountOfMoney amount of money that is withdrawn from the user current balance
     */
    void withdrawal(int userId, double amountOfMoney) throws DBException;
}
