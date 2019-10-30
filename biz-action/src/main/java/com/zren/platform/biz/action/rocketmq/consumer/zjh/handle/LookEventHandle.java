package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.common.util.Tuple.Tuple4x;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 炸金花看牌 消息事件处理
 *
 * @author k.ny
 * @version Id: LookEventHandle.java, v 0.1 2018年12月01日 下午16:52 k.ny Exp $
 */
@Component(value = "zjh_look_handle")
public class LookEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {

        //1  机器人逻辑状态上下文
        ZJHPlayerContext context =new ZJHPlayerContext();

        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
        context.setCurrentId((String) tuple4x._4().get());

        //2. 初始化上下文基础参数
        super.assembleContext(context,object);

        //3. 初始化策略信息
        super.assembleContextStrategy(context,context.getPlayerList());

        //4. 判断跟注金额是否足够, 不够则触发孤注一掷 [ 机器人暂时不考虑余额不足时弃牌 ]
        if(context.getCompareList().size()>0&&!context.isAction()&&context.getBalanceScore().compareTo(BigDecimal.valueOf(context.getCurrentShouldBetScore()))<=0){
            context.setAction(true);
            super.sendAloneMessage(req,context.getCurrentId());
        }

        //5. 弃牌
        if(context.getCompareList().size()>0&&!context.isAction()&&super.isHitRate(context.getLookToGiveUpRate())){
            context.setAction(true);
            Long giveUpMillisecond=Long.valueOf(DataUtil.randomNumber(3,6,1)[0])*1000;
            LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], giveupRate=[ %s ], robot action：giveup..",context.getCurrentId(),context.getStrategyKey(), context.getRealCard(),1+context.getRound(),context.getGiveUpRate()));
            super.sendGiveUpMessage(req,context.getCurrentId(),giveUpMillisecond);
        }

        //6. 比牌 命中概率或者超过单局风控金额限制
        if(context.getCompareList().size()>0&&!context.isAction()&&((1+context.getRound())>=context.getVsmax()||(1+context.getRound())>=context.getVsmin()&&super.isHitRate(context.getVsRate()))){
            context.setAction(true);
            if(StringUtils.isBlank(context.getVsRandomUserId())){
                LogUtil.warn(String.format(" 机器人 [ %s ], 发起 [ %s ] 比牌, compareUserId=[ %s ], 无法进行比牌",context.getCurrentId(),context.getCode(),context.getVsRandomUserId()));
                return;
            }
            LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], vsRate=[ %s ], robot action：vsing....",context.getCurrentId(), context.getStrategyKey(), context.getRealCard(),1+context.getRound(), context.getVsRate()));
            super.sendVsMessage(req,context.getCurrentId(),context.getVsRandomUserId());
        }

        //7. 跟注、加注
        if(context.getCompareList().size()>0&&!context.isAction()&&super.isHitRate(context.getFollowRate())){
            context.setAction(true);
            super.sendFollowMessage(req,context.getCurrentId(),context);
        }

        //7. 警告
        if(context.getCompareList().size()==0){
            LogUtil.warn(String.format("警告：异常消息,已没有可下注/比牌玩家,机器人仍然收到5007:current action消息, code=[ %s ], compareList.size= [ %s ]",context.getCode(),context.getCompareList().size()));
        }
    }
}
