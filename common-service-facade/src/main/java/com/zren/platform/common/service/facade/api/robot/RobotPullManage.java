package com.zren.platform.common.service.facade.api.robot;

import com.zren.platform.common.service.facade.constants.Common;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotDestroyInputModelDTO;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotInitInputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 拉取机器人
 *
 * @author k.y
 * @version Id: RobotPullManage.java, v 0.1 2018年11月05日 下午09:25 k.y Exp $
 */
@FeignClient(value= Common.SERVICE_NAME)
public interface RobotPullManage {


    /**
     * 获取机器人账户信息
     *
     * @param robotInitInputModelDTO   游戏类型  最小金额  机器人数量
     * @return
     */
    @PostMapping("/robotPullManage/receiveRobotInfo")
    RobotBaseResult createRobot(RobotInitInputModelDTO robotInitInputModelDTO);


    /**
     * 销毁机器人
     *
     * @param robotDestroyInputModelList 机器人ID  当前余额  游戏类型
     * @return
     */
    @PostMapping("/robotPullManage/destroyRobot")
    RobotBaseResult destroyRobot(List<RobotDestroyInputModelDTO> robotDestroyInputModelList);

}
