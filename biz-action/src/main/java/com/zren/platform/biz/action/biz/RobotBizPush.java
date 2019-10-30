package com.zren.platform.biz.action.biz;

import com.zren.platform.biz.action.rocketmq.producer.common.RobotPushProducer;
import com.zren.platform.biz.shared.core.robot.AIRobotRuleManageServiceImpl;
import com.zren.platform.biz.shared.core.robot.RobotManageServiceImpl;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.biz.shared.template.transaction.RobotTransactionCallback;
import com.zren.platform.biz.shared.template.transaction.RtTransactionTemplate;
import com.zren.platform.common.service.facade.dto.in.robotInfo.AIRobotInitInputModelDTO;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotInitInputModelDTO;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.BaseStrategyDTO;
import com.zren.platform.common.service.facade.dto.out.robotInfo.RobotInfoDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RobotBizPush {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private RobotManageServiceImpl robotManageServiceImpl;

    @Autowired
    private RtTransactionTemplate rtTransactionTemplate;

    @Autowired
    private AIRobotRuleManageServiceImpl aIRobotRuleManageServiceImpl;

    @Autowired
    private RobotPushProducer robotPushProducer;

    public void createRobot(AIRobotInitInputModelDTO robotInitInputModelDTO) {

            rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                @Override
                public void execute() {
                    List<BaseStrategyDTO> strategylist=null;
                    //初始化机器人基本信息
                    RobotInitInputModelDTO dto=new RobotInitInputModelDTO();
                    dto.setGameId(robotInitInputModelDTO.getGameId());
                    dto.setRoomId(robotInitInputModelDTO.getRoomId());
                    dto.setBrand(robotInitInputModelDTO.getBrand());
                    dto.setCount(robotInitInputModelDTO.getCount());
                    dto.setMinAmount(robotInitInputModelDTO.getMinAmount());
                    RobotBaseResult<List<RobotInfoDTO>> result=robotManageServiceImpl.receiveRobotInfo(dto);
                    List<RobotInfoDTO> lst=result.getResultObj();
                    if(result.isSuccess()==false){
                        throw new RobotSystemException(result.getErrorContext().fetchCurrentError().getDescription(),ErrorCodeEnum.MODULE_INVOKE_ERROR);
                    }

                    List<Long> userIdlist=lst.stream().mapToLong(RobotInfoDTO::getUserId).boxed().collect(Collectors.toList());
                    RuleInputModelDTO ruleDto=new RuleInputModelDTO();
                    ruleDto.setBrand(robotInitInputModelDTO.getBrand());
                    ruleDto.setGameId(robotInitInputModelDTO.getGameId());
                    ruleDto.setRoomId(robotInitInputModelDTO.getRoomId());
                    ruleDto.setTableId(robotInitInputModelDTO.getTableId());
                    ruleDto.setUserIdlist(userIdlist);
                    RobotBaseResult<RuleOutputModelDTO> ruleResult=aIRobotRuleManageServiceImpl.createRule(ruleDto,false);
                    if(ruleResult.isSuccess()==false){
                        throw new RobotSystemException(result.getErrorContext().fetchCurrentError().getDescription(),ErrorCodeEnum.MODULE_INVOKE_ERROR);
                    }
                    strategylist=ruleResult.getResultObj().getStrategylist();

                    robotPushProducer.push(lst,strategylist,robotInitInputModelDTO);
                }
            });
        }

}
