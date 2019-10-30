/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.zren.platform.biz.shared.template;


import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.common.service.facade.result.RobotBaseResult;

public interface BizOpCenterServiceTemplate {

    <P, R> RobotBaseResult<R> doBizProcess(AbstractOpCallback<P, R> bizCallback);

}
