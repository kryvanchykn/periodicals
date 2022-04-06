package com.periodicals.dao;

import com.periodicals.dao.daoImpl.CategoryDaoImpl;
import com.periodicals.entities.Category;
import com.periodicals.entities.CategoryLocalization;
import com.periodicals.entities.Locale;
import com.periodicals.exceptions.DBException;
import com.periodicals.utils.DirectConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.periodicals.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryDaoTest {
    static CategoryDaoImpl categoryDao;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        categoryDao = new CategoryDaoImpl();
        categoryDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASS);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_LOCALE);
        connection.createStatement().executeUpdate(INSERT_LOCALES);
        connection.createStatement().executeUpdate(CREATE_TABLE_CATEGORY);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_CATEGORY);
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
    void testFindCategoryById() throws SQLException, DBException {
        int categoryId = 1;
        List<String> categoryNames = new ArrayList<>();
        for(Locale locale: Locale.values()){
            categoryNames.add(CATEGORY + categoryId + locale.getId());
        }

        String insertStatement = insertCategoryStatement(categoryId);
        connection.createStatement().executeUpdate(insertStatement);

        CategoryLocalization categoryLocalization = categoryDao.findCategoryById(categoryId);

        Assertions.assertNotNull(categoryLocalization);
        Assertions.assertEquals(categoryId, categoryLocalization.getId());
        Assertions.assertEquals(categoryNames, new ArrayList<>(categoryLocalization.getNames().values()));
    }

    @Test
    void testFindAllCategories() throws SQLException, DBException {
        int localeId = Locale.EN.getId();
        int categoryNumber = 5;

        String insertStatement;
        for (int i = 1; i <= categoryNumber; i++) {
            insertStatement = insertCategoryStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        List <Category> categories = categoryDao.findAllCategories(localeId);

        Assertions.assertNotNull(categories);
        assertEquals(categoryNumber, categories.size());
    }

    @Test
    void testAddCategory() throws Exception {
        int localeId = Locale.EN.getId();
        int categoryId = 1;
        CategoryLocalization categoryLocalization = new CategoryLocalization();

        Map<Integer, String> categoryNames = new HashMap<>();
        for(Locale locale: Locale.values()){
            categoryNames.put(locale.getId(), CATEGORY + categoryId + locale.getId());
        }

        categoryLocalization.setNames(categoryNames);

        categoryDao.addCategory(categoryLocalization);
        List<Category> categories = categoryDao.findAllCategories(localeId);

        assertEquals(1, categories.size());
        assertEquals(categoryLocalization.getNames().get(localeId), categories.get(0).getName());
    }

    @Test
    void testUpdateCategory() throws Exception {
        int localeId = Locale.EN.getId();
        int categoryId = 1;
        String insertStatement = insertCategoryStatement(categoryId);
        connection.createStatement().executeUpdate(insertStatement);

        CategoryLocalization oldCategory = categoryDao.findCategoryById(categoryId);
        CategoryLocalization newCategory = categoryDao.findCategoryById(categoryId);

        Map<Integer, String> categoryNames = oldCategory.getNames();
        categoryNames.replace(localeId, "newNameEn");

        categoryDao.updateCategory(newCategory);

        assertEquals(newCategory, categoryDao.findCategoryById(categoryId));
        assertNotEquals(oldCategory, categoryDao.findCategoryById(categoryId));
    }


    @Test
    void testDeleteCategory() throws Exception {
        int localeId = Locale.EN.getId();
        int categoryNumber = 5;

        String insertStatement;
        for (int i = 1; i <= categoryNumber; i++) {
            insertStatement = insertCategoryStatement(i);
            connection.createStatement().executeUpdate(insertStatement);
        }

        for (int i = categoryNumber; i > 0; i--) {
            assertEquals(i, categoryDao.findAllCategories(localeId).size());
            categoryDao.deleteCategory(i);
        }
        assertEquals(0, categoryDao.findAllCategories(localeId).size());
    }


    private String insertCategoryStatement(int categoryId){
        List<String> categoryData;
        List<String> insertData = new ArrayList<>();
        for(Locale locale: Locale.values()){
            categoryData = Arrays.asList(String.valueOf(categoryId), String.valueOf(locale.getId()),
                    CATEGORY + categoryId + locale.getId());
            insertData.add(categoryData.stream().collect(Collectors.joining("\", \"", "\"", "\"")));
        }

        return "INSERT INTO category VALUES (" + String.join("), (", insertData) + ");";
    }
}
