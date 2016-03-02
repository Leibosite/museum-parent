package com.qingruan.museum.nosql.redis;

import lombok.Data;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.qingruan.framework.redis.ShardedJedisSentinelPool;

/**
 * 哨兵实体Bean,应用程序直接连接哨兵，哨兵负责连接Redis节点
 * 
 * @author tommy
 * 
 */
@Data
public class ShardedJedisSentinelPoolFactoryBean implements
		FactoryBean<ShardedJedisSentinelPool>, InitializingBean {
	private ShardedJedisSentinelPool jedisSentinelPool;

	private String minIdleStr;
	private String maxIdleStr;
	private String maxTotalStr;
	private String timeoutStr;
	private String testOnBorrowStr;
	private String masterNames;
	private String urls;

	@Override
	public void afterPropertiesSet() throws Exception {
		Integer minIdle = Integer.decode(minIdleStr.trim());
		Integer maxIdle = Integer.decode(maxIdleStr.trim());
		Integer maxTotal = Integer.decode(maxTotalStr.trim());

		boolean testOnBorrow = (testOnBorrowStr != null && "true"
				.equalsIgnoreCase(testOnBorrowStr.trim())) ? true : false;

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMinIdle(minIdle);
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setTestOnBorrow(testOnBorrow);

		String[] masters = masterNames.split(",");

		String[] sentinels = urls.split(",");

		Integer timeout = Integer.decode(timeoutStr.trim());

		jedisSentinelPool = new ShardedJedisSentinelPool(masters, sentinels,
				poolConfig, timeout);
	}

	@Override
	public ShardedJedisSentinelPool getObject() throws Exception {
		return jedisSentinelPool;
	}

	@Override
	public Class<?> getObjectType() {
		return ShardedJedisSentinelPool.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
