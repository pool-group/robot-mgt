package com.zren.platform.biz.shared.scheduled;

import com.zren.platform.biz.shared.core.robot.RobotClearManageServiceImpl;
import com.zren.platform.common.util.tool.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 清理僵尸机器人定时任务
 *
 * @author k.y
 * @version Id: RobotClearSnapTask.java, v 0.1 2018年12月27日 下午14:17 k.y Exp $
 */
@Component
public class RobotScheduledTask {

    @Autowired
    private RobotClearManageServiceImpl robotClearManageServiceImpl;

    /**Redis Client*/
    @Autowired
    private RedissonClient redisClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 1小时清理一次超过1个小时僵尸机器人，并快照相关账务信息
     */
    @Scheduled(fixedDelay = 1000*60*60)
    public void startClearSnap(){

        String executeTime = stringRedisTemplate.opsForValue()
                .get("Task.robot.clear.executeTime");

        if (StringUtils.isNotEmpty(executeTime)
                && Long.parseLong(executeTime) > System.currentTimeMillis()) {
            return;
        }

        RLock lock = redisClient.getLock("Task.robot.clear");
        boolean isLocked = lock.tryLock();
        if (isLocked) {
            try {
                LogUtil.info(String.format(" Task Thread=[ %s ] 清理僵尸机器人任务 Start..",Thread.currentThread().getId()));
                robotClearManageServiceImpl.clearSnap(new Date());
                LogUtil.info(String.format(" Task Thread=[ %s ] 清理僵尸机器人任务 End..",Thread.currentThread().getId()));
                stringRedisTemplate.opsForValue()
                        .set("Task.robot.clear.executeTime",
                                String.valueOf(System.currentTimeMillis() + 10 * 1000));
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 10分钟恢复一次账变信息
     */
//    @Scheduled(fixedDelay = 1000*60*10)
//    public void startRegainAccount(){
//
//        String executeTime = stringRedisTemplate.opsForValue()
//                .get("Task.robot.regainAccount.executeTime");
//
//        if (StringUtils.isNotEmpty(executeTime)
//                && Long.parseLong(executeTime) > System.currentTimeMillis()) {
//            return;
//        }
//
//        RLock lock = redisClient.getLock("Task.robot.regainAccount");
//        boolean isLocked = lock.tryLock();
//        if (isLocked) {
//            try {
//                LogUtil.info(String.format(" Task Thread=[ %s ] 机器人平账任务 Start..",Thread.currentThread().getId()));
//                robotClearManageServiceImpl.regainAccount();
//                LogUtil.info(String.format(" Task Thread=[ %s ] 机器人平账任务 End..",Thread.currentThread().getId()));
//                stringRedisTemplate.opsForValue()
//                        .set("Task.robot.regainAccount.executeTime",
//                                String.valueOf(System.currentTimeMillis() + 10 * 1000));
//            } finally {
//                lock.unlock();
//            }
//        }
//    }
}
