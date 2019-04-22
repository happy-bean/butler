package org.happybean.butler.transaction;

import org.happybean.butler.remote.BuTransactional;
import org.happybean.butler.remote.RemoteTx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
public class BuTransactionManager {

    private static final Map<String, BuTransactional> connectionMap = new HashMap<>();

    public static void addTransactional(String transactionId, BuTransactional transactional) {
        connectionMap.putIfAbsent(transactionId, transactional);
    }

    public static void removeTransactional(String transactionId) {
        if (connectionMap.containsKey(transactionId)) {
            connectionMap.remove(transactionId);
        }
    }

    protected void doCommit(String groupId) throws SQLException {
        Collection<BuTransactional> transactionals = connectionMap.values();

        for (BuTransactional transactional : transactionals) {
            if (transactional.getGroupId().equals(groupId)) {
                transactional.getConnection().commit();
                connClose(transactional.getConnection());
                BuTransactionManager.removeTransactional(transactional.getTransactionId());
            }
        }
    }

    protected void doRollBack(String groupId) throws SQLException {
        Collection<BuTransactional> transactionals = connectionMap.values();

        for (BuTransactional transactional : transactionals) {
            if (transactional.getGroupId().equals(groupId)) {
                transactional.getConnection().rollback();
                connClose(transactional.getConnection());
                BuTransactionManager.removeTransactional(transactional.getTransactionId());
            }
        }
    }

    protected void connClose(Connection connection) throws SQLException {
        connection.close();
    }

    public static synchronized RemoteTx createRemoteTx() {
        RemoteTx remoteTx = new RemoteTx();
        return remoteTx;
    }

    public static synchronized String getGroupId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    public static synchronized String getTransactionId(String groupId) {
        String uuid = UUID.randomUUID().toString();
        return groupId + "-" + uuid.replace("-", "");
    }
}
