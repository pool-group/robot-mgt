package com.zren.platform.application;

import com.zren.platform.common.service.facade.api.accountPool.AccountPoolManage;
import com.zren.platform.common.service.facade.api.robot.AIRobotRuleManage;
import com.zren.platform.common.service.facade.api.robot.RobotPullManage;
import com.zren.platform.intercomm.gcenter.fegin.RobotIntegrationFeign;
import com.zren.platform.zpoke.common.service.facade.robot.ZpokeClientFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 全局启动类
 *
 * @author k.y
 * @version Id: com.zren.platform.application.Application.java, v 0.1 2018年11月05日 下午09:25 k.y Exp $
 */
@ComponentScan(basePackages = {"com.zren.platform"})
@EnableJpaRepositories(basePackages = {"com.zren.platform.common.dal.repository"})
@EntityScan(basePackages = {"com.zren.platform.common.dal.po"})
@EnableFeignClients(clients = {RobotPullManage.class, AIRobotRuleManage.class, AccountPoolManage.class, RobotIntegrationFeign.class, ZpokeClientFacade.class})
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
