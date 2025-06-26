package com.xiaomi.cug.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class RedisCacheDelayDelete {
        @Autowired
        private  RedisTemplate<String, Object> redisTemplate;
        @Autowired
        private ExecutorService delayDeleteExecutor;

        public void delayDelete(String key, long delayMs) {
                // 第一次删除缓存
                redisTemplate.delete(key);
                // 延迟再次删除缓存
                delayDeleteExecutor.submit(() -> {
                        try {
                                Thread.sleep(delayMs);
                                redisTemplate.delete(key);
                                log.debug("延迟双删执行成功: {}", key);
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                log.error("延迟双删线程中断, key={}", key, e);
                        } catch (Exception ex) {
                                log.error("延迟双删异常, key={}", key, ex);
                        }
                });
        }

        public void delayDelete(List<String> keys, long delayMs) {
                // 第一次删除缓存
                redisTemplate.delete(keys);
                // 延迟再次删除缓存
                delayDeleteExecutor.submit(() -> {
                        try {
                                Thread.sleep(delayMs);
                                redisTemplate.delete(keys);
                                log.debug("延迟双删执行成功: {}", keys);
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                log.error("延迟双删线程中断, keys={}", keys, e);
                        } catch (Exception ex) {
                                log.error("延迟双删异常, keys={}", keys, ex);
                        }
                });
        }

        public void delayDeleteHashFields(String redisKey, List<String> fields, long delayMs) {
                // 第一次删除
                redisTemplate.opsForHash().delete(redisKey, fields.toArray());

                // 延迟再次删除
                delayDeleteExecutor.submit(() -> {
                        try {
                                Thread.sleep(delayMs);
                                redisTemplate.opsForHash().delete(redisKey, fields.toArray());
                                log.debug("延迟双删 Hash 成功: key={}, fields={}", redisKey, fields);
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                log.error("延迟双删线程中断: key={}, fields={}", redisKey, fields, e);
                        } catch (Exception ex) {
                                log.error("延迟双删异常: key={}, fields={}", redisKey, fields, ex);
                        }
                });
        }
}
