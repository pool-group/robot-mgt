package com.zren.platform.common.util.tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date Util Tool
 *
 * @author k.y
 * @version Id: DateUtils.java, v 0.1 2019年04月06日 下午17:43 k.y Exp $
 */
public class DateUtil {

    /**
     * Now Date format to String
     *
     * @param format
     * @return
     */
    public static String getNowDateFormatToString(String format){
        //获得当前时间
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return ldt.format(dtf);
    }
}
