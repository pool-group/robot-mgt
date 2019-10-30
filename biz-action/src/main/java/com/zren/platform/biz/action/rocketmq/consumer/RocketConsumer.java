package com.zren.platform.biz.action.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragelyByCircle;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author k.y
 * @version Id: RocketConsumer.java, v 0.1 2018年11月22日 下午16:30 k.y Exp $
 */
@Slf4j
@Service
public class RocketConsumer extends Consumer<MessageListener, DefaultMQPushConsumer> {

    @Override
    public void initConsumer(String addr, String group, String topic, MessageListener listener) throws Exception {
        if (!CONSUMER_CONCURRENT_HASH_MAP.containsKey(topic)) {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
            consumer.setNamesrvAddr(addr);
            consumer.setMessageListener(listener);
            consumer.setMessageModel(MessageModel.BROADCASTING);
            consumer.setAllocateMessageQueueStrategy(new AllocateMessageQueueAveragelyByCircle());
            Map<String, String> map = new HashMap<>();
            map.put(topic, "*");
            consumer.setSubscription(map);
            consumer.setConsumeMessageBatchMaxSize(1);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.start();
            log.info("rocketmq start addr={}, group={},topic={}", addr, group, topic);
        }
    }

    @PreDestroy
    @Override
    public void destroy() {
        CONSUMER_CONCURRENT_HASH_MAP.forEach((topic, mqPushConsumer) -> {
            mqPushConsumer.shutdown();
        });
    }

    @Override
    public void shutDown(DefaultMQPushConsumer consumer) {
        consumer.shutdown();
    }
}