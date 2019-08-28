package org.cboard.cboardservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author WangKun
 * @create 2019-02-26
 * @desc redis(单节点)分布式锁
 **/
@Component
public class RedisDistributedLock {

    private static final Long SUCCESS = 1L;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    RedisScript<Long> lockScript;
    @Autowired
    RedisScript<Long> unLockScript;

    /**
     * @param: [lockKey, value（所有的客户端必须是唯一的）, expireTime：过期时间（秒）]
     * @describe: 获取锁
     * @author: WangKun
     * @date: 2019-02-26 02:42
     **/
    public boolean lock(String lockKey, String value, int expireTime) {
        boolean ret = false;
        Object result = redisTemplate.execute(lockScript, Collections.singletonList(lockKey), value, expireTime);
        if (SUCCESS.equals(result))
            ret = true;
        return ret;
    }

    /**
     * @param: [key, value]
     * @describe: 释放锁
     * @author: WangKun
     * @date: 2019-02-26 02:44
     **/
    public Object releaseLock(String key, String value) {
        boolean ret = false;
        Object result = redisTemplate.execute(unLockScript, Collections.singletonList(key), value);
        if (SUCCESS.equals(result))
            ret = true;
        return ret;
    }
}
