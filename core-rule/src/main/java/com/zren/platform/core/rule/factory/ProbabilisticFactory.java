package com.zren.platform.core.rule.factory;

import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 概率事件工厂
 *
 * @author k.y
 * @version Id: ProbabilisticFactory.java, v 0.1 2018年11月30日 下午15:43 k.y Exp $
 */
@Component
public class ProbabilisticFactory {

    /**概率区间下限*/
    private final static BigDecimal BASIC_DOWN_INTERVAL = BigDecimal.valueOf(0);

    /**概率区间上限*/
    private final static BigDecimal BASIC_UP_INTERVAL = BigDecimal.valueOf(99900);


    /**
     * 概率区间是否命中
     *
     * @param rate
     * @return
     */
    public int excute(BigDecimal rate){
        BigDecimal current=BASIC_UP_INTERVAL.multiply(rate);
        int random= DataUtil.randomNumber(BASIC_DOWN_INTERVAL.intValue(),BASIC_UP_INTERVAL.intValue()+1,1)[0];
        LogUtil.info(String.format(" ProbabilisticFactory is finish ..  , current=[ %s ], random=[ %s ]",current,random));
        if(BigDecimal.valueOf(random).compareTo(BASIC_DOWN_INTERVAL)>=0&&BigDecimal.valueOf(random).compareTo(current)<=0){
            return 1;
        }
        return 0;
    }

}
