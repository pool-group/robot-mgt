package com.zren.platform.core.rule.strategy.niuniu.process;

import com.zren.platform.common.service.facade.dto.out.niuniu.NiuNiuStrategyInfoDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 机器人行为策略
 *
 * @author k.y
 * @version Id: StrategyActionProcess.java, v 0.1 2018年11月19日 下午15:53 k.y Exp $
 */
@Component
public class NiuNiuStrategyActionProcess {


    /**
     * 行为策略
     *
     * @param algorithmRuleEntity
     * @param chessContext
     */
    public void excute(AlgorithmRuleEntity algorithmRuleEntity, ChessContext chessContext){

        RuleOutputModelDTO<NiuNiuStrategyInfoDTO> ruleOutputModelDTO=algorithmRuleEntity.getRuleOutputModelDTO();
        int robotry=chessContext.getRobotry();
        //当前局机器人状态 0：输  1：赢  2：随机
        ruleOutputModelDTO.setRobotry(robotry);
        List<NiuNiuStrategyInfoDTO> strategylist=new ArrayList<>();
        for(int i=0;i<algorithmRuleEntity.getUserIdlist().size();i++){
            NiuNiuStrategyInfoDTO dto=new NiuNiuStrategyInfoDTO();
            byte isWin=1;
            switch (robotry){
                case 0://机器人状态为输
                    isWin=0;
                    break;
                case 1://机器人状态为赢
                    //随机一个机器人赢
                    int winIndex=DataUtil.randomNumber(0,strategylist.size(),1)[0];
                    if(i==winIndex){//某个机器人状态为赢
                        isWin=1;
                    }else {
                        isWin=0;
                    }
                    break;
                case 2://机器人状态为随机
                    isWin=2;
                    break;
            }
            dto.setUserId(algorithmRuleEntity.getUserIdlist().get(i));
            dto.setIsWin(isWin);
            strategylist.add(dto);
        }
        ruleOutputModelDTO.setStrategylist(strategylist);
    }


}
