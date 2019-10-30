package com.zren.platform.biz.action.model.zjh;

import lombok.Data;
import lombok.ToString;

/**
 * 消息基类
 *
 * @author k.y
 * @version Id: BaseMqModel.java, v 0.1 2018年11月28日 下午17:38 k.y Exp $
 */
@Data
@ToString
public abstract class BaseMqModel {

    /**消息码*/
    private Integer code;

    /**机器人ID*/
    private String userId;

}
