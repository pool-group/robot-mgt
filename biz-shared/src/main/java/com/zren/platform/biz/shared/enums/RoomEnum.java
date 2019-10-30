package com.zren.platform.biz.shared.enums;

/**
 * @author k.y
 * @version Id: RoomEnum.java, v 0.1 2019年08月17日 下午17:35 k.y Exp $
 */
public enum  RoomEnum {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private int code;

    RoomEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
