package com.zren.platform.core.rule.entity.in;

import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 规则策略参数
 *
 * @author k.y
 * @version Id: AlgorithmRuleEntity.java, v 0.1 2018年11月17日 下午11:34 k.y Exp $
 */
@Data
public class AlgorithmRuleEntity {

    /**平台*/
    private String brand;

    /**游戏ID*/
    private Integer gameId;

    /**房间ID*/
    private Integer roomId;

    /**账户本金*/
    private BigDecimal capital;

    /**盈亏额 盈利：正数  亏损：负数*/
    private BigDecimal gainLoss;

    /**某个房间用户总投注*/
    private BigDecimal totalInput ;

    /**风控类型 0：按比例  1：按金额*/
    private Byte controlType;

    /**RTP状态 开启：1  关闭：2*/
    private Byte rtpAble;

    private Byte grayAble;

    private BigDecimal denyPipAmount;
    private BigDecimal positiveGrayRate;
    private Integer positiveGrayTime;
    private BigDecimal positiveTotalbetAlarm;
    private BigDecimal reverseGrayRate;
    private Integer reverseGrayTime;
    private BigDecimal reverseTotalbetAlarm;
    private List<Long> playerlist;

    private BigDecimal reverseStage;
    private BigDecimal reverseRate;
    private BigDecimal difference;

    private Boolean bool;

    /**实际RTP值*/
    private BigDecimal realRtpValue;

    /**预设RTP值*/
    private BigDecimal rtpValue;

    /**RTP作弊几率初始值*/
    private BigDecimal rtpCheatInit;

    /**RTP当前作弊几率*/
    private BigDecimal rtpCurrentCheat=BigDecimal.ZERO;

    /**RTP递增梯度值 公式：当实际RTP≥预设RTP, 作弊几率R=50%+△r1(t1)+△r1(t2)+…+△r1(tn)，△r1=0.5%*/
    private BigDecimal rtpIncrease;

    /**RTP递减梯度值 公式：当实际RTP＜预设RTP，作弊几率R=当前R-△r2(t1)-△r2(t2)-…-△r2(tn)，△r2=1%*/
    private BigDecimal rtpDecrease;

    /**最大盈利比例*/
    private BigDecimal maxGainRate;

    /**最大亏损比例*/
    private BigDecimal maxLossRate;

    /**最大盈利*/
    private BigDecimal maxGain;

    /**最大亏损*/
    private BigDecimal maxLoss;

    /**盈利策略Cron表达式*/
    private String gainCronRule;

    /**亏损策略Cron表达式*/
    private String lossCronRule;

    /**机器人ID*/
    private List<Long> userIdlist;

    /**规则策略*/
    RuleOutputModelDTO ruleOutputModelDTO;

}
