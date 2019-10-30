package com.zren.platform.biz.shared.core.accountPool;

import com.zren.platform.common.dal.po.AccountPoolPO;

import java.math.BigDecimal;

/**
 * 资金池管理接口
 *
 * @author k.y
 * @version Id: AccountPoolManageService.java, v 0.1 2018年11月07日 下午16:25 k.y Exp $
 */
public interface AccountPoolManageService {


    /**
     * 获取水池信息 [ 业主/游戏/房间 ]
     *
     * @param gameId
     * @param brand
     * @return
     */
    AccountPoolPO getUsableCapital(Integer gameId, String brand,Integer roomId);

    /**
     * 冻结资金池资金
     *
     * @param freezeCapital
     * @param gameId
     * @param brand
     * @return
     */
    int freezeCapital(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId);

    /**
     * 更新盈亏
     *
     * @param gainLoss
     * @param gameId
     * @param brand
     * @return
     */
    int updateGainLoss(BigDecimal gainLoss,Integer gameId,String brand,Integer roomId);

    /**
     * 回收可用资金
     *
     * @param freezeCapital
     * @param gameId
     * @param brand
     * @return
     */
    int updateUnfreeze(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId);

    /**
     * 回收可用资金,更新盈亏
     *
     * @param freezeCapital
     * @param gameId
     * @param brand
     * @return
     */
    int updateUnfreezeAndGainLoss(BigDecimal freezeCapital,BigDecimal gainLoss,Integer gameId,String brand,Integer roomId);
}
