package com.zren.platform.biz.action.model.zjh;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 加入台桌 MQ消息领域模型
 *
 * @author k.y
 * @version Id: JoinTableMessageMqModel.java, v 0.1 2018年11月26日 下午13:30 k.y Exp $
 */
@Data
@ToString
public class JoinTableMessageMqModel extends BaseMqModel {

    /**名称*/
    private String username;

    /**头像编号*/
    private Integer avatar;

    /**agentId*/
    private Long agentId;

    /**clientId*/
    private Long clientId;

    /**平台类型*/
    private String brand;

    /**批次ID*/
    private Long batchId;

    /**(暂时不用)*/
    private String device;

    /**本金*/
    private BigDecimal balance;

    /**用户类型  PLAYER：真实玩家  ROBOT：机器人*/
    private String userType;

    /**当前局机器人状态 0：输  1：赢  2：随机*/
    private Integer robotry;

}
