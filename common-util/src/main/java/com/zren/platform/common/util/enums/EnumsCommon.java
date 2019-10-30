package com.zren.platform.common.util.enums;

/**
 * 数据事件分发通用枚举
 *
 * @author k.y
 * @version Id: EnumsCommon.java, v 0.1 2018年11月26日 下午14:00 k.y Exp $
 */
public enum EnumsCommon {

    ZJH(10,"zjh", "zjh_game" ,"assemble_zjh","炸金花"),

    NIUNIU_QZ(22,"niuniu_qz", "niuniu_common" ,"assemble_niuniu","牛牛抢庄"),

    NIUNIU_TB(20,"niuniu_tb", "niuniu_common" ,"assemble_niuniu","牛牛通比"),

    NIUNIU_YZ(21,"niuniu_yz", "niuniu_common" ,"assemble_niuniu","牛牛押注");

    /**游戏ID*/
    private Integer code;
    /**游戏名称 注意：必须与配置文件rocketMQ键值一致*/
    private String name;
    /**根据游戏类型，业务流分发bean名称*/
    private String bean;
    private String assebleBean;
    /**描述信息*/
    private String desc;


    EnumsCommon(Integer code, String name, String bean, String assebleBean, String desc) {
        this.code = code;
        this.name = name;
        this.bean = bean;
        this.assebleBean = assebleBean;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getAssebleBean() {
        return assebleBean;
    }

    public void setAssebleBean(String assebleBean) {
        this.assebleBean = assebleBean;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getName(Integer code){

        for(EnumsCommon e:EnumsCommon.values()){
            if(e.getCode().equals(code)){
                return e.getName();
            }
        }
        return null;
    }

    public static String getBean(Integer code){

        for(EnumsCommon e:EnumsCommon.values()){
            if(e.getCode().equals(code)){
                return e.getBean();
            }
        }
        return null;
    }

    public static String getAssebleBean(Integer code){

        for(EnumsCommon e:EnumsCommon.values()){
            if(e.getCode().equals(code)){
                return e.getAssebleBean();
            }
        }
        return null;
    }
}
