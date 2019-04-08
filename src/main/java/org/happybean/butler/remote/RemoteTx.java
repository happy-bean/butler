package org.happybean.butler.remote;

import org.happybean.butler.transaction.BuTransactionManager;

import java.io.Serializable;

/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
public class RemoteTx implements Serializable {

    private static final long serialVersionUID = 1513403014009888461L;

    private String transactionId;
    private String groupId;
    private TxCommand command;

    public RemoteTx() {
        this.groupId = BuTransactionManager.getGroupId();
        this.transactionId = BuTransactionManager.getTransactionId(groupId);
    }

    public RemoteTx(TxCommand command) {
        this.groupId = BuTransactionManager.getGroupId();
        this.transactionId = BuTransactionManager.getTransactionId(groupId);
        this.command = command;
    }

    public RemoteTx(String groupId, TxCommand command) {
        this.transactionId = BuTransactionManager.getTransactionId(groupId);
        this.groupId = groupId;
        this.command = command;
    }

    public RemoteTx(String transactionId, String groupId, TxCommand command) {
        this.transactionId = transactionId;
        this.groupId = groupId;
        this.command = command;
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
}
