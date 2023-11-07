package com.cn.fastcab.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  自定义RedisTemplate的操作redis的序列化
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/12/10 0010
 * @Version 1.0
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 设置 Redis数据默认过期时间, 默认6小时(不会影响正常 Redis 的过期时间)
     * 设置 @cacheable 序列化方式 (spring cache 序列化方式)
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        // Value 序列化器
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer));
        // 默认 Spring Cache 使用 Redis 的过期时间是 6小时 (不会影响正常 Redis 的过期时间)
        configuration = configuration.entryTtl(Duration.ofHours(6));
        return configuration;
    }

    //创建bean，覆盖原来的，使用自定义RedisTemplate的操作redis的序列化定义
    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        //创建新的RedisTemplate
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //Key序列化方法
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //定义RedisTemplate的key使用什么序列化
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        //Value序列化方法
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        //定义RedisTemplate的value使用什么序列化
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // 全局开启AutoType,这里方便开发,使用全局模式
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        //设置RedisTemplate的连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        //返回自定义的template覆盖原来的
        return template;
    }

    /**
     *  自定义缓存 key 生成策略( Spring Cache )
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            Map<String,Object> container = new HashMap<>(3);
            Class<?> targetClass = target.getClass();
            // 类地址
            container.put("class",targetClass.toGenericString());
            // 方法名称
            container.put("methodName",method.getName());
            // 包名称
            container.put("package",targetClass.getPackage());
            // 参数列表
            for(int i=0; i<params.length; i++){
                container.put(String.valueOf(i),params[i]);
            }
            // 转为 JSON 字符串
            String jsonString = JSON.toJSONString(container);
            // 做 SHA256 哈希得到一个 SHA256 摘要作为 key
            return DigestUtils.sha256Hex(jsonString);
        };
    }

    /**
     *  Value序列化器
     */
    class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
        private final Class<T> clazz;

        FastJsonRedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        /**
         * Serialize the given object to binary data.
         *
         * @param t object to serialize. Can be {@literal null}.
         * @return the equivalent binary data. Can be {@literal null}.
         */
        @Override
        public byte[] serialize(T t) throws SerializationException {
            if(t == null){
                return new byte[0];
            }
            return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
        }

        /**
         * Deserialize an object from the given binary data.
         *
         * @param bytes object binary representation. Can be {@literal null}.
         * @return the equivalent object instance. Can be {@literal null}.
         */
        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if(bytes == null || bytes.length <=0){
                return null;
            }
            String str = new String(bytes,StandardCharsets.UTF_8);
            return JSON.parseObject(str,clazz);
        }
    }

    /**
     *  Key序列化器
     */
    class StringRedisSerializer implements RedisSerializer<Object>{
        private final Charset charset; // 编码
        StringRedisSerializer(){ this(StandardCharsets.UTF_8); }
        private StringRedisSerializer(Charset charset) {
            Assert.notNull(charset,"Charset must not be null!");
            this.charset = charset;
        }

        /**
         * Serialize the given object to binary data.
         *
         * @param object object to serialize. Can be {@literal null}.
         * @return the equivalent binary data. Can be {@literal null}.
         */
        @Override
        public byte[] serialize(Object object) throws SerializationException {
            String string = JSON.toJSONString(object);
            if(StringUtil.isNullOrEmpty(string)){
                return null;
            }
            string = string.replace("\"","");
            return string.getBytes(charset);
        }

        /**
         * Deserialize an object from the given binary data.
         *
         * @param bytes object binary representation. Can be {@literal null}.
         * @return the equivalent object instance. Can be {@literal null}.
         */
        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return (bytes == null ? null:new String(bytes,charset));
        }
    }
}
