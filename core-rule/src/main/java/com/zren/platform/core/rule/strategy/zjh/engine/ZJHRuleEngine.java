package com.zren.platform.core.rule.strategy.zjh.engine;

import com.zren.platform.common.util.constants.ChessCard;
import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.factory.ChessCardFactory;
import com.zren.platform.core.rule.strategy.BaseRuleEngine;
import com.zren.platform.core.rule.strategy.common.process.StrategyWinProcess;
import com.zren.platform.core.rule.strategy.zjh.biz.ZJHPlayCard;
import com.zren.platform.core.rule.strategy.zjh.process.ZJHStrategyActionProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="zjh_game")
public class ZJHRuleEngine extends BaseRuleEngine<AlgorithmRuleEntity,Void> {

    @Autowired
    private StrategyWinProcess strategyWinProcess;

    @Autowired
    private ZJHStrategyActionProcess strategyActionProcess;

    @Autowired
    private ChessCardFactory chessCardFactory;

    @Override
    public Void start(AlgorithmRuleEntity algorithmRuleEntity) {

        ChessContext chessContext=new ChessContext();

        strategyWinProcess.excute(algorithmRuleEntity,chessContext);

        chessCardFactory.create(5, 15 , ChessCard.MIN_INDEX,ChessCard.MAX_INDEX-2, chessContext, new ZJHPlayCard());

        strategyActionProcess.excute(algorithmRuleEntity,chessContext);

        return null;
    }

}
