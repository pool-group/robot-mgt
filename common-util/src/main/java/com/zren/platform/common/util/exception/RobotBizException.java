package com.zren.platform.common.util.exception;

import com.zren.platform.common.util.enums.ErrorCodeEnum;
import lombok.Data;

/**
 * 业务异常
 *
 * @author k.y
 * @version Id: RobotBizException.java, v 0.1 2018年11月02日 下午15:30 k.y Exp $
 */
@Data
public class RobotBizException extends RuntimeException {

    private ErrorCodeEnum errorCodeEnum;

    /**自定义message内容*/
    private String definedMessage;

    public RobotBizException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }

    public RobotBizException(String message, Throwable cause, ErrorCodeEnum errorCodeEnum) {
        super(message, cause);
        this.errorCodeEnum = errorCodeEnum;
    }

    public RobotBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public RobotBizException(ErrorCodeEnum errorCodeEnum,String definedMessage) {
        this.errorCodeEnum = errorCodeEnum;
        this.definedMessage = definedMessage;
    }

    public RobotBizException(String message, ErrorCodeEnum errorCodeEnum) {
        super(message);
        this.errorCodeEnum = errorCodeEnum;
        this.definedMessage = message;
    }

    public RobotBizException(String message) {
        super(message);
        this.definedMessage = message;
    }

    public String getDefinedMessage() {
        if(null==this.definedMessage){
            this.definedMessage="";
        }
        return definedMessage;
    }
}
