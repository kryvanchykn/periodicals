package com.periodicals.dao.daoImpl;

import com.periodicals.dao.daoInterfaces.LocaleDao;
import com.periodicals.dao.utils.ConnectionBuilderSetUp;
import com.periodicals.entities.Locale;
import com.periodicals.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.periodicals.dao.utils.SqlConstants.*;

public class LocaleDaoImpl extends ConnectionBuilderSetUp implements LocaleDao {
    private static final Logger log = LogManager.getLogger(LocaleDaoImpl.class.getName());


    private static Locale mapLocale(ResultSet rs) throws SQLException {
        int k = 0;

        Locale newLocale = Locale.localeById(rs.getInt(++k));
        assert newLocale != null;
        newLocale.setPriceSign(rs.getString(++k));
        newLocale.setExchangeRate(rs.getDouble(++k));

        return newLocale;
    }

    @Override
    public Locale findLocaleByLang(String lang) throws DBException {
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_LOCALE_BY_LANG)) {
            ps.setString(1, lang);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapLocale(rs);
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.locale", e);
        }
    }


    @Override
    public List<Locale> findAllLocales() throws DBException {
        List<Locale> locales = new ArrayList<>();
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_LOCALES);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                locales.add(mapLocale(rs));
            }
            return locales;
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.find.all.locales", e);
        }
    }
}
