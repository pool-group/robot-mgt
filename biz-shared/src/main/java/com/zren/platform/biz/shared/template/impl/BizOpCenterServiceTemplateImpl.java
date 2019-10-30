
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

            bizCallback.preCheck();

            bizCallback.initContent(context);

            LogUtil.info(String.format(" Input ...Parameter [ %s ]  ",null==context.getInputModel()?null:context.getInputModel().toString()));

            bizCallback.doProcess(context);

            bizCallback.afterProcess(context);

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

            LogUtil.info("<<< biz service end..");
        }

        return result;
    }

}
