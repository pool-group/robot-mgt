package com.zren.platform.biz.service.impl.handle;

import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class BizBaseHandle {

    @Autowired
    protected RedisTemplate redisTemplate;

    public abstract void excute(RuleOutputModelDTO t, RuleInputModelDTO ruleInputModelDTO);

    public abstract Integer getCode();
}
