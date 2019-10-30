package com.zren.platform.core.rule.strategy.common.process;

import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.enums.StrategyEnums;
import com.zren.platform.core.rule.factory.CronRuleFactory;
import com.zren.platform.core.rule.factory.ProbabilisticFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 机器人胜负策略
 *
 * @author k.y
 * @version Id: StrategyMain.java, v 0.1 2018年11月17日 下午16:07 k.y Exp $
 */
@Component
public class StrategyWinProcess {

    /**CronRule表达式工厂*/
    @Autowired
    private CronRuleFactory cronRuleFactory;

    /**概率事件工厂*/
    @Autowired
    private ProbabilisticFactory probabilisticFactory;


    /**
     * 牌局胜负策略
     *
     * @param ar
     * @param chessContext
     */
    public void excute(AlgorithmRuleEntity ar,ChessContext chessContext){

        Byte controlType=ar.getControlType();
        switch (controlType){
            case 0: //按比例

                BigDecimal rate=ar.getGainLoss().divide(ar.getCapital(),4);
                //判断Cron策略表达式是否存在
                if(rate.compareTo(BigDecimal.valueOf(0))==-1&&StringUtils.isNotBlank(ar.getLossCronRule())){
                    handleGainLossMainRule(ar,chessContext,ar.getLossCronRule(),rate);
                    break;
                }else if(rate.compareTo(BigDecimal.valueOf(0))>0&&ar.getRtpAble().compareTo((byte) 1)==0){
                    handleGainMainRule(ar,chessContext,ar.getGainCronRule(),ar.getRtpCurrentCheat());
                    break;
                } else{
                    LogUtil.info(Log.CRON.LOG,String.format("brand=[ %s ], gameId=[ %s ], roomId=[ %s ], Cron rule is null or gainLoss is 0, start using defaultRateRule is success.. ",ar.getBrand(),ar.getGameId(),ar.getRoomId()));
                    //Cron策略表达式不存在, 使用默认规则
                    defaultRateRule(ar,rate,chessContext);
                    break;
                }

            case 1: //注意：目前按额度只支持默认规则，不支持扩展策略

                BigDecimal amount=ar.getGainLoss();
                if(amount.compareTo(BigDecimal.valueOf(0))==-1&&amount.abs().compareTo(ar.getMaxLoss())>=0){
                    chessContext.setRobotry(StrategyEnums.WIN.getCode());
                    break;
                }
                if(amount.compareTo(BigDecimal.valueOf(0))==1&&amount.compareTo(ar.getMaxGain())>=0){
                    chessContext.setRobotry(StrategyEnums.LOSS.getCode());
                    break;
                }
                chessContext.setRobotry(StrategyEnums.RAMDOM.getCode());
                break;
        }
    }

    /**
     * 默认规则
     *     说明：默认规则兜底，及表达式配置出现问题，上下限控制
     *
     * @param ar
     * @param rate
     * @param chessContext
     */
    public void defaultRateRule(AlgorithmRuleEntity ar,BigDecimal rate,ChessContext chessContext){
        if(ar.getGainLoss().compareTo(BigDecimal.valueOf(0))==-1&&(rate.abs()).compareTo(ar.getMaxLossRate())>=0){
            chessContext.setRobotry(StrategyEnums.WIN.getCode());

        }
        else if(ar.getGainLoss().compareTo(BigDecimal.valueOf(0))==1&&rate.compareTo(ar.getMaxGainRate())>=0){
            chessContext.setRobotry(StrategyEnums.RAMDOM.getCode());
        } else {
            //默认规则情况下，随机情况下
            LogUtil.info(Log.CRON.LOG,String.format(" Cron rule is pass ..  , Rate event is: rateResult=[ %s ]","Ramdom"));
            chessContext.setRobotry(StrategyEnums.RAMDOM.getCode());
            LogUtil.info(Log.CRON.LOG,String.format(" Rate Hit is: isHit=[ %s ], brand=[ %s ], gameId=[ %s ], roomId=[ %s ] -> Ramdom",2,ar.getBrand(),ar.getGameId(),ar.getRoomId()));
        }
    }


    /**
     * 亏损态策略逻辑处理
     *     说明：亏损态，计算的是触发作弊几率(结果=触发作弊几率+逻辑中随机得到最大牌几率)。
     *
     * @param ar
     * @param chessContext
     * @param cronRule
     * @param rate
     */
    public void handleGainLossMainRule(AlgorithmRuleEntity ar,ChessContext chessContext,String cronRule,BigDecimal rate){
        try {
            //检测Cron策略表达式有效性并解析
            BigDecimal rateResult=cronRuleFactory.resolve(cronRule,rate);
            LogUtil.info(Log.CRON.LOG,String.format(" handleLossMainRule -> Cron rule is pass ..  , Rate event is: rateResult=[ %s ]",rateResult.toString()));
            //调用概率工厂,启用概率事件,通过概率获取结果[命中概率事件返回：1,否则：0]
            int isHit=probabilisticFactory.excute(rateResult);
            LogUtil.info(Log.CRON.LOG,String.format(" Rate Hit is: isHit=[ %s ], brand=[ %s ], gameId=[ %s ], roomId=[ %s ]",isHit==0?2:1,ar.getBrand(),ar.getGameId(),ar.getRoomId()));
            if(isHit==1){
                chessContext.setRobotry(StrategyEnums.WIN.getCode());
            }else {
                chessContext.setRobotry(StrategyEnums.RAMDOM.getCode());
            }
        } catch (RobotBizException e) {
            LogUtil.warn(Log.CRON.LOG,String.format("code=[ %s ],codeType=[ %s ],codeLevel=[ %s ],message=[ %s ],desc=[ %s ]", e.getErrorCodeEnum().getCode(),e.getErrorCodeEnum().getCodeType(),e.getErrorCodeEnum().getCodeLevel(),e.getErrorCodeEnum().getMessage(),e.getErrorCodeEnum().getDesc()));
            //Cron策略表达式解析失败, 使用默认规则
            defaultRateRule(ar,rate,chessContext);
            LogUtil.info(Log.CRON.LOG," Cron rule is error, start using defaultRateRule is success.. ");
        } catch (Exception e) {
            LogUtil.error(Log.CRON.LOG,String.format("code=[ %s ],codeType=[ %s ],codeLevel=[ %s ],message=[ %s ],desc=[ %s ]", ErrorCodeEnum.CRON_RULE_SYS.getCode(),ErrorCodeEnum.CRON_RULE_SYS.getCodeType(),ErrorCodeEnum.CRON_RULE_SYS.getCodeLevel(),ErrorCodeEnum.CRON_RULE_SYS.getMessage(),ErrorCodeEnum.CRON_RULE_SYS.getDesc()));
            //Cron策略表达式解析失败, 使用默认规则
            defaultRateRule(ar,rate,chessContext);
            LogUtil.info(Log.CRON.LOG," Cron rule is error, start using defaultRateRule is success.. ");
        }
    }


    /**
     * 盈利态策略逻辑处理
     *     说明：盈利态，计算的是胜负几率(最终结果) ，好处：盈利态控制盈亏比较灵活，否则出现只能控制作弊的的概率，不能降低胜率放水的情况。
     *
     * @param ar
     * @param chessContext
     * @param cronRule
     * @param rate
     */
    public void handleGainMainRule(AlgorithmRuleEntity ar,ChessContext chessContext,String cronRule,BigDecimal rate){
        try {
            //检测Cron策略表达式有效性并解析
            //调用概率工厂,启用概率事件,通过概率获取结果[命中概率事件返回：1,否则：0]
            int isHit=probabilisticFactory.excute(rate);
            LogUtil.info(Log.RTP.LOG,String.format(" Rate Hit is: isHit=[ %s ], brand=[ %s ], gameId=[ %s ], roomId=[ %s ]",isHit==0?2:1,ar.getBrand(),ar.getGameId(),ar.getRoomId()));
            if(isHit==1){
                chessContext.setRobotry(StrategyEnums.WIN.getCode());
            }else {
                chessContext.setRobotry(StrategyEnums.RAMDOM.getCode());
            }
        } catch (RobotBizException e) {
            LogUtil.warn(Log.RTP.LOG,String.format("code=[ %s ],codeType=[ %s ],codeLevel=[ %s ],message=[ %s ],desc=[ %s ]", e.getErrorCodeEnum().getCode(),e.getErrorCodeEnum().getCodeType(),e.getErrorCodeEnum().getCodeLevel(),e.getErrorCodeEnum().getMessage(),e.getErrorCodeEnum().getDesc()));
            //Cron策略表达式解析失败, 使用默认规则
            defaultRateRule(ar,rate,chessContext);
            LogUtil.info(Log.RTP.LOG," Cron rule is error, start using defaultRateRule is success.. ");
        } catch (Exception e) {
            LogUtil.error(Log.RTP.LOG,String.format("code=[ %s ],codeType=[ %s ],codeLevel=[ %s ],message=[ %s ],desc=[ %s ]", ErrorCodeEnum.CRON_RULE_SYS.getCode(),ErrorCodeEnum.CRON_RULE_SYS.getCodeType(),ErrorCodeEnum.CRON_RULE_SYS.getCodeLevel(),ErrorCodeEnum.CRON_RULE_SYS.getMessage(),ErrorCodeEnum.CRON_RULE_SYS.getDesc()));
            //Cron策略表达式解析失败, 使用默认规则
            defaultRateRule(ar,rate,chessContext);
            LogUtil.info(Log.RTP.LOG," Cron rule is error, start using defaultRateRule is success.. ");
        }
    }


}
