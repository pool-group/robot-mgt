package com.zren.platform.core.rule.strategy.common.process;

import com.zren.platform.common.service.facade.dto.out.BaseStrategyDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.ProbabilisticUtil;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.entity.in.PlayerStrategyInputEntity;
import com.zren.platform.core.rule.entity.out.PlayerStrategyEntity;
import com.zren.platform.core.rule.factory.PlayerStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Player Strategy Process
 *
 * @author k.y
 * @version Id: PlayerStrategyProcess.java, v 0.1 2019年09月11日 下午15:47 k.y Exp $
 */
@Component
public class PlayerStrategyProcess {

    @Autowired
    PlayerStrategyFactory playerStrategyFactory;

    /**
     * Get User Profit and Loss Information Summary
     * Allow/Deny List Resolution Based on Customized Rule Policy
     *
     * @param algorithmRuleEntity
     */
    public void invoke(AlgorithmRuleEntity algorithmRuleEntity){

        PlayerStrategyInputEntity input=new PlayerStrategyInputEntity();
        input.setBrand(algorithmRuleEntity.getBrand());
        input.setGameId(algorithmRuleEntity.getGameId());
        input.setRoomId(algorithmRuleEntity.getRoomId());
        input.setDenyPipAmount(algorithmRuleEntity.getDenyPipAmount());
        input.setPositiveTotalbetAlarm(algorithmRuleEntity.getPositiveTotalbetAlarm());
        input.setReverseTotalbetAlarm(algorithmRuleEntity.getReverseTotalbetAlarm());
        PlayerStrategyEntity output=new PlayerStrategyEntity();
        if(1==algorithmRuleEntity.getGrayAble())
            playerStrategyFactory.initialize(input,output);
        if (output.getValidBool()){
            if(null==algorithmRuleEntity.getPlayerlist()||algorithmRuleEntity.getPlayerlist().size()==0){
                LogUtil.warn("playerList.size=0, output.refresh(algorithmRuleEntity) will Break !");
            }else {
                output.refresh(algorithmRuleEntity);
            }
        }
        LogUtil.info(String.format("=========== output=[ %s ]",output));
        RuleOutputModelDTO dto=algorithmRuleEntity.getRuleOutputModelDTO();
        if(algorithmRuleEntity.getDifference().compareTo(algorithmRuleEntity.getReverseStage())==1&&(0==algorithmRuleEntity.getGrayAble()||output.getRBool())){
            Boolean bool= ProbabilisticUtil.excute(algorithmRuleEntity.getReverseRate()) == 1;
            if(bool){
                dto.setRobotry(0);
                List<BaseStrategyDTO> robotInfolst=dto.getStrategylist();
                for (BaseStrategyDTO d:robotInfolst){
                    d.setIsWin((byte) 0);
                }
            }
            if(output.getRBool()){
                dto.setTarget(output.getAllow().get(0));
                LogUtil.info(Log.RTP.LOG,String.format("============== [ %s_%s_%s ] 【Allow】 touch player win : bool=[ %s ] , robotry=[ %s ], rate=[ %s ], difference=[ %s ], 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",algorithmRuleEntity.getBrand(),algorithmRuleEntity.getGameId(),algorithmRuleEntity.getRoomId(),bool,dto.getRobotry(),algorithmRuleEntity.getReverseRate(),algorithmRuleEntity.getDifference(),algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
            }else {
                LogUtil.info(Log.RTP.LOG,String.format("============== [ %s_%s_%s ] 【Default】 touch player win : bool=[ %s ] , robotry=[ %s ], rate=[ %s ], difference=[ %s ], 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",algorithmRuleEntity.getBrand(),algorithmRuleEntity.getGameId(),algorithmRuleEntity.getRoomId(),bool,dto.getRobotry(),algorithmRuleEntity.getReverseRate(),algorithmRuleEntity.getDifference(),algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
            }
        } else if (algorithmRuleEntity.getDifference().compareTo(BigDecimal.ZERO)<=0&&output.getRBool()){
            dto.setRobotry(2);
            List<BaseStrategyDTO> robotInfolst=dto.getStrategylist();
            for (BaseStrategyDTO d:robotInfolst){
                d.setIsWin((byte) 2);
            }
            LogUtil.info(Log.RTP.LOG,String.format("============== [ %s_%s_%s ] 【Allow】 touch player random : bool=[ %s ] , robotry=[ %s ], rate=[ %s ], difference=[ %s ], 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",algorithmRuleEntity.getBrand(),algorithmRuleEntity.getGameId(),algorithmRuleEntity.getRoomId(), dto.getRobotry().compareTo(1)==0,dto.getRobotry(),algorithmRuleEntity.getRtpCurrentCheat(),algorithmRuleEntity.getDifference(),algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
        } else if (algorithmRuleEntity.getDifference().compareTo(BigDecimal.ZERO)<=0&&output.getPBool()){
            Boolean r_bool=dto.getRobotry().compareTo(1)==0;
            if(r_bool&&playerStrategyFactory.haveDennyEffective(algorithmRuleEntity,output)){
                dto.setRobotry(3);
                List<BaseStrategyDTO> robotInfolst=dto.getStrategylist();
                for (BaseStrategyDTO d:robotInfolst){
                    d.setIsWin((byte) 3);
                }
                dto.setTarget(output.getTarget());
                LogUtil.info(Log.RTP.LOG,String.format("============== [ %s_%s_%s ] 【Deny】 touch player loss : bool=[ %s ] , robotry=[ %s ], rate=[ %s ], difference=[ %s ], 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",algorithmRuleEntity.getBrand(),algorithmRuleEntity.getGameId(),algorithmRuleEntity.getRoomId(), r_bool,dto.getRobotry(),algorithmRuleEntity.getRtpCurrentCheat(),algorithmRuleEntity.getDifference(),algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
            }else {
                LogUtil.info(Log.RTP.LOG,String.format("============== [ %s_%s_%s ] 【Default】 touch player loss : bool=[ %s ] , robotry=[ %s ], rate=[ %s ], difference=[ %s ], 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",algorithmRuleEntity.getBrand(),algorithmRuleEntity.getGameId(),algorithmRuleEntity.getRoomId(), r_bool,dto.getRobotry(),algorithmRuleEntity.getRtpCurrentCheat(),algorithmRuleEntity.getDifference(),algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
            }
        } else {
            LogUtil.info(Log.RTP.LOG,String.format("============== [ %s_%s_%s ] 【ODefault】 touch player loss : bool=[ %s ] , robotry=[ %s ], rate=[ %s ], difference=[ %s ], 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",algorithmRuleEntity.getBrand(),algorithmRuleEntity.getGameId(),algorithmRuleEntity.getRoomId(), dto.getRobotry().compareTo(1)==0,dto.getRobotry(),algorithmRuleEntity.getRtpCurrentCheat(),algorithmRuleEntity.getDifference(),algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
        }
    }
}