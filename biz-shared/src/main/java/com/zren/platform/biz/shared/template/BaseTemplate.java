package com.zren.platform.biz.shared.template;

import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.common.service.facade.context.ErrorContext;
import com.zren.platform.common.service.facade.result.ResultCode;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.LogUtil;

public abstract class BaseTemplate {

    public void fillResultWithErrorCode(RobotBaseResult result, ErrorCodeEnum ece, String definedMessage) {

        ErrorContext errorContext=new ErrorContext(definedMessage);
        errorContext.addError(new ResultCode(ece.getCodeLevel(),ece.getCodeType(),ece.getCode(),ece.getMessage(),ece.getDesc(),definedMessage));
        result.setSuccess(false);
        result.setErrorContext(errorContext);
    }

    public void fillBizExceptionLogModel(RobotBizException ex){
        LogUtil.warn(String.format(
                " BizOpCenterServiceTemplate execute execute biz exception[ codeLevel=%s, codeType=%s, errorCode=%s, errorMsg=%s, desc=%s, definedMessage=%s ]",
                ex.getErrorCodeEnum().getCodeLevel(), ex.getErrorCodeEnum().getCodeType(),ex.getErrorCodeEnum().getCode(),ex.getErrorCodeEnum().getMessage(),ex.getErrorCodeEnum().getDesc(),ex.getDefinedMessage()));
    }


    public void fillSysExceptionLogModel(RobotSystemException se){
        LogUtil.error(se,String.format(
                " BizOpCenterServiceTemplate execute execute system exception[ codeLevel=%s, codeType=%s, errorCode=%s, errorMsg=%s, desc=%s, definedMessage=%s ]",
                se.getErrorCodeEnum().getCodeLevel(), se.getErrorCodeEnum().getCodeType(),se.getErrorCodeEnum().getCode(),se.getErrorCodeEnum().getMessage(),se.getErrorCodeEnum().getDesc(),se.getDefinedMessage()));
    }


    public void fillUnknownExceptionLogModel(Throwable e, StackTraceElement s){
        LogUtil.error(e, " BizOpCenterServiceTemplate Unknown Exception.  >>>>>>>>  ",String.format(
                "errorMsg = [ FileName=%s, ClassName=%s, MethodName=%s, LineNumber=%s, Message=%s ]   >>>>>>>>  ",s.getFileName(), s.getClassName(),s.getMethodName(),s.getLineNumber(),e.getMessage()));
    }


    public void fillSummaryLogModel(RobotBaseResult result, EngineContext context){
        LogUtil.info(String.format(" Output ...Result [ success=%s ,resultObj=%s ]",result.isSuccess(),result.getResultObj()));
    }
}
