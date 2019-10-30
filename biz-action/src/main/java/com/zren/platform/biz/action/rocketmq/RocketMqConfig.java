package com.zren.platform.biz.action.rocketmq;

/**
 * @author k.y
 * @version Id: RocketMqConfig.java, v 0.1 2018年11月22日 下午15:41 k.y Exp $
 */

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@ConfigurationProperties(prefix = "com.zren.rocket")
@Component
public class RocketMqConfig {

    /**
     * 消费mq配置
     */
    private Map<String, RocketMqProp> consumer = new HashMap<>();

    /**
     * 消息生产mq配置
     */
    private Map<String, RocketMqProp> producer = new HashMap<>();

    /**
     * 获取配置中的topic
     */
    public String getTopicByProducer(String gameAlias) {
        if (producer.containsKey(gameAlias)) {
            return producer.get(gameAlias).getTopic();
        }
        return null;
    }


    public String getAdaptByProducer(String gameAlias) {
        if (producer.containsKey(gameAlias)) {
            return producer.get(gameAlias).getAdapt();
        }
        return null;
    }


    public String getHandlerByProducer(String gameAlias) {
        if (producer.containsKey(gameAlias)) {
            return producer.get(gameAlias).getHandler();
        }
        return null;
    }
}
