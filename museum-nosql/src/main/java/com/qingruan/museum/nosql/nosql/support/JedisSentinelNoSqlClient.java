package com.qingruan.museum.nosql.nosql.support;

import java.io.IOException;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ClassUtils;

import redis.clients.jedis.Jedis;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;
import com.qingruan.museum.framework.util.ExceptionLogUtil;
import com.qingruan.museum.nosql.redis.GZipUtils;


/**
 * 
 * @author tommy
 */
@Slf4j
public class JedisSentinelNoSqlClient implements NoSqlClient {
	@Setter
	private ShardedJedisSentinelPoolTemplate template;
	
	@Override
	public void set(Object key, final int expiry, Object value) {
		final byte[] rawKey = rawKey(value.getClass(), key);
		//final byte[] rawValue = rawValue(value);
        try {
            final byte[] zipValue = GZipUtils.compress(rawValue(value));
            template.run(rawKey, new ShardedJedisSentinelPoolCallback<Boolean>() {
                @Override
                public Boolean execute(Jedis jedis) {
                    if (expiry <= 0) {
                        jedis.set(rawKey, zipValue);
                    } else {
                        jedis.setex(rawKey, expiry, zipValue);
                    }
                    return Boolean.TRUE;
                }
            });
        } catch (IOException e) {
            log.error("compress error: {}", ExceptionLogUtil.getErrorStack(e));
        }


	}

	@Override
	public void set(Object key, Object value) {
		final byte[] rawKey = rawKey(value.getClass(), key);

        try {
            final byte[] zipValue = GZipUtils.compress(rawValue(value));
            template.run(rawKey, new ShardedJedisSentinelPoolCallback<Boolean>() {
                @Override
                public Boolean execute(Jedis jedis) {
                    jedis.set(rawKey, zipValue);
                    return Boolean.TRUE;
                }
            });
        } catch (IOException e) {
            log.error("compress error: {}", ExceptionLogUtil.getErrorStack(e));
        }

	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz, Object key) {
		final byte[] rawKey = rawKey(clazz, key);
		
		byte[] rawValue = template.run(rawKey, new ShardedJedisSentinelPoolCallback<byte[]>() {
			@Override
			public byte[] execute(Jedis jedis) {
				return jedis.get(rawKey);
			}
		});

        try {
            if(rawValue != null && rawValue.length > 0){
                return (T) deserializeValue(GZipUtils.decompress(rawValue));
            }else{
                return null;
            }
        } catch (IOException e) {
            log.error("deserializeValue error: {}", ExceptionLogUtil.getErrorStack(e));
            return null;
        }
    }

	@Override
	public <T> boolean exist(Class<T> clazz, Object key) {
		final byte[] rawKey = rawKey(clazz, key);
	
		return template.run(rawKey, new ShardedJedisSentinelPoolCallback<Boolean>() {
			@Override
			public Boolean execute(Jedis jedis) {
				return jedis.exists(rawKey);
			}
		});
	}
	
	@Override
	public <T> void delete(Class<T> clazz, Object key) {
		final byte[] rawKey = rawKey(clazz, key);
		
		template.run(rawKey, new ShardedJedisSentinelPoolCallback<Boolean>() {
			@Override
			public Boolean execute(Jedis jedis) {
				jedis.del(rawKey);
				return Boolean.TRUE;
			}
		});
	}

	@Override
	public void stop() {}
	
	public byte[] rawKey(Class<?> clazz, Object key) {
		if (ClassUtils.isPrimitiveOrWrapper(key.getClass()) || key instanceof String) {
			return template.serializeKey((clazz.getName() + "::" + key.toString()));
		} else {
			return template.serializeKey(new ObjectKey(clazz, key));
		}
	}
	
	public byte[] rawValue(Object value) {
		return template.serializeValue(value);
	}

	public Object deserializeValue(byte[] rawValue) {
		return template.deserializeValue(rawValue);
	}
	
	@Getter
	@Setter
	private static class ObjectKey implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private final Class<?> clazz;
		private final Object key;
		
		public ObjectKey(final Class<?> clazz, final Object key) {
			this.clazz = clazz;
			this.key = key;
		}
	}
}
