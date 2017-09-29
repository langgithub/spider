package com.champion.spider.utils;

import com.champion.spider.download.Downloader;
import com.champion.spider.download.WebClient;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public class PooledClientFactory {
	private final static PooledClientFactory instance = new PooledClientFactory();
	private final GenericObjectPool clientPool = new GenericObjectPool();

	public PooledClientFactory() {
		clientPool.setFactory(new PoolableObjectFactory() {
			@Override
			public boolean validateObject(Object arg0) {
				return false;
			}

			@Override
			public void passivateObject(Object arg0) throws Exception {
			}

			@Override
			public Object makeObject() throws Exception {
				WebClient client = new WebClient();
				return client;
			}

			@Override
			public void destroyObject(Object arg0) throws Exception {
				Downloader client = (Downloader) arg0;
				client = null;
			}

			@Override
			public void activateObject(Object arg0) throws Exception {
			}
		});
	}

	public static PooledClientFactory getInstance() {
		return instance;
	}

	public WebClient getClient() throws Exception {
		return (WebClient) this.clientPool.borrowObject();
	}

	public void returnClient(Downloader client) throws Exception {
		if (client!=null){
			this.clientPool.returnObject(client);
		}
	}

	public static PooledClientFactory getInstance(int maxActive) {
		instance.clientPool.setMaxActive(maxActive);
		return instance;
	}
}