package com.zren.platform.biz.action.rocketmq.consumer.zjh.listener;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.rocketmq.consumer.common.BaseEventHandle;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.tuple.Tuple4x;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.enums.MessageBindBeanEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.tool.ApplicationContextUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.intercomm.util.MqTagUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service(value = "zjhListener")
public class ZJHConsumerListener implements MessageListenerConcurrently {


    private final ApplicationContextUtil applicationContextUtil;

    private final RedisTemplate redisTemplate;

    private final BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private RedissonClient redisClient;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            onMessage(msgs, context);
        }finally {
            //不管消息是否成功消费，都告诉MQ Sucess
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public RobotBaseResult onMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<String,Void>(){

            @Override
            public void doProcess(EngineContext<String, Void> context){
                msgs.forEach(message -> {
                    String msg = null;
                    try {
                    LogUtil.info(String.format("MQ message: topic=[ %s ], tags=[ %s ]",message.getTopic(),message.getTags()));
                    Tuple4x tuple4x=getRoomConfigByTag(message.getTags());
                    if(!ObjectUtils.allNotNull(tuple4x)){
                        return;
                    }
                    Long startTime=System.currentTimeMillis();
                    RLock lock=redisClient.getLock(tuple4x._1().get()+":lock:"+message.getMsgId());
                    boolean isLocked = false;

                    LogUtil.info(String.format(" zjhListener consumeMessage: topic=[ %s ], tags=[ %s ], body=[ %s ],msgId=[ %s ] ", message.getTopic(), message.getTags(), new String(message.getBody(), StandardCharsets.UTF_8),message.getMsgId()));
                    msg = new String(message.getBody(), StandardCharsets.UTF_8);
                    JSONObject object = null;
                        isLocked = lock.tryLock(0, 30, TimeUnit.SECONDS);
                        if(isLocked){
                            LogUtil.info(String.format(" [ %s ]消息处理获取锁成功：isTrue=[ %s ], 耗时：time= [ %s ]ms, gameId=[ %s ], roomId=[ %s ], tableId=[ %s ],",message.getMsgId(),isLocked,System.currentTimeMillis()-startTime,Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),tuple4x._3().get()));
                            if(StringUtils.contains(msg,"账户缓存")){
                                throw new RobotBizException(ErrorCodeEnum.MQ_MESSAGE_ILLEGAL);
                            }
                            object = JSONObject.parseObject(msg);
                            String userId= (String) tuple4x._4().get();
                            String selfUserId=object.getString("userId");
                            int code = object.getIntValue("code");
                            String tag=MqTagUtil.getTagIn(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._4().get());
                            Set<String> set=redisTemplate.opsForSet().members(RedisCommon.getUserRedisKey(RedisCommon.ROBOTINFO,Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get()));

                            if(!StringUtils.equals(selfUserId,userId)&&set.contains(userId)&&code==MessageBindBeanEnum.ZJH_CURRENT_HANDLE.getCode()){
                                code=MessageBindBeanEnum.ZJH_OTHER_BOT_LOOK_HANDLE.getCode();
                            }
                            String beanName= MessageBindBeanEnum.getName(code);
                            if(org.apache.commons.lang3.StringUtils.isNotBlank(beanName)){
                                if((StringUtils.equals(selfUserId,userId)&&set.contains(userId) && (
                                           code==MessageBindBeanEnum.ZJH_LEAVE_TABLE_HANDLE.getCode()
                                        || code==MessageBindBeanEnum.ZJH_SNAPSHOT_HANDLE.getCode()
                                        || code==MessageBindBeanEnum.ZJH_CHANGE_TABLE_HANDLE.getCode()
                                        || code==MessageBindBeanEnum.ZJH_DRIVE_OUT_HANDLE.getCode()
                                        || code==MessageBindBeanEnum.ZJH_GIVE_UP_HANDLE.getCode()
                                        || code==MessageBindBeanEnum.ZJH_LOOK_HANDLE.getCode()
                                        || code==MessageBindBeanEnum.ZJH_JOIN_TABLE_HANDLE.getCode()
                                        || code==MessageBindBeanEnum.ZJH_CURRENT_HANDLE.getCode()))
                                        ||((set.contains(userId) && code==MessageBindBeanEnum.ZJH_GAME_OVER_HANDLE.getCode())
                                        || (set.contains(userId) && code==MessageBindBeanEnum.ZJH_OTHER_BOT_LOOK_HANDLE.getCode()))
                                ){
                                    BaseEventHandle baseHandle = applicationContextUtil.getInstance().getBean(beanName, BaseEventHandle.class);
                                    if(!org.springframework.util.StringUtils.isEmpty(baseHandle)){
                                        baseHandle.excute(tag,object,tuple4x);
                                    }
                                }
                            }
                        }else {
                            LogUtil.info(String.format(" 消息处理，重复消息获取锁失败：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                        }
                    } catch (Exception e) {
                        LogUtil.error(e,String.format(" 被忽略的异常消息 [ %s ]",msg));
                    }
                });
            }
        });
    }

    private Tuple4x<Integer,Integer,String,String> getRoomConfigByTag(String tag) {
        int count=StringUtils.countMatches(tag,"_");
        if(count==3){
            String[] result=StringUtils.split(tag,"_");
            Tuple4x<Integer,Integer,String,String> tuple4x=new Tuple4x(result[0],result[1],result[2],result[3]);
            return tuple4x;
        }else {
            return null;
        }
    }
}
