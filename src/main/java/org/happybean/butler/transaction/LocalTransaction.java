package org.happybean.butler.transaction;

import org.happybean.butler.remote.TxCommand;

import java.sql.Connection;

/**
 * @author wgt
 * @date 2019-04-22
 * @description
 **/
public class LocalTransaction {

    private String transactionId;
    private String groupId;
    private TxCommand command;
    private Connection connection;

    public LocalTransaction(String transactionId, String groupId, Connection connection) {
        this.transactionId = transactionId;
        this.groupId = groupId;
        this.connection = connection;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public TxCommand getCommand() {
        return command;
    }

    public void setCommand(TxCommand command) {
        this.command = command;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
