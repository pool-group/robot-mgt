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
import com.zren.platform.common.dal.po.GameRtpStatsPO;
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
import com.zren.platform.common.util.tool.*;
import com.zren.platform.core.rule.entity.in.CapitalRuleEntity;
import com.zren.platform.core.rule.execute.DistributeCapitalRuleExcute;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class RobotManageServiceImpl {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private RobotManageRepository robotManageRepository;

    @Autowired
    private AccountPoolManageService accountPoolManageServiceImpl;

    @Autowired
    private DistributeCapitalRuleExcute distributeCapitalRule;

    @Autowired
    private RedissonClient redisClient;

    @Autowired
    private RtTransactionTemplate rtTransactionTemplate;

    @Autowired
    private AccountWaterManageRepository accountWaterManageRepository;

    @Autowired
    private AccountRegainSnapRepository accountRegainSnapRepository;

    @Autowired
    private RobotFactory robotFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public RobotBaseResult receiveRobotInfo(RobotInitInputModelDTO dto) {

        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<RobotInitInputModelDTO,List<RobotInfoDTO>>(){

            @Override
            public void preCheck() {}


            @Override
            public void initContent(EngineContext<RobotInitInputModelDTO, List<RobotInfoDTO>> context) {
                context.setInputModel(dto);
            }

            @Override
            public void doProcess(EngineContext<RobotInitInputModelDTO, List<RobotInfoDTO>> context) {

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
                        AtomicInteger inx=new AtomicInteger(0);
                        int index=0;
                        while (true){
                            if(!tryAgin(inx,robotInfoPOList,dto,batchId)){
                                break;
                            }
                            index++;
                            LogUtil.warn(String.format("" +
                                    "，机器人并发重试处理中.. 重试次数 inx=[ %s ]",index));
                            if (index>100){
                                throw new RobotSystemException(String.format("申请机器人并发处理异常, 重试次数已超过[ % ]",index));
                            }
                        }

                        AccountPoolPO accountPoolPO=accountPoolManageServiceImpl.getUsableCapital(dto.getGameId(),dto.getBrand(),dto.getRoomId());
                        LogUtil.info(String.format("查询账户信息：accountPoolPO=[ %s ]",accountPoolPO));
                        capitalRuleEntity.setMinAmount(dto.getMinAmount());
                        capitalRuleEntity.setRobotList(robotInfoPOList);
                        capitalRuleEntity.setMinMultiple(accountPoolPO.getMinMultiple());
                        capitalRuleEntity.setMaxMultiple(accountPoolPO.getMaxMultiple());
                        capitalRuleEntity.setHeadRandom(accountPoolPO.getHeadRandom());
                        capitalRuleEntity.setUsableCapital(accountPoolPO.getCapital().add(accountPoolPO.getGainLoss()));
                        LogUtil.info(String.format("brand=[ %s ] ,gameId=[ %s ] ,roomId=[ %s ] ,账户可用资金=[ %s ]",dto.getBrand(),dto.getGameId(),dto.getRoomId(),accountPoolPO.getCapital().add(accountPoolPO.getGainLoss())));
                        distributeCapitalRule.execute(capitalRuleEntity);

                        List<RobotInfoPO> robotlist=capitalRuleEntity.getRobotList();
                        //记录账户明细
                        robotlist.forEach(s->{
                            //PO转换为DTO
                            RobotInfoDTO robotInfoDTO=new RobotInfoDTO();
                            robotInfoDTO.setId(s.getId());
                            robotInfoDTO.setUserId(s.getUserId());
                            robotInfoDTO.setBatchId(batchId);
                            if(ProbabilisticUtil.excute(BigDecimal.valueOf(0.4))==1){
                                robotInfoDTO.setFeatures((byte) 2);
                            }
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

    private Boolean tryAgin(AtomicInteger inx,List<RobotInfoPO> robotInfoPOList,RobotInitInputModelDTO dto,Long batchId){
        robotInfoPOList.clear();
        int count=dto.getCount();
        Query query=entityManager.createNativeQuery("select * from robot_info r where r.status=0 limit "+inx.get()+", "+count+"",RobotInfoPO.class);
        List<RobotInfoPO> poList=query.getResultList();
        LogUtil.info(String.format("查询机器人信息：robotInfoList=[ %s ]",poList));

        if(null==poList){
            throw new RobotSystemException(ErrorCodeEnum.ROBOT_SELECT_SYS);
        }
        if(poList.size()==0||poList.size()<dto.getCount()){
            LogUtil.warn("当前没有充足的机器人,系统即将生成机器人");
            try {
                robotInfoPOList.addAll(robotFactory.create(dto.getCount()));
                robotManageRepository.saveAll(robotInfoPOList);
            } catch (Exception e) {
                throw new RobotSystemException(ErrorCodeEnum.FAIL_SYS_CREATE_ROBOT);
            }
            LogUtil.info("系统创建机器人完成!");
            return false;
        }else {
            robotInfoPOList.addAll(poList);
            //提取机器人ID
            List<Long> userIdlist=robotInfoPOList.stream().mapToLong(RobotInfoPO::getUserId).boxed().collect(Collectors.toList());

            //冻结机器人
            int ct=robotManageRepository.vfreezeRobots(BizRobotInfoEnum.BUSY_STATUS.getCode(),new Date(),batchId,userIdlist,"0",0L);
            robotManageRepository.flush();
            LogUtil.info(String.format("dto.getCount(): [ %s ], ct=[ %s ], userIdlist=[ %s ]",dto.getCount(),ct,userIdlist));
            if(ct!=dto.getCount()){
                robotManageRepository.vfreezeRobots(BizRobotInfoEnum.FREE_STATUS.getCode(),new Date(),0L,userIdlist,"1",batchId);
                robotManageRepository.flush();
                inx.addAndGet(count);
                return true;
            }
            return false;
        }
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
            public void doProcess(EngineContext<List<RobotDestroyInputModelDTO>, Null> context) {

                    Long startTime=System.currentTimeMillis();
                    RLock lock=redisClient.getLock(RedisCommon.getAccountLock(robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getRoomId()));
                    lock.lock(10, TimeUnit.SECONDS);
                    boolean isLocked = lock.isLocked();
                    LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                    if(isLocked){
                        try {
                            rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                            @Override
                            public void execute() {
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
            public void doProcess(EngineContext<List<RobotDestroyInputModelDTO>, Null> context) {

                Long startTime=System.currentTimeMillis();
                RLock lock=redisClient.getLock(RedisCommon.getAccountLock(robotDestroyInputModelList.get(0).getBrand(),robotDestroyInputModelList.get(0).getGameId(),robotDestroyInputModelList.get(0).getRoomId()));
                lock.lock(10, TimeUnit.SECONDS);
                boolean isLocked = lock.isLocked();
                LogUtil.info(String.format(" 是否获取锁：isTrue=[ %s ], 耗时：time= [ %s ]ms",isLocked,System.currentTimeMillis()-startTime));
                if(isLocked){
                    try {
                        rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                            @Override
                            public void execute() {
                                List<Long> userIdlist=robotDestroyInputModelList.stream().mapToLong(RobotDestroyInputModelDTO::getUserId).boxed().collect(Collectors.toList());
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

    public RobotBaseResult updateRobotAccount(List<RobotDestroyInputModelDTO> robotDestroyInputModelList){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<List<RobotDestroyInputModelDTO>,Null>(){

            @Override
            public void initContent(EngineContext<List<RobotDestroyInputModelDTO>,Null> context) {
                context.setInputModel(robotDestroyInputModelList);
            }

            @Override
            public void doProcess(EngineContext<List<RobotDestroyInputModelDTO>, Null> context) {

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
                            public void execute() {}
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
