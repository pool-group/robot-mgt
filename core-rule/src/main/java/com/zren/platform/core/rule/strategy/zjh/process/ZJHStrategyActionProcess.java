package com.zren.platform.core.rule.strategy.zjh.process;

import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.factory.ZJHStrategyFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ZJHStrategyActionProcess {

    @Autowired
    private ZJHStrategyFactory zJHStrategyFactory;

    public void excute(AlgorithmRuleEntity algorithmRuleEntity, ChessContext chessContext){

        RuleOutputModelDTO ruleOutputModelDTO=algorithmRuleEntity.getRuleOutputModelDTO();
        int robotry=chessContext.getRobotry();
        ruleOutputModelDTO.setFinishChess(chessContext.getChess());
        ruleOutputModelDTO.setRobotry(robotry);
        List<ZjhStrategyInfoDTO> strategylist=new ArrayList<>();
        int indexCount=DataUtil.randomNumber(1,algorithmRuleEntity.getUserIdlist().size()+1,1)[0];
        Integer[] index=DataUtil.randomNumber(0,algorithmRuleEntity.getUserIdlist().size(),indexCount);
        for(int i=0;i<algorithmRuleEntity.getUserIdlist().size();i++){
            ZjhStrategyInfoDTO outDto=new ZjhStrategyInfoDTO();
            ZjhStrategyInfoDTO dto=null;
            switch (robotry){

                case 0:
                    dto=zJHStrategyFactory.initialize(0,chessContext.getRound());
                    BeanUtils.copyProperties(dto, outDto);
                    outDto.setIsWin((byte) 0);
                    break;
                case 1:
                    int winIndex=DataUtil.randomNumber(0,strategylist.size(),1)[0];
                    if(i==winIndex){
                        dto=zJHStrategyFactory.initialize(1,chessContext.getRound());
                        BeanUtils.copyProperties(dto, outDto);
                        outDto.setIsWin((byte) 1);
                    }else {
                        dto=zJHStrategyFactory.initialize(0,chessContext.getRound());
                        BeanUtils.copyProperties(dto, outDto);
                        outDto.setIsWin((byte) 0);
                    }
                    break;
                case -1:
                    dto=zJHStrategyFactory.initialize(-1,chessContext.getRound());
                    BeanUtils.copyProperties(dto, outDto);
                    outDto.setIsWin((byte) -1);
                    break;
                case 2:
                    dto=zJHStrategyFactory.initialize(2,chessContext.getRound());
                    BeanUtils.copyProperties(dto, outDto);
                    outDto.setIsWin((byte) 2);
                    break;
            }
            int millisecondAready=DataUtil.randomNumber(1,7,1)[0]*1000;
            int millisecondJoinTable;
            if(Arrays.asList(index).contains(i)){
                millisecondJoinTable=0;
            }else {
                millisecondJoinTable=DataUtil.randomNumber(0,8,1)[0]*1000;
            }
            outDto.setMillisecondJoinTable(Long.valueOf(millisecondJoinTable));
            outDto.setMillisecondAready(Long.valueOf(millisecondAready));
            outDto.setLeaveCount(DataUtil.randomNumber(1,8,1)[0]);
            outDto.setUserId(algorithmRuleEntity.getUserIdlist().get(i));
            strategylist.add(outDto);
        }
        ruleOutputModelDTO.setStrategylist(strategylist);
    }
}
