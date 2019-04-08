package org.happybean.butler.config;

import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author wgt
 * @date 2019-04-08
 * @description
 **/
public class RedisConfig {

    private final String HOST = "localhost";

    @Bean("jedis")
    public Jedis getRedisResource() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), HOST);
        return pool.getResource();
    }

}
