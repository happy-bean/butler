package org.happybean.butler.dubbo.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
@Deprecated
public class DubboDataSourceTransactionManager extends DataSourceTransactionManager {

    private final Map<String, Connection> connectionMap = new HashMap<>();

    public void addConnection(String transactionId, Connection connection) {
        connectionMap.putIfAbsent(transactionId, connection);
    }

    public void removeConnection(String transactionId) {
        if (connectionMap.containsKey(transactionId)) {
            connectionMap.remove(transactionId);
        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        JdbcTransactionObjectSupport txObject = (JdbcTransactionObjectSupport) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (status.isDebug()) {
            this.logger.debug("Committing JDBC transaction on Connection [" + con + "]");
        }

        try {
            con.commit();
        } catch (SQLException var5) {
            throw new TransactionSystemException("Could not commit JDBC transaction", var5);
        }
    }

    public static String getGroupId() {
        return UUID.randomUUID().toString();
    }
}
