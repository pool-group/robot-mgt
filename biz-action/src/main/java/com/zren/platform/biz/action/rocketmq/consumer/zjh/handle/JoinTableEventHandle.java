package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.common.util.Tuple.Tuple4x;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.springframework.stereotype.Component;

/**
 * 机器人加入桌台监听
 *
 * @author k.y
 * @version Id: GameOverHandle.java, v 0.1 2018年12月01日 下午16:52 k.y Exp $
 */
@Component(value = "zjh_join_table_handle")
public class JoinTableEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {

        String userId=object.getString("userId");
        ZJHPlayerContext context=new ZJHPlayerContext();
        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
        context.setCurrentId((String) tuple4x._4().get());
        Long areadyMillisecond=Long.valueOf(DataUtil.randomNumber(1,5,1)[0]*1000);
        LogUtil.info(String.format(" 机器人 [ %s ] 加入桌台 [ %s ]",userId,req.getTableId()));
        super.sendAlreadyMessage(req,userId,areadyMillisecond);
    }
}
