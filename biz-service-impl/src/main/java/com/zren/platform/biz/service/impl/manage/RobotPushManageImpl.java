package com.zren.platform.biz.service.impl.manage;

import com.zren.platform.biz.action.biz.RobotBizPush;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.common.service.facade.api.robot.RobotPushManage;
import com.zren.platform.common.service.facade.dto.in.robotInfo.AIRobotInitInputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI机器人管理实现
 *
 * @author k.y
 * @version Id: AIRobotManageServiceImpl.java, v 0.1 2018年11月23日 下午17:36 k.y Exp $
 */
@RestController
@Slf4j
public class RobotPushManageImpl implements RobotPushManage {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private RobotBizPush robotBizPush;

    @Override
    public RobotBaseResult createRobot(@RequestBody AIRobotInitInputModelDTO robotInitInputModelDTO) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<AIRobotInitInputModelDTO,Void>(){

           @Override
            public void initContent(EngineContext<AIRobotInitInputModelDTO, Void> context) {
                context.setInputModel(robotInitInputModelDTO);
            }

            @Override
            public void doProcess(EngineContext<AIRobotInitInputModelDTO, Void> context) throws InterruptedException {

                robotBizPush.createRobot(robotInitInputModelDTO);
            }
        });
    }

}
