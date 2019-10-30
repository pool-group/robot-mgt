package com.zren.platform.biz.action.rocketmq.producer;

/**
 * @author k.y
 * @version Id: producer.java, v 0.1 2018年11月22日 下午16:33 k.y Exp $
 */

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息生产者
 */
public abstract class Producer<T> {

    public final ConcurrentHashMap<String, T> PRODUCER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();


    /**
     * 初始化消息发送者
     *
     * @param addr mq 地址
     * @param group 消费集群
     * @param topic 消费主题
     */
    public abstract void initProducer(String addr, String group, String topic) throws Exception;


    /**
     * 发送消息
     *
     * @param topic 主题
     * @param tag tag
     * @param bytes 发送的字节数组
     */
    public abstract void sendMessage(String topic, String tag, byte[] bytes) throws InterruptedException, RemotingException, MQClientException, MQBrokerException;

    /**
     * 关闭
     */
    public abstract void shutDown(T t);

    /**
     * 销毁，程序销毁时调用
     */
    public abstract void destroy();


    /**
     * 关闭消息发送者
     *
     * @param topic topic
     */
    public void close(String topic) {
        if (PRODUCER_CONCURRENT_HASH_MAP.containsKey(topic)) {
            shutDown(PRODUCER_CONCURRENT_HASH_MAP.get(topic));
        }
    }
}