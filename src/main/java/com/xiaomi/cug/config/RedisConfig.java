package com.xiaomi.cug.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(factory);

                // 使用 Jackson JSON 序列化
                Jackson2JsonRedisSerializer<Object> jacksonSerializer =
                                new Jackson2JsonRedisSerializer<>(Object.class);

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                mapper.activateDefaultTyping(
                                mapper.getPolymorphicTypeValidator(),
                                ObjectMapper.DefaultTyping.NON_FINAL
                );
                jacksonSerializer.setObjectMapper(mapper);

                // key 和 value 的序列化方式
                template.setKeySerializer(new StringRedisSerializer());
                template.setValueSerializer(jacksonSerializer);

                // hash 的 key、value 序列化方式
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(jacksonSerializer);

                template.afterPropertiesSet();
                return template;
        }
}
