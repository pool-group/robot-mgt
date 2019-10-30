package com.zren.platform.common.service.integration.api.zpoke;

import com.assembly.common.util.LogUtil;
import com.assembly.template.engine.result.IBaseResult;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.zpoke.common.service.facade.robot.ZpokeClientFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Zpoke Robot Client Integration
 *
 * @author k.y
 * @version Id: ZpokeRobotClientIntegration.java, v 0.1 2019年10月22日 下午13:25 k.y Exp $
 */
@Service
@RequiredArgsConstructor
public class ZpokeRobotClientIntegration {

    final private ZpokeClientFacade zpokeClientFacade;

    @Async
    public void invoke(String jsonString){
        LogUtil.info(String.format(" zpokeRobotClientFacade.invoke(new MessageContext(json)): jsonString=[ %s ]",jsonString));
        try {

            IBaseResult iBaseResult=zpokeClientFacade.invoke(jsonString);
            if(!iBaseResult.isSuccess()){
                throw new RobotBizException(String.format(" zpokeRobotClientFacade.invoke() isSuccess=false, exception:[ %s ]",iBaseResult.getErrorContext().fetchCurrentError().toString()));
            }
        } catch (Exception e) {
            throw new RobotSystemException("zpokeRobotClientFacade.invoke() is exception! ",e);
        }
    }

}