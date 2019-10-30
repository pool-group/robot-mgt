package com.zren.platform.common.service.facade.api.robot;

import com.zren.platform.common.service.facade.constants.Common;
import com.zren.platform.common.service.facade.dto.in.robotInfo.AIRobotInitInputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 推送机器人
 *
 * @author k.y
 * @version Id: RobotPushManage.java, v 0.1 2018年11月23日 下午17:29 k.y Exp $
 */
@FeignClient(value= Common.SERVICE_NAME)
public interface RobotPushManage {

    /**
     * 申请机器人
     *
     * @param robotInitInputModelDTO
     * @return
     */
    @PostMapping("/robotPushManage/createRobot")
    RobotBaseResult createRobot(AIRobotInitInputModelDTO robotInitInputModelDTO);

}
