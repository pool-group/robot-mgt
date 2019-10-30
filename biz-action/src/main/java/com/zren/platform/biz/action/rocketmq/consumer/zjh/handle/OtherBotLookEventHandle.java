package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tuple.Tuple4x;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.springframework.stereotype.Component;

/**
 * 非当前机器人看牌策略 消息事件处理
 *
 * @author k.y
 * @version Id: CurrentHandle.java, v 0.1 2018年11月23日 下午15:55 k.y Exp $
 */
@Component(value = "zjh_other_bot_look_handle")
public class OtherBotLookEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags,JSONObject object,Tuple4x tuple4x) {

        //机器人行为状态上下文
        ZJHPlayerContext context =new ZJHPlayerContext();

        //0. 初始化上下文基础参数
        super.assembleContext(context,object);
        context.setCode(50030);

        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
        context.setCurrentId((String) tuple4x._4().get());
        context.setGameId(req.getGameId());
        context.setRoomId(req.getRoomId());

        //1. 初始化策略信息
        super.assembleContextStrategy(context,context.getPlayerList());

        //3. 看牌
        if((1+context.getRound())<=5&&context.getCompareList().size()>0&&!context.isAction()&&!context.isCurrentIsLooked()&&super.isHitRate(context.getLookRate())){
            context.setCurrentIsLooked(true);
            context.setAction(true);
            LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], lookRate=[ %s ], robot action：looking..",context.getCurrentId(), context.getStrategyKey(), context.getRealCard(),1+context.getRound(), context.getLookRate()));
            super.sendLookMessage(req,context.getCurrentId());
        }
    }
}
