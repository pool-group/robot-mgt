package com.zren.platform.biz.service.impl.manage;

import com.zren.platform.biz.service.impl.handle.BizBaseHandle;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.core.robot.AIRobotRuleManageServiceImpl;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.common.service.facade.api.robot.AIRobotRuleManage;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 規則策略实现
 *
 * @author k.y
 * @version Id: RobotRuleManageServiceImpl.java, v 0.1 2018年12月11日 下午17:02 k.y Exp $
 */
@RestController
@Slf4j
public class AIRobotRuleManageImpl implements AIRobotRuleManage {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    /**規則策略*/
    @Autowired
    private AIRobotRuleManageServiceImpl aIRobotRuleManageServiceImpl;

    /**上下文*/
    @Autowired
    private ApplicationContextUtil applicationContextUtil;

    /**
     * 获取规则策略
     *
     * @param ruleInputModelDTO  平台  游戏类型  房间ID  机器人ID
     * @return
     */
    @Override
    public RobotBaseResult createRule(@RequestBody RuleInputModelDTO ruleInputModelDTO) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<RuleInputModelDTO,RuleOutputModelDTO>(){

            @Override
            public void doProcess(EngineContext<RuleInputModelDTO, RuleOutputModelDTO> context) throws InterruptedException {

                RobotBaseResult<RuleOutputModelDTO> ruleResult=aIRobotRuleManageServiceImpl.createRule(ruleInputModelDTO);
                if(ruleResult.isSuccess()==false){
                    throw new RobotSystemException(ruleResult.getErrorContext().fetchCurrentError().getDescription(),ErrorCodeEnum.MODULE_INVOKE_ERROR);
                }
                Map<String, BizBaseHandle> map = applicationContextUtil.getInstance().getBeansOfType(BizBaseHandle.class);
                map.forEach((key, handle) -> {
                    Integer code = handle.getCode();
                    if (handle instanceof BizBaseHandle&&code.equals(ruleInputModelDTO.getGameId())) {
                        handle.excute(ruleResult.getResultObj(),ruleInputModelDTO);
                    }
                });
                context.setOutputModel(ruleResult.getResultObj());
            }
        });
    }
}
