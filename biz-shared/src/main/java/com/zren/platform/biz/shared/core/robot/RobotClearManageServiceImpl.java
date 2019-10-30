package com.zren.platform.biz.shared.core.robot;

import com.google.common.collect.Lists;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.core.accountPool.AccountPoolManageService;
import com.zren.platform.biz.shared.enums.BizRobotInfoEnum;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.biz.shared.template.transaction.RobotTransactionCallback;
import com.zren.platform.biz.shared.template.transaction.RtTransactionTemplate;
import com.zren.platform.common.dal.po.AccountRegainSnapInfoPO;
import com.zren.platform.common.dal.po.AccountWaterPO;
import com.zren.platform.common.dal.po.RobotInfoPO;
import com.zren.platform.common.dal.repository.AccountRegainSnapRepository;
import com.zren.platform.common.dal.repository.AccountWaterManageRepository;
import com.zren.platform.common.dal.repository.RobotClearSnapRepository;
import com.zren.platform.common.dal.repository.RobotManageRepository;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.tool.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 清理机器人僵尸账号
 *
 * @author k.y
 * @version Id: RobotClearManageServiceImpl.java, v 0.1 2018年12月27日 下午14:30 k.y Exp $
 */
@Component
public class RobotClearManageServiceImpl {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private RobotManageRepository robotManageRepository;

    @Autowired
    private RobotClearSnapRepository robotClearSnapRepository;

    /**事务模板*/
    @Autowired
    private RtTransactionTemplate rtTransactionTemplate;

    /**机器人账目流水*/
    @Autowired
    private AccountWaterManageRepository accountWaterManageRepository;

    /**资金池服务*/
    @Autowired
    private AccountPoolManageService accountPoolManageServiceImpl;

    /**异常账变恢复*/
    @Autowired
    private AccountRegainSnapRepository accountRegainSnapRepository;

    /**
     * 恢复僵尸机器人任务
     *
     * @param dateTime
     * @return
     */
    public RobotBaseResult clearSnap(Date dateTime){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<String,Void>(){

            @Override
            public void initContent(EngineContext<String, Void> context) {
                context.setInputModel(String.format("当前时间：[ %s ]",dateTime));
            }

            @Override
            public void doProcess(EngineContext<String, Void> context) throws InterruptedException {

                //事务链
                rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                        @Override
                        public void execute() {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(dateTime);
                            calendar.add(Calendar.HOUR, -1);
                            Date date = calendar.getTime();
                            LogUtil.info(String.format("实际时间参数 [减去1小时]: [ %s ]", date));
                            List<RobotInfoPO> lst = robotManageRepository.findByStatusAndDateTimeLessThan(BizRobotInfoEnum.BUSY_STATUS.getCode(), date);

                            LogUtil.info(String.format("僵尸机器人查询结果：[ %s ]",lst));

                            List<Long> userIdlist = Lists.newArrayList();
                            for (RobotInfoPO po : lst) {
                                if (po.getBatchId().compareTo(0L) == 0) {
                                    continue;
                                }
//                                List<AccountWaterPO> accountWaterLst = accountWaterManageRepository.findByBatchIdAndUserId(po.getBatchId(), po.getUserId());
//                                if (accountWaterLst.size() == 0) {
//                                    throw new RobotBizException(ErrorCodeEnum.INVALID_BATCH);
//                                }

                                userIdlist.add(po.getUserId());

                                //当前机器人账变本金
//                                BigDecimal initialBalance = accountWaterLst.stream().map(AccountWaterPO::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
//                                RobotClearSnapInfoPO snapInfoPO = new RobotClearSnapInfoPO();
//                                snapInfoPO.setUserId(po.getUserId());
//                                snapInfoPO.setBatchId(po.getBatchId());
//                                snapInfoPO.setBalance(initialBalance);
//                                snapInfoPO.setBrand(accountWaterLst.get(0).getBrand());
//                                snapInfoPO.setGameId(accountWaterLst.get(0).getGameId());
//                                snapInfoPO.setRoomId(accountWaterLst.get(0).getRoomId());
//                                snapInfoPO.setDeadTime(po.getDateTime());
//                                snapInfoPO.setCreateTime(new Date());
//                                robotClearSnapRepository.saveAndFlush(snapInfoPO);
//
//                                //解冻账户资金
//                                accountPoolManageServiceImpl.updateUnfreeze(BigDecimal.ZERO,accountWaterLst.get(0).getGameId(),accountWaterLst.get(0).getBrand(),accountWaterLst.get(0).getRoomId());
                            }
                            if (userIdlist.size() != 0) {
                                //解锁机器人
                                robotManageRepository.freezeRobots(BizRobotInfoEnum.FREE_STATUS.getCode(), new Date(), 0L, userIdlist);
                            }
                        }
                    });
            }

            @Override
            public void afterProcess(EngineContext<String, Void> context) {
                LogUtil.info(String.format("任务执行成功, 当前时间：[ %s ]",new Date()));
            }
        });
    }


    /**
     * 恢复异常账变资金任务
     *
     * @return
     */
    public RobotBaseResult regainAccount(){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<String,Void>(){

            @Override
            public void doProcess(EngineContext<String, Void> context) throws InterruptedException {

                //事务链
                rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                    @Override
                    public void execute() {

                        List<AccountRegainSnapInfoPO> lst = accountRegainSnapRepository.findByStatus(Byte.valueOf(BizRobotInfoEnum.ACCOUNT_WATER_OUT.getCode()));

                        LogUtil.info(String.format("机器人异常账变查询结果：[ %s ]",lst));

                        List<Long> userIdlist = Lists.newArrayList();
                        for (AccountRegainSnapInfoPO po : lst) {
                            List<AccountWaterPO> accountWaterLst = accountWaterManageRepository.findByBatchIdAndUserId(po.getBatchId(), po.getUserId());
                            if (accountWaterLst.size() == 0) {
                                throw new RobotBizException(ErrorCodeEnum.INVALID_BATCH);
                            }

                            userIdlist.add(po.getUserId());

                            //解冻账户资金, 更新账变盈亏额
                            /**
                             * 机器人改造说明：
                             *     暂时废除冻结资金策略，通过管端盈亏额来控制
                             *
                             *     freezeCapital=: new BigDecimal(0)
                             */
                            accountPoolManageServiceImpl.updateUnfreezeAndGainLoss(BigDecimal.ZERO,BigDecimal.ZERO,po.getGameId(),po.getBrand(),po.getRoomId());

                            //记录账户明细
                            AccountWaterPO accountWaterPO=new AccountWaterPO();
                            accountWaterPO.setUserId(po.getUserId());
                            accountWaterPO.setBatchId(po.getBatchId());
                            //盈亏金额
                            accountWaterPO.setBalance(po.getGainLoss());
                            accountWaterPO.setWaterType(Byte.valueOf(BizRobotInfoEnum.ACCOUNT_WATER_GAIN_LOSS_IN.getCode()));
                            accountWaterPO.setGameId(po.getGameId());
                            accountWaterPO.setBrand(po.getBrand());
                            accountWaterPO.setRoomId(po.getRoomId());
                            accountWaterPO.setCreateTime(new Date());
                            accountWaterManageRepository.saveAndFlush(accountWaterPO);

                            //重置数据消费状态
                            accountRegainSnapRepository.freezeAccountRegain(BizRobotInfoEnum.IS_SUCCESS.getCode(), new Date(), po.getBatchId(), userIdlist);
                        }
                    }
                });
            }

            @Override
            public void afterProcess(EngineContext<String, Void> context) {
                LogUtil.info(String.format("任务执行成功, 当前时间：[ %s ]",new Date()));
            }
        });
    }
}
