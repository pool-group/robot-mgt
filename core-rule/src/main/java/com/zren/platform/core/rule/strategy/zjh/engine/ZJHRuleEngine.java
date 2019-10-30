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

/**
 * 炸金花
 *
 * @author k.y
 * @version Id: ZjhRuleEngine.java, v 0.1 2018年11月17日 下午12:46 k.y Exp $
 */
@Component(value="zjh_game")
public class ZJHRuleEngine extends BaseRuleEngine<AlgorithmRuleEntity,Void> {

    /**胜负策略*/
    @Autowired
    private StrategyWinProcess strategyWinProcess;

    /**行为策略*/
    @Autowired
    private ZJHStrategyActionProcess strategyActionProcess;

    /**卡牌工厂*/
    @Autowired
    private ChessCardFactory chessCardFactory;

    /**
     * 流程执行
     *
     * @param algorithmRuleEntity
     * return
     */
    @Override
    public Void start(AlgorithmRuleEntity algorithmRuleEntity) {

        //摘要信息上下文
        ChessContext chessContext=new ChessContext();

        //1.胜负策略: 根据风控类型,计算大账户盈亏，得出策略类型 1：机器人赢  0：机器人输  2.胜负随机
        strategyWinProcess.excute(algorithmRuleEntity,chessContext);

        //2.从54张牌中随机获取5份牌(共15张),指定取牌的索引区间
        chessCardFactory.create(5, 15 , ChessCard.MIN_INDEX,ChessCard.MAX_INDEX-1, chessContext, new ZJHPlayCard());

        //3.行为策略: 每个机器人的行为策略 1.机器人赢：...  2.机器人输：控制下注，跟注上限   3.随机：...
        strategyActionProcess.excute(algorithmRuleEntity,chessContext);

        return null;
    }

}
