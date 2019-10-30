package com.zren.platform.common.util.tool;

import java.math.BigDecimal;

/**
 * @author k.y
 * @version Id: ProbabilisticUtil.java, v 0.1 2019年08月13日 下午18:28 k.y Exp $
 */
public class ProbabilisticUtil {

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
    public static int excute(BigDecimal rate){
        BigDecimal current=BASIC_UP_INTERVAL.multiply(rate);
        int random= DataUtil.randomNumber(BASIC_DOWN_INTERVAL.intValue(),BASIC_UP_INTERVAL.intValue()+1,1)[0];
        LogUtil.info(String.format(" ProbabilisticFactory is finish ..  , current=[ %s ], random=[ %s ]",current,random));
        if(BigDecimal.valueOf(random).compareTo(BASIC_DOWN_INTERVAL)>=0&&BigDecimal.valueOf(random).compareTo(current)<=0){
            return 1;
        }
        return 0;
    }
}
