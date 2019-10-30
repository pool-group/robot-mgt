package com.zren.platform.common.util.exception;

/**
 * Cron expression format validation exception capture
 *
 * @author k.y
 * @version Id: CronstrRuleException.java, v 0.1 2019年08月02日 下午21:14 k.y Exp $
 */
public class CronstrRuleException extends RuntimeException {

    /**
     * Cron expression format validation exception constructor
     * @param message
     * @param cause
     */
    public CronstrRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public CronstrRuleException(String message) {
        super(message);
    }

    public CronstrRuleException() {
    }
}
