package com.zren.platform.biz.shared.core.robot;

import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.core.accountPool.AccountPoolManageService;
import com.zren.platform.biz.shared.core.accountPool.impl.GameRtpStatsServiceImpl;
import com.zren.platform.biz.shared.enums.RoomEnum;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.common.dal.po.AccountPoolPO;
import com.zren.platform.common.dal.po.GameRtpStatsPO;
import com.zren.platform.common.dal.repository.AccountPoolManageRepository;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.rule.RuleOutputModelDTO;
import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.EnumsCommon;
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


/**
 * 规则策略接口实现
 *
 * @author k.y
 * @version Id: ZJHRobotRuleManageServiceImpl.java, v 0.1 2018年11月21日 下午10:35 k.y Exp $
 */
@Component
public class AIRobotRuleManageServiceImpl {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    /**资金池服务*/
    @Autowired
    private AccountPoolManageService accountPoolManageServiceImpl;

    @Autowired
    private AccountPoolManageRepository accountPoolManageRepository;

    /**规则入口*/
    @Autowired
    private AlgorithmRuleExcute algorithmRuleExcute;

    /**RTP*/
    @Autowired
    private GameRtpStatsServiceImpl gameRtpStatsServiceImpl;

    /**Redis Client*/
    @Autowired
    private RedissonClient redisClient;

    /**
     * 获取规则策略
     *
     * @param ruleInputModelDTO  平台  游戏类型  机器人ID
     * @return
     */
    public RobotBaseResult createRule(RuleInputModelDTO ruleInputModelDTO){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<RuleInputModelDTO,RuleOutputModelDTO>(){

            @Override
            public void initContent(EngineContext<RuleInputModelDTO, RuleOutputModelDTO> context) {
                context.setInputModel(ruleInputModelDTO);
            }

            @Override
            public void doProcess(EngineContext<RuleInputModelDTO, RuleOutputModelDTO> context) throws InterruptedException {

                Long startTime=System.currentTimeMillis();
                RLock lock=redisClient.getLock(RedisCommon.getRobotRuleLock(ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId()));
                lock.lock(10, TimeUnit.SECONDS);
                boolean isLocked = lock.isLocked();
                LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                if(isLocked){
                    try {
                        //获取大账户信息
                        AccountPoolPO accountPoolPO=accountPoolManageServiceImpl.getUsableCapital(ruleInputModelDTO.getGameId(),ruleInputModelDTO.getBrand(),ruleInputModelDTO.getRoomId());
                        //获取某个业主/游戏/房间盈亏额
                        GameRtpStatsPO gameRtpStatsPO= gameRtpStatsServiceImpl.queryRtpResult(ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId());

                        LogUtil.info(Log.RTP.LOG,String.format("brand=%s, gameId=%s, roomId=%s,当日机器人盈亏额：%s ,总投入：%s",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),gameRtpStatsPO.getTotalPnl().negate(),gameRtpStatsPO.getTotalInput()));
                        LogUtil.info(Log.CRON.LOG,String.format("brand=%s, gameId=%s, roomId=%s,当日机器人盈亏额：%s ,总投入：%s",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),gameRtpStatsPO.getTotalPnl().negate(),gameRtpStatsPO.getTotalInput()));
                        RuleOutputModelDTO dto= new RuleOutputModelDTO();
                        //规则入参设置
                        AlgorithmRuleEntity algorithmRuleEntity=new AlgorithmRuleEntity();
                        algorithmRuleEntity.setBrand(accountPoolPO.getBrand());
                        algorithmRuleEntity.setGameId(accountPoolPO.getGameId());
                        algorithmRuleEntity.setRoomId(accountPoolPO.getRoomId());
                        algorithmRuleEntity.setCapital(accountPoolPO.getCapital());
                        algorithmRuleEntity.setRtpValue(accountPoolPO.getRtpValue());
                        algorithmRuleEntity.setRtpCheatInit(accountPoolPO.getRtpCheatInit());
                        algorithmRuleEntity.setRtpIncrease(accountPoolPO.getRtpIncrease());
                        algorithmRuleEntity.setRtpDecrease(accountPoolPO.getRtpDecrease());
                        algorithmRuleEntity.setGainLoss(gameRtpStatsPO.getTotalPnl().negate());
                        algorithmRuleEntity.setRtpAble(accountPoolPO.getRtpAble());
                        //盈亏额为正(盈利状态)并且RTP开启状态,才使用此规则
                        if(algorithmRuleEntity.getGainLoss().compareTo(BigDecimal.ZERO)>0&&accountPoolPO.getRtpAble().compareTo((byte) 1)==0){
                            LogUtil.info(Log.RTP.LOG,String.format("启用RTP规则.. brand=%s, gameId=%s, roomId=%s",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId()));
                            if(gameRtpStatsPO.getTotalInput().compareTo(BigDecimal.valueOf(0))==0){
                                accountPoolPO.setRtpCurrentCheat(accountPoolPO.getRtpCheatInit());
                                algorithmRuleEntity.setRealRtpValue(BigDecimal.ONE);
                                LogUtil.info(Log.RTP.LOG,String.format("当日房间总投入为0, 实际RTP=%s, 初始作弊几率=%s",algorithmRuleEntity.getRealRtpValue(),accountPoolPO.getRtpCurrentCheat()));
                            }else {
                                algorithmRuleEntity.setRealRtpValue(BigDecimal.ONE.subtract(algorithmRuleEntity.getGainLoss().divide(gameRtpStatsPO.getTotalInput(),4)));
                            }
                            if(algorithmRuleEntity.getRealRtpValue().compareTo(algorithmRuleEntity.getRtpValue())>=0){
                                algorithmRuleEntity.setRtpCurrentCheat(accountPoolPO.getRtpCurrentCheat().add(accountPoolPO.getRtpIncrease()));
                            }else {
                                //如果rtpDecrease设置为1,相当于直接把当前作弊几率rtpCurrentCheat梯度重置为0
                                BigDecimal rtpCurrentCheat=accountPoolPO.getRtpCurrentCheat().subtract(accountPoolPO.getRtpDecrease());
                                algorithmRuleEntity.setRtpCurrentCheat(rtpCurrentCheat.compareTo(BigDecimal.ZERO)==-1?BigDecimal.ZERO:rtpCurrentCheat);
                            }
                            //规则范围处理
                            controlRtpCurrentCheat(algorithmRuleEntity,accountPoolPO);

                            LogUtil.info(Log.RTP.LOG,String.format("brand=%s, gameId=%s, roomId=%s, 实际RTP=%s, 预设RTP=%s, 作弊几率R=%s",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId(),algorithmRuleEntity.getRealRtpValue(),algorithmRuleEntity.getRtpValue(),algorithmRuleEntity.getRtpCurrentCheat()));
                        }else if(algorithmRuleEntity.getGainLoss().compareTo(BigDecimal.ZERO)>0&&accountPoolPO.getRtpAble().compareTo((byte) 0)==0){
                            LogUtil.info(Log.RTP.LOG,String.format("brand=%s, gameId=%s, roomId=%s, RTP规则处于关闭状态",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId()));
                        }else {
                            LogUtil.info(Log.CRON.LOG,String.format("brand=%s, gameId=%s, roomId=%s, 启用cron rule规则",ruleInputModelDTO.getBrand(),ruleInputModelDTO.getGameId(),ruleInputModelDTO.getRoomId()));
                        }
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
                        //计算赢家规则，并且从牌库取多份牌(游戏类型不同，份数基数不同)
                        algorithmRuleExcute.execute(algorithmRuleEntity);

                        BigDecimal totalgain=gameRtpStatsPO.getTotalPnl().negate();
                        BigDecimal shouldgain=BigDecimal.ONE.subtract(algorithmRuleEntity.getRtpValue()).multiply(gameRtpStatsPO.getTotalInput());
                        int difference=totalgain.subtract(shouldgain).intValue();
                        if (EnumsCommon.ZJH.getCode().equals(accountPoolPO.getGameId())&&((RoomEnum.ONE.getCode()==accountPoolPO.getRoomType()&&difference>0.5)
                                ||(RoomEnum.TWO.getCode()==accountPoolPO.getRoomType()&&difference>100)
                                ||(RoomEnum.THREE.getCode()==accountPoolPO.getRoomType()&&difference>100)
                                ||(RoomEnum.FOUR.getCode()==accountPoolPO.getRoomType()&&difference>100))
                                ){
                            Boolean bool=ProbabilisticUtil.excute(BigDecimal.valueOf(0.3))==1?true:false;
                            if(bool){
                                dto.setRobotry(0);
                                List<ZjhStrategyInfoDTO> robotInfolst=dto.getStrategylist();
                                for (ZjhStrategyInfoDTO d:robotInfolst){
                                    d.setIsWin((byte) 0);
                                }
                            }
                            LogUtil.info(Log.RTP.LOG,String.format("======================== touch player win or loss : bool=[ %s ] , robotry=[ %s ], roomType=[ %s ], difference=[ %s ]",bool,dto.getRobotry(),accountPoolPO.getRoomType(),difference));
                        }
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

    /**
     * Controlling the upper limit of RTP change to prevent a large number of concurrent events in a
     * round of time leading to a large increase in RTP gradient threshold. The current probability of cheating is up to 45%.
     *
     * Control the minimum RTP value, avoid the system crazy to win money, less than that value,
     * the robot loses, increase the player's experience of winning money.
     */
    public void controlRtpCurrentCheat(AlgorithmRuleEntity algorithmRuleEntity,AccountPoolPO accountPoolPO){
        accountPoolPO.setRtpCurrentCheat(algorithmRuleEntity.getRtpCurrentCheat().compareTo(BigDecimal.valueOf(0.45))>=0?BigDecimal.valueOf(0.45):algorithmRuleEntity.getRtpCurrentCheat());
        accountPoolManageRepository.save(accountPoolPO);
    }

}
