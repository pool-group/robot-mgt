package com.zren.platform.core.rule.strategy;

/**
 * 引擎基类
 *
 * @author k.y
 * @version Id: BaseRuleEngine.java, v 0.1 2018年11月17日 下午12:40 k.y Exp $
 */
public abstract class BaseRuleEngine<P,R> {

    public abstract R start(P param);
}
