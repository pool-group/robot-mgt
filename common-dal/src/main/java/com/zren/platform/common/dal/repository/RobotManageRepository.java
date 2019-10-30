package com.zren.platform.common.dal.repository;

import com.zren.platform.common.dal.po.RobotInfoPO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 机器人管理 Repository
 *
 * @author k.y
 * @version Id: RobotManageRepository.java, v 0.1 2018年11月06日 下午16:13 k.y Exp $
 */
@Transactional
@Repository
public interface RobotManageRepository extends JpaRepository<RobotInfoPO, Integer> {

    @Query(value = "select * from robot_info r where r.status=0 limit :start, :count",nativeQuery = true)
    @Modifying(clearAutomatically = true)
    List<RobotInfoPO> findAllInfo(@Param("start")int start,@Param("count")int count);

    /**
     * 普通查询
     *
     * @param userId
     * @return
     */
    RobotInfoPO findByUserId(Long userId);

    /**
     * 查询僵尸机器人
     *
     * @return
     */
    List<RobotInfoPO> findByStatusAndDateTimeLessThan(String status,Date dateTime);


    /**
     * 冻结/解冻机器人
     *
     * @param status
     * @param userId
     * @return
     */
    @Modifying
    @Query(value = "update robot_info a set a.status = :status, a.date_time = :dateTime, a.batch_id= :batchId where a.user_id in :userId",nativeQuery = true)
    int freezeRobots(@Param("status") String status, @Param("dateTime") Date dateTime, @Param("batchId") Long batchId, @Param("userId")List<Long> userId);

    /**
     * 冻结/解冻机器人
     *
     * @param status
     * @param userId
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update robot_info a set a.status = :status, a.date_time = :dateTime, a.batch_id= :batchId where a.user_id in :userId and a.status= :ostatus and a.batch_id= :wbatchId",nativeQuery = true)
    int vfreezeRobots(@Param("status") String status, @Param("dateTime") Date dateTime, @Param("batchId") Long batchId, @Param("userId")List<Long> userId, @Param("ostatus") String ostatus, @Param("wbatchId") Long wbatchId);

}
