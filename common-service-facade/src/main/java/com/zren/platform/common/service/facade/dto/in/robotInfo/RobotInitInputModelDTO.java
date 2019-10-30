package com.zren.platform.common.service.facade.dto.in.robotInfo;

import com.zren.platform.common.service.facade.result.BaseSerializable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 机器人初始化入参
 *
 * @author k.y
 * @version Id: RobotInitInputModelDTO.java, v 0.1 2018年11月07日 下午13:33 k.y Exp $
 */
@Data
public class RobotInitInputModelDTO extends BaseSerializable {

    /**游戏ID*/
    private Integer gameId;

    /**平台类型*/
    private String brand;

    /**桌台ID*/
    private String tableId;

    /**房间ID*/
    private Integer roomId;

    /**最小金额*/
    private BigDecimal minAmount;

    /**机器人数量*/
    private int count;

}
