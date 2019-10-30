package com.zren.platform.core.rule.strategy.common;

import com.zren.platform.common.dal.po.ZjhStrategyPO;
import com.zren.platform.common.dal.repository.ZjhStrategyRepository;
import com.zren.platform.common.util.Tuple.Tuple2x;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Policy Information Query Base Strategy
 *
 * @author k.y
 * @version Id: BaseStrategy.java, v 0.1 2019年08月01日 下午14:37 k.y Exp $
 */
public abstract class BaseStrategy {

    protected final static Long expire=2*60*1000L;

    public final static String mark="##";

    @Autowired
    private ZjhStrategyRepository zjhStrategyRepository;

    public void initcontainer(Map<String,Tuple2x> model){
        List<ZjhStrategyPO> lst=zjhStrategyRepository.findAll();
        lst.stream().forEach(s -> {
            model.put(s.getCardType()+mark+s.getCardKey()+mark+s.getRound(),new Tuple2x<Long,ZjhStrategyPO>(System.currentTimeMillis()+expire,s));
        });
    }
}
