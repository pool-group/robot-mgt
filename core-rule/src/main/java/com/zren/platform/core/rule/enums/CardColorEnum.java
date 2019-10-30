package com.zren.platform.core.rule.enums;

/**
 * @author k.y
 * @version Id: CardColorEnum.java, v 0.1 2018年11月20日 下午10:31 k.y Exp $
 */
public enum  CardColorEnum {

    BIG_KING(6),
    SMALL_KING(5),
    SPADES(4),
    RED_PEACH(3),
    PLUM(2),
    BLOCK(1);


    private int priority;

    public int getPriority() {
        return priority;
    }

    CardColorEnum(int priority) {
        this.priority = priority;
    }
}
