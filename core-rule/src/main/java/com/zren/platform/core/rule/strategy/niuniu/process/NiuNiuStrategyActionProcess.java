package com.zren.platform.core.rule.strategy.niuniu.process;

import com.zren.platform.common.service.facade.dto.out.niuniu.NiuNiuStrategyInfoDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NiuNiuStrategyActionProcess {

    public void excute(AlgorithmRuleEntity algorithmRuleEntity, ChessContext chessContext){

        RuleOutputModelDTO<NiuNiuStrategyInfoDTO> ruleOutputModelDTO=algorithmRuleEntity.getRuleOutputModelDTO();
        int robotry=chessContext.getRobotry();
        ruleOutputModelDTO.setRobotry(robotry);
        List<NiuNiuStrategyInfoDTO> strategylist=new ArrayList<>();
        for(int i=0;i<algorithmRuleEntity.getUserIdlist().size();i++){
            NiuNiuStrategyInfoDTO dto=new NiuNiuStrategyInfoDTO();
            byte isWin=1;
            switch (robotry){
                case 0:
                    isWin=0;
                    break;
                case 1:
                    int winIndex=DataUtil.randomNumber(0,strategylist.size(),1)[0];
                    if(i==winIndex){
                        isWin=1;
                    }else {
                        isWin=0;
                    }
                    break;
                case 2:
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
