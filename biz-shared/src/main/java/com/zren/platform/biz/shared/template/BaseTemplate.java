package com.zren.platform.biz.shared.template;

import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.common.service.facade.context.ErrorContext;
import com.zren.platform.common.service.facade.result.ResultCode;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.LogUtil;

/**
 * 模板基类
 *
 * @author k.y
 * @version Id: BaseTemplate.java, v 0.1 2018年11月02日 下午18:01 k.y Exp $
 */
public abstract class BaseTemplate {

    /**
     * 填充错误堆栈
     *
     * @param result
     * @param ece
     */
    public void fillResultWithErrorCode(RobotBaseResult result, ErrorCodeEnum ece, String definedMessage) {

        ErrorContext errorContext=new ErrorContext(definedMessage);
        errorContext.addError(new ResultCode(ece.getCodeLevel(),ece.getCodeType(),ece.getCode(),ece.getMessage(),ece.getDesc(),definedMessage));
        result.setSuccess(false);
        result.setErrorContext(errorContext);
    }

    /**
     * 业务异常日志
     *
     * @param ex
     */
    public void fillBizExceptionLogModel(RobotBizException ex){
        LogUtil.warn(String.format(
                " BizOpCenterServiceTemplate execute execute biz exception[ codeLevel=%s, codeType=%s, errorCode=%s, errorMsg=%s, desc=%s, definedMessage=%s ]",
                ex.getErrorCodeEnum().getCodeLevel(), ex.getErrorCodeEnum().getCodeType(),ex.getErrorCodeEnum().getCode(),ex.getErrorCodeEnum().getMessage(),ex.getErrorCodeEnum().getDesc(),ex.getDefinedMessage()));
    }


    /**
     * 系统异常日志
     *
     * @param se
     */
    public void fillSysExceptionLogModel(RobotSystemException se){
        LogUtil.error(se,String.format(
                " BizOpCenterServiceTemplate execute execute system exception[ codeLevel=%s, codeType=%s, errorCode=%s, errorMsg=%s, desc=%s, definedMessage=%s ]",
                se.getErrorCodeEnum().getCodeLevel(), se.getErrorCodeEnum().getCodeType(),se.getErrorCodeEnum().getCode(),se.getErrorCodeEnum().getMessage(),se.getErrorCodeEnum().getDesc(),se.getDefinedMessage()));
    }


    /**
     * 未知异常日志
     *
     * @param e
     * @param s
     */
    public void fillUnknownExceptionLogModel(Throwable e, StackTraceElement s){
        LogUtil.error(e, " BizOpCenterServiceTemplate Unknown Exception.  >>>>>>>>  ",String.format(
                "errorMsg = [ FileName=%s, ClassName=%s, MethodName=%s, LineNumber=%s, Message=%s ]   >>>>>>>>  ",s.getFileName(), s.getClassName(),s.getMethodName(),s.getLineNumber(),e.getMessage()));
    }


    /**
     * 摘要日志
     *
     * @param result
     * @param context
     */
    public void fillSummaryLogModel(RobotBaseResult result, EngineContext context){
        LogUtil.info(String.format(" Output ...Result [ success=%s ,resultObj=%s ]",result.isSuccess(),result.getResultObj()));
    }
}
