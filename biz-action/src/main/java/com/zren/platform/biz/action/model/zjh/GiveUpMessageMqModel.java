package com.zren.platform.biz.action.model.zjh;

import lombok.Data;
import lombok.ToString;

/**
 * 弃牌 MQ消息领域模型
 *
 * @author k.y
 * @version Id: FollowMessageMqModel.java, v 0.1 2018年11月28日 下午17:08 k.y Exp $
 */
@Data
@ToString
public class GiveUpMessageMqModel extends BaseMqModel {

    /**原因*/
    private String reason;
}
