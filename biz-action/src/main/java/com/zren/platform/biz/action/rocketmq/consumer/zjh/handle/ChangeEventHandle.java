package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.common.util.tuple.Tuple4x;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(value = "zjh_change_table_handle")
public class ChangeEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {

        ZJHPlayerContext context =new ZJHPlayerContext();

        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
        context.setCurrentId((String) tuple4x._4().get());

        super.assembleContext(context,object);
        context.setBalanceScore(new BigDecimal(object.getString("balanceScore")));//离开台桌时的余额

        super.destroyRobot(context,req);

    }
}
