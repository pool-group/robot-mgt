package com.zren.platform.biz.service.impl.handle;

import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 业务handle基类
 *
 * @author k.y
 * @version Id: BizBaseHandle.java, v 0.1 2018年12月11日 下午17:20 k.y Exp $
 */
@Component
public abstract class BizBaseHandle {

    @Autowired
    protected RedisTemplate redisTemplate;

    public abstract void excute(RuleOutputModelDTO t, RuleInputModelDTO ruleInputModelDTO);

    public abstract Integer getCode();
}
