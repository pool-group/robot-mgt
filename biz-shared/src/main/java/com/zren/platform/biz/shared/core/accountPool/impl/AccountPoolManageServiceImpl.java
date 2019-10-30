package com.zren.platform.biz.shared.core.accountPool.impl;

import com.zren.platform.biz.shared.core.accountPool.AccountPoolManageService;
import com.zren.platform.common.dal.po.AccountPoolPO;
import com.zren.platform.common.dal.repository.AccountPoolManageRepository;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.tool.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountPoolManageServiceImpl implements AccountPoolManageService {

    @Autowired
    private AccountPoolManageRepository accountPoolManageRepository;

    @Override
    public AccountPoolPO getUsableCapital(Integer gameId,String brand,Integer roomId) {

        AccountPoolPO accountPoolPO=accountPoolManageRepository.findByGameIdAndBrandAndRoomId(gameId,brand,roomId);
        if(null==accountPoolPO){
            throw new RobotBizException(ErrorCodeEnum.INVALID_ACCOUNT_POOL);
        }
        if(accountPoolPO.getIsAble().compareTo((byte) 0)==0){
            throw new RobotBizException(ErrorCodeEnum.ACCOUNT_IS_UNABLE);
        }
        LogUtil.info(String.format(" SQL: findByGameIdAndBrandAndRoomId() accountPoolPO=[ %s ]",accountPoolPO));
        return accountPoolPO;
    }

    @Override
    public int freezeCapital(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId){
        return accountPoolManageRepository.updateFreeze(freezeCapital,gameId,brand,roomId);
    }

    @Override
    public int updateGainLoss(BigDecimal gainLoss,Integer gameId,String brand,Integer roomId) {
        return accountPoolManageRepository.updateGainLoss(gainLoss,gameId,brand,roomId);
    }

    @Override
    public int updateUnfreeze(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId) {
        return accountPoolManageRepository.updateUnfreeze(freezeCapital,gameId,brand,roomId);
    }

    @Override
    public int updateUnfreezeAndGainLoss(BigDecimal freezeCapital,BigDecimal gainLoss,Integer gameId,String brand,Integer roomId) {
        return accountPoolManageRepository.updateUnfreezeAndGainLoss(freezeCapital,gainLoss,gameId,brand,roomId);
    }
}
