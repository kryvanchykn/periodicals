package com.periodicals.dao;

import com.periodicals.dao.daoImpl.LocaleDaoImpl;
import com.periodicals.entities.Locale;
import com.periodicals.exceptions.DBException;
import com.periodicals.utils.DirectConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.periodicals.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocaleDaoTest {
    static LocaleDaoImpl localeDao;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        localeDao = new LocaleDaoImpl();
        localeDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASS);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_LOCALE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_LOCALE);
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
    void testLocaleByLang() throws SQLException, DBException {
        int localeId = 1;
        String lang = LANG_NAME + localeId;
        String priceSign = PRICE_SIGN + localeId;

        String insertStatement = insertLocaleStatement(localeId);
        connection.createStatement().executeUpdate(insertStatement);

        Locale locale = localeDao.findLocaleByLang(lang);

        Assertions.assertNotNull(locale);
        Assertions.assertEquals(localeId, locale.getId());
        Assertions.assertEquals(priceSign, locale.getPriceSign());
    }

    @Test
    void testFindAllLocales() throws SQLException, DBException {
        int localesNumber = Locale.values().length;

        String insertStatement;
        for (int i = 1; i <= localesNumber; i++){
            insertStatement = insertLocaleStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        List<Locale> locales = localeDao.findAllLocales();

        Assertions.assertNotNull(locales);
        assertEquals(localesNumber, locales.size());

    }

    private String insertLocaleStatement(int localeId){
        String localeIdString = String.valueOf(localeId);
        List<String> localeData = List.of(localeIdString, LANG_NAME + localeId, PRICE_SIGN + localeId, localeIdString);

        String insertData = localeData.stream().collect(Collectors.joining("\", \"", "\"", "\""));

        return "INSERT INTO locale (id, lang_name, price_sign, exchange_rate) VALUES (" + insertData + ");";
    }
}
