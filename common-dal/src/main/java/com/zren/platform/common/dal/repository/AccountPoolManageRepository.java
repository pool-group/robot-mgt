package com.zren.platform.common.dal.repository;

import com.zren.platform.common.dal.po.AccountPoolPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金池管理接口
 *
 * @author k.y
 * @version Id: AccountPoolManageRepository.java, v 0.1 2018年11月06日 下午16:13 k.y Exp $
 */

@Transactional
@Repository
public interface AccountPoolManageRepository extends JpaRepository<AccountPoolPO, Integer> {


    /**
     * 普通查询 [ 业主/游戏/房间 ]
     *
     * @param gameId
     * @return
     */
    AccountPoolPO findByGameIdAndBrandAndRoomId(Integer gameId,String brand,Integer roomId);

    /**
     * 普通查询 [ 业主 ]
     *
     * @param brand
     * @return
     */
    List<AccountPoolPO> findByBrand(String brand);

    /**
     * 普通查询 [ 业主/游戏 ]
     *
     * @param brand
     * @return
     */
    List<AccountPoolPO> findByBrandAndGameId(String brand,Integer gameId);

    /**
     * 冻结账户资金
     *
     * 目前暂时表里面只有一条记录，使用表级锁，后面若设计变更，在改成行级锁
     *
     * @param
     * @param
     */
    @Modifying
    @Query(value = "update account_pool a set a.freeze = (a.freeze+:freezeCapital) where a.game_id = :gameId and a.brand = :brand and a.room_id =  :roomId",nativeQuery = true)
    int updateFreeze(@Param("freezeCapital") BigDecimal freezeCapital, @Param("gameId")Integer gameId,@Param("brand")String brand,@Param("roomId")Integer roomId);

    /**
     * 变更盈亏
     *
     * 目前暂时表里面只有一条记录，使用表级锁，后面若设计变更，在改成行级锁
     *
     * @param
     * @param
     */
    @Modifying
    @Query(value = "update account_pool a set a.gain_loss=(a.gain_loss+:gainLoss) where a.game_id = :gameId and a.brand =  :brand and a.room_id =  :roomId",nativeQuery = true)
    int updateGainLoss(@Param("gainLoss") BigDecimal gainLoss, @Param("gameId")Integer gameId,@Param("brand")String brand,@Param("roomId")Integer roomId);

    /**
     * 解冻账户资金
     *
     * 目前暂时表里面只有一条记录，使用表级锁，后面若设计变更，在改成行级锁
     *
     * @param
     * @param
     */
    @Modifying
    @Query(value = "update account_pool a set a.freeze = (a.freeze-:freezeCapital) where a.game_id = :gameId and a.brand =  :brand and a.room_id =  :roomId",nativeQuery = true)
    int updateUnfreeze(@Param("freezeCapital") BigDecimal freezeCapital, @Param("gameId")Integer gameId,@Param("brand")String brand,@Param("roomId")Integer roomId);

    /**
     * 解冻账户资金，变更盈亏
     *
     * 目前暂时表里面只有一条记录，使用表级锁，后面若设计变更，在改成行级锁
     *
     * @param
     * @param
     */
    @Modifying
    @Query(value = "update account_pool a set a.freeze = (a.freeze-:freezeCapital),a.gain_loss=(a.gain_loss+:gainLoss) where a.game_id = :gameId and a.brand =  :brand and a.room_id =  :roomId",nativeQuery = true)
    int updateUnfreezeAndGainLoss(@Param("freezeCapital") BigDecimal freezeCapital,@Param("gainLoss") BigDecimal gainLoss, @Param("gameId")Integer gameId,@Param("brand")String brand,@Param("roomId")Integer roomId);
}
