package com.periodicals.dao;

import com.periodicals.dao.daoImpl.CategoryDaoImpl;
import com.periodicals.dao.daoImpl.MagazineDaoImpl;
import com.periodicals.dao.daoImpl.SubscriptionDaoImpl;
import com.periodicals.dao.daoImpl.UserDaoImpl;
import com.periodicals.entities.Locale;
import com.periodicals.entities.Magazine;
import com.periodicals.entities.SubscriptionInfo;
import com.periodicals.exceptions.DBException;
import com.periodicals.utils.DirectConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.periodicals.utils.TestConstants.*;
import static com.periodicals.utils.TestConstants.DROP_DATABASE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionDaoTest {
    static UserDaoImpl userDao;
    static CategoryDaoImpl categoryDao;
    static MagazineDaoImpl magazineDao;
    static SubscriptionDaoImpl subscriptionDao;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        userDao = new UserDaoImpl();
        userDao.setConnectionBuilder(new DirectConnection());

        categoryDao = new CategoryDaoImpl();
        categoryDao.setConnectionBuilder(new DirectConnection());

        magazineDao = new MagazineDaoImpl();
        magazineDao.setConnectionBuilder(new DirectConnection());

        subscriptionDao = new SubscriptionDaoImpl();
        subscriptionDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASS);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_LOCALE);
        connection.createStatement().executeUpdate(INSERT_LOCALES);

        connection.createStatement().executeUpdate(CREATE_TABLE_ROLE);
        connection.createStatement().executeUpdate(INSERT_ROLES);

        connection.createStatement().executeUpdate(CREATE_TABLE_USER);
        connection.createStatement().executeUpdate(INSERT_USERS);

        connection.createStatement().executeUpdate(CREATE_TABLE_CATEGORY);
        connection.createStatement().executeUpdate(INSERT_CATEGORIES);

        connection.createStatement().executeUpdate(CREATE_TABLE_MAGAZINE);
        connection.createStatement().executeUpdate(INSERT_MAGAZINES);

        connection.createStatement().executeUpdate(CREATE_TABLE_MAGAZINE_LOCALIZATION);
        connection.createStatement().executeUpdate(INSERT_MAGAZINE_LOCALIZATION);

        connection.createStatement().executeUpdate(CREATE_TABLE_SUBSCRIPTION);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_SUBSCRIPTION);
        connection.createStatement().executeUpdate(DROP_TABLE_MAGAZINE_LOCALIZATION);
        connection.createStatement().executeUpdate(DROP_TABLE_MAGAZINE);
        connection.createStatement().executeUpdate(DROP_TABLE_CATEGORY);
        connection.createStatement().executeUpdate(DROP_TABLE_USER);
        connection.createStatement().executeUpdate(DROP_TABLE_ROLE);
        connection.createStatement().executeUpdate(DROP_TABLE_LOCALE);
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_DATABASE);
    }


    @Test
    void testConnection() {
        assertNotNull(connection);
    }

    @Test
    void testIsSubscribed() throws SQLException, DBException {
        List<Magazine> magazines = magazineDao.findAllMagazines(Locale.EN.getId());
        int subscribedUserId = userDao.findAllUsers().get(0).getId();
        int unsubscribedUserId = userDao.findAllUsers().get(1).getId();

        String insertStatement;
        for (Magazine magazine: magazines) {
            insertStatement = insertActiveSubscriptionStatement(magazine.getId(), subscribedUserId);
            connection.createStatement().executeUpdate(insertStatement);
        }

        for (Magazine magazine: magazines) {
            Assertions.assertTrue(subscriptionDao.isSubscribed(subscribedUserId,magazine.getId()));
            Assertions.assertFalse(subscriptionDao.isSubscribed(unsubscribedUserId,magazine.getId()));
        }
    }

    @Test
    void testAddSubscription() throws Exception {
        int localeId = Locale.EN.getId();
        int subscribedUserId = userDao.findAllUsers().get(0).getId();
        int unsubscribedUserId = userDao.findAllUsers().get(1).getId();
        Magazine magazine = magazineDao.findAllMagazines(localeId).get(0);
        int magazineId = magazine.getId();
        double magazinePrice = magazine.getPrice();
        String magazineName = magazine.getName();
        String categoryName = categoryDao.findCategoryById(magazine.getCategoryId()).getNames().get(localeId);

        subscriptionDao.addSubscription(subscribedUserId, magazineId, magazinePrice);
        List<SubscriptionInfo> subscriptions = subscriptionDao.findActiveSubscriptionsInfoByUserId(subscribedUserId, localeId);

        Assertions.assertEquals(1, subscriptions.size());
        Assertions.assertEquals(magazineName, subscriptions.get(0).getMagazineName());
        Assertions.assertEquals(categoryName, subscriptions.get(0).getMagazineCategory());
        Assertions.assertEquals(0, subscriptionDao.findActiveSubscriptionsInfoByUserId(unsubscribedUserId, localeId).size());
    }


    @Test
    void testFindActiveSubscriptionsInfoByUserId() throws Exception {
        int localeId = Locale.EN.getId();
        int userId = userDao.findAllUsers().get(0).getId();

        Magazine magazine = magazineDao.findAllMagazines(localeId).get(0);
        String magazineName = magazine.getName();
        String categoryName = categoryDao.findCategoryById(magazine.getCategoryId()).getNames().get(localeId);

        Magazine expiredMagazine = magazineDao.findAllMagazines(localeId).get(1);

        String insertStatement = insertActiveSubscriptionStatement(magazine.getId(), userId);
        connection.createStatement().executeUpdate(insertStatement);

        insertStatement = insertExpiredSubscriptionStatement(expiredMagazine.getId(), userId);
        connection.createStatement().executeUpdate(insertStatement);

        List<SubscriptionInfo> subscriptions = subscriptionDao.findActiveSubscriptionsInfoByUserId(userId, localeId);

        Assertions.assertEquals(1, subscriptions.size());
        Assertions.assertEquals(magazineName, subscriptions.get(0).getMagazineName());
        Assertions.assertEquals(categoryName, subscriptions.get(0).getMagazineCategory());
    }


    @Test
    void testFindExpiredSubscriptionsInfoByUserId() throws Exception {
        int localeId = Locale.EN.getId();
        int userId = userDao.findAllUsers().get(0).getId();

        Magazine magazine = magazineDao.findAllMagazines(localeId).get(0);

        Magazine expiredMagazine = magazineDao.findAllMagazines(localeId).get(1);
        String expiredMagazineName = expiredMagazine.getName();
        String categoryName = categoryDao.findCategoryById(expiredMagazine.getCategoryId()).getNames().get(localeId);

        String insertStatement = insertActiveSubscriptionStatement(magazine.getId(), userId);
        connection.createStatement().executeUpdate(insertStatement);

        insertStatement = insertExpiredSubscriptionStatement(expiredMagazine.getId(), userId);
        connection.createStatement().executeUpdate(insertStatement);

        List<SubscriptionInfo> subscriptions = subscriptionDao.findExpiredSubscriptionsInfoByUserId(userId, localeId);

        Assertions.assertEquals(1, subscriptions.size());
        Assertions.assertEquals(expiredMagazineName, subscriptions.get(0).getMagazineName());
        Assertions.assertEquals(categoryName, subscriptions.get(0).getMagazineCategory());

    }


    private String insertSubscriptionStatement(int magazineId,int userId, boolean isActive) {
        LocalDate startDate;
        LocalDate endDate;

        if (isActive){
            startDate = LocalDate.now();
            endDate =  LocalDate.now().plusMonths(SUBSCRIPTION_PERIOD);
        } else {
            endDate =  LocalDate.now().minusMonths(SUBSCRIPTION_PERIOD);
            startDate =  endDate.minusMonths(SUBSCRIPTION_PERIOD);
        }

        List<String> subscriptionData = List.of(magazineId + "" + userId, String.valueOf(magazineId),
                String.valueOf(userId), startDate.toString(), endDate.toString());

        String insertData = subscriptionData.stream().collect(Collectors.joining("\", \"", "\"", "\""));

        return "INSERT INTO subscription (id, magazine_id, user_id, start_date, end_date) " +
                "VALUES (" + insertData + ");";
    }

    private String insertActiveSubscriptionStatement(int magazineId,int userId) {
        return insertSubscriptionStatement(magazineId, userId, true);
    }

    private String insertExpiredSubscriptionStatement(int magazineId,int userId) {
        return insertSubscriptionStatement(magazineId, userId, false);
    }
}
