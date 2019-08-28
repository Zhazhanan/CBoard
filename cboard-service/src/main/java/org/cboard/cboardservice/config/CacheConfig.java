package org.cboard.cboardservice.config;

import org.cboard.cboardservice.cache.EhCacheManager;
import org.cboard.cboardservice.cache.RedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author WangKun
 * @create 2018-07-30
 * @desc
 **/
@EnableCaching
@Configuration
public class CacheConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean(name = "rawDataCache")
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheAlias("jvmAggregator");
        LOGGER.info("ehCacheManager init ok");
        return ehCacheManager;
    }


    //    @Bean(name = "rawDataCache")
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisCacheManager.setRedisTemplate(redisTemplate);
        LOGGER.info("redisCacheManager init ok");
        return redisCacheManager;
    }

    @Bean(name = "lockScript")
    public RedisScript<Long> lockScript() {
        String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end";
        return RedisScript.of(script, Long.class);
    }

    @Bean(name = "unLockScript")
    public RedisScript<Long> releaseLockScript() {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return RedisScript.of(script, Long.class);
    }
}
