package com.zren.platform.biz.shared.core.accountPool.impl;

import com.zren.platform.common.dal.po.GameRtpStatsPO;
import com.zren.platform.common.dal.repository.GameRtpStatsRepository;
import com.zren.platform.common.util.tool.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRtpStatsServiceImpl {

    @Autowired
    private GameRtpStatsRepository gameTableStatsRepository;

    public GameRtpStatsPO queryRtpResult(String brand,Integer gameId,Integer roomId){

        GameRtpStatsPO gameRtpStatsPO=gameTableStatsRepository.queryRtpResult(brand,gameId,roomId);
        LogUtil.info(String.format("brand=[ %s ] ,gameId=[ %s ] ,roomId=[ %s ] ,gameRtpStatsPO=[ %s ]",brand,gameId,roomId,gameRtpStatsPO));
        return gameRtpStatsPO;
    }
}
