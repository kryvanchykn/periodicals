package com.periodicals.dao.daoInterfaces;

import com.periodicals.entities.Category;
import com.periodicals.entities.CategoryLocalization;
import com.periodicals.exceptions.DBException;

import java.util.List;

public interface CategoryDao{
    /**
     * Get category by id from category table in database
     * @param categoryId id of categoryLocalization
     * @return category
     */
    CategoryLocalization findCategoryById(int categoryId) throws DBException;

    /**
     * Get all categories from category table in database
     * @param localeId id of locale
     * @return list of categories
     */
    List<Category> findAllCategories(int localeId) throws DBException;

    /**
     * Add category to category table in database
     * @param categoryLocalization entity of class categoryLocalization
     */
    void addCategory(CategoryLocalization categoryLocalization) throws Exception;

    /**
     * Update category in category table in database
     * @param categoryLocalization entity of class categoryLocalization that is updated
     */
    void updateCategory(CategoryLocalization categoryLocalization) throws Exception;

    /**
     * Delete category from category table in database
     * @param categoryId id of category
     */
    void deleteCategory(int categoryId) throws Exception;
}
