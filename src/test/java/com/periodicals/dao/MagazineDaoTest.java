package com.periodicals.dao;

import com.periodicals.dao.daoImpl.CategoryDaoImpl;
import com.periodicals.dao.daoImpl.MagazineDaoImpl;
import com.periodicals.entities.*;
import com.periodicals.exceptions.DBException;
import com.periodicals.utils.DirectConnection;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.periodicals.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class MagazineDaoTest {
    static MagazineDaoImpl magazineDao;
    static CategoryDaoImpl categoryDao;
    static Connection connection;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        magazineDao = new MagazineDaoImpl();
        magazineDao.setConnectionBuilder(new DirectConnection());

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
        connection.createStatement().executeUpdate(INSERT_CATEGORIES);
        connection.createStatement().executeUpdate(CREATE_TABLE_MAGAZINE);
        connection.createStatement().executeUpdate(CREATE_TABLE_MAGAZINE_LOCALIZATION);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_MAGAZINE_LOCALIZATION);
        connection.createStatement().executeUpdate(DROP_TABLE_MAGAZINE);
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
    void testFindMagazineById() throws SQLException, DBException {
        int magazineId = 1;
        Date magazinePublicationDate = Date.valueOf(PUBLICATION_DATE);

        connection.createStatement().executeUpdate(insertMagazineStatement(magazineId));

        List<String> magazineLocalizationStatements = insertMagazineLocalizationStatement(magazineId);
        for (String statement: magazineLocalizationStatements) {
            connection.createStatement().executeUpdate(statement);
        }

        MagazineLocalization magazine = magazineDao.findMagazineById(magazineId);

        Assertions.assertNotNull(magazine);
        Assertions.assertEquals(magazineId, magazine.getId());
        Assertions.assertEquals(magazinePublicationDate, magazine.getPublicationDate());
    }

    @Test
    void testFindAllMagazines() throws SQLException, DBException {
        int magazinesNumber = 5;

        String insertStatement;
        List<String> magazineLocalizationStatements;
        for (int i = 1; i <= magazinesNumber; i++){
            insertStatement = insertMagazineStatement(i);
            connection.createStatement().executeUpdate(insertStatement);

            magazineLocalizationStatements = insertMagazineLocalizationStatement(i);
            for (String statement: magazineLocalizationStatements) {
                connection.createStatement().executeUpdate(statement);
            }
        }

        List<Magazine> magazines;
        for (Locale locale: Locale.values()) {
            magazines = magazineDao.findAllMagazines(locale.getId());

            Assertions.assertNotNull(magazines);
            assertEquals(magazinesNumber, magazines.size());
        }
    }

    @Test
    void testFindSortedMagazinesOnPage() throws SQLException, DBException {
        int magazinesNumber = 10;
        int magazinesOnPage = 3;
        String name = "name";
        int pageNum = 0;

        List<String> magazineLocalizationStatements;
        for (int i = 1; i <= magazinesNumber; i++) {
            connection.createStatement().executeUpdate(insertMagazineStatement(i));

            magazineLocalizationStatements = insertMagazineLocalizationStatement(i);
            for (String statement: magazineLocalizationStatements) {
                connection.createStatement().executeUpdate(statement);
            }
        }

        List<Magazine> sortedMagazines;
        for (Locale locale: Locale.values()) {
            sortedMagazines = magazineDao.findSortedMagazinesOnPage(PRICE, name,
                    magazinesOnPage, pageNum, locale.getId());

            Assertions.assertNotNull(sortedMagazines);
            Assertions.assertTrue(magazinesOnPage >= sortedMagazines.size());
            assertThat(sortedMagazines, isSortedByPrice());
            for (Magazine magazine : sortedMagazines) {
                Assertions.assertTrue(magazine.getName().contains(name));
            }
        }
    }

    @Test
    void testFindSortedMagazinesByCategoryOnPage() throws SQLException, DBException {
        int localeId = 1;
        int magazinesNumber = 10;
        int categoryId = categoryDao.findAllCategories(localeId).get(0).getId();
        String name = "name";
        int magazinesOnPage = 3;
        int pageNum = 0;

        for (int i = 1; i <= magazinesNumber; i++) {
            connection.createStatement().executeUpdate(insertMagazineStatement(i));

            List<String> magazineLocalizationStatements = insertMagazineLocalizationStatement(i);
            for (String statement: magazineLocalizationStatements) {
                connection.createStatement().executeUpdate(statement);
            }
        }

        List<Magazine> sortedMagazines;
        for (Locale locale: Locale.values()) {
            sortedMagazines = magazineDao.findSortedMagazinesByCategoryOnPage(categoryId, PRICE, name,
                    magazinesOnPage, pageNum, locale.getId());

            Assertions.assertNotNull(sortedMagazines);
            Assertions.assertTrue(magazinesOnPage >= sortedMagazines.size());
            assertThat(sortedMagazines, isSortedByPrice());
            for (Magazine magazine : sortedMagazines) {
                Assertions.assertEquals(categoryId, magazine.getCategoryId());
                Assertions.assertTrue(magazine.getName().contains(name));
            }
        }
    }

    @Test
    void testGetMagazinesNumber() throws SQLException, DBException {
        int magazinesNumber = 10;
        String correctName = "name";
        String incorrectName = "incorrectName";

        for (int i = 1; i <= magazinesNumber; i++) {
            connection.createStatement().executeUpdate(insertMagazineStatement(i));

            List<String> magazineLocalizationStatements = insertMagazineLocalizationStatement(i);
            for (String statement: magazineLocalizationStatements) {
                connection.createStatement().executeUpdate(statement);
            }
        }

        for (Locale locale: Locale.values()) {
            Assertions.assertEquals(0, magazineDao.getMagazinesNumber(incorrectName, locale.getId()));
            Assertions.assertEquals(magazinesNumber, magazineDao.getMagazinesNumber(correctName, locale.getId()));
        }
    }

    @Test
    void testGetMagazinesNumberInCategory() throws SQLException, DBException {
        int localeId = 1;
        int magazinesNumber = 10;
        int categoryId = categoryDao.findAllCategories(localeId).get(0).getId();
        int categoriesNumber = categoryDao.findAllCategories(localeId).size();
        String correctName = "name";
        String incorrectName = "incorrectName";

        for (int i = 1; i <= magazinesNumber; i++) {
            connection.createStatement().executeUpdate(insertMagazineStatement(i));

            List<String> magazineLocalizationStatements = insertMagazineLocalizationStatement(i);
            for (String statement: magazineLocalizationStatements) {
                connection.createStatement().executeUpdate(statement);
            }
        }

        for (Locale locale: Locale.values()) {
            Assertions.assertEquals(0, magazineDao.getMagazinesNumberInCategory(categoryId, incorrectName, locale.getId()));
            Assertions.assertEquals(magazinesNumber / categoriesNumber, magazineDao.getMagazinesNumberInCategory(categoryId, correctName, locale.getId()));
        }
    }

    @Test
    void testAddMagazine() throws Exception {
        int localeId = 1;
        int magazineId = 1;
        int categoryId = categoryDao.findAllCategories(localeId).get(0).getId();

        MagazineLocalization magazine = new MagazineLocalization();
        magazine.setId(magazineId);
        magazine.setCategoryId(categoryId);
        magazine.setPrice(0);
        magazine.setPublicationDate(Date.valueOf(PUBLICATION_DATE));
        magazine.setImageURL(IMAGE_URL);

        HashMap<Integer, String> names = new HashMap<>();
        HashMap<Integer, String> publishers = new HashMap<>();
        HashMap<Integer, String> descriptions = new HashMap<>();

        for (Locale locale: Locale.values()) {
            names.put(locale.getId(), NAME + locale.name());
            publishers.put(locale.getId(), PUBLISHER + locale.name());
            descriptions.put(locale.getId(), DESCRIPTION + locale.name());
        }

        magazine.setNames(names);
        magazine.setPublishers(publishers);
        magazine.setDescriptions(descriptions);

        magazineDao.addMagazine(magazine);
        MagazineLocalization magazineFromDB = magazineDao.findMagazineById(magazineId);

        Assertions.assertEquals(magazine, magazineFromDB);
    }

    @Test
    void testUpdateMagazine() throws Exception {
        int magazineId = 1;
        Random rand = new Random();
        int dateUpperBound = Date.valueOf(PUBLICATION_DATE).toLocalDate().getDayOfYear();
        int priceUpperBound = 100000;

        connection.createStatement().executeUpdate(insertMagazineStatement(magazineId));

        List<String> magazineLocalizationStatements = insertMagazineLocalizationStatement(magazineId);
        for (String statement: magazineLocalizationStatements) {
            connection.createStatement().executeUpdate(statement);
        }

        MagazineLocalization oldMagazine = magazineDao.findMagazineById(magazineId);
        MagazineLocalization newMagazine = magazineDao.findMagazineById(magazineId);
        newMagazine.setPrice(newMagazine.getPrice() + rand.nextInt(priceUpperBound));
        newMagazine.setPublicationDate(Date.valueOf(newMagazine.getPublicationDate().toLocalDate().
                minusDays(rand.nextInt(dateUpperBound)).toString()));
        magazineDao.updateMagazine(newMagazine);

        assertEquals(newMagazine, magazineDao.findMagazineById(magazineId));
        assertNotEquals(oldMagazine, magazineDao.findMagazineById(magazineId));
    }

    @Test
    void testDeleteMagazine() throws Exception {
        int magazinesNumber = 5;
        int langId = Locale.EN.getId();
        String insertStatement;
        List<String> magazineLocalizationStatements;

        for (int i = 1; i <= magazinesNumber; i++) {
            insertStatement = insertMagazineStatement(i);
            connection.createStatement().executeUpdate(insertStatement);

            magazineLocalizationStatements = insertMagazineLocalizationStatement(i);
            for (String statement: magazineLocalizationStatements) {
                connection.createStatement().executeUpdate(statement);
            }
        }

        for (int i = magazinesNumber; i > 0; i--) {
            assertEquals(i, magazineDao.findAllMagazines(langId).size());
            magazineDao.deleteMagazine(i);
        }

        assertEquals(0, magazineDao.findAllMagazines(langId).size());
    }



    private String insertMagazineStatement(int magazineId) throws DBException {
        int localeId = 1;
        int categoriesNumber = categoryDao.findAllCategories(localeId).size();
        String magazineIdString = String.valueOf(magazineId);
        List<String> magazineData = List.of(magazineIdString, String.valueOf(magazineId % categoriesNumber + 1),
                magazineIdString, PUBLICATION_DATE, IMAGE_URL + magazineId);

        String insertData = magazineData.stream().collect(Collectors.joining("\", \"", "\"", "\""));

        return "INSERT INTO magazine (id, category_id, price, publication_date, image_url) " +
                "VALUES (" + insertData + ");";
    }

    private List<String> insertMagazineLocalizationStatement(int magazineId){
        List<String> statements = new ArrayList<>();
        List<String> magazineData;
        String insertData;
        for (Locale locale: Locale.values()) {
            magazineData = List.of(String.valueOf(magazineId), String.valueOf(locale.getId()),
                    NAME + locale.name() + magazineId, DESCRIPTION + locale.name() + magazineId,
                    PUBLISHER + locale.name() + magazineId);
            insertData = magazineData.stream().collect(Collectors.joining("\", \"", "\"", "\""));
            statements.add("INSERT INTO magazine_localization (magazine_id, locale_id, name, description, publisher) " +
                    "VALUES (" + insertData + ");");
        }

        return statements;
    }

    private Matcher<List<Magazine>> isSortedByPrice() {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            protected boolean matchesSafely(List<Magazine> magazines) {
                for (int i = 0; i < magazines.size() - 1; i++) {
                    if (magazines.get(i).getPrice() > magazines.get(i + 1).getPrice()) return false;
                }
                return true;
            }
        };
    }
}
