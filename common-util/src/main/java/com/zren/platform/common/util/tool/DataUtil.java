package com.zren.platform.common.util.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据处理工具类
 *
 * @author k.y
 * @version Id: DataUtil.java, v 0.1 2018年11月07日 下午18:56 k.y Exp $
 */
public class DataUtil {

    /**26字母*/
    protected final static char[] CHARACTER = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    /**0-9数字*/
    protected final static int[] NUMBER     = {0,1,2,3,4,5,6,7,8,9};

    /**类型0：字母  1：数字*/
    protected final static int[] TYPE       ={0,1};

    /**长度0：纯字母  1：纯数字 2：字母数字混合*/
    protected final static int[] NAME_TYPE  ={0,1,2};

    /**
     * 功能：产生min-max中的n个不重复的随机数 [min,max) 左闭右开
     *
     * min:产生随机数的其实位置
     * mab：产生随机数的最大位置
     * n: 所要产生多少个随机数
     *
     */
    public static Integer[] randomNumber(int min, int max, int n){

        //判断是否已经达到索要输出随机数的个数
        if(n>(max-min+1) || max <min){
            return null;
        }

        Integer[] result = new Integer[n]; //用于存放结果的数组

        int count = 0;
        while(count <n){
            int num = (int)(Math.random()*(max-min))+min;
            boolean flag = true;
            for(int j=0;j<count;j++){
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }


    /**
     * 动态获取机器人名称
     *
     * @param numberSize 名字长度
     * @return
     */
    public static String getRandomUserName(int numberSize){

        StringBuilder sb=new StringBuilder();
        int nameType=NAME_TYPE[randomNumber(0,3,1)[0]];
        for(int i=0;i<numberSize;i++){
            switch (nameType){
                case 0:
                    sb.append(CHARACTER[randomNumber(0, 26, 1)[0]]);
                    break;
                case 1:
                    sb.append(NUMBER[randomNumber(0, 10, 1)[0]]);
                    break;
                case 2:
                    int type=TYPE[randomNumber(0,2,1)[0]];
                    switch (type){
                        case 0:
                            sb.append(CHARACTER[randomNumber(0, 26, 1)[0]]);
                            break;
                        case 1:
                            sb.append(NUMBER[randomNumber(0, 10, 1)[0]]);
                            break;
                    }
                    break;
            }
        }
        return sb.toString();
    }


    /**
     * Generate 19-bit non-repetitive UID
     *
     * @return
     */
    public static Long createSequenceUid(){

        Integer[] randomNumber=DataUtil.randomNumber(0,10,4);
        return Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)+""+(System.currentTimeMillis()+"")+(randomNumber[0].longValue()+"")+(randomNumber[1].longValue()+""));
    }

    /**
     * Generate 9-bit non-repetitive UID
     *
     * @return
     */
    public static Long createNineSequenceUid(){

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String datestr = format.format(Calendar.getInstance().getTime());
        datestr=datestr.substring(0,datestr.length()-4);
        Integer[] randomNumber=DataUtil.randomNumber(0,10,5);
        return Long.valueOf(datestr+randomNumber[0]+randomNumber[1]+randomNumber[2]+randomNumber[3]+randomNumber[4]);
    }

    /**
     *
     */
    public static <T> List<T> distinct(List<T> lst){
        List<T> resultList = lst.stream().collect(Collectors.collectingAndThen(Collectors
                .groupingBy(Function.identity(), Collectors.counting()), map->{
            map.values().removeIf(size -> size == 1);
            List<T> tempList = new ArrayList(map.keySet());
            return tempList;
        }));
        return resultList;
    }

}
