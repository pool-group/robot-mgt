package com.zren.platform.common.dal.repository;

import com.zren.platform.common.dal.po.AccountWaterPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资金池管理接口
 *
 * @author k.y
 * @version Id: AccountPoolManageRepository.java, v 0.1 2018年11月06日 下午16:13 k.y Exp $
 */

@Transactional
@Repository
public interface AccountWaterManageRepository extends JpaRepository<AccountWaterPO, Long> {

    /**
     * 查询当前批次所有账目流水
     *
     * @param batchId  批次ID
     * @return
     */
    List<AccountWaterPO> findByBatchIdAndUserId(Long batchId,Long userId);
}
