package com.zren.platform.core.rule.entity.in;

import com.zren.platform.core.rule.enums.RobotFeaturesEnum;
import lombok.Data;

/**
 * Policy Information Request Parameters
 *
 * @author k.y
 * @version Id: ZjhStrategyEntity.java, v 0.1 2019年08月01日 下午13:59 k.y Exp $
 */
@Data
public class ZjhStrategyEntity {

    private RobotFeaturesEnum features;//

    /**当前局机器人是否赢*/
    private Integer isWin;

    /**机器人是否看牌*/
    private Boolean isRlook;//

    /**玩家最新一次下注之前是否看牌*/
    private Boolean isPlook;//

    /**玩家最新一次是跟注还是加注 follow add*/
    private RobotFeaturesEnum betType;//

    /**跟注或者加注对应的界别*/
    private RobotFeaturesEnum level;

    /**玩家最新一次下注额度*/
    private Double lastBetScore;//

    /**当前第几轮*/
    private Integer round;

    /**桌台当前没有弃牌的玩家总人数*/
    private Integer pableCount;//

    /**当前机器人牌值*/
    private int[] cards;

    /**暗牌筹码 数组以，号组成字符串*/
    private String dark;

    /**明牌筹码 数组 以， 号组成字符串*/
    private String brights;

    private String realCard;

    /**平台*/
    private String brand;

    /**游戏ID*/
    private Integer gameId;

    /**房间ID*/
    private Integer roomId;

    private String realPlayerId;

    private Double playerTotalBetScore;

    private Integer robotry;

}
