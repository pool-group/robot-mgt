package com.zren.platform.core.rule.factory;

import com.google.common.collect.Maps;
import com.zren.platform.common.dal.po.ZjhStrategyPO;
import com.zren.platform.common.util.tuple.Tuple2x;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.core.rule.entity.in.ZjhStrategyEntity;
import com.zren.platform.core.rule.strategy.common.BaseIntegration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Strategic Factory
 *
 * @author k.y
 * @version Id: ZJHStrategyFactory.java, v 0.1 2018年12月17日 下午14:56 k.y Exp $
 */
@Component
public class ZJHAIStrategyFactory extends BaseIntegration implements InitializingBean {


    /**
     * Advanced Container for Advanced Behavior Policy, KeY Storage is divided into two levels
     * <p>
     * Level 1 KEY is a brand value type, such as single card, counterpart, etc.
     * Secondary KEY is the brand type value, such as: single card maximum, pair, cis-zi maximum, etc.
     * Secondary KEY supports interval ranges as KEY, e.g. 4-9
     * Behavior Strategy of LIST Storage Board with Different Rounds
     */
    private final Map<String,Tuple2x>         AI_ACTION_MODEL                                  =      Maps.newConcurrentMap();

    /**
     * Back up a copy of the available policy basics when the container starts
     * Error while dynamically modifying database script policy, use backup container information
     * Ensure high availability of policy information
     * The rule policy for backup container storage is the correct content at service startup
     */
    private final Map<String,Tuple2x>         AI_ACTION_AVAILABILITY_MODEL                     =      Maps.newConcurrentMap();

    /**
     * Using Optimistic Locks to Ensure Performance
     * Only one thread can update the policy container at a time.
     * Preventing Cache Penetration
     */
    private AtomicBoolean                     A_BOOL                                           =      new AtomicBoolean(true);

    @Override
    public void afterPropertiesSet() {
        refreshAIActionOriginal();
        if(null!=AI_ACTION_MODEL)
            AI_ACTION_AVAILABILITY_MODEL.putAll(AI_ACTION_MODEL);
    }

    private void refreshAIActionOriginal(){
        try {
            super.initcontainer(AI_ACTION_MODEL);
        } finally {
            A_BOOL.compareAndSet(false,true);
        }
        LogUtil.info("AI Policy container parameters refreshed success!");
    }

    public ZjhStrategyPO initAIActionOriginal(String key, Boolean boolx, ZjhStrategyEntity zse){
        LogUtil.info(String.format("ai strategy request parameter: key=[ %s ]",key));
        if(!boolx&&System.currentTimeMillis()>(Long) AI_ACTION_MODEL.get(key)._1().get()&&A_BOOL.compareAndSet(true,false)){
            refreshAIActionOriginal();
        }
        Tuple2x tuple2x;
        if (boolx)
            tuple2x=AI_ACTION_AVAILABILITY_MODEL.get(key);
        else
            tuple2x=AI_ACTION_MODEL.get(key);
        addDenyMember(zse);
        return (ZjhStrategyPO) tuple2x._2().get();
    }
}
