package com.zren.platform.common.util.tool;

import com.google.common.collect.Maps;
import com.zren.platform.common.util.exception.CronstrRuleException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Analysis of Cron Expressions for Decimal Points of Character Mapping
 *
 * @author k.y
 * @version Id: CronstrRuleUtil.java, v 0.1 2019年08月02日 下午18:07 k.y Exp $
 */
public class CronstrRuleUtil {

    private final static String MARK_A      =   "|";

    private final static String MARK_B      =   "=";

    private final static String MARK_C      =   "\\";

    /**
     * The process of parsing cron expression format to filter out the specified content
     * Cron expression format has validation requirements and does not meet throwing exceptions
     * The cron expression format is: A = 0.01  OR  A=0.01|B=0.02
     * @param cron
     * @param key
     * @return
     */
    public static BigDecimal invoke(String cron,String key){
        if (StringUtils.isBlank(cron)){
            throw new CronstrRuleException();
        }
        Map<String,BigDecimal> model= Maps.newHashMap();
        if(cron.contains(MARK_A)){
            String[] st=StringUtils.split(cron,MARK_C+MARK_A);
            for(String cr: st){
                analysis(cr,model);
            }
        }else
            analysis(cron,model);
        return model.get(key);
    }

    private static void analysis(String cr,Map<String,BigDecimal> model){
        if(cr.contains(MARK_B)){
            String[] crob=StringUtils.split(cr,MARK_B);
            model.put(crob[0],new BigDecimal(crob[1]));
        }else {
            LogUtil.error(String.format("string cron rule is not right, cron=[ %s ]",cr));
        }
    }
}
