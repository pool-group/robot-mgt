package com.zren.platform.biz.shared.core.robot;

import com.google.common.collect.Lists;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.core.accountPool.AccountPoolManageService;
import com.zren.platform.biz.shared.enums.BizRobotInfoEnum;
import com.zren.platform.biz.shared.factory.RobotFactory;
import com.zren.platform.biz.shared.jpa.page.BasePageable;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.biz.shared.template.transaction.RobotTransactionCallback;
import com.zren.platform.biz.shared.template.transaction.RtTransactionTemplate;
import com.zren.platform.common.dal.po.AccountPoolPO;
import com.zren.platform.common.dal.po.RobotInfoPO;
import com.zren.platform.common.dal.repository.AccountRegainSnapRepository;
import com.zren.platform.common.dal.repository.AccountWaterManageRepository;
import com.zren.platform.common.dal.repository.RobotManageRepository;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotDestroyInputModelDTO;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotInitInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.robotInfo.RobotInfoDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.core.rule.entity.in.CapitalRuleEntity;
import com.zren.platform.core.rule.execute.DistributeCapitalRuleExcute;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 机器人管理实现类
 *
 * @author k.y
 * @version Id: RobotManageServiceImpl.java, v 0.1 2018年11月05日 下午14:04 k.y Exp $
 */
@Component
public class RobotManageServiceImpl {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    /**机器人管理*/
    @Autowired
    private RobotManageRepository robotManageRepository;

    /**资金池服务*/
    @Autowired
    private AccountPoolManageService accountPoolManageServiceImpl;

    /**初始化分配资金规则*/
    @Autowired
    private DistributeCapitalRuleExcute distributeCapitalRule;

    /**Redis Client*/
    @Autowired
    private RedissonClient redisClient;

    /**事务模板*/
    @Autowired
    private RtTransactionTemplate rtTransactionTemplate;

    /**机器人账目流水*/
    @Autowired
    private AccountWaterManageRepository accountWaterManageRepository;

    /**异常账变恢复*/
    @Autowired
    private AccountRegainSnapRepository accountRegainSnapRepository;

    @Autowired
    private RobotFactory robotFactory;

    /**
     * 获取机器人
     *
     * @param dto  游戏ID  最小金额  机器人数量
     * @return
     */
    public RobotBaseResult receiveRobotInfo(RobotInitInputModelDTO dto) {

        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<RobotInitInputModelDTO,List<RobotInfoDTO>>(){

            @Override
            public void preCheck() {}


            @Override
            public void initContent(EngineContext<RobotInitInputModelDTO, List<RobotInfoDTO>> context) {
                context.setInputModel(dto);
            }

            @Override
            public void doProcess(EngineContext<RobotInitInputModelDTO, List<RobotInfoDTO>> context) throws InterruptedException {

                Long startTime=System.currentTimeMillis();
                RLock lock=redisClient.getLock(RedisCommon.getAccountLock(dto.getBrand(),dto.getGameId(),dto.getRoomId()));
                lock.lock(10, TimeUnit.SECONDS);
                boolean isLocked = lock.isLocked();
                LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                CapitalRuleEntity capitalRuleEntity=new CapitalRuleEntity();
                if(isLocked){
                    try {
                        List<RobotInfoPO> robotInfoPOList= Lists.newArrayList();
                        List<RobotInfoDTO> lst=new ArrayList<>();
                        //生成机器人同一批次ID
                        Long batchId = DataUtil.createSequenceUid();
                        int inx=0;
                        while (tryAgin(robotInfoPOList,dto,batchId)){
                            inx++;
                            LogUtil.warn(String.format("拉取机器人出现并发，重试处理中.. 重试次数 inx=[ %s ]",inx));
                        }

                        //获取大账户信息
                        AccountPoolPO accountPoolPO=accountPoolManageServiceImpl.getUsableCapital(dto.getGameId(),dto.getBrand(),dto.getRoomId());
                        LogUtil.info(String.format("查询账户信息：accountPoolPO=[ %s ]",accountPoolPO));
                        //最小资金
                        capitalRuleEntity.setMinAmount(dto.getMinAmount());
                        //机器人账户信息列表
                        capitalRuleEntity.setRobotList(robotInfoPOList);
                        //最小倍数
                        capitalRuleEntity.setMinMultiple(accountPoolPO.getMinMultiple());
                        //最大倍数
                        capitalRuleEntity.setMaxMultiple(accountPoolPO.getMaxMultiple());
                        //头像编号区间
                        capitalRuleEntity.setHeadRandom(accountPoolPO.getHeadRandom());
                        //可用资金
                        capitalRuleEntity.setUsableCapital(accountPoolPO.getCapital().add(accountPoolPO.getGainLoss()));
                        LogUtil.info(String.format("brand=[ %s ] ,gameId=[ %s ] ,roomId=[ %s ] ,账户可用资金=[ %s ]",dto.getBrand(),dto.getGameId(),dto.getRoomId(),accountPoolPO.getCapital().add(accountPoolPO.getGainLoss())));
                        //初始化机器人本金规则
                        distributeCapitalRule.execute(capitalRuleEntity);

                        List<RobotInfoPO> robotlist=capitalRuleEntity.getRobotList();

                        //冻结资金
                        /**
                         * 机器人改造说明：
                         *
                         * 暂时废除冻结资金策略，通过管端盈亏额来控制
                         */
                        //记录账户明细
                        robotlist.forEach(s->{
                            //PO转换为DTO
                            RobotInfoDTO robotInfoDTO=new RobotInfoDTO();
                            robotInfoDTO.setId(s.getId());
                            robotInfoDTO.setUserId(s.getUserId());
                            robotInfoDTO.setBatchId(batchId);
                            robotInfoDTO.setUserName(s.getUserName());
                            robotInfoDTO.setBalance(s.getBalance());
                            robotInfoDTO.setStatus(s.getStatus());
                            robotInfoDTO.setBrand(accountPoolPO.getBrand());
                            robotInfoDTO.setHeadUrl(s.getHeadUrl());
                            lst.add(robotInfoDTO);
                        });
                        context.setOutputModel(lst);
                    }catch (Exception e){
                        throw e;
                    } finally {
                        if(lock.isHeldByCurrentThread()){
                            lock.unlock();
                        }
                    }
                }
            }

            @Override
            public void afterProcess(EngineContext<RobotInitInputModelDTO, List<RobotInfoDTO>> context) {}

        });
    }

    private Boolean tryAgin(List<RobotInfoPO> robotInfoPOList,RobotInitInputModelDTO dto,Long batchId){
        RobotInfoPO po=new RobotInfoPO();
        po.setStatus(BizRobotInfoEnum.FREE_STATUS.getCode());
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new BasePageable(dto.getCount(), sort);
        Example<RobotInfoPO> example=Example.of(po,matcher);

        //获取机器人基本信息
        Page<RobotInfoPO> pages=robotManageRepository.findAll(example,pageable);
        LogUtil.info(String.format("查询机器人信息：robotInfoList=[ %s ]",pages.getContent()));

        robotInfoPOList.addAll(pages.getContent());
        if(null==pages||null==pages.getContent()){
            throw new RobotSystemException(ErrorCodeEnum.ROBOT_SELECT_SYS);
        }
        if(robotInfoPOList.size()==0||robotInfoPOList.size()<dto.getCount()){
            LogUtil.warn("当前没有充足的机器人,系统即将生成机器人");
            try {
                robotInfoPOList=robotFactory.create(dto.getCount());
                robotManageRepository.saveAll(robotInfoPOList);
            } catch (Exception e) {
                throw new RobotSystemException(ErrorCodeEnum.FAIL_SYS_CREATE_ROBOT);
            }
            LogUtil.info("系统创建机器人完成!");
        }
        //提取机器人ID
        List<Long> userIdlist=robotInfoPOList.stream().mapToLong(RobotInfoPO::getUserId).boxed().collect(Collectors.toList());

        boolean bool=false;
        //冻结机器人
        int ct=robotManageRepository.vfreezeRobots(BizRobotInfoEnum.BUSY_STATUS.getCode(),new Date(),batchId,userIdlist,"0");
        if(ct!=dto.getCount()){
            robotManageRepository.vfreezeRobots(BizRobotInfoEnum.FREE_STATUS.getCode(),new Date(),0L,userIdlist,"1");
            bool=true;
        }
        return bool;
    }

    /**
     * 回收机器人
     *
     * @param robotDestroyInputModelList 机器人ID  当前余额
     * @return
     */
    public RobotBaseResult freezeRobot(List<RobotDestroyInputModelDTO> robotDestroyInputModelList) {

        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<List<RobotDestroyInputModelDTO>,Null>(){

            @Override
            public void initContent(EngineContext<List<RobotDestroyInputModelDTO>,Null> context) {
                context.setInputModel(robotDestroyInputModelList);
            }

            @Override
            public void doProcess(EngineContext<List<RobotDestroyInputModelDTO>, Null> context) throws InterruptedException {

                    Long startTime=System.currentTimeMillis();
                    RLock lock=redisClient.getLock(RedisCommon.getAccountLock(robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getRoomId()));
                    lock.lock(10, TimeUnit.SECONDS);
                    boolean isLocked = lock.isLocked();
                    LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                    if(isLocked){
                        try {

                        //事务控制
                        rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                            @Override
                            public void execute() {

//                                robotDestroyInputModelList.forEach(s->{
//
//                                    List<AccountWaterPO> accountWaterLst=accountWaterManageRepository.findByBatchIdAndUserId(robotDestroyInputModelList.get(0).getBatchId(),s.getUserId());
//                                    if(accountWaterLst.size()==0){
//                                        throw new RobotBizException(ErrorCodeEnum.INVALID_BATCH);
//                                    }
//                                    //归还资金汇总
//                                    BigDecimal balance=s.getBalance();
//
//                                    //当前机器人账变本金
//                                    BigDecimal initialBalance=accountWaterLst.stream().map(AccountWaterPO::getBalance).reduce(BigDecimal.ZERO,BigDecimal::add);
//
//                                    //盈亏额汇总
//                                    BigDecimal gainLossSum=balance.subtract(initialBalance);
//
//                                    //机器人归还[解冻]资金,并变更盈亏
//                                    /**
//                                     * 机器人改造说明：
//                                     *     暂时废除冻结资金策略，通过管端盈亏额来控制
//                                     *
//                                     *     freezeCapital=: BigDecimal.ZERO
//                                     *     freezeCapital=: BigDecimal.ZERO
//                                     */
//                                    accountPoolManageServiceImpl.updateUnfreezeAndGainLoss(BigDecimal.ZERO, BigDecimal.ZERO,robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getRoomId());
//
//                                    //记录账户明细
//                                    AccountWaterPO accountWaterPO=new AccountWaterPO();
//                                    accountWaterPO.setUserId(s.getUserId());
//                                    accountWaterPO.setBatchId(s.getBatchId());
//                                    //归还本金
//                                    accountWaterPO.setBalance(balance);
//                                    accountWaterPO.setWaterType(Byte.valueOf(BizRobotInfoEnum.ACCOUNT_WATER_IN.getCode()));
//                                    accountWaterPO.setGameId(s.getGameId());
//                                    accountWaterPO.setBrand(s.getBrand());
//                                    accountWaterPO.setRoomId(s.getRoomId());
//                                    accountWaterPO.setCreateTime(new Date());
//                                    accountWaterManageRepository.saveAndFlush(accountWaterPO);
//
//                                    AccountWaterPO gainLossPO=new AccountWaterPO();
//                                    gainLossPO.setUserId(s.getUserId());
//                                    gainLossPO.setBatchId(s.getBatchId());
//                                    //盈亏金额
//                                    gainLossPO.setBalance(gainLossSum);
//                                    gainLossPO.setWaterType(Byte.valueOf(BizRobotInfoEnum.ACCOUNT_WATER_GAIN_LOSS_IN.getCode()));
//                                    gainLossPO.setGameId(s.getGameId());
//                                    gainLossPO.setBrand(s.getBrand());
//                                    gainLossPO.setRoomId(s.getRoomId());
//                                    gainLossPO.setCreateTime(new Date());
//                                    accountWaterManageRepository.saveAndFlush(gainLossPO);
//                                });

                                //提取机器人ID
                                List<Long> userIdlist=robotDestroyInputModelList.stream().mapToLong(RobotDestroyInputModelDTO::getUserId).boxed().collect(Collectors.toList());

                                //解锁机器人
                                LogUtil.info(String.format(" 解锁机器人请求参数：userIdlist=[ %s ]",userIdlist));
                                robotManageRepository.freezeRobots(BizRobotInfoEnum.FREE_STATUS.getCode(),new Date(),0L,userIdlist);
                            }
                        });
                        }catch (Exception e){
                            throw e;
                        }finally {
                            if(lock.isHeldByCurrentThread()){
                                lock.unlock();
                            }
                    }
                }
            }

            @Override
            public void afterProcess(EngineContext<List<RobotDestroyInputModelDTO>,Null> context) {}
        });
    }


    /**
     * 回收机器人
     *
     * @param robotDestroyInputModelList 机器人ID  当前余额
     * @return
     */
    public RobotBaseResult destroyRobot(List<RobotDestroyInputModelDTO> robotDestroyInputModelList) {

        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<List<RobotDestroyInputModelDTO>,Null>(){

            @Override
            public void initContent(EngineContext<List<RobotDestroyInputModelDTO>,Null> context) {
                context.setInputModel(robotDestroyInputModelList);
            }

            @Override
            public void preCheck() {
                if(null==robotDestroyInputModelList||robotDestroyInputModelList.size()==0){
                    throw new RobotBizException(ErrorCodeEnum.INPUT_PARAMETER_NOT_EMPTY,String.format("请求参数不能为空或空数组, robotDestroyInputModelList=[ %s ]",robotDestroyInputModelList));
                }
            }

            @Override
            public void doProcess(EngineContext<List<RobotDestroyInputModelDTO>, Null> context) throws InterruptedException {

                Long startTime=System.currentTimeMillis();
                RLock lock=redisClient.getLock(RedisCommon.getAccountLock(robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getRoomId()));
                lock.lock(10, TimeUnit.SECONDS);
                boolean isLocked = lock.isLocked();
                LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                if(isLocked){
                    try {

                        //事务控制
                        rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                            @Override
                            public void execute() {

//                                robotDestroyInputModelList.forEach(s->{
//
//                                    List<AccountWaterPO> accountWaterLst=accountWaterManageRepository.findByBatchIdAndUserId(robotDestroyInputModelList.get(0).getBatchId(),s.getUserId());
//                                    if(accountWaterLst.size()==0){
//                                        throw new RobotBizException(ErrorCodeEnum.INVALID_BATCH);
//                                    }
//                                    //归还资金汇总
//                                    BigDecimal balance=s.getBalance();
//
//                                    //当前机器人账变本金
//                                    BigDecimal initialBalance=accountWaterLst.stream().map(AccountWaterPO::getBalance).reduce(BigDecimal.ZERO,BigDecimal::add);
//
//                                    //盈亏额汇总
//                                    BigDecimal gainLossSum=balance.subtract(initialBalance);
//
//                                    //机器人归还[解冻]资金,并变更盈亏
//                                    /**
//                                     * 机器人改造说明：
//                                     *     暂时废除冻结资金策略，通过管端盈亏额来控制
//                                     *
//                                     *     freezeCapital=: BigDecimal.ZERO
//                                     */
//                                    accountPoolManageServiceImpl.updateUnfreezeAndGainLoss(BigDecimal.ZERO,BigDecimal.ZERO,robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getRoomId());
//
//                                    //记录账户明细
//                                    AccountWaterPO gainLossPO=new AccountWaterPO();
//                                    gainLossPO.setUserId(s.getUserId());
//                                    gainLossPO.setBatchId(s.getBatchId());
//                                    //盈亏金额
//                                    gainLossPO.setBalance(initialBalance);
//                                    gainLossPO.setWaterType(Byte.valueOf(BizRobotInfoEnum.ACCOUNT_WATER_IN.getCode()));
//                                    gainLossPO.setGameId(s.getGameId());
//                                    gainLossPO.setBrand(s.getBrand());
//                                    gainLossPO.setRoomId(s.getRoomId());
//                                    gainLossPO.setCreateTime(new Date());
//                                    accountWaterManageRepository.saveAndFlush(gainLossPO);
//                                });

                                //提取机器人ID
                                List<Long> userIdlist=robotDestroyInputModelList.stream().mapToLong(RobotDestroyInputModelDTO::getUserId).boxed().collect(Collectors.toList());

                                //解锁机器人
                                robotManageRepository.freezeRobots(BizRobotInfoEnum.FREE_STATUS.getCode(),new Date(),0L,userIdlist);
                            }
                        });
                    }catch (Exception e){
                        throw e;
                    }finally {
                        if(lock.isHeldByCurrentThread()){
                            lock.unlock();
                        }
                    }
                }
            }

            @Override
            public void afterProcess(EngineContext<List<RobotDestroyInputModelDTO>,Null> context) {}
        });
    }


    /**
     * 更新机器人账变
     *
     * @param robotDestroyInputModelList
     * @return
     */
    public RobotBaseResult updateRobotAccount(List<RobotDestroyInputModelDTO> robotDestroyInputModelList){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<List<RobotDestroyInputModelDTO>,Null>(){

            @Override
            public void initContent(EngineContext<List<RobotDestroyInputModelDTO>,Null> context) {
                context.setInputModel(robotDestroyInputModelList);
            }

            @Override
            public void doProcess(EngineContext<List<RobotDestroyInputModelDTO>, Null> context) throws InterruptedException {

                Long startTime=System.currentTimeMillis();
                RLock lock=redisClient.getLock(RedisCommon.getAccountLock(robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getRoomId()));
                lock.lock(10, TimeUnit.SECONDS);
                boolean isLocked = lock.isLocked();
                LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                if(isLocked){
                    try {

                        //事务控制
                        rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                            @Override
                            public void execute() {
//                                robotDestroyInputModelList.forEach(s->{
//                                    List<AccountWaterPO> accountWaterLst=accountWaterManageRepository.findByBatchIdAndUserId(robotDestroyInputModelList.get(0).getBatchId(),s.getUserId());
//                                    if(accountWaterLst.size()==0){
//                                        throw new RobotBizException(ErrorCodeEnum.INVALID_BATCH);
//                                    }
//                                    //归还资金汇总
//                                    BigDecimal balance=s.getBalance();
//
//                                    //当前机器人账变本金
//                                    BigDecimal initialBalance=accountWaterLst.stream().map(AccountWaterPO::getBalance).reduce(BigDecimal.ZERO,BigDecimal::add);
//
//                                    //盈亏额汇总
//                                    BigDecimal gainLossSum=balance.subtract(initialBalance);
//
//                                    //变更盈亏
//                                    accountPoolManageServiceImpl.updateGainLoss(BigDecimal.ZERO,robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getRoomId());
//
//                                    //记录账户明细
//                                    AccountWaterPO accountWaterPO=new AccountWaterPO();
//                                    accountWaterPO.setUserId(s.getUserId());
//                                    accountWaterPO.setBatchId(s.getBatchId());
//                                    //盈亏金额
//                                    accountWaterPO.setBalance(gainLossSum);
//                                    accountWaterPO.setWaterType(Byte.valueOf(BizRobotInfoEnum.ACCOUNT_WATER_GAIN_LOSS_IN.getCode()));
//                                    accountWaterPO.setGameId(s.getGameId());
//                                    accountWaterPO.setBrand(s.getBrand());
//                                    accountWaterPO.setRoomId(s.getRoomId());
//                                    accountWaterPO.setCreateTime(new Date());
//                                    accountWaterManageRepository.save(accountWaterPO);
//                                });
                            }
                        });
                    } finally {
                        if(lock.isHeldByCurrentThread()){
                            lock.unlock();
                        }
                    }
                }
            }

            @Override
            public void afterProcess(EngineContext<List<RobotDestroyInputModelDTO>,Null> context) {}
        });
    }

}
