package com.zren.platform.core.rule.factory;

import com.google.common.collect.Maps;
import com.zren.platform.common.util.tuple.Tuple2x;
import com.zren.platform.common.util.tuple.Tuple3x;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.entity.in.PlayerStrategyInputEntity;
import com.zren.platform.core.rule.entity.out.PlayerStrategyEntity;
import com.zren.platform.core.rule.strategy.common.BaseIntegration;
import com.zren.platform.intercomm.dto.RobotIntegrationDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Player Strategy Factory
 *
 * @author k.y
 * @version Id: PlayerStrategyFactory.java, v 0.1 2019年09月11日 下午19:29 k.y Exp $
 */
@Component
public class PlayerStrategyFactory extends BaseIntegration {


    /**
     * Player's Current Profit and Loss Data Local Container
     * Priority to query profit and loss data from local containers
     * If the local data timeout occurs, the latest data is queried from redis
     * <p>
     * res: Relevant information is associated with Tuple3x components
     */
    private final Map<String, Tuple3x<Long,Integer,List<RobotIntegrationDto>>>          CURRENT_DATA_TOTAL                                 =      Maps.newHashMap();


    private final Map<String, Tuple2x>          HISTORY_DATA_UP10                                  =      Maps.newHashMap();

    /**
     * Using Optimistic Locks to Ensure Performance
     * Only one thread can update the policy container at a time.
     * <br/>
     * Preventing Cache Penetration
     */
    private AtomicBoolean                       BOOL                                               =      new AtomicBoolean(true);


    private void refreshPlayerOriginal(PlayerStrategyInputEntity inputEntity){
        if(BOOL.compareAndSet(true,false)) {
            ExecutorService exec=null;
            try {
                exec=Executors.newFixedThreadPool(1);
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        saveOrUpdateAsync(inputEntity);
                    }
                });
            } finally {
                exec.shutdown();
                BOOL.compareAndSet(false,true);
            }
        }else {}
    }

    public void saveOrUpdateAsync(PlayerStrategyInputEntity inputEntity){
        saveOrUpdate(inputEntity,CURRENT_DATA_TOTAL);
    }

    /**
     * Performance considerations, logical if judgment refinement
     * <p>
     * Request: <li> brand <li/><li> gameId <li/><li> roomId <li/>
     * Res: Relevant information is associated with Tuple3x components
     * @param inputEntity
     * @param playerStrategyEntity
     */
    public void initialize(PlayerStrategyInputEntity inputEntity,PlayerStrategyEntity playerStrategyEntity) {
        Tuple3x<Long,Integer,List<RobotIntegrationDto>> local_tuple3x=CURRENT_DATA_TOTAL.get(getCurrentDataTotalKey(inputEntity));
        Long currentTimeMillis=System.currentTimeMillis();
        if(null==local_tuple3x) {
            refreshPlayerOriginal(inputEntity);
        }else if(currentTimeMillis.compareTo((Long)local_tuple3x._1().get())==1) {
            Tuple3x<Long,Integer,List<RobotIntegrationDto>> exact_tuple3x=super.find(inputEntity);
            if(null==exact_tuple3x){
                refreshPlayerOriginal(inputEntity);
            }else if (currentTimeMillis.compareTo((Long)exact_tuple3x._1().get())==1) {
                refreshPlayerOriginal(inputEntity);
            }else {
                playerStrategyEntity.setValidBool(true);
                playerStrategyEntity.setPlayerSize((Integer) exact_tuple3x._2().get());
                playerStrategyEntity.setRobotIntegrationDtoList(exact_tuple3x._3().get());
            }
        }else {
            playerStrategyEntity.setValidBool(true);
            playerStrategyEntity.setPlayerSize((Integer) local_tuple3x._2().get());
            playerStrategyEntity.setRobotIntegrationDtoList(local_tuple3x._3().get());
        }
        super.refresh(inputEntity);
    }

    /**
     * Real-time monitoring of expiration of blacklist time
     * Monitoring to determine whether a user is included in the expiration of blacklist time
     * <p>
     * @param input
     * @return
     */
    public Boolean haveDennyEffective(AlgorithmRuleEntity input,PlayerStrategyEntity output){
        return super.haveDennyEffective(input,output);
    }
}