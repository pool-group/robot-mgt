package com.zren.platform.common.service.facade.dto.in.robotInfo;

import com.zren.platform.common.service.facade.result.BaseSerializable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 机器人初始化入参
 *
 * @author k.y
 * @version Id: AIRobotInitInputModelDTO.java, v 0.1 2018年11月07日 下午13:33 k.y Exp $
 */
@Data
public class AIRobotInitInputModelDTO extends BaseSerializable {

    /**游戏类型*/
    private int gameId;

    /**平台类型*/
    private String brand;

    /**最小金额*/
    private BigDecimal minAmount;

    /**机器人数量*/
    private int count;

    /**agentId*/
    private Long agentId;

    /**clientId*/
    private Long clientId;

    /**房间ID*/
    private int roomId;

    /**桌台ID*/
    private String tableId;

}
