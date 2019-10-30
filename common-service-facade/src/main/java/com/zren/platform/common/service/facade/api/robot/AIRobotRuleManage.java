package com.zren.platform.common.service.facade.api.robot;

import com.zren.platform.common.service.facade.constants.Common;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 机器人规则策略接口
 *
 * @author k.y
 * @version Id: AIRobotRuleManage.java, v 0.1 2018年11月21日 下午10:26 k.y Exp $
 */
@FeignClient(value= Common.SERVICE_NAME)
public interface AIRobotRuleManage {


    /**
     * 获取规则策略
     *
     * @param ruleInputModelDTO
     * @return
     */
    @PostMapping("/aiRobotRuleManage/createRule")
    RobotBaseResult createRule(RuleInputModelDTO ruleInputModelDTO);

}
