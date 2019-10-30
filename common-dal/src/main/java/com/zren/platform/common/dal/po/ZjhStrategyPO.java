package com.zren.platform.common.dal.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * zjh Policy information po
 *
 * @author k.y
 * @version Id: ZjhStrategyPO.java, v 0.1 2019年08月01日 下午14:03 k.y Exp $
 */
@Entity
@Table(name="robot_zjh_strategy")
@Data
public class ZjhStrategyPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String  cardType;
    @Column(nullable = false)
    private String  cardKey;
    @Column(nullable = false)
    private Integer round;
    @Column(nullable = false)
    private String  lookStrategy;
    @Column(nullable = false)
    private String  lookToGiveUpStrategy;
    @Column(nullable = false)
    private String  giveUpStrategy;
    @Column(nullable = false)
    private String  vsStrategy;
    @Column(nullable = false)
    private Integer vsmin;
    @Column(nullable = false)
    private Integer vsmax;
    @Column(nullable = false)
    private String  addBetStrategy;
    @Column(nullable = false)
    private BigDecimal quickThinkingStrategy;
    @Column(nullable = false)
    private BigDecimal  followRate;
    @Column(nullable = false)
    private Date    creatTime;
}