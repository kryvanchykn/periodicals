package com.periodicals.dao.daoImpl;

import com.periodicals.dao.daoInterfaces.SubscriptionDao;
import com.periodicals.dao.utils.ConnectionBuilderSetUp;
import com.periodicals.dao.utils.ConnectionPool;
import com.periodicals.entities.SubscriptionInfo;
import com.periodicals.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.periodicals.dao.utils.SqlConstants.*;


public class SubscriptionDaoImpl extends ConnectionBuilderSetUp implements SubscriptionDao {
    private static final Logger log = LogManager.getLogger(SubscriptionDaoImpl.class.getName());

    private static SubscriptionInfo mapSubscriptionInfo(ResultSet rs) throws SQLException {
        int k = 0;

        SubscriptionInfo newSubscriptionInfo = new SubscriptionInfo();
        newSubscriptionInfo.setMagazineName(rs.getString(++k));
        newSubscriptionInfo.setMagazineCategory(rs.getString(++k));
        newSubscriptionInfo.setStartDate(rs.getDate(++k));
        newSubscriptionInfo.setEndDate(rs.getDate(++k));

        return newSubscriptionInfo;
    }

    private void setSubscriptionAttributes(int userId, int magazineId, PreparedStatement ps) throws SQLException {
        int k = 0;
        ps.setInt(++k, userId);
        ps.setInt(++k, magazineId);
        ps.setDate(++k, Date.valueOf(LocalDate.now()));
        ps.setDate(++k, Date.valueOf(LocalDate.now().plusMonths(SUBSCRIPTION_PERIOD)));
    }

    private List<SubscriptionInfo> findSubscriptionsInfoByUserId(int userId, int localeId, boolean isActive) throws Exception {
        List<SubscriptionInfo> subscriptionInfo = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.getConnectionBuilder().getConnection();

            if (isActive){
                ps = con.prepareStatement(FIND_SUBSCRIPTION_INFO_BY_USER_ID + " >= '" + LocalDate.now() + "'");
            } else {
                ps = con.prepareStatement(FIND_SUBSCRIPTION_INFO_BY_USER_ID + " < '" + LocalDate.now() + "'");
            }

            int k = 0;
            ps.setInt(++k, userId);
            ps.setInt(++k, localeId);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    subscriptionInfo.add(mapSubscriptionInfo(rs));
                }
                return subscriptionInfo;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.get.subscription.info", e);
        } finally {
            ConnectionPool.close(ps);
            ConnectionPool.close(con);
        }
    }


    @Override
    public boolean isSubscribed(int userId, int magazineId) throws DBException {
        boolean isSubscribed = false;
        try (Connection con = this.getConnectionBuilder().getConnection();
             PreparedStatement ps = con.prepareStatement(IS_SUBSCRIBED)) {
            int k = 0;
            ps.setInt(++k, userId);
            ps.setInt(++k, magazineId);
            log.info("ps = " + ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isSubscribed = true;
                }
                return isSubscribed;
            }
        } catch (SQLException e) {
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.check.subscription", e);
        }
    }

    @Override
    public void addSubscription(int userId, int magazineId, double magazinePrice) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.getConnectionBuilder().getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(ADD_SUBSCRIPTION);

            setSubscriptionAttributes(userId, magazineId, ps);

            UserDaoImpl userDao = new UserDaoImpl();
            userDao.setConnectionBuilder(this.getConnectionBuilder());
            userDao.withdrawal(userId, magazinePrice);

            if (ps.executeUpdate() == 0) {
                throw new DBException("error.page.add.subscription.failed");
            }
            con.commit();

        } catch (SQLException e) {
            ConnectionPool.rollback(con);
            log.error("Exception: " + e.getMessage());
            throw new DBException("error.page.add.subscription", e);
        } finally {
            ConnectionPool.close(ps);
            ConnectionPool.close(con);
        }
    }

    @Override
    public List<SubscriptionInfo> findActiveSubscriptionsInfoByUserId(int userId, int localeId) throws Exception {
        return findSubscriptionsInfoByUserId(userId, localeId, true);
    }

    @Override
    public List<SubscriptionInfo> findExpiredSubscriptionsInfoByUserId(int userId, int localeId) throws Exception {
        return findSubscriptionsInfoByUserId(userId, localeId, false);
    }

}
