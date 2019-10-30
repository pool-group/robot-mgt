package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.common.util.tuple.Tuple4x;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(value = "zjh_look_handle")
public class LookEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {

        ZJHPlayerContext context =new ZJHPlayerContext();

        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
        context.setCurrentId((String) tuple4x._4().get());
        context.setGameId(req.getGameId());
        context.setRoomId(req.getRoomId());

        super.assembleContext(context,object);

        super.assembleContextStrategy(context,context.getPlayerList());

        if(context.getCompareList().size()>0&&!context.isAction()&&context.getBalanceScore().compareTo(BigDecimal.valueOf(context.getCurrentShouldBetScore()))<=0){
            context.setAction(true);
            super.sendAloneMessage(req,context.getCurrentId());
        }

        if(context.getCompareList().size()>0&&!context.isAction()&&super.isHitRate(context.getLookToGiveUpRate())){
            context.setAction(true);
            Long giveUpMillisecond=Long.valueOf(DataUtil.randomNumber(3,6,1)[0])*1000;
            LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], giveupRate=[ %s ], robot action：giveup..",context.getCurrentId(),context.getStrategyKey(), context.getRealCard(),1+context.getRound(),context.getGiveUpRate()));
            super.sendGiveUpMessage(req,context.getCurrentId(),giveUpMillisecond);
        }

        if(context.getCompareList().size()>0&&!context.isAction()&&((1+context.getRound())>=context.getVsmax()||(1+context.getRound())>=context.getVsmin()&&super.isHitRate(context.getVsRate()))){
            context.setAction(true);
            if(StringUtils.isBlank(context.getVsRandomUserId())){
                LogUtil.warn(String.format(" 机器人 [ %s ], 发起 [ %s ] 比牌, compareUserId=[ %s ], 无法进行比牌",context.getCurrentId(),context.getCode(),context.getVsRandomUserId()));
                return;
            }
            LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], vsRate=[ %s ], robot action：vsing....",context.getCurrentId(), context.getStrategyKey(), context.getRealCard(),1+context.getRound(), context.getVsRate()));
            super.sendVsMessage(req,context.getCurrentId(),context.getVsRandomUserId());
        }

        if(context.getCompareList().size()>0&&!context.isAction()&&super.isHitRate(context.getFollowRate())){
            if (context.isCurrentIsAddbet())
                LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], addRate=[ %s ], robot action：adding....",context.getCurrentId(),context.getStrategyKey(), context.getRealCard(),1+context.getRound(), context.getAddBetRate()));
            else
                LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], addRate=[ %s ], robot action：following....",context.getCurrentId(),context.getStrategyKey(), context.getRealCard(),1+context.getRound(), context.getAddBetRate()));
            context.setAction(true);
            super.sendFollowMessage(req,context.getCurrentId(),context);
        }

        if(context.getCompareList().size()==0){
            LogUtil.warn(String.format("警告：异常消息,已没有可下注/比牌玩家,机器人仍然收到5007:current action消息, code=[ %s ], compareList.size= [ %s ]",context.getCode(),context.getCompareList().size()));
        }
    }
}
