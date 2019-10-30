package com.zren.platform.biz.shared.core.robot;

import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.core.accountPool.AccountPoolManageService;
import com.zren.platform.biz.shared.core.accountPool.impl.GameRtpStatsServiceImpl;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.common.dal.po.AccountPoolPO;
import com.zren.platform.common.dal.po.GameRtpStatsPO;
import com.zren.platform.common.dal.repository.AccountPoolManageRepository;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.BaseStrategyDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.ProbabilisticUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.execute.AlgorithmRuleExcute;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AIRobotRuleManageServiceImpl {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private AccountPoolManageService accountPoolManageServiceImpl;

    @Autowired
    private AccountPoolManageRepository accountPoolManageRepository;

    @Autowired
    private AlgorithmRuleExcute algorithmRuleExcute;

    @Autowired
    private GameRtpStatsServiceImpl gameRtpStatsServiceImpl;

    @Autowired
    private RedissonClient redisClient;

    public RobotBaseResult createRule(RuleInputModelDTO ruleInputModelDTO,Boolean bool){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<RuleInputModelDTO,RuleOutputModelDTO>(){

            @Override
            public void initContent(EngineContext<RuleInputModelDTO, RuleOutputModelDTO> context) {
                context.setInputModel(ruleInputModelDTO);
            }

            @Override
            public void doProcess(EngineContext<RuleInputModelDTO, RuleOutputModelDTO> context) {

                Long startTime=System.currentTimeMillis();
                RLock lock=redisClient.getLock(RedisCommon.getRobotRuleLock(ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId()));
                lock.lock(10, TimeUnit.SECONDS);
                boolean isLocked = lock.isLocked();
                LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                if(isLocked){
                    try {
                        AccountPoolPO accountPoolPO=accountPoolManageServiceImpl.getUsableCapital(ruleInputModelDTO.getGameId(),ruleInputModelDTO.getBrand(),ruleInputModelDTO.getRoomId());
                        GameRtpStatsPO gameRtpStatsPO= gameRtpStatsServiceImpl.queryRtpResult(ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId());
                        BigDecimal rtpInputInit=accountPoolPO.getRtpInputInit();
                        BigDecimal rtpGainLossInit=BigDecimal.ONE.subtract(accountPoolPO.getRtpValue()).multiply(accountPoolPO.getRtpInputInit()).negate();
                        BigDecimal rtpInputTotal=rtpInputInit.add(gameRtpStatsPO.getTotalInput());
                        BigDecimal rtpPnTotal=rtpGainLossInit.add(gameRtpStatsPO.getTotalPnl());
                        RuleOutputModelDTO dto= new RuleOutputModelDTO();
                        AlgorithmRuleEntity algorithmRuleEntity=new AlgorithmRuleEntity();
                        algorithmRuleEntity.setBrand(accountPoolPO.getBrand());
                        algorithmRuleEntity.setGameId(accountPoolPO.getGameId());
                        algorithmRuleEntity.setRoomId(accountPoolPO.getRoomId());
                        algorithmRuleEntity.setCapital(accountPoolPO.getCapital());
                        algorithmRuleEntity.setRtpValue(accountPoolPO.getRtpValue());
                        algorithmRuleEntity.setRtpCheatInit(accountPoolPO.getRtpCheatInit());
                        algorithmRuleEntity.setRtpIncrease(accountPoolPO.getRtpIncrease());
                        algorithmRuleEntity.setRtpDecrease(accountPoolPO.getRtpDecrease());
                        algorithmRuleEntity.setGainLoss(rtpPnTotal.negate());
                        algorithmRuleEntity.setRtpAble(accountPoolPO.getRtpAble());
                        algorithmRuleEntity.setPositiveGrayRate(accountPoolPO.getPositiveGrayRate());
                        algorithmRuleEntity.setPositiveGrayTime(accountPoolPO.getPositiveGrayTime());
                        algorithmRuleEntity.setReverseGrayRate(accountPoolPO.getReverseGrayRate());
                        algorithmRuleEntity.setReverseGrayTime(accountPoolPO.getReverseGrayTime());
                        algorithmRuleEntity.setPlayerlist(ruleInputModelDTO.getPlayerlist());
                        algorithmRuleEntity.setGrayAble(accountPoolPO.getGrayAble());
                        algorithmRuleEntity.setBool(bool);
                        algorithmRuleEntity.setDenyPipAmount(accountPoolPO.getDenyPipAmount());
                        algorithmRuleEntity.setPositiveTotalbetAlarm(accountPoolPO.getPositiveTotalbetAlarm());
                        algorithmRuleEntity.setReverseTotalbetAlarm(accountPoolPO.getReverseTotalbetAlarm());
                        if(bool){
                            if(algorithmRuleEntity.getGainLoss().compareTo(BigDecimal.ZERO)>0&&accountPoolPO.getRtpAble().compareTo((byte) 1)==0){
                                LogUtil.info(Log.RTP.LOG,String.format("启用RTP规则.. brand=%s, gameId=%s, roomId=%s",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId()));
                                if(rtpInputTotal.compareTo(BigDecimal.valueOf(0))==0){
                                    accountPoolPO.setRtpCurrentCheat(accountPoolPO.getRtpCheatInit());
                                    algorithmRuleEntity.setRealRtpValue(BigDecimal.ONE);
                                    LogUtil.info(Log.RTP.LOG,String.format("当日房间总投入为0, 实际RTP=%s, 初始作弊几率=%s",algorithmRuleEntity.getRealRtpValue(),accountPoolPO.getRtpCurrentCheat()));
                                }else {
                                    algorithmRuleEntity.setRealRtpValue(BigDecimal.ONE.subtract(algorithmRuleEntity.getGainLoss().divide(rtpInputTotal,4)));
                                }
                                if(algorithmRuleEntity.getRealRtpValue().compareTo(algorithmRuleEntity.getRtpValue())>=0){
                                    algorithmRuleEntity.setRtpCurrentCheat(accountPoolPO.getRtpCurrentCheat().add(accountPoolPO.getRtpIncrease()));
                                }else {
                                    //如果rtpDecrease设置为1,相当于直接把当前作弊几率rtpCurrentCheat梯度重置为0
                                    BigDecimal rtpCurrentCheat=accountPoolPO.getRtpCurrentCheat().subtract(accountPoolPO.getRtpDecrease());
                                    algorithmRuleEntity.setRtpCurrentCheat(rtpCurrentCheat.compareTo(BigDecimal.ZERO)==-1?BigDecimal.ZERO:rtpCurrentCheat);
                                }
                                controlRtpCurrentCheat(algorithmRuleEntity,accountPoolPO);

                                LogUtil.info(Log.RTP.LOG,String.format("brand=%s, gameId=%s, roomId=%s,当日机器人盈亏额：%s ,总投入：%s, 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),rtpPnTotal.negate(),rtpInputTotal,algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
                            }else if(algorithmRuleEntity.getGainLoss().compareTo(BigDecimal.ZERO)>0&&accountPoolPO.getRtpAble().compareTo((byte) 0)==0){
                                LogUtil.info(Log.RTP.LOG,String.format("brand=%s, gameId=%s, roomId=%s, RTP规则处于关闭状态",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId()));
                            }else {
                                LogUtil.info(Log.CRON.LOG,String.format("brand=%s, gameId=%s, roomId=%s,当日机器人盈亏额：%s ,总投入：%s, 启用cron rule规则",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),rtpPnTotal.negate(),rtpInputTotal));
                            }
                        }
                        BigDecimal totalgain=rtpPnTotal.negate();
                        BigDecimal shouldgain=BigDecimal.ONE.subtract(algorithmRuleEntity.getRtpValue()).multiply(rtpInputTotal);
                        BigDecimal difference=totalgain.subtract(shouldgain);
                        algorithmRuleEntity.setReverseStage(accountPoolPO.getReverseStage());
                        algorithmRuleEntity.setReverseRate(accountPoolPO.getReverseRate());
                        algorithmRuleEntity.setDifference(difference);
                        algorithmRuleEntity.setControlType(accountPoolPO.getControlType());
                        algorithmRuleEntity.setMaxGain(accountPoolPO.getMaxGain());
                        algorithmRuleEntity.setMaxLoss(accountPoolPO.getMaxLoss());
                        algorithmRuleEntity.setMaxGainRate(accountPoolPO.getMaxGainRate());
                        algorithmRuleEntity.setMaxLossRate(accountPoolPO.getMaxLossRate());
                        algorithmRuleEntity.setGainCronRule(accountPoolPO.getGainCronRule());
                        algorithmRuleEntity.setLossCronRule(accountPoolPO.getLossCronRule());
                        algorithmRuleEntity.setUserIdlist(ruleInputModelDTO.getUserIdlist());
                        algorithmRuleEntity.setRuleOutputModelDTO(dto);
                        LogUtil.info(String.format("规则入参： AlgorithmRuleEntity=[ %s ] ",algorithmRuleEntity));
                        algorithmRuleExcute.execute(algorithmRuleEntity);
                        context.setOutputModel(dto);
                    } finally {
                        if(lock.isHeldByCurrentThread()){
                            lock.unlock();
                        }
                    }
                }
            }
        });
    }

    public void controlRtpCurrentCheat(AlgorithmRuleEntity algorithmRuleEntity,AccountPoolPO accountPoolPO){
        accountPoolPO.setRtpCurrentCheat(algorithmRuleEntity.getRtpCurrentCheat().compareTo(accountPoolPO.getPositiveRate())>0?accountPoolPO.getPositiveRate():algorithmRuleEntity.getRtpCurrentCheat());
        accountPoolManageRepository.save(accountPoolPO);
    }

}
