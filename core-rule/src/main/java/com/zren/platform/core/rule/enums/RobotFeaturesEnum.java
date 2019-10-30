package com.zren.platform.core.rule.enums;

/**
 * Robot Behavior and Characteristic Attributes
 *
 * @author k.y
 * @version Id: CardTypeEnum.java, v 0.1 2018年11月20日 下午10:37 k.y Exp $
 */
public enum RobotFeaturesEnum {
    FOLLOW,
    LEVEL_ONE,
    LEVEL_TWO,
    LEVEL_THREE,
    LEVEL_FOUR,
    ADD,
    NORMAL,
    BOLD,
    CAUTIOUS,
    LEVEL1,
    LEVEL2,
    LEVEL3,
    LEVEL4,
    LEVEL12,
    LEVEL23,
    LEVEL34,
    LEVEL234,
    LEVEL1234;

    public static RobotFeaturesEnum features(Byte i){
        switch (i){
            case 1:
                return NORMAL;
            case 2:
                return BOLD;
            case 3:
                return CAUTIOUS;
            default:
                return NORMAL;
        }
    }

    public static Boolean isLevel(RobotFeaturesEnum level,RobotFeaturesEnum vs){
        switch (level){
            case LEVEL1:
                return vs==LEVEL_ONE;
            case LEVEL2:
                return vs==LEVEL_TWO;
            case LEVEL3:
                return vs==LEVEL_THREE;
            case LEVEL4:
                return vs==LEVEL_FOUR;
            case LEVEL12:
                return (vs==LEVEL_ONE||vs==LEVEL_TWO);
            case LEVEL23:
                return (vs==LEVEL_TWO||vs==LEVEL_THREE);
            case LEVEL34:
                return (vs==LEVEL_THREE||vs==LEVEL_FOUR);
            case LEVEL234:
                return (vs==LEVEL_TWO||vs==LEVEL_THREE||vs==LEVEL_FOUR);
            case LEVEL1234:
                return (vs==LEVEL_ONE||vs==LEVEL_TWO||vs==LEVEL_THREE||vs==LEVEL_FOUR);
            default:
                return false;
        }
    }
}
