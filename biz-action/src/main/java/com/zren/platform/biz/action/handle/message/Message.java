package com.zren.platform.biz.action.handle.message;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列消息
 *
 * @author k.y
 * @version Id: Message.java, v 0.1 2018年11月23日 下午18:21 k.y Exp $
 */
@Data
@ToString
public class Message implements Delayed {

    private String topic;

    private String tag;

    private byte[] body;

    private long excuteTime;// 延迟时长，这个是必须的属性因为要按照这个判断延时时长。

    public Message(String topic, String tag, byte[] body, long delayTime) {
        this.topic = topic;
        this.tag = tag;
        this.body = body;
        this.excuteTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    //延迟任务是否到时就是按照这个方法判断如果返回的是负数则说明到期否则还没到期
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
