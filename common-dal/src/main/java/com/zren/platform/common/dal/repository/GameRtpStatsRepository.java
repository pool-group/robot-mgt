package com.zren.platform.common.dal.repository;

import com.zren.platform.common.dal.po.GameRtpStatsPO;
import com.zren.platform.common.util.tool.DateUtil;
import com.zren.platform.common.util.tool.LogUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * RTP
 *
 * @author k.y
 * @version Id: GameTableStatsRepository.java, v 0.1 2019年02月26日 下午11:10 k.y Exp $
 */
@Repository
public class GameRtpStatsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public GameRtpStatsPO queryRtpResult(String brand, Integer gameId,Integer roomId){

        String dateStr=DateUtil.getNowDateFormatToString("yyyyMMdd");
        dateStr=dateStr.substring(2,dateStr.length());
        Integer dateInt=Integer.valueOf(dateStr);
        String sql="SELECT * FROM game_rtp_stats gt WHERE gt.brand='"+brand+"' AND gt.game_id="+gameId+" AND gt.room_id="+roomId+" AND gt.create_date="+dateInt+" ";
        Query query = entityManager.createNativeQuery(sql,GameRtpStatsPO.class);
        List<GameRtpStatsPO> poList=query.getResultList();
        LogUtil.info(String.format("查询房间总投入信息, poList=[ %s ]",poList));
        if(null==poList||poList.size()==0){
            GameRtpStatsPO po=new GameRtpStatsPO();
            po.setTotalInput(BigDecimal.valueOf(0));
            po.setTotalPnl(BigDecimal.valueOf(0));
            return po;
        }else {
            return poList.get(0);
        }
    }
}
