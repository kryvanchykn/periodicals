package com.periodicals.dao.daoImpl;

import com.periodicals.dao.daoInterfaces.CategoryDao;
import com.periodicals.dao.utils.ConnectionBuilderSetUp;
import com.periodicals.dao.utils.ConnectionPool;
import com.periodicals.entities.*;
import com.periodicals.entities.Locale;
import com.periodicals.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.periodicals.dao.utils.SqlConstants.*;

public class CategoryDaoImpl extends ConnectionBuilderSetUp implements CategoryDao {
    private static final Logger log = LogManager.getLogger(CategoryDaoImpl.class.getName());

    private static final String NAME = "name";
    private static final String LOCALE_ID = "locale_id";

    private static Category mapCategory(ResultSet rs) throws SQLException {
        int k = 0;

        Category newCategory = new Category();
        newCategory.setId(rs.getInt(++k));
        newCategory.setLocaleId(rs.getInt(++k));
        newCategory.setName(rs.getString(++k));

        return newCategory;
    }

    private CategoryLocalization mapLocalizedCategory(ResultSet rs) throws SQLException {
        int k = 0;

        CategoryLocalization newCategoryLocalization = new CategoryLocalization();
        newCategoryLocalization.setId(rs.getInt(++k));

        Map<Integer, String> names = new HashMap<>();

        do {
            Integer localeId = rs.getInt(LOCALE_ID);
            String name = rs.getString(NAME);
            names.put(localeId, name);

        }

        while (rs.next());
        newCategoryLocalization.setNames(names);

        return newCategoryLocalization;
    }


    private void setCategoryLocalizationAttributes(CategoryLocalization categoryLocalization, int localeId,
                                                             PreparedStatement ps) throws SQLException {
        int k = 0;

        ps.setInt(++k, categoryLocalization.getId());
        ps.setInt(++k, localeId);
        ps.setString(++k, categoryLocalization.getNames().get(localeId));
    }

    @Override
    public CategoryLocalization findCategoryById(int categoryId) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_CATEGORY_BY_ID)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapLocalizedCategory(rs);
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.category", e);
        }
    }

    @Override
    public List<Category> findAllCategories(int localeId) throws DBException {
        List<Category> categories = new ArrayList<>();
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_CATEGORIES)){
            int k = 0;
            ps.setInt(++k, localeId);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    categories.add(mapCategory(rs));
                }
                return categories;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.all.categories", e);
        }
    }

    @Override
    public void addCategory(CategoryLocalization categoryLocalization) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psDefault = null;
        try {
            con = this.getConnectionBuilder().getConnection();
            con.setAutoCommit(false);

            psDefault = con.prepareStatement(ADD_CATEGORY_DEFAULT, Statement.RETURN_GENERATED_KEYS);
            ps = con.prepareStatement(ADD_CATEGORY);

            int k = 0;
            int localeId = Arrays.stream(Locale.values()).findFirst().get().getId();
            psDefault.setInt(++k, localeId);
            psDefault.setString(++k, categoryLocalization.getNames().get(localeId));
            psDefault.executeUpdate();
            ResultSet rs = psDefault.getGeneratedKeys();
            rs.next();
            categoryLocalization.setId(rs.getInt(1));

            for (Locale locale: Arrays.stream(Locale.values()).skip(1).collect(Collectors.toList())) {
                setCategoryLocalizationAttributes(categoryLocalization, locale.getId(), ps);
                ps.executeUpdate();
            }

            con.commit();

        } catch (SQLException e) {
            if (con != null) ConnectionPool.rollback(con);
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.add.category", e);
        } finally {
            ConnectionPool.close(ps);
            ConnectionPool.close(psDefault);
            ConnectionPool.close(con);
        }
    }

    @Override
    public void updateCategory(CategoryLocalization categoryLocalization) throws Exception {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_CATEGORY)) {
            con.setAutoCommit(false);

            int k;
            for (Locale locale : Locale.values()) {
                k = 0;
                ps.setString(++k, categoryLocalization.getNames().get(locale.getId()));
                ps.setInt(++k, categoryLocalization.getId());
                ps.setInt(++k, locale.getId());
                ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.update.category", e);
        }
    }

    @Override
    public void deleteCategory(int categoryId) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_CATEGORY)) {
            con.setAutoCommit(false);
            ps.setInt(1, categoryId);
            if (ps.executeUpdate() == 0) {
                throw new DBException("error.page.delete.category.failed");
            }
            con.commit();
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.delete.category", e);
        }
    }


}
