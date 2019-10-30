package com.zren.platform.biz.shared.core.accountPool;

import com.zren.platform.common.dal.po.AccountPoolPO;

import java.math.BigDecimal;

public interface AccountPoolManageService {

    AccountPoolPO getUsableCapital(Integer gameId, String brand,Integer roomId);

    int freezeCapital(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId);

    int updateGainLoss(BigDecimal gainLoss,Integer gameId,String brand,Integer roomId);

    int updateUnfreeze(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId);

    int updateUnfreezeAndGainLoss(BigDecimal freezeCapital,BigDecimal gainLoss,Integer gameId,String brand,Integer roomId);
}
