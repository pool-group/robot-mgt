package com.zren.platform.biz.service.impl.manage;

import com.zren.platform.biz.action.handle.DelayQueueHandle;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.core.robot.RobotManageServiceImpl;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.biz.shared.template.transaction.RobotTransactionCallback;
import com.zren.platform.biz.shared.template.transaction.RtTransactionTemplate;
import com.zren.platform.common.service.facade.api.robot.RobotPullManage;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotDestroyInputModelDTO;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotInitInputModelDTO;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.robotInfo.RobotInfoDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AI机器人管理实现
 *
 * @author k.y
 * @version Id: AIRobotManageServiceImpl.java, v 0.1 2018年11月23日 下午17:36 k.y Exp $
 */
@RestController
@Slf4j
public class RobotPullManageImpl implements RobotPullManage {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    /**获取机器人*/
    @Autowired
    private RobotManageServiceImpl robotManageServiceImpl;

    /**延时队列操作*/
    @Autowired
    private DelayQueueHandle delayQueueHandle;

    /**事务模板*/
    @Autowired
    private RtTransactionTemplate rtTransactionTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**规则策略接口*/
    @Autowired
    private AIRobotRuleManageImpl aIRobotRuleManageImpl;


    @Override
    public RobotBaseResult createRobot(@RequestBody RobotInitInputModelDTO robotInitInputModelDTO) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<RobotInitInputModelDTO,RuleOutputModelDTO>(){

            @Override
            public void initContent(EngineContext<RobotInitInputModelDTO, RuleOutputModelDTO> context) {
                context.setInputModel(robotInitInputModelDTO);
            }

            @Override
            public void doProcess(EngineContext<RobotInitInputModelDTO, RuleOutputModelDTO> context) throws InterruptedException {

                rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                    @Override
                    public void execute() {
                        //初始化机器人基本信息
                        RobotBaseResult<List<RobotInfoDTO>> result=robotManageServiceImpl.receiveRobotInfo(robotInitInputModelDTO);
                        if(result.isSuccess()==false){
                            throw new RobotSystemException(result.getErrorContext().fetchCurrentError().getDescription(),ErrorCodeEnum.MODULE_INVOKE_ERROR);
                        }
                        List<RobotInfoDTO> lst=result.getResultObj();
                        //提取机器人ID
                        List<Long> userIdlist=lst.stream().mapToLong(RobotInfoDTO::getUserId).boxed().collect(Collectors.toList());

                        //获取机器人规则
                        RuleInputModelDTO zJHRuleInputModelDTO=new RuleInputModelDTO();
                        zJHRuleInputModelDTO.setBrand(robotInitInputModelDTO.getBrand());
                        zJHRuleInputModelDTO.setGameId(robotInitInputModelDTO.getGameId());
                        zJHRuleInputModelDTO.setRoomId(robotInitInputModelDTO.getRoomId());
                        zJHRuleInputModelDTO.setTableId(robotInitInputModelDTO.getTableId());
                        zJHRuleInputModelDTO.setUserIdlist(userIdlist);
                        RobotBaseResult<RuleOutputModelDTO> ruleResult=aIRobotRuleManageImpl.createRule(zJHRuleInputModelDTO);
                        if(ruleResult.isSuccess()==false){
                            throw new RobotSystemException(ruleResult.getErrorContext().fetchCurrentError().getDescription(),ErrorCodeEnum.MODULE_INVOKE_ERROR);
                        }
                        RuleOutputModelDTO ruleOutputModelDTO=ruleResult.getResultObj();
                        ruleOutputModelDTO.setRobotInfolst(lst);
                        context.setOutputModel(ruleOutputModelDTO);
                    }
                });
            }
        });
    }

    @Override
    public RobotBaseResult destroyRobot(@RequestBody List<RobotDestroyInputModelDTO> robotDestroyInputModelList) {
        return robotManageServiceImpl.destroyRobot(robotDestroyInputModelList);
    }
}
