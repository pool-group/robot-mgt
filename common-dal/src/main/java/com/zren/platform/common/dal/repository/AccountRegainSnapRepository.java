package com.zren.platform.common.dal.repository;

import com.zren.platform.common.dal.po.AccountRegainSnapInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 机器人异常账变恢复 Repository
 *
 * @author k.y
 * @version Id: RobotManageRepository.java, v 0.1 2018年11月06日 下午16:13 k.y Exp $
 */
@Transactional
@Repository
public interface AccountRegainSnapRepository extends JpaRepository<AccountRegainSnapInfoPO, Integer> {

    /**
     * 查询未消费的异常账变信息
     *
     * @retur     */
    List<AccountRegainSnapInfoPO> findByStatus(Byte status);

    /**
     * 查询判断是否重复消费
     *
     * @param batchId
     * @param userId
     * @param status
     * @return
     */
    List<AccountRegainSnapInfoPO> findByBatchIdAndUserIdAndStatus(Long batchId,Long userId,Byte status);


    /**
     * 设置数据消费状态
     *
     * @param status
     * @param userId
     * @return
     */
    @Modifying
    @Query(value = "update account_regain_snap a set a.status = :status, a.create_time = :createTime where a.batch_id= :batchId and a.user_id in :userId",nativeQuery = true)
    int freezeAccountRegain(@Param("status") String status, @Param("createTime") Date createTime, @Param("batchId") Long batchId, @Param("userId")List<Long> userId);
}
