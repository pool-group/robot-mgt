package com.zren.platform.biz.service.impl.handle.zjh;

import com.zren.platform.biz.service.impl.handle.BizBaseHandle;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.BaseStrategyDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import com.zren.platform.common.util.enums.EnumsCommon;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.BeanUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.intercomm.util.MqTagUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 炸金花特殊业务处理
 *
 * @author k.y
 * @version Id: ZjhServiceHandle.java, v 0.1 2018年12月11日 下午17:22 k.y Exp $
 */
@Component
public class ZjhServiceHandle extends BizBaseHandle {

    @Override
    public void excute(RuleOutputModelDTO ruleOutputModelDTO, RuleInputModelDTO ruleInputModelDTO) {

        List<ZjhStrategyInfoDTO> strategylist=ruleOutputModelDTO.getStrategylist();
        List<Long> lst=ruleInputModelDTO.getUserIdlist();
        try {

            for(int i=0;i<lst.size();i++){
                //保存该桌台机器人ID
                redisTemplate.opsForSet().add(RedisCommon.getUserRedisKey(RedisCommon.ROBOTINFO,ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),ruleInputModelDTO.getTableId()),String.valueOf(lst.get(i)));

                String tag = MqTagUtil.getTagIn(ruleInputModelDTO.getGameId(), ruleInputModelDTO.getRoomId(), ruleInputModelDTO.getTableId());
                //机器人行为策略KEY值生成
                String redisUserStrategyKey= RedisCommon.getRedisStrategyKey(ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),ruleInputModelDTO.getTableId(),String.valueOf(lst.get(i)),RedisCommon.STRATEGY);
                //保存机器人行为策略到redis
                for (BaseStrategyDTO baseStrategyDTO:strategylist){
                    if(baseStrategyDTO.getUserId().equals(lst.get(i))){
                        redisTemplate.opsForHash().putAll(redisUserStrategyKey, BeanUtil.beanToMap(baseStrategyDTO));
                    }
                }
            }
        } catch (Exception e) {

            //清空机器人策略缓存数据
            for (BaseStrategyDTO baseZjhStrategyDTO:strategylist){
                //机器人行为策略KEY值生成
                String redisUserStrategyKey= RedisCommon.getRedisStrategyKey(ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),ruleInputModelDTO.getTableId(),String.valueOf(baseZjhStrategyDTO.getUserId()),RedisCommon.STRATEGY);
                if(redisTemplate.hasKey(redisUserStrategyKey)){
                    redisTemplate.delete(redisUserStrategyKey);
                }
            }

            //抛出异常
            throw new RobotSystemException(e, ErrorCodeEnum.ROBOT_PUSH_INIT_SYS);
        }
    }

    @Override
    public Integer getCode() {
        return EnumsCommon.ZJH.getCode();
    }
}
