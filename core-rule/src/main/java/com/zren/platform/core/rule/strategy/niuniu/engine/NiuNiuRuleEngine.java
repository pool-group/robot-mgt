package com.zren.platform.core.rule.strategy.niuniu.engine;

import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.strategy.BaseRuleEngine;
import com.zren.platform.core.rule.strategy.common.process.StrategyWinProcess;
import com.zren.platform.core.rule.strategy.niuniu.process.NiuNiuStrategyActionProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="niuniu_common")
public class NiuNiuRuleEngine extends BaseRuleEngine<AlgorithmRuleEntity,Void> {

    @Autowired
    private StrategyWinProcess strategyWinProcess;

    @Autowired
    private NiuNiuStrategyActionProcess strategyActionProcess;

    @Override
    public Void start(AlgorithmRuleEntity algorithmRuleEntity) {

        ChessContext chessContext=new ChessContext();

        strategyWinProcess.excute(algorithmRuleEntity,chessContext);

        strategyActionProcess.excute(algorithmRuleEntity,chessContext);

        return null;
    }
}
