package com.zren.platform.common.util.exception;

import com.zren.platform.common.util.enums.ErrorCodeEnum;
import lombok.Data;

/**
 * 系统异常
 *
 * @author k.y
 * @version Id: RobotSystemException.java, v 0.1 2018年11月02日 下午15:36 k.y Exp $
 */
@Data
public class RobotSystemException extends RuntimeException {

    private ErrorCodeEnum errorCodeEnum;

    /**自定义message内容*/
    private String definedMessage;

    public RobotSystemException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }

    public RobotSystemException(String message, Throwable cause, ErrorCodeEnum errorCodeEnum) {
        super(message, cause);
        this.errorCodeEnum = errorCodeEnum;
    }

    public RobotSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public RobotSystemException(Throwable cause, ErrorCodeEnum errorCodeEnum) {
        super(cause);
        this.errorCodeEnum = errorCodeEnum;
    }

    public RobotSystemException(ErrorCodeEnum errorCodeEnum,String definedMessage) {
        this.errorCodeEnum = errorCodeEnum;
        this.definedMessage = definedMessage;
    }

    public RobotSystemException(String message, ErrorCodeEnum errorCodeEnum) {
        super(message);
        this.errorCodeEnum = errorCodeEnum;
        this.definedMessage = message;
    }

    public RobotSystemException(String message) {
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
