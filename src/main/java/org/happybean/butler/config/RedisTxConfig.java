package org.happybean.butler.config;

import org.happybean.butler.remote.Remote;
import org.happybean.butler.transaction.listener.RedisListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author wgt
 * @date 2019-04-08
 * @description
 **/
@Configuration
public class RedisTxConfig {

    private final String HOST = "localhost";

    @Bean("bu_jedis_poll")
    public JedisPool getRedisPooll() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), HOST);
        return pool;
    }

    @Bean("bu_redis_li")
    public RedisListener redisListener() {
        RedisListener redisListener = new RedisListener();
        redisListener.subscribe(Remote.BU_TX);
        Jedis jedis = getRedisPooll().getResource();
        jedis.subscribe(redisListener);
        return redisListener;
    }
}
