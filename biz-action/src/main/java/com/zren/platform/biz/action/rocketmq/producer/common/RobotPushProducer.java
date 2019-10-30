package com.zren.platform.biz.action.rocketmq.producer.common;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.enums.RobotEnum;
import com.zren.platform.biz.action.handle.DelayQueueHandle;
import com.zren.platform.biz.action.model.zjh.JoinTableMessageMqModel;
import com.zren.platform.common.service.facade.dto.in.robotInfo.AIRobotInitInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.BaseStrategyDTO;
import com.zren.platform.common.service.facade.dto.out.robotInfo.RobotInfoDTO;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.BeanUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.intercomm.util.MqTagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RobotPushProducer {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DelayQueueHandle delayQueueHandle;

    public void push(List<RobotInfoDTO> lst, List<BaseStrategyDTO> strategylist,AIRobotInitInputModelDTO robotInitInputModelDTO){
        try {

            for(int i=0;i<lst.size();i++){
                //保存该桌台机器人ID
                redisTemplate.opsForSet().add(RedisCommon.getUserRedisKey(RedisCommon.ROBOTINFO,robotInitInputModelDTO.getGameId(),robotInitInputModelDTO.getRoomId(),robotInitInputModelDTO.getTableId()),String.valueOf(lst.get(i).getUserId()));

                //处理延迟消息
                JoinTableMessageMqModel model=new JoinTableMessageMqModel();
                model.setCode(1000);
                model.setBrand(robotInitInputModelDTO.getBrand());
                model.setUserId(String.valueOf(lst.get(i).getUserId()));
                model.setBatchId(lst.get(i).getBatchId());
                model.setUsername(lst.get(i).getUserName());
                model.setAgentId(robotInitInputModelDTO.getAgentId());
                model.setAvatar(lst.get(i).getHeadUrl());
                model.setClientId(robotInitInputModelDTO.getClientId());
                model.setDevice("");
                model.setBalance(lst.get(i).getBalance());
                model.setUserType(RobotEnum.ROBOT.toString());
                String tag = MqTagUtil.getTagIn(robotInitInputModelDTO.getGameId(), robotInitInputModelDTO.getRoomId(), robotInitInputModelDTO.getTableId());
                //机器人行为策略KEY值生成
                String redisUserStrategyKey= RedisCommon.getRedisStrategyKey(robotInitInputModelDTO.getGameId(),robotInitInputModelDTO.getRoomId(),robotInitInputModelDTO.getTableId(),String.valueOf(lst.get(i).getUserId()),RedisCommon.STRATEGY);
                //保存机器人行为策略到redis
                for (BaseStrategyDTO baseStrategyDTO:strategylist){
                    if(baseStrategyDTO.getUserId().equals(lst.get(i).getUserId())){
                        redisTemplate.opsForHash().putAll(redisUserStrategyKey, BeanUtil.beanToMap(baseStrategyDTO));
                        //几秒加入桌台
                        Long millisecondJoinTable= baseStrategyDTO.getMillisecondJoinTable();
                        model.setRobotry(Integer.valueOf(baseStrategyDTO.getIsWin()));
                        delayQueueHandle.addDelayQueue(robotInitInputModelDTO.getGameId(), tag, JSONObject.toJSON(model).toString(), millisecondJoinTable);
                    }
                }
            }
        } catch (Exception e) {

            //清空机器人策略缓存数据
            for (BaseStrategyDTO baseZjhStrategyDTO:strategylist){
                //机器人行为策略KEY值生成
                String redisUserStrategyKey= RedisCommon.getRedisStrategyKey(robotInitInputModelDTO.getGameId(),robotInitInputModelDTO.getRoomId(),robotInitInputModelDTO.getTableId(),String.valueOf(baseZjhStrategyDTO.getUserId()),RedisCommon.STRATEGY);
                if(redisTemplate.hasKey(redisUserStrategyKey)){
                    redisTemplate.delete(redisUserStrategyKey);
                }
            }

            throw new RobotSystemException(e, ErrorCodeEnum.ROBOT_PUSH_INIT_SYS);
        }
    }
}
