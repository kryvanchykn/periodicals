package com.periodicals.utils;

public class TestConstants {
    public static final String DB_URL = "jdbc:mysql://localhost/";
    public static final String USER = "root";
    public static final String PASS = "root";
    public static final String FULL_URL = "jdbc:mysql://localhost:3306/test?user=root&password=root";
    public static final String CREATE_DATABASE = "CREATE DATABASE test;";
    public static final String USE_DATABASE = "USE test";
    public static final String DROP_DATABASE = "DROP DATABASE test";

    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String CATEGORY = "category";
    public static final String NAME = "name_";
    public static final String DESCRIPTION = "description_";
    public static final String PUBLISHER = "publisher_";
    public static final String PUBLICATION_DATE = "1999-09-09";
    public static final String IMAGE_URL = "imageUrl";
    public static final String PRICE = "price";
    public static final String LANG_NAME = "langName";
    public static final String PRICE_SIGN = "priceSign";
    public static final int SUBSCRIPTION_PERIOD = 1;


    public static String CREATE_TABLE_LOCALE =
            " CREATE TABLE IF NOT EXISTS locale (" +
                    "  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "  lang_name VARCHAR(10) NOT NULL," +
                    "  price_sign VARCHAR(30) NOT NULL," +
                    "  exchange_rate DECIMAL(10,3) UNSIGNED NOT NULL);";

    public static String INSERT_LOCALES =
            "INSERT INTO locale(id, lang_name, price_sign, exchange_rate)" +
                    "VALUES (1, 'EN', '&#36', '1')," +
                    "       (2, 'UA', '&#8372', '0.05');";

    public static String DROP_TABLE_LOCALE = "DROP TABLE locale CASCADE";


    public static String CREATE_TABLE_ROLE =
            "CREATE TABLE IF NOT EXISTS role (" +
                    "  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "  name VARCHAR(30) NOT NULL DEFAULT 'reader');";

    public static String INSERT_ROLES =
            "INSERT INTO role(id, name)" +
                    "VALUES (1, 'reader')," +
                    "       (2, 'blocked_reader')," +
                    "       (3, 'admin');";

    public static String DROP_TABLE_ROLE = "DROP TABLE role CASCADE";

    public static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS user (" +
                    "  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "  login VARCHAR(25) NOT NULL UNIQUE," +
                    "  password VARCHAR(250) NOT NULL," +
                    "  email VARCHAR(30) NOT NULL UNIQUE," +
                    "  phone VARCHAR(12) NULL DEFAULT NULL UNIQUE," +
                    "  balance DECIMAL(10,2) UNSIGNED NULL DEFAULT '0.00'," +
                    "  role_id INT NOT NULL DEFAULT 1," +
                    "    FOREIGN KEY (role_id)" +
                    "    REFERENCES role (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";

    public static String INSERT_USERS =
            "INSERT INTO user(id, login, password, email, phone, balance, role_id) " +
                    "VALUES(1, 'login1', 'password', 'emaillogin1@gmail.com', '0971111111', 100.00, 1)," +
                    "      (2, 'login2', 'password', 'emaillogin2@gmail.com', '0972222222', 100.00, 1);";

    public static String DROP_TABLE_USER = "DROP TABLE user CASCADE";


    public static String CREATE_TABLE_CATEGORY =
            "CREATE TABLE IF NOT EXISTS category (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  locale_id INT NOT NULL," +
                    "  name VARCHAR(100) NOT NULL UNIQUE," +
                    "  primary key (id, locale_id)," +
                    "   FOREIGN KEY (locale_id)" +
                    "    REFERENCES locale (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";

    public static String INSERT_CATEGORIES =
            "INSERT INTO category(id, locale_id, name)" +
                    "VALUES (1, 1, 'Fashion')," +
                    "       (2, 1, 'Beauty')," +
                    "       (1, 2, 'Мода')," +
                    "       (2, 2, 'Краса');";

    public static String DROP_TABLE_CATEGORY = "DROP TABLE category CASCADE";

    public static String CREATE_TABLE_MAGAZINE =
            "CREATE TABLE IF NOT EXISTS magazine (" +
            "  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
            "  category_id INT NOT NULL," +
            "  price DECIMAL(10,2) UNSIGNED NOT NULL," +
            "  publication_date DATE NOT NULL," +
            "  image_url VARCHAR(150) NULL DEFAULT NULL," +
            "    FOREIGN KEY (category_id)" +
            "    REFERENCES category(id)" +
            "    ON DELETE CASCADE" +
            "    ON UPDATE CASCADE);";

    public static String INSERT_MAGAZINES =
            "INSERT INTO magazine(id, category_id, price, publication_date, image_url) " +
                    "VALUES(1, 1, 5, '2001-11-11', 'https://vogue.ua/cache/inline_990x/uploads/article-inline/ce8/1cf/a01/2021_osnovnaya_6177a011cfce8.jpeg')," +
                    "      (2, 2, 7.50, '2000-11-11', 'https://images.ua.prom.st/2660272410_w640_h640_zhurnal-cosmopolitan-10.jpg');";

    public static String DROP_TABLE_MAGAZINE = "DROP TABLE magazine CASCADE";

    public static String CREATE_TABLE_MAGAZINE_LOCALIZATION =
            "CREATE TABLE IF NOT EXISTS magazine_localization (" +
            "  magazine_id INT NOT NULL," +
            "  locale_id INT NOT NULL," +
            "  name VARCHAR(45) NOT NULL UNIQUE," +
            "  description VARCHAR(500) NOT NULL," +
            "  publisher VARCHAR(45) NOT NULL," +
            "  UNIQUE KEY (magazine_id, locale_id)," +
            "    FOREIGN KEY (magazine_id)" +
            "    REFERENCES magazine (id)" +
            "    ON DELETE CASCADE" +
            "    ON UPDATE CASCADE," +
            "    FOREIGN KEY (locale_id)" +
            "    REFERENCES locale (id)" +
            "    ON DELETE CASCADE" +
            "    ON UPDATE CASCADE);";

    public static String INSERT_MAGAZINE_LOCALIZATION =
            "INSERT INTO magazine_localization(magazine_id, locale_id, name, description, publisher) " +
                    "VALUES(1, 1, 'Vogue', 'It is Vogue magazine', 'Condé Nast')," +
                    "      (2, 1, 'Cosmopolitan', 'It is Cosmopolitan magazine', 'Hearst Corporation')," +
                    "      (1, 2, 'Вок', 'Це журнал Вок', 'Конде Наст')," +
                    "      (2, 2, 'Космополітан', 'Це журнал Космополітан', 'Харст Корп');";

    public static String DROP_TABLE_MAGAZINE_LOCALIZATION = "DROP TABLE magazine_localization CASCADE";

    public static String CREATE_TABLE_SUBSCRIPTION =
            "CREATE TABLE IF NOT EXISTS subscription (" +
            "  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
            "  user_id INT NOT NULL," +
            "  magazine_id INT NOT NULL," +
            "  start_date DATE NOT NULL," +
            "  end_date DATE NOT NULL," +
            "    FOREIGN KEY (user_id)" +
            "    REFERENCES user (id)" +
            "    ON DELETE CASCADE" +
            "    ON UPDATE CASCADE," +
            "    FOREIGN KEY (magazine_id)" +
            "    REFERENCES magazine (id)" +
            "    ON DELETE CASCADE" +
            "    ON UPDATE CASCADE);";

    public static String DROP_TABLE_SUBSCRIPTION = "DROP TABLE subscription CASCADE";
}
