package com.periodicals.dao.utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SqlConstants {
    public static final int READER = 1;
    public static final int BLOCKED_READER = 2;
    public static final int ADMIN = 3;

    public static final int SUBSCRIPTION_PERIOD = 1;

    public static final String FIND_LOCALE_BY_LANG = "SELECT id, price_sign, exchange_rate FROM locale WHERE lang_name = ?";
    public static final String FIND_ALL_LOCALES = "SELECT id, price_sign, exchange_rate FROM locale ORDER BY id";

    public static final String FIND_ALL_USERS = "SELECT * FROM user ORDER BY role_id";
    public static final String FIND_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
    public static final String LOG_IN = "SELECT * FROM user WHERE login = ? AND password = ?";
    public static final String SIGN_UP = "INSERT INTO user (id, login, password, email, phone) VALUES (DEFAULT, ?, ?, ?, ?)";
    public static final String GET_BALANCE = "SELECT balance FROM user WHERE id = ?";
    public static final String UPDATE_BALANCE = "UPDATE user SET balance = ? WHERE id = ?";
    public static final String CHANGE_ROLE = "UPDATE user SET role_id = ? WHERE id = ?";

    public static final String IS_SUBSCRIBED = "SELECT * FROM subscription WHERE user_id=? AND magazine_id=? AND end_date >= '" + LocalDate.now() + "'";
    public static final String ADD_SUBSCRIPTION = "INSERT INTO subscription VALUES(DEFAULT, ?, ?, ?, ?)";
    public static final String FIND_SUBSCRIPTION_INFO_BY_USER_ID =
            "SELECT ml.name, c.name, s.start_date, " +
            "s.end_date FROM subscription s \n" +
            "INNER JOIN magazine m ON m.id = s.magazine_id \n" +
            "INNER JOIN magazine_localization ml ON m.id = ml.magazine_id \n" +
            "INNER JOIN category c ON c.id = m.category_id AND c.locale_id = ml.locale_id\n" +
            "WHERE s.user_id = ? AND ml.locale_id = ? AND s.end_date";

    public static final String FIND_MAGAZINE_BY_ID = "SELECT id, category_id, price, publication_date, image_url, locale_id, name, description, publisher FROM magazine JOIN magazine_localization ON magazine.id = magazine_localization.magazine_id WHERE id = ?";
    public static final String FIND_ALL_MAGAZINES = "SELECT m.id, category_id, price, publication_date, image_url, name, description, publisher FROM magazine m INNER JOIN magazine_localization ml ON m.id = ml.magazine_id INNER JOIN locale l ON l.id = ml.locale_id WHERE l.id = ?";
    public static final String FIND_SORTED_MAGAZINES = "SELECT id, category_id, price, publication_date, image_url, name, description, publisher FROM magazine INNER JOIN magazine_localization ON magazine.id = magazine_localization.magazine_id WHERE locale_id = ? AND name LIKE '";
    public static final String FIND_SORTED_MAGAZINES_BY_CATEGORY = "SELECT id, category_id, price, publication_date, image_url, name, description, publisher FROM magazine INNER JOIN magazine_localization ON magazine.id = magazine_localization.magazine_id WHERE category_id = ? AND locale_id = ? AND name LIKE '";
    public static final String ADD_MAGAZINE_MAIN_INFO = "INSERT INTO magazine(id, category_id, price, publication_date, image_url) VALUES (DEFAULT, ?, ?, ?, ?)";
    public static final String ADD_MAGAZINE_ADDITIONAL_INFO = "INSERT INTO magazine_localization(magazine_id, locale_id, name, description, publisher) VALUES (?, ?, ?, ?, ?)";
    public static final String GET_MAGAZINES_COUNT = "SELECT COUNT(*) FROM magazine m INNER JOIN magazine_localization ml ON m.id = ml.magazine_id WHERE ml.locale_id = ? AND name LIKE '";
    public static final String GET_MAGAZINES_COUNT_IN_CATEGORY = "SELECT COUNT(*) FROM magazine m INNER JOIN magazine_localization ml ON m.id = ml.magazine_id WHERE m.category_id = ? AND ml.locale_id = ? AND name LIKE '";
    public static final String UPDATE_MAGAZINE_MAIN_INFO = "UPDATE magazine SET category_id = ?, price = ?, publication_date = ?, image_url = ? WHERE id = ?";
    public static final String UPDATE_MAGAZINE_ADDITIONAL_INFO = "UPDATE magazine_localization SET name = ?, description = ?, publisher = ? WHERE magazine_id = ? AND locale_id = ?";
    public static final String DELETE_MAGAZINE = "DELETE FROM magazine WHERE id = ?";

    public static String NAME_PARAMETER(String name){
        return name + "%'";
    }

    public static String SORT_PARAMETER(String sortBy){
        return " ORDER BY " + sortBy;
    }

    public static String LIMIT_PARAMETER(int pageNum, int magazinesOnPage){
        return " LIMIT " + pageNum * magazinesOnPage + ", " + magazinesOnPage;
    }

    public static final String FIND_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";
    public static final String FIND_ALL_CATEGORIES = "SELECT * FROM category WHERE locale_id = ?";
    public static final String ADD_CATEGORY_DEFAULT = "INSERT INTO category(id, locale_id, name) VALUES (DEFAULT, ?, ?)";
    public static final String ADD_CATEGORY = "INSERT INTO category(id, locale_id, name) VALUES (?, ?, ?)";
    public static final String UPDATE_CATEGORY = "UPDATE category SET name = ? WHERE id = ? AND locale_id = ?";
    public static final String DELETE_CATEGORY = "DELETE FROM category WHERE id = ?";

    public static final Map<String, String> sortingTypes = new HashMap<>();

    static {
        sortingTypes.put("catalog.order.param.name", "name");
        sortingTypes.put("catalog.order.param.price", "price");
        sortingTypes.put("catalog.order.param.publication.date", "publication_date");
    }

}
