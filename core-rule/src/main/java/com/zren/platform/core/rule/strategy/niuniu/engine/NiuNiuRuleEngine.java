package com.zren.platform.core.rule.strategy.niuniu.engine;

import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.strategy.BaseRuleEngine;
import com.zren.platform.core.rule.strategy.common.process.StrategyWinProcess;
import com.zren.platform.core.rule.strategy.niuniu.process.NiuNiuStrategyActionProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 牛牛
 *
 * @author k.y
 * @version Id: NiuNiuRuleEngine.java, v 0.1 2018年11月29日 下午12:38 k.y Exp $
 */
@Component(value="niuniu_common")
public class NiuNiuRuleEngine extends BaseRuleEngine<AlgorithmRuleEntity,Void> {

    /**胜负策略*/
    @Autowired
    private StrategyWinProcess strategyWinProcess;

    /**行为策略*/
    @Autowired
    private NiuNiuStrategyActionProcess strategyActionProcess;

    /**
     * 流程執行
     *
     * @param algorithmRuleEntity
     * return
     */
    @Override
    public Void start(AlgorithmRuleEntity algorithmRuleEntity) {

        //摘要信息上下文
        ChessContext chessContext=new ChessContext();

        //1.胜负策略: 根据风控类型,计算大账户盈亏，得出策略类型 1：机器人赢  2：机器人输  3.胜负随机
        strategyWinProcess.excute(algorithmRuleEntity,chessContext);

        //2.行为策略: 每个机器人的行为策略 1.机器人赢：...  2.机器人输：控制下注，跟注上限   3.随机：...
        strategyActionProcess.excute(algorithmRuleEntity,chessContext);

        return null;
    }
}
