package org.happybean.butler.remote;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
@FunctionalInterface
public interface Remoter {

    void sendRemoter(RemoteTx tx);
}
