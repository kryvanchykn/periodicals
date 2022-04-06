package com.periodicals.dao.daoInterfaces;

import com.periodicals.entities.Locale;
import com.periodicals.exceptions.DBException;

import java.util.List;

public interface LocaleDao {
    /**
     * Get locale by lang_name from locale table in database
     * @param lang lang_name of locale
     * @return null if there isn't locale with this lang_name
     * or locale object
     */
    Locale findLocaleByLang(String lang) throws DBException;

    /**
     * Get all locales from locale table in database
     * @return list of locales
     */
    List<Locale> findAllLocales() throws DBException;
}
