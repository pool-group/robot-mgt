package com.zren.platform.common.dal.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金池PO
 *
 * @author k.y
 * @version Id: AccountPoolPO.java, v 0.1 2018年11月07日 下午15:48 k.y Exp $
 */
@Entity
@Table(name="account_pool")
@Data
public class AccountPoolPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**账户本金*/
    @Column(nullable = false, unique = true)
    private BigDecimal capital;

    /**冻结资金*/
    @Column(nullable = false, unique = true)
    private BigDecimal freeze;

    /**盈亏额 盈利：正数  亏损：负数*/
    @Column(nullable = false, unique = true)
    private BigDecimal gainLoss;

    /**平台名称*/
    @Column(nullable = false, unique = true)
    private String brand;

    /**房间类型*/
    @Column(nullable = false, unique = true)
    private Byte roomType;

    /**游戏ID*/
    @Column(nullable = false, unique = true)
    private Integer gameId;

    /**房间ID*/
    @Column(nullable = false, unique = true)
    private Integer roomId;

    /**风控类型 0：按比例  1：按金额*/
    @Column(nullable = false, unique = true)
    private Byte controlType;

    @Column(nullable = false, unique = true)
    private BigDecimal denyPipAmount;

//    @Column(nullable = false, unique = true)
    @Transient
    private BigDecimal positiveTotalbetAlarm=BigDecimal.ZERO;

    @Column(nullable = false, unique = true)
    private BigDecimal positiveGrayRate;
    
    @Column(nullable = false, unique = true)
    private Integer positiveGrayTime;

//    @Column(nullable = false, unique = true)
    @Transient
    private BigDecimal reverseTotalbetAlarm=BigDecimal.ZERO;

    @Column(nullable = false, unique = true)
    private BigDecimal reverseGrayRate;

    @Column(nullable = false, unique = true)
    private Integer reverseGrayTime;

    @Column(nullable = false, unique = true)
    private BigDecimal reverseStage;

    @Column(nullable = false, unique = true)
    private BigDecimal reverseRate;

    @Column(nullable = false, unique = true)
    private BigDecimal positiveRate;

    @Column(nullable = false, unique = true)
    private Byte grayAble;

    /**RTP是否启用*/
    @Column(nullable = false, unique = true)
    private Byte rtpAble;

    /**预设RTP值*/
    @Column(nullable = false, unique = true)
    private BigDecimal rtpValue;

    /**总投入初始值/天*/
    @Column(nullable = false, unique = true)
    private BigDecimal rtpInputInit;

    /**RTP作弊几率初始值*/
    @Column(nullable = false, unique = true)
    private BigDecimal rtpCheatInit;

    /**RTP当前作弊几率*/
    @Column(nullable = false, unique = true)
    private BigDecimal rtpCurrentCheat;

    /**RTP递增梯度值 公式：当实际RTP≥预设RTP, 作弊几率R=50%+△r1(t1)+△r1(t2)+…+△r1(tn)，△r1=0.5%*/
    @Column(nullable = false, unique = true)
    private BigDecimal rtpIncrease;

    /**RTP递减梯度值 公式：当实际RTP＜预设RTP，作弊几率R=当前R-△r2(t1)-△r2(t2)-…-△r2(tn)，△r2=1%*/
    @Column(nullable = false, unique = true)
    private BigDecimal rtpDecrease;

    /**最大盈利比例*/
    @Column(nullable = false, unique = true)
    private BigDecimal maxGainRate;

    /**最大亏损比例*/
    @Column(nullable = false, unique = true)
    private BigDecimal maxLossRate;

    /**最大盈利*/
    @Column(nullable = false, unique = true)
    private BigDecimal maxGain;

    /**最大亏损*/
    @Column(nullable = false, unique = true)
    private BigDecimal maxLoss;

    /**盈利策略Cron表达式*/
    @Column(nullable = false, unique = true)
    private String gainCronRule;

    /**亏损策略Cron表达式*/
    @Column(nullable = false, unique = true)
    private String lossCronRule;

    /**初始化资金最小倍数*/
    @Column(nullable = false, unique = true)
    private Double minMultiple;

    /**初始化资金最大倍数*/
    @Column(nullable = false, unique = true)
    private Double maxMultiple;

    /**头像编号区间*/
    @Column(nullable = false, unique = true)
    private Integer headRandom;

    /**账户是否可用*/
    @Column(nullable = false, unique = true)
    private Byte isAble;

    /**创建时间*/
    @Column(nullable = false, unique = true)
    private Date createTime;

    /**更新时间*/
    @Column(nullable = false, unique = true)
    private Date updateTime;

    /**操作人*/
    @Column(nullable = false, unique = true)
    private String operator;

}
