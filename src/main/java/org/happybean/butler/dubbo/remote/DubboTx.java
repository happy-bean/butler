package org.happybean.butler.dubbo.remote;

/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
public class DubboTx {

    private String transactionId;
    private String groupId;
    private TxCommand command;

    public DubboTx(String transactionId, String groupId, TxCommand command) {
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
