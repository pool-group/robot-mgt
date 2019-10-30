package com.zren.platform.core.rule.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 炸金花策略工厂
 *
 * @author k.y
 * @version Id: ZJHStrategyFactory.java, v 0.1 2018年12月17日 下午14:56 k.y Exp $
 */
@Component
public class ZJHStrategyFactory implements InitializingBean {

    /**
     * 行为策略基础数据容器,储存四类数据
     *
     *     LOSS_MIN: 状态为输,并且牌最小的机器人策略
     *     LOSS_MIDDLE: 状态为输,并且牌不是最大,也不是最小的机器人策略
     *     WIN: 状态为赢的策略
     *     RANDOM: 状态为随机的策略
     *
     */
    private final Map<Integer,List<ZjhStrategyInfoDTO>>         ACTION_MODEL     =      Maps.newConcurrentMap();

    /**加注策略容器*/
    private final Map<Integer,Map<Integer,Double>>       ADD_BET_MODEL     =      Maps.newConcurrentMap();

    private final static Integer WIN                                             =      1;

    private final static Integer LOSS_MIN                                        =     -1;

    private final static Integer LOSS_MIDDLE                                     =      0;

    private final static Integer RANDOM                                          =      2;

    @Override
    public void afterPropertiesSet() throws Exception {

        //初始化机器人以胜负和轮次为维度的行为策略
        initActionOriginal();

        //初始化机器人以投注分数、胜负作为维度的加注策略   ，如果看牌加注概率更大
        initAddBetOriginal();
    }


    public void initAddBetOriginal(){
        Map<Integer,Double> level_win=Maps.newConcurrentMap();
        level_win.put(0, 1.0);
        level_win.put(1, 0.5);
        level_win.put(2, 0.25);
        level_win.put(3, 0.1);
        level_win.put(4, 0.0);
        Map<Integer,Double> level_min=Maps.newConcurrentMap();
        level_min.put(0, 1.0);
        level_min.put(1, 0.05);
        level_min.put(2, 0.0);
        level_min.put(3, 0.0);
        level_min.put(4, 0.0);
        Map<Integer,Double> level_middle=Maps.newConcurrentMap();
        level_middle.put(0, 1.0);
        level_middle.put(1, 0.45);
        level_middle.put(2, 0.1);
        level_middle.put(3, 0.0);
        level_middle.put(4, 0.0);
        Map<Integer,Double> level_random=Maps.newConcurrentMap();
        level_random.put(0, 1.0);
        level_random.put(1, 0.45);
        level_random.put(2, 0.05);
        level_random.put(3, 0.0);
        level_random.put(4, 0.0);
        ADD_BET_MODEL.put(WIN, level_win);
        ADD_BET_MODEL.put(LOSS_MIN, level_min);
        ADD_BET_MODEL.put(LOSS_MIDDLE, level_middle);
        ADD_BET_MODEL.put(RANDOM, level_random);
    }


    public void initActionOriginal(){
        ACTION_MODEL.put(WIN, Lists.newArrayList());
        ACTION_MODEL.put(LOSS_MIN, Lists.newArrayList());
        ACTION_MODEL.put(LOSS_MIDDLE, Lists.newArrayList());
        ACTION_MODEL.put(RANDOM, Lists.newArrayList());

        //第一轮
        initStrategy(WIN,0,0,0,0,0,0,5,0.4,0.7,1.0);
        initStrategy(LOSS_MIN,0,0,0,0,0,0,2,0,0.6,1.0);
        initStrategy(LOSS_MIDDLE,0,0,0,0,0,0,3,0.35,0.6,1.0);
        initStrategy(RANDOM,0,0,0,0,0,0,3,0.35,0.7,1.0);

        //第二轮
        initStrategy(WIN,1,0.3,0,0,0,0.3,5,0.7,0.6,1.0);
        initStrategy(LOSS_MIN,1,0.5,0.4,0.5,0,0.3,2,0,0.6,1.0);
        initStrategy(LOSS_MIDDLE,1,0.3,0.1,0.05,0,0.3,3,0.4,0.6,1.0);
        initStrategy(RANDOM,1,0.3,0.08,0.1,0,0.3,3,0.7,0.6,1.0);

        //第三轮
        initStrategy(WIN,2,0.35,0,0,0,0.5,5,0.8,0.4,1.0);
        initStrategy(LOSS_MIN,2,0.65,0.6,0.5,0,0.5,3,0,0.4,1.0);
        initStrategy(LOSS_MIDDLE,2,0.3,0.1,0.1,0,0.3,3,0.4,0.4,1.0);
        initStrategy(RANDOM,2,0.3,0.08,0.1,0,0.35,3,0.65,0.4,1.0);

        //第四轮
        initStrategy(WIN,3,0.4,0,0,0,0.5,5,0.5,0.4,1.0);
        initStrategy(LOSS_MIN,3,0.8,0.85,0.85,0,1,3,0,0.4,1.0);
        initStrategy(LOSS_MIDDLE,3,0.4,0,0.1,0,0.35,3,0.4,0.4,1.0);
        initStrategy(RANDOM,3,0.3,0,0.08,0,0.35,3,0.4,0.4,1.0);
    }

    public void initStrategy(Integer type, Integer index, double lookRate,double lookToGiveUpRate, double giveUpRate, double showRate, double vsRate, Integer countVs,double addBetRate,double quickThinkingRate, double followRate){
        ZjhStrategyInfoDTO strategy=new ZjhStrategyInfoDTO();
        strategy.setIndex(index);
        strategy.setLookRate(lookRate);
        strategy.setGiveUpRate(giveUpRate);
        strategy.setShowRate(showRate);
        strategy.setVsRate(vsRate);
        strategy.setVsmin(countVs);
        strategy.setFollowRate(followRate);
        strategy.setAddBetRate(addBetRate);
        strategy.setQuickThinkingRate(quickThinkingRate);
        strategy.setLookToGiveUpRate(lookToGiveUpRate);
        strategy.setIsWin((byte) 2);
        ACTION_MODEL.get(type).add(strategy);
    }

    public ZjhStrategyInfoDTO initialize(Integer robotry,Integer index){
        if(null==ACTION_MODEL.get(WIN)
                ||null==ACTION_MODEL.get(LOSS_MIN)
                ||null==ACTION_MODEL.get(LOSS_MIDDLE)
                ||null==ACTION_MODEL.get(RANDOM)){
            initActionOriginal();
        }
        ZjhStrategyInfoDTO out=new ZjhStrategyInfoDTO();
        BeanUtils.copyProperties(ACTION_MODEL.get(robotry).get(index),out);
        if(index.compareTo(5)==1){
            out.setVsRate(out.getVsRate()+(index-5)*0.08);
        }
        return out;
    }

    public Double findAddBetStrategy(Integer robotry,Integer index){
        if(robotry>2)
            robotry=2;
        if(null==ADD_BET_MODEL.get(WIN)
                ||null==ADD_BET_MODEL.get(LOSS_MIN)
                ||null==ADD_BET_MODEL.get(LOSS_MIDDLE)
                ||null==ADD_BET_MODEL.get(RANDOM)){
            initAddBetOriginal();
        }
        return null==ADD_BET_MODEL.get(robotry).get(index)?ADD_BET_MODEL.get(robotry).get(4):ADD_BET_MODEL.get(robotry).get(index);
    }


}
