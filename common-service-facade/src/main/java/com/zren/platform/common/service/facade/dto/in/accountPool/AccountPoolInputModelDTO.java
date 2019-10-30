package com.zren.platform.common.service.facade.dto.in.accountPool;

import com.zren.platform.common.service.facade.result.BaseSerializable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 机器人水池配置 入参
 *
 * @author k.y
 * @version Id: AccountPoolInputModelDTO.java, v 0.1 2019年02月28日 下午16:03 k.y Exp $
 */
@Data
public class AccountPoolInputModelDTO extends BaseSerializable {

    /**主键ID*/
    private Integer id;

    /**平台名称*/
    private String brand;

    /**游戏ID*/
    private Integer gameId;

    /**房间ID*/
    private Integer roomId;

    /**房间类型*/
    private Byte roomType;

    /**账户本金*/
    private BigDecimal capital;

    /**冻结资金*/
    private BigDecimal freeze;

    /**盈亏额 盈利：正数  亏损：负数*/
    private BigDecimal gainLoss;

    /**风控类型 0：按比例  1：按金额*/
    private Byte controlType;

    /**预设RTP值*/
    private BigDecimal rtpValue;

    /**RTP作弊几率初始值*/
    private BigDecimal rtpCheatInit;

    /**RTP当前作弊几率*/
    private BigDecimal rtpCurrentCheat;

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

    /**初始化资金最小倍数*/
    private Double minMultiple;

    /**初始化资金最大倍数*/
    private Double maxMultiple;

    /**头像编号区间*/
    private Integer headRandom;

    /**账户是否可用*/
    private Byte isAble;

    /**创建时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;

    /**操作人*/
    private String operator;

}
