package com.periodicals.dao.daoInterfaces;


import com.periodicals.entities.Magazine;
import com.periodicals.entities.MagazineLocalization;
import com.periodicals.exceptions.DBException;

import java.util.List;


public interface MagazineDao{
    /**
     * Get magazine by id from magazine table in database
     * @param magazineId id of magazine
     * @return magazine
     */
    MagazineLocalization findMagazineById(int magazineId) throws DBException;

    /**
     * Get all magazines by locale from magazine table in database
     * @param localeId id of locale
     * @return list of magazines
     */
    List<Magazine> findAllMagazines(int localeId) throws DBException;

    /**
     * Get sorted magazines by parameter from magazine table in database
     * @param sortBy parameter of sorting
     * @param name name of magazine
     * @param magazinesOnPage number of magazines to be shown per page
     * @param pageNum number of page
     * @param localeId id of locale
     * @return list of magazines
     */
    List<Magazine> findSortedMagazinesOnPage(String sortBy, String name, int magazinesOnPage, int pageNum, int localeId) throws DBException;

    /**
     * Get sorted magazines by parameter in category from magazine table in database
     * @param categoryId id of category
     * @param sortBy parameter of sorting
     * @param name name of magazine
     * @param magazinesOnPage number of magazines to be shown per page
     * @param pageNum number of page
     * @param localeId id of locale
     * @return list of magazines
     */
    List<Magazine> findSortedMagazinesByCategoryOnPage(int categoryId, String sortBy, String name, int magazinesOnPage, int pageNum, int localeId) throws DBException;

    /**
     * Get number of magazines by name from magazine table in database
     * @param name name of magazine
     * @param localeId id of locale
     * @return number of magazines
     */
    int getMagazinesNumber(String name, int localeId) throws DBException;

    /**
     * Get number of magazines by name in category from magazine table in database
     * @param categoryId id of category
     * @param name name of magazine
     * @param localeId id of locale
     * @return number of magazines
     */
    int getMagazinesNumberInCategory(int categoryId, String name, int localeId) throws DBException;

    /**
     * Add magazine to magazine table in database
     * @param magazineLocalization magazine
     */
    void addMagazine(MagazineLocalization magazineLocalization) throws Exception;

    /**
     * Update magazine in magazine table in database
     * @param magazineLocalization entity of class magazineLocalization that is updated
     */
    void updateMagazine(MagazineLocalization magazineLocalization) throws Exception;

    /**
     * Delete magazine from magazine table in database
     * @param magazineId id of magazine
     */
    void deleteMagazine(int magazineId) throws DBException;
}
