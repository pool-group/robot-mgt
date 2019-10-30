package com.zren.platform.application;

import com.zren.platform.common.service.facade.api.accountPool.AccountPoolManage;
import com.zren.platform.common.service.facade.api.robot.AIRobotRuleManage;
import com.zren.platform.common.service.facade.api.robot.RobotPullManage;
import com.zren.platform.common.service.facade.api.robot.RobotPushManage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 全局启动类
 *
 * @author k.y
 * @version Id: com.zren.platform.test.Application.java, v 0.1 2018年11月05日 下午09:25 k.y Exp $
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.zren.platform.common.util","com.zren.platform.application","com.zren.platform.common.dal"})
@EnableJpaRepositories(basePackages = "com.zren.platform.common.dal")
@EntityScan(basePackages = "com.zren.platform.common.dal")
@EnableFeignClients(clients = {RobotPullManage.class, AIRobotRuleManage.class, RobotPushManage.class, AccountPoolManage.class})
public class Application
{

    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }
}
