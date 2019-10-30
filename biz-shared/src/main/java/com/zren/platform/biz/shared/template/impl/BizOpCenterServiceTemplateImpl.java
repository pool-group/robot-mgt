
package com.zren.platform.biz.shared.template.impl;

import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.template.BaseTemplate;
import com.zren.platform.biz.shared.template.BizOpCenterServiceTemplate;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.LogUtil;
import org.springframework.stereotype.Service;

/**
 * 内部统一服务模板
 *
 * @author k.y
 * @version Id: BizOpCenterServiceTemplateImpl.java, v 0.1 2018年11月02日 下午14:59 k.y Exp $
 */
@Service
public class BizOpCenterServiceTemplateImpl extends BaseTemplate implements BizOpCenterServiceTemplate {

    /**
     * @see BizOpCenterServiceTemplate#doBizProcess(AbstractOpCallback) 
     */
    @Override
    public <P, R> RobotBaseResult<R> doBizProcess(AbstractOpCallback<P, R> bizCallback) {
        RobotBaseResult<R> result = new RobotBaseResult<>();
        EngineContext<P, R> context = new EngineContext<>();
        try {
            LogUtil.info(">>> biz service start..");

            //1.检查参数
            bizCallback.preCheck();

            //2.初始化参数及上下文
            bizCallback.initContent(context);

            //3.请求参数
            LogUtil.info(String.format(" Input ...Parameter [ %s ]  ",null==context.getInputModel()?null:context.getInputModel().toString()));

            //4.业务处理
            bizCallback.doProcess(context);

            //5.后置处理
            bizCallback.afterProcess(context);

            //6.设置结果，默认true
            result.setResultObj(context.getOutputModel());
            result.setSuccess(true);

        } catch (RobotBizException ex) {

            fillBizExceptionLogModel(ex);
            fillResultWithErrorCode(result, ex.getErrorCodeEnum(),ex.getDefinedMessage());

        } catch (RobotSystemException se) {

            fillSysExceptionLogModel(se);
            fillResultWithErrorCode(result, se.getErrorCodeEnum(), se.getDefinedMessage());

        } catch (Throwable e) {

            StackTraceElement s= e.getStackTrace()[0];
            fillUnknownExceptionLogModel(e,s);
            fillResultWithErrorCode(result, ErrorCodeEnum.SYS_EXCP, String.format(
                "errorMsg = [ FileName=%s, ClassName=%s, MethodName=%s, LineNumber=%s, Message=%s ]   >>>>>>>>  ",s.getFileName(), s.getClassName(),s.getMethodName(),s.getLineNumber(),e.getMessage()));

        } finally {

            fillSummaryLogModel(result, context);

            //7.清理上下文

            LogUtil.info("<<< biz service end..");
        }

        return result;
    }

}
