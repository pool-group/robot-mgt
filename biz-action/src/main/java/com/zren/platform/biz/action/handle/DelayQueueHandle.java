package com.zren.platform.biz.action.handle;

import com.zren.platform.biz.action.executor.DelayQueueThread;
import com.zren.platform.biz.action.handle.message.Message;
import com.zren.platform.biz.action.rocketmq.RocketMqConfig;
import com.zren.platform.biz.action.rocketmq.producer.RocketProducer;
import com.zren.platform.common.util.enums.EnumsCommon;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.tool.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 延时队列操作类
 *
 * @author k.y
 * @version Id: DelayQueueHandle.java, v 0.1 2018年11月23日 下午18:29 k.y Exp $
 */
@RequiredArgsConstructor
@Component
public class DelayQueueHandle {

    private final RocketProducer rocketProducer;

    private final RocketMqConfig rocketMqConfig;

    /**
     * 添加延时消息
     *
     * @param gameId
     * @param tag
     * @param json
     * @param delayTime
     */
    public void addDelayQueue(Integer gameId, String tag, String json, long delayTime){

        LogUtil.info(String.format(" Add DelayQueue Message: MessageMqModel=[ %s ]", json));
        //获取Topic
        String topic = rocketMqConfig.getProducer().get(EnumsCommon.getName(gameId)).getTopic();
        if(null==topic){
            throw new RobotBizException(ErrorCodeEnum.TOPIC_MATCH_NULL);
        }
        //创建延时队列
        DelayQueue<Message> queue=new DelayQueue();
        //创建延时消息
        Message message = new Message(topic, tag, json.getBytes(), delayTime);
        //将延时消息放到延时队列中
        queue.offer(message);

        ExecutorService exec = null;
        try {
            exec = Executors.newFixedThreadPool(1);
            exec.execute(new DelayQueueThread(queue,rocketProducer));
        } finally {
            exec.shutdown();
        }
    }

}
