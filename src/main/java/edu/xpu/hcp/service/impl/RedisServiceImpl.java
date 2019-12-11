package edu.xpu.hcp.service.impl;

import com.alibaba.fastjson.JSON;
import edu.xpu.hcp.service.IRedisService;
import edu.xpu.hcp.service.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author hcp
 */
@Service("redisService")
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis resource = null;
        String str = "";
        try{
            resource = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            str = resource.get(realKey);
        }finally {
            returnToPool(resource);
            return stringToBean(str,clazz);
        }
    }

    @Override
    public <T> boolean set(KeyPrefix prefix,String key,T value) {
        Jedis resource = null;
        try{
            resource = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            int seconds =  prefix.expireSeconds();

            String str = resource.set(realKey,beanToString(value));
            if(str == null || str.length() == 0){
                return false;
            }
            if(seconds <= 0) {
                resource.set(realKey, str);
            }else {
                resource.setex(realKey, seconds, str);
            }
        }finally {
            returnToPool(resource);
            return true;
        }
    }

    /**
     * 判断key是否存在
     * */
    @Override
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     * */
    @Override
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    @Override
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    private <T> T stringToBean(String str,Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if(clazz == String.class){
            return (T) str;
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    private <T> String beanToString(T value){
        if(value == null){
            return null;
        }
        Class<?> valueClass = value.getClass();
        if(valueClass == int.class || valueClass == Integer.class){
            return ""+value;
        }else if(valueClass == String.class){
            return (String) value;
        }else if(valueClass == long.class || valueClass == Long.class){
            return ""+value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    private void returnToPool(Jedis resource) {
        if(resource != null){
            resource.close();
        }
    }
}
