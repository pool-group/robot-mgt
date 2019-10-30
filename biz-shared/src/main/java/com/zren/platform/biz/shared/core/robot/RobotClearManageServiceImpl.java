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

    @Autowired
    private RtTransactionTemplate rtTransactionTemplate;

    @Autowired
    private AccountWaterManageRepository accountWaterManageRepository;

    @Autowired
    private AccountPoolManageService accountPoolManageServiceImpl;

    @Autowired
    private AccountRegainSnapRepository accountRegainSnapRepository;

    public RobotBaseResult clearSnap(Date dateTime){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<String,Void>(){

            @Override
            public void initContent(EngineContext<String, Void> context) {
                context.setInputModel(String.format("当前时间：[ %s ]",dateTime));
            }

            @Override
            public void doProcess(EngineContext<String, Void> context) {
                rtTransactionTemplate.doTransaction(new RobotTransactionCallback(){
                        @Override
                        public void execute() {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(dateTime);
                            calendar.add(Calendar.HOUR, (int) -0.5);
                            Date date = calendar.getTime();
                            LogUtil.info(String.format("实际时间参数 [减去1小时]: [ %s ]", date));
                            List<RobotInfoPO> lst = robotManageRepository.findByStatusAndDateTimeLessThan(BizRobotInfoEnum.BUSY_STATUS.getCode(), date);
                            LogUtil.info(String.format("僵尸机器人查询结果：[ %s ]",lst));
                            List<Long> userIdlist = Lists.newArrayList();
                            for (RobotInfoPO po : lst) {
                                if (po.getBatchId().compareTo(0L) == 0) {
                                    continue;
                                }
                                userIdlist.add(po.getUserId());
                            }
                            if (userIdlist.size() != 0) {
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

    public RobotBaseResult regainAccount(){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<String,Void>(){

            @Override
            public void doProcess(EngineContext<String, Void> context) {

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
                            accountPoolManageServiceImpl.updateUnfreezeAndGainLoss(BigDecimal.ZERO,BigDecimal.ZERO,po.getGameId(),po.getBrand(),po.getRoomId());

                            AccountWaterPO accountWaterPO=new AccountWaterPO();
                            accountWaterPO.setUserId(po.getUserId());
                            accountWaterPO.setBatchId(po.getBatchId());
                            accountWaterPO.setBalance(po.getGainLoss());
                            accountWaterPO.setWaterType(Byte.valueOf(BizRobotInfoEnum.ACCOUNT_WATER_GAIN_LOSS_IN.getCode()));
                            accountWaterPO.setGameId(po.getGameId());
                            accountWaterPO.setBrand(po.getBrand());
                            accountWaterPO.setRoomId(po.getRoomId());
                            accountWaterPO.setCreateTime(new Date());
                            accountWaterManageRepository.saveAndFlush(accountWaterPO);
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
