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
@Table(name="robot_info")
@Data
public class RobotInfoPO {

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

    @Transient
    /**机器人账号*/
    private String userName;

    @Transient
    /**头像*/
    private Integer headUrl;

    @Column(nullable = false)
    /**机器人状态0：空闲  1：游戏中*/
    private String status;

    @Transient
    /**余额*/
    private BigDecimal balance;

    @Column(nullable = false, unique = true)
    /**最新操作时间*/
    private Date dateTime;

}
