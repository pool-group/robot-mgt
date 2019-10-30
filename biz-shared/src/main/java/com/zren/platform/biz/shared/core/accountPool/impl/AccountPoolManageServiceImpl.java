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

/**
 * 资金池管理实现
 *
 * @author k.y
 * @version Id: AccountPoolManageServiceImpl.java, v 0.1 2018年11月07日 下午16:25 k.y Exp $
 */
@Service
public class AccountPoolManageServiceImpl implements AccountPoolManageService {


    /**资金池管理*/
    @Autowired
    private AccountPoolManageRepository accountPoolManageRepository;


    /**
     * 获取可用资金
     *
     * @return
     */
    @Override
    public AccountPoolPO getUsableCapital(Integer gameId,String brand,Integer roomId) {

        //获取资金池某一类大账户信息
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

    /**
     * 冻结资金池资金
     *
     * @param freezeCapital
     * @return
     */
    @Override
    public int freezeCapital(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId){
        return accountPoolManageRepository.updateFreeze(freezeCapital,gameId,brand,roomId);
    }

    /**
     * 变更盈亏
     *
     * @return
     */
    @Override
    public int updateGainLoss(BigDecimal gainLoss,Integer gameId,String brand,Integer roomId) {
        return accountPoolManageRepository.updateGainLoss(gainLoss,gameId,brand,roomId);
    }

    /**
     * [解冻]回收可用资金
     *
     * @return
     */
    @Override
    public int updateUnfreeze(BigDecimal freezeCapital,Integer gameId,String brand,Integer roomId) {
        return accountPoolManageRepository.updateUnfreeze(freezeCapital,gameId,brand,roomId);
    }

    /**
     * [解冻]回收可用资金,并变更盈亏
     *
     * @return
     */
    @Override
    public int updateUnfreezeAndGainLoss(BigDecimal freezeCapital,BigDecimal gainLoss,Integer gameId,String brand,Integer roomId) {
        return accountPoolManageRepository.updateUnfreezeAndGainLoss(freezeCapital,gainLoss,gameId,brand,roomId);
    }


}
