package org.happybean.butler.transaction.listener;

import com.alibaba.fastjson.JSONObject;
import org.happybean.butler.remote.RemoteTx;
import org.happybean.butler.remote.TxCommand;
import org.happybean.butler.transaction.BuTransactionManager;
import redis.clients.jedis.JedisPubSub;

import java.sql.SQLException;

/**
 * @author wgt
 * @date 2019-04-08
 * @description
 **/
public class RedisListener extends JedisPubSub implements TransactionListener {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("Message received: " + message.toString());
        RemoteTx remoteTx = JSONObject.parseObject(message, RemoteTx.class);
        doExcute(remoteTx);
    }

    void doExcute(RemoteTx remoteTx) {
        if (remoteTx.getCommand().equals(TxCommand.COMMIT)) {
            doCommit(remoteTx);
        } else {
            doRollBack(remoteTx);
        }
    }

    @Override
    public void doCommit(RemoteTx remoteTx) {
        try {
            BuTransactionManager.doCommit(remoteTx.getGroupId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doRollBack(RemoteTx remoteTx) {
        try {
            BuTransactionManager.doRollBack(remoteTx.getGroupId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
