package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.common.util.tuple.Tuple4x;
import org.springframework.stereotype.Component;

@Component(value = "zjh_snapshot_handle")
public class SnapshotEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {

        //机器人逻辑状态上下文
//        ZJHPlayerContext context =new ZJHPlayerContext();
//
//        RoomConfigReq req=new RoomConfigReq(Integer.valueOf(tuple4x._1().get().toString()),Integer.valueOf(tuple4x._2().get().toString()),(String)tuple4x._3().get());
//        context.setCurrentId((String) tuple4x._4().get());
//
//        //1. 初始化上下文参数
//        JSONArray seats=object.getJSONArray("seats");




    }
}
