package com.zren.platform.common.util.tool;

import java.text.MessageFormat;

/**
 * redis 键值KEY生成类
 *
 * @author k.y
 * @version Id: RedisCommon.java, v 0.1 2018年11月27日 下午21:27 k.y Exp $
 */
public class RedisCommon {

    /**备份机器人信息*/
    public final static String BACKUP_ROBOTINFO="backupRobotInfo";
    /**机器人信息*/
    public final static String ROBOTINFO="robotInfo";
    /**策略*/
    public final static String STRATEGY="strategy";

    /**
     * 炸金花某一桌台唯一KEY
     *
     * @param type
     * @param gameId
     * @param roomId
     * @param tableId
     * @return
     */
    public static String getUserRedisKey(String type,int gameId,int roomId,String tableId){
        return MessageFormat.format("game_{0}:"+"room_{1}:"+"table_{2}:{3}" ,gameId,roomId,tableId,type);
    }

    /**
     * 炸金花某一桌台用户唯一KEY
     *
     * @param type
     * @param gameId
     * @param roomId
     * @param tableId
     * @return
     */
    public static String getRedisStrategyKey(int gameId,int roomId,String tableId,String userId,String type){
        return MessageFormat.format("game_{0}:"+"room_{1}:"+"table_{2}:"+"{3}:userId_{4}" ,gameId,roomId,tableId,type,userId);
    }

    /**
     * 账户锁
     *
     * @param brand
     * @param gameId
     * @param roomId
     * @return
     */
    public static String getAccountLock(String brand,int gameId,int roomId){
        return MessageFormat.format("brand_{0}:game_{1}:"+"room_{2}:"+"robot.account.Lock" ,brand,gameId,roomId);
    }

    /**
     * 机器人规则锁
     *
     * @param brand
     * @param gameId
     * @param roomId
     * @return
     */
    public static String getRobotRuleLock(String brand,int gameId,int roomId){
        return MessageFormat.format("brand_{0}:game_{1}:"+"room_{2}:"+"robot.rule.Lock" ,brand,gameId,roomId);
    }

    /**
     * Player Strategy Key
     *
     * @param brand
     * @param gameId
     * @param roomId
     * @return
     */
    public static String getPlayerStrategyKey(String brand,int gameId,int roomId){
        return MessageFormat.format("playerStrategyKey:brand_{0}:game_{1}:room_{2}" ,brand,gameId,roomId);
    }

    /**
     * Deny Player Strategy Key
     *
     * @param brand
     * @param gameId
     * @param roomId
     * @return
     */
    public static String getDenyPlayerStrategyKey(String brand,int gameId,int roomId){
        return MessageFormat.format("playerStrategyKey:deny:brand_{0}:game_{1}:room_{2}" ,brand,gameId,roomId);
    }

    /**
     * Deny Player Strategy Key
     *
     * @param brand
     * @param gameId
     * @param roomId
     * @return
     */
    public static String getStandardStrategyKey(String brand,int gameId,int roomId){
        return MessageFormat.format("standardStrategyKey:brand_{0}:game_{1}:room_{2}" ,brand,gameId,roomId);
    }

    /**
     * Deny Player Strategy Key
     *
     * @param brand
     * @param gameId
     * @param roomId
     * @return
     */
    public static String getExpirePlayerStrategyKey(String brand,int gameId,int roomId){
        return MessageFormat.format("playerStrategyKey:expire:brand_{0}:game_{1}:room_{2}" ,brand,gameId,roomId);
    }

    /**
     * player strategy lock
     *
     * @param brand
     * @param gameId
     * @param roomId
     * @return
     */
    public static String getplayerStrategyLock(String brand,int gameId,int roomId){
        return MessageFormat.format("playerStrategyKey:brand_{0}:game_{1}:room_{2}:player.strategy.Lock" ,brand,gameId,roomId);
    }
}
