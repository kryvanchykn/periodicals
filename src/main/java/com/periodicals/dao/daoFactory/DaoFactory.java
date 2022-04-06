package com.periodicals.dao.daoFactory;

import com.periodicals.dao.daoImpl.*;
import com.periodicals.dao.daoInterfaces.*;

/**
 * factory that returns Dao entities
 *
 */

public class DaoFactory {
    public static UserDao createUserDao() {
        return new UserDaoImpl();
    }

    public static SubscriptionDao createSubscriptionDao() {
        return new SubscriptionDaoImpl();
    }

    public static MagazineDao createMagazineDao() {
        return new MagazineDaoImpl();
    }

    public static CategoryDao createCategoryDao() {
        return new CategoryDaoImpl();
    }

    public static LocaleDao createLocaleDao() {
        return new LocaleDaoImpl();
    }
}
