package com.zren.platform.common.util.enums;

import com.zren.platform.common.util.constants.ResultCodeLevel;
import com.zren.platform.common.util.constants.ResultCodeType;

/**
 * 系统枚举定义
 *
 * @author k.y
 * @version Id: ErrorCodeEnum.java, v 0.1 2018年11月02日 下午15:30 k.y Exp $
 */
public enum ErrorCodeEnum {

    /**
     * 定义说明：根据不同类型定义错误码开头，如：3开头为：警告级别  5开头为：系统异常  9开头为：未知异常
     */

    SYS_EXCP(ResultCodeLevel.SYS_EXCP, ResultCodeType.SYS_EXCP,"901001000","BizOpCenterServiceTemplate Unknown Exception.","系统未知异常"),

    MODULE_INVOKE_ERROR(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR,"511001000","MODULE INVOKE ERROR","模块调用异常"),

    /*--机器人-----------------------------------------------------------------------------------------------------------------------------------------*/
    ACCOUNT_POOL_NOT_ENOUGH(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001000", "ACCOUNT_POOL_NOT_ENOUGH", "分配资金超过资金池风控余额"),

    ACCOUNT_POOL_ZERO(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001001", "ACCOUNT_POOL_ZERO", "可用资金为零"),

    ACCOUNT_POOL_EXCEPTION(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001002", "ACCOUNT_POOL_EXCEPTION", "存在账务异常,冻结资金超过原始本金"),

    ROBOT_NO_ENOUGH(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001003","ROBOT NO ENOUGH","当前没有充足的机器人可以使用"),

    INVALID_BATCH(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001004","INVALID BATCH NUMBER","该批次[batchId和userId]未查到对应账目流水信息,请核实"),

    INVALID_ACCOUNT_POOL(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001005","INVALID ACCOUNT POOL","未查到对应账户信息,请核实大账户是否存在"),

    ACCOUNT_IS_UNABLE(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001009","ACCOUNT IS UNABLE","该房间的机器人大账户已关闭, 无法申请机器人"),

    CRON_RULE_SYS(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001010","CRON RULE SYS","Cron Rule策略表达式解析异常,请查看是否已容错启用默认规则,并检查数据库表达式配置是否有误"),

    CRON_RULE_NO_COMPLETE(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001011","CRON RULE NO COMPLETE","Cron Rule策略表达式脚本配置区间不完整,请检查!"),

    ROBOT_PUSH_INIT_SYS(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001012","ROBOT PUSH INIT SYS","机器人推送,初始化信息和策略出现异常"),

    ROBOT_DESTROY_SYS(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR, "501001004","robot destroy is error","机器人回收出现异常"),

    ROBOT_UPDATE_ACCOUNT_SYS(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR, "501001005","robot destroy is error","机器人更新账变出现异常"),

    ROBOT_SELECT_SYS(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR, "501001006","robot select is fail","机器人查询失败"),

    /*--炸金花-----------------------------------------------------------------------------------------------------------------------------------------*/

    COMPARE_CARD_EXCP(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR,"501001000","Compare Card Unknown Exception.","比牌异常"),

    SEND_MQ_MESSAGE_SYS(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR,"501001001","SEND MQ MESSAGE SYS","发送MQ消息异常"),

    SEND_DELAY_MESSAGE_SYS(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR,"501001002","SEND DELAY MESSAGE SYS","发送延迟消息异常"),

    ILLEGAL_CARD(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "301001006","ILLEGAL CODE CARD ","非法牌值"),

    MESSAGE_BIND_BEAN_SYS(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR,"301001007","MESSAGE BIND BEAN SYS","该类型消息分发Bean未绑定, 请检查"),

    TOPIC_MATCH_NULL(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR,"301001008","TOPIC MATCH NULL","topic匹配为空,请确认facade通配枚举与RocketMQ键值配置是否一致"),

    MQ_MESSAGE_ILLEGAL(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR,"301001013","MQ MESSAGE SYS","非法MQ消息"),

    INPUT_PARAMETER_NOT_EMPTY(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR,"301001014","INPUT PARAMETER NOT EMPTY","请求参数不正确"),

    INIT_CROPN_BACKUP_SYS(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR,"501001001","SEND MQ MESSAGE SYS","从策略备份容器中获取信息或者策略cron解析异常"),

    FAIL_SYS_CREATE_ROBOT(ResultCodeLevel.ERROR, ResultCodeType.SYS_ERROR,"501001003","System Creation Robot Failed!","系统创建机器人失败");



    private String codeLevel;
    private String codeType;
    private String code;
    private String message;
    private String desc;

    ErrorCodeEnum(String codeLevel, String codeType, String code, String message, String desc) {
        this.codeLevel=codeLevel;
        this.codeType=codeType;
        this.code=code;
        this.message=message;
        this.desc=desc;
    }

    public String getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(String codeLevel) {
        this.codeLevel = codeLevel;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
