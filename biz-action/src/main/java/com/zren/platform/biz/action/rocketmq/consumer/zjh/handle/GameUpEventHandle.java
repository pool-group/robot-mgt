package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.common.util.tuple.Tuple4x;
import org.springframework.stereotype.Component;

@Component(value = "zjh_give_up_handle")
public class GameUpEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {


    }
}
