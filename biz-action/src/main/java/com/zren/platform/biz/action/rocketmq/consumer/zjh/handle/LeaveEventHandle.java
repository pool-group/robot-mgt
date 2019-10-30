package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.common.util.Tuple.Tuple4x;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 离开桌台 消息事件处理
 *
 * @author k.y
 * @version Id: GameOverHandle.java, v 0.1 2018年12月01日 下午16:52 k.y Exp $
 */
@Component(value = "zjh_leave_table_handle")
public class LeaveEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {

        //机器人逻辑状态上下文
        ZJHPlayerContext context =new ZJHPlayerContext();

        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
        context.setCurrentId((String) tuple4x._4().get());

        //1. 初始化上下文参数
        super.assembleContext(context,object);
        context.setBalanceScore(new BigDecimal(object.getString("balanceScore").toString()));//离开台桌时的余额

        //2. 回收机器人
        super.destroyRobot(context,req);

    }
}
