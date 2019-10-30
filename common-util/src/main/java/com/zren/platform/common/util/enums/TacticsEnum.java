package com.zren.platform.common.util.enums;

/**
 * Tactics Enum
 *
 * @author k.y
 * @version Id: TacticsEnum.java, v 0.1 2019年09月24日 下午10:31 k.y Exp $
 */
public enum TacticsEnum {

    MIN(-1),
    LOSS(0),
    GAIN(1),
    RANDOM(2),
    SEC_MAX(3);

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    TacticsEnum(Integer code) {
        this.code=code;
    }
}
