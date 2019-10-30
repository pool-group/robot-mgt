package com.zren.platform.biz.action.executor;

import com.zren.platform.biz.action.handle.message.Message;
import com.zren.platform.biz.action.rocketmq.producer.RocketProducer;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.LogUtil;

import java.util.concurrent.DelayQueue;

/**
 * 获取延时消息线程
 *
 * @author k.y
 * @version Id: DelayQueueThread.java, v 0.1 2018年11月26日 下午10:27 k.y Exp $
 */
public class DelayQueueThread implements Runnable{

    private DelayQueue<Message> queue;

    private RocketProducer rocketProducer;

    public DelayQueueThread(DelayQueue<Message> queue,RocketProducer rocketProducer) {
        this.queue = queue;
        this.rocketProducer=rocketProducer;
    }

    @Override
    public void run() {

        boolean flag=true;
        while (flag) {
            try {

                LogUtil.info("DelayQueueThread readying..");
                Message take = queue.take();
                rocketProducer.sendMessage(take.getTopic(),take.getTag(),take.getBody());
            } catch (Exception e) {
                throw new RobotSystemException(ErrorCodeEnum.SEND_MQ_MESSAGE_SYS);
            }finally {
                flag=false;
                LogUtil.info("DelayQueueThread flag=false  Ending..");
            }

        }
    }
}
