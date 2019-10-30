package com.zren.platform.common.service.facade.result;

import lombok.Data;

/**
 * 返回结果
 *
 * @author k.y
 * @version Id: RobotBaseResult.java, v 0.1 2018年11月02日 下午15:13 k.y Exp $
 */
@Data
public class RobotBaseResult<R> extends BaseResult {

    private R resultObj;

}
