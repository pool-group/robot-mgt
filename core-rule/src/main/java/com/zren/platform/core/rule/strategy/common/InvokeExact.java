package com.zren.platform.core.rule.strategy.common;

import com.zren.platform.common.dal.po.ZjhStrategyPO;
import com.zren.platform.common.util.exception.CronstrRuleException;
import com.zren.platform.common.util.tool.CronstrRuleUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Accurate Search for the Type of Strategy
 *
 * @author k.y
 * @version Id: LookInvokeExact.java, v 0.1 2019年08月02日 下午15:35 k.y Exp $
 */
@Data
public class InvokeExact {

    private AtomicBoolean boolAx                                             = new AtomicBoolean(false);

    private AtomicBoolean boolBx                                             = new AtomicBoolean(false);

    private AtomicBoolean boolCx                                             = new AtomicBoolean(false);

    private AtomicBoolean boolEx                                             = new AtomicBoolean(false);

    private AtomicBoolean boolEVx                                            = new AtomicBoolean(false);

    private ZjhStrategyPO po;
    private String Ax;
    private String Bx;
    private String Cx;
    private String Ex;
    private String EVx;
    private String Axcron;
    private String Bxcron;
    private String Cxcron;
    private String Excron;
    private String EVxcron;

    public InvokeExact(ZjhStrategyPO po) {
        this.po = po;
    }

    public InvokeExact bindAx(final String x) {
        if (StringUtils.isNotBlank(x) && boolAx.compareAndSet(false, true)) {
            this.Ax = x;
            this.Axcron = po.getLookStrategy();
        }
        return this;
    }

    public InvokeExact bindBx(final String x) {
        if (StringUtils.isNotBlank(x) && boolBx.compareAndSet(false, true)) {
            this.Bx = x;
            this.Bxcron = po.getLookToGiveUpStrategy();

        }
        return this;
    }

    public InvokeExact bindCx(final String x) {
        if (StringUtils.isNotBlank(x) && boolCx.compareAndSet(false, true)) {
            this.Cx = x;
            this.Cxcron = po.getVsStrategy();
        }
        return this;
    }

    public InvokeExact bindEx(final String x) {
        if (StringUtils.isNotBlank(x) && boolEx.compareAndSet(false, true))
            this.Ex = x;
        this.Excron = po.getAddBetStrategy();
        return this;
    }

    public InvokeExact bindEVx(final String x) {
        if (StringUtils.isNotBlank(x) && boolEVx.compareAndSet(false, true)) {
            this.EVx = x;
            this.EVxcron = po.getAddBetStrategy();
        }
        return this;
    }

    public BigDecimal analysisLook() {
        BigDecimal result = null;
        try {
            result = CronstrRuleUtil.invoke(Axcron, Ax);
        } catch (Exception e) {
            throw new CronstrRuleException(String.format("key=[ %s ], cron=[ %s ] analysis Ax string cron rule is exception ..",Ax, Axcron),e);
        }
        return result;
    }

    public BigDecimal analysisGiveup() {
        BigDecimal result = null;
        try {
            result = CronstrRuleUtil.invoke(Bxcron, Bx);
        } catch (Exception e) {
            throw new CronstrRuleException(String.format("key=[ %s ], cron=[ %s ] analysis Bx string cron rule is exception ..",Bx, Bxcron),e);
        }
        return result;
    }

    public BigDecimal analysisVs() {
        BigDecimal result = null;
        try {
            result = CronstrRuleUtil.invoke(Cxcron, Cx);
        } catch (Exception e) {
            throw new CronstrRuleException(String.format("key=[ %s ], cron=[ %s ] analysis Cx string cron rule is exception ..",Cx, Cxcron),e);
        }
        return result;
    }

    public BigDecimal analysisUnLookAdd() {
        BigDecimal result = null;
        try {
            result = CronstrRuleUtil.invoke(Excron, Ex);
        } catch (Exception e) {
            throw new CronstrRuleException(String.format("key=[ %s ], cron=[ %s ] analysis Ex string cron rule is exception ..",Ex, Excron),e);
        }
        return result;
    }

    public BigDecimal analysisAdd() {
        BigDecimal result = null;
        try {
            result = CronstrRuleUtil.invoke(EVxcron, EVx);
        } catch (Exception e) {
            throw new CronstrRuleException(String.format("key=[ %s ], cron=[ %s ] analysis EVx string cron rule is exception ..",EVx, EVxcron),e);
        }
        return result;
    }
}
