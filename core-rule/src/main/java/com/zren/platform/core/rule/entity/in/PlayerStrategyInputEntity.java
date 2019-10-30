package com.zren.platform.core.rule.entity.in;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Player Strategy Input Entity
 *
 * @author k.y
 * @version Id: PlayerStrategyInputEntity.java, v 0.1 2019年09月12日 下午11:35 k.y Exp $
 */
@Data
public class PlayerStrategyInputEntity {

    private String brand;
    private Integer gameId;
    private Integer roomId;
    private Integer day=1;
    private Integer mins=2;
    private Integer limit=300;
    private Integer state=0;
    private BigDecimal denyPipAmount;
    private BigDecimal positiveTotalbetAlarm;
    private BigDecimal reverseTotalbetAlarm;
}