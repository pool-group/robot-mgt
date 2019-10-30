package com.zren.platform.core.rule.entity.in;

import com.zren.platform.common.dal.po.RobotInfoPO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金规则业务实体
 *
 * @author k.y
 * @version Id: CapitalRuleEntity.java, v 0.1 2018年11月07日 下午17:53 k.y Exp $
 */
@Data
public class CapitalRuleEntity {


    /**账户本金*/
    private BigDecimal capital;

    /**最小资金*/
    private BigDecimal minAmount;

    /**可用资金*/
    private BigDecimal usableCapital;

    /**最小倍数*/
    private Double minMultiple;

    /**最大倍数*/
    private Double maxMultiple;

    /**头像编号区间*/
    private Integer headRandom;

    /**机器人账户信息列表*/
    private List<RobotInfoPO> robotList;

}
