package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.biz.action.enums.RobotEnum;
import com.zren.platform.common.util.Tuple.Tuple4x;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 游戏结束 消息事件处理
 *
 * @author k.y
 * @version Id: GameOverHandle.java, v 0.1 2018年12月01日 下午16:52 k.y Exp $
 */
@Component(value = "zjh_game_over_handle")
public class GameOverEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {

        //机器人逻辑状态上下文
        ZJHPlayerContext context =new ZJHPlayerContext();

        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
        context.setCurrentId((String) tuple4x._4().get());

        List<Map<String,Object>> lst=(List) JSON.parseArray(object.getString("infoForRobots"), HashMap.class);//当前机器人台桌所有用户列表(包括机器人)
        context.setMinEntry(object.getDouble("minEntry"));
        context.setBrand(object.getString("brand"));
        context.setCode(object.getInteger("code"));
        context.setPlayerList(lst);

        String infos=object.getString("i");
        Map<String,Object> map = JSONObject.parseObject(infos);
        Set<String> robotSet=redisTemplate.opsForSet().members(RedisCommon.getUserRedisKey(RedisCommon.ROBOTINFO,req.getGameId(),req.getRoomId(),req.getTableId()));

        int robotCount=0;
        int realPlayerCount=0;
        for(Map userMap:context.getPlayerList()){
            if(RobotEnum.ROBOT.toString().equals(userMap.get("userType"))){
                robotCount++;
            }else {
                realPlayerCount++;
            }
            context.setAgentId(888888L);
            context.setClientId(999999L);
            context.setBrand(userMap.get("brand").toString());
        }

        LogUtil.info(String.format(" 桌台 [ %s ] 游戏结束: 玩家数量共 [ %s ] 人, 其中机器人数量为 [ %s ] 人",req.getTableId(),context.getPlayerList().size(),robotCount));

        for (String userId : map.keySet()) {
            for(Map<String,Object> userMap:context.getPlayerList()){
                if(userMap.get("userId").equals(userId)&&robotSet.contains(userId)){
                    Integer i= super.redisForIncrement(req,userId,"leaveCount");
                    context.setCurrentId(userId);
                    context.setBalanceScore(new BigDecimal(userMap.get("balanceScore").toString()));
                    context.setBatchId((Long) userMap.get("batchId"));
                    context.setGainLossScore(new BigDecimal(map.get(userId).toString().split("_")[0]));
                    //更新机器人账变
//                    super.updateRobotAccount(context,req);
                    /***
                     * 离开桌台的3个因素：
                     *      1.次数限制
                     *      2.余额不足
                     *      3.桌台只剩下机器人
                     */
                    //离开桌台
                    if(i<0/*||context.getBalanceScore().compareTo(BigDecimal.valueOf(context.getMinEntry()))<=0*/||robotCount==context.getPlayerList().size()){
                        LogUtil.info(String.format(" code=[ %s ], 桌台 [ %s ], 游戏结束: 机器人[ %s ]发起离开房间申请",context.getCode(),req.getTableId(),String.valueOf(userMap.get("userId"))));
                        super.sendLeaveMessage(req,userId);
                    }
                    //机器人准备
                    else {
                        Long areadyMillisecond=Long.valueOf(DataUtil.randomNumber(1,5,1)[0])*1000;
                        super.sendAlreadyMessage(req,userId,areadyMillisecond);
                    }
                }
            }
        }

        //是否满足机器人推送
        this.robotProcess(context,req,realPlayerCount);
    }
}
