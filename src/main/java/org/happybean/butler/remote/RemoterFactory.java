package org.happybean.butler.remote;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
@Component
public class RemoterFactory {

    private final int keyTimeOut = 1000;

    @Resource(name = "bu_jedis_poll")
    private JedisPool jedisPool;

    public static Remoter getRemoter(RegisterType type) {
        Remoter remoter = null;
        if (type.equals(RegisterType.REDIS)) {
            remoter = new RemoterFactory().redisRemoter();
        }
        return remoter;
    }

    private Remoter redisRemoter() {
        Remoter remoter = tx -> {
            Jedis jedis = jedisPool.getResource();
            jedis.setex(Remote.BU_TX, keyTimeOut, serialize(tx));
        };
        return remoter;
    }

    private String serialize(Object o) {
        return JSON.toJSONString(o);
    }
}
