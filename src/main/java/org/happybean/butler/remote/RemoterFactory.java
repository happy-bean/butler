package org.happybean.butler.remote;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
public class RemoterFactory {

    private final int keyTimeOut = 1000;

    @Autowired
    private Jedis jedis;

    public static Remoter getRemoter(RegisterType type) {
        Remoter remoter = null;
        if (type.equals(RegisterType.REDIS)) {
            remoter = new RemoterFactory().redisRemoter();
        }
        return remoter;
    }

    private Remoter redisRemoter() {
        Remoter remoter = tx -> {
            jedis.setex(Remote.REMOTE_TX, keyTimeOut, serialize(tx));
        };
        return remoter;
    }

    private String serialize(Object o) {
        return JSON.toJSONString(o);
    }
}
