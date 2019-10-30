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
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class AIRobotRuleManageImpl implements AIRobotRuleManage {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private AIRobotRuleManageServiceImpl aIRobotRuleManageServiceImpl;

    @Autowired
    private ApplicationContextUtil applicationContextUtil;

    @Override
    public RobotBaseResult createRule(@RequestBody RuleInputModelDTO ruleInputModelDTO) {
        return createRuleProcess(ruleInputModelDTO,true);
    }

    public RobotBaseResult createRuleProcess(RuleInputModelDTO ruleInputModelDTO,Boolean bool){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<RuleInputModelDTO,RuleOutputModelDTO>(){

            @Override
            public void doProcess(EngineContext<RuleInputModelDTO, RuleOutputModelDTO> context){

                RobotBaseResult<RuleOutputModelDTO> ruleResult=aIRobotRuleManageServiceImpl.createRule(ruleInputModelDTO,bool);
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
