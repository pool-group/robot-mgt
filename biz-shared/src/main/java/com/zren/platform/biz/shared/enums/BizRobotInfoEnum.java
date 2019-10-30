package com.zren.platform.biz.shared.enums;

/**
 * 机器人业务枚举定义
 *
 * @author k.y
 * @version Id: BizRobotInfoEnum.java, v 0.1 2018年11月07日 下午14:49 k.y Exp $
 */
public enum  BizRobotInfoEnum {

    IS_SUCCESS("1","IS SUCCESS","已消费"),

    FREE_STATUS("0","Robot is free","闲置状态"),

    BUSY_STATUS("1","Robot is busy","繁忙状态"),

    ACCOUNT_WATER_GAIN_LOSS_IN("2","ACCOUNT WATER GAIN LOSS IN","盈亏明细"),

    ACCOUNT_WATER_IN("1","ACCOUNT WATER IN","进帐流水"),

    ACCOUNT_WATER_OUT("0","ACCOUNT WATER OUT","出帐流水");


    private String code;
    private String msg;
    private String desc;


    BizRobotInfoEnum(String code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
