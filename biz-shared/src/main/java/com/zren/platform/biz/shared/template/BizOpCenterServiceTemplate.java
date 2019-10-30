/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.zren.platform.biz.shared.template;


import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.common.service.facade.result.RobotBaseResult;

/**
 * 内部统一服务模板
 *
 * @author k.y
 * @version Id: BizOpCenterServiceTemplate.java, v 0.1 2018年11月02日 下午14:59 k.y Exp $
 */
public interface BizOpCenterServiceTemplate {

    /**
     * 服务模板方法
     * 
     * @param bizCallback 回调
     * @return 通用结果
     */
    <P, R> RobotBaseResult<R> doBizProcess(AbstractOpCallback<P, R> bizCallback);

}
