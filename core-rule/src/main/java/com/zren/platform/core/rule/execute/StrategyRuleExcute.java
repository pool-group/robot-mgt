package com.zren.platform.core.rule.execute;

import com.zren.platform.common.dal.po.ZjhStrategyPO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.CronstrRuleException;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.entity.in.ZjhStrategyEntity;
import com.zren.platform.core.rule.factory.ZJHAIStrategyFactory;
import com.zren.platform.core.rule.factory.ZJHStrategyFactory;
import com.zren.platform.core.rule.strategy.common.process.AIStrategyRuleProcess;
import com.zren.platform.core.rule.strategy.zjh.process.ZJHStrategyActionProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Rule Policy Processing
 *
 * @author k.y
 * @version Id: StrategyRuleExcute.java, v 0.1 2018年12月18日 下午17:34 k.y Exp $
 */
@Component
public class StrategyRuleExcute implements AIStrategyRuleProcess {

    @Autowired
    private ZJHStrategyActionProcess zJHStrategyActionProcess;

    @Autowired
    private ZJHStrategyFactory zJHStrategyFactory;

    @Autowired
    private ZJHAIStrategyFactory zJHAIStrategyFactory;

    /**
     * Acquisition Behavior Strategy
     *
     * @param robotry
     * @param round
     * @param userIdList
     * @return
     */
    public ZjhStrategyInfoDTO refreshZjhStrategy(Integer robotry, Integer round,List<Long> userIdList){

        AlgorithmRuleEntity are=new AlgorithmRuleEntity();
        are.setRuleOutputModelDTO(new RuleOutputModelDTO());
        are.setUserIdlist(userIdList);
        ChessContext chessContext=new ChessContext();
        chessContext.setRobotry(robotry);
        if(round.compareTo(3)>0){
            round=3;
        }
        chessContext.setRound(round);
        zJHStrategyActionProcess.excute(are,chessContext);
        return (ZjhStrategyInfoDTO) are.getRuleOutputModelDTO().getStrategylist().get(0);
    }

    /**
     * Acquisition annotation strategy
     *
     * @param robotry
     * @param index
     * @return
     */
    public Double findAddBetStrategy(Integer robotry,Integer index){
       return zJHStrategyFactory.findAddBetStrategy(robotry,index);
    }

    /**
     * Acquisition of AI Behavior Strategy
     * Method Executes error-reporting cron expression to resolve exceptions, then obtains policy from backup container
     * The rule policy for backup container storage is the correct content at service startup
     *
     * @param zse
     * @return
     */
    public ZjhStrategyInfoDTO refreshZjhAIStrategy(ZjhStrategyEntity zse){
        ZjhStrategyInfoDTO out=new ZjhStrategyInfoDTO();
        String key=null;
        try {
            key=analysisKey(zse);
            ZjhStrategyPO po=zJHAIStrategyFactory.initAIActionOriginal(key,false);
            analysisResult(zse,po,out);
        } catch (Exception e) {
            if(e instanceof CronstrRuleException){
                try {
                    LogUtil.warn(String.format("analysis this cron string rule message is Exceptions, start use availability container.."));
                    ZjhStrategyPO po=zJHAIStrategyFactory.initAIActionOriginal(key,true);
                    analysisResult(zse,po,out);
                } catch (Exception e1) {
                    if(e1 instanceof CronstrRuleException){
                        throw new RobotSystemException(e1, ErrorCodeEnum.INIT_CROPN_BACKUP_SYS);
                    }else {
                        throw e1;
                    }
                }
            }else {
                throw e;
            }
        }
        out.setRealCard(zse.getRealCard());
        out.setKey(key);
        return out;
    }

}
