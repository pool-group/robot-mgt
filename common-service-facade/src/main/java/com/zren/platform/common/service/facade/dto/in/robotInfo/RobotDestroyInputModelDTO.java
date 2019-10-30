package com.zren.platform.common.service.facade.dto.in.robotInfo;

import com.zren.platform.common.service.facade.result.BaseSerializable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 机器人回收入参
 *
 * @author k.y
 * @version Id: RobotDestroyInputModelDTO.java, v 0.1 2018年11月08日 下午11:34 k.y Exp $
 */
@Data
public class RobotDestroyInputModelDTO extends BaseSerializable{

    /**机器人ID*/
    private Long userId;

    /**机器人同一批次ID*/
    private Long batchId;

    /**当前余额*/
    private BigDecimal balance;

    /**游戏ID*/
    private Integer gameId;

    /**房间ID*/
    private Integer roomId;

    /**平台类型*/
    private String brand;

}
