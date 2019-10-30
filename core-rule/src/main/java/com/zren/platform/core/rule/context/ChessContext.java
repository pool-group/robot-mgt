package com.zren.platform.core.rule.context;

import lombok.Data;

import java.util.List;

/**
 * 机器人规则计算结果上下文
 *
 * @author k.y
 * @version Id: ChessInfoEntity.java, v 0.1 2018年11月17日 下午13:19 k.y Exp $
 */
@Data
public class ChessContext {

    /**机器人开牌状态 0：输  1：赢  2：随机*/
    private Integer robotry;

    /**牌型*/
    private List<String> chess;

    /**桌台操作轮次*/
    private int round;

}
