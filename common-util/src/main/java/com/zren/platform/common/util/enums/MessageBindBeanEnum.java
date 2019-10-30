package com.zren.platform.common.util.enums;

/**
 * 消息事件分发绑定
 *
 * @author k.y
 * @version Id: MessageBindBeanEnum.java, v 0.1 2018年11月27日 下午11:45 k.y Exp $
 */
public enum MessageBindBeanEnum {

    ZJH_OTHER_BOT_LOOK_HANDLE(50030,"zjh_other_bot_look_handle","非当前操作机器人看牌策略"),
    ZJH_SNAPSHOT_HANDLE(4000,"zjh_snapshot_handle","快照"),
    ZJH_CHANGE_TABLE_HANDLE(5001,"zjh_change_table_handle","换桌台"),
    ZJH_LOOK_HANDLE(5003,"zjh_look_handle","看牌"),
    ZJH_GIVE_UP_HANDLE(5004,"zjh_give_up_handle","弃牌"),
    ZJH_CURRENT_HANDLE(5007,"zjh_current_handle","当前操作"),
    ZJH_JOIN_TABLE_HANDLE(5008,"zjh_join_table_handle","加入桌台"),
    ZJH_LEAVE_TABLE_HANDLE(5009,"zjh_leave_table_handle","离开桌台"),
    ZJH_GAME_OVER_HANDLE(5010,"zjh_game_over_handle","游戏结束"),
    ZJH_DRIVE_OUT_HANDLE(5012,"zjh_drive_out_handle","踢出");


    private Integer code;
    private String name;
    private String desc;


    MessageBindBeanEnum(Integer code, String name, String desc) {
        this.code = code;
        this.name = name;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getName(Integer code){

        for(MessageBindBeanEnum e:MessageBindBeanEnum.values()){
            if(e.getCode().equals(code)){
                return e.getName();
            }
        }
        return null;
    }
}
