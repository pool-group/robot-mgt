package com.zren.platform.biz.action.rocketmq.consumer.common;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.handle.DelayQueueHandle;
import com.zren.platform.biz.action.model.zjh.BaseMqModel;
import com.zren.platform.biz.action.rocketmq.RocketMqConfig;
import com.zren.platform.biz.action.rocketmq.producer.RocketProducer;
import com.zren.platform.common.service.facade.dto.out.BaseStrategyDTO;
import com.zren.platform.common.util.Tuple.Tuple4x;
import com.zren.platform.common.util.enums.EnumsCommon;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.BeanUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.core.rule.factory.ProbabilisticFactory;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import com.zren.platform.intercomm.util.MqTagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * handle基类
 *
 * @author k.y
 * @version Id: BaseHandle.java, v 0.1 2018年11月27日 下午11:53 k.y Exp $
 */
@Component
public abstract class BaseEventHandle {

    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    protected RocketProducer rocketProducer;

    @Autowired
    protected RocketMqConfig rocketMqConfig;

    @Autowired
    protected DelayQueueHandle delayQueueHandle;

    @Autowired
    protected ProbabilisticFactory probabilisticFactory;

    public abstract void excute(String tags,JSONObject object,Tuple4x tuple4x);

    /**
     * 通过key, hashKey获取hash值
     *
     * @param req
     * @param userId
     * @param hashKey
     * @return
     */
    public String getRedisValueByKey(RoomConfigReq req, String userId, String hashKey){
        //策略KEY值生成
        String rediskey=RedisCommon.getRedisStrategyKey(req.getGameId(),req.getRoomId(),req.getTableId(),userId,RedisCommon.STRATEGY);
        return String.valueOf(redisTemplate.opsForHash().get(rediskey,hashKey));
    }

    /**
     * 通过key获取Map值
     *
     * @param req
     * @param userId
     * @return
     */
    public Map<String,Object> entriesRedisMapByKey(RoomConfigReq req, String userId){
        //策略KEY值生成
        String rediskey=RedisCommon.getRedisStrategyKey(req.getGameId(),req.getRoomId(),req.getTableId(),userId,RedisCommon.STRATEGY);
        return redisTemplate.opsForHash().entries(rediskey);
    }

    /**
     * 通过key更新Map值
     *
     * @param req
     * @param userId
     * @return
     */
    public void updateRedisMapByKey(RoomConfigReq req, String userId, BaseStrategyDTO baseStrategyDTO){
        //策略KEY值生成
        String rediskey=RedisCommon.getRedisStrategyKey(req.getGameId(),req.getRoomId(),req.getTableId(),userId,RedisCommon.STRATEGY);
        redisTemplate.opsForHash().putAll(rediskey, BeanUtil.beanToMap(baseStrategyDTO));
    }

    /**
     * redis运算-1
     *
     * @param req
     * @param userId
     * @param key
     * @return
     */
    public Integer redisForIncrement(RoomConfigReq req, String userId, String key){
        //策略KEY值生成
        String zjhRedisTableUserKey=RedisCommon.getRedisStrategyKey(req.getGameId(),req.getRoomId(),req.getTableId(),userId,RedisCommon.STRATEGY);
        return Math.toIntExact(redisTemplate.opsForHash().increment(zjhRedisTableUserKey, key, -1));
    }

    /**
     * 获取发送消息Topic
     *
     * @param gameId
     * @return
     */
    public String getSendTopic(Integer gameId){
        //获取Topic
        return rocketMqConfig.getProducer().get(EnumsCommon.getName(gameId)).getTopic();
    }

    /**
     * 发送消息
     *
     * @param req
     * @param baseModel
     */
    public void sendMessage(RoomConfigReq req,BaseMqModel baseModel){
        String sendTopic=null;
        String tag=null;
        try {
            sendTopic=getSendTopic(req.getGameId());
            //获取Tag
            tag= MqTagUtil.getTagIn(req.getGameId(), req.getRoomId(), req.getTableId());
            rocketProducer.sendMessage(sendTopic,tag,JSONObject.toJSON(baseModel).toString().getBytes());
        } catch (Exception e) {
            throw new RobotSystemException(ErrorCodeEnum.SEND_MQ_MESSAGE_SYS,String.format("RoomConfigReq=[ %s ],baseModel=[ %s ],sendTopic=[ %s ],tag=[ %s ]",req,baseModel,sendTopic,tag));
        }
    }

    /**
     * 发送延迟消息
     *
     * @param req
     * @param baseModel
     */
    public void sendDelayMessage(RoomConfigReq req,BaseMqModel baseModel,Long millisecond){
        String tag=null;
        try {
            //获取Tag
            tag= MqTagUtil.getTagIn(req.getGameId(), req.getRoomId(), baseModel.getUserId());
            delayQueueHandle.addDelayQueue(req.getGameId(), tag, JSONObject.toJSON(baseModel).toString(),millisecond);
        } catch (Exception e) {
            throw new RobotSystemException(ErrorCodeEnum.SEND_DELAY_MESSAGE_SYS,String.format("RoomConfigReq=[ %s ],baseModel=[ %s ],tag=[ %s ]",req,baseModel,tag));
        }
    }

    /**
     * 是否命中概率事件
     *
     * @param rate
     * @return
     */
    public Boolean isHitRate(double rate){
        return 1==probabilisticFactory.excute(BigDecimal.valueOf(rate))?true:false;
    }

}
