package com.zren.platform.biz.shared.context;

import lombok.Data;
import org.slf4j.Logger;

/**
 * 业务上下文
 *
 * @author k.y
 * @version Id: EngineContext.java, v 0.1 2018年11月02日 下午14:59 k.y Exp $
 */
@Data
public class EngineContext<P, R> {

    private P inputModel;

    private R outputModel;

    private Logger log;
}
