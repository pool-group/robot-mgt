package com.zren.platform.common.dal.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 机器人基本信息PO
 *
 * @author k.y
 * @version Id: RobotInfoPO.java, v 0.1 2018年11月06日 下午17:12 k.y Exp $
 */
@Entity
@Table(name="robot_clean_snap")
@Data
public class RobotClearSnapInfoPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**主键*/
    private Integer id;

    @Column(nullable = false, unique = true)
    /**机器人ID*/
    private Long userId;

    @Column(nullable = false, unique = true)
    /**机器人批次ID*/
    private Long batchId;

    @Column(nullable = false, unique = true)
    /**余额*/
    private BigDecimal balance;

    /**游戏类型*/
    @Column(nullable = false, unique = true)
    private Integer gameId;

    /**房间ID*/
    @Column(nullable = false, unique = true)
    private Integer roomId;

    /**平台*/
    @Column(nullable = false, unique = true)
    private String brand;

    @Column(nullable = false, unique = true)
    /**异常发生时间*/
    private Date deadTime;

    @Column(nullable = false, unique = true)
    /**创建时间*/
    private Date createTime;

}
