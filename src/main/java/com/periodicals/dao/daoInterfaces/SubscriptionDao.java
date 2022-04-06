package com.periodicals.dao.daoInterfaces;


import com.periodicals.entities.SubscriptionInfo;
import com.periodicals.exceptions.DBException;

import java.util.List;

public interface SubscriptionDao{
    /**
     * Check if user is subscribed on magazine in subscription table in database
     * @param userId id of user
     * @param magazineId id of magazine
     * @return true if user is subscribed on magazine, false otherwise
     */
    boolean isSubscribed(int userId, int magazineId) throws DBException;

    /**
     * Add subscription to subscription table in database
     * @param userId id of user
     * @param magazineId id of magazine
     * @param magazinePrice price of magazine
     */
    void addSubscription(int userId, int magazineId, double magazinePrice) throws Exception;

    /**
     * Get subscriptions with end_date greater than the current one from subscription table in database
     * @param userId id of user
     * @param localeId id of locale
     * @return list of SubscriptionInfo
     */
    List<SubscriptionInfo> findActiveSubscriptionsInfoByUserId(int userId, int localeId) throws Exception;

    /**
     * Get subscriptions with end_date less than the current one from subscription table in database
     * @param userId id of user
     * @param localeId id of locale
     * @return list of SubscriptionInfo
     */
    List<SubscriptionInfo> findExpiredSubscriptionsInfoByUserId(int userId, int localeId) throws Exception;
}
