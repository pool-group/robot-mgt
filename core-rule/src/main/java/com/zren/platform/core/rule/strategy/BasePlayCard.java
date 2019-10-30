package com.zren.platform.core.rule.strategy;

/**
 * 不同游戏通用比牌抽象
 *
 * @author k.y
 * @version Id: BasePlayCard.java, v 0.1 2018年11月20日 下午11:14 k.y Exp $
 */
public abstract class BasePlayCard {

    /**
     * 比牌抽象方法
     *
     * @param f
     * @param t
     * @return
     */
    public abstract Boolean compareTo(int[] f, int[] t);
}
