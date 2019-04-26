package org.happybean.butler.transaction;

import org.happybean.butler.remote.RemoteTx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
public class BuTransactionManager {

    //group_id
    private static final Map<String, List<LocalTransaction>> LOCAL_TRANSACTIONS = new ConcurrentHashMap<>();

    public static void addTransactional(LocalTransaction transaction) {
        if (LOCAL_TRANSACTIONS.containsKey(transaction.getGroupId())) {
            LOCAL_TRANSACTIONS.get(transaction.getGroupId()).add(transaction);
        } else {
            ArrayList<LocalTransaction> localTransactions = new ArrayList<>();
            localTransactions.add(transaction);
            LOCAL_TRANSACTIONS.put(transaction.getGroupId(), localTransactions);
        }
    }

    private static void removeTransactional(String transactionId) {
        if (LOCAL_TRANSACTIONS.containsKey(transactionId)) {
            LOCAL_TRANSACTIONS.remove(transactionId);
        }
    }

    public static void doCommit(String groupId) throws SQLException {
        if (LOCAL_TRANSACTIONS.containsKey(groupId)) {
            List<LocalTransaction> localTransactions = LOCAL_TRANSACTIONS.get(groupId);
            for (LocalTransaction transaction : localTransactions) {
                Connection connection = transaction.getConnection();
                connection.commit();
                connClose(connection);
            }
            removeTransactional(groupId);
        }
    }

    public static void doRollBack(String groupId) throws SQLException {
        if (LOCAL_TRANSACTIONS.containsKey(groupId)) {
            List<LocalTransaction> localTransactions = LOCAL_TRANSACTIONS.get(groupId);
            for (LocalTransaction transaction : localTransactions) {
                Connection connection = transaction.getConnection();
                connection.rollback();
                connClose(connection);
            }
            removeTransactional(groupId);
        }
    }

    protected static void connClose(Connection connection) throws SQLException {
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
