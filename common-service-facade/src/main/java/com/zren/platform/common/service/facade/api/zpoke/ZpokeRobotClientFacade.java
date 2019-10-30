package com.zren.platform.common.service.facade.api.zpoke;

import com.zren.platform.common.service.facade.constants.Common;
import com.zren.platform.common.service.facade.dto.in.zpoke.TableMachineDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Zpoke Robot Manage
 *
 * @author k.y
 * @version Id: ZpokeRobotManage.java, v 0.1 2019年10月22日 下午10:49 k.y Exp $
 */
@FeignClient(value= Common.SERVICE_NAME)
public interface ZpokeRobotClientFacade {

    @PostMapping("/zpokeRobotClientFacade/invoke")
    RobotBaseResult invoke(TableMachineDTO tableMachineDTO);
}
