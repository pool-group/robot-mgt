package com.zren.platform.core.rule.factory;

import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * Cron Rule表达式工厂解析
 *
 * @author k.y
 * @version Id: CronRuleFactory.java, v 0.1 2018年11月30日 下午11:10 k.y Exp $
 */
@Component
public class CronRuleFactory {


    /*
     * Cron Rule合法表达式样例：
     *
     * 多组：0~5=20|5~15=30|15~25=40|25~35=50|35~50=60
     * 单组：35~50=60
     *
     * 描述：单组举例：当风控类型为比例，当前风控参数在35%到50%之间时，赢的事件概率为60%。
     *
     */

    private final static String MARK_A      =   "~";

    private final static String MARK_B      =   "=";

    private final static String MARK_C      =   "|";

    private final static String MARK_D      =   "\\";

    private final static String PATTERN     =   "{0}~{1}={2}";

    /**
     * cron rule表达式解析, 并映射对应结果
     *
     * @param cronRule
     * @param rateParam
     * @return
     */
    public static BigDecimal resolve(String cronRule, BigDecimal rateParam){

        //是否为正数
        Boolean isPlus=false;
        //正数为盈利状态，负数为亏损状态
        if(rateParam.compareTo(BigDecimal.valueOf(0))>0){
            isPlus=true;
        }
        BigDecimal rate=rateParam.abs();
        Map<String,BigDecimal> resultmap=new HashMap<>();
        resultmap.put("bdResult",new BigDecimal(0));
        List<Map<String,BigDecimal>> lst=new ArrayList<>();
        //判断表达式只有一组的情况
        if(!cronRule.contains(MARK_C)){
            if(!checkAndCompareTo(cronRule,resultmap,rate,lst)){
                throw new RobotBizException(ErrorCodeEnum.CRON_RULE_SYS);
            }
        }else {
            String[] cronArray=cronRule.split(MARK_D+MARK_C);
            for(String cron:cronArray){
                if(!checkAndCompareTo(cron,resultmap,rate,lst)){
                    throw new RobotBizException(ErrorCodeEnum.CRON_RULE_SYS);
                }
            }
        }
        BigDecimal bdResult=resultmap.get("bdResult");
        Collections.sort(lst, new Comparator<Map<String, BigDecimal>>() {
            @Override
            public int compare(Map<String, BigDecimal> o1, Map<String, BigDecimal> o2) {
                BigDecimal map1value = o1.get("scendBD");
                BigDecimal map2value = o2.get("scendBD");
                return map1value.compareTo(map2value);
            }
        });
        BigDecimal maxBD=lst.get(lst.size()-1).get("scendBD");
        BigDecimal minBD=lst.get(0).get("firstBD");
        /*
         * 对表达式配置遗漏做容错处理
         */
        if(rate.compareTo(maxBD)>0){
            if(isPlus){
                bdResult=BigDecimal.valueOf(0.01);
            }else {
                bdResult=BigDecimal.valueOf(0.99);
            }
        }
        if(rate.compareTo(minBD)<0){
            throw new RobotBizException(ErrorCodeEnum.CRON_RULE_NO_COMPLETE);
        }
        if(rate.compareTo(minBD)>0&&rate.compareTo(maxBD)<0&&bdResult.compareTo(BigDecimal.valueOf(0))==0){
            throw new RobotBizException(ErrorCodeEnum.CRON_RULE_NO_COMPLETE);
        }
        if(bdResult.compareTo(BigDecimal.valueOf(0))==0){
            throw new RobotBizException(ErrorCodeEnum.CRON_RULE_NO_COMPLETE);
        }
        return bdResult;
    }

    /**
     * 验证表达式是否合法
     *
     * @param cronRule
     * @return
     */
    private static Boolean checkAndCompareTo(String cronRule,Map<String,BigDecimal> resultmap,BigDecimal rate,List<Map<String,BigDecimal>> lst) {
        if(cronRule.contains(MARK_A)&&cronRule.contains(MARK_B)){
            Map<String,BigDecimal> model=new HashMap<>();
            String oldValue=cronRule;
            String firstStr=cronRule.split(MARK_A)[0];
            String scendStr=cronRule.split(MARK_A)[1].split(MARK_B)[0];
            String threeStr=cronRule.split(MARK_A)[1].split(MARK_B)[1];
            //整数数字校验,非数字类型会抛出异常
            BigDecimal firstBD= new BigDecimal(firstStr).divide(BigDecimal.valueOf(100));
            BigDecimal scendBD= new BigDecimal(scendStr).divide(BigDecimal.valueOf(100));
            BigDecimal threeBD= new BigDecimal(threeStr).divide(BigDecimal.valueOf(100));
            if(rate.compareTo(firstBD)==1&&rate.compareTo(scendBD)<=0){
                resultmap.put("bdResult",threeBD);
            }
            model.put("firstBD",firstBD);
            model.put("scendBD",scendBD);
            model.put("threeBD",threeBD);
            lst.add(model);
            String newValue= MessageFormat.format(PATTERN ,firstStr,scendStr,threeStr);
            return oldValue.equals(newValue);
        }
        return false;
    }

}
