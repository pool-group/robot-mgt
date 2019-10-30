package com.zren.platform.common.dal.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * RTP基础数据
 *
 * @author k.y
 * @version Id: GameRtpStatsPO.java, v 0.1 2019年04月06日 下午17:54 k.y Exp $
 */
@Entity
@Table(name="game_rtp_stats")
@Data
public class GameRtpStatsPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Integer gameId;
    @Column(nullable = false, unique = true)
    private String gameName;
    @Column(nullable = false, unique = true)
    private String brand;
    @Column(nullable = false, unique = true)
    private Integer roomId;
    @Column(nullable = false, unique = true)
    private String roomName;
    @Column(nullable = false, unique = true)
    private Integer tableId;
    @Column(nullable = false, unique = true)
    private String tableName;
    @Column(nullable = false, unique = true)
    private BigDecimal totalInput;
    @Column(nullable = false, unique = true)
    private BigDecimal totalPnl;
    @Column(nullable = false, unique = true)
    private Integer createDate;
}
