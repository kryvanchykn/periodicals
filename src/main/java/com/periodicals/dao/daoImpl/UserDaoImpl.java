package com.periodicals.dao.daoImpl;

import com.periodicals.dao.daoInterfaces.UserDao;
import com.periodicals.dao.utils.ConnectionBuilderSetUp;
import com.periodicals.dao.utils.ConnectionPool;
import com.periodicals.dao.utils.Utils;
import com.periodicals.entities.User;
import com.periodicals.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.periodicals.dao.utils.SqlConstants.*;

public class UserDaoImpl extends ConnectionBuilderSetUp implements UserDao {
    private static final Logger log = LogManager.getLogger(UserDaoImpl.class.getName());

    private static User mapUser(ResultSet rs) throws SQLException {
        int k = 0;

        User newUser = new User();
        newUser.setId(rs.getInt(++k));
        newUser.setLogin(rs.getString(++k));
        newUser.setPassword(rs.getString(++k));
        newUser.setEmail(rs.getString(++k));
        newUser.setPhone(rs.getString(++k));
        newUser.setBalance(rs.getDouble(++k));
        newUser.setRoleId(rs.getInt(++k));

        return newUser;
    }

    private void setUserAttributes(User user, String hashPass, PreparedStatement ps) throws SQLException {
        int k = 0;
        ps.setString(++k, user.getLogin());
        ps.setString(++k, hashPass);
        ps.setString(++k, user.getEmail());
        ps.setString(++k, user.getPhone());
    }


    @Override
    public List<User> findAllUsers() throws DBException {
        List<User> users = new ArrayList<>();
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_USERS);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {                users.add(mapUser(rs));
            }
            return users;
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.all.users", e);
        }
    }

    @Override
    public User findUserByLogin(String login) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_USER_BY_LOGIN)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapUser(rs);
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.user", e);
        }
    }

    @Override
    public boolean isLoginUnique(String login) throws DBException {
        return findUserByLogin(login) == null;
    }

    @Override
    public User logIn(String login, String password) throws DBException {
        String hashPass = Utils.hash(password.getBytes(StandardCharsets.UTF_8));
        try (Connection con = this.getConnectionBuilder().getConnection();
            PreparedStatement ps = con.prepareStatement(LOG_IN)) {
            int k = 0;
            ps.setString(++k, login);
            ps.setString(++k, hashPass);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapUser(rs);
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.login", e);
        }
    }


    @Override
    public void signUp(User user) throws Exception {
        String hashPass = Utils.hash(user.getPassword().getBytes(StandardCharsets.UTF_8));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.getConnectionBuilder().getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(SIGN_UP, Statement.RETURN_GENERATED_KEYS);

            setUserAttributes(user, hashPass, ps);

            if (ps.executeUpdate() == 0) {
                throw new DBException("error.page.signup.failed");
            } else {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
            }

            con.commit();
            findUserByLogin(user.getLogin());

        } catch (SQLException e) {
            ConnectionPool.rollback(con);
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.signup", e);
        } finally {
            ConnectionPool.close(ps);
            ConnectionPool.close(con);
        }
    }


    @Override
    public void changeRole(int userId, int newRoleId) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(CHANGE_ROLE)) {
            con.setAutoCommit(false);
            int k = 0;
            ps.setInt(++k, newRoleId);
            ps.setInt(++k, userId);
            if (ps.executeUpdate() == 0) {
                throw new DBException("error.page.change.role.failed");
            }
            con.commit();
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.change.role", e);
        }
    }

    @Override
    public double getBalance(int userId) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(GET_BALANCE)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0;
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.get.balance", e);
        }
    }

    @Override
    public double replenishBalance(int userId, double amountOfMoney) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_BALANCE)) {
            con.setAutoCommit(false);
            double newBalance = getBalance(userId) + amountOfMoney;
            int k = 0;
            ps.setDouble(++k, newBalance);
            ps.setInt(++k, userId);
            if (ps.executeUpdate() == 0) {
                throw new DBException("error.page.replenish.balance.failed");
            }
            con.commit();
            return newBalance;
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.replenish.balance", e);
        }
    }

    @Override
    public void withdrawal(int userId, double amountOfMoney) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_BALANCE)) {
            con.setAutoCommit(false);
            double newBalance = getBalance(userId) - amountOfMoney;

            int k = 0;
            ps.setDouble(++k, newBalance);
            ps.setInt(++k, userId);
            if (ps.executeUpdate() == 0) {
                throw new DBException("error.page.withdrawal.failed");
            }
            con.commit();
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.withdrawal", e);
        }
    }
}
