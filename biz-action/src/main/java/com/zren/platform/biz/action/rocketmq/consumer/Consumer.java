package com.zren.platform.biz.action.rocketmq.consumer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author k.y
 * @version Id: Consumer.java, v 0.1 2018年11月22日 下午16:31 k.y Exp $
 */
public abstract class Consumer<T1, T2> {


    public final ConcurrentHashMap<String, T2> CONSUMER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();


    /**
     * 初始化消费者
     *
     * @param addr mq 地址
     * @param group 消费集群
     * @param topic 消费主题
     * @param t 监听类
     * @throws Exception 异常
     */
    public abstract void initConsumer(String addr,String group,String topic , T1 t) throws Exception;


    /**
     * 销毁
     */
    public abstract void destroy();

    /**
     * 关闭
     */
    public abstract void shutDown(T2 t);

    public void close(String topic) {
        if (CONSUMER_CONCURRENT_HASH_MAP.containsKey(topic)) {
            shutDown(CONSUMER_CONCURRENT_HASH_MAP.get(topic));
        }
    }
}