package org.happybean.butler.transaction.listener;

import redis.clients.jedis.JedisPubSub;

/**
 * @author wgt
 * @date 2019-04-08
 * @description
 **/
public class RedisListener extends JedisPubSub {

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPSubscribe "
                + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

        System.out.println("onPMessage pattern "
                + pattern + " " + channel + " " + message);
    }
}
