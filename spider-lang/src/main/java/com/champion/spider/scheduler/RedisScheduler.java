package com.champion.spider.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.champion.spider.download.WebRequest;
import com.champion.spider.utils.RedisUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * Created by root on 2017/8/25.
 */
public class RedisScheduler  implements Scheduler{

    private static Logger LOG=Logger.getLogger(RedisScheduler.class);

    public String key;

    public RedisScheduler(String key) {
        this.key = key;
    }

    public Jedis getRedis() {
        Jedis jedis = null;

        while (true) {
            try {
                jedis = RedisUtils.getJedis();
            } catch (Exception e) {
                LOG.warn(e.getMessage());
            }

            if (jedis != null) {
                break;
            }

            try {
                Thread.sleep(5000);
                LOG.warn("redis获取失败，重新获取中...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jedis;
    }

    @Override
    public void put(WebRequest request) {
        if (request==null) {
            LOG.info("WebRequest 为空，请检查");
            return;
        }
        Jedis jedis = getRedis();
        jedis.sadd(key, JSON.toJSONString(request));
        RedisUtils.returnResource(jedis);
    }

    @Override
    public WebRequest poll() {
        Jedis jedis = getRedis();
        String member = jedis.spop(key);
        RedisUtils.returnResource(jedis);
        if(member==null){
            return null;
        }else {
            return JSONObject.parseObject(member,WebRequest.class);
        }
    }
}
