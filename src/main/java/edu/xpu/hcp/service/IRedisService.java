package edu.xpu.hcp.service;

/**
 * @author hcp
 */
public interface IRedisService {
    <T> T get(KeyPrefix prefix,String key,Class<T> clazz);

    <T> boolean set(KeyPrefix prefix,String key,T value);

    <T> boolean exists(KeyPrefix prefix, String key);

    <T> Long incr(KeyPrefix prefix, String key);

    <T> Long decr(KeyPrefix prefix, String key);
}
