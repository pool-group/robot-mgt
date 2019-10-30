package com.zren.platform.core.rule.factory;

import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProbabilisticFactory {

    private final static BigDecimal BASIC_DOWN_INTERVAL = BigDecimal.valueOf(0);

    private final static BigDecimal BASIC_UP_INTERVAL = BigDecimal.valueOf(99900);

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
