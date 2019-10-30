package com.zren.platform.common.service.facade.dto.in.rule;

import lombok.Data;

import java.util.List;

/**
 * 获取规则策略参数
 *
 * @author k.y
 * @version Id: ZJHRuleInputDTO.java, v 0.1 2018年11月21日 下午10:53 k.y Exp $
 */
@Data
public class RuleInputModelDTO {

    /**平台*/
    private String brand;

    /**游戏ID*/
    private Integer gameId;

    /**房间ID*/
    private Integer roomId;

    /**桌台ID*/
    private String tableId;

    /**机器人ID*/
    private List<Long> userIdlist;

    /**玩家ID*/
    private List<Long> playerlist;
}
