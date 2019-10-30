package com.zren.platform.core.rule.enums;

/**
 * 牌型枚举
 *
 * @author k.y
 * @version Id: CardTypeEnum.java, v 0.1 2018年11月20日 下午10:37 k.y Exp $
 */
public enum  CardTypeEnum {

    UNKNOW(7,"未看牌"),

    /**
     *  豹子：三张同样大小的牌。　　
     */
    THREE_SAME(6,"豹子"),
    /**
     * 顺金：花色相同的三张连牌。　　
     */
    SAME_COLOR_TOGETHER(5,"顺金"),
    /**
     * 金花：三张花色相同的牌,但不相连。
     */
    SAME_COLOR_UNTOGETHER(4,"金花"),
    /**
     * 顺子:三张花色不全相同的连牌。　　
     */
    DIF_COLOR_TOGETHER(3,"顺子"),
    /**
     * 对子：三张牌中有两张同样大小的牌。
     */
    TWO_SAME(2,"对子"),
    /**
     * 单张：除以上牌型的牌。
     */
    SINGLE(1,"单张");

    private int priority;
    private String typeName;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    CardTypeEnum(int priority,String typeName) {
        this.priority = priority;
        this.typeName = typeName;
    }
}
