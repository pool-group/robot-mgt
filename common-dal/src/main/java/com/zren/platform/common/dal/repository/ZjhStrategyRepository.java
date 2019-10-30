package com.zren.platform.common.dal.repository;

import com.zren.platform.common.dal.po.ZjhStrategyPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * The Behavior Strategy of Blossoming Golden Flower
 *
 * @author k.y
 * @version Id: ZjhStrategyRepository.java, v 0.1 2019年08月01日 下午14:16 k.y Exp $
 */
@Transactional
@Repository
public interface ZjhStrategyRepository extends JpaRepository<ZjhStrategyPO, Integer> {}
