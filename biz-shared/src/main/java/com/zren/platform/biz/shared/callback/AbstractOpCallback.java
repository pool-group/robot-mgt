package com.zren.platform.biz.shared.callback;

import com.zren.platform.biz.shared.context.EngineContext;

/**
 * 系统中枢回调
 *
 * @author k.y
 * @version Id: AbstractOpCallback.java, v 0.1 2018年11月02日 下午14:43 k.y Exp $
 */
public abstract class AbstractOpCallback<P, R> {

    /**
     * 参数合法性校验[前置处理]
     */
    public void preCheck(){}

    /**
     * 初始化参数及上下文
     * @return 上下文
     */
    public void initContent(EngineContext<P, R> context) {}

    /**
     * 业务处理核心逻辑
     *
     * @param context 上下文
     */
    public abstract void doProcess(EngineContext<P, R> context);

    /**
     * 业务[后置处理]
     *
     * @param context 上下文
     */
    public void afterProcess(EngineContext<P, R> context){}


}
