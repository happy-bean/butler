package org.happybean.butler.transaction.listener;

import org.happybean.butler.remote.RemoteTx;

/**
 * @author wgt
 * @date 2019-04-04
 * @description
 **/
public interface TransactionListener {

    void doCommit(RemoteTx remoteTx);

    void doRollBack(RemoteTx remoteTx);
}
