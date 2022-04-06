package com.periodicals.dao;

import com.periodicals.dao.daoImpl.UserDaoImpl;
import com.periodicals.dao.utils.Utils;
import com.periodicals.entities.User;
import com.periodicals.exceptions.DBException;
import com.periodicals.utils.DirectConnection;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.periodicals.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserDaoTest {
    static UserDaoImpl userDao;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        userDao = new UserDaoImpl();
        userDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASS);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_ROLE);
        connection.createStatement().executeUpdate(INSERT_ROLES);
        connection.createStatement().executeUpdate(CREATE_TABLE_USER);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_USER);
        connection.createStatement().executeUpdate(DROP_TABLE_ROLE);
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_DATABASE);
    }


    @Test
    void testConnection(){
        assertNotNull(connection);
    }

    @Test
    void testFindUserById() throws SQLException, DBException {
        int userId = 1;
        String userLogin = "login1";

        String insertStatement = insertUserStatement(userId);
        connection.createStatement().executeUpdate(insertStatement);

        User user = userDao.findUserByLogin(userLogin);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());
        Assertions.assertEquals(userLogin, user.getLogin());
    }


    @Test
    void testFindAllUsers() throws SQLException, DBException {
        int usersNumber = 5;

        String insertStatement;
        for (int i = 1; i <= usersNumber; i++){
            insertStatement = insertUserStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        List<User> users = userDao.findAllUsers();

        Assertions.assertNotNull(users);
        assertEquals(usersNumber, users.size());
    }


    @Test
    void testIsLoginUnique() throws SQLException, DBException {
        int usersNumber = 2;

        String insertStatement;
        for (int i = 1; i <= usersNumber; i++){
            insertStatement = insertUserStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        List<User> users = userDao.findAllUsers();
        String login1 = users.get(0).getLogin();
        String login2 = users.get(1).getLogin();
        String login3 = "uniqueLogin";

        assertFalse(userDao.isLoginUnique(login1));
        assertFalse(userDao.isLoginUnique(login2));
        assertTrue(userDao.isLoginUnique(login3));
    }

    @Test
    void testLogIn() throws SQLException, DBException {
        int usersNumber = 2;

        String insertStatement;
        for (int i = 1; i <= usersNumber; i++){
            insertStatement = insertUserStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        List<User> users = userDao.findAllUsers();

        User expectedUser1 = users.get(0);
        User expectedUser2 = users.get(1);

        User actualUser1 = userDao.logIn(expectedUser1.getLogin(), PASSWORD);
        User actualUser2 = userDao.logIn(expectedUser2.getLogin(), PASSWORD);

        String wrongLogin = "wrongLogin";
        String wrongPassword = "wrongPassword";

        assertEquals(expectedUser1, actualUser1);
        assertEquals(expectedUser2, actualUser2);
        assertNull(userDao.logIn(wrongLogin, expectedUser1.getPassword()));
        assertNull(userDao.logIn(expectedUser1.getLogin(), wrongPassword));

    }

    @Test
    void testSignUp() throws Exception {
        User user = new User();
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setPhone(PHONE);
        user.setRoleId(1);

        userDao.signUp(user);
        List<User> users = userDao.findAllUsers();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void testChangeRole() throws Exception {
        int usersNumber = 2;

        String insertStatement;
        for (int i = 1; i <= usersNumber; i++){
            insertStatement = insertUserStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        userDao.changeRole(1, 2);
        userDao.changeRole(2, 3);

        List<User> users = userDao.findAllUsers();

        assertEquals(2, users.get(0).getRoleId());
        assertEquals(3, users.get(1).getRoleId());
        assertThrows(DBException.class, () -> userDao.changeRole(2, 4));
    }

    @Test
    void testGetBalance() throws Exception {
        int usersNumber = 2;

        String insertStatement;
        for (int i = 1; i <= usersNumber; i++){
            insertStatement = insertUserStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        assertEquals(1, userDao.getBalance(1));
        assertEquals(2, userDao.getBalance(2));
    }

    @Test
    void testReplenishBalance() throws Exception {
        int usersNumber = 2;

        String insertStatement;
        for (int i = 1; i <= usersNumber; i++){
            insertStatement = insertUserStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        userDao.replenishBalance(1, 100);
        userDao.replenishBalance(2, 200);

        assertEquals(101, userDao.getBalance(1));
        assertEquals(202, userDao.getBalance(2));
    }

    @Test
    void testWithdrawal() throws Exception {
        int usersNumber = 2;

        String insertStatement;
        for (int i = 1; i <= usersNumber; i++){
            insertStatement = insertUserStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        userDao.withdrawal(1,1);
        userDao.withdrawal(2,2);

        assertEquals(0, userDao.getBalance(1));
        assertEquals(0, userDao.getBalance(2));
    }



    private String insertUserStatement(int userId){
        List<String> userData = List.of(String.valueOf(userId), LOGIN + userId,
                Utils.hash(PASSWORD.getBytes(StandardCharsets.UTF_8)),
                EMAIL + userId, PHONE + userId, String.valueOf(userId));

        String insertData = userData.stream().collect(Collectors.joining("\", \"", "\"", "\""));

        return "INSERT INTO user (id, login, password, email, phone, balance) " +
                "VALUES (" + insertData + ");";
    }
}