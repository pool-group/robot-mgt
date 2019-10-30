package com.zren.platform.common.service.facade.result;

import lombok.Data;

/**
 * 标准结果码
 *
 * @author k.y
 * @version Id: ResultCode.java, v 0.1 2018年11月02日 下午18:25 k.y Exp $
 */
@Data
public class ResultCode extends BaseSerializable {

    public ResultCode() {
    }

    /** 序列ID */
    private static final long     serialVersionUID = 3951948353107763580L;

    /** 结果码固定前缀 */
    protected static final String PREFIX           = "ROBOT_RC_";

    /** 结果码级别，INFO-1,WARN-3,ERROR-5,FATAL-7，参见<code>ResultCodeLevel</code>定义 */
    private String                codeLevel;

    /** 结果码类型，SUCCESS-0,BIZ_ERROR-1,SYS_ERROR-2,THIRD_ERROR-3，参见<code>ResultCodeType</code>定义 */
    private String                codeType;

    /** 系统编号 */
    private final String          systemCode="ROBOT_MGT_101001000201811";

    /** 系统名称 */
    private final String          systemName = "ROBOT-MGT";

    /** 具体结果码 */
    private String                errorCode;

    /** 错误英文简称 */
    private String                errorName;

    /** 结果码信息描述，可空 */
    private String                description;

    /**第三方错误*/
    private String                thirdPartyError;

    /**事务ID*/
    private String                tid;

    /**
     * 构造方法。
     *
     * @param codeLevel     结果码级别
     * @param codeType      结果码类型
     * @param errorCode     具体结果码
     * @param errorName     错误英文简称
     * @param description     结果码信息描述
     */
    public ResultCode(String codeLevel, String codeType, String errorCode, String errorName, String description, String thirdPartyError) {
        this.codeLevel = codeLevel;
        this.codeType = codeType;
        this.errorCode = errorCode;
        this.errorName = errorName;
        this.description = description;
        this.thirdPartyError = thirdPartyError;
        this.tid= String.valueOf(System.currentTimeMillis());
    }

}
