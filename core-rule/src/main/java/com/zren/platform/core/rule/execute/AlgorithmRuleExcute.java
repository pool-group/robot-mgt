package com.zren.platform.core.rule.execute;

import com.zren.platform.common.util.enums.EnumsCommon;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.strategy.BaseRuleEngine;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 流转
 *
 * @author k.y
 * @version Id: AlgorithmMainRule.java, v 0.1 2018年11月17日 下午11:22 k.y Exp $
 */
@Component
public class AlgorithmRuleExcute implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    public Void execute(AlgorithmRuleEntity algorithmRuleEntity){

        String beanName=EnumsCommon.getBean(algorithmRuleEntity.getGameId());
        //注意：如果没有配置新游戏映射流转,则使用默认的通比牛牛规则
        BaseRuleEngine<AlgorithmRuleEntity,Void> baseRuleEngine = applicationContext.getBean(StringUtils.isNotBlank(beanName)?beanName:EnumsCommon.getBean(20), BaseRuleEngine.class);
        return null==baseRuleEngine?null:baseRuleEngine.start(algorithmRuleEntity);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
