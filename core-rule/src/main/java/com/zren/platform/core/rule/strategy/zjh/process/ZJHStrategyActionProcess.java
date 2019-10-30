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

/**
 * 机器人行为策略
 *
 * @author k.y
 * @version Id: StrategyActionProcess.java, v 0.1 2018年11月19日 下午15:53 k.y Exp $
 */
@Component
public class ZJHStrategyActionProcess {

    /**炸金花策略工厂*/
    @Autowired
    private ZJHStrategyFactory zJHStrategyFactory;

    public void excute(AlgorithmRuleEntity algorithmRuleEntity, ChessContext chessContext){

        RuleOutputModelDTO ruleOutputModelDTO=algorithmRuleEntity.getRuleOutputModelDTO();
        int robotry=chessContext.getRobotry();
        //生成的牌型结果
        ruleOutputModelDTO.setFinishChess(chessContext.getChess());
        //当前局机器人状态 0：输  1：赢  2：随机
        ruleOutputModelDTO.setRobotry(robotry);
        List<ZjhStrategyInfoDTO> strategylist=new ArrayList<>();

        //随机几个机器人先进去桌台
        int indexCount=DataUtil.randomNumber(1,algorithmRuleEntity.getUserIdlist().size()+1,1)[0];
        //具体某个机器人先进入桌台
        Integer[] index=DataUtil.randomNumber(0,algorithmRuleEntity.getUserIdlist().size(),indexCount);
        for(int i=0;i<algorithmRuleEntity.getUserIdlist().size();i++){
            ZjhStrategyInfoDTO outDto=new ZjhStrategyInfoDTO();
            ZjhStrategyInfoDTO dto=null;
            switch (robotry){

                case 0://机器人状态为输
                    dto=zJHStrategyFactory.initialize(0,chessContext.getRound());
                    BeanUtils.copyProperties(dto, outDto);
                    outDto.setIsWin((byte) 0);
                    break;
                case 1://机器人状态为赢,则随机一个机器人赢
                    int winIndex=DataUtil.randomNumber(0,strategylist.size(),1)[0];
                    if(i==winIndex){//某个机器人状态为赢
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
                case 2://机器人状态为随机
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
