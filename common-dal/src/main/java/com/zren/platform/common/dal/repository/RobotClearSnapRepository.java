package com.zren.platform.common.dal.repository;

import com.zren.platform.common.dal.po.RobotClearSnapInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 机器人清理 Repository
 *
 * @author k.y
 * @version Id: RobotManageRepository.java, v 0.1 2018年11月06日 下午16:13 k.y Exp $
 */
@Transactional
@Repository
public interface RobotClearSnapRepository extends JpaRepository<RobotClearSnapInfoPO, Integer> {


}
