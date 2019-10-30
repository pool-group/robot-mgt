package com.zren.platform.core.rule.enums;

/**
 * 策略枚举
 *
 * @author k.y
 * @version Id: StrategyEnums.java, v 0.1 2018年11月17日 下午16:51 k.y Exp $
 */
public enum StrategyEnums {


    LOSS(0, "LOSS", "机器人输"),

    WIN(1, "WIN", "机器人赢"),

    RAMDOM(2, "RAMDOM", "胜负随机");


    private Integer code;

    private String msg;

    private String desc;

    StrategyEnums(Integer code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
