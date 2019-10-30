package com.zren.platform.common.dal.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账目流水
 *
 * @author k.y
 * @version Id: AccountWaterPO.java, v 0.1 2018年11月09日 下午11:56 k.y Exp $
 */
@Entity
@Table(name="account_water")
@Data
public class AccountWaterPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**机器人ID*/
    @Column(nullable = false, unique = true)
    private Long userId;

    /**批次ID*/
    @Column(nullable = false, unique = true)
    private Long batchId;

    /**金额*/
    @Column(nullable = false, unique = true)
    private BigDecimal balance;

    /**流水类型 0：流出  1：流进*/
    @Column(nullable = false, unique = true)
    private Byte waterType;

    /**游戏类型*/
    @Column(nullable = false, unique = true)
    private Integer gameId;

    /**房间ID*/
    @Column(nullable = false, unique = true)
    private Integer roomId;

    /**平台*/
    @Column(nullable = false, unique = true)
    private String brand;

    /**创建时间*/
    @Column(nullable = false, unique = true)
    private Date createTime;

}
