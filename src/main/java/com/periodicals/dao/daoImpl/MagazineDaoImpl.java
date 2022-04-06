package com.periodicals.dao.daoImpl;

import com.periodicals.dao.daoInterfaces.MagazineDao;
import com.periodicals.dao.utils.ConnectionBuilderSetUp;
import com.periodicals.dao.utils.ConnectionPool;
import com.periodicals.entities.Magazine;
import com.periodicals.entities.Locale;
import com.periodicals.entities.MagazineLocalization;
import com.periodicals.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

import static com.periodicals.dao.utils.SqlConstants.*;

public class MagazineDaoImpl extends ConnectionBuilderSetUp implements MagazineDao {
    private static final Logger log = LogManager.getLogger(MagazineDaoImpl.class.getName());


    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PUBLISHER = "publisher";
    private static final String LOCALE_ID = "locale_id";

    private Magazine mapMagazine(ResultSet rs) throws SQLException {
        int k = 0;

        Magazine newMagazine = new Magazine();
        newMagazine.setId(rs.getInt(++k));
        newMagazine.setCategoryId(rs.getInt(++k));
        newMagazine.setPrice(rs.getDouble(++k));
        newMagazine.setPublicationDate(rs.getDate(++k));
        newMagazine.setImageURL(rs.getString(++k));
        newMagazine.setName(rs.getString(++k));
        newMagazine.setDescription(rs.getString(++k));
        newMagazine.setPublisher(rs.getString(++k));

        return newMagazine;
    }

    private MagazineLocalization mapLocalizedMagazine(ResultSet rs) throws SQLException {
        int k = 0;

        MagazineLocalization newMagazineLocalization = new MagazineLocalization();
        newMagazineLocalization.setId(rs.getInt(++k));
        newMagazineLocalization.setCategoryId(rs.getInt(++k));
        newMagazineLocalization.setPrice(rs.getDouble(++k));
        newMagazineLocalization.setPublicationDate(rs.getDate(++k));
        newMagazineLocalization.setImageURL(rs.getString(++k));

        Map<Integer, String> names = new HashMap<>();
        Map<Integer, String> descriptions = new HashMap<>();
        Map<Integer, String> publishers = new HashMap<>();

        do {
            Integer localeId = rs.getInt(LOCALE_ID);
            String name = rs.getString(NAME);
            String description = rs.getString(DESCRIPTION);
            String publisher = rs.getString(PUBLISHER);
            names.put(localeId, name);
            descriptions.put(localeId, description);
            publishers.put(localeId, publisher);
        }

        while (rs.next());
        newMagazineLocalization.setNames(names);
        newMagazineLocalization.setDescriptions(descriptions);
        newMagazineLocalization.setPublishers(publishers);

        return newMagazineLocalization;
    }

    private void setMagazineLocalizationMainAttributes(MagazineLocalization magazineLocalization, PreparedStatement ps)
            throws SQLException {
        int k = 0;

        ps.setInt(++k, magazineLocalization.getCategoryId());
        ps.setDouble(++k, magazineLocalization.getPrice());
        ps.setDate(++k, magazineLocalization.getPublicationDate());
        ps.setString(++k, magazineLocalization.getImageURL());
    }

    private void setMagazineLocalizationAdditionalAttributes(MagazineLocalization magazineLocalization, int localeId,
                                                             PreparedStatement ps) throws SQLException {
        int k = 0;

        ps.setInt(++k, magazineLocalization.getId());
        ps.setInt(++k, localeId);
        ps.setString(++k, magazineLocalization.getNames().get(localeId));
        ps.setString(++k, magazineLocalization.getDescriptions().get(localeId));
        ps.setString(++k, magazineLocalization.getPublishers().get(localeId));
    }


    @Override
    public MagazineLocalization findMagazineById(int id) throws DBException {
        MagazineLocalization magazineLocalization = null;
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_MAGAZINE_BY_ID)) {

            int k = 0;
            ps.setInt(++k, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    magazineLocalization = mapLocalizedMagazine(rs);
                }
                return magazineLocalization;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.magazine", e);
        }
    }

    @Override
    public List<Magazine> findAllMagazines(int localeId) throws DBException {
        List<Magazine> magazines = new ArrayList<>();
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_MAGAZINES)){
            int k = 0;
            ps.setInt(++k, localeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    magazines.add(mapMagazine(rs));
                }
                return magazines;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.all.magazines", e);
        }
    }

    @Override
    public List<Magazine> findSortedMagazinesOnPage(String sortBy, String name, int magazinesOnPage, int pageNum,
                                                    int localeId) throws DBException {
        List<Magazine> magazines = new ArrayList<>();
        try (Connection c = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = c.prepareStatement(FIND_SORTED_MAGAZINES + NAME_PARAMETER(name) +
                     SORT_PARAMETER(sortBy) + LIMIT_PARAMETER(pageNum, magazinesOnPage))) {
            int k = 0;
            ps.setInt(++k, localeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    magazines.add(mapMagazine(rs));
                }
                return magazines;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.sorted.magazines", e);
        }
    }

    @Override
    public List<Magazine> findSortedMagazinesByCategoryOnPage(int categoryId, String sortBy, String name, int magazinesOnPage,
                                                              int pageNum, int localeId) throws DBException {
        List<Magazine> magazines = new ArrayList<>();
        try (Connection c = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = c.prepareStatement(FIND_SORTED_MAGAZINES_BY_CATEGORY + NAME_PARAMETER(name) +
                     SORT_PARAMETER(sortBy) + LIMIT_PARAMETER(pageNum, magazinesOnPage))) {
            int k = 0;
            ps.setInt(++k, categoryId);
            ps.setInt(++k, localeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    magazines.add(mapMagazine(rs));
                }
                return magazines;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.sorted.magazines", e);
        }
    }

    @Override
    public int getMagazinesNumber(String name, int localeId) throws DBException {
        try (Connection c = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = c.prepareStatement(GET_MAGAZINES_COUNT + NAME_PARAMETER(name))){
             int k = 0;
             ps.setInt(++k, localeId);
             ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.get.magazines.number", e);
        }
    }

    @Override
    public int getMagazinesNumberInCategory(int categoryId, String name, int localeId) throws DBException {
        try (Connection c = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = c.prepareStatement(GET_MAGAZINES_COUNT_IN_CATEGORY + NAME_PARAMETER(name))) {
            int k = 0;
            ps.setInt(++k, categoryId);
            ps.setInt(++k, localeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return -1;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.get.magazines.number", e);
        }
    }


    @Override
    public void addMagazine(MagazineLocalization magazineLocalization) throws Exception {
        Connection con = null;
        PreparedStatement psMain = null;
        PreparedStatement psAdditional = null;
        try {
            con = this.getConnectionBuilder().getConnection();
            con.setAutoCommit(false);

            psMain = con.prepareStatement(ADD_MAGAZINE_MAIN_INFO, Statement.RETURN_GENERATED_KEYS);
            psAdditional = con.prepareStatement(ADD_MAGAZINE_ADDITIONAL_INFO);

            setMagazineLocalizationMainAttributes(magazineLocalization, psMain);
            int changedRows = psMain.executeUpdate();

            ResultSet rs = psMain.getGeneratedKeys();
            rs.next();
            magazineLocalization.setId(rs.getInt(1));

            for (Locale locale : Locale.values()) {
                setMagazineLocalizationAdditionalAttributes(magazineLocalization, locale.getId(), psAdditional);
                psAdditional.executeUpdate();
            }

            if (changedRows == 0) {
                throw new DBException("error.page.add.magazine.failed");
            }

            con.commit();

        } catch (SQLException e) {
            ConnectionPool.rollback(con);
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.add.magazine", e);
        } finally {
            ConnectionPool.close(psAdditional);
            ConnectionPool.close(psMain);
            ConnectionPool.close(con);
        }
    }

    @Override
    public void updateMagazine(MagazineLocalization magazineLocalization) throws Exception {
        Connection con = null;
        PreparedStatement psMain = null;
        PreparedStatement psAdditional = null;
        try {
            con = this.getConnectionBuilder().getConnection();
            con.setAutoCommit(false);

            psMain = con.prepareStatement(UPDATE_MAGAZINE_MAIN_INFO);
            psAdditional = con.prepareStatement(UPDATE_MAGAZINE_ADDITIONAL_INFO);

            int k = 0;
            psMain.setInt(++k, magazineLocalization.getCategoryId());
            psMain.setDouble(++k, magazineLocalization.getPrice());
            psMain.setDate(++k, magazineLocalization.getPublicationDate());
            psMain.setString(++k, magazineLocalization.getImageURL());
            psMain.setInt(++k, magazineLocalization.getId());

            int changedRows = psMain.executeUpdate();

            for (Locale locale : Locale.values()) {
                k = 0;
                psAdditional.setString(++k, magazineLocalization.getNames().get(locale.getId()));
                psAdditional.setString(++k, magazineLocalization.getDescriptions().get(locale.getId()));
                psAdditional.setString(++k, magazineLocalization.getPublishers().get(locale.getId()));
                psAdditional.setInt(++k, magazineLocalization.getId());
                psAdditional.setInt(++k, locale.getId());
                psAdditional.executeUpdate();
            }

            if (changedRows == 0) {
                throw new DBException("error.page.update.magazine.failed");
            }

            con.commit();

        } catch (SQLException e) {
            ConnectionPool.rollback(con);
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.update.magazine", e);
        } finally {
            ConnectionPool.close(psAdditional);
            ConnectionPool.close(psMain);
            ConnectionPool.close(con);
        }
    }

    @Override
    public void deleteMagazine(int magazineId) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_MAGAZINE)) {
            con.setAutoCommit(false);
            ps.setInt(1, magazineId);
            if (ps.executeUpdate() == 0) {
                throw new DBException("error.page.delete.magazine.failed");
            }
            con.commit();
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.delete.magazine", e);
        }
    }
}
