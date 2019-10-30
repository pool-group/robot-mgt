package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.common.util.Tuple.Tuple4x;
import org.springframework.stereotype.Component;

/**
 * 弃牌 消息事件处理
 *
 * @author k.y
 * @version Id: GameOverHandle.java, v 0.1 2018年12月01日 下午16:52 k.y Exp $
 */
@Component(value = "zjh_give_up_handle")
public class GameUpEventHandle extends ZJHEventHandle {

    @Override
    public void excute(String tags, JSONObject object, Tuple4x tuple4x) {


    }
}
