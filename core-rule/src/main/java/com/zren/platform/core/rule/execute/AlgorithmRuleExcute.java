package com.zren.platform.core.rule.execute;

import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.BaseStrategyDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.util.enums.EnumsCommon;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.ProbabilisticUtil;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.strategy.BaseRuleEngine;
import com.zren.platform.core.rule.strategy.common.process.PlayerStrategyProcess;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AlgorithmRuleExcute implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    PlayerStrategyProcess playerStrategyProcess;


    public Void execute(AlgorithmRuleEntity algorithmRuleEntity){

        String beanName=EnumsCommon.getBean(algorithmRuleEntity.getGameId());
        BaseRuleEngine<AlgorithmRuleEntity,Void> baseRuleEngine = applicationContext.getBean(StringUtils.isNotBlank(beanName)?beanName:EnumsCommon.getBean(20), BaseRuleEngine.class);
        if (null==baseRuleEngine)
            return null;
        else {
            baseRuleEngine.start(algorithmRuleEntity);
            if(algorithmRuleEntity.getBool()){
                playerStrategyProcess.invoke(algorithmRuleEntity);
            }
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AlgorithmRuleExcute.applicationContext =applicationContext;
    }
}
